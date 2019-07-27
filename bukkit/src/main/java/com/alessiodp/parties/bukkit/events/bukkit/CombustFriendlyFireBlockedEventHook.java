package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CombustFriendlyFireBlockedEventHook extends BukkitPartiesCombustFriendlyFireBlockedEvent {
	private boolean cancelled;
	private final PartyPlayer victim;
	private final PartyPlayer attacker;
	private final EntityCombustByEntityEvent originalEvent;
	
	public CombustFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
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
	
	@NonNull
	@Override
	public EntityCombustByEntityEvent getOriginalEvent() {
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
