package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.storage.StorageType;

public class CommandMigrate implements ICommand {
	private Parties plugin;
	 
	public CommandMigrate(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		if (commandData.getSender() instanceof Player) {
			Player player = (Player) commandData.getSender();
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
			
			
			commandData.setPlayer((Player) commandData.getSender());
			commandData.setPartyPlayer(pp);
			commandData.addPermission(PartiesPermission.ADMIN_MIGRATE);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		if (pp == null || !ConfigMain.STORAGE_MIGRATE_ONLYCONSOLE) {
			// If party is null == is a console (PASS), or if it's a player => is onlyconsole false? (PASS)
			if (pp == null || commandData.havePermission(PartiesPermission.ADMIN_MIGRATE)) {
				StorageType databaseNow = plugin.getDatabaseManager().getDatabaseType();
				if (commandData.getArgs().length == 3) {
					// Migrate command
					StorageType databaseFrom = StorageType.getExactEnum(commandData.getArgs()[1]);
					StorageType databaseTo = StorageType.getExactEnum(commandData.getArgs()[2]);
					
					if (databaseFrom != null && databaseTo != null
							&& !databaseFrom.isNone() && !databaseTo.isNone()) {
						// Exist these databases?
						if (!databaseFrom.equals(databaseTo)) {
							// Are different these databases?
							if (!plugin.getDatabaseManager().isStorageOnline(databaseFrom)) {
								// Database from is offline
								String msg = Messages.MAINCMD_MIGRATE_FAILED_DBOFFLINE
										.replace("%database%",databaseFrom.getFormattedName());
								sendMessage(msg, pp, commandData.getSender());
							} else if (!plugin.getDatabaseManager().isStorageOnline(databaseTo)) {
								// Database to is offline
								String msg = Messages.MAINCMD_MIGRATE_FAILED_DBOFFLINE
										.replace("%database%",databaseTo.getFormattedName());
								sendMessage(msg, pp, commandData.getSender());
							} else {
								// Databases online, start migration
								if (plugin.getDatabaseManager().migrate(databaseFrom, databaseTo)) {
									// Migration done
									String msg = Messages.MAINCMD_MIGRATE_COMPLETED
											.replace("%database_to%", databaseTo.getFormattedName())
											.replace("%database_from%", databaseFrom.getFormattedName());
									sendMessage(msg, pp, commandData.getSender());
									LoggerManager.log(LogLevel.BASIC, msg, commandData.getPlayer() != null ? true : false);
								} else {
									// Migration failed
									String msg = Messages.MAINCMD_MIGRATE_FAILED_FAILED
											.replace("%database_to%", databaseTo.getFormattedName())
											.replace("%database_from%", databaseFrom.getFormattedName());
									sendMessage(msg, pp, commandData.getSender());
									LoggerManager.log(LogLevel.BASIC, msg, commandData.getPlayer() != null ? true : false);
								}
							}
						} else {
							// Same databases
							sendMessage(Messages.MAINCMD_MIGRATE_FAILED_SAMEDB, pp, commandData.getSender());
						}
					} else {
						// Wrong database (or None)
						sendMessage(Messages.MAINCMD_MIGRATE_WRONGDB, pp, commandData.getSender());
					}
				} else {
					// Shows info
					String msg = Messages.MAINCMD_MIGRATE_INFO
							.replace("%database%", databaseNow.getFormattedName());
					sendMessage(msg, pp, commandData.getSender());
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