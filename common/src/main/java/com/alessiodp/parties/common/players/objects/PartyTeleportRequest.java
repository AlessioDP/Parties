package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
public abstract class PartyTeleportRequest {
	@NonNull protected final PartiesPlugin plugin;
	@Getter @Setter protected PartyPlayerImpl player;
	@Getter @Setter protected PartyPlayerImpl requester;
	
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
