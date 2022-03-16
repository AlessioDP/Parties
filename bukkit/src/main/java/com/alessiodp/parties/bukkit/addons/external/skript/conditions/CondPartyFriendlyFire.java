package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.alessiodp.parties.api.interfaces.Party;

@SuppressWarnings("NullableProblems")
@Name("Party is Friendly Fire Protected")
@Description("Checks if a party is friendly fire protected.")
@Examples({
		"if party is friendly fire protected:",
		"\tmessage \"The party %party% is protected from friendly fire\""
})
@Since("3.0.0")
public class CondPartyFriendlyFire extends PropertyCondition<Party> {
	static {
		register(CondPartyFriendlyFire.class, "[friendly[-]fire] protected [from friendly[-]fire]", "party");
	}
	
	@Override
	public boolean check(Party party) {
		return party.isFriendlyFireProtected();
	}
	
	@Override
	protected String getPropertyName() {
		return "friendly fire protected";
	}
}
