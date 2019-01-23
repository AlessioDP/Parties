package com.alessiodp.parties.bungeecord.commands.main;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandTeleport;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.main.CommandParty;
import com.alessiodp.parties.common.configuration.data.ConfigParties;

public class BungeeCommandParty extends CommandParty {
	
	public BungeeCommandParty(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void reload() {
		super.reload();
		
		// Teleport
		if (ConfigParties.TELEPORT_ENABLE)
			super.register(CommonCommands.TELEPORT, new BungeeCommandTeleport(plugin));
	}
}
