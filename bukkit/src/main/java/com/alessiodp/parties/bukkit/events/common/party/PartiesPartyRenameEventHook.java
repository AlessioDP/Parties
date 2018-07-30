package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.events.party.PartiesPartyRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class PartiesPartyRenameEventHook extends BukkitAbstractEventCancellable implements PartiesPartyRenameEvent {
	private Party party;
	private String newName;
	private PartyPlayer player;
	private boolean isAdmin;
	
	public PartiesPartyRenameEventHook(Party party, String newName, PartyPlayer player, boolean isAdmin) {
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
}
