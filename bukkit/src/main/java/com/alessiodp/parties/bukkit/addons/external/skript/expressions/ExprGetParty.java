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
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

@Name("Get Party")
@Description("Get the party with the given name/id.")
@Examples({"send \"the %party \"test\"%\"",
		"send \"the %party with id \"d6bdf67e-cdfc-4dda-be51-d1c745ba9c3e\"%\""})
@Since("3.0.0")
public class ExprGetParty extends SimpleExpression<Party> {
	static {
		Skript.registerExpression(ExprGetParty.class, Party.class, ExpressionType.COMBINED,
				"[get|the] party [with name] %string%",
				"[get|the] party with id %string%");
	}
	
	private Expression<String> nameOrId;
	private boolean isUuid = false;
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public Class<? extends Party> getReturnType() {
		return Party.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		nameOrId = (Expression<String>) exprs[0];
		isUuid = matchedPattern == 1;
		return true;
	}
	
	@Override
	@Nullable
	protected Party[] get(Event e) {
		Party party = getParty(nameOrId.getSingle(e));
		if (party != null) {
			return new Party[]{party};
		}
		return new Party[0];
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "party " + (isUuid ? "with id " : "") + nameOrId.toString(e, debug);
	}
	
	private Party getParty(String nameOrId) {
		if (isUuid)
			return Parties.getApi().getParty(UUID.fromString(nameOrId));
		return Parties.getApi().getParty(nameOrId);
	}
}
