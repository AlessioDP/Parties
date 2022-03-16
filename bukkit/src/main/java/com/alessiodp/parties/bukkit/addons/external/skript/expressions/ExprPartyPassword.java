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
@Name("Party Password")
@Description("Get the password of the given party. The password is hashed, if you set one you must insert a plain password.")
@Examples({"send \"%password of party with name \"test\"%\"",
		"send \"%password of event-party%\""})
@Since("3.0.0")
public class ExprPartyPassword extends SimplePropertyExpression<Party, String> {
	static {
		register(ExprPartyPassword.class, String.class, "password", "party");
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "password";
	}
	
	@Override
	public String convert(Party party) {
		return party.getPassword();
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null) {
			Party party = getExpr().getSingle(e);
			String newPassword = (String) delta[0];
			switch (mode) {
				case SET:
					party.setPasswordUnhashed(newPassword);
					break;
				case DELETE:
					party.setPasswordUnhashed(null);
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
