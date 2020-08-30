package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.alessiodp.parties.api.Parties;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@Name("Exists Party")
@Description("Checks if a party exists.")
@Examples({"exists the party name"})
public class CondExistsParty extends Condition {
	static {
		Skript.registerCondition(CondExistsParty.class,
				"exists [the] party %string%",
				"(not|does not|doesn't) exists [the] party %string%");
	}
	
	private Expression<String> party;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
		party = (Expression<String>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}
	
	@Override
	public boolean check(final Event e) {
		return party.check(e,
				p -> Parties.getApi().getParty(p) != null, isNegated());
	}
	
	@Override
	public String toString(final @Nullable Event e, boolean debug) {
		return "exists party " + party.toString(e, debug);
	}
}
