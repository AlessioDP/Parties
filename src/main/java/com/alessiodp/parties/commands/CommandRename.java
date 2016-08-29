package com.alessiodp.parties.commands;

import java.util.UUID;
import java.util.regex.Pattern;

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

public class CommandRename implements CommandInterface{
	private Parties plugin;
	 
    public CommandRename(Parties parties) {
		plugin = parties;
	}
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.ADMIN_RENAME.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_RENAME.toString()));
			return true;
		}
		if (args.length != 3) {
			tp.sendMessage(Messages.rename_wrongcmd);
			return true;
		}
		if(!plugin.getPartyHandler().existParty(args[1])){
			tp.sendMessage(Messages.rename_noexist.replace("%party%", args[1]));
			return true;
		}		
		Party party = plugin.getPartyHandler().loadParty(args[1]);
		if(party == null){
			tp.sendMessage(Messages.rename_noexist);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		String partyName = args[2];
		if (partyName.length() > Variables.party_maxlengthname) {
			tp.sendMessage(Messages.create_toolongname);
			return true;
		}
		if (partyName.length() < Variables.party_minlengthname) {
			tp.sendMessage(Messages.create_tooshortname);
			return true;
		}
		if (Variables.censor_enable) {
			for (String cen : Variables.censor_contains) {
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().contains(cen.toLowerCase())) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				} else {
					if (partyName.contains(cen)) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				}
			}
			for (String cen : Variables.censor_startwith) {
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().startsWith(cen.toLowerCase())) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				} else {
					if (partyName.startsWith(cen)) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				}
			}
			for (String cen : Variables.censor_endwith) {
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().endsWith(cen.toLowerCase())) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				} else {
					if (partyName.endsWith(cen)) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				}
			}
		}
		if (!Pattern.compile(Variables.party_allowedchars).matcher(partyName).matches()) {
			tp.sendMessage(Messages.create_invalidname);
			return true;
		}
		
		if (plugin.getPartyHandler().existParty(partyName)) {
			tp.sendMessage(Messages.create_alreadyexist.replace("%party%", partyName));
			return true;
		}
		if(!Variables.database_type.equalsIgnoreCase("none")){
			plugin.getConfigHandler().getData().renameParty(args[1], partyName);
			for(UUID uuid : party.getMembers())
				plugin.getConfigHandler().getData().setPartyName(uuid, partyName);
		}
		for(Player player : party.getOnlinePlayers()){
			plugin.getPlayerHandler().getThePlayer(player).setPartyName(partyName);
		}
		party.setName(partyName);
		plugin.getPartyHandler().listParty.remove(args[1]);
		plugin.getPartyHandler().listParty.remove(party.getName());
		plugin.getPartyHandler().listParty.put(partyName, party);
		plugin.getPartyHandler().scoreboard_refreshParty(partyName);
		
		party.sendBroadcastParty(p, Messages.rename_broadcast);
		tp.sendMessage(Messages.rename_renamed.replace("%old%", args[1]).replace("%party%", party.getName()));
		
		LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] renamed the party " + args[1] + " in " + party.getName());
		return true;
	}
}