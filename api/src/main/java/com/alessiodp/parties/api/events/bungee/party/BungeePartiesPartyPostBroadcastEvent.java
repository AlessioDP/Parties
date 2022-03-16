package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostBroadcastEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPartyPostBroadcastEvent extends BungeePartiesEvent implements IPartyPostBroadcastEvent {
	private final Party party;
	private final String message;
	private final PartyPlayer player;
	
	public BungeePartiesPartyPostBroadcastEvent(Party party, String message, PartyPlayer player) {
		this.party = party;
		this.message = message;
		this.player = player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @NotNull String getMessage() {
		return message;
	}
	
	@Override
	public @Nullable PartyPlayer getPartyPlayer() {
		return player;
	}
}
