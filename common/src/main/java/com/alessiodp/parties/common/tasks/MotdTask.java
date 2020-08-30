package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MotdTask implements Runnable {
	@NonNull private final PartiesPlugin plugin;
	@NonNull private final UUID playerUUID;
	@NonNull private final UUID createID;

	@Override
	public void run() {
		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(playerUUID);
		User sender = plugin.getPlayer(playerUUID);
		if (sender != null
				&& ConfigParties.ADDITIONAL_MOTD_ENABLE
				&& createID.equals(partyPlayer.getCreateID())) {
			PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
				
			if (party != null && party.getMotd() != null && !party.getMotd().isEmpty()) {
				// Formatting motd
				StringBuilder motd = new StringBuilder();
				for (String str : party.getMotd().split(ConfigParties.ADDITIONAL_MOTD_NEWLINECODE)) {
					motd.append(str).append("\n");
				}
				
				for (String line : Messages.ADDCMD_MOTD_CONTENT) {
					line = line.replace("%motd%", "%temporary_motd%"); // Used to bypass tags from convertAllPlaceholders
					line = plugin.getMessageUtils().convertPlaceholders(line, partyPlayer, party);
					line = Color.translateAlternateColorCodes(line);
					line = line.replace("%temporary_motd%", motd.toString());
					
					partyPlayer.sendMessage(line);
				}
			}
		}
	}
}
