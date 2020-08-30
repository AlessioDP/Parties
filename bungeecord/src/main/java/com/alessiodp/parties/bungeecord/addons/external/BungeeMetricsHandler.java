package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.core.bungeecord.addons.external.bstats.bungeecord.Metrics;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMetricsHandler extends MetricsHandler {
	public BungeeMetricsHandler(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void registerMetrics() {
		Metrics metrics = new Metrics((Plugin) plugin.getBootstrap(), plugin.getBstatsId());
		
		metrics.addCustomChart(new Metrics.SimplePie("type_of_party_used", () -> {
			if (ConfigParties.ADDITIONAL_FIXED_ENABLE)
				return "Fixed";
			return "Normal";
		}));
		metrics.addCustomChart(new Metrics.SimplePie("type_of_database_used", () -> {
			if (plugin.getDatabaseManager().getDatabaseType() == StorageType.NONE)
				return "None";
			else if (plugin.getDatabaseManager().getDatabaseType() == StorageType.MYSQL)
				return "MySQL";
			else if (plugin.getDatabaseManager().getDatabaseType() == StorageType.SQLITE)
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