package com.alessiodp.parties.bungeecord.events.common.party;

import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartyPreDeleteEventHook extends BungeePartiesPartyPreDeleteEvent {
	private boolean cancelled;
	private Party party;
	private DeleteCause cause;
	private PartyPlayer kickedPlayer;
	private PartyPlayer commandSender;
	
	public PartyPreDeleteEventHook(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		this.party = party;
		this.cause = cause;
		this.kickedPlayer = kickedPlayer;
		this.commandSender = commandSender;
	}
	
	@NotNull
	@Override
	public Party getParty() {
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
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
