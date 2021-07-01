package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;

import java.util.ArrayList;
import java.util.List;

public class CommandTag extends PartiesSubCommand {
	private final String syntaxOthers;
	
	public CommandTag(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.TAG,
				PartiesPermission.USER_TAG,
				ConfigMain.COMMANDS_SUB_TAG,
				false
		);
		
		syntax = String.format("%s <%s/%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_TAG,
				ConfigMain.COMMANDS_MISC_REMOVE
		);
		
		syntaxOthers = String.format("%s [%s] <%s/%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY,
				Messages.PARTIES_SYNTAX_TAG,
				ConfigMain.COMMANDS_MISC_REMOVE
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_TAG;
		help = Messages.HELP_ADDITIONAL_COMMANDS_TAG;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_TAG_OTHERS))
			return syntaxOthers;
		return syntax;
	}
	
	@Override
	public String getConsoleSyntax() {
		return syntaxOthers;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party;
		
		// Command handling
		String tag = null;
		if (commandData.getArgs().length == 3 && sender.hasPermission(PartiesPermission.ADMIN_TAG_OTHERS)) {
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
			if (party != null) {
				if (!commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE)) {
					tag = commandData.getArgs()[2];
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", commandData.getArgs()[1]));
				return;
			}
		} else if (commandData.getArgs().length == 2) {
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId());
			
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
			
			if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_TAG)) {
				return;
			}
			
			if (!commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE)) {
				tag = commandData.getArgs()[1];
			}
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
			return;
		}
		
		if (tag != null) {
			if (!CensorUtils.checkAllowedCharacters(ConfigParties.ADDITIONAL_TAG_ALLOWEDCHARS, tag, PartiesConstants.DEBUG_CMD_TAG_REGEXERROR_AC)
					|| (tag.length() > ConfigParties.ADDITIONAL_TAG_MAXLENGTH)
					|| (tag.length() < ConfigParties.ADDITIONAL_TAG_MINLENGTH)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TAG_INVALID);
				return;
			}
			if (CensorUtils.checkCensor(ConfigParties.ADDITIONAL_TAG_CENSORREGEX, tag, PartiesConstants.DEBUG_CMD_TAG_REGEXERROR_CEN)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TAG_CENSORED);
				return;
			}
		}
		
		if (tag != null
				&& ConfigParties.ADDITIONAL_TAG_MUST_BE_UNIQUE
				&& ((PartiesPlugin) plugin).getPartyManager().existsTag(tag)) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TAG_ALREADY_USED.replace("%tag%", tag));
			return;
		}
		
		if (!sender.hasPermission(PartiesPermission.ADMIN_TAG_OTHERS)
				&& ((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.TAG, partyPlayer, commandData.getCommandLabel(), commandData.getArgs())) {
			return;
		}
		
		// Command starts
		party.setTag(tag);
		
		if (tag == null) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TAG_REMOVED);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_TAG_REM,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TAG_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_TAG_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_TAG,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_MISC_REMOVE);
		}
		return ret;
	}
}