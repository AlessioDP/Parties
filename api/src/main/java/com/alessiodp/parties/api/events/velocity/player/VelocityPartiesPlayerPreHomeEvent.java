package com.alessiodp.parties.api.events.velocity.player;

import com.alessiodp.parties.api.events.common.player.IPlayerPreHomeEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesPlayerPreHomeEvent extends VelocityPartiesEvent implements IPlayerPreHomeEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final PartyHome home;
	
	public VelocityPartiesPlayerPreHomeEvent(PartyPlayer player, Party party, PartyHome home) {
		this.player = player;
		this.party = party;
		this.home = home;
	}
	
	@Override
	public @NotNull PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @NotNull PartyHome getHome() {
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
