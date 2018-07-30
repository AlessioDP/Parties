package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

import java.util.UUID;

public class MotdTask implements Runnable {
	private PartiesPlugin plugin;
	private UUID playerUUID;
	private UUID createID;
	
	public MotdTask(PartiesPlugin plugin, UUID playerUUID, UUID createID) {
		this.plugin = plugin; 
		this.playerUUID = playerUUID;
		this.createID = createID;
	}

	@Override
	public void run() {
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(playerUUID);
		User sender = plugin.getPlayer(playerUUID);
		if (sender != null) {
			
			if (createID.equals(pp.getCreateID()) && !pp.getPartyName().isEmpty()) {
				PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
				
				if (party != null) {
					if (!party.getMotd().isEmpty()) {
						// Formatting motd
						StringBuilder motd = new StringBuilder();
						for (String str : party.getMotd().split(ConfigParties.MOTD_NEWLINECODE)) {
							motd.append(str)
									.append("\n");
						}
						
						for (String line : Messages.ADDCMD_MOTD_CONTENT) {
							line = line.replace(Constants.PLACEHOLDER_PARTY_MOTD, "%temporary_motd%"); // Used to bypass tags from convertAllPlaceholders
							line = plugin.getMessageUtils().convertAllPlaceholders(line, party, pp);
							line = plugin.getMessageUtils().convertColors(line);
							line = line.replace("%temporary_motd%", motd.toString());
							
							pp.sendMessage(line);
						}
					}
				}
			}
		}
	}
}
