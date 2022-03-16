package com.alessiodp.parties.api.events.velocity.party;

import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesPartyLevelUpEvent extends VelocityPartiesEvent implements IPartyLevelUpEvent {
	private final Party party;
	private final int newLevel;
	
	public VelocityPartiesPartyLevelUpEvent(Party party, int newLevel) {
		this.party = party;
		this.newLevel = newLevel;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public int getNewLevel() {
		return newLevel;
	}
}
