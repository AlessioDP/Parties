package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandTeleport;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.TeleportDelayTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BukkitTeleportDelayTask extends TeleportDelayTask {
	private final Player bukkitPlayer;
	private final Location startingLocation;
	private final double distanceLimitSquared;
	
	public BukkitTeleportDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyPlayerImpl targetPlayer) {
		super(plugin, partyPlayer, delayTime, targetPlayer);
		
		bukkitPlayer = Bukkit.getPlayer(partyPlayer.getPlayerUUID());
		if (bukkitPlayer == null) {
			cancel();
		}
		startingLocation = bukkitPlayer != null ? bukkitPlayer.getLocation() : null;
		distanceLimitSquared = BukkitConfigParties.ADDITIONAL_TELEPORT_CANCEL_DISTANCE * BukkitConfigParties.ADDITIONAL_TELEPORT_CANCEL_DISTANCE;
	}
	
	@Override
	protected boolean canRunning() {
		if (BukkitConfigParties.ADDITIONAL_TELEPORT_CANCEL_MOVING && (
				!Objects.equals(startingLocation.getWorld(), bukkitPlayer.getLocation().getWorld())
				|| bukkitPlayer.getLocation().distanceSquared(startingLocation) > distanceLimitSquared
		)) {
			// Cancel teleport
			cancel();
			
			player.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_TELEPORT_PLAYER_TELEPORTDENIED, partyPlayer, null), true);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_TELEPORT_DENIED_MOVING, player.getUUID()), true);
			return false;
		}
		return true;
	}
	
	@Override
	protected void performTeleport() {
		Player bukkitTarget = Bukkit.getPlayer(targetPlayer.getPlayerUUID());
		if (bukkitTarget != null) {
			BukkitCommandTeleport.teleportSinglePlayer(plugin, partyPlayer, targetPlayer, bukkitTarget.getLocation());
		}
	}
}
