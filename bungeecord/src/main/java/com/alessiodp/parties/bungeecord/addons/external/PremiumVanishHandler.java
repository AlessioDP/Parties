package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import de.myzelyam.api.vanish.BungeeVanishAPI;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class PremiumVanishHandler {
	@NotNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "PremiumVanish";
	
	private static boolean active = false;
	
	public void init() {
		active = false;
		if (ProxyServer.getInstance().getPluginManager().getPlugin(ADDON_NAME) != null) {
			active = true;
			
			plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
		}
	}
	
	public static boolean isPlayerVanished(UUID uuid) {
		if (active) {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
			if (player != null)
				return BungeeVanishAPI.isInvisible(player);
		}
		return false;
	}
}