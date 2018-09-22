package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.api.events.bungee.player.BungeePartiesChatEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class ChatEventHook extends BungeePartiesChatEvent {
	private boolean cancelled;
	private PartyPlayer player;
	private Party party;
	private String message;
	
	public ChatEventHook(PartyPlayer player, Party party, String message) {
		this.player = player;
		this.party = party;
		this.message = message;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NotNull
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
