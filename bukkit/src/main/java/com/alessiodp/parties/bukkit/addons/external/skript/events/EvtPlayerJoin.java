package com.alessiodp.parties.bukkit.addons.external.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class EvtPlayerJoin {
	static {
		Skript.registerEvent("PartyPlayer Pre Join Party", SimpleEvent.class, BukkitPartiesPlayerPreJoinEvent.class,
				"[player] pre join[s] [a] party")
				.description("Called when a player is joining a party.")
				.examples("on player pre join party:",
						"\tmessage \"Player %name of event-partyplayer% is joining the party %name of event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPlayerPreJoinEvent.class, Party.class, new Getter<Party, BukkitPartiesPlayerPreJoinEvent>() {
			@Override
			public Party get(BukkitPartiesPlayerPreJoinEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPreJoinEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPlayerPreJoinEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPlayerPreJoinEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPreJoinEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPlayerPreJoinEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPlayerPreJoinEvent e) {
				return Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID());
			}
		}, 0);
		
		Skript.registerEvent("PartyPlayer Post Join Party", SimpleEvent.class, BukkitPartiesPlayerPostJoinEvent.class,
				"[player] [post] join[s] [a] party")
				.description("Called when a player joined a party.")
				.examples("on player post join party:",
						"\tmessage \"Player %name of event-partyplayer% joined the party %name of event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPlayerPostJoinEvent.class, Party.class, new Getter<Party, BukkitPartiesPlayerPostJoinEvent>() {
			@Override
			public Party get(BukkitPartiesPlayerPostJoinEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPostJoinEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPlayerPostJoinEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPlayerPostJoinEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPostJoinEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPlayerPostJoinEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPlayerPostJoinEvent e) {
				return Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID());
			}
		}, 0);
	}
}
