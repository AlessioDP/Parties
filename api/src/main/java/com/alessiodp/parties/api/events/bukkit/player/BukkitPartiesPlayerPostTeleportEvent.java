package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

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
}
