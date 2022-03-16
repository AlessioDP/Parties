package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPartyGetExperienceEvent extends PartiesEvent {
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the experience
	 *
	 * @return the experience
	 */
	double getExperience();
	
	/**
	 * Get experience source
	 *
	 * @return the {@link ExpSource}
	 */
	@NotNull ExpSource getSource();
	
	/**
	 * Get the player who killed the mob
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getKiller();
	
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
