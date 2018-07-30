package com.alessiodp.parties.bungeecord.events.common.player;

import com.alessiodp.parties.bungeecord.events.implementations.BungeeAbstractEventCancellable;
import com.alessiodp.parties.api.events.player.PartiesPlayerLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartiesPlayerLeaveEventHook extends BungeeAbstractEventCancellable implements PartiesPlayerLeaveEvent {
	private PartyPlayer player;
	private Party party;
	private boolean isKicked;
	private PartyPlayer kickedBy;
	
	public PartiesPlayerLeaveEventHook(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
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
