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
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@Name("Create Party")
@Description("Create a party.")
@Examples({"create party with name test"})
public class EffCreateParty extends Effect {
	static {
		Skript.registerEffect(EffCreateParty.class,
				"(create|make) [a] party with name %string% and leader %offlineplayer%");
	}
	
	private Expression<String> name;
	private Expression<OfflinePlayer> leader;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		name = (Expression<String>) exprs[0];
		leader = (Expression<OfflinePlayer>) exprs[1];
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(leader.getSingle(e).getUniqueId());
		if (partyPlayer != null) {
			Parties.getApi().createParty(name.getSingle(e), partyPlayer);
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "create party with name " + name.toString(e, debug) + " and leader " + leader.toString(e, debug);
	}
}
