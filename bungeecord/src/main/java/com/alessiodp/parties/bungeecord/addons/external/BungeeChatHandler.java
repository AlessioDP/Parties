package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BungeeChatHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "BungeeChat";
	
	private static boolean active = false;
	
	public void init() {
		active = false;
		if (BungeeConfigMain.ADDITIONAL_MODERATION_ENABLE && BungeeConfigMain.ADDITIONAL_MODERATION_PLUGINS_BUNGEECHAT) {
			if (ProxyServer.getInstance().getPluginManager().getPlugin(ADDON_NAME) != null) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isPlayerMuted(UUID uuid) {
		if (active) {
			Optional<BungeeChatAccount> account = AccountManager.getAccount(uuid);
			if (account.isPresent())
				return account.get().isMuted();
		}
		return false;
	}
}