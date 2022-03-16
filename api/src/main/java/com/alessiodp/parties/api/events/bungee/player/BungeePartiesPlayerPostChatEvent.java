package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class BungeePartiesPlayerPostChatEvent extends BungeePartiesEvent implements IPlayerPostChatEvent {
	private final PartyPlayer player;
	private final Party party;
	private final String formattedMessage;
	private final String message;
	
	public BungeePartiesPlayerPostChatEvent(PartyPlayer player, Party party, String formattedMessage, String message) {
		this.player = player;
		this.party = party;
		this.formattedMessage = formattedMessage;
		this.message = message;
	}
	
	@Override
	public @NotNull PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @NotNull String getFormattedMessage() {
		return formattedMessage;
	}
	
	@Override
	public @NotNull String getMessage() {
		return message;
	}
}
