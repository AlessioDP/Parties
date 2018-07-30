package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.bungeecord.events.implementations.BungeeAbstractEventCancellable;
import com.alessiodp.parties.api.events.player.PartiesChatEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class PartiesChatEventHook extends BungeeAbstractEventCancellable implements PartiesChatEvent {
	private PartyPlayer player;
	private Party party;
	private String message;
	
	public PartiesChatEventHook(PartyPlayer player, Party party, String message) {
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
}
