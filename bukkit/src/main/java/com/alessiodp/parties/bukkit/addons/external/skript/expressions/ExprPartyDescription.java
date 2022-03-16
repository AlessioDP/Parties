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

@SuppressWarnings("NullableProblems")
@Name("Party Description")
@Description("Get the description of the given party.")
@Examples({"send \"%description of party with name \"test\"%\"",
		"send \"%description of event-party%\""})
@Since("3.0.0")
public class ExprPartyDescription extends SimplePropertyExpression<Party, String> {
	static {
		register(ExprPartyDescription.class, String.class, "desc[ription]", "party");
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "description";
	}
	
	@Override
	public String convert(Party party) {
		return party.getDescription();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null) {
			Party party = getExpr().getSingle(e);
			String newDescription = (String) delta[0];
			switch (mode) {
				case SET:
					party.setDescription(newDescription);
					break;
				case DELETE:
					party.setDescription(null);
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
		return (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE) ? CollectionUtils.array(String.class) : null;
	}
}
