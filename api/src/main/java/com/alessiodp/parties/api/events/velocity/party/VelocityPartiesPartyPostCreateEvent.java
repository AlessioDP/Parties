package com.alessiodp.parties.api.events.velocity.party;

import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelocityPartiesPartyPostCreateEvent extends VelocityPartiesEvent implements IPartyPostCreateEvent {
	private final PartyPlayer player;
	private final Party party;
	
	public VelocityPartiesPartyPostCreateEvent(PartyPlayer player, Party party) {
		this.player = player;
		this.party = party;
	}
	
	@Override
	public @Nullable PartyPlayer getCreator() {
		return player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
}
