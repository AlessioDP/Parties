package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public interface IPlayerPreChatEvent extends PartiesEvent, Cancellable {
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
	 * Set the formatted message of the player. This message should contain %message% placeholder
	 *
	 * @param formattedMessage the formatted message to set
	 */
	void setFormattedMessage(String formattedMessage);
	
	/**
	 * Get the message of the player
	 *
	 * @return the message
	 */
	@NotNull String getMessage();
	
	/**
	 * Set the message of the player
	 *
	 * @param message the message to set
	 */
	void setMessage(String message);
}
