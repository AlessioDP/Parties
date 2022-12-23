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
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

public class CommandClose extends PartiesSubCommand {
	private final String syntaxOthers;
	private final String syntaxOthersConsole;
	
	public CommandClose(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.CLOSE,
				PartiesPermission.USER_CLOSE,
				ConfigMain.COMMANDS_SUB_CLOSE,
				true
		);
		
		syntax = baseSyntax();
		
		syntaxOthers = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		syntaxOthersConsole = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_CLOSE;
		help = Messages.HELP_ADDITIONAL_COMMANDS_CLOSE;
	}
	
	@Override
	public @NotNull String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_CLOSE_OTHERS))
			return syntaxOthers;
		return syntax;
	}
	
	@Override
	public @NotNull String getConsoleSyntax() {
		return syntaxOthersConsole;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		boolean ret = handlePreRequisitesFull(commandData, null, 1, 2);
		if (ret && ((PartiesCommandData) commandData).getPartyPlayer() != null) {
			commandData.addPermission(PartiesPermission.ADMIN_CLOSE_OTHERS);
			commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_CLOSE_BYPASS);
		}
		return ret;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party;
		
		// Command handling
		if (commandData.getArgs().length == 1 && sender.isPlayer()) {
			party = getPlugin().getPartyManager().getPartyOfPlayer(partyPlayer);
		} else if (commandData.getArgs().length == 2 && commandData.havePermission(PartiesPermission.ADMIN_CLOSE_OTHERS)) {
			party = getPlugin().getPartyManager().getParty(commandData.getArgs()[1]);
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
			return;
		}
		
		if (party == null) {
			if (commandData.getArgs().length > 1)
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", commandData.getArgs()[1]));
			else
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (!party.isOpen()) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_ALREADY_CLOSED);
			return;
		}
		
		boolean mustStartCooldown = false;
		int cooldown = ConfigParties.ADDITIONAL_JOIN_OPENCLOSE_COOLDOWN_CLOSE;
		if (cooldown > 0 && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_CLOSE_BYPASS)) {
			String customCooldown = sender.getDynamicPermission(PartiesPermission.USER_CLOSE + ".cooldown.");
			if (customCooldown != null) {
				try {
					cooldown = Integer.parseInt(customCooldown);
				} catch (Exception ignored) {}
			}
			if (cooldown > 0) {
				mustStartCooldown = true;
				long remainingCooldown = getPlugin().getCooldownManager().canAction(CooldownManager.Action.CLOSE, sender.getUUID(), cooldown);
				
				if (remainingCooldown > 0) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_COOLDOWN
							.replace("%seconds%", String.valueOf(remainingCooldown)));
					return;
				}
			}
		}
		
		if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.CLOSE, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown)
			getPlugin().getCooldownManager().startAction(
					CooldownManager.Action.CLOSE,
					sender.getUUID(),
					cooldown
			);
		
		
		// Command starts
		party.setOpen(false);
		
		sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_CLOSED);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_OPENCLOSE,
				partyPlayer.getName(), party.getName() != null ? party.getName() : "_", false), true);
	}
}
