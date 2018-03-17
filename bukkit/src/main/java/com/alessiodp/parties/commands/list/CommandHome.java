package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitTask;

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
import com.alessiodp.parties.tasks.HomeTask;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandHome implements ICommand {
	private Parties plugin;
	
	public CommandHome(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.HOME.toString())) {
			pp.sendNoPermission(PartiesPermission.HOME);
			return false;
		}
		
		if (commandData.getArgs().length > 1) {
			if (!player.hasPermission(PartiesPermission.HOME_OTHERS.toString())) {
				pp.sendMessage(Messages.ADDCMD_HOME_WRONGCMD);
				return false;
			} else if (commandData.getArgs().length > 2) {
				pp.sendMessage(Messages.ADDCMD_HOME_WRONGCMD_ADMIN);
				return false;
			}
				
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		PartyEntity party;
		if (commandData.getArgs().length > 1)
			party = plugin.getPartyManager().getParty(commandData.getArgs()[1]);
		else
			party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		if (party == null) {
			if (commandData.getArgs().length > 1)
				pp.sendMessage(Messages.ADDCMD_HOME_NOEXISTS);
			else
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (commandData.getArgs().length == 1 && !PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_HOME))
			return;
		
		if (party.getHome() == null) {
			pp.sendMessage(Messages.ADDCMD_HOME_NOHOME, party);
			return;
		}
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.HOME, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		int delay = ConfigParties.HOME_DELAY;
		for (PermissionAttachmentInfo pa : commandData.getPlayer().getEffectivePermissions()) {
			String perm = pa.getPermission();
			if (perm.startsWith("parties.home.")) {
				String[] splitted = perm.split("\\.");
				try {
					delay = Integer.parseInt(splitted[2]);
				} catch(Exception ex) {}
			}
		}
		
		if (delay > 0) {
			pp.setHomeFrom(commandData.getPlayer().getLocation());
			plugin.getPlayerManager().addHomeCount();
			BukkitTask it = new HomeTask(plugin, pp, party.getHome()).runTaskLater(plugin, delay * 20);
			pp.setHomeTask(it.getTaskId());
			
			pp.sendMessage(Messages.ADDCMD_HOME_TELEPORTIN
					.replace("%time%", Integer.toString(delay)));
		} else {
			plugin.getPartiesScheduler().runSync(() -> {
				commandData.getPlayer().teleport(party.getHome());
			});
			pp.sendMessage(Messages.ADDCMD_HOME_TELEPORTED);
		}
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_HOME
				.replace("{player}", pp.getName())
				.replace("{party}", party.getName()), true);
	}
}