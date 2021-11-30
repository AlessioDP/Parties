package com.alessiodp.parties.bukkit.addons.external;

import java.util.UUID;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.confuser.banmanager.bukkit.api.events.PlayerBannedEvent;
import me.confuser.banmanager.common.api.BmAPI;
import me.confuser.banmanager.common.data.PlayerData;
import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class BanManagerHandler implements Listener {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "BanManager";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDITIONAL_MODERATION_ENABLE && BukkitConfigMain.ADDITIONAL_MODERATION_PLUGINS_BANMANAGER) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, (BukkitPartiesBootstrap) plugin.getBootstrap());
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDITIONAL_MODERATION_PLUGINS_BANMANAGER = false;
				active = false;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isMuted(UUID uuid) {
		if (active) {
			return BmAPI.isMuted(uuid);
		}
		return false;
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event) {
		if (active && BukkitConfigMain.ADDITIONAL_MODERATION_AUTOKICK) {
			PlayerData pl = event.getBan().getPlayer();
			plugin.getPartyManager().kickBannedPlayer(pl.getUUID());
		}
	}
}
