package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BungeePartiesPlayerPostInviteEvent extends BungeePartiesEvent implements IPlayerPostInviteEvent {
	private final PartyPlayer invitedPlayer;
	private final PartyPlayer inviter;
	private final Party party;
	
	public BungeePartiesPlayerPostInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		this.invitedPlayer = invitedPlayer;
		this.inviter = inviter;
		this.party = party;
	}
	
	@NonNull
	@Override
	public PartyPlayer getInvitedPlayer() {
		return invitedPlayer;
	}
	
	@Nullable
	@Override
	public PartyPlayer getInviter() {
		return inviter;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
}
