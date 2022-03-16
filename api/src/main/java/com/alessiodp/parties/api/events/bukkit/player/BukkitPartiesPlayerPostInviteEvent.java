package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitPartiesPlayerPostInviteEvent extends BukkitPartiesEvent implements IPlayerPostInviteEvent {
	private final PartyPlayer invitedPlayer;
	private final PartyPlayer inviter;
	private final Party party;
	
	public BukkitPartiesPlayerPostInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		super(true);
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
