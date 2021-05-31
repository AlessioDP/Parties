package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreHomeEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BungeePartiesPlayerPreHomeEvent extends BungeePartiesEvent implements IPlayerPreHomeEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final PartyHome home;
	
	public BungeePartiesPlayerPreHomeEvent(PartyPlayer player, Party party, PartyHome home) {
		this.player = player;
		this.party = party;
		this.home = home;
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
	public @NonNull PartyHome getHome() {
		return home;
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
