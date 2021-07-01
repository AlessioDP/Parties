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
import com.alessiodp.parties.common.players.objects.PartyTeleportRequest;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.HashMap;
import java.util.Map;

public class CommandAccept extends PartiesSubCommand {
	private final String syntaxAskTeleport;
	
	public CommandAccept(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.ACCEPT,
				PartiesPermission.USER_ACCEPT,
				ConfigMain.COMMANDS_SUB_ACCEPT,
				false
		);
		
		syntax = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		syntaxAskTeleport = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PLAYER
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_ACCEPT;
		help = Messages.HELP_MAIN_COMMANDS_ACCEPT;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		PartyPlayerImpl player = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(user.getUUID());
		if (player != null
				&& player.isInParty()
				&& (ConfigParties.ADDITIONAL_ASK_ENABLE
						|| (ConfigParties.ADDITIONAL_TELEPORT_ENABLE && ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE))) {
			return syntaxAskTeleport;
		}
		return syntax;
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
		
		if (commandData.getArgs().length > 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
			return false;
		}
		
		if (partyPlayer.getPartyId() != null) {
			if ((ConfigParties.ADDITIONAL_ASK_ENABLE
						&& ((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ASK_ACCEPT)
					)
					|| (ConfigParties.ADDITIONAL_TELEPORT_ENABLE
						&& ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE
						&& ((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_TELEPORT_ACCEPT))) {
				((PartiesCommandData) commandData).setParty(((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId()));
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
				return false;
			}
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
			boolean noPendingRequests = false;
			if (ConfigParties.ADDITIONAL_ASK_ENABLE) {
				// Accept ask request
				PartyImpl party = ((PartiesCommandData) commandData).getParty();
				HashMap<String, PartyAskRequest> pendingAskRequests = new HashMap<>();
				party.getAskRequests().forEach(pv -> pendingAskRequests.put(CommonUtils.toLowerCase(pv.getAsker().getName()), pv));
				
				if (commandData.getArgs().length > 1
						&& !pendingAskRequests.containsKey(CommonUtils.toLowerCase(commandData.getArgs()[1]))) {
					noPendingRequests = true;
				} else {
					PartyAskRequest partyAskRequest = null;
					if (pendingAskRequests.size() > 0) {
						if (pendingAskRequests.size() == 1) {
							partyAskRequest = pendingAskRequests.values().iterator().next();
						} else if (commandData.getArgs().length > 1) {
							partyAskRequest = pendingAskRequests.get(CommonUtils.toLowerCase(commandData.getArgs()[1]));
						} else {
							// Missing player
							sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEREQUESTS);
							for (Map.Entry<String, PartyAskRequest> entry : pendingAskRequests.entrySet()) {
								sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEREQUESTS_PLAYER
										.replace("%player%", entry.getValue().getAsker().getName()), (PartyImpl) entry.getValue().getParty());
							}
							return;
						}
					} else {
						noPendingRequests = true;
					}
					
					if (partyAskRequest != null) {
						// Command starts
						partyAskRequest.accept(partyPlayer);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_ACCEPT_ASK,
								partyPlayer.getName(), partyAskRequest.getAsker().getName(), partyAskRequest.getParty().getName() != null ? partyAskRequest.getParty().getName() : "_"), true);
						return;
					}
				}
			}
			
			if (ConfigParties.ADDITIONAL_TELEPORT_ENABLE && ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE) {
				// Accept teleport request
				HashMap<String, PartyTeleportRequest> pendingTeleportRequests = new HashMap<>();
				partyPlayer.getPendingTeleportRequests().forEach(pv -> pendingTeleportRequests.put(CommonUtils.toLowerCase(pv.getRequester().getName()), pv));
				
				if (commandData.getArgs().length > 1
						&& !pendingTeleportRequests.containsKey(CommonUtils.toLowerCase(commandData.getArgs()[1]))) {
					noPendingRequests = true;
				} else {
					PartyTeleportRequest partyTeleportRequest = null;
					if (pendingTeleportRequests.size() > 0) {
						if (pendingTeleportRequests.size() == 1) {
							partyTeleportRequest = pendingTeleportRequests.values().iterator().next();
						} else if (commandData.getArgs().length > 1) {
							partyTeleportRequest = pendingTeleportRequests.get(CommonUtils.toLowerCase(commandData.getArgs()[1]));
						} else {
							// Missing player
							sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEREQUESTS);
							for (Map.Entry<String, PartyTeleportRequest> entry : pendingTeleportRequests.entrySet()) {
								sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEREQUESTS_PLAYER
										.replace("%player%", entry.getValue().getRequester().getName()));
							}
							return;
						}
					} else {
						noPendingRequests = true;
					}
					
					if (partyTeleportRequest != null) {
						// Command starts
						partyTeleportRequest.accept();
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_ACCEPT_TELEPORT,
								partyPlayer.getName(), partyTeleportRequest.getRequester().getName()), true);
						return;
					}
				}
			}
			
			if (noPendingRequests) {
				if (commandData.getArgs().length > 1) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOEXISTS);
				} else {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOREQUEST);
				}
				return;
			}
			
			// No ask and no teleport - already in party
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
		} else {
			// Accept invite request
			HashMap<String, PartyInvite> pendingInvites = new HashMap<>();
			partyPlayer.getPendingInvites().stream().filter(pv -> pv.getParty().getName() != null).forEach(pv -> pendingInvites.put(CommonUtils.toLowerCase(pv.getParty().getName()), pv));
			
			if (commandData.getArgs().length > 1
					&& !pendingInvites.containsKey(CommonUtils.toLowerCase(commandData.getArgs()[1]))) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOEXISTS);
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
					sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEREQUESTS);
					for (Map.Entry<String, PartyInvite> entry : pendingInvites.entrySet()) {
						sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEREQUESTS_PARTY
								.replace("%party%", entry.getKey()), (PartyImpl) entry.getValue().getParty());
					}
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOREQUEST);
				return;
			}
			
			if (partyInvite == null) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOEXISTS);
				return;
			}
			
			// Command starts
			partyInvite.accept();
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_ACCEPT_INVITE,
					partyPlayer.getName(), partyInvite.getParty().getName() != null ? partyInvite.getParty().getName() : "_"), true);
		}
	}
}
