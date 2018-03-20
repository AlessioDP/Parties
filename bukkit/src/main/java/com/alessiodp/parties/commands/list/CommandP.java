package com.alessiodp.parties.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.tasks.ChatTask;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.events.PartiesChatEvent;

public class CommandP implements ICommand {
	private Parties plugin;
	 
	public CommandP(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.SENDMESSAGE.toString())) {
			pp.sendNoPermission(PartiesPermission.SENDMESSAGE);
			return false;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_SENDMESSAGE))
			return false;
		
		if (commandData.getArgs().length == 0) {
			pp.sendMessage(Messages.MAINCMD_P_WRONGCMD);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		commandData.addPermission(PartiesPermission.KICK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		if (ConfigParties.GENERAL_CHAT_CHATCD > 0
				&& !PartiesUtils.checkPlayerRank(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = plugin.getPlayerManager().getChatCooldown().get(pp.getPlayerUUID());
			long unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				pp.sendMessage(Messages.MAINCMD_P_COOLDOWN
						.replace("%seconds%", String.valueOf(ConfigParties.GENERAL_CHAT_CHATCD - (unixNow - unixTimestamp))));
				return;
			}
			
			plugin.getPlayerManager().getChatCooldown().put(pp.getPlayerUUID(), unixNow);
			new ChatTask(pp.getPlayerUUID(), plugin.getPlayerManager()).runTaskLater(plugin, ConfigParties.GENERAL_CHAT_CHATCD * 20);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_CMD_P_TASK
					.replace("{value}", Integer.toString(ConfigParties.GENERAL_CHAT_CHATCD * 20))
					.replace("{player}", pp.getName()), true);
		}
		
		/*
		 * Command starts
		 */
		StringBuilder sb = new StringBuilder();
		for (String word : commandData.getArgs()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(word);
		}
		
		// Calling API event
		PartiesChatEvent partiesChatEvent = new PartiesChatEvent(pp, party, sb.toString());
		Bukkit.getServer().getPluginManager().callEvent(partiesChatEvent);
		String newMessage = partiesChatEvent.getMessage();
		if (!partiesChatEvent.isCancelled()) {
			party.sendPlayerMessage(pp, newMessage);
			
			if (ConfigMain.STORAGE_LOG_CHAT)
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_P
						.replace("{party}", party.getName())
						.replace("{player}", pp.getName())
						.replace("{message}", newMessage), true);
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_CHATEVENT_DENY
					.replace("{player}", pp.getName())
					.replace("{message}", sb.toString()), true);
	}
}
