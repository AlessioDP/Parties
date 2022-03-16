package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPlayerPostInviteEvent extends PartiesEvent {
	/**
	 * Get the invited player
	 *
	 * @return the {@link PartyPlayer}
	 */
	@NotNull PartyPlayer getInvitedPlayer();
	
	/**
	 * Get the inviter
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getInviter();
	
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
}
