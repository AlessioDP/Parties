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

	public MotdTask(Parties plugin, Player player) {
		this.plugin = plugin; 
		this.pl = player;
		createID = plugin.getPlayerHandler().getThePlayer(pl).getCreateID();
	}

	public void run() {
		if(pl == null || !pl.isOnline())
			return;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(pl);
		if(!createID.equals(tp.getCreateID())){
			return;
		}
		if(tp.getPartyName().isEmpty())
			return;
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
		if(!party.getMOTD().isEmpty()){
			tp.sendMessage(Messages.motd_header);
			for(String str : party.getMOTD().split(Variables.motd_newline)){
				tp.sendMessage(Messages.motd_color.concat(str));
			}
			tp.sendMessage(Messages.motd_footer);
		}
	}
}
