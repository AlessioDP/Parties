package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitPartiesPartyPostCreateEvent extends BukkitPartiesEvent implements IPartyPostCreateEvent {
	private final PartyPlayer player;
	private final Party party;
	
	public BukkitPartiesPartyPostCreateEvent(PartyPlayer player, Party party) {
		super(true);
		this.player = player;
		this.party = party;
	}
	
	@Override
	public @Nullable PartyPlayer getCreator() {
		return player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
}
