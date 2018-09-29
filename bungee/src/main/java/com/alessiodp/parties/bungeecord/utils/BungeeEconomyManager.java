package com.alessiodp.parties.bungeecord.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;

public class BungeeEconomyManager extends EconomyManager {
	
	public BungeeEconomyManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean payCommand(PaidCommand paidCommand, PartyPlayerImpl partyPlayerEntity, String commandLabel, String[] args) {
		return false;
	}
}
