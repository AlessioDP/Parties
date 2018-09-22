package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FriendlyFireBlockedEventHook extends BukkitPartiesFriendlyFireBlockedEvent {
	private boolean cancelled;
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private EntityDamageByEntityEvent originalEvent;
	
	public FriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
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
