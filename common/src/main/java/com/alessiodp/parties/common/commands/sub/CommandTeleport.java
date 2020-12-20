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
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class CommandTeleport extends PartiesSubCommand {
	
	public CommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.TELEPORT,
				PartiesPermission.USER_TELEPORT,
				ConfigMain.COMMANDS_CMD_TELEPORT,
				false
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_TELEPORT;
		help = Messages.HELP_ADDITIONAL_COMMANDS_TELEPORT;
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
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ADMIN_TELEPORT))
			return false;
		
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
		boolean mustStartCooldown = false;
		if (ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_TELEPORT_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = ((PartiesPlugin) plugin).getCooldownManager().canTeleport(partyPlayer.getPlayerUUID(), ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown) {
			((PartiesPlugin) plugin).getCooldownManager().startTeleportCooldown(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
		}
		
		// Command starts
		performTeleport(party, partyPlayer);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_TELEPORT,
				partyPlayer.getName(), party.getName()), true);
	}
	
	protected abstract void performTeleport(PartyImpl party, PartyPlayerImpl player);
}
