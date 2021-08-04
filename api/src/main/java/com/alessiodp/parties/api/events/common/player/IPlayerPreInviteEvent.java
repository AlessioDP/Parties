package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPlayerPreInviteEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the invited player
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
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
	@NotNull
	Party getParty();
	
}
