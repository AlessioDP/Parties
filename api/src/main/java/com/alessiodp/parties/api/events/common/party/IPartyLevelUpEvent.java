package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface IPartyLevelUpEvent extends PartiesEvent {
	/**
	 * Get the party that leveled up
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
	/**
	 * Get the new party level
	 *
	 * @return Returns the new level of the party
	 */
	int getNewLevel();
	
}
