package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEvent;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.party.PartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartiesPartyPostDeleteEventHook extends BukkitAbstractEvent implements PartiesPartyPostDeleteEvent {
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
