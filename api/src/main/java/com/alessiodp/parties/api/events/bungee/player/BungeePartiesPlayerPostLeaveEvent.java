package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
	
	@NonNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NonNull
	@Override
	public LeaveCause getCause() {
		return cause;
	}
	
	@Nullable
	@Override
	public PartyPlayer getKicker() {
		return kicker;
	}
}
