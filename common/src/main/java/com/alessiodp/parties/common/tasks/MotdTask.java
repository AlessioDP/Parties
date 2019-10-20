package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MotdTask implements Runnable {
	@NonNull private PartiesPlugin plugin;
	@NonNull private UUID playerUUID;
	@NonNull private UUID createID;

	@Override
	public void run() {
		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(playerUUID);
		User sender = plugin.getPlayer(playerUUID);
		if (sender != null
				&& ConfigParties.MOTD_ENABLE
				&& createID.equals(partyPlayer.getCreateID())
				&& !partyPlayer.getPartyName().isEmpty()) {
			PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyName());
				
			if (party != null && !party.getMotd().isEmpty()) {
				// Formatting motd
				StringBuilder motd = new StringBuilder();
				for (String str : party.getMotd().split(ConfigParties.MOTD_NEWLINECODE)) {
					motd.append(str).append("\n");
				}
				
				for (String line : Messages.ADDCMD_MOTD_CONTENT) {
					line = line.replace(PartiesConstants.PLACEHOLDER_PARTY_MOTD, "%temporary_motd%"); // Used to bypass tags from convertAllPlaceholders
					line = plugin.getMessageUtils().convertAllPlaceholders(line, party, partyPlayer);
					line = plugin.getColorUtils().convertColors(line);
					line = line.replace("%temporary_motd%", motd.toString());
					
					partyPlayer.sendMessage(line);
				}
			}
		}
	}
}
