package com.alessiodp.parties.api.events.velocity.party;

import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelocityPartiesPartyGetExperienceEvent extends VelocityPartiesEvent implements IPartyGetExperienceEvent {
	private final Party party;
	private final double experience;
	
	private final ExpSource source;
	private final PartyPlayer killer;
	
	public VelocityPartiesPartyGetExperienceEvent(Party party, double experience, PartyPlayer killer) {
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
