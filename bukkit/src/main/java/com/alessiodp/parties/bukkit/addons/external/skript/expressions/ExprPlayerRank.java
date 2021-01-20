package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.Event;

@Name("Party Player Rank")
@Description("Get the rank of the given partyplayer.")
@Examples({"send \"%rank of partyplayer player%\"",
		"send \"%rank of event-partyplayer%\""})
@Since("3.0.0")
public class ExprPlayerRank extends SimplePropertyExpression<PartyPlayer, Integer> {
	static {
		register(ExprPlayerRank.class, Integer.class, "rank", "partyplayer");
	}
	
	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "rank";
	}
	
	@Override
	public Integer convert(PartyPlayer partyPlayer) {
		return partyPlayer.getRank();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null && mode == Changer.ChangeMode.SET) {
			PartyPlayer partyPlayer = getExpr().getSingle(e);
			Integer newRank = (Integer) delta[0];
			partyPlayer.setRank(newRank);
		}
	}
	
	@Override
	public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
		return (mode == Changer.ChangeMode.SET) ? CollectionUtils.array(Integer.class) : null;
	}
}
