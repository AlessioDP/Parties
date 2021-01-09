package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IPartyGetExperienceEvent extends PartiesEvent {
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
	/**
	 * Get the experience
	 *
	 * @return Returns the experience
	 */
	double getExperience();
	
	/**
	 * Get experience source
	 *
	 * @return The {@link ExpSource}
	 */
	@NonNull
	ExpSource getSource();
	
	/**
	 * Get the player who killed the mob
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@Nullable
	PartyPlayer getKiller();
	
	enum ExpSource {
		/**
		 * Killed entity by a player
		 */
		KILL,
		/**
		 * External sources like plugin API
		 */
		API
	}
}
