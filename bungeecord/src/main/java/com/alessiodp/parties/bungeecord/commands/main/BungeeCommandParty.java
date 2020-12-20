package com.alessiodp.parties.bungeecord.commands.main;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandHome;
import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandSetHome;
import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandTeleport;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.main.CommandParty;
import com.alessiodp.parties.common.commands.sub.CommandDebug;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

public class BungeeCommandParty extends CommandParty {
	
	public BungeeCommandParty(PartiesPlugin instance) {
		super(instance);
		
		// Debug
		if (ConfigMain.PARTIES_DEBUG_COMMAND)
			super.register(new CommandDebug(plugin, this));
		
		// Home
		if (BungeeConfigParties.ADDITIONAL_HOME_ENABLE) {
			super.register(new BungeeCommandHome(plugin, this));
			super.register(new BungeeCommandSetHome(plugin, this));
		}
		
		// Teleport
		if (BungeeConfigParties.ADDITIONAL_TELEPORT_ENABLE)
			super.register(new BungeeCommandTeleport(plugin, this));
	}
}
