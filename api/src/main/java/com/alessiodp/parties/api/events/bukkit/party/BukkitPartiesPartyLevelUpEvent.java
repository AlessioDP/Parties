package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesPartyLevelUpEvent extends BukkitPartiesEvent implements IPartyLevelUpEvent {
	private final Party party;
	private final int newLevel;
	
	public BukkitPartiesPartyLevelUpEvent(Party party, int newLevel) {
		super(true);
		this.party = party;
		this.newLevel = newLevel;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public int getNewLevel() {
		return newLevel;
	}
}
