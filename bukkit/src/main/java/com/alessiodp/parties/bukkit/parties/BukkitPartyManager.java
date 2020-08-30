package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
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
	public void reload() {
		super.reload();
		this.bukkit_killSystem = BukkitConfigParties.ADDITIONAL_KILLS_ENABLE;
		this.bukkit_expSystem = (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE);
	}
	
	@Override
	public PartyImpl initializeParty(UUID id) {
		return new BukkitPartyImpl(plugin, id);
	}
}
