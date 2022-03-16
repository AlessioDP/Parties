package com.alessiodp.parties.bukkit.addons.external.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostRenameEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreRenameEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("NullableProblems")
public class EvtPartyRename extends SelfRegisteringSkriptEvent {
	static {
		Skript.registerEvent("Party Pre Rename", EvtPartyRename.class, BukkitPartiesPartyPreRenameEvent.class,
				"[player] pre rename[s] [a] party")
				.description("Called when a player is renaming a party. \"event-partyplayer\" can be null if executed by console. Cancellable.")
				.examples("on pre rename party:",
						"\tmessage \"Party %name of event-party% is getting renamed to %event-string%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyPreRenameEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyPreRenameEvent>() {
			@Override
			public Party get(BukkitPartiesPartyPreRenameEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreRenameEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPartyPreRenameEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPartyPreRenameEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreRenameEvent.class, String.class, new Getter<String, BukkitPartiesPartyPreRenameEvent>() {
			@Override
			public String get(BukkitPartiesPartyPreRenameEvent e) {
				return e.getNewPartyName();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPreRenameEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPartyPreRenameEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPartyPreRenameEvent e) {
				return e.getPartyPlayer() != null ? Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID()) : Bukkit.getConsoleSender();
			}
		}, 0);
		
		Skript.registerEvent("Party Post Rename", EvtPartyRename.class, BukkitPartiesPartyPostRenameEvent.class,
				"[player] [post] rename[s] [a] party")
				.description("Called when a player renamed a party. \"event-partyplayer\" can be null if executed by console.")
				.examples("on post rename party:",
						"\tmessage \"Party %event-string% has been renamed to %name of event-party%\"")
				.since("3.0.0");
		EventValues.registerEventValue(BukkitPartiesPartyPostRenameEvent.class, Party.class, new Getter<Party, BukkitPartiesPartyPostRenameEvent>() {
			@Override
			public Party get(BukkitPartiesPartyPostRenameEvent e) {
				return e.getParty();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostRenameEvent.class, PartyPlayer.class, new Getter<PartyPlayer, BukkitPartiesPartyPostRenameEvent>() {
			@Override
			public PartyPlayer get(BukkitPartiesPartyPostRenameEvent e) {
				return e.getPartyPlayer();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostRenameEvent.class, String.class, new Getter<String, BukkitPartiesPartyPostRenameEvent>() {
			@Override
			public String get(BukkitPartiesPartyPostRenameEvent e) {
				return e.getOldPartyName();
			}
		}, 0);
		EventValues.registerEventValue(BukkitPartiesPartyPostRenameEvent.class, CommandSender.class, new Getter<CommandSender, BukkitPartiesPartyPostRenameEvent>() {
			@Override
			public CommandSender get(BukkitPartiesPartyPostRenameEvent e) {
				return e.getPartyPlayer() != null ? Bukkit.getPlayer(e.getPartyPlayer().getPlayerUUID()) : Bukkit.getConsoleSender();
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
		return "party rename";
	}
}
