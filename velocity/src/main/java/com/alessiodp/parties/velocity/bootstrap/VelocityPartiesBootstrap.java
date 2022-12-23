package com.alessiodp.parties.velocity.bootstrap;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.velocity.addons.external.bstats.velocity.Metrics;
import com.alessiodp.core.velocity.bootstrap.ADPVelocityBootstrap;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.velocity.VelocityConstants;
import com.alessiodp.parties.velocity.VelocityPartiesPlugin;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.nio.file.Path;

@Plugin(
		id = PartiesConstants.PLUGIN_FALLBACK,
		name = PartiesConstants.PLUGIN_NAME,
		description = VelocityConstants.DESCRIPTION,
		authors = {VelocityConstants.AUTHORS},
		url = VelocityConstants.URL,
		version = VelocityConstants.VERSION,
		dependencies = {
				@Dependency(id = "lastloginapi", optional = true),
				@Dependency(id = "redisbungee", optional = true)
		}
)
public class VelocityPartiesBootstrap extends ADPVelocityBootstrap {
	@Inject
	public VelocityPartiesBootstrap(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
		super(server, logger, dataDirectory, metricsFactory);
	}
	
	@Override
	protected ADPPlugin initializePlugin() {
		return new VelocityPartiesPlugin(this);
	}
	
	@Override
	public @NotNull String getAuthor() {
		return VelocityConstants.AUTHORS;
	}
	
	@Override
	public @NotNull String getVersion() {
		return VelocityConstants.VERSION;
	}
}
