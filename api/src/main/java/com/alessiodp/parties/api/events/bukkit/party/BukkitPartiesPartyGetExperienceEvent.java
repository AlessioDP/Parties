package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitPartiesPartyGetExperienceEvent extends BukkitPartiesEvent implements IPartyGetExperienceEvent {
	private final Party party;
	private final double experience;
	
	private final ExpSource source;
	private final PartyPlayer killer;
	
	public BukkitPartiesPartyGetExperienceEvent(Party party, double experience, PartyPlayer killer) {
		super(true);
		this.party = party;
		this.experience = experience;
		source = killer != null ? ExpSource.KILL : ExpSource.API;
		this.killer = killer;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@Override
	public double getExperience() {
		return experience;
	}
	
	@NotNull
	@Override
	public ExpSource getSource() {
		return source;
	}
	
	@Nullable
	@Override
	public PartyPlayer getKiller() {
		return killer;
	}
}
