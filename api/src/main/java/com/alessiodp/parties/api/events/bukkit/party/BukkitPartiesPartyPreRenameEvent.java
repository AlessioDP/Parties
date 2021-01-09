package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPartyPreRenameEvent extends BukkitPartiesEvent implements IPartyPreRenameEvent {
	private boolean cancelled;
	private final Party party;
	private final String oldName;
	private String newName;
	private final PartyPlayer player;
	private final boolean isAdmin;
	
	public BukkitPartiesPartyPreRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin) {
		super(false);
		this.party = party;
		this.oldName = oldName;
		this.newName = newName;
		this.player = player;
		this.isAdmin = isAdmin;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@Nullable
	@Override
	public String getOldPartyName() {
		return oldName;
	}
	
	@Nullable
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
