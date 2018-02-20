package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPlayerJoinEvent extends PartiesEventCancellable {
	@Deprecated
	private Player _player;
	@Deprecated
	private String _party;
	private PartyPlayer player;
	private Party party;
	private boolean isInvited;
	private UUID invitedBy;
	
	@Deprecated
	public PartiesPlayerJoinEvent(Player player, String party, boolean isInvited, UUID invitedBy) {
		_player = player;
		_party = party;
		this.isInvited = isInvited;
		this.invitedBy = invitedBy;
	}
	
	public PartiesPlayerJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		this.player = player;
		_player = player != null ? Bukkit.getPlayer(player.getPlayerUUID()) : null;
		this.party = party;
		_party = party.getName();
		this.isInvited = isInvited;
		this.invitedBy = invitedBy;
	}
	
	/**
	 * Get the player that is joining inside the party
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
	 * Has been invited?
	 * 
	 * @return Returns {@code true} if the player has been invited
	 */
	public boolean isInvited() {
		return isInvited;
	}
	
	/**
	 * Get the inviter
	 * 
	 * @return Returns the {@code UUID} of the inviter
	 */
	public UUID getInviter() {
		return invitedBy;
	}
	
}
