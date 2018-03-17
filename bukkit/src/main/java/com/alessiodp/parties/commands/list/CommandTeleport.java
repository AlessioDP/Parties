package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.tasks.TeleportTask;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandTeleport implements ICommand {
	private Parties plugin;
	
	public CommandTeleport(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.TELEPORT.toString())) {
			pp.sendNoPermission(PartiesPermission.TELEPORT);
			return false;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_ADMIN_TELEPORT))
			return false;
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		long unixNow = -1;
		if (ConfigParties.TELEPORT_COOLDOWN > 0 && !PartiesUtils.checkPlayerRank(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = plugin.getPlayerManager().getTeleportCooldown().get(pp.getPlayerUUID());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				pp.sendMessage(Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(ConfigParties.TELEPORT_COOLDOWN - (unixNow - unixTimestamp))));
				return;
			}
		}
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.TELEPORT, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		if (unixNow != -1) {
			plugin.getPlayerManager().getTeleportCooldown().put(pp.getPlayerUUID(), unixNow);
			new TeleportTask(pp.getPlayerUUID(), plugin.getPlayerManager()).runTaskLater(plugin, ConfigParties.TELEPORT_COOLDOWN * 20);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_START
					.replace("{value}", Integer.toString(ConfigParties.TELEPORT_COOLDOWN * 20))
					.replace("{player}", pp.getName()), true);
		}
		
		pp.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTING);
		for (Player pl : party.getOnlinePlayers()) {
			if (!pl.getUniqueId().equals(pp.getPlayerUUID())) {
				// Make it sync
				plugin.getPartiesScheduler().runSync(() -> {
					pl.teleport(commandData.getPlayer().getLocation());
					plugin.getPlayerManager().getPlayer(pl.getUniqueId()).sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTED, pp);
				});
			}
		}
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_TELEPORT
				.replace("{player}", pp.getName()), true);
	}
}
