package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public interface IPlayerPostChatEvent extends PartiesEvent {
	/**
	 * Get the player who sent the message
	 *
	 * @return the {@link PartyPlayer}
	 */
	@NotNull PartyPlayer getPartyPlayer();
	
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the formatted message of the player
	 *
	 * @return the formatted message
	 */
	@NotNull String getFormattedMessage();
	
	/**
	 * Get the message of the player
	 *
	 * @return the message
	 */
	@NotNull String getMessage();
}
