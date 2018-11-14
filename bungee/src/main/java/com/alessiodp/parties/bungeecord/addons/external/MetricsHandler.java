package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import org.bstats.bungeecord.Metrics;

public class MetricsHandler {
	private BungeePartiesPlugin plugin;
	
	public MetricsHandler(BungeePartiesPlugin instance) {
		plugin = instance;
		registerMetrics();
	}
	
	private void registerMetrics() {
		Metrics metrics = new Metrics(plugin.getBootstrap());
		metrics.addCustomChart(new Metrics.SimplePie("type_of_party_used", () -> {
			if (ConfigParties.FIXED_ENABLE)
				return "Fixed";
			return "Normal";
		}));
		metrics.addCustomChart(new Metrics.SimplePie("type_of_database_used", () -> {
			if (plugin.getDatabaseManager().getDatabaseType().isNone())
				return "None";
			else if (plugin.getDatabaseManager().getDatabaseType().isMySQL())
				return "MySQL";
			else if (plugin.getDatabaseManager().getDatabaseType().isSQLite())
				return "SQLite";
			return "YAML";
		}));
		metrics.addCustomChart(new Metrics.SimplePie("using_api", () -> {
			if (Parties.isFlagHook())
				return "Yes";
			return "No";
		}));
	}
}