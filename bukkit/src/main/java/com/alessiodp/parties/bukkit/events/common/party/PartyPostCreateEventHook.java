package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PartyPostCreateEventHook extends BukkitPartiesPartyPostCreateEvent {
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
