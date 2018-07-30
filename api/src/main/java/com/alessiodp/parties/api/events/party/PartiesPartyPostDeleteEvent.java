package com.alessiodp.parties.api.events.party;

import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface PartiesPartyPostDeleteEvent extends PartiesEvent {
	/**
	 * Get the deleted party name
	 * 
	 * @return Returns the name of the deleted party
	 */
	@NotNull
	String getPartyName();
	
	/**
	 * Get the delete cause
	 * 
	 * @return Returns the {@link DeleteCause} of the delete
	 */
	@NotNull
	DeleteCause getCause();
	
	/**
	 * Get the kicked player
	 * 
	 * @return Returns the {@link PartyPlayer} of the kicked player, returns
	 *         {@code null} if the delete cause is {@link DeleteCause#DELETE} or
	 *         {@link DeleteCause#TIMEOUT}
	 */
	@Nullable
	PartyPlayer getKickedPlayer();
	
	/**
	 * Get the player who performed the command
	 * 
	 * @return Returns the {@link PartyPlayer} who did the command, returns
	 *         {@code null} if the delete cause is {@link DeleteCause#TIMEOUT}
	 */
	@Nullable
	PartyPlayer getCommandSender();
}
