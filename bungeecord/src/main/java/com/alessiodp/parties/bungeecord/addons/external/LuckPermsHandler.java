package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import de.myzelyam.api.vanish.BungeeVanishAPI;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class LuckPermsHandler {
	@NotNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "LuckPerms";
	
	private static boolean active = false;
	
	public void init() {
		active = false;

		if (ProxyServer.getInstance().getPluginManager().getPlugin(ADDON_NAME) != null) {
			active = true;
			plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
		}
	}
	
	public static String getPlayerPrefix(UUID uuid) {
		if (active) {
			LuckPerms api = LuckPermsProvider.get();
			User user = api.getUserManager().getUser(uuid);
			if (user != null) {
				return user.getCachedData().getMetaData().getPrefix();
			}
		}
		return "";
	}
}