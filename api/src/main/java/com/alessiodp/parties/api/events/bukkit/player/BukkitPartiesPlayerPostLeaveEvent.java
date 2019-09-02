package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPlayerPostLeaveEvent extends BukkitPartiesEvent implements IPlayerPostLeaveEvent {
	private final PartyPlayer player;
	private final Party party;
	private final boolean isKicked;
	private final PartyPlayer kickedBy;
	
	public BukkitPartiesPlayerPostLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		super(true);
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