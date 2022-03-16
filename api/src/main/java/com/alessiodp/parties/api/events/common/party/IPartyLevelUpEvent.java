package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.jetbrains.annotations.NotNull;

public interface IPartyLevelUpEvent extends PartiesEvent {
	/**
	 * Get the party that leveled up
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the new party level
	 *
	 * @return the new level of the party
	 */
	int getNewLevel();
}
