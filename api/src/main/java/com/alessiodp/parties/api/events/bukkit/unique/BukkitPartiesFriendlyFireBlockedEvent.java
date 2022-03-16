package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitPartiesFriendlyFireBlockedEvent extends BukkitPartiesEvent implements Cancellable {
	private boolean cancelled;
	private final PartyPlayer victim;
	private final PartyPlayer attacker;
	private final EntityDamageByEntityEvent originalEvent;
	
	public BukkitPartiesFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
		super(false);
		this.victim = victim;
		this.attacker = attacker;
		this.originalEvent = originalEvent;
	}
	
	/**
	 * Get the victim of the event
	 *
	 * @return the {@link PartyPlayer}
	 */
	public @NotNull PartyPlayer getPlayerVictim() {
		return victim;
	}
	
	/**
	 * Get the attacker
	 *
	 * @return the {@link PartyPlayer}
	 */
	public @NotNull PartyPlayer getPlayerAttacker() {
		return attacker;
	}
	
	/**
	 * Get the original Bukkit event handled by Parties
	 *
	 * @return the original {@link EntityDamageByEntityEvent}, or {@code null} if there is no bukkit event
	 */
	public @Nullable EntityDamageByEntityEvent getOriginalEvent() {
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
