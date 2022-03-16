package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.bukkit.addons.external.bstats.bukkit.Metrics;
import com.alessiodp.core.bukkit.addons.external.bstats.charts.SimplePie;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitMetricsHandler extends MetricsHandler {
	public BukkitMetricsHandler(@NotNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void registerMetrics() {
		Metrics metrics = new Metrics((JavaPlugin) plugin.getBootstrap(), plugin.getBstatsId());
		
		metrics.addCustomChart(new SimplePie("type_of_party_used", () -> {
			if (BukkitConfigParties.ADDITIONAL_FIXED_ENABLE)
				return "Fixed";
			return "Normal";
		}));
		
		// Config
		metrics.addCustomChart(new SimplePie("type_of_database_used", () -> plugin.getDatabaseManager().getDatabaseType().getFormattedName()));
		
		metrics.addCustomChart(new SimplePie("auto_command_system", () -> {
			if (BukkitConfigMain.ADDITIONAL_AUTOCMD_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("experience_system", () -> {
			if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("follow_system", () -> {
			if (BukkitConfigMain.ADDITIONAL_FOLLOW_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("skript_support", () -> {
			if (BukkitConfigMain.PARTIES_HOOK_INTO_SKRIPT)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("vault_system", () -> {
			if (BukkitConfigMain.ADDONS_VAULT_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		// Parties
		metrics.addCustomChart(new SimplePie("color_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_COLOR_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("friendly_fire_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("home_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_HOME_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("kills_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_KILLS_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("nickname_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_NICKNAME_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("tag_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_TAG_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("teleport_system", () -> {
			if (BukkitConfigParties.ADDITIONAL_TELEPORT_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		// Extra
		metrics.addCustomChart(new SimplePie("using_api", () -> {
			if (Parties.isFlagHook())
				return "Yes";
			return "No";
		}));
	}
}