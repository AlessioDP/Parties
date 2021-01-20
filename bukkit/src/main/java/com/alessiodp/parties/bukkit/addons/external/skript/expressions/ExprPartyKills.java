package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.alessiodp.parties.api.interfaces.Party;
import org.bukkit.event.Event;

@Name("Party Kills")
@Description("Get the kills number of the given party.")
@Examples({"send \"%kills of party with name \"test\"%\"",
		"send \"%kills of event-party%\""})
@Since("3.0.0")
public class ExprPartyKills extends SimplePropertyExpression<Party, Integer> {
	static {
		register(ExprPartyKills.class, Integer.class, "kills", "party");
	}
	
	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "kills";
	}
	
	@Override
	public Integer convert(Party party) {
		return party.getKills();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null) {
			Party party = getExpr().getSingle(e);
			Integer newKills = (Integer) delta[0];
			switch (mode) {
				case SET:
					party.setKills(newKills);
					break;
				case DELETE:
					party.setKills(0);
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
		return (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE) ? CollectionUtils.array(Integer.class) : null;
	}
}
