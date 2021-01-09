package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPlayerPostJoinEvent extends BukkitPartiesEvent implements IPlayerPostJoinEvent {
	private final PartyPlayer player;
	private final Party party;
	private final JoinCause cause;
	private final PartyPlayer inviter;
	
	public BukkitPartiesPlayerPostJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter) {
		super(true);
		this.player = player;
		this.party = party;
		this.cause = cause;
		this.inviter = inviter;
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
	public JoinCause getCause() {
		return cause;
	}
	
	@Nullable
	@Override
	public PartyPlayer getInviter() {
		return inviter;
	}
}