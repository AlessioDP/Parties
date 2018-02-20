package com.alessiodp.parties.storage.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public interface IDatabaseFile {
	
	public void initFile();
	public void stopFile();
	
	public File createDataFile();
	public boolean prepareNewOutput();
	
	public boolean existData(String path);
	public void saveData(Map<String, Object> map);
	public void saveData(String path, Object obj);
	public Object loadData(String path);
	public PartyPlayer loadPartyPlayerData(UUID uuid);
	public Party loadPartyData(String party);
	
	public void renameParty(String before, String now);
	public List<Party> getAllParties();
	public List<PartyPlayer> getAllPlayers();
}
