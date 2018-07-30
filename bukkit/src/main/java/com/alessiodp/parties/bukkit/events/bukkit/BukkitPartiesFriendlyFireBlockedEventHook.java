package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.events.bukkit.PartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitPartiesFriendlyFireBlockedEventHook extends BukkitAbstractEventCancellable implements PartiesFriendlyFireBlockedEvent {
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private EntityDamageByEntityEvent originalEvent;
	
	public BukkitPartiesFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
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
}
