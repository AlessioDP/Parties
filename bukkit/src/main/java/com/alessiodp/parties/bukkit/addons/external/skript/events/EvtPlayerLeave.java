package com.alessiodp.parties.bukkit.addons.external.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("NullableProblems")
public class EvtPlayerLeave extends SelfRegisteringSkriptEvent {
	static {
		Skript.registerEvent("PartyPlayer Pre Leave Party", EvtPlayerLeave.class, BukkitPartiesPlayerPreLeaveEvent.class,
				"[player] pre leave[s] [a] party")
				.description("Called when a player is leaving a party.")
				.examples("on player pre leave party:",
						"\tmessage \"Player %name of event-partyplayer% is leaving the party %name of event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPlayerPreLeaveEvent.class, Party.class, new Getter<Party, BukkitPartiesPlayerPreLeaveEvent>() {
			@Override
			public Party get(BukkitPartiesPlayerPreLeaveEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPreLeaveEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPlayerPreLeaveEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPlayerPreLeaveEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPreLeaveEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPlayerPreLeaveEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPlayerPreLeaveEvent e) {
				return Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID());
			}
		}, 0);
		
		Skript.registerEvent("PartyPlayer Post Leave Party", EvtPlayerLeave.class, BukkitPartiesPlayerPostLeaveEvent.class,
				"[player] [post] leave[s] [a] party")
				.description("Called when a player left a party.")
				.examples("on player post leave party:",
						"\tmessage \"Player %name of event-partyplayer% left the party %name of event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPlayerPostLeaveEvent.class, Party.class, new Getter<Party, BukkitPartiesPlayerPostLeaveEvent>() {
			@Override
			public Party get(BukkitPartiesPlayerPostLeaveEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPostLeaveEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPlayerPostLeaveEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPlayerPostLeaveEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPlayerPostLeaveEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPlayerPostLeaveEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPlayerPostLeaveEvent e) {
				return Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID());
			}
		}, 0);
	}
	
	final static Collection<Trigger> triggers = new ArrayList<>();
	
	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
		return true;
	}
	
	@Override
	public void register(Trigger trigger) {
		triggers.add(trigger);
	}
	
	@Override
	public void unregister(Trigger trigger) {
		triggers.remove(trigger);
	}
	
	@Override
	public void unregisterAll() {
		triggers.clear();
	}
	
	@Override
	public String toString(Event event, boolean debug) {
		return "party leave";
	}
}
