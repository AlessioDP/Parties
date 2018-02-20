package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPlayerLeaveEvent extends PartiesEventCancellable {
	@Deprecated private Player _player;
	private PartyPlayer player;
	private Party party;
	private boolean isKicked;
	private PartyPlayer kickedBy;
	
	@Deprecated
	public PartiesPlayerLeaveEvent(Player player, String party, boolean isKicked, UUID kickedBy) {
		_player = player;
		this.isKicked = isKicked;
	}
	public PartiesPlayerLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		this.player = player;
		this.party = party;
		_player = player != null ? Bukkit.getPlayer(player.getPlayerUUID()) : null;
		this.isKicked = isKicked;
		this.kickedBy = kickedBy;
	}
	
	/**
	 * Get the player that is leaveing the party
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
	 * Has the player been kicked?
	 * 
	 * @return Returns {@code true} if the player has been kicked
	 */
	public boolean isKicked() {
		return isKicked;
	}
	/**
	 * Get the kicker
	 * 
	 * @return Returns the {@link PartyPlayer} that has kicked the player, returns {@code null} if {@link #isKicked()} returns {@code false}
	 */
	public PartyPlayer getKicker() {
		return kickedBy;
	}
	

}
