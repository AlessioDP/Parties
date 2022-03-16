package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreExperienceDropEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPartyPreExperienceDropEvent extends BungeePartiesEvent implements IPartyPreExperienceDropEvent {
	private boolean cancelled;
	private final Party party;
	private final PartyPlayer player;
	private final Object killedEntity;
	private double experience;
	
	public BungeePartiesPartyPreExperienceDropEvent(Party party, PartyPlayer player, Object killedEntity, double experience) {
		cancelled = false;
		this.party = party;
		this.player = player;
		this.killedEntity = killedEntity;
		this.experience = experience;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @Nullable PartyPlayer getPlayer() {
		return player;
	}
	
	@Override
	public @Nullable Object getKilledEntity() {
		return killedEntity;
	}
	
	@Override
	public double getExperience() {
		return experience;
	}
	
	@Override
	public void setExperience(double experience) {
		this.experience = experience;
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
