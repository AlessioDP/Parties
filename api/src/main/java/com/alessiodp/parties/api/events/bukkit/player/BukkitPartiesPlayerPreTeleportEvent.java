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
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	/**
	 * Get the destination as Location
	 *
	 * @return Returns the {@link Location}
	 */
	@NotNull
	public Location getDestination() {
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
