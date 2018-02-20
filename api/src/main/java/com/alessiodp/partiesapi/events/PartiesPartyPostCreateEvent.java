package com.alessiodp.partiesapi.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPartyPostCreateEvent extends PartiesEvent {
	@Deprecated
	private Player _player;
	@Deprecated
	private String _party;
	@Deprecated
	private boolean _fixed;
	private PartyPlayer player;
	private Party party;
	
	@Deprecated
	public PartiesPartyPostCreateEvent(Player player, String party, boolean fixed) {
		_player = player;
		_party = party;
		_fixed = fixed;
	}
	
	public PartiesPartyPostCreateEvent(PartyPlayer player, Party party) {
		this.player = player;
		_player = Bukkit.getPlayer(player.getPlayerUUID());
		this.party = party;
		_party = party.getName();
		_fixed = party.isFixed();
	}
	
	/**
	 * Get the creator of the party
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	public PartyPlayer getCreator() {
		return player;
	}
	
	/**
	 * @deprecated Replaced by {@link #getCreator()}
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
	 * @deprecated Replaced by {@link #getParty()}
	 */
	@Deprecated
	public boolean isFixed() {
		return _fixed;
	}
}
