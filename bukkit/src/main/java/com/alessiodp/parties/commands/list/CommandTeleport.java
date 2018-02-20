package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
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
	
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.TELEPORT.toString())) {
			pp.sendNoPermission(PartiesPermission.TELEPORT);
			return;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_ADMIN_TELEPORT))
			return;
		
		/*
		 * Command handling
		 */
		long unixNow = -1;
		if (ConfigParties.TELEPORT_COOLDOWN > 0 && !PartiesUtils.checkPlayerRank(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = plugin.getPlayerManager().getTeleportCooldown().get(p.getUniqueId());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				pp.sendMessage(Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(ConfigParties.TELEPORT_COOLDOWN - (unixNow - unixTimestamp))));
				return;
			}
		}
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.TELEPORT, pp, commandLabel, args))
			return;
		
		/*
		 * Command starts
		 */
		if (unixNow != -1) {
			plugin.getPlayerManager().getTeleportCooldown().put(p.getUniqueId(), unixNow);
			new TeleportTask(p.getUniqueId(), plugin.getPlayerManager()).runTaskLater(plugin, ConfigParties.TELEPORT_COOLDOWN * 20);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_START
					.replace("{value}", Integer.toString(ConfigParties.TELEPORT_COOLDOWN * 20))
					.replace("{player}", p.getName()), true);
		}
		
		pp.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTING);
		for (Player pl : party.getOnlinePlayers()) {
			if (!pl.getUniqueId().equals(p.getUniqueId())) {
				pl.teleport(p.getLocation());
				plugin.getPlayerManager().getPlayer(pl.getUniqueId()).sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTED, pp);
			}
		}
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_TELEPORT
				.replace("{player}", p.getName()), true);
	}
}
