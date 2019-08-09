package com.alessiodp.parties.bungeecord.commands.main;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandTeleport;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.main.CommandParty;

public class BungeeCommandParty extends CommandParty {
	
	public BungeeCommandParty(PartiesPlugin instance) {
		super(instance);
		
		// Teleport
		if (BungeeConfigParties.TELEPORT_ENABLE)
			super.register(CommonCommands.TELEPORT, new BungeeCommandTeleport(plugin, this));
	}
}
