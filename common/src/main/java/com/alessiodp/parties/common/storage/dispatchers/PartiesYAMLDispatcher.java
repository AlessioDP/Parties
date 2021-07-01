package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.dispatchers.YAMLDispatcher;
import com.alessiodp.core.common.storage.file.YAMLDao;
import com.alessiodp.core.common.storage.file.YAMLUpgradeManager;
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
import com.alessiodp.parties.common.storage.file.PartiesYAMLUpgradeManager;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class PartiesYAMLDispatcher extends YAMLDispatcher implements IPartiesDatabase {
	
	public PartiesYAMLDispatcher(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected YAMLDao initDao() {
		return new YAMLDao(
				plugin,
				ConfigMain.STORAGE_SETTINGS_YAML_DBFILE,
				PartiesConstants.VERSION_DATABASE_YAML
		);
	}
	
	@Override
	protected YAMLUpgradeManager initUpgradeManager() {
		return new PartiesYAMLUpgradeManager(plugin, database);
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		if (player.isPersistent()) {
			ConfigurationSection node = database.getYaml().getConfigurationSection("players." + player.getPlayerUUID().toString());
			if (node == null)
				node = database.getYaml().createSection("players." + player.getPlayerUUID().toString());
			if (player.getPartyId() != null) {
				node.set("party", player.getPartyId().toString());
				node.set("rank", player.getRank());
				node.set("nickname", player.getNickname());
			} else {
				node.set("party", null);
				node.set("rank", null);
				node.set("nickname", null);
			}
			
			if (player.isChatParty() || player.isSpy() || player.isMuted()) {
				node.set("options.chat", player.isChatParty() ? true : null);
				node.set("options.spy", player.isSpy() ? true : null);
				node.set("options.mute", player.isMuted() ? true : null);
			} else {
				node.set("options", null);
			}
		} else {
			database.getYaml().set("players." + player.getPlayerUUID().toString(), null);
		}
		
		save();
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		ConfigurationSection node = database.getYaml().getConfigurationSection("players." + uuid.toString());
		return getPlayerFromNode(node);
	}
	
	@Override
	public int getListPlayersInPartyNumber() {
		int ret = 0;
		ConfigurationSection players = database.getYaml().getConfigurationSection("players");
		Set<String> keys = players.getKeys(false);
		for (String key : keys) {
			if (players.get(key + ".party") != null)
				ret++;
		}
		return ret;
		//return (int) database.getRootNode().node("players").childrenList().stream().filter(p -> !p.node("party").empty()).count();
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		ConfigurationSection node = database.getYaml().getConfigurationSection("parties." + party.getId().toString());
		if (node == null)
			node = database.getYaml().createSection("parties." + party.getId().toString());
		
		// Save in map name -> id
		if (party.getName() != null) {
			ConfigurationSection mapNameToId = database.getYaml().getConfigurationSection("map-parties-by-name");
			if (mapNameToId == null)
				mapNameToId = database.getYaml().createSection("map-parties-by-name");
			if (node.get("name") != null && !node.getString("name", "").equals(CommonUtils.toLowerCase(party.getName())))
				mapNameToId.set(CommonUtils.toLowerCase(node.getString("name", "")), null); // Remove old name
			mapNameToId.set(CommonUtils.toLowerCase(party.getName()), party.getId().toString());
		}
		
		// Save party
		node.set("name", party.getName());
		node.set("tag", CommonUtils.getNoEmptyOr(party.getTag(), null));
		node.set("description", CommonUtils.getNoEmptyOr(party.getDescription(), null));
		node.set("motd", CommonUtils.getNoEmptyOr(party.getMotd(), null));
		node.set("color", party.getColor() != null ? party.getColor().getName() : null);
		node.set("kills", party.getKills() > 0 ? party.getKills() : null);
		node.set("password", CommonUtils.getNoEmptyOr(party.getTag(), null));
		node.set("protection", party.getProtection() ? true : null);
		node.set("experience", party.getExperience() > 0 ? party.getExperience() : null);
		node.set("follow", party.isFollowEnabled() ? null : false); // By default is true, so insert it only if false
		node.set("home", party.getHomes().size() > 0 ? PartyHomeImpl.serializeMultiple(party.getHomes()) : null);
		node.set("leader", party.getLeader() != null ? party.getLeader().toString() : null);
		
		List<String> lt = new ArrayList<>();
		for (UUID uuid : party.getMembers())
			lt.add(uuid.toString());
		node.set("members", lt);
		
		save();
	}
	
	@Override
	public PartyImpl getParty(UUID id) {
		ConfigurationSection node = database.getYaml().getConfigurationSection("parties." + id.toString());
		return getPartyFromNode(node);
	}
	
	@Override
	public PartyImpl getPartyByName(String name) {
		String partyId = database.getYaml().getString("map-parties-by-name." + CommonUtils.toLowerCase(name), null);
		return partyId != null ? getParty(UUID.fromString(partyId)) : null;
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		// Only remove the party, players are handled by Party.removeParty()
		database.getYaml().set("parties." + party.getId().toString(), null);
		if (party.getName() != null)
			database.getYaml().set("map-parties-by-name." + CommonUtils.toLowerCase(party.getName()), null);
		
		save();
	}
	
	@Override
	public boolean existsParty(String name) {
		return database.getYaml().get("map-parties-by-name." + CommonUtils.toLowerCase(name)) != null;
	}
	
	@Override
	public boolean existsTag(String tag) {
		String lowerTag = CommonUtils.toLowerCase(tag);
		ConfigurationSection node = database.getYaml().getConfigurationSection("parties");
		for (String key : node.getKeys(false)) {
			if (node.getString(key + ".tag", null) != null && tag.equalsIgnoreCase(node.getString(key + ".tag"))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public LinkedHashSet<PartyImpl> getListParties(PartiesDatabaseManager.ListOrder order, int limit, int offset) {
		ConfigurationSection node = database.getYaml().getConfigurationSection("parties");
		LinkedHashSet<PartyImpl> ret = new LinkedHashSet<>();
		
		List<String> lowerCaseBlacklist = new ArrayList<>();
		for (String b : ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES)
			lowerCaseBlacklist.add(CommonUtils.toLowerCase(b));
		
		TreeSet<? extends Pair<String, ?>> entries;
		if (order == PartiesDatabaseManager.ListOrder.NAME)
			entries = listByName(node, lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.MEMBERS)
			entries = listByMembers(node, lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.KILLS)
			entries = listByKills(node, lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.EXPERIENCE)
			entries = listByExperience(node, lowerCaseBlacklist);
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
	
	private TreeSet<Pair<String, String>> listByName(ConfigurationSection node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, String>> ret = new TreeSet<>((p1, p2) -> p1.getValue().compareTo(p2.getValue()));
		for (String key : node.getKeys(false)) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(node.getString(key + ".name", ""))))
				ret.add(new Pair<>(key, node.getString(key + ".name")));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Integer>> listByMembers(ConfigurationSection node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (String key : node.getKeys(false)) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(node.getString(key + ".name", "")))) {
				List<String> members = node.getStringList(key + ".members");
				if (members != null)
					ret.add(new Pair<>(key, members.size()));
			}
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Integer>> listByKills(ConfigurationSection node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (String key : node.getKeys(false)) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(node.getString(key + ".name", ""))))
				ret.add(new Pair<>(key, node.getInt(key + ".kills")));
		}
		return ret;
	}
	
	private TreeSet<Pair<String, Double>> listByExperience(ConfigurationSection node, List<String> lowerCaseBlacklist) {
		TreeSet<Pair<String, Double>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (String key : node.getKeys(false)) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(node.getString(key + ".name", ""))))
				ret.add(new Pair<>(key, node.getDouble(key + ".experience")));
		}
		return ret;
	}
	
	@Override
	public int getListPartiesNumber() {
		int ret = 0;
		List<String> lowerCaseBlacklist = new ArrayList<>();
		for (String b : ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES)
			lowerCaseBlacklist.add(CommonUtils.toLowerCase(b));
		
		ConfigurationSection node = database.getYaml().getConfigurationSection("parties");
		for (String key : node.getKeys(false)) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(node.getString(key + ".name"))))
				ret++;
		}
		return ret;
	}
	
	@Override
	public Set<PartyImpl> getListFixed() {
		Set<PartyImpl> ret = new HashSet<>();
		ConfigurationSection node = database.getYaml().getConfigurationSection("parties");
		for (String key : node.getKeys(false)) {
			if (node.get(key + ".leader") == null) {
				ret.add(getPartyFromNode(node.getConfigurationSection(key)));
			}
		}
		return ret;
	}
	
	private PartyPlayerImpl getPlayerFromNode(ConfigurationSection node) {
		PartyPlayerImpl ret = null;
		if (node != null) {
			ret = ((PartiesPlugin) plugin).getPlayerManager().initializePlayer(UUID.fromString(node.getName()));
			ret.setAccessible(true);
			if (node.getString("party") != null) {
				ret.setPartyId(UUID.fromString(node.getString("party", "")));
				ret.setRank(node.getInt("rank"));
			}
			ret.setSpy(node.getBoolean("options.spy", false));
			ret.setMuted(node.getBoolean("options.mute", false));
			ret.setAccessible(false);
		}
		return ret;
	}
	
	private PartyImpl getPartyFromNode(ConfigurationSection node) {
		PartyImpl ret = null;
		if (node != null) {
			ret = ((PartiesPlugin) plugin).getPartyManager().initializeParty(UUID.fromString(node.getName()));
			
			ret.setAccessible(true);
			ret.setup(node.getString("name"), node.getString("leader"));
			ret.setDescription(node.getString("tag"));
			ret.setDescription(node.getString("description"));
			ret.setMotd(node.getString("motd"));
			ret.setColor(((PartiesPlugin) plugin).getColorManager().searchColorByName(node.getString("color")));
			ret.setKills(node.getInt("kills"));
			ret.setPassword(node.getString("password"));
			ret.getHomes().addAll(PartyHomeImpl.deserializeMultiple(node.getString("home")));
			ret.setProtection(node.getBoolean("protection", false));
			ret.setExperience(node.getDouble("experience"));
			ret.setFollowEnabled(node.getBoolean("follow", true));
			ret.setAccessible(false);
			
			// Members check
			if (node.getStringList("members") != null) {
				for (String id : node.getStringList("members")) {
					try {
						ret.getMembers().add(UUID.fromString(id));
					} catch (Exception ex) {
						plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_DB_FILE_ERROR, ex);
					}
				}
			}
		}
		return ret;
	}
}