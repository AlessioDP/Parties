package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IPartyPostRenameEvent extends PartiesEvent {
	/**
	 * Get the renamed party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
	/**
	 * Get the old party name
	 *
	 * @return Returns the old name of the party
	 */
	@Nullable
	String getOldPartyName();
	
	/**
	 * Get the new party name
	 *
	 * @return Returns the new name of the party
	 */
	@Nullable
	String getNewPartyName();
	
	/**
	 * Get the player who performed the command
	 *
	 * @return Returns the {@link PartyPlayer} or null if not executed by a player
	 */
	@Nullable
	PartyPlayer getPartyPlayer();
	
	/**
	 * Is it an admin renaming?
	 *
	 * @return Returns {@code true} if the rename has been performed by an admin
	 */
	boolean isAdmin();
	
}
