package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.tasks.HomeTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class CommandHome extends AbstractCommand {
	
	public CommandHome(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.HOME.toString())) {
			pp.sendNoPermission(PartiesPermission.HOME);
			return false;
		}
		
		if (commandData.getArgs().length > 1) {
			if (!sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS.toString())) {
				pp.sendMessage(BukkitMessages.ADDCMD_HOME_WRONGCMD);
				return false;
			} else if (commandData.getArgs().length > 2) {
				pp.sendMessage(BukkitMessages.ADDCMD_HOME_WRONGCMD_ADMIN);
				return false;
			}
				
		}
		
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		PartyImpl party;
		if (commandData.getArgs().length > 1)
			party = plugin.getPartyManager().getParty(commandData.getArgs()[1]);
		else
			party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		if (party == null) {
			if (commandData.getArgs().length > 1)
				pp.sendMessage(BukkitMessages.ADDCMD_HOME_NOEXISTS);
			else
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (commandData.getArgs().length == 1 && !plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_HOME))
			return;
		
		if (party.getHome() == null) {
			pp.sendMessage(BukkitMessages.ADDCMD_HOME_NOHOME, party);
			return;
		}
		
		if (pp.getHomeDelayTask() != -1) {
			pp.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTWAITING, party);
			return;
		}
		
		if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.HOME, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		Player bukkitPlayer = Bukkit.getPlayer(commandData.getSender().getUUID());
		int delay = BukkitConfigParties.HOME_DELAY;
		for (PermissionAttachmentInfo pa : bukkitPlayer.getEffectivePermissions()) {
			String perm = pa.getPermission();
			if (perm.startsWith("parties.home.")) {
				String[] splitted = perm.split("\\.");
				try {
					delay = Integer.parseInt(splitted[2]);
				} catch(Exception ignored) {}
			}
		}
		
		Location loc = new Location(
				Bukkit.getWorld(party.getHome().getWorld()),
				party.getHome().getX(),
				party.getHome().getY(),
				party.getHome().getZ(),
				party.getHome().getYaw(),
				party.getHome().getPitch());
		
		if (delay > 0) {
			HomeTask homeTask = new HomeTask(
					plugin,
					pp,
					bukkitPlayer,
					delay,
					loc
			);
			int taskId = plugin.getPartiesScheduler().scheduleAsyncTaskTimer(homeTask, 5L);
			pp.setHomeDelayTask(taskId);
			
			/*
			int taskId = plugin.getPartiesScheduler().scheduleTaskLater(new HomeTask(pp, loc), delay * 20L);
			HomeCooldown homeCooldown = new HomeCooldown(plugin, taskId, bukkitPlayer.getLocation());
			pp.setHomeCooldown(homeCooldown);
			homeCooldown.save();*/
			
			pp.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTIN
					.replace("%time%", Integer.toString(delay)));
		} else {
			plugin.getPartiesScheduler().runSync(() -> {
				bukkitPlayer.teleport(loc);
				pp.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTED);
			});
		}
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_HOME
				.replace("{player}", pp.getName())
				.replace("{party}", party.getName()), true);
	}
}