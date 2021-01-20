package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;

@Name("Player is in Party")
@Description("Checks if a player is in party.")
@Examples({
		"if player is in party:",
		"\tmessage \"The player %player% is in a party\""
})
@Since("3.0.0")
public class CondInParty extends PropertyCondition<OfflinePlayer> {
	static {
		register(CondInParty.class, "in [a] party", "offlineplayers");
	}
	
	@Override
	public boolean check(OfflinePlayer o) {
		PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(o.getUniqueId());
		return partyPlayer != null && partyPlayer.isInParty();
	}
	
	@Override
	protected String getPropertyName() {
		return "in party";
	}
}
