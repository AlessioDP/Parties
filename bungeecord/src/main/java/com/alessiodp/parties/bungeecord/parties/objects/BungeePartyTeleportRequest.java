package com.alessiodp.parties.bungeecord.parties.objects;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandTeleport;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyTeleportRequest;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePartyTeleportRequest extends PartyTeleportRequest {
	public BungeePartyTeleportRequest(@NonNull PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl requester) {
		super(plugin, player, requester);
	}
	
	@Override
	protected void teleportPlayer() {
		ProxiedPlayer bungeeRequester = ProxyServer.getInstance().getPlayer(requester.getPlayerUUID());
		if (bungeeRequester != null) {
			BungeeCommandTeleport.teleportSinglePlayer(plugin, player, requester, bungeeRequester.getServer().getInfo());
		}
	}
}
