package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.libraries.ILibrary;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.FileDispatcher;
import com.alessiodp.core.common.storage.file.YAMLDao;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.HomeLocationImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.file.PartiesFileUpgradeManager;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabaseDispatcher;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class PartiesFileDispatcher extends FileDispatcher implements IPartiesDatabaseDispatcher {
	
	public PartiesFileDispatcher(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void init(StorageType type) {
		// Configurate is necessary to handle every type of file database, loading core
		if (plugin.getLibraryManager().initLibrary(ILibrary.CONFIGURATE_CORE)) {
			switch (type) {
				case YAML:
					database = new YAMLDao(
							plugin,
							ConfigMain.STORAGE_SETTINGS_YAML_DBFILE,
							PartiesConstants.VERSION_DATABASE_YAML);
					database.initFile();
					break;
				default:
					// Unsupported storage type
			}
		}
		
		if (database != null && !database.isFailed()) {
			upgradeManager = new PartiesFileUpgradeManager(plugin, database, type);
			upgradeManager.checkForUpgrades();
		}
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		ConfigurationNode node = database.getRootNode().getNode("players", player.getPlayerUUID().toString());
		
		boolean existData = false;
		if (!player.getPartyName().isEmpty() || player.isSpy() || player.isMuted())
			existData = true;
		
		if (existData) {
			// Save the data
			if (player.getPartyName().isEmpty()) {
				// Out of party (free space)
				node.getNode("party").setValue(null);
				node.getNode("rank").setValue(null);
			} else {
				// In party
				node.getNode("party").setValue(player.getPartyName());
				node.getNode("rank").setValue(player.getRank());
			}
			node.getNode("options", "spy").setValue(player.isSpy() ? true : null);
			node.getNode("options", "mute").setValue(player.isMuted() ? true : null);
		} else {
			// Remove the data
			node.setValue(null);
		}
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
		}
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		ConfigurationNode node = database.getRootNode().getNode("players", uuid.toString());
		return getPlayerFromNode(node);
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		ConfigurationNode node = database.getRootNode().getNode("parties", party.getName());
		
		if (ConfigParties.DESC_ENABLE)
			node.getNode("desc").setValue(!party.getDescription().isEmpty() ? party.getDescription() : null);
		
		if (ConfigParties.MOTD_ENABLE)
			node.getNode("motd").setValue(!party.getMotd().isEmpty() ? party.getMotd() : null);
		
		if (ConfigParties.COLOR_ENABLE)
			node.getNode("color").setValue(party.getColor() != null ? party.getColor().getName() : null);
		
		if (((PartiesPlugin) plugin).getPartyManager().isBukkit_killSystem() && party.getKills() > 0)
			node.getNode("kills").setValue(party.getKills());
		
		if (ConfigParties.PASSWORD_ENABLE)
			node.getNode("password").setValue(!party.getPassword().isEmpty() ? party.getPassword() : null);
		
		node.getNode("protection").setValue(party.getProtection());
		
		if (((PartiesPlugin) plugin).getPartyManager().isBukkit_expSystem() && party.getExperience() > 0)
			node.getNode("experience").setValue(party.getExperience());
		
		if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE && ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD) {
			node.getNode("follow").setValue(party.isFollowEnabled() ? null : false); // By default is true, so insert it only if false
		}
		
		node.getNode("home").setValue(party.getHome() != null ? party.getHome().toString() : null);
		
		node.getNode("leader").setValue(party.isFixed() ? PartiesConstants.FIXED_VALUE_TEXT : party.getLeader().toString());
		
		List<String> lt = new ArrayList<>();
		for (UUID uuid : party.getMembers())
			lt.add(uuid.toString());
		node.getNode("members").setValue(lt);
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
		}
	}
	
	@Override
	public PartyImpl getParty(String party) {
		ConfigurationNode node = database.getRootNode().getNode("parties", party);
		return getPartyFromNode(node);
	}
	
	@Override
	public void renameParty(String before, String now) {
		ConfigurationNode oldNode = database.getRootNode().getNode("parties", before);
		ConfigurationNode newNode = database.getRootNode().getNode("parties", now);
		newNode.setValue(oldNode.getValue());
		oldNode.setValue(null);
		
		Function<Object, String> f = o -> (String) o;
		for (String id : newNode.getNode("members").getList(f)) {
			database.getRootNode().getNode("players", id, "party").setValue(now);
		}
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
		}
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		// Only remove the party, players are handled by Party.removeParty()
		database.getRootNode().getNode("parties", party.getName()).setValue(null);
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
		}
	}
	
	@Override
	public boolean existParty(String party) {
		boolean ret = false;
		for (ConfigurationNode partyNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			if (partyNode.getKey().toString().equalsIgnoreCase(party)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	@Override
	public List<PartyImpl> getAllFixed() {
		List<PartyImpl> ret = new ArrayList<>();
		for (ConfigurationNode confNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			if (confNode.getNode("leader").getString("").equals(PartiesConstants.FIXED_VALUE_UUID)) {
				ret.add(getPartyFromNode(confNode));
			}
		}
		return ret;
	}
	
	@Override
	public List<PartyImpl> getAllParties() {
		List<PartyImpl> ret = new ArrayList<>();
		for (ConfigurationNode confNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			ret.add(getPartyFromNode(confNode));
		}
		return ret;
	}
	
	@Override
	public List<PartyPlayerImpl> getAllPlayers() {
		List<PartyPlayerImpl> ret = new ArrayList<>();
		for (ConfigurationNode confNode : database.getRootNode().getNode("players").getChildrenMap().values()) {
			ret.add(getPlayerFromNode(confNode));
		}
		return ret;
	}
	
	private PartyPlayerImpl getPlayerFromNode(ConfigurationNode node) {
		PartyPlayerImpl ret = null;
		if (node != null && node.getValue() != null) {
			ret = ((PartiesPlugin) plugin).getPlayerManager().initializePlayer(UUID.fromString(node.getKey().toString()));
			ret.fromDatabase(
					node.getNode("party").getString(""),
					node.getNode("rank").getInt(),
					node.getNode("options", "spy").getBoolean(false),
					node.getNode("options", "mute").getBoolean(false)
			);
		}
		return ret;
	}
	private PartyImpl getPartyFromNode(ConfigurationNode node) {
		PartyImpl ret = null;
		if (node != null && node.getKey() != null && node.getValue() != null) {
			ret = ((PartiesPlugin) plugin).getPartyManager().initializeParty(node.getKey().toString());
			
			// Leader check
			UUID leader = null;
			boolean fixed = false;
			String leaderStr = node.getNode("leader").getString("");
			try {
				if (!leaderStr.isEmpty()) {
					if (leaderStr.equalsIgnoreCase(PartiesConstants.FIXED_VALUE_TEXT)) {
						leader = UUID.fromString(PartiesConstants.FIXED_VALUE_UUID);
						fixed = true;
					} else
						leader = UUID.fromString(leaderStr);
				}
			} catch (Exception ex) {
				plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
			}
			
			// Members check
			List<UUID> members = new ArrayList<>();
			Function<Object, String> f = o -> (String) o;
			for (String id : node.getNode("members").getList(f)) {
				try {
					members.add(UUID.fromString(id));
				} catch (Exception ex) {
					plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
				}
			}
			
			ret.fromDatabase(
					leader,
					members,
					fixed,
					node.getNode("desc").getString(""),
					node.getNode("motd").getString(""),
					((PartiesPlugin) plugin).getColorManager().searchColorByName(node.getNode("color").getString("")),
					node.getNode("kills").getInt(0),
					node.getNode("password").getString(""),
					HomeLocationImpl.deserialize(node.getNode("home").getString("")),
					node.getNode("protection").getBoolean(false),
					node.getNode("experience").getDouble(0),
					node.getNode("follow").getBoolean(true)
			);
		}
		return ret;
	}
}
