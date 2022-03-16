package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreTeleportEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesPlayerPreTeleportEvent extends BukkitPartiesEvent implements IPlayerPreTeleportEvent, Cancellable {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final Location destination;
	
	public BukkitPartiesPlayerPreTeleportEvent(PartyPlayer player, Party party, Location destination) {
		super(false);
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
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	/**
	 * Get the destination as Location
	 *
	 * @return the {@link Location}
	 */
	public @NotNull Location getDestination() {
		return destination;
	}
}
