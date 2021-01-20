package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.Cancellable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPlayerPreJoinEvent extends BukkitPartiesEvent implements IPlayerPreJoinEvent, Cancellable {
	private boolean cancelled;
	private final PartyPlayer player;
	private final Party party;
	private final JoinCause cause;
	private final PartyPlayer inviter;
	
	public BukkitPartiesPlayerPreJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter) {
		super(false);
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
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
