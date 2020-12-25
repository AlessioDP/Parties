package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BungeePartiesPartyPostRenameEvent extends BungeePartiesEvent implements IPartyPostRenameEvent {
	private final Party party;
	private final String oldName;
	private final String newName;
	private final PartyPlayer player;
	private final boolean isAdmin;
	
	public BungeePartiesPartyPostRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin) {
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
	
	@NonNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public boolean isAdmin() {
		return isAdmin;
	}
}
