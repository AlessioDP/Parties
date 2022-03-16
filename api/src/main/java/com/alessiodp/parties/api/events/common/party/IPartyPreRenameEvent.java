package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPartyPreRenameEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the party that is getting renamed
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the old party name
	 *
	 * @return the old name of the party
	 */
	@Nullable String getOldPartyName();
	
	/**
	 * Get the new party name
	 *
	 * @return the new name of the party
	 */
	@Nullable String getNewPartyName();
	
	/**
	 * Set the new name of the party
	 *
	 * @param name the party name to set
	 */
	void setNewPartyName(String name);
	
	/**
	 * Get the player who performed the command
	 *
	 * @return the {@link PartyPlayer} or null if not executed by a player
	 */
	@Nullable PartyPlayer getPartyPlayer();
	
	/**
	 * Is it an admin renaming?
	 *
	 * @return {@code true} if the rename has been performed by an admin
	 */
	boolean isAdmin();
	
}
