package com.alessiodp.parties.api.events.velocity.player;

import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesPlayerPostChatEvent extends VelocityPartiesEvent implements IPlayerPostChatEvent {
	private final PartyPlayer player;
	private final Party party;
	private final String formattedMessage;
	private final String message;
	
	public VelocityPartiesPlayerPostChatEvent(PartyPlayer player, Party party, String formattedMessage, String message) {
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
