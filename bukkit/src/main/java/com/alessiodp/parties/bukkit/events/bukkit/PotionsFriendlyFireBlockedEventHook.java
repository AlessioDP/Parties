package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPotionsFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.PotionSplashEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PotionsFriendlyFireBlockedEventHook extends BukkitPartiesPotionsFriendlyFireBlockedEvent {
	private boolean cancelled;
	private final PartyPlayer victim;
	private final PartyPlayer attacker;
	private final PotionSplashEvent originalEvent;
	
	public PotionsFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
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
	public PotionSplashEvent getOriginalEvent() {
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
