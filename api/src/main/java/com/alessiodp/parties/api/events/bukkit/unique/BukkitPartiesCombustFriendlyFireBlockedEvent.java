package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BukkitPartiesCombustFriendlyFireBlockedEvent extends BukkitPartiesEvent implements Cancellable {
	
	public BukkitPartiesCombustFriendlyFireBlockedEvent() {
		super(false);
	}
	
	/**
	 * Get the victim of the event
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	public abstract PartyPlayer getPlayerVictim();
	
	/**
	 * Get the attacker
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	public abstract PartyPlayer getPlayerAttacker();
	
	/**
	 * Get the original Bukkit event handled by Parties
	 *
	 * @return Returns the original {@link EntityCombustByEntityEvent}
	 */
	@NonNull
	public abstract EntityCombustByEntityEvent getOriginalEvent();
}
