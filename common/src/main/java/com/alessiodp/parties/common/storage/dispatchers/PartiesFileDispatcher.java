package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.FileDispatcher;
import com.alessiodp.core.common.storage.file.FileUpgradeManager;
import com.alessiodp.core.common.storage.file.YAMLDao;
import com.alessiodp.core.common.storage.interfaces.IDatabaseFile;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.storage.file.PartiesFileUpgradeManager;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;

public class PartiesFileDispatcher extends FileDispatcher implements IPartiesDatabase {
	
	public PartiesFileDispatcher(ADPPlugin plugin, StorageType storageType) {
		super(plugin, storageType);
	}
	
	@Override
	protected IDatabaseFile initDao() {
		IDatabaseFile ret = null;
		switch (storageType) {
			case YAML:
				ret = new YAMLDao(
						plugin,
						ConfigMain.STORAGE_SETTINGS_YAML_DBFILE,
						PartiesConstants.VERSION_DATABASE_YAML);
				break;
			default:
				// Unsupported storage type
		}
		return ret;
	}
	
	@Override
	protected FileUpgradeManager initUpgradeManager() {
		return new PartiesFileUpgradeManager(plugin, database, storageType);
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		ConfigurationNode node = database.getRootNode().getNode("players", player.getPlayerUUID().toString());
		
		if (player.isPersistent()) {
			if (player.getPartyId() != null) {
				node.getNode("party").setValue(player.getPartyId().toString());
				node.getNode("rank").setValue(player.getRank());
			} else {
				node.getNode("party").setValue(null);
				node.getNode("rank").setValue(null);
			}
			
			node.getNode("options", "chat").setValue(player.isChatParty() ? true : null);
			node.getNode("options", "spy").setValue(player.isSpy() ? true : null);
			node.getNode("options", "mute").setValue(player.isMuted() ? true : null);
		} else {
			node.setValue(null);
		}
		
		save();
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		ConfigurationNode node = database.getRootNode().getNode("players", uuid.toString());
		return getPlayerFromNode(node);
	}
	
