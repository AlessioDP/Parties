package com.alessiodp.parties.velocity.commands.main;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.main.CommandParty;
import com.alessiodp.parties.common.commands.sub.CommandDebug;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.velocity.commands.sub.VelocityCommandHome;
import com.alessiodp.parties.velocity.commands.sub.VelocityCommandSetHome;
import com.alessiodp.parties.velocity.commands.sub.VelocityCommandTeleport;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigParties;

public class VelocityCommandParty extends CommandParty {
	
	public VelocityCommandParty(PartiesPlugin instance) {
		super(instance);
		
		// Debug
		if (ConfigMain.PARTIES_DEBUG_COMMAND)
			super.register(new CommandDebug(plugin, this));
		
		// Home
		if (VelocityConfigParties.ADDITIONAL_HOME_ENABLE) {
			super.register(new VelocityCommandHome(plugin, this));
			super.register(new VelocityCommandSetHome(plugin, this));
		}
		
		// Teleport
		if (VelocityConfigParties.ADDITIONAL_TELEPORT_ENABLE)
			super.register(new VelocityCommandTeleport(plugin, this));
	}
}
