package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import org.bstats.bukkit.Metrics;


public class MetricsHandler {
	private BukkitPartiesPlugin plugin;
	
	public MetricsHandler(BukkitPartiesPlugin instance) {
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
		metrics.addCustomChart(new Metrics.SimplePie("exp_levels", () -> {
			if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
				switch (BukkitConfigMain.ADDITIONAL_EXP_LEVELS_MODE.toLowerCase()) {
					case "normal":
						return "Normal";
					case "skillapi":
						return "SkillAPI";
					default:
						return "Party";
					
				}
			}
			return "Disabled";
		}));
		metrics.addCustomChart(new Metrics.SimplePie("exp_drop", () -> {
			if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_DROP_ENABLE) {
				if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE)
					return "Enabled with MM";
				return "Enabled";
			}
			return "Disabled";
		}));
		metrics.addCustomChart(new Metrics.SimplePie("vault_system", () -> {
			if (BukkitConfigMain.ADDONS_VAULT_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		metrics.addCustomChart(new Metrics.SimplePie("tag_system", () -> "Disabled"));
		metrics.addCustomChart(new Metrics.SimplePie("using_api", () -> {
			if (Parties.isFlagHook())
				return "Yes";
			return "No";
		}));
	}
}