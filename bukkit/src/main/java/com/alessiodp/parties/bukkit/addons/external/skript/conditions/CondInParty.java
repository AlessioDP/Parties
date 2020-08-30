package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@Name("Is In Party")
@Description("Checks if a player is in a party.")
@Examples({"player is in party",
		"victim is not in party"})
public class CondInParty extends Condition {
	static {
		Skript.registerCondition(CondInParty.class,
				"%offlineplayers% (is|are) in [a] party",
				"%offlineplayers% (is|are)(n't| not) in [a] party");
	}
	
	private Expression<OfflinePlayer> players;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
		players = (Expression<OfflinePlayer>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}
	
	@Override
	public boolean check(final Event e) {
		return players.check(e,
				player -> {
					PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
					return partyPlayer != null && partyPlayer.isInParty();
				}, isNegated());
	}
	
	@Override
	public String toString(final @Nullable Event e, boolean debug) {
		return PropertyCondition.toString(this, PropertyCondition.PropertyType.BE, e, debug, players,
				"in party");
	}
}
