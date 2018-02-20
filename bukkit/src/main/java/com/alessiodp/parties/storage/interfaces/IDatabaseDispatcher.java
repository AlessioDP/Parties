package com.alessiodp.parties.storage.interfaces;

import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.logging.LogLine;
import com.alessiodp.parties.storage.DatabaseData;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public interface IDatabaseDispatcher {
	
	public void init();
	public void stop();
	
	public DatabaseData loadEntireData();
	public boolean saveEntireData(DatabaseData data);
	public boolean prepareNewOutput();
	
	
	public void updatePlayer(PartyPlayer player);
	public PartyPlayer getPlayer(UUID uuid);
	public List<PartyPlayer> getPartyPlayersByName(String name);
	
	
	public void updateParty(Party party);
	public Party getParty(String party);
	public void renameParty(String prev, String next);
	public void removeParty(Party party);
	public boolean existParty(String party);
	public List<Party> getAllFixed();
	public List<Party> getAllParties();
	public List<PartyPlayer> getAllPlayers();
	
	
	public void insertLog(LogLine line);
}
