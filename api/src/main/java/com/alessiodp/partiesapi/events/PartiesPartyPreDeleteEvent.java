package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class PartiesPartyPreDeleteEvent extends PartiesEventCancellable {
	@Deprecated
	private String _party;
	private Party party;
	private DeleteCause cause;
	@Deprecated private UUID _player;
	@Deprecated
	private Player _commandSender;
	private PartyPlayer kickedPlayer;
	private PartyPlayer commandSender;
	
	@Deprecated
	public PartiesPartyPreDeleteEvent(String party, DeleteCause cause, UUID player, Player commandSender) {
		_party = party;
		this.cause = cause;
		_player = player;
		_commandSender = commandSender;
	}
	
	public PartiesPartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		this.party = party;
		_party = party.getName();
		this.cause = cause;
		this.kickedPlayer = kickedPlayer;
		_player = kickedPlayer != null ? kickedPlayer.getPlayerUUID() : null;
		this.commandSender = commandSender;
		_commandSender = commandSender != null ? Bukkit.getPlayer(commandSender.getPlayerUUID()) : null;
	}
	
	/**
	 * Get the party that is getting cancelled
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
		return _party;
	}
	
	/**
	 * Get the cause of delete
	 * 
	 * @return Returns the {@link DeleteCause} of the delete
	 */
	public DeleteCause getCause() {
		return cause;
	}
	
	/**
	 * Get the kicked player
	 * 
	 * @return Returns the {@link PartyPlayer} of the kicked player, returns {@code null}
	 *         if the delete cause is {@link DeleteCause#DELETE} or
	 *         {@link DeleteCause#TIMEOUT}
	 */
	public PartyPlayer getKickedPlayer() {
		return kickedPlayer;
	}
	/**
	 * @deprecated Replaced by {@link #getParty()}
	 */
	@Deprecated
	public UUID getPlayer() {
		return _player;
	}
	
	/**
	 * Get the player who performed the command
	 * 
	 * @return Returns the {@link PartyPlayer} who did the command, returns
	 *         {@code null} if the delete cause is {@link DeleteCause#TIMEOUT}
	 */
	public PartyPlayer getCommandSender() {
		return commandSender;
	}
	
	/**
	 * @deprecated Replaced by {@link #getCommandSender()}
	 */
	@Deprecated
	public Player getSender() {
		return _commandSender;
	}
	
	public enum DeleteCause {
		@Deprecated JOIN, LEAVE, KICK, DELETE, BAN, TIMEOUT;
	}
}
