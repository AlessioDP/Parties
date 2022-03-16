package com.alessiodp.parties.bukkit.parties.objects;

import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandTeleport;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyTeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitPartyTeleportRequest extends PartyTeleportRequest {
	public BukkitPartyTeleportRequest(@NotNull PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl requester) {
		super(plugin, player, requester);
	}
	
	@Override
	protected void teleportPlayer() {
		Player bukkitRequester = Bukkit.getPlayer(requester.getPlayerUUID());
		if (bukkitRequester != null) {
			BukkitCommandTeleport.teleportSinglePlayer(plugin, player, requester, bukkitRequester.getLocation());
		}
	}
}
