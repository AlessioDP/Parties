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
import com.alessiodp.parties.common.players.objects.RequestCooldown;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;

public class CommandAsk extends PartiesSubCommand {
	
	public CommandAsk(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.ASK,
				PartiesPermission.USER_ASK,
				ConfigMain.COMMANDS_SUB_ASK,
				false
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_ASK;
		help = Messages.HELP_ADDITIONAL_COMMANDS_ASK;
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
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
			return false;
		}
		
		if (commandData.getArgs().length != 2) {
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
		String partyName = commandData.getArgs()[1];
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyName);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		if (party.isFull()) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		boolean mustStartCooldown = false;
		if (ConfigParties.ADDITIONAL_ASK_COOLDOWN_ENABLE && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_ASK_BYPASS)) {
			// Check invite cooldown
			mustStartCooldown = true;
			RequestCooldown askCooldown = ((PartiesPlugin) plugin).getCooldownManager().canAsk(partyPlayer.getPlayerUUID(), party.getId());
			
			if (askCooldown != null) {
				sendMessage(sender, partyPlayer, (askCooldown.isGlobal() ? Messages.ADDCMD_ASK_COOLDOWN_GLOBAL : Messages.ADDCMD_ASK_COOLDOWN_INDIVIDUAL)
						.replace("%seconds%", String.valueOf(askCooldown.getWaitTime())));
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.ASK, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		partyPlayer.askToJoin(party);
		
		if (mustStartCooldown) {
			((PartiesPlugin) plugin).getCooldownManager().startAskCooldown(partyPlayer.getPlayerUUID(), null, ConfigParties.ADDITIONAL_ASK_COOLDOWN_GLOBAL);
			((PartiesPlugin) plugin).getCooldownManager().startAskCooldown(partyPlayer.getPlayerUUID(), party.getId(), ConfigParties.ADDITIONAL_ASK_COOLDOWN_INDIVIDUAL);
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_ASK,
				partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
	}
}
