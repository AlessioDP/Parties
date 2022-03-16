package com.alessiodp.parties.velocity.addons.external;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.MetricsHandler;
import com.alessiodp.core.velocity.addons.external.bstats.charts.SimplePie;
import com.alessiodp.core.velocity.addons.external.bstats.velocity.Metrics;
import com.alessiodp.core.velocity.bootstrap.ADPVelocityBootstrap;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigMain;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigParties;
import org.jetbrains.annotations.NotNull;

public class VelocityMetricsHandler extends MetricsHandler {
	public VelocityMetricsHandler(@NotNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void registerMetrics() {
		ADPVelocityBootstrap velocityPlugin = ((ADPVelocityBootstrap) plugin.getBootstrap());
		Metrics metrics = velocityPlugin.getMetricsFactory().make(velocityPlugin, plugin.getBstatsId());
		
		metrics.addCustomChart(new SimplePie("type_of_party_used", () -> {
			if (VelocityConfigParties.ADDITIONAL_FIXED_ENABLE)
				return "Fixed";
			return "Normal";
		}));
		
		// Config
		metrics.addCustomChart(new SimplePie("type_of_database_used", () -> plugin.getDatabaseManager().getDatabaseType().getFormattedName()));
		
		metrics.addCustomChart(new SimplePie("auto_command_system", () -> {
			if (VelocityConfigMain.ADDITIONAL_AUTOCMD_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("experience_system", () -> {
			if (VelocityConfigMain.ADDITIONAL_EXP_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("follow_system", () -> {
			if (VelocityConfigMain.ADDITIONAL_FOLLOW_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		// Parties
		metrics.addCustomChart(new SimplePie("color_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_COLOR_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("friendly_fire_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("home_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_HOME_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("kills_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_KILLS_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("nickname_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_NICKNAME_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("tag_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_TAG_ENABLE)
				return "Enabled";
			return "Disabled";
		}));
		
		metrics.addCustomChart(new SimplePie("teleport_system", () -> {
			if (VelocityConfigParties.ADDITIONAL_TELEPORT_ENABLE)
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