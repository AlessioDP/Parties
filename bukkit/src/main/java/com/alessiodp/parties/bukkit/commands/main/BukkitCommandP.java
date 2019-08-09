package com.alessiodp.parties.bukkit.commands.main;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.main.CommandP;

public class BukkitCommandP extends CommandP {
	public BukkitCommandP(PartiesPlugin instance) {
		super(instance);
		
		description = BukkitConfigMain.COMMANDS_DESC_P;
	}
}
