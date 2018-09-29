package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerPreJoinEventHook extends BungeePartiesPlayerPreJoinEvent {
	private boolean cancelled;
	private PartyPlayer player;
	private Party party;
	private boolean isInvited;
	private UUID invitedBy;
	
	public PlayerPreJoinEventHook(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		this.player = player;
		this.party = party;
		this.isInvited = isInvited;
		this.invitedBy = invitedBy;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@Override
	public boolean isInvited() {
		return isInvited;
	}
	
	@Nullable
	@Override
	public UUID getInviter() {
		return invitedBy;
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
