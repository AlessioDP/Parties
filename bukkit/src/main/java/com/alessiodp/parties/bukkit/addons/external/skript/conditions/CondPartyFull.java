package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.alessiodp.parties.api.interfaces.Party;

@SuppressWarnings("NullableProblems")
@Name("Party is Full")
@Description("Checks if a party is full.")
@Examples({
		"if party is full:",
		"\tmessage \"The party %party% is full\""
})
@Since("3.0.0")
public class CondPartyFull extends PropertyCondition<Party> {
	static {
		register(CondPartyFull.class, "full", "party");
	}
	
	@Override
	public boolean check(Party party) {
		return party.isFull();
	}
	
	@Override
	protected String getPropertyName() {
		return "full";
	}
}
