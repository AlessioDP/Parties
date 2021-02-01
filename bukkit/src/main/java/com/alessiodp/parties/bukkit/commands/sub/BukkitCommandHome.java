package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.tasks.BukkitHomeDelayTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandHome;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.tasks.HomeDelayTask;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BukkitCommandHome extends CommandHome {
	
	public BukkitCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void teleportPlayer(User player, PartyPlayerImpl partyPlayer, PartyHomeImpl home) {
		Location loc = new Location(
				Bukkit.getWorld(home.getWorld()),
				home.getX(),
				home.getY(),
				home.getZ(),
				home.getYaw(),
				home.getPitch()
		);
		
		BukkitUser bukkitUser = (BukkitUser) plugin.getPlayer(partyPlayer.getPlayerUUID());
		if (bukkitUser != null)
			teleportToPartyHome((PartiesPlugin) plugin, partyPlayer, bukkitUser, loc);
	}
	
	@Override
	protected HomeDelayTask teleportPlayerWithDelay(PartyPlayerImpl partyPlayer, PartyHomeImpl home, int delay) {
		return new BukkitHomeDelayTask(
				(PartiesPlugin) plugin,
				partyPlayer,
				delay,
				home
		);
	}
	
	public static void teleportToPartyHome(PartiesPlugin plugin, PartyPlayerImpl player, BukkitUser bukkitUser, Location location) {
		teleportToPartyHome(plugin, player, bukkitUser, location, Messages.ADDCMD_HOME_TELEPORTED);
	}
	
	public static void teleportToPartyHome(PartiesPlugin plugin, PartyPlayerImpl player, BukkitUser bukkitUser, Location location, String message) {
		plugin.getScheduler().getSyncExecutor().execute(() -> {
			bukkitUser.teleportAsync(location).thenAccept(result -> {
				if (result) {
					EssentialsHandler.updateLastTeleportLocation(player.getPlayerUUID());
					player.sendMessage(message);
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_TELEPORT_DONE, player.getPlayerUUID().toString()), true);
				} else {
					plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_TELEPORT_ASYNC, player.getPlayerUUID().toString()));
				}
			});
		});
	}
}