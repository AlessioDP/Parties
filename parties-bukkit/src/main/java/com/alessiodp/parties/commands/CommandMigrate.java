package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandMigrate implements CommandInterface {
	private Parties plugin;
	 
	public CommandMigrate(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean isPlayer = sender instanceof Player;
		ThePlayer tp = isPlayer ? plugin.getPlayerHandler().getPlayer(((Player)sender).getUniqueId()) : null;
		if (!(tp != null && Variables.database_migrate_console)) {
			if (!(tp != null && !sender.hasPermission(PartiesPermissions.ADMIN_MIGRATE.toString()))) {
				if (!plugin.getDatabaseType().isNone()) {
					if (plugin.getDataHandler().isSQLEnabled()) {
						// You can perform this command
						if (args.length > 1) {
							switch (args[1].toLowerCase()) {
							case "sql":
								if (plugin.getDataHandler().migrateYAMLtoSQL()) {
									// Success
									if (tp != null)
										tp.sendMessage(Messages.migrate_tosql);
									LogHandler.log(LogLevel.BASIC, "Database system migrated to SQL, by " + sender.getName(), true, ConsoleColors.CYAN);
								} else {
									// Fail
									if (tp != null)
										tp.sendMessage(Messages.migrate_failed);
									LogHandler.log(LogLevel.BASIC, "Database migration failed, by " + sender.getName(), true, ConsoleColors.RED);
								}
									
								
								break;
							case "file":
								if (plugin.getDataHandler().migrateSQLtoYAML()) {
									// Success
									if (tp != null)
										tp.sendMessage(Messages.migrate_tosql);
									LogHandler.log(LogLevel.BASIC, "Database system migrated to YAML, by " + sender.getName(), true, ConsoleColors.CYAN);
								} else {
									// Fail
									if (tp != null)
										tp.sendMessage(Messages.migrate_failed);
									LogHandler.log(LogLevel.BASIC, "Database migration failed, by " + sender.getName(), true, ConsoleColors.RED);
								}
								break;
							default:
								if (tp != null)
									tp.sendMessage(Messages.migrate_wrongcmd);
								else
									plugin.log(Messages.migrate_wrongcmd);
							}
						} else {
							// Argument missing
							if (tp != null)
								tp.sendMessage(Messages.migrate_wrongcmd);
							else
								plugin.log(Messages.migrate_wrongcmd);
						}
					} else {
						// SQL Database is offline
						if (tp != null)
							tp.sendMessage(Messages.migrate_offlinesql);
						LogHandler.printError(Messages.migrate_offlinesql);
					}
				} else {
					// Database is set to NONE
					if (tp != null)
						tp.sendMessage(Messages.migrate_none);
					LogHandler.printError(Messages.migrate_none);
				}
			} else {
				// Sender doesn't have migrate permission
				tp.sendMessage(Messages.nopermission
						.replace("%permission%", PartiesPermissions.ADMIN_MIGRATE.toString()));
			}
		} else {
			// Players can't use migrate command
			tp.sendMessage(Messages.invalidcommand);
		}
		return true;
	}
}