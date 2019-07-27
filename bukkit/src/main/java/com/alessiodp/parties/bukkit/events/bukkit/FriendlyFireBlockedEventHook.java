package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FriendlyFireBlockedEventHook extends BukkitPartiesFriendlyFireBlockedEvent {
	private boolean cancelled;
	private final PartyPlayer victim;
	private final PartyPlayer attacker;
	private final EntityDamageByEntityEvent originalEvent;
	
	public FriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
		this.victim = victim;
		this.attacker = attacker;
		this.originalEvent = originalEvent;
	}
	
	@NonNull
	@Override
	public PartyPlayer getPlayerVictim() {
		return victim;
	}
	
	@NonNull
	@Override
	public PartyPlayer getPlayerAttacker() {
		return attacker;
	}
	
	@Nullable
	@Override
	public EntityDamageByEntityEvent getOriginalEvent() {
		return originalEvent;
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
