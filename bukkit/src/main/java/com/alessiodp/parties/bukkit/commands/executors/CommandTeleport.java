package com.alessiodp.parties.bukkit.commands.executors;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.bukkit.tasks.TeleportTask;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandTeleport extends AbstractCommand {
	
	public CommandTeleport(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.TELEPORT.toString())) {
			pp.sendNoPermission(PartiesPermission.TELEPORT);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_ADMIN_TELEPORT))
			return false;
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		long unixNow = -1;
		if (BukkitConfigParties.TELEPORT_COOLDOWN > 0 && !plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = ((BukkitCooldownManager) plugin.getCooldownManager()).getTeleportCooldown().get(pp.getPlayerUUID());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				pp.sendMessage(BukkitMessages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(BukkitConfigParties.TELEPORT_COOLDOWN - (unixNow - unixTimestamp))));
				return;
			}
		}
		
		if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		if (unixNow != -1) {
			((BukkitCooldownManager) plugin.getCooldownManager()).getTeleportCooldown().put(pp.getPlayerUUID(), unixNow);
			plugin.getPartiesScheduler().scheduleTaskLater(new TeleportTask(plugin, pp.getPlayerUUID()), BukkitConfigParties.TELEPORT_COOLDOWN);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_START
					.replace("{value}", Integer.toString(BukkitConfigParties.TELEPORT_COOLDOWN * 20))
					.replace("{player}", pp.getName()), true);
		}
		
		Player bukkitPlayer = Bukkit.getPlayer(pp.getPlayerUUID());
		pp.sendMessage(BukkitMessages.ADDCMD_TELEPORT_TELEPORTING);
		plugin.getPartiesScheduler().runSync(() -> {
			for (PartyPlayer onlinePlayer : party.getOnlinePlayers()) {
				if (!onlinePlayer.getPlayerUUID().equals(pp.getPlayerUUID())) {
					Player bukkitOnlinePlayer = Bukkit.getPlayer(onlinePlayer.getPlayerUUID());
					bukkitOnlinePlayer.teleport(bukkitPlayer.getLocation());
					
					plugin.getPlayerManager().getPlayer(onlinePlayer.getPlayerUUID()).sendMessage(BukkitMessages.ADDCMD_TELEPORT_TELEPORTED, pp);
				}
			}
		});
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_TELEPORT
				.replace("{player}", pp.getName()), true);
	}
}
