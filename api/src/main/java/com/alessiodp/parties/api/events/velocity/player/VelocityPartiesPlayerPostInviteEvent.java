package com.alessiodp.parties.api.events.velocity.player;

import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelocityPartiesPlayerPostInviteEvent extends VelocityPartiesEvent implements IPlayerPostInviteEvent {
	private final PartyPlayer invitedPlayer;
	private final PartyPlayer inviter;
	private final Party party;
	
	public VelocityPartiesPlayerPostInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		this.invitedPlayer = invitedPlayer;
		this.inviter = inviter;
		this.party = party;
	}
	
	@Override
	public @NotNull PartyPlayer getInvitedPlayer() {
		return invitedPlayer;
	}
	
	@Override
	public @Nullable PartyPlayer getInviter() {
		return inviter;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
}
