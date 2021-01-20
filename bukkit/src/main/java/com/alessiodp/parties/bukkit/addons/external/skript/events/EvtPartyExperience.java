package com.alessiodp.parties.bukkit.addons.external.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyLevelUpEvent;
import com.alessiodp.parties.api.interfaces.Party;

public class EvtPartyExperience {
	static {
		Skript.registerEvent("Party Get Experience", SimpleEvent.class, BukkitPartiesPartyGetExperienceEvent.class,
				"party get[s] experience")
				.description("Called when a party gets experience.")
				.examples("on party get experience:",
						"\tmessage \"%event-party% got %event-number% experience\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyGetExperienceEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyGetExperienceEvent>() {
			@Override
			public Party get(BukkitPartiesPartyGetExperienceEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyGetExperienceEvent.class, Number.class, new Getter<Number, BukkitPartiesPartyGetExperienceEvent>() {
			@Override
			public Number get(BukkitPartiesPartyGetExperienceEvent e) {
				return e.getExperience();
			}
		}, 0);
		
		Skript.registerEvent("Party Level Up", SimpleEvent.class, BukkitPartiesPartyLevelUpEvent.class,
				"party level[s] up")
				.description("Called when a party levels up.")
				.examples("on party level up:",
						"\tmessage \"%event-party% leveled up to %event-number%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyLevelUpEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyLevelUpEvent>() {
			@Override
			public Party get(BukkitPartiesPartyLevelUpEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyLevelUpEvent.class, Number.class, new Getter<Number, BukkitPartiesPartyLevelUpEvent>() {
			@Override
			public Number get(BukkitPartiesPartyLevelUpEvent e) {
				return e.getNewLevel();
			}
		}, 0);
	}
}
