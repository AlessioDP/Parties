package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitCommandTeleport extends CommandTeleport {
	
	public BukkitCommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		boolean mustStartCooldown = false;
		if (ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_TELEPORT_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = ((PartiesPlugin) plugin).getCooldownManager().canTeleport(partyPlayer.getPlayerUUID(), ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown) {
			((PartiesPlugin) plugin).getCooldownManager().startTeleportCooldown(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
		}
		
		// Command starts
		Player bukkitPlayer = Bukkit.getPlayer(partyPlayer.getPlayerUUID());
		if (bukkitPlayer != null) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_TELEPORTING);
			
			for (PartyPlayer onlinePlayer : party.getOnlineMembers(true)) {
				if (!onlinePlayer.getPlayerUUID().equals(partyPlayer.getPlayerUUID())) {
					User user = plugin.getPlayer(onlinePlayer.getPlayerUUID());
					if (user != null) {
						((BukkitUser) user).teleportAsync(bukkitPlayer.getLocation()).thenAccept(result -> {
							if (result) {
								EssentialsHandler.updateLastTeleportLocation(user.getUUID());
								sendMessage(user, partyPlayer, Messages.ADDCMD_TELEPORT_TELEPORTED, partyPlayer);
							} else {
								plugin.getLoggerManager().printError(PartiesConstants.DEBUG_TELEPORT_ASYNC);
							}
						});
					}
				}
			}
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_TELEPORT,
					partyPlayer.getName(), party.getName()), true);
		}
	}
}
