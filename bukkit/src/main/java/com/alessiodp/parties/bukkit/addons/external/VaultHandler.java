package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class VaultHandler {
	@NotNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "Vault";
	
	private static Economy economy;
	
	public void init() {
		if (BukkitConfigMain.ADDONS_VAULT_ENABLE) {
			if (Bukkit.getServer().getPluginManager().getPlugin(ADDON_NAME) != null && setupEconomy()) {
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDONS_VAULT_ENABLE = false;
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
			}
		}
	}
	
	private boolean setupEconomy() {
		boolean ret = false;
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp != null) {
			economy = rsp.getProvider();
			ret = true;
		}
		return ret;
	}
	
	public static double getPlayerBalance(User player) {
		return economy.getBalance(Bukkit.getOfflinePlayer(player.getUUID()));
	}
	
	public static void withdrawPlayer(User player, double commandPrice) {
		economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUUID()), commandPrice);
	}
}
