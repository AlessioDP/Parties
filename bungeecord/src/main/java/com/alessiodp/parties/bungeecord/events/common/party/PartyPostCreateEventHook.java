package com.alessiodp.parties.bungeecord.events.common.party;

import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PartyPostCreateEventHook extends BungeePartiesPartyPostCreateEvent {
	private final PartyPlayer player;
	private final Party party;
	
	public PartyPostCreateEventHook(PartyPlayer player, Party party) {
		this.player = player;
		this.party = party;
	}
	
	@NonNull
	@Override
	public PartyPlayer getCreator() {
		return player;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
}
