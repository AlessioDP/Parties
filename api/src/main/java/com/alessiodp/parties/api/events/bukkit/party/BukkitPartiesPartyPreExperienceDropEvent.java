package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitPartiesPartyPreExperienceDropEvent extends BukkitPartiesEvent implements Cancellable {
	private boolean cancelled;
	private final Party party;
	private final PartyPlayer player;
	private final Entity killedEntity;
	private int normalExperience;
	private int skillApiExperience;
	
	public BukkitPartiesPartyPreExperienceDropEvent(Party party, PartyPlayer player, Entity killedEntity, int normalExperience, int skillApiExperience) {
		super(false);
		cancelled = false;
		this.party = party;
		this.player = player;
		this.killedEntity = killedEntity;
		this.normalExperience = normalExperience;
		this.skillApiExperience = skillApiExperience;
	}
	
	@NonNull
	public PartyPlayer getPlayer() {
		return player;
	}
	
	@NonNull
	public Party getParty() {
		return party;
	}
	
	@NonNull
	public Entity getKilledEntity() {
		return killedEntity;
	}
	
	public int getNormalExperience() {
		return normalExperience;
	}
	
	public void setNormalExperience(int normalExperience) {
		this.normalExperience = normalExperience;
	}
	
	public int getSkillApiExperience() {
		return skillApiExperience;
	}
	
	public void setSkillApiExperience(int skillApiExperience) {
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
