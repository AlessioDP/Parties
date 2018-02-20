package com.alessiodp.partiesapi.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesChatEvent extends PartiesEventCancellable {
	@Deprecated
	private Player _player;
	@Deprecated
	private String _partyName;
	private PartyPlayer player;
	private Party party;
	private String message;
	
	@Deprecated
	public PartiesChatEvent(Player player, String partyName, String message) {
		_player = player;
		_partyName = partyName;
		this.message = message;
	}
	
	public PartiesChatEvent(PartyPlayer player, Party party, String message) {
		this.player = player;
		_player = Bukkit.getPlayer(player.getPlayerUUID());
		this.party = party;
		_partyName = party.getName();
		this.message = message;
	}
	
	/**
	 * Get the player who sent the message
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
	 * Get the party
	 * 
	 * @return Returns the the {@link Party}
	 */
	public Party getParty() {
		return party;
	}
	
	/**
	 * @deprecated Replaced by {@link #getParty()}
	 */
	@Deprecated
	public String getPartyName() {
		return _partyName;
	}
	
	/**
	 * Get the message of the player
	 * 
	 * @return Returns the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the message of the player
	 * 
	 * @param message The
	 *            message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
