package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.RequestCooldown;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

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
	public boolean preRequisites(@NotNull CommandData commandData) {
		boolean ret = handlePreRequisitesFull(commandData, false, 2, 2);
		if (ret) {
			commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_ASK_BYPASS);
		}
		return ret;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String partyName = commandData.getArgs()[1];
		PartyImpl party = getPlugin().getPartyManager().getParty(partyName);
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
		if (ConfigParties.ADDITIONAL_ASK_COOLDOWN_ENABLE && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_ASK_BYPASS)) {
			// Check invite cooldown
			mustStartCooldown = true;
			RequestCooldown askCooldown = getPlugin().getCooldownManager().canMultiAction(
					CooldownManager.MultiAction.ASK,
					partyPlayer.getPlayerUUID(),
					party.getId()
			);
			
			if (askCooldown != null) {
				sendMessage(sender, partyPlayer, (askCooldown.isGlobal() ? Messages.ADDCMD_ASK_COOLDOWN_GLOBAL : Messages.ADDCMD_ASK_COOLDOWN_INDIVIDUAL)
						.replace("%seconds%", String.valueOf(askCooldown.getWaitTime())));
			}
		}
		
		if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.ASK, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		partyPlayer.askToJoin(party);
		
		if (mustStartCooldown) {
			String customCooldownGlobal = sender.getDynamicPermission(PartiesPermission.USER_ASK + ".global.cooldown.");
			String customCooldownIndividual = sender.getDynamicPermission(PartiesPermission.USER_ASK + ".individual.cooldown.");
			int cooldownGlobal = ConfigParties.ADDITIONAL_ASK_COOLDOWN_GLOBAL;
			int cooldownIndividual = ConfigParties.ADDITIONAL_ASK_COOLDOWN_INDIVIDUAL;
			if (customCooldownGlobal != null) {
				try {
					cooldownGlobal = Integer.parseInt(customCooldownGlobal);
				} catch (Exception ignored) {}
			}
			if (customCooldownIndividual != null) {
				try {
					cooldownIndividual = Integer.parseInt(customCooldownIndividual);
				} catch (Exception ignored) {}
			}
			
			if (cooldownGlobal > 0) {
				getPlugin().getCooldownManager().startMultiAction(
						CooldownManager.MultiAction.ASK,
						partyPlayer.getPlayerUUID(),
						null,
						cooldownGlobal
				);
			}
			if (cooldownIndividual > 0) {
				getPlugin().getCooldownManager().startMultiAction(
						CooldownManager.MultiAction.ASK,
						partyPlayer.getPlayerUUID(),
						party.getId(),
						cooldownIndividual
				);
			}
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_ASK,
				partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
	}
}
