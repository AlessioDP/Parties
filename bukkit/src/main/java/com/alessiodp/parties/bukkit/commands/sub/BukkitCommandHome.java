package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.tasks.HomeTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class BukkitCommandHome extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public BukkitCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.DESC.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.DESC);
			return false;
		}
		
		if (commandData.getArgs().length > 1) {
			if (!sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS.toString())) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_WRONGCMD);
				return false;
			} else if (commandData.getArgs().length > 2) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_WRONGCMD_ADMIN);
				return false;
			}
				
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		BukkitPartyPlayerImpl partyPlayer = (BukkitPartyPlayerImpl) ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		PartyImpl party;
		if (commandData.getArgs().length > 1)
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
		else
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyName());
		
		if (party == null) {
			if (commandData.getArgs().length > 1)
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_NOEXISTS);
			else
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (commandData.getArgs().length == 1 && !((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_HOME))
			return;
		
		if (party.getHome() == null) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_NOHOME, party);
			return;
		}
		
		if (partyPlayer.getHomeDelayTask() != null) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_TELEPORTWAITING, party);
			return;
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.HOME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		Player bukkitPlayer = Bukkit.getPlayer(commandData.getSender().getUUID());
		int delay = BukkitConfigParties.HOME_DELAY;
		String homeDelayPermission = sender.getDynamicPermission(PartiesPermission.HOME.toString() + ".");
		if (homeDelayPermission != null) {
			try {
				delay = Integer.parseInt(homeDelayPermission);
			} catch (Exception ignored) {}
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
					(PartiesPlugin) plugin,
					partyPlayer,
					bukkitPlayer,
					delay,
					loc
			);
			CancellableTask task = plugin.getScheduler().scheduleAsyncRepeating(homeTask, 0, 300, TimeUnit.MILLISECONDS);
			partyPlayer.setHomeDelayTask(task);
			
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_TELEPORTIN
					.replace("%time%", Integer.toString(delay)));
		} else {
			plugin.getScheduler().getSyncExecutor().execute(() -> {
				EssentialsHandler.updateLastTeleportLocation(bukkitPlayer);
				bukkitPlayer.teleport(loc);
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_TELEPORTED);
			});
		}
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_HOME
				.replace("{player}", sender.getName())
				.replace("{party}", party.getName()), true);
	}
}