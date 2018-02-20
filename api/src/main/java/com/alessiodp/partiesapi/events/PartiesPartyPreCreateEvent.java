package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPartyPreCreateEvent extends PartiesEventCancellable {
	@Deprecated
	private Player _player;
	@Deprecated
	private UUID _leader;
	private PartyPlayer player;
	private String party;
	private boolean fixed;
	
	@Deprecated
	public PartiesPartyPreCreateEvent(Player player, String party, boolean fixed) {
		_player = player;
		if (fixed)
			_leader = null;
		else
			_leader = player.getUniqueId();
		this.party = party;
		this.fixed = fixed;
	}
	
	public PartiesPartyPreCreateEvent(PartyPlayer player, String party, boolean fixed) {
		this.player = player;
		_player = player != null ? Bukkit.getPlayer(player.getPlayerUUID()) : null;
		_leader = (!fixed && _player != null) ? _player.getUniqueId() : null;
		this.party = party;
		this.fixed = fixed;
	}
	
	/**
	 * Get the player that is creating the party
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	/**
	 * @deprecated Replaced by {@link #getPartyPlayer()}
	 */
	@Deprecated
	public Player getPlayer() {
		return _player;
	}
	
	/**
	 * @deprecated Replaced by {@link #getPartyPlayer()}
	 */
	@Deprecated
	public UUID getLeader() {
		return _leader;
	}
	
	/**
	 * @deprecated Replaced by {@link #getPartyPlayer()}
	 */
	@Deprecated
	public void setLeader(UUID leader) {
		_leader = leader;
	}
	
	/**
	 * Get the name of the party
	 * 
	 * @return Returns the party name
	 */
	public String getPartyName() {
		return party;
	}
	
	/**
	 * Set a new name to the party
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setPartyName(String name) {
		party = name;
	}
	
	/**
	 * Is the party fixed?
	 * 
	 * @return Returns {@code true} if it's fixed
	 */
	public boolean isFixed() {
		return fixed;
	}
	
	/**
	 * Set if the party is fixed
	 * 
	 * @param fixed
	 *            {@code True} to be fixed
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
}
