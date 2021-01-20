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

@Name("Party Name")
@Description("Get the name of the given party.")
@Examples({"send \"%name of party with name \"test\"%\"",
		"send \"%name of event-party%\""})
@Since("3.0.0")
public class ExprPartyName extends SimplePropertyExpression<Party, String> {
	static {
		register(ExprPartyName.class, String.class, "name", "party");
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "name";
	}
	
	@Override
	public String convert(Party party) {
		return party.getName();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null) {
			Party party = getExpr().getSingle(e);
			String newName = (String) delta[0];
			switch (mode) {
				case SET:
					party.rename(newName);
					break;
				case DELETE:
					party.rename(null);
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
