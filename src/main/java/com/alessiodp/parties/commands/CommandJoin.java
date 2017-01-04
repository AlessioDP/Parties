package com.alessiodp.parties.commands;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandJoin implements CommandInterface{
	private Parties plugin;
	 
    public CommandJoin(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.JOIN.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.JOIN.toString()));
			return true;
		}
		if (tp.haveParty() && !Variables.password_bypassleave) {
			tp.sendMessage(Messages.join_alreadyinparty);
			return true;
		}
		if(args.length < 2 || args.length > 3){
			tp.sendMessage(Messages.join_wrongcmd);
			return true;
		}
		String partyname = args[1];
		Party party = plugin.getPartyHandler().loadParty(partyname);
		if (party == null) {
			tp.sendMessage(Messages.join_noexist);
			return true;
		}
		if(args.length == 2){
			if(!p.hasPermission(PartiesPermissions.JOIN_OTHERS.toString())){
				if(party.getPassword() != null && !party.getPassword().isEmpty()){
					tp.sendMessage(Messages.join_wrongpw);
					return true;
				}
			}
		} else {
			if(!hash(args[2]).equals(party.getPassword())){
				tp.sendMessage(Messages.join_wrongpw);
				return true;
			}
		}
		if((Variables.party_maxmembers != -1) && (party.getMembers().size() >= Variables.party_maxmembers)){
			tp.sendMessage(Messages.join_maxplayers);
			return true;
		}
		if(Variables.password_bypassleave && tp.haveParty()){
			/* From CommandLeave */
			Party party_2 = plugin.getPartyHandler().loadParty(tp.getPartyName());
			if(party_2!=null){
				party_2.getMembers().remove(p.getUniqueId());
				party_2.getOnlinePlayers().remove(p);

				party_2.sendBroadcastParty(p, Messages.leave_playerleaved);
				party_2.sendSpyMessage(p, Messages.leave_playerleaved);

				tp.sendMessage(Messages.leave_byeplayer.replace("%party%", party_2.getName()));

				if (party_2.getLeader().equals(p.getUniqueId())) {
					party_2.sendBroadcastParty(p, Messages.leave_disbanded);
					party_2.sendSpyMessage(p, Messages.leave_disbanded);
					plugin.log(ConsoleColors.CYAN.getCode() + "Party " + party_2.getName() + " deleted via leave(join), by: " + p.getName());
					party_2.removeParty();
					
					plugin.getPartyHandler().tag_removePlayer(p, null);
					LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] deleted " + party_2.getName() + " via leave(join)");
				} else {
					party_2.updateParty();
					plugin.getPartyHandler().tag_removePlayer(p, party_2);
				}
			}
			/* End of CommandLeave */
		}
		tp.sendMessage(Messages.join_joined);
		
		party.sendBroadcastParty(tp.getPlayer(), Messages.join_playerjoined);
		party.sendSpyMessage(tp.getPlayer(), Messages.join_playerjoined);
				
		party.getMembers().add(tp.getUUID());
		party.getOnlinePlayers().add(tp.getPlayer());

		tp.setHaveParty(true);
		tp.setPartyName(party.getName());
		tp.setRank(Variables.rank_default);
				
		party.updateParty();
		tp.updatePlayer();
				
		plugin.getPartyHandler().tag_addPlayer(p, party);
		
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] joined in the party " + party.getName());
		return true;
	}
	
	private String hash(String text){
		byte[] result = null;
		try {
			result = MessageDigest.getInstance(Variables.password_hash).digest(text.getBytes(Variables.password_encode));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; ++i) {
	        	sb.append(Integer.toHexString((result[i] & 0xFF) | 0x100).substring(1,3));
	        }
			return sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
