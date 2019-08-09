package com.alessiodp.parties.common.storage.interfaces;

import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.List;
import java.util.UUID;

public interface IPartiesDatabaseDispatcher extends IDatabaseDispatcher {
	void updatePlayer(PartyPlayerImpl player);
	PartyPlayerImpl getPlayer(UUID uuid);
	
	void updateParty(PartyImpl party);
	PartyImpl getParty(String party);
	void renameParty(String prev, String next);
	void removeParty(PartyImpl party);
	boolean existParty(String party);
	List<PartyImpl> getAllFixed();
	List<PartyImpl> getAllParties();
	List<PartyPlayerImpl> getAllPlayers();
}
