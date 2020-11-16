package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitPartiesPreExperienceDropEvent extends BukkitPartiesEvent implements Cancellable {
	private boolean cancelled;
	private final Party party;
	private final PartyPlayer player;
	private final Entity killedEntity;
	private double normalExperience;
	private double skillApiExperience;
	
	public BukkitPartiesPreExperienceDropEvent(Party party, PartyPlayer player, Entity killedEntity, double normalExperience, double skillApiExperience) {
		super(false);
		cancelled = false;
		this.party = party;
		this.player = player;
		this.killedEntity = killedEntity;
		this.normalExperience = normalExperience;
		this.skillApiExperience = skillApiExperience;
	}
	
	/**
	 * Get the player who killed the mob
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	public PartyPlayer getPlayer() {
		return player;
	}
	
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	public Party getParty() {
		return party;
	}
	
	/**
	 * Get the killed entity
	 *
	 * @return The killed {@link Entity}
	 */
	@NonNull
	public Entity getKilledEntity() {
		return killedEntity;
	}
	
	/**
	 * Get the vanilla dropped experience
	 *
	 * @return The number of vanilla experience dropped
	 */
	public double getNormalExperience() {
		return normalExperience;
	}
	
	/**
	 * Set the vanilla dropped experience
	 *
	 * @param normalExperience The number of vanilla experience dropped
	 */
	public void setNormalExperience(double normalExperience) {
		this.normalExperience = normalExperience;
	}
	
	/**
	 * Get the SkillAPI dropped experience
	 *
	 * @return The number of SkillAPI experience dropped
	 */
	public double getSkillApiExperience() {
		return skillApiExperience;
	}
	
	/**
	 * Set the SkillAPI dropped experience
	 *
	 * @param skillApiExperience The number of SkillAPI experience dropped
	 */
	public void setSkillApiExperience(double skillApiExperience) {
		this.skillApiExperience = skillApiExperience;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
