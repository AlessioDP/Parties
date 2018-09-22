package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPotionsFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jetbrains.annotations.NotNull;

public class PotionsFriendlyFireBlockedEventHook extends BukkitPartiesPotionsFriendlyFireBlockedEvent {
	private boolean cancelled;
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private PotionSplashEvent originalEvent;
	
	public PotionsFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
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
