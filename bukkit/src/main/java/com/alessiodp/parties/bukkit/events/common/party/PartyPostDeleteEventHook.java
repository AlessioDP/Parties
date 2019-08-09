package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PartyPostDeleteEventHook extends BukkitPartiesPartyPostDeleteEvent {
	private final String party;
	private final DeleteCause cause;
	private final PartyPlayer kickedPlayer;
	private final PartyPlayer commandSender;
	
	public PartyPostDeleteEventHook(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		this.party = party;
		this.cause = cause;
		this.kickedPlayer = kickedPlayer;
		this.commandSender = commandSender;
	}
	
	@NonNull
	@Override
	public String getPartyName() {
		return party;
	}
	
	@NonNull
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
