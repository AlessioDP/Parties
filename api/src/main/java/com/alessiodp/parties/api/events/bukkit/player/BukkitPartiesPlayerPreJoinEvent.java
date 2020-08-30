package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPlayerPreJoinEvent extends BukkitPartiesEvent implements IPlayerPreJoinEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final PartyPlayer inviter;
	private final JoinCause cause;
	
	public BukkitPartiesPlayerPreJoinEvent(PartyPlayer player, Party party, PartyPlayer inviter, JoinCause cause) {
		super(false);
		this.player = player;
		this.party = party;
		this.inviter = inviter;
		this.cause = cause;
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
	
	@Nullable
	@Override
	public PartyPlayer getInviter() {
		return inviter;
	}
	
	@NonNull
	@Override
	public JoinCause getCause() {
		return cause;
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
