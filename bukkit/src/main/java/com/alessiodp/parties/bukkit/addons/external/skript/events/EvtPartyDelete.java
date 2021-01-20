package com.alessiodp.parties.bukkit.addons.external.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class EvtPartyDelete {
	static {
		Skript.registerEvent("Party Pre Delete", SimpleEvent.class, BukkitPartiesPartyPreDeleteEvent.class,
				"[player] pre delete[s] [a] party")
				.description("Called when a player is deleting a party. Cancellable.")
				.examples("on pre delete party:",
						"\tmessage \"%event-partyplayer% is deleting the party %event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyPreDeleteEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyPreDeleteEvent>() {
			@Override
			public Party get(BukkitPartiesPartyPreDeleteEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreDeleteEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPartyPreDeleteEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPartyPreDeleteEvent e) {
				return e.getCommandSender();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreDeleteEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPartyPreDeleteEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPartyPreDeleteEvent e) {
				return e.getKickedPlayer() != null ? Bukkit.getPlayer(e.getKickedPlayer().getPlayerUUID()) : Bukkit.getConsoleSender();
			}
		}, 0);
		
		Skript.registerEvent("Party Post Delete", SimpleEvent.class, BukkitPartiesPartyPostDeleteEvent.class,
				"[player] [post] delete[s] [a] party")
				.description("Called when a player has deleted a party.")
				.examples("on post delete party:",
						"\tmessage \"%event-partyplayer% deleted the party %event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyPostDeleteEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyPostDeleteEvent>() {
			@Override
			public Party get(BukkitPartiesPartyPostDeleteEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostDeleteEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPartyPostDeleteEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPartyPostDeleteEvent e) {
				return e.getCommandSender();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostDeleteEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPartyPostDeleteEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPartyPostDeleteEvent e) {
				return e.getKickedPlayer() != null ? Bukkit.getPlayer(e.getKickedPlayer().getPlayerUUID()) : Bukkit.getConsoleSender();
			}
		}, 0);
	}
}
