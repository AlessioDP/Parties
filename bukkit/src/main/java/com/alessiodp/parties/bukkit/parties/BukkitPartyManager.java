package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

import java.util.UUID;

public class BukkitPartyManager extends PartyManager {
	public BukkitPartyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public PartyImpl initializeParty(UUID id) {
		return new BukkitPartyImpl(plugin, id);
	}
}
