package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPartyGetExperienceEvent extends BungeePartiesEvent implements IPartyGetExperienceEvent {
	private final Party party;
	private final double experience;
	
	private final ExpSource source;
	private final PartyPlayer killer;
	
	public BungeePartiesPartyGetExperienceEvent(Party party, double experience, PartyPlayer killer) {
		this.party = party;
		this.experience = experience;
		source = ExpSource.KILL;
		this.killer = killer;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public double getExperience() {
		return experience;
	}
	
	@Override
	public @NotNull ExpSource getSource() {
		return source;
	}
	
	@Override
	public @Nullable PartyPlayer getKiller() {
		return killer;
	}
}
