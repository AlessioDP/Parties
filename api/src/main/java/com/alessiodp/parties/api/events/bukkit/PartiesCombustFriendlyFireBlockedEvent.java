package com.alessiodp.parties.api.events.bukkit;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.jetbrains.annotations.NotNull;

public interface PartiesCombustFriendlyFireBlockedEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the victim of the event
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	PartyPlayer getPlayerVictim();
	
	/**
	 * Get the attacker
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	PartyPlayer getPlayerAttacker();
	
	/**
	 * Get the original Bukkit event handled by Parties
	 * 
	 * @return Returns the original {@link EntityCombustByEntityEvent}
	 */
	@NotNull
	EntityCombustByEntityEvent getOriginalEvent();
}
