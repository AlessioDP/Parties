package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BungeePartiesPlayerPostJoinEvent extends BungeePartiesEvent implements IPlayerPostJoinEvent {
	private final PartyPlayer player;
	private final Party party;
	private final PartyPlayer inviter;
	private final JoinCause cause;
	
	public BungeePartiesPlayerPostJoinEvent(PartyPlayer player, Party party, PartyPlayer inviter, JoinCause cause) {
		this.player = player;
		this.party = party;
		this.inviter = inviter;
		this.cause = cause;
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
	
	@Nullable
	@Override
	public PartyPlayer getInviter() {
		return inviter;
	}
	
	@NonNull
	@Override
	public JoinCause getCause() {
		return cause;
	}
}
