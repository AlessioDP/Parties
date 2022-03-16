package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostHomeEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesPlayerPostHomeEvent extends BukkitPartiesEvent implements IPlayerPostHomeEvent {
	private final PartyPlayer player;
	private final Party party;
	private final PartyHome home;
	
	public BukkitPartiesPlayerPostHomeEvent(PartyPlayer player, Party party, PartyHome home) {
		super(false);
		this.player = player;
		this.party = party;
		this.home = home;
	}
	
	@Override
	public @NotNull PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @NotNull PartyHome getHome() {
		return home;
	}
}
