package com.alessiodp.parties.addons.external;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;

import net.milkbowl.vault.economy.Economy;

public class VaultHandler {
	private Parties plugin;
	private static final String ADDON_NAME = "Vault";
	
	private static Economy economy;
	
	
	public VaultHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		if (ConfigMain.ADDONS_VAULT_ENABLE) {
			if (plugin.getServer().getPluginManager().getPlugin(ADDON_NAME) != null) {
				if (setupEconomy()) {
					LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
							.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
				} else {
					ConfigMain.ADDONS_VAULT_ENABLE = false;
					LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
							.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
				}
			}
		}
	}
	
	private boolean setupEconomy() {
		boolean ret = false;
		RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp != null) {
			economy = rsp.getProvider();
			ret = economy != null;
		}
		return ret;
	}
	
	
	public static boolean payCommand(VaultCommand vc, PartyPlayerEntity player, String commandLabel, String[] args) {
		boolean blockerReturn = false;
		if (ConfigMain.ADDONS_VAULT_ENABLE) {
			double commandPrice = vc.getValue();
			Player pl = player.getPlayer();
			if (commandPrice > 0 && !pl.hasPermission(PartiesPermission.ADMIN_VAULTBYPASS.toString())) {
				// Pay starts here
				if (ConfigMain.ADDONS_VAULT_CONFIRM_ENABLE) {
					// Confirm command
					if (player.getLastCommand() != null && ((boolean) player.getLastCommand()[2]) == true) {
						if (economy.getBalance(pl) >= commandPrice) {
							// Paid
							economy.withdrawPlayer(pl, commandPrice);
							player.setLastCommand(null);
						} else {
							// Confirmed but no money
							player.sendMessage(vc.getMessage(commandPrice));
							player.setLastCommand(null);
							blockerReturn = true;
						}
					} else {
						// Asking for confirm
						String c = commandLabel;
						for (String s : args)
							c = c.concat(" " + s);
						
						player.setLastCommand(new Object[]{System.currentTimeMillis(), c, false});
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
						player.sendMessage(vc.getMessage(commandPrice));
						blockerReturn = true;
					}
				}
			}
		}
		return blockerReturn;
	}
	
	public enum VaultCommand {
		CLAIM, COLOR, CREATE, DESC, HOME, JOIN, MOTD, PREFIX, SETHOME, SUFFIX, TELEPORT;
		
		public double getValue() {
			double ret = 0;
			switch (this) {
			case CLAIM:
				ret = ConfigMain.ADDONS_VAULT_PRICE_CLAIM;
				break;
			case COLOR:
				ret = ConfigMain.ADDONS_VAULT_PRICE_COLOR;
				break;
			case CREATE:
				ret = ConfigMain.ADDONS_VAULT_PRICE_CREATE;
				break;
			case DESC:
				ret = ConfigMain.ADDONS_VAULT_PRICE_DESC;
				break;
			case HOME:
				ret = ConfigMain.ADDONS_VAULT_PRICE_HOME;
				break;
			case JOIN:
				ret = ConfigMain.ADDONS_VAULT_PRICE_JOIN;
				break;
			case MOTD:
				ret = ConfigMain.ADDONS_VAULT_PRICE_MOTD;
				break;
			case PREFIX:
				ret = ConfigMain.ADDONS_VAULT_PRICE_PREFIX;
				break;
			case SETHOME:
				ret = ConfigMain.ADDONS_VAULT_PRICE_SETHOME;
				break;
			case SUFFIX:
				ret = ConfigMain.ADDONS_VAULT_PRICE_SUFFIX;
				break;
			case TELEPORT:
				ret = ConfigMain.ADDONS_VAULT_PRICE_TELEPORT;
			}
			return ret;
		}
		
		public String getMessage(double price) {
			String ret = "";
			switch (this) {
			case CLAIM:
				ret = Messages.ADDCMD_VAULT_NOMONEY_CLAIM;
				break;
			case COLOR:
				ret = Messages.ADDCMD_VAULT_NOMONEY_COLOR;
				break;
			case CREATE:
				ret = Messages.ADDCMD_VAULT_NOMONEY_CREATE;
				break;
			case DESC:
				ret = Messages.ADDCMD_VAULT_NOMONEY_DESC;
				break;
			case HOME:
				ret = Messages.ADDCMD_VAULT_NOMONEY_HOME;
				break;
			case JOIN:
				ret = Messages.ADDCMD_VAULT_NOMONEY_JOIN;
				break;
			case MOTD:
				ret = Messages.ADDCMD_VAULT_NOMONEY_MOTD;
				break;
			case PREFIX:
				ret = Messages.ADDCMD_VAULT_NOMONEY_PREFIX;
				break;
			case SETHOME:
				ret = Messages.ADDCMD_VAULT_NOMONEY_SETHOME;
				break;
			case SUFFIX:
				ret = Messages.ADDCMD_VAULT_NOMONEY_SUFFIX;
				break;
			case TELEPORT:
				ret = Messages.ADDCMD_VAULT_NOMONEY_TELEPORT;
			}
			ret = ret.replace("%price%", Double.toString(price));
			
			return ret;
		}
	}
}
