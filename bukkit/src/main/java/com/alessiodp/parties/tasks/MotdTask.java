package com.alessiodp.parties.tasks;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class MotdTask extends BukkitRunnable {
	private Parties plugin;
	private Player pl;
	private UUID createID;
	
	public MotdTask(Parties plugin, Player player, UUID create) {
		this.plugin = plugin; 
		this.pl = player;
		createID = create;
	}

	@Override
	public void run() {
		if (pl != null && pl.isOnline()) {
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(pl.getUniqueId());
			
			if (createID.equals(pp.getCreateID()) && !pp.getPartyName().isEmpty()) {
				PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
				if (party != null && !party.getMotd().isEmpty()) {
					
					StringBuilder sb = new StringBuilder();
					for (String str : Messages.ADDCMD_MOTD_CONTENT) {
						sb.append(str.replace(Constants.PLACEHOLDER_PARTY_MOTD, "%temporary_motd%") + "\n");
					}
					
					String message = PartiesUtils.convertAllPlaceholders(sb.toString(), party, pp);
					
					sb = new StringBuilder();
					for (String str : party.getMotd().split(ConfigParties.MOTD_NEWLINECODE)) {
						sb.append(str + "\n");
					}
					message = message.replace("%temporary_motd%", sb.toString());
					
					pp.sendMessage(message);
				}
			}
		}
	}
}
