package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.events.bukkit.PartiesPotionsFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesPotionsFriendlyFireBlockedEventHook extends BukkitAbstractEventCancellable implements PartiesPotionsFriendlyFireBlockedEvent {
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private PotionSplashEvent originalEvent;
	
	public BukkitPartiesPotionsFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
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
}
