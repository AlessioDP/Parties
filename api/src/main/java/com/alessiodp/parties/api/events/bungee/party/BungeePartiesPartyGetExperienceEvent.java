package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@Override
	public double getExperience() {
		return experience;
	}
	
	@NonNull
	@Override
	public ExpSource getSource() {
		return source;
	}
	
	@Nullable
	@Override
	public PartyPlayer getKiller() {
		return killer;
	}
}
