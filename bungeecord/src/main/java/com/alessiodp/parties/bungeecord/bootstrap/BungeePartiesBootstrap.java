package com.alessiodp.parties.bungeecord.bootstrap;

import com.alessiodp.core.bungeecord.bootstrap.ADPBungeeBootstrap;
import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;

public class BungeePartiesBootstrap extends ADPBungeeBootstrap {
	public BungeePartiesBootstrap() {
		plugin = new BungeePartiesPlugin(this);
	}
}
