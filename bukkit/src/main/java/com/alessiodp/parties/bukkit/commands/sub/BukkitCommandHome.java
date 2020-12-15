package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.tasks.BukkitHomeTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandHome;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.tasks.HomeTask;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BukkitCommandHome extends CommandHome {
	
	public BukkitCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void executeHome(User player, PartyPlayerImpl partyPlayer, PartyImpl party, PartyHomeImpl home) {
		Location loc = new Location(
				Bukkit.getWorld(home.getWorld()),
				home.getX(),
				home.getY(),
				home.getZ(),
				home.getYaw(),
				home.getPitch()
		);
		
		String message = ((PartiesPlugin) plugin).getMessageUtils().convertPlaceholders(Messages.ADDCMD_HOME_TELEPORTED, partyPlayer, party);
		teleportToPartyHome((PartiesPlugin) plugin, player, loc, message);
	}
	
	@Override
	protected HomeTask executeHomeWithDelay(PartyPlayerImpl partyPlayer, PartyImpl party, PartyHomeImpl home, int delay) {
		return new BukkitHomeTask(
				(PartiesPlugin) plugin,
				partyPlayer,
				party,
				delay,
				home
		);
	}
	
	public static void teleportToPartyHome(PartiesPlugin plugin, User player, Location location, String message) {
		plugin.getScheduler().getSyncExecutor().execute(() -> {
			((BukkitUser) player).teleportAsync(location).thenAccept(result -> {
				if (result) {
					EssentialsHandler.updateLastTeleportLocation(player.getUUID());
					player.sendMessage(message, true);
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_TELEPORT_DONE, player.getUUID().toString()), true);
				} else {
					plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_TELEPORT_ASYNC, player.getUUID().toString()));
				}
			});
		});
	}
}