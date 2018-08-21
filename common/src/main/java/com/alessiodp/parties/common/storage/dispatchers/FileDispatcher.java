package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.libraries.ILibrary;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.logging.LogLine;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.HomeLocationImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.DatabaseData;
import com.alessiodp.parties.common.storage.StorageType;
import com.alessiodp.parties.common.storage.file.FileUpgradeManager;
import com.alessiodp.parties.common.storage.file.YAMLDao;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseFile;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

public class FileDispatcher implements IDatabaseDispatcher {
	private PartiesPlugin plugin;
	
	private IDatabaseFile database;
	
	public FileDispatcher(PartiesPlugin instance) {
		plugin = instance;
	}
	
	@Override
	public void init(StorageType type) {
		// Configurate is necessary to handle every type of file database, loading core
		if (plugin.getLibraryManager().initLibrary(ILibrary.CONFIGURATE_CORE)) {
			switch (type) {
				case YAML:
					database = new YAMLDao(plugin);
					database.initFile();
					break;
				case MYSQL:
				case SQLITE:
				case NONE:
					// Pass
			}
		}
		
		if (database != null && !database.isFailed()) {
			FileUpgradeManager.checkUpgrades(database);
		}
	}
	
	@Override
	public void stop() {
		// Nothing to do
	}
	
	public boolean isFailed() {
		return database.isFailed();
	}
	
	@Override
	public DatabaseData loadEntireData() {
		// Players
		Map<UUID, PartyPlayerImpl> players = new HashMap<>();
		for (PartyPlayerImpl pp : getAllPlayers())
			players.put(pp.getPlayerUUID(), pp);
		
		// Parties
		Map<String, PartyImpl> parties = new HashMap<>();
		for (PartyImpl p : getAllParties())
			parties.put(p.getName(), p);
		
		return new DatabaseData(players, parties);
	}
	
	@Override
	public boolean saveEntireData(DatabaseData data) {
		boolean ret = false;
		try {
			// Players
			for (Entry<UUID, PartyPlayerImpl> entry : data.getPlayers().entrySet())
				updatePlayer(entry.getValue());
			
			// Parties
			for (Entry<String, PartyImpl> entry : data.getParties().entrySet())
				updateParty(entry.getValue());
			ret = true;
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
		return ret;
	}
	
	@Override
	public boolean prepareNewOutput() {
		boolean ret = false;
		if (database != null)
			ret = database.prepareNewOutput();
		return ret;
	}
	
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		ConfigurationNode node = database.getRootNode().getNode("players", player.getPlayerUUID().toString());
		
		boolean existData = false;
		if (!player.getPartyName().isEmpty() || player.isSpy() || player.isPreventNotify())
			existData = true;
		
		if (!existData) {
			// Remove old player data
			node.setValue(null);
		} else {
			if (player.getPartyName().isEmpty()) {
				// Avoid useless space
				node.getNode("party").setValue(null);
				node.getNode("rank").setValue(null);
			} else {
				node.getNode("party").setValue(player.getPartyName());
				node.getNode("rank").setValue(player.getRank());
			}
			node.getNode("name", "name").setValue(player.getName());
			node.getNode("name", "timestamp").setValue(System.currentTimeMillis() / 1000L);
			node.getNode("options", "spy").setValue(player.isSpy() ? true : null);
			node.getNode("options", "notify").setValue(player.isPreventNotify() ? true : null);
		}
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		ConfigurationNode node = database.getRootNode().getNode("players", uuid.toString());
		return getPlayerFromNode(node);
	}
	
