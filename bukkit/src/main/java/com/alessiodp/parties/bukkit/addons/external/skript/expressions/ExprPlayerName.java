package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

@Name("Party Player Name")
@Description("Get the name of the given partyplayer.")
@Examples({"send \"%name of partyplayer player%\"",
		"send \"%name of event-partyplayer%\""})
@Since("3.0.0")
public class ExprPlayerName extends SimplePropertyExpression<PartyPlayer, String> {
	static {
		register(ExprPlayerName.class, String.class, "name", "partyplayer");
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
	public String convert(PartyPlayer partyPlayer) {
		return partyPlayer.getName();
	}
}
