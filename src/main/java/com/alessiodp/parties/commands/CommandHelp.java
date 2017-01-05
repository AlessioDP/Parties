package com.alessiodp.parties.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandHelp implements CommandInterface{
	private Parties plugin;
	 
    public CommandHelp(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		
		if(!p.hasPermission(PartiesPermissions.HELP.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.HELP.toString()));
			return true;
		}		
		ArrayList<String> str = new ArrayList<String>();
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		
		if (tp.haveParty()) {
			if(p.hasPermission(PartiesPermissions.HELP.toString()))
				str.add(Messages.help_help);
			if (p.hasPermission(PartiesPermissions.SENDMESSAGE.toString()) && r.havePermission(PartiesPermissions.PRIVATE_SENDMESSAGE.toString()))
				str.add(Messages.help_p);
			if (p.hasPermission(PartiesPermissions.LEAVE.toString()))
				str.add(Messages.help_leave);
			if (p.hasPermission(PartiesPermissions.INFO.toString()))
				str.add(Messages.help_info);
			if (p.hasPermission(PartiesPermissions.MEMBERS.toString()))
				str.add(Messages.help_members);
			if (p.hasPermission(PartiesPermissions.CHAT.toString()))
				str.add(Messages.help_chat);
			if (p.hasPermission(PartiesPermissions.LIST.toString()))
				str.add(Messages.help_list);
			if(p.hasPermission(PartiesPermissions.HOME_OTHERS.toString()))
				str.add(Messages.help_home_others);
			else
				if(p.hasPermission(PartiesPermissions.HOME.toString()) && r.havePermission(PartiesPermissions.PRIVATE_HOME.toString()))
					str.add(Messages.help_home);
			if (p.hasPermission(PartiesPermissions.SETHOME.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_HOME.toString()))
				str.add(Messages.help_sethome);
			if (p.hasPermission(PartiesPermissions.INVITE.toString()) && r.havePermission(PartiesPermissions.PRIVATE_INVITE.toString()))
				str.add(Messages.help_invite);
			if(p.hasPermission(PartiesPermissions.DESC.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_DESC.toString()))
				str.add(Messages.help_desc);
			if(p.hasPermission(PartiesPermissions.MOTD.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_MOTD.toString()))
				str.add(Messages.help_motd);
			if(Variables.password_enable && p.hasPermission(PartiesPermissions.PASSWORD.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_PASSWORD.toString()))
				str.add(Messages.help_password);
			if (p.hasPermission(PartiesPermissions.RANK.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_RANK.toString()))
				str.add(Messages.help_rank);
			if (p.hasPermission(PartiesPermissions.PREFIX.toString()) && Variables.tag_enable && !Variables.tag_system && Variables.tag_custom_prefix && r.havePermission(PartiesPermissions.PRIVATE_EDIT_PREFIX.toString()))
				str.add(Messages.help_prefix);
			if (p.hasPermission(PartiesPermissions.SUFFIX.toString()) && Variables.tag_enable && !Variables.tag_system && Variables.tag_custom_suffix && r.havePermission(PartiesPermissions.PRIVATE_EDIT_SUFFIX.toString()))
				str.add(Messages.help_suffix);
			if (p.hasPermission(PartiesPermissions.KICK.toString()) && r.havePermission(PartiesPermissions.PRIVATE_KICK.toString()))
				str.add(Messages.help_kick);
			if(p.hasPermission(PartiesPermissions.CLAIM.toString()) && Variables.griefprevention_enable  && r.havePermission(PartiesPermissions.PRIVATE_CLAIM.toString()))
				str.add(Messages.help_claim);
		} else {
			if (p.hasPermission(PartiesPermissions.CREATE.toString())){
				if(p.hasPermission(PartiesPermissions.ADMIN_CREATE_FIXED.toString()))
					str.add(Messages.help_createfixed);
				else
					str.add(Messages.help_create);
			}
			if (Variables.password_enable && p.hasPermission(PartiesPermissions.JOIN.toString()))
				str.add(Messages.help_join);
			if (p.hasPermission(PartiesPermissions.ACCEPT.toString()))
				str.add(Messages.help_accept);
			if (p.hasPermission(PartiesPermissions.DENY.toString()))
				str.add(Messages.help_deny);
			if (p.hasPermission(PartiesPermissions.IGNORE.toString()))
				str.add(Messages.help_ignore);
			if(p.hasPermission(PartiesPermissions.HOME_OTHERS.toString()))
				str.add(Messages.help_home_others);
		}
		if (p.hasPermission(PartiesPermissions.ADMIN_SPY.toString()))
			str.add(Messages.help_spy);
		if (p.hasPermission(PartiesPermissions.ADMIN_DELETE.toString()))
			str.add(Messages.help_delete);
		if (p.hasPermission(PartiesPermissions.ADMIN_RENAME.toString()))
			str.add(Messages.help_rename);
		if (p.hasPermission(PartiesPermissions.ADMIN_RELOAD.toString()))
			str.add(Messages.help_reload);
		if(p.hasPermission(PartiesPermissions.ADMIN_MIGRATE.toString()) && !Variables.database_migrate_console)
			str.add(Messages.help_migrate);
		
		int page = 1;
		int maxpages = str.size()%Variables.permissionspagehelp == 0 ? str.size()/Variables.permissionspagehelp : str.size()/Variables.permissionspagehelp+1;
		if(args.length>1){
			try{
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex){
				page = 1;
			}
			if(page > maxpages || page < 1)
				page = maxpages;
		}
		if(str.size() == 0)
			maxpages=1;
		int c=0;
		tp.sendMessage(Messages.help_header.replace("%page%", ""+page).replace("%maxpages%", ""+maxpages));
		
		for(String string : str){
			if(c >= (page-1)*Variables.permissionspagehelp && c < (page-1)*Variables.permissionspagehelp+Variables.permissionspagehelp){
				tp.sendMessage(string);
			}
			c++;
		}
		return true;
	}
}
