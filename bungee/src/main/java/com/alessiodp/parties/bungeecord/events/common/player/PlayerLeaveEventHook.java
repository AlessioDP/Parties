package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerLeaveEventHook extends BungeePartiesPlayerLeaveEvent {
	private boolean cancelled;
	private PartyPlayer player;
	private Party party;
	private boolean isKicked;
	private PartyPlayer kickedBy;
	
	public PlayerLeaveEventHook(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		this.player = player;
		this.party = party;
		this.isKicked = isKicked;
		this.kickedBy = kickedBy;
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
