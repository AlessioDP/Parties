package com.alessiodp.parties.bungeecord.events.common.party;

import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class PartyRenameEventHook extends BungeePartiesPartyRenameEvent {
	private boolean cancelled;
	private Party party;
	private String newName;
	private PartyPlayer player;
	private boolean isAdmin;
	
	public PartyRenameEventHook(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		this.party = party;
		this.newName = newName;
		this.player = player;
		this.isAdmin = isAdmin;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NotNull
	@Override
	public String getNewPartyName() {
		return newName;
	}
	
	@Override
	public void setNewPartyName(String name) {
		newName = name;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public boolean isAdmin() {
		return isAdmin;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
