package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class EconomyManager {
	protected final PartiesPlugin plugin;
	
	public abstract boolean payCommand(PaidCommand vaultCommand, PartyPlayerImpl partyPlayerImpl, String commandLabel, String[] args);
	
	public enum PaidCommand {
		CLAIM, COLOR, CREATE, DESC, HOME, JOIN, MOTD, SETHOME, TELEPORT
	}
}
