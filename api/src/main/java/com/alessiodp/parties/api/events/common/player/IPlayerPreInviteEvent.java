package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IPlayerPreInviteEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the invited player
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	PartyPlayer getInvitedPlayer();
	
	/**
	 * Get the inviter
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@Nullable
	PartyPlayer getInviter();
	
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
}
