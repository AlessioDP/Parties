package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class PartiesPartyPostCreateEventHook extends BukkitAbstractEvent implements PartiesPartyPostCreateEvent {
	private PartyPlayer player;
	private Party party;
	
	public PartiesPartyPostCreateEventHook(PartyPlayer player, Party party) {
		this.player = player;
		this.party = party;
	}
	
	@NotNull
	@Override
	public PartyPlayer getCreator() {
		return player;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
}
