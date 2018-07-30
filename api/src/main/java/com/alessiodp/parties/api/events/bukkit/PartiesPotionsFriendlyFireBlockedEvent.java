package com.alessiodp.parties.api.events.bukkit;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jetbrains.annotations.NotNull;

public interface PartiesPotionsFriendlyFireBlockedEvent extends PartiesEvent, Cancellable {
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
	 * Gets the original Bukkit event handled by Parties
	 * 
	 * @return Returns the original {@link PotionSplashEvent}
	 */
	@NotNull
	PotionSplashEvent getOriginalEvent();
}
