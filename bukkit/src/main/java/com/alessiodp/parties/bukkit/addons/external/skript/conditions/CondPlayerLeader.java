package com.alessiodp.parties.bukkit.addons.external.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

@SuppressWarnings("NullableProblems")
@Name("Party Player is Leader")
@Description("Checks if the partyplayer is leader.")
@Examples({
		"if event-partyplayer is leader:",
		"\tmessage \"The player %name of event-partyplayer% is leader of %party of event-partyplayer%\""
})
@Since("3.0.0")
public class CondPlayerLeader extends PropertyCondition<PartyPlayer> {
	static {
		register(CondPlayerLeader.class, "leader", "partyplayer");
	}
	
	@Override
	public boolean check(PartyPlayer partyPlayer) {
		if (partyPlayer.isInParty()) {
			Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
			if (party != null) {
				return partyPlayer.getPlayerUUID().equals(party.getLeader());
			}
		}
		return false;
	}
	
	@Override
	protected String getPropertyName() {
		return "leader";
	}
}
