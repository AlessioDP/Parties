package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class EconomyManager {
	protected PartiesPlugin plugin;
	
	protected EconomyManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract boolean payCommand(PaidCommand vaultCommand, PartyPlayerImpl partyPlayerImpl, String commandLabel, String[] args);
	
	public abstract double getCommandValue(PaidCommand vaultCommand);
	public abstract String getCommandMessage(PaidCommand vaultCommand, double price);
	
	public enum PaidCommand {
		CLAIM, COLOR, CREATE, DESC, HOME, JOIN, MOTD, SETHOME, TELEPORT
	}
}
