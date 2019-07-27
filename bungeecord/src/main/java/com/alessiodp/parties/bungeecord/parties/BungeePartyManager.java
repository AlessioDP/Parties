package com.alessiodp.parties.bungeecord.parties;

import com.alessiodp.parties.bungeecord.parties.objects.BungeePartyImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

public class BungeePartyManager extends PartyManager {
	public BungeePartyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public PartyImpl initializeParty(String partyName) {
		return new BungeePartyImpl(plugin, partyName);
	}
}
