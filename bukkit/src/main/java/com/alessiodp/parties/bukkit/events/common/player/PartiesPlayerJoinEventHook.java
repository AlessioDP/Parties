package com.alessiodp.parties.bukkit.events.common.player;

import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEventCancellable;
import com.alessiodp.parties.api.events.player.PartiesPlayerJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PartiesPlayerJoinEventHook extends BukkitAbstractEventCancellable implements PartiesPlayerJoinEvent {
	private PartyPlayer player;
	private Party party;
	private boolean isInvited;
	private UUID invitedBy;
	
	public PartiesPlayerJoinEventHook(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		this.player = player;
		this.party = party;
		this.isInvited = isInvited;
		this.invitedBy = invitedBy;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@Override
	public boolean isInvited() {
		return isInvited;
	}
	
	@Nullable
	@Override
	public UUID getInviter() {
		return invitedBy;
	}
}
