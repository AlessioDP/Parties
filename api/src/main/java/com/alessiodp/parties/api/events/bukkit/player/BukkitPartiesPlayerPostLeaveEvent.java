package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPlayerPostLeaveEvent extends BukkitPartiesEvent implements IPlayerPostLeaveEvent {
	private final PartyPlayer player;
	private final Party party;
	private final LeaveCause cause;
	private final PartyPlayer kicker;
	
	public BukkitPartiesPlayerPostLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker) {
		super(true);
		this.player = player;
		this.party = party;
		this.cause = cause;
		this.kicker = kicker;
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
	
	@NonNull
	@Override
	public LeaveCause getCause() {
		return cause;
	}
	
	@Nullable
	@Override
	public PartyPlayer getKicker() {
		return kicker;
	}
}