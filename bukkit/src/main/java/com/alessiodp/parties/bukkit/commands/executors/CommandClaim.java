package com.alessiodp.parties.bukkit.commands.executors;

import com.alessiodp.parties.bukkit.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import org.bukkit.Bukkit;

public class CommandClaim extends AbstractCommand {
	
	public CommandClaim(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.CLAIM.toString())) {
			pp.sendNoPermission(PartiesPermission.CLAIM);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_CLAIM))
			return false;
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		if (commandData.getArgs().length != 2) {
			pp.sendMessage(BukkitMessages.ADDCMD_CLAIM_WRONGCMD);
			return;
		}
		
		Selection selection = Selection.FAILED_GENERAL;
		if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_TRUST))
			selection = Selection.TRUST;
		else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_CONTAINER))
			selection = Selection.CONTAINER;
		else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_ACCESS))
			selection = Selection.ACCESS;
		else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_REMOVE))
			selection = Selection.REMOVE;
		
		if (!selection.equals(Selection.FAILED_GENERAL)) {
			GriefPreventionHandler.Result res = GriefPreventionHandler.isManager(Bukkit.getPlayer(commandData.getSender().getUUID()));
			switch (res) {
			case NOEXIST:
				selection = Selection.FAILED_NOEXIST;
				break;
			case NOMANAGER:
				selection = Selection.FAILED_NOMANAGER;
				break;
			default:
				// Success, selection it's okay
				break;
			}
		}
		
		if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.CLAIM, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		switch (selection) {
		case FAILED_NOEXIST:
			// Return: No exist claim
			pp.sendMessage(BukkitMessages.ADDCMD_CLAIM_CLAIMNOEXISTS);
			break;
		case FAILED_NOMANAGER:
			// Return: No manager
			pp.sendMessage(BukkitMessages.ADDCMD_CLAIM_NOMANAGER);
			break;
		case FAILED_GENERAL:
			// Return: Wrong command
			pp.sendMessage(BukkitMessages.ADDCMD_CLAIM_WRONGCMD);
			break;
		default:
			GriefPreventionHandler.addPartyPermission(Bukkit.getPlayer(commandData.getSender().getUUID()), party, selection.getGPPermission());
			pp.sendMessage(selection.getGPPermission().isRemove() ? BukkitMessages.ADDCMD_CLAIM_REMOVED : BukkitMessages.ADDCMD_CLAIM_CLAIMED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_CLAIM
					.replace("{player}", pp.getName())
					.replace("{value}", commandData.getArgs()[1].toLowerCase()), true);
		}
	}
	
	
	
	private enum Selection {
		TRUST, CONTAINER, ACCESS, REMOVE, FAILED_GENERAL, FAILED_NOEXIST, FAILED_NOMANAGER;
		
		GriefPreventionHandler.PermissionType getGPPermission() {
			GriefPreventionHandler.PermissionType ret;
			switch (this) {
			case TRUST:
				ret = GriefPreventionHandler.PermissionType.BUILD;
				break;
			case CONTAINER:
				ret = GriefPreventionHandler.PermissionType.INVENTORY;
				break;
			case ACCESS:
				ret = GriefPreventionHandler.PermissionType.ACCESS;
				break;
			default:
				ret = GriefPreventionHandler.PermissionType.REMOVE;
			}
			return ret;
		}
	}
}
