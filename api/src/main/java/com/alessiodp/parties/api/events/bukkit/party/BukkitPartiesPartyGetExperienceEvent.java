package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitPartiesPartyGetExperienceEvent extends BukkitPartiesEvent {
	private final Party party;
	private final int experience;
	
	private final ExpSource source;
	private final PartyPlayer killer;
	
	public BukkitPartiesPartyGetExperienceEvent(Party party, int experience, PartyPlayer killer) {
		super(true);
		this.party = party;
		this.experience = experience;
		source = ExpSource.KILL;
		this.killer = killer;
	}
	
	@NonNull
	public Party getParty() {
		return party;
	}
	
	public int getExperience() {
		return experience;
	}
	
	@NonNull
	public ExpSource getSource() {
		return source;
	}
	
	@Nullable
	public PartyPlayer getKiller() {
		return killer;
	}
	
	public enum ExpSource {
		KILL
	}
}
