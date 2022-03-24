package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.ExpManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandDebug extends PartiesSubCommand {
	private final String syntaxConfig;
	private final String syntaxExp;
	private final String syntaxParty;
	private final String syntaxPlayer;
	
	protected String extraBungeecord;
	
	public CommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DEBUG,
				PartiesPermission.ADMIN_DEBUG,
				ConfigMain.COMMANDS_SUB_DEBUG,
				true
		);
		initializeExtraCommands();
		
		syntax = String.format("%s <%s%s/%s/%s/%s> ...",
				baseSyntax(),
				extraBungeecord != null ? extraBungeecord + "/" : "",
				ConfigMain.COMMANDS_MISC_CONFIG,
				ConfigMain.COMMANDS_MISC_EXP,
				ConfigMain.COMMANDS_MISC_PARTY,
				ConfigMain.COMMANDS_MISC_PLAYER
		);
		syntaxConfig = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_CONFIG
		);
		syntaxExp = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_EXP
		);
		syntaxParty = String.format("%s %s <%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_PARTY,
				Messages.PARTIES_SYNTAX_PARTY
		);
		syntaxPlayer = String.format("%s %s [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_PLAYER,
				Messages.PARTIES_SYNTAX_PLAYER
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_DEBUG;
		help = Messages.HELP_ADDITIONAL_COMMANDS_DEBUG;
	}
	
	protected void initializeExtraCommands() {
		// Nothing to do
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		return handlePreRequisitesFull(commandData, null, 2, Integer.MAX_VALUE);
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling

		PartyImpl targetParty = null;
		PartyPlayerImpl targetPlayer = null;
		CommandType commandType = CommandType.parse(commandData.getArgs()[1]);
		
		if (commandType == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		if(commandType.equals(CommandType.CONFIG)) {

			ConfigCommandType configCommandType = new ConfigCommandType(plugin, mainCommand, sender, partyPlayer, syntaxConfig);
			configCommandType.onCommand(commandData);
		}
		else if(commandType.equals(CommandType.EXP)) {

			ExpCommandType expCommandType = new ExpCommandType(plugin, mainCommand, sender, partyPlayer, syntaxExp);
			expCommandType.onCommand(commandData);
		}
		else if(commandType.equals(CommandType.PLAYER)) {

			PartyCommandType partyCommandType = new PartyCommandType(plugin, mainCommand, sender, partyPlayer, syntaxParty, targetParty);
			partyCommandType.onCommand(commandData);
		}
		else if(commandType.equals(CommandType.PARTY)) {

			PlayerCommandType playerCommandType = new PlayerCommandType(plugin, mainCommand, sender, partyPlayer, syntaxPlayer, targetPlayer);
			playerCommandType.onCommand(commandData);
		}
			else{
				if (commandHandleExtra(commandType, commandData, sender, partyPlayer)) {
					// Command handle extra stopped, return
					return;
				}
		}
		
		// Command starts
		commandStart(commandType, sender, partyPlayer, targetParty, targetPlayer);
	}
	
	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission)) {
			if (args.length == 2) {
				if (extraBungeecord != null)
					ret.add(extraBungeecord);
				ret.add(ConfigMain.COMMANDS_MISC_CONFIG);
				ret.add(ConfigMain.COMMANDS_MISC_EXP);
				ret.add(ConfigMain.COMMANDS_MISC_PARTY);
				ret.add(ConfigMain.COMMANDS_MISC_PLAYER);
			} else if(args.length == 3) {
				if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PARTY)) {
					getPlugin().getPartyManager().getCacheParties().values().stream()
							.filter(p -> p.getName() != null && !p.getName().isEmpty())
							.forEach(p -> ret.add(p.getName()));
				} else if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PLAYER)) {
					return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 2);
				}
			}
		}
		return plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[args.length - 1]);
	}
	
	protected boolean commandHandleExtra(CommandType commandType, CommandData commandData, User sender, PartyPlayerImpl partyPlayer) {
		return false;
	}
	
	protected void commandStart(CommandType commandType, User sender, PartyPlayerImpl partyPlayer, PartyImpl targetParty, PartyPlayerImpl targetPlayer) {
		if(commandType.equals(CommandType.CONFIG)) {
			ConfigCommandType configCommandType = new ConfigCommandType(plugin, mainCommand, sender, partyPlayer, syntaxConfig);
			configCommandType.commandStart();
		}
		else if(commandType.equals(CommandType.EXP)) {
			ExpCommandType expCommandType = new ExpCommandType(plugin, mainCommand, sender, partyPlayer, syntaxExp);
			expCommandType.commandStart();
		}

		else if(commandType.equals(CommandType.PLAYER)) {
			PartyCommandType partyCommandType = new PartyCommandType(plugin, mainCommand, sender, partyPlayer, syntaxParty, targetParty);
			partyCommandType.commandStart();

		}
		else if(commandType.equals(CommandType.PARTY)) {
			PlayerCommandType playerCommandType = new PlayerCommandType(plugin, mainCommand, sender, partyPlayer, syntaxPlayer, targetPlayer);
			playerCommandType.commandStart();
		}
		}

	protected static class TemporaryPartyPlayer extends PartyPlayerImpl {
		@Setter private boolean persistent = true;
		
		public TemporaryPartyPlayer(@NotNull PartiesPlugin plugin, @NotNull UUID uuid) {
			super(plugin, uuid);
		}
		
		@Override
		public boolean isPersistent() {
			return persistent;
		}
		
		@Override
		public void playSound(String sound, double volume, double pitch) {
			// Nothing to do
		}
		
		@Override
		public void playChatSound() {
			// Nothing to do
		}
		
		@Override
		public void playBroadcastSound() {
			// Nothing to do
		}
		
		@Override
		public void sendPacketUpdate() {
			// Nothing to do
		}
		
		@Override
		public boolean isVanished() {
			return false;
		}
	}
	
	protected enum CommandType {
		BUNGEECORD, CONFIG, EXP, PARTY, PLAYER;
		
		public static String commandBungeecord;
		
		public static CommandType parse(String command) {
			if (command.equalsIgnoreCase(commandBungeecord))
				return BUNGEECORD;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_CONFIG))
				return CONFIG;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_EXP))
				return EXP;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PARTY))
				return PARTY;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PLAYER))
				return PLAYER;
			return null;
		}
	}
}
