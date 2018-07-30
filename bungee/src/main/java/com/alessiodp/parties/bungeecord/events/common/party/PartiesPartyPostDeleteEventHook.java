package com.alessiodp.parties.bungeecord.events.common.party;

import com.alessiodp.parties.bungeecord.events.implementations.BungeeAbstractEvent;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.party.PartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartiesPartyPostDeleteEventHook extends BungeeAbstractEvent implements PartiesPartyPostDeleteEvent {
	private String party;
	private DeleteCause cause;
	private PartyPlayer kickedPlayer;
	private PartyPlayer commandSender;
	
	public PartiesPartyPostDeleteEventHook(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
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
