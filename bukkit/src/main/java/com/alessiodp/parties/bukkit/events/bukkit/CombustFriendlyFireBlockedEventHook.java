package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class CombustFriendlyFireBlockedEventHook extends BukkitPartiesCombustFriendlyFireBlockedEvent {
	private boolean cancelled;
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private EntityCombustByEntityEvent originalEvent;
	
	public CombustFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
		this.victim = victim;
		this.attacker = attacker;
		this.originalEvent = originalEvent;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPlayerVictim() {
		return victim;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPlayerAttacker() {
		return attacker;
	}
	
	@NotNull
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
