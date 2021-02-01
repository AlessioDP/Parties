package com.alessiodp.parties.bungeecord.tasks;

import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandTeleport;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.TeleportDelayTask;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeTeleportDelayTask extends TeleportDelayTask {
	
	public BungeeTeleportDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyPlayerImpl targetPlayer) {
		super(plugin, partyPlayer, delayTime, targetPlayer);
	}
	
	@Override
	protected void performTeleport() {
		ProxiedPlayer bungeeTargetPlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(targetPlayer.getPlayerUUID());
		if (bungeeTargetPlayer != null) {
			BungeeCommandTeleport.teleportSinglePlayer(plugin, partyPlayer, targetPlayer, bungeeTargetPlayer.getServer().getInfo());
		}
	}
}
