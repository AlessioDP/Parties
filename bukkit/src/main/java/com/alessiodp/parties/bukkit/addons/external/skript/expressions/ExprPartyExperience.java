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

@Name("Party Experience")
@Description("Get the experience of the given party.")
@Examples({"send \"%experience of party with name \"test\"%\"",
		"send \"%experience of event-party%\""})
@Since("3.0.0")
public class ExprPartyExperience extends SimplePropertyExpression<Party, Double> {
	static {
		register(ExprPartyExperience.class, Double.class, "exp[erience]", "party");
	}
	
	@Override
	public Class<? extends Double> getReturnType() {
		return Double.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "experience";
	}
	
	@Override
	public Double convert(Party party) {
		return party.getExperience();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null) {
			Party party = getExpr().getSingle(e);
			Double newExperience = (Double) delta[0];
			switch (mode) {
				case SET:
					party.setExperience(newExperience);
					break;
				case ADD:
					party.giveExperience(newExperience);
					break;
				case DELETE:
				case RESET:
					party.setExperience(0);
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
		return (mode != Changer.ChangeMode.REMOVE && mode != Changer.ChangeMode.REMOVE_ALL) ? CollectionUtils.array(String.class) : null;
	}
}
