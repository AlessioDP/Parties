package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface IPartyRenameEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the party that is getting renamed
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
	/**
	 * Get the new party name
	 *
	 * @return Returns the new name of the party
	 */
	@NonNull
	String getNewPartyName();
	
	/**
	 * Set the new name of the party
	 *
	 * @param name The party name to set
	 */
	void setNewPartyName(String name);
	
	/**
	 * Get the player who performed the command
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	PartyPlayer getPartyPlayer();
	
	/**
	 * Is it an admin renaming?
	 *
	 * @return Returns {@code true} if the rename has been performed by an admin
	 */
	boolean isAdmin();
	
}
