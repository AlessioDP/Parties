package com.alessiodp.parties.bungeecord.events.common.party;

import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartyPostDeleteEventHook extends BungeePartiesPartyPostDeleteEvent {
	private String party;
	private DeleteCause cause;
	private PartyPlayer kickedPlayer;
	private PartyPlayer commandSender;
	
	public PartyPostDeleteEventHook(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		this.party = party;
		this.cause = cause;
		this.kickedPlayer = kickedPlayer;
		this.commandSender = commandSender;
	}
	
	@NotNull
	@Override
	public String getPartyName() {
		return party;
	}
	
	@NotNull
	@Override
	public DeleteCause getCause() {
		return cause;
	}
	
	@Nullable
	@Override
	public PartyPlayer getKickedPlayer() {
		return kickedPlayer;
	}
	
	@Nullable
	@Override
	public PartyPlayer getCommandSender() {
		return commandSender;
	}
}
