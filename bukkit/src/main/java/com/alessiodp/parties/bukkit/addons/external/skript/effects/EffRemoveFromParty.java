package com.alessiodp.parties.bukkit.addons.external.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("NullableProblems")
@Name("Remove From Party")
@Description("Remove the player from the party.")
@Examples({"remove player from party"})
@Since("3.0.0")
public class EffRemoveFromParty extends Effect {
	static {
		Skript.registerEffect(EffRemoveFromParty.class,
				"(remove|kick) %offlineplayers% from party",
				"(remove|kick) %partyplayer% from party");
	}
	
	private Expression<OfflinePlayer> players;
	private Expression<PartyPlayer> partyPlayers;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (matchedPattern == 0)
			players = (Expression<OfflinePlayer>) exprs[0];
		else
			partyPlayers = (Expression<PartyPlayer>) exprs[0];
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		if (players != null) {
			for (OfflinePlayer player : players.getAll(e)) {
				PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
				if (partyPlayer != null && !partyPlayer.isInParty()) {
					Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
					if (party != null) {
						party.removeMember(partyPlayer);
					}
				}
			}
		} else {
			for (PartyPlayer partyPlayer : partyPlayers.getAll(e)) {
				if (!partyPlayer.isInParty()) {
					Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
					if (party != null) {
						party.removeMember(partyPlayer);
					}
				}
			}
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "remove " + (players != null ? players : partyPlayers).toString(e, debug) + " from party";
	}
}
