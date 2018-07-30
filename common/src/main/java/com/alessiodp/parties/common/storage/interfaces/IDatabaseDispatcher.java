package com.alessiodp.parties.common.storage.interfaces;

import com.alessiodp.parties.common.logging.LogLine;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.DatabaseData;
import com.alessiodp.parties.common.storage.StorageType;

import java.util.List;
import java.util.UUID;

public interface IDatabaseDispatcher {
	
	void init(StorageType type);
	void stop();
	
	DatabaseData loadEntireData();
	boolean saveEntireData(DatabaseData data);
	boolean prepareNewOutput();
	
	
	void updatePlayer(PartyPlayerImpl player);
	PartyPlayerImpl getPlayer(UUID uuid);
	List<PartyPlayerImpl> getPartyPlayersByName(String name);
	
	
	void updateParty(PartyImpl party);
	PartyImpl getParty(String party);
	void renameParty(String prev, String next);
	void removeParty(PartyImpl party);
	boolean existParty(String party);
	List<PartyImpl> getAllFixed();
	List<PartyImpl> getAllParties();
	List<PartyPlayerImpl> getAllPlayers();
	
	
	void insertLog(LogLine line);
}
