package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class MotdTask implements Runnable {
	@NotNull private final PartiesPlugin plugin;
	@NotNull private final UUID playerUUID;
	@NotNull private final UUID createID;

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
				String motd = party.getMotd();
				motd = motd.replace(ConfigParties.ADDITIONAL_MOTD_NEWLINECODE, "\n");
				
				for (String line : Messages.ADDCMD_MOTD_CONTENT) {
					line = line.replace("%motd%", "%temporary_motd%"); // Used to bypass tags from convertAllPlaceholders
					line = plugin.getMessageUtils().convertPlaceholders(line, partyPlayer, party);
					line = Color.translateAlternateColorCodes(line);
					line = line.replace("%temporary_motd%", motd);
					
					partyPlayer.sendMessage(line);
				}
			}
		}
	}
}
