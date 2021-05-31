package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreTeleportEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BungeePartiesPlayerPreTeleportEvent extends BungeePartiesEvent implements IPlayerPreTeleportEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final ServerInfo destination;
	
	public BungeePartiesPlayerPreTeleportEvent(PartyPlayer player, Party party, ServerInfo destination) {
		this.player = player;
		this.party = party;
		this.destination = destination;
	}
	
	@NonNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	/**
	 * Get the destination as ServerInfo
	 *
	 * @return Returns the {@link ServerInfo}
	 */
	@NonNull
	public ServerInfo getDestination() {
		return destination;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
