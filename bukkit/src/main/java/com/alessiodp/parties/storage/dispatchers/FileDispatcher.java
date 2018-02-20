package com.alessiodp.parties.storage.dispatchers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLine;
import com.alessiodp.parties.storage.DatabaseData;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.storage.file.YAMLDao;
import com.alessiodp.parties.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.storage.interfaces.IDatabaseFile;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class FileDispatcher implements IDatabaseDispatcher {
	private Parties plugin;
	
	private IDatabaseFile database;
	
	public FileDispatcher(Parties instance) {
		plugin = instance;
	}
	
	@Override
	public void init() {}
	public void init(StorageType type) {
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
	
	@Override
	public void stop() {
		if (database != null)
			database.stopFile();
	}
	
	@Override
	public DatabaseData loadEntireData() {
		// Players
		Map<UUID, PartyPlayer> players = new HashMap<UUID, PartyPlayer>();
		for (PartyPlayer pp : getAllPlayers())
			players.put(pp.getPlayerUUID(), pp);
		
		// Parties
		Map<String, Party> parties = new HashMap<String, Party>();
		for (Party p : getAllParties())
			parties.put(p.getName(), p);
		
		return new DatabaseData(players, parties);
	}
	
	@Override
	public boolean saveEntireData(DatabaseData data) {
		boolean ret = false;
		try {
			// Players
			for (Entry<UUID, PartyPlayer> entry : data.getPlayers().entrySet())
				updatePlayer(entry.getValue());
			
			// Parties
			for (Entry<String, Party> entry : data.getParties().entrySet())
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
	public void updatePlayer(PartyPlayer player) {
		String node = "players." + player.getPlayerUUID().toString();
		boolean existData = false;
		if (!player.getPartyName().isEmpty() || player.isSpy() || player.isPreventNotify())
			existData = true;
		
		if (!existData) {
			// Remove old player data
			database.saveData(node, null);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			if (player.getPartyName().isEmpty()) {
				// Avoid useless space
				map.put(node + ".party", null);
				map.put(node + ".rank", null);
				map.put(node + ".name", null);
			} else {
				map.put(node + ".party", player.getPartyName());
				map.put(node + ".rank", player.getRank());
				map.put(node + ".name.name", player.getName());
				map.put(node + ".name.timestamp", System.currentTimeMillis() / 1000L);
			}
			map.put(node + ".options.spy", player.isSpy() ? true : null);
			map.put(node + ".options.notify", player.isPreventNotify() ? true : null);
			
			database.saveData(map);
		}
	}
	
	@Override
	public PartyPlayer getPlayer(UUID uuid) {
		PartyPlayer ret = null;
		if (database.existData("players." + uuid.toString())) {
			ret = database.loadPartyPlayerData(uuid);
		}
		return ret;
	}
	@Override
	public List<PartyPlayer> getPartyPlayersByName(String name) {
		List<PartyPlayer> ret = new ArrayList<PartyPlayer>();
		for (PartyPlayer pp : getAllPlayers()) {
			if (pp.getName().equalsIgnoreCase(name))
				ret.add(pp);
		}
		return ret;
	}
	
	
	@Override
	public void updateParty(Party party) {
		String node = "parties." + party.getName();
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (ConfigParties.DESC_ENABLE)
			map.put(node + ".desc", !party.getDescription().isEmpty() ? party.getDescription() : null);
		
		if (ConfigParties.MOTD_ENABLE)
			map.put(node + ".motd", !party.getMotd().isEmpty() ? party.getMotd() : null);
		
		if (ConfigMain.ADDITIONAL_TAG_ENABLE)
			map.put(node + ".prefix", !party.getPrefix().isEmpty() ? party.getPrefix() : null);
		
		if (ConfigMain.ADDITIONAL_TAG_ENABLE)
			map.put(node + ".suffix", !party.getSuffix().isEmpty() ? party.getSuffix() : null);
		
		if (ConfigParties.COLOR_ENABLE)
			map.put(node + ".color", party.getColor() != null ? party.getColor().getName() : null);
		
		if (ConfigParties.KILLS_ENABLE && party.getKills() > 0)
			map.put(node + ".kills", party.getKills());
		
		if (ConfigParties.PASSWORD_ENABLE)
			map.put(node + ".password", !party.getPassword().isEmpty() ? party.getPassword() : null);
		
		map.put(node + ".home", party.getHome() != null ? PartiesUtils.formatHome(party.getHome()) : null);
		
		map.put(node + ".leader", party.isFixed() ? Constants.FIXED_VALUE_TEXT : party.getLeader().toString());
		
		List<String> lt = new ArrayList<String>();
		for (UUID uuid : party.getMembers())
			lt.add(uuid.toString());
		map.put(node + ".members", lt);
		
		
		database.saveData(map);
	}
	
	@Override
	public Party getParty(String party) {
		Party ret = null;
		String node = "parties." + party;
		if (database.existData(node)) {
			ret = database.loadPartyData(party);
		}
		return ret;
	}
	
	@Override
	public void renameParty(String before, String now) {
		database.renameParty(before, now);
	}
	
	@Override
	public void removeParty(Party party) {
		database.saveData("parties." + party.getName(), null);
	}
	
	@Override
	public boolean existParty(String party) {
		return database.existData("parties." + party);
	}
	
	@Override
	public List<Party> getAllFixed() {
		List<Party> ret = new ArrayList<Party>();
		for (Party p : getAllParties()) {
			if (p.isFixed())
				ret.add(p);
		}
		return ret;
	}
	
	@Override
	public List<Party> getAllParties() {
		return database.getAllParties();
	}
	
	@Override
	public List<PartyPlayer> getAllPlayers() {
		return database.getAllPlayers();
	}
	
	
	@Override
	public void insertLog(LogLine line) {
		try {
			File file = new File(plugin.getDataFolder(), ConfigMain.STORAGE_SETTINGS_FILE_TXT_LOGNAME);
			if (!file.exists())
				file.createNewFile();
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(line.getFormattedMessage());
			out.close();
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
}
