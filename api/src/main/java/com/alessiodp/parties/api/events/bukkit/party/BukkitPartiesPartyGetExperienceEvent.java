package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@Override
	public double getExperience() {
		return experience;
	}
	
	@NonNull
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
