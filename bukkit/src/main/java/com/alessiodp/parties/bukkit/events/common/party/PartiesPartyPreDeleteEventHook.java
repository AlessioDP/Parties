package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.party.PartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartiesPartyPreDeleteEventHook extends BukkitAbstractEventCancellable implements PartiesPartyPreDeleteEvent {
	private Party party;
	private DeleteCause cause;
	private PartyPlayer kickedPlayer;
	private PartyPlayer commandSender;
	
	public PartiesPartyPreDeleteEventHook(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
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
}
