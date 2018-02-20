package com.alessiodp.partiesapi.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPartyRenameEvent extends PartiesEventCancellable {
	@Deprecated
	private String _party;
	private Party party;
	private String newName;
	@Deprecated
	private Player _player;
	private PartyPlayer player;
	private boolean isAdmin;
	
	@Deprecated
	public PartiesPartyRenameEvent(String party, String newName, Player player, boolean isAdmin) {
		_party = party;
		this.newName = newName;
		_player = player;
		this.isAdmin = isAdmin;
	}
	
	public PartiesPartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		this.party = party;
		_party = party.getName();
		this.newName = newName;
		this.player = player;
		_player = player != null ? Bukkit.getPlayer(player.getPlayerUUID()) : null;
		this.isAdmin = isAdmin;
	}
	
	/**
	 * Get the party that is getting renamed
	 * 
	 * @return Returns the {@link Party}
	 */
	public Party getParty() {
		return party;
	}
	
	/**
	 * @deprecated Replaced by {@link #getParty()}
	 */
	@Deprecated
	public String getPartyName() {
		return _party;
	}
	
	/**
	 * Get the new party name
	 * 
	 * @return Returns the new name of the party
	 */
	public String getNewPartyName() {
		return newName;
	}
	
	/**
	 * Set the new name of the party
	 * 
	 * @param name
	 *            The party name to set
	 */
	public void setNewPartyName(String name) {
		newName = name;
	}
	
	/**
	 * Get the player who performed the command
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
	 * Is it an admin renaming?
	 * 
	 * @return Returns {@code true} if the rename has been performed by an admin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
	
}