	@Override
	public List<PartyPlayerImpl> getPartyPlayersByName(String name) {
		List<PartyPlayerImpl> ret = new ArrayList<>();
		for (ConfigurationNode confNode : database.getRootNode().getNode("players").getChildrenMap().values()) {
			if (confNode.getNode("name", "name").getString("").equalsIgnoreCase(name)) {
				ret.add(getPlayerFromNode(confNode));
			}
		}
		return ret;
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
		
		if (plugin.getPartyManager().isBukkit_killSystem() && party.getKills() > 0)
			node.getNode("kills").setValue(party.getKills());
		
		if (ConfigParties.PASSWORD_ENABLE)
			node.getNode("password").setValue(!party.getPassword().isEmpty() ? party.getPassword() : null);
		
		node.getNode("pvp").setValue(party.isFriendlyFireProtected());
		
		if (plugin.getPartyManager().isBukkit_expSystem() && party.getExperience() > 0)
			node.getNode("experience").setValue(party.getExperience());
		
		node.getNode("home").setValue(party.getHome() != null ? party.getHome().toString() : null);
		
		node.getNode("leader").setValue(party.isFixed() ? Constants.FIXED_VALUE_TEXT : party.getLeader().toString());
		
		List<String> lt = new ArrayList<>();
		for (UUID uuid : party.getMembers())
			lt.add(uuid.toString());
		node.getNode("members").setValue(lt);
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
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
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		// Only remove the party, players are handled by Party.removeParty()
		database.getRootNode().getNode("parties", party.getName()).setValue(null);
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	
	@Override
	public boolean existParty(String party) {
		return database.getRootNode().getNode("parties", party).getValue() != null;
	}
	
	@Override
	public List<PartyImpl> getAllFixed() {
		List<PartyImpl> ret = new ArrayList<>();
		for (ConfigurationNode confNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			if (confNode.getNode("leader").getString("").equals(Constants.FIXED_VALUE_UUID)) {
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
	
	
	@Override
	public void insertLog(LogLine line) {
		try {
			Path filePath = plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_FILE_TXT_LOGNAME);
			if (!Files.exists(filePath))
				Files.createFile(filePath);
			
			Files.write(filePath, line.getFormattedMessage().getBytes(), StandardOpenOption.APPEND);
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	
	/*
	 * Private
	 */
	private PartyPlayerImpl getPlayerFromNode(ConfigurationNode node) {
		PartyPlayerImpl ret = null;
		if (node != null && node.getValue() != null) {
			ret = plugin.getPlayerManager().initializePlayer(UUID.fromString(node.getKey().toString()));
			ret.setName(node.getNode("name", "name").getString(""));
			ret.setNameTimestamp(node.getNode("name", "timestamp").getLong());
			ret.setRank(node.getNode("rank").getInt());
			ret.setPartyName(node.getNode("party").getString(""));
			ret.setSpy(node.getNode("options", "spy").getBoolean(false));
			ret.setPreventNotify(node.getNode("options", "notify").getBoolean(false));
		}
		return ret;
	}
	private PartyImpl getPartyFromNode(ConfigurationNode node) {
		PartyImpl ret = null;
		if (node != null && node.getValue() != null) {
			ret = plugin.getPartyManager().initializeParty(node.getKey().toString());
			
			ret.setDescription(node.getNode("desc").getString(""));
			ret.setMotd(node.getNode("motd").getString(""));
			ret.setColor(
					plugin.getColorManager().searchColorByName(
							node.getNode("color").getString("")
					)
			);
			ret.setKills(node.getNode("kills").getInt(0));
			ret.setPassword(node.getNode("password").getString(""));
			ret.setHome(HomeLocationImpl.deserialize(node.getNode("home").getString("")));
			ret.setFriendlyFireProtected(node.getNode("pvp").getBoolean(false));
			ret.setExperience(node.getNode("experience").getDouble(0));
			
			// Leader check
			String leader = node.getNode("leader").getString("");
			try {
				if (!leader.isEmpty()) {
					if (leader.equalsIgnoreCase(Constants.FIXED_VALUE_TEXT)) {
						ret.setLeader(UUID.fromString(Constants.FIXED_VALUE_UUID));
						ret.setFixed(true);
					} else
						ret.setLeader(UUID.fromString(leader));
				}
			} catch (Exception ex) {
				LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
			}
			
			// Members check
			List<UUID> list = new ArrayList<>();
			Function<Object, String> f = o -> (String) o;
			for (String id : node.getNode("members").getList(f)) {
				try {
					list.add(UUID.fromString(id));
				} catch (Exception ex) {
					LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
				}
			}
			ret.setMembers(list);
		}
		return ret;
	}
}
