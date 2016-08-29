package com.alessiodp.parties.commands;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandList implements CommandInterface{
	private Parties plugin;
	 
    public CommandList(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.LIST.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.LIST.toString()));
			return true;
		}
		if(args.length > 2){
			tp.sendMessage(Messages.list_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		ArrayList<Party> parties = new ArrayList<Party>();
		for(Entry<String,Party> entry : plugin.getPartyHandler().listParty.entrySet()){
			Party party = entry.getValue();
			if(party == null)
				continue;
			if(party.getOnlinePlayers().size() >= Variables.list_filter)
				parties.add(party);
		}
		switch(Variables.list_orderedby.toLowerCase()){
		case "kills":
			if(!Variables.kill_enable)
				parties = orderName(parties);
			else
				parties = orderKills(parties);
			break;
		case "players":
			parties = orderPlayers(parties);
			break;
		case "allplayers":
			parties = orderAllPlayers(parties);
			break;
		default:
			parties = orderName(parties);
		}
		
		int page = 1;
		int maxpages = parties.size()%Variables.list_maxpages == 0 ? parties.size()/Variables.list_maxpages : parties.size()/Variables.list_maxpages+1;
		if(args.length>1){
			try{
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex){
				tp.sendMessage(Messages.list_wrongcmd);
				return true;
			}
			if(page > maxpages)
				page = maxpages;
			else if(page < 1)
				page = 1;
		}
		if(parties.size() == 0)
			maxpages=1;
		int c=0;
		tp.sendMessage(Messages.list_header.replace("%number%", ""+parties.size()).replace("%page%", ""+page).replace("%maxpages%", ""+maxpages));
		tp.sendMessage(Messages.list_subheader.replace("%number%", ""+parties.size()).replace("%page%", ""+page).replace("%maxpages%", ""+maxpages));
		
		for(Party party : parties){
			if(c >= (page-1)*Variables.list_maxpages && c < (page-1)*Variables.list_maxpages+Variables.list_maxpages){
				tp.sendMessage(Messages.list_formatparty
						.replace("%party%", party.getName())
						.replace("%desc%", party.getDescription())
						.replace("%players%", ""+party.getOnlinePlayers().size())
						.replace("%allplayers%", ""+party.getMembers().size())
						.replace("%kills%", ""+party.getKills()));
			}
			c++;
		}
		if(parties.size() == 0)
			tp.sendMessage(Messages.list_offline);
		tp.sendMessage(Messages.list_footer.replace("%number%", ""+parties.size()).replace("%page%", ""+page).replace("%maxpages%", ""+maxpages));
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] used command list");
		return true;
	}
	
	private ArrayList<Party> orderName(ArrayList<Party> list){
		int from;
		
		for(int c=0; c < list.size() - 1;c++)
		 {
		     from = c;

		     for (int c2=c+1 ; c2<list.size(); c2++)
		     {
		         if(list.get(c2).getName().trim().compareTo(list.get(from).getName().trim())<0)
		         {
		        	 from = c2;  
		         }
		     }
		     if(from != c)
		     {
		         Party temp = list.get(c);
		         list.set(c, list.get(from)); 
		         list.set(from, temp);
		     }
		 }
		return list;
	}
	private ArrayList<Party> orderPlayers(ArrayList<Party> list){
		int from;
		
		for(int c=0; c < list.size() - 1;c++)
		 {
		     from = c;

		     for (int c2=c+1 ; c2<list.size(); c2++)
		     {
		         if(list.get(c2).getOnlinePlayers().size()>list.get(from).getOnlinePlayers().size())
		         {
		        	 from = c2;  
		         }
		     }
		     if(from != c)
		     {
		         Party temp = list.get(c);
		         list.set(c, list.get(from)); 
		         list.set(from, temp);
		     }
		 }
		return list;
	}
	private ArrayList<Party> orderAllPlayers(ArrayList<Party> list){
		int from;
		
		for(int c=0; c < list.size() - 1;c++)
		 {
		     from = c;

		     for (int c2=c+1 ; c2<list.size(); c2++)
		     {
		         if(list.get(c2).getMembers().size()>list.get(from).getMembers().size())
		         {
		        	 from = c2;  
		         }
		     }
		     if(from != c)
		     {
		         Party temp = list.get(c);
		         list.set(c, list.get(from)); 
		         list.set(from, temp);
		     }
		 }
		return list;
	}
	private ArrayList<Party> orderKills(ArrayList<Party> list){
		int from;
		
		for(int c=0; c < list.size() - 1;c++)
		 {
		     from = c;

		     for (int c2=c+1 ; c2<list.size(); c2++)
		     {
		         if(list.get(c2).getKills() > list.get(from).getKills())
		         {
		        	 from = c2;  
		         }
		     }
		     if(from != c)
		     {
		         Party temp = list.get(c);
		         list.set(c, list.get(from)); 
		         list.set(from, temp);
		     }
		 }
		return list;
	}
}