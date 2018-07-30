package com.alessiodp.parties.bukkit.events.bukkit;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.events.bukkit.PartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesCombustFriendlyFireBlockedEventHook extends BukkitAbstractEventCancellable implements PartiesCombustFriendlyFireBlockedEvent {
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private EntityCombustByEntityEvent originalEvent;
	
	public BukkitPartiesCombustFriendlyFireBlockedEventHook(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
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
}
