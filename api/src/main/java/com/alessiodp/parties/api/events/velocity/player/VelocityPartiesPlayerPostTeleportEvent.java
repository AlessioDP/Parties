package com.alessiodp.parties.api.events.velocity.player;

import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesPlayerPostTeleportEvent extends VelocityPartiesEvent implements IPlayerPostTeleportEvent {
	private final PartyPlayer player;
	private final Party party;
	private final RegisteredServer destination;
	
	public VelocityPartiesPlayerPostTeleportEvent(PartyPlayer player, Party party, RegisteredServer destination) {
		this.player = player;
		this.party = party;
		this.destination = destination;
	}
	
	@Override
	public @NotNull PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	/**
	 * Get the destination as RegisteredServer
	 *
	 * @return the {@link RegisteredServer}
	 */
	public @NotNull RegisteredServer getDestination() {
		return destination;
	}
}
