package com.alessiodp.parties.bukkit.commands.list;

import com.alessiodp.core.common.commands.list.ADPCommand;

public enum BukkitCommands implements ADPCommand {
	CLAIM,
	CONFIRM,
	HOME,
	PROTECTION,
	SETHOME;
	
	@Override
	public String getOriginalName() {
		return this.name();
	}
}
