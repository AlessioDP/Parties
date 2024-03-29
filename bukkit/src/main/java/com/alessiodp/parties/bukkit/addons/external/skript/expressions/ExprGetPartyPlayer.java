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
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("NullableProblems")
@Name("Get Party Player")
@Description("Get the partyplayer of the given player.")
@Examples({"send \"the %partyplayer of player%\"",
		"send \"the %partyplayer of event-player%\""})
@Since("3.0.0")
public class ExprGetPartyPlayer extends SimpleExpression<PartyPlayer> {
	static {
		Skript.registerExpression(ExprGetPartyPlayer.class, PartyPlayer.class, ExpressionType.COMBINED,
				"[get|the] party(player|member|-player|-member) [of] %offlineplayer%");
	}
	
	private Expression<OfflinePlayer> player;
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public Class<? extends PartyPlayer> getReturnType() {
		return PartyPlayer.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		player = (Expression<OfflinePlayer>) exprs[0];
		return true;
	}
	
	@Override
	@Nullable
	protected PartyPlayer[] get(Event e) {
		PartyPlayer pp = Parties.getApi().getPartyPlayer(player.getSingle(e).getUniqueId());
		if (pp != null) {
			return new PartyPlayer[]{pp};
		}
		return new PartyPlayer[0];
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "partyplayer " + player.toString(e, debug);
	}
}
