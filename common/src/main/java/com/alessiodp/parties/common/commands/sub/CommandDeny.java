package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.interfaces.PartyAskRequest;
import com.alessiodp.parties.api.interfaces.PartyInvite;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.HashMap;
import java.util.Map;

public class CommandDeny extends PartiesSubCommand {
	
	public CommandDeny(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DENY,
				PartiesPermission.USER_DENY,
				ConfigMain.COMMANDS_CMD_DENY,
				false
		);
		
		if (ConfigParties.GENERAL_ASK_ENABLE) {
			syntax = String.format("%s [%s/%s]",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_PARTY,
					Messages.PARTIES_SYNTAX_PLAYER
			);
		} else {
			syntax = String.format("%s [%s]",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_PARTY
			);
		}
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_DENY;
		help = Messages.HELP_MAIN_COMMANDS_DENY;
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
		
		if (partyPlayer.getPartyId() != null) {
			if (!ConfigParties.GENERAL_ASK_ENABLE) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
				return false;
			} else if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ASK_DENY)) {
				return false;
			} else {
				((PartiesCommandData) commandData).setParty(((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId()));
			}
		}
		
		if (commandData.getArgs().length > 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		if (partyPlayer.isInParty()) {
			if (!ConfigParties.GENERAL_ASK_ENABLE) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
			} else {
				// Deny ask request
				PartyImpl party = ((PartiesCommandData) commandData).getParty();
				HashMap<String, PartyAskRequest> pendingAskRequests = new HashMap<>();
				party.getAskRequests().forEach(pv -> pendingAskRequests.put(CommonUtils.toLowerCase(pv.getAsker().getName()), pv));
				
				if (commandData.getArgs().length > 1
						&& !pendingAskRequests.containsKey(CommonUtils.toLowerCase(commandData.getArgs()[1]))) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_NOEXISTS);
					return;
				}
				
				PartyAskRequest partyAskRequest;
				if (pendingAskRequests.size() > 0) {
					if (pendingAskRequests.size() == 1) {
						partyAskRequest = pendingAskRequests.values().iterator().next();
					} else if (commandData.getArgs().length > 1) {
						partyAskRequest = pendingAskRequests.get(CommonUtils.toLowerCase(commandData.getArgs()[1]));
					} else {
						// Missing player
						sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_MULTIPLEREQUESTS);
						for (Map.Entry<String, PartyAskRequest> entry : pendingAskRequests.entrySet()) {
							sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_MULTIPLEREQUESTS_PLAYER
									.replace("%player%", entry.getValue().getAsker().getName()), (PartyImpl) entry.getValue().getParty());
						}
						return;
					}
				} else {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_NOREQUEST);
					return;
				}
				
				if (partyAskRequest == null) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_NOEXISTS);
					return;
				}
				
				// Command starts
				partyAskRequest.accept(partyPlayer);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_DENY_ASK,
						partyPlayer.getName(), partyAskRequest.getAsker().getName(), partyAskRequest.getParty().getName()), true);
			}
		} else {
			// Deny invite request
			HashMap<String, PartyInvite> pendingInvites = new HashMap<>();
			partyPlayer.getPendingInvites().forEach(pv -> pendingInvites.put(CommonUtils.toLowerCase(pv.getParty().getName()), pv));
			
			if (commandData.getArgs().length > 1
					&& !pendingInvites.containsKey(CommonUtils.toLowerCase(commandData.getArgs()[1]))) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_NOEXISTS);
				return;
			}
			
			PartyInvite partyInvite;
			if (pendingInvites.size() > 0) {
				if (pendingInvites.size() == 1) {
					partyInvite = pendingInvites.values().iterator().next();
				} else if (commandData.getArgs().length > 1) {
					partyInvite = pendingInvites.get(CommonUtils.toLowerCase(commandData.getArgs()[1]));
				} else {
					// Missing party
					sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_MULTIPLEREQUESTS);
					for (Map.Entry<String, PartyInvite> entry : pendingInvites.entrySet()) {
						sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_MULTIPLEREQUESTS_PARTY
								.replace("%party%", entry.getKey()), (PartyImpl) entry.getValue().getParty());
					}
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_NOREQUEST);
				return;
			}
			
			if (partyInvite == null) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_DENY_NOEXISTS);
				return;
			}
			
			// Command starts
			partyInvite.deny();
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_DENY_INVITE,
					partyPlayer.getName(), partyInvite.getParty().getName()), true);
		}
	}
}
