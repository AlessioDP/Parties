package com.alessiodp.parties.velocity.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;

public class VelocityEconomyManager extends EconomyManager {
	
	public VelocityEconomyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean payCommand(PaidCommand paidCommand, PartyPlayerImpl partyPlayerEntity, String commandLabel, String[] args) {
		return false;
	}
}
