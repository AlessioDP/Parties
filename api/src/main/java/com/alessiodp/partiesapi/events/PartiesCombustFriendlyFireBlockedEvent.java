package com.alessiodp.partiesapi.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesCombustFriendlyFireBlockedEvent extends PartiesEventCancellable {
	@Deprecated
	private Player _victim;
	@Deprecated
	private Player _attacker;
	private PartyPlayer victim;
	private PartyPlayer attacker;
	private EntityCombustByEntityEvent originalEvent;
	
	@Deprecated
	public PartiesCombustFriendlyFireBlockedEvent(Player victim, Player attacker,
			EntityCombustByEntityEvent originalEvent) {
		this._victim = victim;
		this._attacker = attacker;
		this.originalEvent = originalEvent;
	}
	
	public PartiesCombustFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker,
			EntityCombustByEntityEvent originalEvent) {
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
	 * Get the original Bukkit event handled by Parties
	 * 
	 * @return Returns the original {@link EntityCombustByEntityEvent}
	 */
	public EntityCombustByEntityEvent getOriginalEvent() {
		return originalEvent;
	}
}
