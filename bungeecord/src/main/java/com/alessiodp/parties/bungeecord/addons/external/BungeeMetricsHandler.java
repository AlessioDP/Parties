package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.core.bungeecord.addons.external.bstats.bungeecord.Metrics;
import com.alessiodp.core.bungeecord.addons.external.bstats.charts.SimplePie;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BungeeMetricsHandler extends MetricsHandler {
	public BungeeMetricsHandler(@NotNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void registerMetrics() {
		Metrics metrics = new Metrics((Plugin) plugin.getBootstrap(), plugin.getBstatsId());
		
		metrics.addCustomChart(new SimplePie("type_of_party_used", () -> {
			if (BungeeConfigParties.ADDITIONAL_FIXED_ENABLE)
				return "Fixed";
			return "Normal";
		}));
		
		// Config
		metrics.addCustomChart(new SimplePie("type_of_database_used", () -> plugin.getDatabaseManager().getDatabaseType().getFormattedName()));
		
		metrics.addCustomChart(new SimplePie("auto_command_system", () -> {
			if (BungeeConfigMain.ADDITIONAL_AUTOCMD_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("experience_system", () -> {
			if (BungeeConfigMain.ADDITIONAL_EXP_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("follow_system", () -> {
			if (BungeeConfigMain.ADDITIONAL_FOLLOW_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		// Parties
		metrics.addCustomChart(new SimplePie("color_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_COLOR_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("friendly_fire_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("home_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_HOME_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("kills_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_KILLS_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("nickname_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_NICKNAME_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("tag_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_TAG_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("teleport_system", () -> {
			if (BungeeConfigParties.ADDITIONAL_TELEPORT_ENABLE)
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