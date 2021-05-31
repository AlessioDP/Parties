package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitPartiesPlayerPostTeleportEvent extends BukkitPartiesEvent implements IPlayerPostTeleportEvent {
	private final PartyPlayer player;
	private final Party party;
	private final Location destination;
	
	public BukkitPartiesPlayerPostTeleportEvent(PartyPlayer player, Party party, Location destination) {
		super(false);
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
	 * Get the destination as Location
	 *
	 * @return Returns the {@link Location}
	 */
	@NonNull
	public Location getDestination() {
		return destination;
	}
}
