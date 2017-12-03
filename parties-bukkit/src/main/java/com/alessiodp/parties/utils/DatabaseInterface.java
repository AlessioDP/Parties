package com.alessiodp.parties.utils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public interface DatabaseInterface {
	
	public void init();
	public void stop();
	public boolean isFailed();
	
	
	public void updateSpies(List<UUID> list);
	public List<UUID> getSpies();
	
	
	public void updatePlayer(ThePlayer tp);
	public ThePlayer getPlayer(UUID uuid);
	public void removePlayer(UUID uuid);
	public String getPlayerPartyName(UUID uuid);
	public int getRank(UUID uuid);
	public HashMap<UUID, Object[]> getPlayersRank(String party);
	public HashMap<UUID, Long> getPlayersFromName(String name);
	public String getOldPlayerName(UUID uuid);
	
	
	public void updateParty(Party party);
	public Party getParty(String party);
	public void renameParty(String prev, String next);
	public void removeParty(Party party);
	public boolean existParty(String party);
	public List<Party> getAllParties();
	public List<String> getAllFixed();
	
	
	public void insertLog(LogLine line);
}
