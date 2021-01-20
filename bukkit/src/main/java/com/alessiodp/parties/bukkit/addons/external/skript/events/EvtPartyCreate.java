package com.alessiodp.parties.bukkit.addons.external.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class EvtPartyCreate {
	static {
		Skript.registerEvent("Party Pre Create", SimpleEvent.class, BukkitPartiesPartyPreCreateEvent.class,
				"[player] pre create[s] [a] party")
				.description("Called when a player is creating a party. Cancellable.")
				.examples("on pre create party:",
						"\tmessage \"%event-partyplayer% is creating the party %event-string%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyPreCreateEvent.class, String.class, new Getter<String, BukkitPartiesPartyPreCreateEvent>() {
			@Override
			public String get(BukkitPartiesPartyPreCreateEvent e) {
				return e.getPartyName();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreCreateEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPartyPreCreateEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPartyPreCreateEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreCreateEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPartyPreCreateEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPartyPreCreateEvent e) {
				return e.getPartyPlayer() != null ? Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID()) : Bukkit.getConsoleSender();
			}
		}, 0);
		
		Skript.registerEvent("Party Post Create", SimpleEvent.class, BukkitPartiesPartyPostCreateEvent.class,
				"[player] [post] create[s] [a] party")
				.description("Called when a player has created a party.")
				.examples("on post create party:",
						"\tmessage \"%event-partyplayer% created the party %event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyPostCreateEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyPostCreateEvent>() {
			@Override
			public Party get(BukkitPartiesPartyPostCreateEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostCreateEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPartyPostCreateEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPartyPostCreateEvent e) {
				return e.getCreator();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostCreateEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPartyPostCreateEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPartyPostCreateEvent e) {
				return e.getCreator() != null ? Bukkit.getPlayer(e.getCreator().getPlayerUUID()) : Bukkit.getConsoleSender();
			}
		}, 0);
	}
}
