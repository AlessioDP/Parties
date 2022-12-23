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
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

public class CommandOpen extends PartiesSubCommand {
	private final String syntaxOthers;
	private final String syntaxOthersConsole;
	
	public CommandOpen(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.OPEN,
				PartiesPermission.USER_OPEN,
				ConfigMain.COMMANDS_SUB_OPEN,
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
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_OPEN;
		help = Messages.HELP_ADDITIONAL_COMMANDS_OPEN;
	}
	
	@Override
	public @NotNull String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_OPEN_OTHERS))
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
			commandData.addPermission(PartiesPermission.ADMIN_OPEN_OTHERS);
			commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_OPEN_BYPASS);
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
		} else if (commandData.getArgs().length == 2 && commandData.havePermission(PartiesPermission.ADMIN_OPEN_OTHERS)) {
			party = getPlugin().getPartyManager().getParty(commandData.getArgs()[1]);
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
			return;
		}
		
		if (party == null) {
			if (commandData.getArgs().length > 1) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", commandData.getArgs()[1]));
				return;
			} else {
				if (ConfigParties.ADDITIONAL_JOIN_OPENCLOSE_AUTO_CREATE && ConfigParties.GENERAL_NAME_DYNAMIC_ENABLE) {
					String partyName = CommandCreate.generateDynamicName(getPlugin(), partyPlayer);
					party = CommandCreate.createParty((PartiesPlugin) plugin, this, sender, partyPlayer, partyName, false);
					
					if (party == null || party.isFixed()) {
						sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_FAILED);
						return;
					}
				} else {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return;
				}
			}
		}
		
		if (party.isOpen()) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_ALREADY_OPEN);
			return;
		}
		
		boolean mustStartCooldown = false;
		int cooldown = ConfigParties.ADDITIONAL_JOIN_OPENCLOSE_COOLDOWN_OPEN;
		if (cooldown > 0 && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_OPEN_BYPASS)) {
			String customCooldown = sender.getDynamicPermission(PartiesPermission.USER_OPEN + ".cooldown.");
			if (customCooldown != null) {
				try {
					cooldown = Integer.parseInt(customCooldown);
				} catch (Exception ignored) {}
			}
			if (cooldown > 0) {
				mustStartCooldown = true;
				long remainingCooldown = getPlugin().getCooldownManager().canAction(CooldownManager.Action.OPEN, sender.getUUID(), cooldown);
				
				if (remainingCooldown > 0) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_COOLDOWN
							.replace("%seconds%", String.valueOf(remainingCooldown)));
					return;
				}
			}
		}
		
		if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.OPEN, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown)
			getPlugin().getCooldownManager().startAction(CooldownManager.Action.OPEN, sender.getUUID(), cooldown);
		
		
		// Command starts
		party.setOpen(true);
		
		sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_OPENED);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_OPENCLOSE,
				partyPlayer.getName(), party.getName() != null ? party.getName() : "_", true), true);
	}
}
