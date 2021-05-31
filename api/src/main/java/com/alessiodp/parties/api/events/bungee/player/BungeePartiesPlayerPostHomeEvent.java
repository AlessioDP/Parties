package com.alessiodp.parties.api.events.bungee.player;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostHomeEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BungeePartiesPlayerPostHomeEvent extends BungeePartiesEvent implements IPlayerPostHomeEvent {
	private final PartyPlayer player;
	private final Party party;
	private final PartyHome home;
	
	public BungeePartiesPlayerPostHomeEvent(PartyPlayer player, Party party, PartyHome home) {
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
}
