package com.alessiodp.parties.bukkit.utils;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.VaultHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;

public class BukkitEconomyManager extends EconomyManager {
	
	public BukkitEconomyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean payCommand(PaidCommand paidCommand, PartyPlayerImpl partyPlayerEntity, String commandLabel, String[] args) {
		return payCommand(paidCommand, (BukkitPartyPlayerImpl) partyPlayerEntity, commandLabel, args);
	}
	
	private boolean payCommand(PaidCommand vaultCommand, BukkitPartyPlayerImpl partyPlayerEntity, String commandLabel, String[] args) {
		boolean denyCommand = false;
		if (BukkitConfigMain.ADDONS_VAULT_ENABLE) {
			double commandPrice = getCommandValue(vaultCommand);
			User bukkitPlayer = plugin.getPlayer(partyPlayerEntity.getPlayerUUID());
			
			//Player pl = player.getPlayer();
			if (commandPrice > 0 && !bukkitPlayer.hasPermission(PartiesPermission.ADMIN_VAULTBYPASS)) {
				// Pay starts here
				if (BukkitConfigMain.ADDONS_VAULT_CONFIRM_ENABLE) {
					// Confirm command
					if (partyPlayerEntity.getLastConfirmedCommand() != null && partyPlayerEntity.getLastConfirmedCommand().isConfirmed()) {
						if (VaultHandler.getPlayerBalance(bukkitPlayer) >= commandPrice) {
							// Paid
							VaultHandler.withdrawPlayer(bukkitPlayer, commandPrice);
							partyPlayerEntity.setLastConfirmedCommand(null);
						} else {
							// Confirmed but no money
							bukkitPlayer.sendMessage(plugin.getMessageUtils().convertPlaceholders(getCommandMessage(vaultCommand, commandPrice), partyPlayerEntity, null), true);
							partyPlayerEntity.setLastConfirmedCommand(null);
							denyCommand = true;
						}
					} else {
						// Asking for confirm
						String c = commandLabel;
						for (String s : args)
							c = c.concat(" " + s);
						
						LastConfirmedCommand packet = new LastConfirmedCommand(
								System.currentTimeMillis(),
								c
						);
						
						partyPlayerEntity.setLastConfirmedCommand(packet);
						bukkitPlayer.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_VAULT_CONFIRM_WARNONBUY
								.replace("%cmd%", args[0])
								.replace("%price%", Double.toString(commandPrice)), partyPlayerEntity, null), true);
						denyCommand = true;
					}
				} else {
					// Pay to go
					if (VaultHandler.getPlayerBalance(bukkitPlayer) >= commandPrice) {
						VaultHandler.withdrawPlayer(bukkitPlayer, commandPrice);
					} else {
						bukkitPlayer.sendMessage(plugin.getMessageUtils().convertPlaceholders(getCommandMessage(vaultCommand, commandPrice), partyPlayerEntity, null), true);
						denyCommand = true;
					}
				}
			}
		}
		return denyCommand;
	}
	
	private double getCommandValue(PaidCommand vaultCommand) {
		double ret = 0;
		switch (vaultCommand) {
			case ASK:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_ASK;
				break;
			case CLAIM:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_CLAIM;
				break;
			case COLOR:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_COLOR;
				break;
			case CREATE:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_CREATE;
				break;
			case DESC:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_DESC;
				break;
			case FOLLOW:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_FOLLOW;
				break;
			case HOME:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_HOME;
				break;
			case JOIN:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_JOIN;
				break;
			case MOTD:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_MOTD;
				break;
			case PASSWORD:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_PASSWORD;
				break;
			case PROTECTION:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_PROTECTION;
				break;
			case RENAME:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_RENAME;
				break;
			case SETHOME:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_SETHOME;
				break;
			case TAG:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_TAG;
				break;
			case TELEPORT:
				ret = BukkitConfigMain.ADDONS_VAULT_PRICE_TELEPORT;
				break;
			default:
				// Nothing to do
				break;
		}
		return ret;
	}
	
	private String getCommandMessage(PaidCommand vaultCommand, double price) {
		String ret = "";
		switch (vaultCommand) {
			case ASK:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_ASK;
				break;
			case CLAIM:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_CLAIM;
				break;
			case COLOR:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_COLOR;
				break;
			case CREATE:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_CREATE;
				break;
			case DESC:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_DESC;
				break;
			case FOLLOW:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_FOLLOW;
				break;
			case HOME:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_HOME;
				break;
			case JOIN:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_JOIN;
				break;
			case MOTD:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_MOTD;
				break;
			case PASSWORD:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_PASSWORD;
				break;
			case PROTECTION:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_PROTECTION;
				break;
			case RENAME:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_RENAME;
				break;
			case SETHOME:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_SETHOME;
				break;
			case TAG:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_TAG;
				break;
			case TELEPORT:
				ret = BukkitMessages.ADDCMD_VAULT_NOMONEY_TELEPORT;
				break;
			default:
				// Nothing to do
				break;
		}
		ret = ret.replace("%price%", Double.toString(price));
		
		return ret;
	}
}
