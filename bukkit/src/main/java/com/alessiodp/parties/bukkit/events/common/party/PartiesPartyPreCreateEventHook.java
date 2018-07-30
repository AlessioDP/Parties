package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.events.party.PartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class PartiesPartyPreCreateEventHook extends BukkitAbstractEventCancellable implements PartiesPartyPreCreateEvent {
	private PartyPlayer player;
	private String party;
	private boolean fixed;
	
	public PartiesPartyPreCreateEventHook(PartyPlayer player, String party, boolean fixed) {
		this.player = player;
		this.party = party;
		this.fixed = fixed;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public String getPartyName() {
		return party;
	}
	
	@Override
	public void setPartyName(String name) {
		party = name;
	}
	
	@Override
	public boolean isFixed() {
		return fixed;
	}
	
	@Override
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
}
