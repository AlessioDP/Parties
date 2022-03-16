package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPlayerPostLeaveEvent extends BungeePartiesEvent implements IPlayerPostLeaveEvent {
	private final PartyPlayer player;
	private final Party party;
	private final LeaveCause cause;
	private final PartyPlayer kicker;
	
	public BungeePartiesPlayerPostLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker) {
		this.player = player;
		this.party = party;
		this.cause = cause;
		this.kicker = kicker;
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
	public @NotNull LeaveCause getCause() {
		return cause;
	}
	
	@Override
	public @Nullable PartyPlayer getKicker() {
		return kicker;
	}
}
