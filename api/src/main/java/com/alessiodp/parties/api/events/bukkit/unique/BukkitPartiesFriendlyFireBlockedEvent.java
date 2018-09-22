package com.alessiodp.parties.api.events.bukkit.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BukkitPartiesFriendlyFireBlockedEvent extends BukkitPartiesEvent implements Cancellable {
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
	 * @return Returns the original {@link EntityDamageByEntityEvent}, or {@code null} if there is no bukkit event
	 */
	@Nullable
	public abstract EntityDamageByEntityEvent getOriginalEvent();
}
