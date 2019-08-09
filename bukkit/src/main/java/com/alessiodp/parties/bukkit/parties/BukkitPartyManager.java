package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

public class BukkitPartyManager extends PartyManager {
	public BukkitPartyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void reload() {
		super.reload();
		this.bukkit_killSystem = BukkitConfigParties.KILLS_ENABLE;
		this.bukkit_expSystem = (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE);
	}
	
	@Override
	public PartyImpl initializeParty(String partyName) {
		return new BukkitPartyImpl(plugin, partyName);
	}
}
