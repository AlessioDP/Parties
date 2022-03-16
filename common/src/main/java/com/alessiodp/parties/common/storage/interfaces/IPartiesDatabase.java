package com.alessiodp.parties.common.storage.interfaces;

import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public interface IPartiesDatabase {
	void updatePlayer(PartyPlayerImpl player);
	PartyPlayerImpl getPlayer(UUID uuid);
	
	void updateParty(PartyImpl party);
	void removeParty(PartyImpl party);
	
	PartyImpl getParty(UUID id);
	PartyImpl getPartyByName(String name);
	boolean existsParty(String name);
	boolean existsTag(String tag);
	
	LinkedHashSet<PartyImpl> getListParties(PartiesDatabaseManager.ListOrder order, int limit, int offset);
	int getListPartiesNumber();
	Set<PartyImpl> getListFixed();
}
