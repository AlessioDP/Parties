package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.InviteCooldown;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.List;

public class CommandInvite extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandInvite(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.INVITE.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.INVITE);
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
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_WRONGCMD);
			return false;
		}
		
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
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
		if (!invitedPartyPlayer.getPartyName().isEmpty()) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYERINPARTY, invitedPartyPlayer);
			return;
		}
		
		if (ConfigParties.GENERAL_INVITE_PREVENTINVITEPERM
				&& !invitedPlayer.hasPermission(PartiesPermission.ACCEPT.toString())) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_PLAYERNOPERM, invitedPartyPlayer);
			return;
		}
		
		if (invitedPartyPlayer.getIgnoredParties().contains(party.getName())) {
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
		if (invitedPartyPlayer.getPartyInvites().containsKey(party)) {
			isRevoke = true;
			
			if (!ConfigParties.GENERAL_INVITE_REVOKE) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_INVITE_ALREADYINVITED, invitedPartyPlayer);
				return;
			}
		}
		
		if (!isRevoke && ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE) {
			// Check invite cooldown
			List<InviteCooldown> list = ((PartiesPlugin) plugin).getCooldownManager().getInviteCooldown().get(partyPlayer.getPlayerUUID());
			if (list != null) {
				for (InviteCooldown ic : list) {
					long diff = ic.getDiffTime();
					
					if (ic.getType() == InviteCooldown.CooldownType.GLOBAL
							|| ic.getInvited().equals(invitedPlayer.getUUID())) {
						if (diff < ic.getType().getCooldown()) {
							String msg = "";
							switch (ic.getType()) {
							case GLOBAL:
								msg = Messages.MAINCMD_INVITE_COOLDOWN_GLOBAL;
								break;
							case INDIVIDUAL:
								msg = Messages.MAINCMD_INVITE_COOLDOWN_INDIVIDUAL;
							}
							
							sendMessage(sender, partyPlayer, msg.replace("%seconds%", String.valueOf(ic.getType().getCooldown() - diff)));
							return;
						}
					}
				}
			}
		}
		
		// Command starts
		if (isRevoke) {
			// Revoke invite
			party.revokeInvite(invitedPlayer.getUUID());
		} else {
			// Send invite
			party.invitePlayer(partyPlayer, invitedPartyPlayer);
		}
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_INVITE
				.replace("{player}", sender.getName())
				.replace("{party}", party.getName())
				.replace("{revoke}", Boolean.toString(isRevoke)), true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
	}
}
