package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

@SuppressWarnings("NullableProblems")
@Name("Party Members")
@Description("Get the member list of the given party.")
@Examples({"send \"%members of party with name \"test\"%\"",
		"send \"%members of event-party%\""})
@Since("3.0.0")
public class ExprPartyMembers extends SimpleExpression<PartyPlayer> {
	static {
		Skript.registerExpression(ExprPartyMembers.class, PartyPlayer.class, ExpressionType.COMBINED,
						"[the] [party(-| )](members|players) of %party%");
	}
	
	private Expression<Party> party;
	
	@Override
	public boolean isSingle() {
		return false;
	}
	
	@Override
	public Class<? extends PartyPlayer> getReturnType() {
		return PartyPlayer.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		party = (Expression<Party>) exprs[0];
		return true;
	}
	
	@Override
	@Nullable
	protected PartyPlayer[] get(Event e) {
		PartyPlayer[] ret = new PartyPlayer[party.getSingle(e).getMembers().size()];
		int i = 0;
		for (UUID u : party.getSingle(e).getMembers()) {
			ret[i] = Parties.getApi().getPartyPlayer(u);
			i++;
		}
		return ret;
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "members of " + party.toString(e, debug);
	}
}
