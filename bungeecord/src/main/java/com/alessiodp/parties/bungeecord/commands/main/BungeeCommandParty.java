package com.alessiodp.parties.bungeecord.commands.main;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandTeleport;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.main.CommandParty;

public class BungeeCommandParty extends CommandParty {
	
	public BungeeCommandParty(PartiesPlugin instance) {
		super(instance);
		
		// wip add debug command
		
		// Teleport
		if (BungeeConfigParties.ADDITIONAL_TELEPORT_ENABLE)
			super.register(new BungeeCommandTeleport(plugin, this));
	}
}
