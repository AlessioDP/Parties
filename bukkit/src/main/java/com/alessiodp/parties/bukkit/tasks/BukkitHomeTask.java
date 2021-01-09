package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandHome;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BukkitHomeTask extends HomeTask {
	
	private final Player bukkitPlayer;
	private final Location startingLocation;
	private final Location finalLocation;
	private final double distanceLimitSquared;
	
	public BukkitHomeTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, PartyImpl party, long delayTime, PartyHomeImpl home) {
		super(plugin, partyPlayer, party, delayTime, home);
		
		bukkitPlayer = Bukkit.getPlayer(partyPlayer.getPlayerUUID());
		if (bukkitPlayer == null) {
			cancel();
		}
		startingLocation = bukkitPlayer != null ? bukkitPlayer.getLocation() : null;
		finalLocation = new Location(
				Bukkit.getWorld(home.getWorld()),
				home.getX(),
				home.getY(),
				home.getZ(),
				home.getYaw(),
				home.getPitch()
		);
		distanceLimitSquared = BukkitConfigParties.ADDITIONAL_HOME_CANCEL_DISTANCE * BukkitConfigParties.ADDITIONAL_HOME_CANCEL_DISTANCE;
	}
	
	@Override
	protected boolean canRunning() {
		if (BukkitConfigParties.ADDITIONAL_HOME_CANCEL_MOVING
				&& bukkitPlayer.getLocation().distanceSquared(startingLocation) > distanceLimitSquared) {
			// Cancel teleport
			cancel();
			
			player.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED, partyPlayer, null), true);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_HOME_DENIED_MOVING, player.getUUID().toString()), true);
			return false;
		}
		return true;
	}
	
	@Override
	protected void performTeleport() {
		String message = plugin.getMessageUtils().convertPlaceholders(Messages.ADDCMD_HOME_TELEPORTED, partyPlayer, party);
		BukkitCommandHome.teleportToPartyHome(plugin, player, finalLocation, message);
	}
}
