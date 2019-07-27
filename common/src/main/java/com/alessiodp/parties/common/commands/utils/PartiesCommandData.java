package com.alessiodp.parties.common.commands.utils;

import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;
import lombok.Setter;

public class PartiesCommandData extends CommandData {
	@Getter @Setter private PartyPlayerImpl partyPlayer;
	@Getter @Setter private PartyImpl party;
}
