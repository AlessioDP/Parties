package com.alessiodp.parties.bukkit.events.common.player;

import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerPostLeaveEventHook extends BukkitPartiesPlayerPostLeaveEvent {
	private final PartyPlayer player;
	private final Party party;
	private final boolean isKicked;
	private final PartyPlayer kickedBy;
	
	public PlayerPostLeaveEventHook(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
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
}
