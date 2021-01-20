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
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@Name("Create Party")
@Description("Create a party with the given name and player as leader.")
@Examples({"create party with name test and leader player"})
@Since("3.0.0")
public class EffCreateParty extends Effect {
	static {
		Skript.registerEffect(EffCreateParty.class,
				"(create|make) [a] party [with name] %string% (and|with) leader %offlineplayer%",
				"(create|make) [a] party [with name] %string% (and|with) leader %partyplayer%");
	}
	
	private Expression<String> name;
	private Expression<OfflinePlayer> leaderPlayer;
	private Expression<PartyPlayer> leaderPartyPlayer;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		name = (Expression<String>) exprs[0];
		if (matchedPattern == 0)
			leaderPlayer = (Expression<OfflinePlayer>) exprs[1];
		else
			leaderPartyPlayer = (Expression<PartyPlayer>) exprs[1];
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		PartyPlayer pp = leaderPartyPlayer != null ? leaderPartyPlayer.getSingle(e) : null;
		if (pp == null)
			pp = Parties.getApi().getPartyPlayer(leaderPlayer.getSingle(e).getUniqueId());
		if (pp != null && !pp.isInParty()) {
			Parties.getApi().createParty(name.getSingle(e), pp);
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "create party " + name.toString(e, debug) + " with leader " + (leaderPlayer != null ? leaderPlayer : leaderPartyPlayer).toString(e, debug);
	}
}
