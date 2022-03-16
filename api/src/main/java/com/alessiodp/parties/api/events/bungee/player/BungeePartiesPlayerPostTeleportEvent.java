package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.NotNull;

public class BungeePartiesPlayerPostTeleportEvent extends BungeePartiesEvent implements IPlayerPostTeleportEvent {
	private final PartyPlayer player;
	private final Party party;
	private final ServerInfo destination;
	
	public BungeePartiesPlayerPostTeleportEvent(PartyPlayer player, Party party, ServerInfo destination) {
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
	 * Get the destination as ServerInfo
	 *
	 * @return the {@link ServerInfo}
	 */
	public @NotNull ServerInfo getDestination() {
		return destination;
	}
}
