package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreExperienceDropEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.entity.Entity;

/**
 * @deprecated Use {@link BukkitPartiesPartyPreExperienceDropEvent} instead
 */
@Deprecated
public class BukkitPartiesPreExperienceDropEvent extends BukkitPartiesPartyPreExperienceDropEvent {
	public BukkitPartiesPreExperienceDropEvent(Party party, PartyPlayer player, Entity killedEntity, double normalExperience, double skillApiExperience) {
		super(party, player, killedEntity, normalExperience);
	}
	
	/**
	 * Get the vanilla dropped experience
	 *
	 * @deprecated use {@link BukkitPartiesPartyPreExperienceDropEvent} instead
	 * @return the number of vanilla experience dropped
	 */
	@Deprecated
	public double getNormalExperience() {
		return getExperience();
	}
	
	/**
	 * Set the vanilla dropped experience
	 *
	 * @deprecated use {@link BukkitPartiesPartyPreExperienceDropEvent} instead
	 * @param normalExperience the number of vanilla experience dropped
	 */
	@Deprecated
	public void setNormalExperience(double normalExperience) {
		setExperience(normalExperience);
	}
	
	/**
	 * Get the SkillAPI dropped experience
	 *
	 * @deprecated use {@link BukkitPartiesPartyPreExperienceDropEvent} instead
	 * @return the number of SkillAPI experience dropped
	 */
	@Deprecated
	public double getSkillApiExperience() {
		return 0; // Deprecated
	}
	
	/**
	 * Set the SkillAPI dropped experience
	 *
	 * @deprecated use {@link BukkitPartiesPartyPreExperienceDropEvent} instead
	 * @param skillApiExperience the number of SkillAPI experience dropped
	 */
	@Deprecated
	public void setSkillApiExperience(double skillApiExperience) {
		// Deprecated
	}
}
