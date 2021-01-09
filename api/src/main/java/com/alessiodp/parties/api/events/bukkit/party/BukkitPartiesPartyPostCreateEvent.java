package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPartyPostCreateEvent extends BukkitPartiesEvent implements IPartyPostCreateEvent {
	private final PartyPlayer player;
	private final Party party;
	
	public BukkitPartiesPartyPostCreateEvent(PartyPlayer player, Party party) {
		super(true);
		this.player = player;
		this.party = party;
	}
	
	@Nullable
	@Override
	public PartyPlayer getCreator() {
		return player;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
}
