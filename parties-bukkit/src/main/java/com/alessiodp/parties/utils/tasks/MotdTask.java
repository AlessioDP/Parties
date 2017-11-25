package com.alessiodp.parties.utils.tasks;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class MotdTask extends BukkitRunnable {
	private Parties plugin;
	private Player pl;
	private UUID createID;

	public MotdTask(Parties plugin, Player player, UUID create) {
		this.plugin = plugin; 
		this.pl = player;
		createID = create;
	}

	public void run() {
		if (pl != null && pl.isOnline()) {
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(pl.getUniqueId());
			
			if (createID.equals(tp.getCreateID()) && !tp.getPartyName().isEmpty()) {
				Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
				if (party != null && !party.getMOTD().isEmpty()) {
					String res = (Messages.motd_header.isEmpty() ? "" : Messages.motd_header + "\n");
					for (String str : party.getMOTD().split(Variables.motd_newline)) {
						res = res + Messages.motd_color + str;
					}
					res = res + (Messages.motd_footer.isEmpty() ? "" : "\n" + Messages.motd_footer);
					tp.sendMessage(res);
				}
			}
		}
	}
}
