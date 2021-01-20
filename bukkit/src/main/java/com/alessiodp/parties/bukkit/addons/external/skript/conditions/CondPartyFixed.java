package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.alessiodp.parties.api.interfaces.Party;

@Name("Party is Fixed")
@Description("Checks if a party is fixed.")
@Examples({
		"if party is fixed:",
		"\tmessage \"The party %party% is fixed\""
})
@Since("3.0.0")
public class CondPartyFixed extends PropertyCondition<Party> {
	static {
		register(CondPartyFixed.class, "fixed", "party");
	}
	
	@Override
	public boolean check(Party party) {
		return party.isFixed();
	}
	
	@Override
	protected String getPropertyName() {
		return "fixed";
	}
}
