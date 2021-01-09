package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitPartiesPlayerPostChatEvent extends BukkitPartiesEvent implements IPlayerPostChatEvent {
	private final PartyPlayer player;
	private final Party party;
	private final String message;
	
	public BukkitPartiesPlayerPostChatEvent(PartyPlayer player, Party party, String message) {
		super(true);
		this.player = player;
		this.party = party;
		this.message = message;
	}
	
	@NonNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NonNull
	@Override
	public String getMessage() {
		return message;
	}
}
