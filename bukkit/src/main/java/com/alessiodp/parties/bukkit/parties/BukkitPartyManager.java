package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

public class BukkitPartyManager extends PartyManager {
	public BukkitPartyManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public PartyImpl initializeParty(String partyName) {
		return new BukkitPartyImpl(plugin, partyName);
	}
}
