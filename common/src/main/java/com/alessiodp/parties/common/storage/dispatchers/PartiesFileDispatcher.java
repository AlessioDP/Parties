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
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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
		ConfigurationNode node = database.getRootNode().node("players", player.getPlayerUUID().toString());
		
		try {
			
			if (player.isPersistent()) {
				if (player.getPartyId() != null) {
					node.node("party").set(player.getPartyId().toString());
					node.node("rank").set(player.getRank());
					node.node("nickname").set(player.getNickname());
				} else {
					node.node("party").set(null);
					node.node("rank").set(null);
					node.node("nickname").set(null);
				}
				
				node.node("options", "chat").set(player.isChatParty() ? true : null);
				node.node("options", "spy").set(player.isSpy() ? true : null);
				node.node("options", "mute").set(player.isMuted() ? true : null);
			} else {
				node.set(null);
			}
			
			save();
		} catch (SerializationException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		ConfigurationNode node = database.getRootNode().node("players", uuid.toString());
		return getPlayerFromNode(node);
	}
	
	@Override
	public int getListPlayersInPartyNumber() {
		return (int) database.getRootNode().node("players").childrenList().stream().filter(p -> !p.node("party").empty()).count();
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		try {
			ConfigurationNode node = database.getRootNode().node("parties", party.getId().toString());
			
			// Save in map name -> id
			if (party.getName() != null) {
				ConfigurationNode mapNameToId = database.getRootNode().node("map-parties-by-name");
				if (!node.node("name").empty() && !node.node("name").getString("").equals(CommonUtils.toLowerCase(party.getName())))
					mapNameToId.removeChild(CommonUtils.toLowerCase(node.node("name").getString(""))); // Remove old name
				mapNameToId.node(CommonUtils.toLowerCase(party.getName())).set(party.getId().toString());
			}
			
			// Save party
			node.node("name").set(party.getName());
			node.node("tag").set(CommonUtils.getNoEmptyOr(party.getTag(), null));
			node.node("description").set(CommonUtils.getNoEmptyOr(party.getDescription(), null));
			node.node("motd").set(CommonUtils.getNoEmptyOr(party.getMotd(), null));
			node.node("color").set(party.getColor() != null ? party.getColor().getName() : null);
			node.node("kills").set(party.getKills() > 0 ? party.getKills() : null);
			node.node("password").set(CommonUtils.getNoEmptyOr(party.getTag(), null));
			node.node("protection").set(party.getProtection() ? true : null);
			node.node("experience").set(party.getExperience() > 0 ? party.getExperience() : null);
			node.node("follow").set(party.isFollowEnabled() ? null : false); // By default is true, so insert it only if false
			node.node("home").set(party.getHomes().size() > 0 ? PartyHomeImpl.serializeMultiple(party.getHomes()) : null);
			node.node("leader").set(party.getLeader() != null ? party.getLeader().toString() : null);
			
			List<String> lt = new ArrayList<>();
			for (UUID uuid : party.getMembers())
				lt.add(uuid.toString());
			node.node("members").set(lt);
			
			save();
		} catch (SerializationException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public PartyImpl getParty(UUID id) {
		ConfigurationNode node = database.getRootNode().node("parties", id.toString());
		return getPartyFromNode(node);
	}
	
	@Override
	public PartyImpl getPartyByName(String name) {
		String partyId = database.getRootNode().node("map-parties-by-name", CommonUtils.toLowerCase(name)).getString();
		return partyId != null ? getParty(UUID.fromString(partyId)) : null;
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		try {
			// Only remove the party, players are handled by Party.removeParty()
			database.getRootNode().node("parties", party.getId().toString()).set(null);
			if (party.getName() != null)
				database.getRootNode().node("map-parties-by-name").removeChild(CommonUtils.toLowerCase(party.getName()));
			
			save();
		} catch (SerializationException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean existsParty(String name) {
		return !database.getRootNode().node("map-parties-by-name", CommonUtils.toLowerCase(name)).empty();
	}
	
	@Override
	public boolean existsTag(String tag) {
		String lowerTag = CommonUtils.toLowerCase(tag);
		for (ConfigurationNode confNode : database.getRootNode().node("parties").childrenMap().values()) {
			if (CommonUtils.toLowerCase(confNode.node("tag").getString("")).equals(lowerTag)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public LinkedHashSet<PartyImpl> getListParties(PartiesDatabaseManager.ListOrder order, int limit, int offset) {
		ConfigurationNode configurationNode = database.getRootNode().node("parties");
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
		for (ConfigurationNode confNode : node.childrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.node("name").getString(""))))
				ret.add(new Pair<>((String) confNode.key(), confNode.node("name").getString()));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Integer>> listByMembers(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (ConfigurationNode confNode : node.childrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.node("name").getString("")))) {
				try {
					List<String> members = confNode.node("members").getList(String.class);
					if (members != null)
						ret.add(new Pair<>((String) confNode.key(), members.size()));
				} catch (SerializationException ex) {
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Integer>> listByKills(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (ConfigurationNode confNode : node.childrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.node("name").getString(""))))
				ret.add(new Pair<>((String) confNode.key(), confNode.node("kills").getInt()));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Double>> listByExperience(ConfigurationNode node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Double>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (ConfigurationNode confNode : node.childrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.node("name").getString(""))))
				ret.add(new Pair<>((String) confNode.key(), confNode.node("experience").getDouble()));
		}
		return ret;
	}
	
	@Override
	public int getListPartiesNumber() {
		int ret = 0;
		List<String> lowerCaseBlacklist = new ArrayList<>();
		for (String b : ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES)
			lowerCaseBlacklist.add(CommonUtils.toLowerCase(b));
		
		for (ConfigurationNode confNode : database.getRootNode().node("parties").childrenMap().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(confNode.node("name").getString(""))))
				ret++;
		}
		return ret;
	}
	
	@Override
	public Set<PartyImpl> getListFixed() {
		Set<PartyImpl> ret = new HashSet<>();
		for (ConfigurationNode confNode : database.getRootNode().node("parties").childrenMap().values()) {
			if (confNode.node("leader").isNull()) {
				ret.add(getPartyFromNode(confNode));
			}
		}
		return ret;
	}
	
	private PartyPlayerImpl getPlayerFromNode(ConfigurationNode node) {
		PartyPlayerImpl ret = null;
		if (node != null && node.key() != null && !node.isNull()) {
			ret = ((PartiesPlugin) plugin).getPlayerManager().initializePlayer(UUID.fromString(node.key().toString()));
			ret.setAccessible(true);
			if (!node.node("party").empty()) {
				ret.setPartyId(UUID.fromString(node.node("party").getString("")));
				ret.setRank(node.node("rank").getInt());
			}
			ret.setSpy(node.node("options", "spy").getBoolean(false));
			ret.setMuted(node.node("options", "mute").getBoolean(false));
			ret.setAccessible(false);
		}
		return ret;
	}
	
	private PartyImpl getPartyFromNode(ConfigurationNode node) {
		PartyImpl ret = null;
		if (node != null && node.key() != null && !node.isNull()) {
			ret = ((PartiesPlugin) plugin).getPartyManager().initializeParty(UUID.fromString(node.key().toString()));
			
			ret.setAccessible(true);
			ret.setup(node.node("name").getString(), node.node("leader").getString());
			ret.setDescription(node.node("tag").getString());
			ret.setDescription(node.node("description").getString());
			ret.setMotd(node.node("motd").getString());
			ret.setColor(((PartiesPlugin) plugin).getColorManager().searchColorByName(node.node("color").getString()));
			ret.setKills(node.node("kills").getInt(0));
			ret.setPassword(node.node("password").getString());
			ret.getHomes().addAll(PartyHomeImpl.deserializeMultiple(node.node("home").getString()));
			ret.setProtection(node.node("protection").getBoolean(false));
			ret.setExperience(node.node("experience").getDouble(0));
			ret.setFollowEnabled(node.node("follow").getBoolean(true));
			ret.setAccessible(false);
			
			// Members check
			try {
				if (!node.node("members").isNull()) {
					for (String id : node.node("members").getList(String.class)) {
						try {
							ret.getMembers().add(UUID.fromString(id));
						} catch (Exception ex) {
							plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
						}
					}
				}
			} catch (SerializationException ex) {
				ex.printStackTrace();
			}
		}
		return ret;
	}
}