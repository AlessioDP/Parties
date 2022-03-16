package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @Nullable String getOldPartyName() {
		return oldName;
	}
	
	@Override
	public @Nullable String getNewPartyName() {
		return newName;
	}
	
	@Override
	public @Nullable PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public boolean isAdmin() {
		return isAdmin;
	}
}