	@Override
	public int getListPlayersNumber() {
		return database.getRootNode().getNode("players").getChildrenList().size();
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		ConfigurationNode node = database.getRootNode().getNode("parties", party.getId().toString());
		
		// Save in map name -> id
		ConfigurationNode mapNameToId = database.getRootNode().getNode("map-parties-by-name");
		if (!node.getNode("name").isEmpty() && !node.getNode("name").getValue("").equals(CommonUtils.toLowerCase(party.getName())))
			mapNameToId.removeChild(CommonUtils.toLowerCase(node.getNode("name").getString(""))); // Remove old name
		mapNameToId.getNode(CommonUtils.toLowerCase(party.getName())).setValue(party.getId().toString());
		
		// Save party
		node.getNode("name").setValue(party.getName());
		node.getNode("tag").setValue(CommonUtils.getNoEmptyOr(party.getTag(), null));
		node.getNode("description").setValue(CommonUtils.getNoEmptyOr(party.getDescription(), null));
		node.getNode("motd").setValue(CommonUtils.getNoEmptyOr(party.getMotd(), null));
		node.getNode("color").setValue(party.getColor() != null ? party.getColor().getName() : null);
		node.getNode("kills").setValue(party.getKills() > 0 ? party.getKills() : null);
		node.getNode("password").setValue(CommonUtils.getNoEmptyOr(party.getTag(), null));
		node.getNode("protection").setValue(party.getProtection() ? true : null);
		node.getNode("experience").setValue(party.getExperience() > 0 ? party.getExperience() : null);
		node.getNode("follow").setValue(party.isFollowEnabled() ? null : false); // By default is true, so insert it only if false
		node.getNode("home").setValue(party.getHomes().size() > 0 ? PartyHomeImpl.serializeMultiple(party.getHomes()) : null);
		node.getNode("leader").setValue(party.getLeader() != null ? party.getLeader().toString() : null);
		
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
	public PartyImpl getParty(UUID id) {
		ConfigurationNode node = database.getRootNode().getNode("parties", id.toString());
		return getPartyFromNode(node);
	}
	
	@Override
	public PartyImpl getPartyByName(String name) {
		String partyId = database.getRootNode().getNode("map-parties-by-name", CommonUtils.toLowerCase(name)).getString();
		return partyId != null ? getParty(UUID.fromString(partyId)) : null;
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		// Only remove the party, players are handled by Party.removeParty()
		database.getRootNode().getNode("parties", party.getId().toString()).setValue(null);
		database.getRootNode().getNode("map-parties-by-name").removeChild(CommonUtils.toLowerCase(party.getName()));
		
		try {
			database.saveFile();
		} catch (IOException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
		}
	}
	
	@Override
	public boolean existsParty(String name) {
		return !database.getRootNode().getNode("map-parties-by-name", CommonUtils.toLowerCase(name)).isEmpty();
	}
	
	@Override
	public boolean existsTag(String tag) {
		String lowerTag = CommonUtils.toLowerCase(tag);
		for (ConfigurationNode confNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			if (CommonUtils.toLowerCase(confNode.getNode("tag").getString("")).equals(lowerTag)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public LinkedHashSet<PartyImpl> getListParties(PartiesDatabaseManager.ListOrder order, int limit, int offset) {
		ConfigurationNode configurationNode = database.getRootNode().getNode("parties");
		LinkedHashSet<PartyImpl> ret = new LinkedHashSet<>();
		
		List<String> lowerCaseBlacklist = new ArrayList<>();
		for (String b : ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES)
			lowerCaseBlacklist.add(CommonUtils.toLowerCase(b));
		
		TreeSet<? extends Pair<String, ?>> entries;
		if (order == PartiesDatabaseManager.ListOrder.NAME)
			entries = listByName(configurationNode, lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.MEMBERS)
			entries = listByMembers(configurationNode, lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.KILLS)
			entries = listByKills(configurationNode, lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.EXPERIENCE)
			entries = listByExperience(configurationNode, lowerCaseBlacklist);
		else
			throw new IllegalStateException("Cannot get the list of parties with the order" + order.name());
		
		// Limit and offset
		Iterator<? extends Pair<String, ?>> iterator = entries.iterator();
		int n = 0;
		for (int c = 0; iterator.hasNext() && n < limit; c++) {
			String uuid = iterator.next().getKey();
			if (c >= offset) {
				ret.add(getParty(UUID.fromString(uuid)));
				n++;
			}
		}
		return ret;
	}
	
	private TreeSet<Pair<String, String>> listByName(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, String>> ret = new TreeSet<>((p1, p2) -> p1.getValue().compareTo(p2.getValue()));
		for (ConfigurationNode confNode : node.getChildrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.getNode("name").getString(""))))
				ret.add(new Pair<>((String) confNode.getKey(), confNode.getNode("name").getString()));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Integer>> listByMembers(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		Function<Object, String> f = o -> (String) o;
		TreeSet<Pair<String, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (ConfigurationNode confNode : node.getChildrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.getNode("name").getString(""))))
				ret.add(new Pair<>((String) confNode.getKey(), confNode.getNode("members").getList(f).size()));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Integer>> listByKills(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (ConfigurationNode confNode : node.getChildrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.getNode("name").getString(""))))
				ret.add(new Pair<>((String) confNode.getKey(), confNode.getNode("kills").getInt()));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Double>> listByExperience(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Double>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (ConfigurationNode confNode : node.getChildrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.getNode("name").getString(""))))
				ret.add(new Pair<>((String) confNode.getKey(), confNode.getNode("experience").getDouble()));
		}
		return ret;
	}
	
	@Override
	public int getListPartiesNumber() {
		int ret = 0;
		List<String> lowerCaseBlacklist = new ArrayList<>();
		for (String b : ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES)
			lowerCaseBlacklist.add(CommonUtils.toLowerCase(b));
		
		for (ConfigurationNode confNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.getNode("name").getString(""))))
				ret++;
		}
		return ret;
	}
	
	@Override
	public Set<PartyImpl> getListFixed() {
		Set<PartyImpl> ret = new HashSet<>();
		for (ConfigurationNode confNode : database.getRootNode().getNode("parties").getChildrenMap().values()) {
			if (confNode.getNode("leader").getValue() == null) {
				ret.add(getPartyFromNode(confNode));
			}
		}
		return ret;
	}
	
	private PartyPlayerImpl getPlayerFromNode(ConfigurationNode node) {
		PartyPlayerImpl ret = null;
		if (node != null && node.getKey() != null && node.getValue() != null) {
			ret = ((PartiesPlugin) plugin).getPlayerManager().initializePlayer(UUID.fromString(node.getKey().toString()));
			ret.setAccessible(true);
			if (!node.getNode("party").isEmpty()) {
				ret.setPartyId(UUID.fromString(node.getNode("party").getString("")));
				ret.setRank(node.getNode("rank").getInt());
			}
			ret.setSpy(node.getNode("options", "spy").getBoolean(false));
			ret.setMuted(node.getNode("options", "mute").getBoolean(false));
			ret.setAccessible(false);
		}
		return ret;
	}
	
	private PartyImpl getPartyFromNode(ConfigurationNode node) {
		PartyImpl ret = null;
		if (node != null && node.getKey() != null && node.getValue() != null) {
			ret = ((PartiesPlugin) plugin).getPartyManager().initializeParty(UUID.fromString(node.getKey().toString()));
			
			ret.setAccessible(true);
			ret.setup(node.getNode("name").getString(), node.getNode("leader").getString());
			ret.setDescription(node.getNode("tag").getString());
			ret.setDescription(node.getNode("description").getString());
			ret.setMotd(node.getNode("motd").getString());
			ret.setColor(((PartiesPlugin) plugin).getColorManager().searchColorByName(node.getNode("color").getString()));
			ret.setKills(node.getNode("kills").getInt(0));
			ret.setPassword(node.getNode("password").getString());
			ret.getHomes().addAll(PartyHomeImpl.deserializeMultiple(node.getNode("home").getString()));
			ret.setProtection(node.getNode("protection").getBoolean(false));
			ret.setExperience(node.getNode("experience").getDouble(0));
			ret.setFollowEnabled(node.getNode("follow").getBoolean(true));
			ret.setAccessible(false);
			
			// Members check
			Function<Object, String> f = o -> (String) o;
			for (String id : node.getNode("members").getList(f)) {
				try {
					ret.getMembers().add(UUID.fromString(id));
				} catch (Exception ex) {
					plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
				}
			}
		}
		return ret;
	}
}