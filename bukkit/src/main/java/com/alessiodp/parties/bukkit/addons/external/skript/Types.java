package com.alessiodp.parties.bukkit.addons.external.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Converter;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

@SuppressWarnings("NullableProblems")
public class Types {
	
	static {
		// Party
		Classes.registerClass(new ClassInfo<>(Party.class, "party")
				.defaultExpression(new EventValueExpression<>(Party.class))
				.name("Party")
				.user("party")
				.description("Represents a party entity handled by Parties plugin.")
				.examples("add player to party \"test\"",
						"on party create:",
						"\tbroadcast \"Party %name of event-party% has been created")
				.since("3.0.0")
				.parser(new Parser<Party>() {
					@Override
					public Party parse(String s, ParseContext context) {
						return null;
					}
					
					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}
					
					@Override
					public String toString(Party party, int i) {
						return party.getName() != null ? party.getName() : party.getId().toString();
					}
					
					@Override
					public String toVariableNameString(Party party) {
						return "party:" + party.getId();
					}
				})
		);
		
		// PartyPlayer
		Classes.registerClass(new ClassInfo<>(PartyPlayer.class, "partyplayer")
				.defaultExpression(new EventValueExpression<>(PartyPlayer.class))
				.name("PartyPlayer")
				.user("party-?(player|member)")
				.description("Represents a party-player entity handled by Parties plugin.")
				.examples("on player join party:",
						"\tif partyplayer player is leader:",
						"\t\t...")
				.since("3.0.0")
				.parser(new Parser<PartyPlayer>() {
					@Override
					public PartyPlayer parse(String s, ParseContext context) {
						return null;
					}
					
					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}
					
					@Override
					public String toString(PartyPlayer partyPlayer, int i) {
						return partyPlayer.getName();
					}
					
					@Override
					public String toVariableNameString(PartyPlayer partyPlayer) {
						return "partyplayer:" + partyPlayer.getPlayerUUID();
					}
				})
		);
		Converters.registerConverter(PartyPlayer.class, OfflinePlayer.class, partyPlayer -> Bukkit.getOfflinePlayer(partyPlayer.getPlayerUUID()), Converter.NO_COMMAND_ARGUMENTS);
	}
}
