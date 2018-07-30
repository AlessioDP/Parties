package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.ConsoleColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class VaultHandler {
	private BukkitPartiesPlugin plugin;
	private static final String ADDON_NAME = "Vault";
	
	private static Economy economy;
	
	
	public VaultHandler(BukkitPartiesPlugin instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		if (BukkitConfigMain.ADDONS_VAULT_ENABLE) {
			if (plugin.getBootstrap().getServer().getPluginManager().getPlugin(ADDON_NAME) != null) {
				if (setupEconomy()) {
					LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
							.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
				} else {
					BukkitConfigMain.ADDONS_VAULT_ENABLE = false;
					LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
							.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
				}
			}
		}
	}
	
	private boolean setupEconomy() {
		boolean ret = false;
		RegisteredServiceProvider<Economy> rsp = plugin.getBootstrap().getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp != null) {
			economy = rsp.getProvider();
			ret = economy != null;
		}
		return ret;
	}
	
	public static double getPlayerBalance(User player) {
		return economy.getBalance(Bukkit.getOfflinePlayer(player.getUUID()));
	}
	
	public static void withdrawPlayer(User player, double commandPrice) {
		economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUUID()), commandPrice);
	}
	
	/*
	public static boolean payCommand(EconomyManager.PaidCommand vc, double commandPrice, String commandMessage, BukkitPartyPlayerImpl player, String commandLabel, String[] args) {
		boolean blockerReturn = false;
		if (ConfigMain.ADDONS_VAULT_ENABLE) {
			Player pl = Bukkit.getPlayer(player.getPlayerUUID());
			if (commandPrice > 0 && !pl.hasPermission(PartiesPermission.ADMIN_VAULTBYPASS.toString())) {
				// Pay starts here
				if (ConfigMain.ADDONS_VAULT_CONFIRM_ENABLE) {
					// Confirm command
					if (player.getLastConfirmedCommand() != null && player.getLastConfirmedCommand().isConfirmed()) {
						if (economy.getBalance(pl) >= commandPrice) {
							// Paid
							economy.withdrawPlayer(pl, commandPrice);
							player.setLastConfirmedCommand(null);
						} else {
							// Confirmed but no money
							player.sendMessage(commandMessage);
							player.setLastConfirmedCommand(null);
							blockerReturn = true;
						}
					} else {
						// Asking for confirm
						String c = commandLabel;
						for (String s : args)
							c = c.concat(" " + s);
						
						player.setLastConfirmedCommand(
								new LastConfirmedCommand(System.currentTimeMillis(), c, false));
						player.sendMessage(Messages.ADDCMD_VAULT_CONFIRM_WARNONBUY
								.replace("%cmd%", args[0])
								.replace("%price%", Double.toString(commandPrice)));
						blockerReturn = true;
					}
				} else {
					// Pay to go
					if (economy.getBalance(pl) >= commandPrice) {
						economy.withdrawPlayer(pl, commandPrice);
					} else {
						player.sendMessage(commandMessage);
						blockerReturn = true;
					}
				}
			}
		}
		return blockerReturn;
	}*/
}
