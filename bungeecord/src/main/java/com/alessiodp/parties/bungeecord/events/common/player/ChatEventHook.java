package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.api.events.bungee.player.BungeePartiesChatEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ChatEventHook extends BungeePartiesChatEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private String message;
	
	public ChatEventHook(PartyPlayer player, Party party, String message) {
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
	
	@Override
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
