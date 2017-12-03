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
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.parties.utils.enums.StorageType;

public class CommandMigrate implements CommandInterface {
	private Parties plugin;
	 
	public CommandMigrate(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean isPlayer = sender instanceof Player;
		ThePlayer tp = isPlayer ? plugin.getPlayerHandler().getPlayer(((Player)sender).getUniqueId()) : null;
		if (tp == null || !Variables.storage_migrate_onlyconsole) {
			// If party is null == is a console (PASS), or if it's a player => is onlyconsole false? (PASS)
			if (tp == null || sender.hasPermission(PartiesPermissions.ADMIN_MIGRATE.toString())) {
				StorageType databaseNow = plugin.getDatabaseType();
				if (args.length == 3) {
					// Migrate command
					StorageType databaseFrom = StorageType.getExactEnum(args[1]);
					StorageType databaseTo = StorageType.getExactEnum(args[2]);
					if (databaseFrom != null && databaseTo != null
							&& !databaseFrom.isNone() && !databaseTo.isNone()) {
						// Exist these databases?
						if (!databaseFrom.equals(databaseTo)) {
							// Are different these databases?
							if (!plugin.getDatabaseDispatcher().isStorageOnline(databaseFrom)) {
								// Database from is offline
								String msg = Messages.migrate_failed_offline
										.replace("%database%",databaseFrom.getFormattedName());
								sendMessage(msg, tp, sender);
							} else if (!plugin.getDatabaseDispatcher().isStorageOnline(databaseTo)) {
								// Database to is offline
								String msg = Messages.migrate_failed_offline
										.replace("%database%",databaseTo.getFormattedName());
								sendMessage(msg, tp, sender);
							} else {
								// Databases online, start migration
								if (plugin.getDatabaseDispatcher().migrateStart(databaseFrom, databaseTo)) {
									// Migration done
									String msg = Messages.migrate_complete
											.replace("%database_to%", databaseTo.getFormattedName())
											.replace("%database_from%", databaseFrom.getFormattedName());
									sendMessage(msg, tp, sender);
									LogHandler.log(LogLevel.BASIC, msg, isPlayer ? true : false);
								} else {
									// Migration failed
									String msg = Messages.migrate_failed_migration
											.replace("%database_to%", databaseTo.getFormattedName())
											.replace("%database_from%", databaseFrom.getFormattedName());
									sendMessage(msg, tp, sender);
									LogHandler.log(LogLevel.BASIC, msg, isPlayer ? true : false);
								}
							}
						} else {
							// Same databases
							sendMessage(Messages.migrate_failed_same, tp, sender);
						}
					} else {
						// Wrong database (or None)
						sendMessage(Messages.migrate_wrongdatabase, tp, sender);
					}
				} else {
					// Shows info
					String msg = Messages.migrate_info
							.replace("%database%", databaseNow.getFormattedName());
					sendMessage(msg, tp, sender);
				}
			} else {
				// No permission
				tp.sendMessage(Messages.nopermission
						.replace("%permission%", PartiesPermissions.ADMIN_MIGRATE.toString()));
			}
		} else {
			// Players can't use migrate command
			tp.sendMessage(Messages.invalidcommand);
		}
		return true;
	}
	
	private void sendMessage(String msg, ThePlayer tp, CommandSender sender) {
		if (tp != null)
			tp.sendMessage(msg);
		else
			plugin.log(msg);
	}
}