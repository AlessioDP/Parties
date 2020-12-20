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
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.RequestCooldown;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.List;
import java.util.Optional;

public class CommandInvite extends PartiesSubCommand {
	
	public CommandInvite(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.INVITE,
				PartiesPermission.USER_INVITE,
				ConfigMain.COMMANDS_CMD_INVITE,
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
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
			return false;
		}
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_INVITE))
			return false;
		
		if (commandData.getArgs().length != 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		if (party.isFull()) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYFULL);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		((PartiesCommandData) commandData).setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		User invitedPlayer = plugin.getPlayerByName(commandData.getArgs()[1]);
		if (invitedPlayer == null) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYEROFFLINE);
			return;
		}
		
		PartyPlayerImpl invitedPartyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(invitedPlayer.getUUID());
		
		if (invitedPartyPlayer.isVanished()) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYEROFFLINE);
			return;
		}
		
		if (invitedPartyPlayer.isInParty()) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYERINPARTY, invitedPartyPlayer);
			return;
		}
		
		if (ConfigParties.GENERAL_INVITE_PREVENTINVITEPERM
				&& !invitedPlayer.hasPermission(PartiesPermission.USER_ACCEPT)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYERNOPERM, invitedPartyPlayer);
			return;
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
		Optional<PartyInvite> revokeInvite = invitedPartyPlayer.getPendingInvites().stream().filter(pv -> pv.getParty().equals(party)).findAny();
		if (revokeInvite.isPresent()) {
			isRevoke = true;
			
			if (!ConfigParties.GENERAL_INVITE_REVOKE) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_ALREADYINVITED, invitedPartyPlayer);
				return;
			}
		}
		
		boolean mustStartCooldown = false;
		if (!isRevoke && ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_INVITE_BYPASS)) {
			// Check invite cooldown
			mustStartCooldown = true;
			RequestCooldown inviteCooldown = ((PartiesPlugin) plugin).getCooldownManager().canInvite(partyPlayer.getPlayerUUID(), invitedPlayer.getUUID());
			
			if (inviteCooldown != null) {
				sendMessage(sender, partyPlayer, (inviteCooldown.isGlobal() ? Messages.MAINCMD_INVITE_COOLDOWN_GLOBAL : Messages.MAINCMD_INVITE_COOLDOWN_INDIVIDUAL)
						.replace("%seconds%", String.valueOf(inviteCooldown.getCooldown() - inviteCooldown.getDiffTime())));
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
				((PartiesPlugin) plugin).getCooldownManager().startInviteCooldown(partyPlayer.getPlayerUUID(), invitedPartyPlayer.getPlayerUUID(), ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL);
				((PartiesPlugin) plugin).getCooldownManager().startInviteCooldown(partyPlayer.getPlayerUUID(), invitedPartyPlayer.getPlayerUUID(), ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL);
			}
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_INVITE,
				partyPlayer.getName(), invitedPartyPlayer.getName(), party.getName(), isRevoke), true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
	}
}
