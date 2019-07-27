package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPreLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerPreLeaveEventHook extends BungeePartiesPlayerPreLeaveEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final boolean isKicked;
	private final PartyPlayer kickedBy;
	
	public PlayerPreLeaveEventHook(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		this.player = player;
		this.party = party;
		this.isKicked = isKicked;
		this.kickedBy = kickedBy;
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
	public boolean isKicked() {
		return isKicked;
	}
	
	@Nullable
	@Override
	public PartyPlayer getKicker() {
		return kickedBy;
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
