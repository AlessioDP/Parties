package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.storage.StorageType;

public class CommandMigrate implements ICommand {
	private Parties plugin;
	 
	public CommandMigrate(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		boolean isPlayer = sender instanceof Player;
		PartyPlayerEntity pp = isPlayer ? plugin.getPlayerManager().getPlayer(((Player)sender).getUniqueId()) : null;
		if (pp == null || !ConfigMain.STORAGE_MIGRATE_ONLYCONSOLE) {
			// If party is null == is a console (PASS), or if it's a player => is onlyconsole false? (PASS)
			if (pp == null || sender.hasPermission(PartiesPermission.ADMIN_MIGRATE.toString())) {
				StorageType databaseNow = plugin.getDatabaseManager().getDatabaseType();
				if (args.length == 3) {
					// Migrate command
					StorageType databaseFrom = StorageType.getExactEnum(args[1]);
					StorageType databaseTo = StorageType.getExactEnum(args[2]);
					if (databaseFrom != null && databaseTo != null
							&& !databaseFrom.isNone() && !databaseTo.isNone()) {
						// Exist these databases?
						if (!databaseFrom.equals(databaseTo)) {
							// Are different these databases?
							if (!plugin.getDatabaseManager().isStorageOnline(databaseFrom)) {
								// Database from is offline
								String msg = Messages.MAINCMD_MIGRATE_FAILED_DBOFFLINE
										.replace("%database%",databaseFrom.getFormattedName());
								sendMessage(msg, pp, sender);
							} else if (!plugin.getDatabaseManager().isStorageOnline(databaseTo)) {
								// Database to is offline
								String msg = Messages.MAINCMD_MIGRATE_FAILED_DBOFFLINE
										.replace("%database%",databaseTo.getFormattedName());
								sendMessage(msg, pp, sender);
							} else {
								// Databases online, start migration
								if (plugin.getDatabaseManager().migrate(databaseFrom, databaseTo)) {
									// Migration done
									String msg = Messages.MAINCMD_MIGRATE_COMPLETED
											.replace("%database_to%", databaseTo.getFormattedName())
											.replace("%database_from%", databaseFrom.getFormattedName());
									sendMessage(msg, pp, sender);
									LoggerManager.log(LogLevel.BASIC, msg, isPlayer ? true : false);
								} else {
									// Migration failed
									String msg = Messages.MAINCMD_MIGRATE_FAILED_FAILED
											.replace("%database_to%", databaseTo.getFormattedName())
											.replace("%database_from%", databaseFrom.getFormattedName());
									sendMessage(msg, pp, sender);
									LoggerManager.log(LogLevel.BASIC, msg, isPlayer ? true : false);
								}
							}
						} else {
							// Same databases
							sendMessage(Messages.MAINCMD_MIGRATE_FAILED_SAMEDB, pp, sender);
						}
					} else {
						// Wrong database (or None)
						sendMessage(Messages.MAINCMD_MIGRATE_WRONGDB, pp, sender);
					}
				} else {
					// Shows info
					String msg = Messages.MAINCMD_MIGRATE_INFO
							.replace("%database%", databaseNow.getFormattedName());
					sendMessage(msg, pp, sender);
				}
			} else {
				// No permission
				pp.sendNoPermission(PartiesPermission.ADMIN_MIGRATE);
			}
		} else {
			// Players can't use migrate command
			pp.sendMessage(Messages.PARTIES_COMMON_INVALIDCMD);
		}
	}
	
	private void sendMessage(String msg, PartyPlayerEntity pp, CommandSender sender) {
		if (pp != null)
			pp.sendMessage(msg);
		else
			plugin.log(msg);
	}
}