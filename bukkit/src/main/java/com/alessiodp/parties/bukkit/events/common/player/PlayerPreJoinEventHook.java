package com.alessiodp.parties.bukkit.events.common.player;

import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class PlayerPreJoinEventHook extends BukkitPartiesPlayerPreJoinEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final boolean isInvited;
	private final UUID invitedBy;
	
	public PlayerPreJoinEventHook(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		this.player = player;
		this.party = party;
		this.isInvited = isInvited;
		this.invitedBy = invitedBy;
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
