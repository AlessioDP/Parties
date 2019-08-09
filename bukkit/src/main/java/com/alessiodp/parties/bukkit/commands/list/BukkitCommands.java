package com.alessiodp.parties.bukkit.commands.list;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import lombok.Getter;

public enum BukkitCommands implements ADPCommand {
	CLAIM,
	CONFIRM,
	HOME,
	PROTECTION,
	SETHOME;
	
	@Getter private String command;
	@Getter private String help;
	@Getter private String permission;
	
	BukkitCommands() {
		command = "";
		help = "";
		permission = "";
	}
	
	public static void setup() {
		BukkitCommands.CLAIM.command = BukkitConfigMain.COMMANDS_CMD_CLAIM;
		BukkitCommands.CLAIM.help = BukkitMessages.HELP_ADDCMD_CLAIM;
		BukkitCommands.CLAIM.permission = PartiesPermission.CLAIM.toString();
		BukkitCommands.CONFIRM.command = BukkitConfigMain.COMMANDS_CMD_CONFIRM;
		BukkitCommands.HOME.command = BukkitConfigMain.COMMANDS_CMD_HOME;
		BukkitCommands.HOME.help = BukkitMessages.HELP_ADDCMD_HOME;
		BukkitCommands.HOME.permission = PartiesPermission.HOME.toString();
		BukkitCommands.PROTECTION.command = BukkitConfigMain.COMMANDS_CMD_PROTECTION;
		BukkitCommands.PROTECTION.help = BukkitMessages.HELP_ADDCMD_PROTECTION;
		BukkitCommands.PROTECTION.permission = PartiesPermission.PROTECTION.toString();
		BukkitCommands.SETHOME.command = BukkitConfigMain.COMMANDS_CMD_SETHOME;
		BukkitCommands.SETHOME.help = BukkitMessages.HELP_ADDCMD_SETHOME;
		BukkitCommands.SETHOME.permission = PartiesPermission.SETHOME.toString();
	}
	
	@Override
	public String getOriginalName() {
		return this.name();
	}
}
