package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.TeleportTask;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

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
		long unixNow = -1;
		if (ConfigParties.TELEPORT_COOLDOWN > 0 && !((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = ((PartiesPlugin) plugin).getCooldownManager().getTeleportCooldown().get(partyPlayer.getPlayerUUID());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(ConfigParties.TELEPORT_COOLDOWN - (unixNow - unixTimestamp))));
				return;
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		if (unixNow != -1) {
			((PartiesPlugin) plugin).getCooldownManager().getTeleportCooldown().put(partyPlayer.getPlayerUUID(), unixNow);
			plugin.getScheduler().scheduleAsyncLater(new TeleportTask(((PartiesPlugin) plugin), partyPlayer.getPlayerUUID()), ConfigParties.TELEPORT_COOLDOWN, TimeUnit.SECONDS);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_TELEPORT_START
					.replace("{value}", Integer.toString(ConfigParties.TELEPORT_COOLDOWN * 20))
					.replace("{player}", sender.getName()), true);
		}
		
		Player bukkitPlayer = Bukkit.getPlayer(partyPlayer.getPlayerUUID());
		if (bukkitPlayer != null) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_TELEPORTING);
			
			// Make it sync
			plugin.getScheduler().getSyncExecutor().execute(() -> {
				for (PartyPlayer onlinePlayer : party.getOnlineMembers(true)) {
					if (!onlinePlayer.getPlayerUUID().equals(partyPlayer.getPlayerUUID())) {
						Player bukkitOnlinePlayer = Bukkit.getPlayer(onlinePlayer.getPlayerUUID());
						if (bukkitOnlinePlayer != null) {
							bukkitOnlinePlayer.teleport(bukkitPlayer.getLocation());
							
							User onlinePlayerUser = plugin.getPlayer(onlinePlayer.getPlayerUUID());
							sendMessage(onlinePlayerUser, partyPlayer, Messages.ADDCMD_TELEPORT_TELEPORTED, partyPlayer);
						}
					}
				}
			});
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_TELEPORT
					.replace("{player}", sender.getName()), true);
		}
	}
}
