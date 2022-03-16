package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EqualsAndHashCode
public abstract class PartyTeleportRequest {
	@EqualsAndHashCode.Exclude protected final PartiesPlugin plugin;
	@Getter @Setter @EqualsAndHashCode.Exclude protected PartyPlayerImpl player;
	@Getter @Setter @EqualsAndHashCode.Exclude protected PartyPlayerImpl requester;
	private final UUID playerId;
	private final UUID requesterId;
	
	public PartyTeleportRequest(@NotNull PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl requester) {
		this.plugin = plugin;
		this.player = player;
		this.requester = requester;
		// Used for hash purpose
		playerId = player.getPlayerUUID();
		requesterId = requester.getPlayerUUID();
	}
	
	public void accept() {
		if (player.getPendingTeleportRequests().remove(this)) {
			teleportPlayer();
		}
	}
	
	public void deny() {
		if (player.getPendingTeleportRequests().remove(this)) {
			player.sendMessage(Messages.ADDCMD_TELEPORT_ACCEPT_REQUEST_DENIED, requester);
		}
	}
	
	public void timeout() {
		player.getPendingTeleportRequests().remove(this);
	}
	
	protected abstract void teleportPlayer();
}
