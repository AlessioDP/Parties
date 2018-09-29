package com.alessiodp.parties.bukkit.commands.list;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import lombok.Getter;

public enum BukkitCommands implements PartiesCommand {
	CLAIM,
	CONFIRM,
	HOME,
	PROTECTION,
	SETHOME;
	
	@Getter private String command;
	@Getter private String help;
	
	BukkitCommands() {
		command = "";
		help = "";
	}
	
	public static void setup() {
		BukkitCommands.CLAIM.command = BukkitConfigMain.COMMANDS_CMD_CLAIM;
		BukkitCommands.CLAIM.help = BukkitMessages.HELP_ADDCMD_CLAIM;
		BukkitCommands.CONFIRM.command = BukkitConfigMain.COMMANDS_CMD_CONFIRM;
		BukkitCommands.HOME.command = BukkitConfigMain.COMMANDS_CMD_HOME;
		BukkitCommands.HOME.help = BukkitMessages.HELP_ADDCMD_HOME;
		BukkitCommands.PROTECTION.command = BukkitConfigMain.COMMANDS_CMD_PROTECTION;
		BukkitCommands.PROTECTION.help = BukkitMessages.HELP_ADDCMD_PROTECTION;
		BukkitCommands.SETHOME.command = BukkitConfigMain.COMMANDS_CMD_SETHOME;
		BukkitCommands.SETHOME.help = BukkitMessages.HELP_ADDCMD_SETHOME;
	}
	
	public String getType() {
		return this.name();
	}
}
