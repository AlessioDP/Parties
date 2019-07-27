package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.PotionSplashEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BukkitPartiesPotionsFriendlyFireBlockedEvent extends BukkitPartiesEvent implements Cancellable {
	
	public BukkitPartiesPotionsFriendlyFireBlockedEvent() {
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
	 * Gets the original Bukkit event handled by Parties
	 *
	 * @return Returns the original {@link PotionSplashEvent}
	 */
	@NonNull
	public abstract PotionSplashEvent getOriginalEvent();
}
