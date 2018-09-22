package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitPartiesCombustFriendlyFireBlockedEvent extends BukkitPartiesEvent implements Cancellable {
	/**
	 * Get the victim of the event
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	public abstract PartyPlayer getPlayerVictim();
	
	/**
	 * Get the attacker
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	public abstract PartyPlayer getPlayerAttacker();
	
	/**
	 * Get the original Bukkit event handled by Parties
	 * 
	 * @return Returns the original {@link EntityCombustByEntityEvent}
	 */
	@NotNull
	public abstract EntityCombustByEntityEvent getOriginalEvent();
}
