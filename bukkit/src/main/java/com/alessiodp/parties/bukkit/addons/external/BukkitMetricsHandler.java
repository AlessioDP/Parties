package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.bukkit.addons.external.bstats.bukkit.Metrics;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class BukkitMetricsHandler extends MetricsHandler {
	public BukkitMetricsHandler(@NonNull ADPPlugin plugin) {
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
		
		metrics.addCustomChart(new Metrics.SimplePie("exp_levels", () -> {
			if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
				switch (CommonUtils.toLowerCase(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_MODE)) {
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