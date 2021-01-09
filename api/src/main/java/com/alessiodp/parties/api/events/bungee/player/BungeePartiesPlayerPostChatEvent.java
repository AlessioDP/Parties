package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BungeePartiesPlayerPostChatEvent extends BungeePartiesEvent implements IPlayerPostChatEvent {
	private final PartyPlayer player;
	private final Party party;
	private final String message;
	
	public BungeePartiesPlayerPostChatEvent(PartyPlayer player, Party party, String message) {
		this.player = player;
		this.party = party;
		this.message = message;
	}
	
	@NonNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NonNull
	@Override
	public String getMessage() {
		return message;
	}
}
