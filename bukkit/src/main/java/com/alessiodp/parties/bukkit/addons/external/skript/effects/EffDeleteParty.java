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
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

@Name("Delete Party")
@Description("Delete the party.")
@Examples({"delete party with name test"})
public class EffDeleteParty extends Effect {
	static {
		Skript.registerEffect(EffDeleteParty.class,
				"(delete|remove) [the] party with name %string%",
				"(delete|remove) [the] party with id %string%");
	}
	
	private Expression<String> name;
	private UUID id;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (matchedPattern == 0)
			name = (Expression<String>) exprs[0];
		else
			id = UUID.fromString(String.valueOf(exprs[0]));
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		Party party;
		if (id != null)
			party = Parties.getApi().getParty(id);
		else
			party = Parties.getApi().getParty(name.getSingle(e));
		
		if (party != null) {
			party.delete();
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		if (id != null)
			return "delete party with id " + id.toString();
		else
			return "delete party with name " + name.toString(e, debug);
	}
}
