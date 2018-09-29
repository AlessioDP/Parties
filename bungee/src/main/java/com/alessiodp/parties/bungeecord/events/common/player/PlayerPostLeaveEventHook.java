package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPostLeaveEventHook extends BungeePartiesPlayerPostLeaveEvent {
	private PartyPlayer player;
	private Party party;
	private boolean isKicked;
	private PartyPlayer kickedBy;
	
	public PlayerPostLeaveEventHook(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
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
}
