package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.interfaces.PartyInvite;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.RequestCooldown;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CommandInvite extends PartiesSubCommand {
	
	public CommandInvite(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.INVITE,
				PartiesPermission.USER_INVITE,
				ConfigMain.COMMANDS_SUB_INVITE,
				false
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PLAYER
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_INVITE;
		help = Messages.HELP_MAIN_COMMANDS_INVITE;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = getPlugin().getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
			return false;
		}
		
		if (commandData.getArgs().length != 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		PartyImpl party = getPlugin().getPartyManager().getPartyOfPlayer(partyPlayer);
		if (party == null) {
			if (!ConfigParties.GENERAL_INVITE_AUTO_CREATE_PARTY_UPON_INVITE) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
		} else {
			if (!getPlugin().getRankManager().checkPlayerRankAlerter(partyPlayer, RankPermission.INVITE))
				return false;
			
			if (party.isFull()) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYFULL);
				return false;
			}
			
			
			((PartiesCommandData) commandData).setParty(party);
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_INVITE_BYPASS);
		return true;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		User invitedPlayer = plugin.getPlayerByName(commandData.getArgs()[1]);
		if (invitedPlayer == null) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYEROFFLINE);
			return;
		}
		
		PartyPlayerImpl invitedPartyPlayer = getPlugin().getPlayerManager().getPlayer(invitedPlayer.getUUID());
		
		if (invitedPartyPlayer.isVanished()) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYEROFFLINE);
			return;
		}
		
		if (invitedPartyPlayer.getPlayerUUID().equals(sender.getUUID())) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_INVITE_YOURSELF);
			return;
		}
		
		if (invitedPartyPlayer.isInParty()) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYERINPARTY, invitedPartyPlayer);
			return;
		}
		
		if (ConfigParties.GENERAL_INVITE_PREVENTINVITEPERM
				&& invitedPlayer.isInsideNetwork() // Check if the user is inside the network (skip if Redis)
				&& !invitedPlayer.hasPermission(PartiesPermission.USER_ACCEPT)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYERNOPERM, invitedPartyPlayer);
			return;
		}
		
		// Check for party, create one if option enabled
		if (party == null) {
			if (ConfigParties.GENERAL_INVITE_AUTO_CREATE_PARTY_UPON_INVITE && ConfigParties.GENERAL_NAME_DYNAMIC_ENABLE) {
				String partyName = CommandCreate.generateDynamicName(getPlugin(), partyPlayer);
				party = CommandCreate.createParty((PartiesPlugin) plugin, this, sender, partyPlayer, partyName, false);
				
				if (party == null || party.isFixed()) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_FAILED, invitedPartyPlayer);
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
		}
		
		if (invitedPartyPlayer.getIgnoredParties().contains(party.getId())) {
			// Invited player ignoring the party, fake sent
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_SENT, invitedPartyPlayer);
			return;
		}
		
		if (invitedPartyPlayer.isMuted()
				&& ConfigMain.ADDITIONAL_MUTE_ENABLE
				&& ConfigMain.ADDITIONAL_MUTE_BLOCK_INVITE) {
			// Invited player has disabled notifications, fake sent
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_SENT, invitedPartyPlayer);
			return;
		}
		
		boolean isRevoke = false;
		final PartyImpl finalParty = party;
		Optional<PartyInvite> revokeInvite = invitedPartyPlayer.getPendingInvites().stream().filter(pv -> pv.getParty().getId().equals(finalParty.getId())).findAny();
		if (revokeInvite.isPresent()) {
			isRevoke = true;
			
			if (!ConfigParties.GENERAL_INVITE_REVOKE) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_ALREADYINVITED, invitedPartyPlayer);
				return;
			}
		}
		
		boolean mustStartCooldown = false;
		if (!isRevoke && ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_INVITE_BYPASS)) {
			// Check invited player cooldown
			RequestCooldown inviteAfterLeaveCooldown = getPlugin().getCooldownManager().canMultiAction(
					CooldownManager.MultiAction.INVITE_AFTER_LEAVE,
					invitedPlayer.getUUID(),
					party.getId()
			);
			if (inviteAfterLeaveCooldown != null) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_COOLDOWN_ON_LEAVE
						.replace("%seconds%", String.valueOf(inviteAfterLeaveCooldown.getWaitTime())));
				return;
			}
			
			// Check invite cooldown
			mustStartCooldown = true;
			RequestCooldown inviteCooldown = getPlugin().getCooldownManager().canMultiAction(
					CooldownManager.MultiAction.INVITE,
					partyPlayer.getPlayerUUID(),
					invitedPlayer.getUUID()
			);
			
			if (inviteCooldown != null) {
				sendMessage(sender, partyPlayer, (inviteCooldown.isGlobal() ? Messages.MAINCMD_INVITE_COOLDOWN_GLOBAL : Messages.MAINCMD_INVITE_COOLDOWN_INDIVIDUAL)
						.replace("%seconds%", String.valueOf(inviteCooldown.getCooldown() - inviteCooldown.getDiffTime())));
				return;
			}
		}
		
		// Command starts
		if (isRevoke) {
			// Revoke invite
			revokeInvite.get().revoke();
		} else {
			// Send invite
			party.invitePlayer(invitedPartyPlayer, partyPlayer);
			
			if (mustStartCooldown) {
				getPlugin().getCooldownManager().startMultiAction(
						CooldownManager.MultiAction.INVITE,
						partyPlayer.getPlayerUUID(),
						null,
						ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL
				);
				getPlugin().getCooldownManager().startMultiAction(
						CooldownManager.MultiAction.INVITE,
						partyPlayer.getPlayerUUID(),
						invitedPartyPlayer.getPlayerUUID(),
						ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL
				);
			}
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_INVITE,
				partyPlayer.getName(), invitedPartyPlayer.getName(), party.getName() != null ? party.getName() : party.getId(), isRevoke), true);
	}
	
	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
	}
}
