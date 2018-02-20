package com.alessiodp.partiesapi.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;

import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPotionsFriendlyFireBlockedEvent extends PartiesEventCancellable {
	@Deprecated
	private Player _victim;
	@Deprecated
	private Player _attacker;
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private PotionSplashEvent originalEvent;
	
	@Deprecated
	public PartiesPotionsFriendlyFireBlockedEvent(Player victim, Player attacker, PotionSplashEvent originalEvent) {
		_victim = victim;
		_attacker = attacker;
		this.originalEvent = originalEvent;
	}
	
	public PartiesPotionsFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker,
			PotionSplashEvent originalEvent) {
		this.victim = victim;
		_victim = Bukkit.getPlayer(victim.getPlayerUUID());
		this.attacker = attacker;
		_attacker = Bukkit.getPlayer(attacker.getPlayerUUID());
		this.originalEvent = originalEvent;
	}
	
	/**
	 * Get the victim of the event
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	public PartyPlayer getPlayerVictim() {
		return victim;
	}
	
	/**
	 * @deprecated Replaced by {@link #getPlayerVictim()}
	 */
	@Deprecated
	public Player getVictim() {
		return _victim;
	}
	
	/**
	 * Get the attacker
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	public PartyPlayer getPlayerAttacker() {
		return attacker;
	}
	
	/**
	 * @deprecated Replaced by {@link #getPlayerAttacker()}
	 */
	@Deprecated
	public Player getAttacker() {
		return _attacker;
	}
	
	/**
	 * Gets the original Bukkit event handled by Parties
	 * 
	 * @return Returns the original {@link PotionSplashEvent}
	 */
	public PotionSplashEvent getOriginalEvent() {
		return originalEvent;
	}
}
