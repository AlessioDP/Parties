package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPartyPreCreateEvent extends BukkitPartiesEvent implements IPartyPreCreateEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private String name;
	private String tag;
	private boolean fixed;
	
	public BukkitPartiesPartyPreCreateEvent(PartyPlayer player, String name, String tag, boolean fixed) {
		super(false);
		cancelled = false;
		this.player = player;
		this.name = name;
		this.tag = tag;
		this.fixed = fixed;
	}
	
	@Nullable
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NonNull
	@Override
	public String getPartyName() {
		return name;
	}
	
	@Nullable
	@Override
	public String getPartyTag() {
		return tag;
	}
	
	@Override
	public void setPartyName(@NonNull String name) {
		this.name = name;
	}
	
	@Override
	public void setPartyTag(@Nullable String tag) {
		this.tag = tag;
	}
	
	@Override
	public boolean isFixed() {
		return fixed;
	}
	
	@Override
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
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
