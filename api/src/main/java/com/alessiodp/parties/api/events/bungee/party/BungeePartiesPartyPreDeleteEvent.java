package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPartyPreDeleteEvent extends BungeePartiesEvent implements IPartyPreDeleteEvent {
	private boolean cancelled;
	private final Party party;
	private final DeleteCause cause;
	private final PartyPlayer kickedPlayer;
	private final PartyPlayer commandSender;
	
	public BungeePartiesPartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		this.party = party;
		this.cause = cause;
		this.kickedPlayer = kickedPlayer;
		this.commandSender = commandSender;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @NotNull DeleteCause getCause() {
		return cause;
	}
	
	@Override
	public @Nullable PartyPlayer getKickedPlayer() {
		return kickedPlayer;
	}
	
	@Override
	public @Nullable PartyPlayer getCommandSender() {
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
