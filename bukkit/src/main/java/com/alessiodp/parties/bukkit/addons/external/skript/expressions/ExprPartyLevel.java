package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.alessiodp.parties.api.interfaces.Party;

@Name("Party Level")
@Description("Get the level number of the given party.")
@Examples({"send \"%level of party with name \"test\"%\"",
		"send \"%level of event-party%\""})
@Since("3.0.0")
public class ExprPartyLevel extends SimplePropertyExpression<Party, Integer> {
	static {
		register(ExprPartyLevel.class, Integer.class, "level", "party");
	}
	
	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "level";
	}
	
	@Override
	public Integer convert(Party party) {
		return party.getLevel();
	}
}
