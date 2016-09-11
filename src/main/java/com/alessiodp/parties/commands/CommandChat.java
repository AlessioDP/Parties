package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandChat implements CommandInterface{
	private Parties plugin;
	 
    public CommandChat(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.CHAT.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.CHAT.toString()));
			return true;
		}
		if (!tp.haveParty()) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		if(args.length > 1){
			if(args[1].equalsIgnoreCase(Variables.command_sub_on))
				tp.setChatParty(true);
			else if(args[1].equalsIgnoreCase(Variables.command_sub_off))
				tp.setChatParty(false);
			else {
				tp.sendMessage(Messages.chat_wrongcmd);
				return true;
			}
		} else {
			tp.setChatParty(!tp.chatParty());
		}
		if(tp.chatParty()){
			tp.sendMessage(Messages.chat_enabled);
			LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] toggled party chat in true");
		} else {
			tp.sendMessage(Messages.chat_disabled);
			LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] toggled party chat in false");
		}
		return true;
	}
}
