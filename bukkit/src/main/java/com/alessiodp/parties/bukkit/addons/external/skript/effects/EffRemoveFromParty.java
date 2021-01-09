package com.alessiodp.parties.bukkit.addons.external.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
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

@Name("Remove From Party")
@Description("Remove the player from the party.")
@Examples({"remove player from the party"})
public class EffRemoveFromParty extends Effect {
	static {
		Skript.registerEffect(EffRemoveFromParty.class,
				"remove %offlineplayers% from [the] party");
	}
	
	private Expression<OfflinePlayer> players;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		players = (Expression<OfflinePlayer>) exprs[0];
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		for (OfflinePlayer player : players.getArray(e)) {
			PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
			if (partyPlayer != null && partyPlayer.isInParty()) {
				Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
				if (party != null) {
					party.removeMember(partyPlayer);
				}
			}
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "remove " + players.toString(e, debug) + " from party";
	}
}
