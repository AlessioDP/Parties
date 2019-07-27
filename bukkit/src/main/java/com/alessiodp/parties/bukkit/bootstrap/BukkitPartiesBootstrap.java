package com.alessiodp.parties.bukkit.bootstrap;

import com.alessiodp.core.bukkit.bootstrap.ADPBukkitBootstrap;
import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;

public class BukkitPartiesBootstrap extends ADPBukkitBootstrap {
	public BukkitPartiesBootstrap() {
		plugin = new BukkitPartiesPlugin(this);
	}
}
