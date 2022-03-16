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
import com.alessiodp.parties.api.interfaces.Party;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("NullableProblems")
@Name("Delete Party")
@Description("Delete the party.")
@Examples({"delete party with name \"test\""})
@Since("3.0.0")
public class EffDeleteParty extends Effect {
	static {
		Skript.registerEffect(EffDeleteParty.class,
				"delete %party%");
	}
	
	private Expression<Party> parties;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		parties = (Expression<Party>) exprs[0];
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		if (parties != null) {
			for (Party p : parties.getAll(e)) {
				p.delete();
			}
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "delete " + parties.toString(e, debug);
	}
}
