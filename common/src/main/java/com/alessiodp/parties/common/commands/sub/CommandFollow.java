package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.PartiesUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandFollow extends AbstractCommand {
	
	public CommandFollow(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.FOLLOW.toString())) {
			pp.sendNoPermission(PartiesPermission.FOLLOW);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_FOLLOW))
			return false;
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		Boolean follow = PartiesUtils.handleOnOffCommand(party.getFollowEnabled(), commandData.getArgs());
		if (follow == null) {
			pp.sendMessage(Messages.ADDCMD_FOLLOW_WRONGCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		party.setFollowEnabled(follow);
		party.updateParty();
		
		if (follow)
			pp.sendMessage(Messages.ADDCMD_FOLLOW_ON);
		else
			pp.sendMessage(Messages.ADDCMD_FOLLOW_OFF);
		
		LoggerManager.log(LogLevel.MEDIUM,
				(follow ? Constants.DEBUG_CMD_FOLLOW_ON : Constants.DEBUG_CMD_FOLLOW_OFF)
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()),
				true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return PartiesUtils.tabCompleteOnOff(args);
	}
}
