package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.alessiodp.parties.api.interfaces.Party;

@SuppressWarnings("NullableProblems")
@Name("Party ID")
@Description("Get the id of the given party.")
@Examples({"send \"%id of party with name \"test\"%\"",
		"send \"%id of event-party%\""})
@Since("3.0.0")
public class ExprPartyId extends SimplePropertyExpression<Party, String> {
	static {
		register(ExprPartyId.class, String.class, "[uu]id", "party");
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "id";
	}
	
	@Override
	public String convert(Party party) {
		return party.getId().toString();
	}
}
