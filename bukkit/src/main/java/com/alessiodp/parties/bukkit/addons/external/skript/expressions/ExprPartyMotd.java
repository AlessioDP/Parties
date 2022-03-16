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
@Name("Party MOTD")
@Description("Get the motd of the given party.")
@Examples({"send \"%motd of party with name \"test\"%\"",
		"send \"%motd of event-party%\""})
@Since("3.0.0")
public class ExprPartyMotd extends SimplePropertyExpression<Party, String> {
	static {
		register(ExprPartyMotd.class, String.class, "motd", "party");
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "motd";
	}
	
	@Override
	public String convert(Party party) {
		return party.getMotd();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null) {
			Party party = getExpr().getSingle(e);
			String newMotd = (String) delta[0];
			switch (mode) {
				case SET:
					party.setMotd(newMotd);
					break;
				case DELETE:
					party.setMotd(null);
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
