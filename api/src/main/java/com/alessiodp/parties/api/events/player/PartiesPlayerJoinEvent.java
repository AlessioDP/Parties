package com.alessiodp.parties.api.events.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PartiesPlayerJoinEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player that is joining inside the party
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	PartyPlayer getPartyPlayer();
	
	/**
	 * Get the party
	 * 
	 * @return Returns the {@link Party}
	 */
	@NotNull
	Party getParty();
	
	/**
	 * Has been invited?
	 * 
	 * @return Returns {@code true} if the player has been invited
	 */
	boolean isInvited();
	
	/**
	 * Get the inviter
	 * 
	 * @return Returns the {@code UUID} of the inviter, {@code null} if the player joined via join command
	 */
	@Nullable
	UUID getInviter();
	
}
