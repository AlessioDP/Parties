package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PartyRenameEventHook extends BukkitPartiesPartyRenameEvent {
	private boolean cancelled;
	private final Party party;
	private String newName;
	private final PartyPlayer player;
	private final boolean isAdmin;
	
	public PartyRenameEventHook(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		this.party = party;
		this.newName = newName;
		this.player = player;
		this.isAdmin = isAdmin;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NonNull
	@Override
	public String getNewPartyName() {
		return newName;
	}
	
	@Override
	public void setNewPartyName(String name) {
		newName = name;
	}
	
	@NonNull
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
