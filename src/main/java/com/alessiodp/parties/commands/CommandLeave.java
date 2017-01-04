package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandLeave implements CommandInterface{
	private Parties plugin;
	 
    public CommandLeave(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		
		if(!p.hasPermission(PartiesPermissions.LEAVE.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.LEAVE.toString()));
			return true;
		}
		if (!tp.haveParty()) {
			tp.sendMessage(Messages.noparty);
			return true;
		}

		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());

		party.getMembers().remove(p.getUniqueId());
		party.getOnlinePlayers().remove(p);
		
		tp.removePlayer();

		party.sendBroadcastParty(p, Messages.leave_playerleaved);
		party.sendSpyMessage(p, Messages.leave_playerleaved);

		tp.sendMessage(Messages.leave_byeplayer.replace("%party%", party.getName()));

		if (party.getLeader().equals(p.getUniqueId())) {
			party.sendBroadcastParty(p, Messages.leave_disbanded);
			party.sendSpyMessage(p, Messages.leave_disbanded);
			plugin.log(ConsoleColors.CYAN.getCode() + "Party " + party.getName() + " deleted via leave, by: " + p.getName());
			party.removeParty();
			
			plugin.getPartyHandler().tag_removePlayer(p, null);
			LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] deleted " + party.getName() + " via leave");
			return true;
		}
		
		party.updateParty();
		plugin.getPartyHandler().tag_removePlayer(p, party);
		
		LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] leaved the party " + party.getName());
		return true;
	}
}
