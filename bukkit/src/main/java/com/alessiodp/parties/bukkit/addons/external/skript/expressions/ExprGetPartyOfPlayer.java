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
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("NullableProblems")
@Name("Get Party Of Player")
@Description("Get the party of the given player.")
@Examples({"send \"the %party of player%\"",
		"send \"the %party of event-partyplayer%\""})
@Since("3.0.0")
public class ExprGetPartyOfPlayer extends SimpleExpression<Party> {
	static {
		Skript.registerExpression(ExprGetPartyOfPlayer.class, Party.class, ExpressionType.COMBINED,
				"[get|the] party of [the] [player] %offlineplayer%",
				"[get|the] party of [the] [player] %partyplayer%");
	}
	
	private Expression<OfflinePlayer> player;
	private Expression<PartyPlayer> partyPlayer;
	
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
		if (matchedPattern == 0)
			player = (Expression<OfflinePlayer>) exprs[0];
		else
			partyPlayer = (Expression<PartyPlayer>) exprs[0];
		return true;
	}
	
	@Override
	@Nullable
	protected Party[] get(Event e) {
		PartyPlayer pp = partyPlayer != null ? partyPlayer.getSingle(e) : null;
		if (pp == null)
			pp = Parties.getApi().getPartyPlayer(player.getSingle(e).getUniqueId());
		if (pp != null && pp.isInParty()) {
			return new Party[] {Parties.getApi().getParty(pp.getPartyId())};
		}
		return new Party[0];
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "party of " + (player != null ? player : partyPlayer).toString(e, debug);
	}
}
