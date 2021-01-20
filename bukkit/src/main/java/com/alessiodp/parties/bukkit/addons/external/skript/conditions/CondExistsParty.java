package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

@Name("Party Exists")
@Description("Checks if a party exists.")
@Examples({
		"if party \"test\" exists:",
		"\tmessage \"A party named test exists\"",
		"if party \"test\" with id \"d6bdf67e-cdfc-4dda-be51-d1c745ba9c3e\" not exists:",
		"\tmessage \"A party with id d6bdf67e-cdfc-4dda-be51-d1c745ba9c3e exists\""
})
@Since("3.0.0")
public class CondExistsParty extends Condition {
	static {
		Skript.registerCondition(CondExistsParty.class,
				"[the] party [with name] %string% exist[s]",
				"[the] party with id %string% exist[s]",
				"[the] party [with name] %string% (not|does not|doesn't) exist[s]",
				"[the] party with id %string% (not|does not|doesn't) exist[s]");
	}
	
	private Expression<String> nameOrId;
	private boolean isUuid = false;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
		nameOrId = (Expression<String>) exprs[0];
		isUuid = matchedPattern % 2 != 0;
		setNegated(matchedPattern >= 2);
		return true;
	}
	
	@Override
	public boolean check(final Event e) {
		return nameOrId.check(e, (party) -> getParty(party) != null, isNegated());
	}
	
	@Override
	public String toString(final @Nullable Event e, boolean debug) {
		return "party "
				+ (isUuid ? "with id " : "")
				+ nameOrId.toString(e, debug)
				+ " exists";
	}
	
	private Party getParty(String nameOrId) {
		if (isUuid)
			return Parties.getApi().getParty(UUID.fromString(nameOrId));
		return Parties.getApi().getParty(nameOrId);
	}
}
