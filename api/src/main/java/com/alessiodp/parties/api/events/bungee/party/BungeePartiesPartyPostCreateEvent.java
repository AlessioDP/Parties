package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPartyPostCreateEvent extends BungeePartiesEvent implements IPartyPostCreateEvent {
	private final PartyPlayer player;
	private final Party party;
	
	public BungeePartiesPartyPostCreateEvent(PartyPlayer player, Party party) {
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
