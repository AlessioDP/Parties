package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPartyPreExperienceDropEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the player who killed the entity, null if there isn't any
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getPlayer();
	
	/**
	 * Get the killed entity, null if doesn't exist.
	 * Only Bukkit handles killed entities.
	 * The object, if not null, its an instance of Bukkit Entity.
	 *
	 * @return the killed entity, intance of Bukkit Entity
	 */
	@Nullable Object getKilledEntity();
	
	/**
	 * Get the dropped experience
	 *
	 * @return the number of experience dropped
	 */
	double getExperience();
	
	/**
	 * Set the dropped experience
	 *
	 * @param experience the number of experience dropped
	 */
	void setExperience(double experience);
}
