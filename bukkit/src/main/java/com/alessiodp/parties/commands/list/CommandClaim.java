package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.addons.external.GriefPreventionHandler.PermissionType;
import com.alessiodp.parties.addons.external.GriefPreventionHandler.Result;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandClaim implements ICommand {
	private Parties plugin;
	 
	public CommandClaim(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.CLAIM.toString())) {
			pp.sendNoPermission(PartiesPermission.CLAIM);;
			return false;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_CLAIM))
			return false;
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		if (commandData.getArgs().length != 2) {
			pp.sendMessage(Messages.ADDCMD_CLAIM_WRONGCMD);
			return;
		}
		
		Selection selection = Selection.FAILED_GENERAL;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.ADDONS_GRIEFPREVENTION_CMD_TRUST))
			selection = Selection.TRUST;
		else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.ADDONS_GRIEFPREVENTION_CMD_CONTAINER))
			selection = Selection.CONTAINER;
		else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.ADDONS_GRIEFPREVENTION_CMD_ACCESS))
			selection = Selection.ACCESS;
		else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.ADDONS_GRIEFPREVENTION_CMD_REMOVE))
			selection = Selection.REMOVE;
		
		if (!selection.equals(Selection.FAILED_GENERAL)) {
			Result res = GriefPreventionHandler.isManager(commandData.getPlayer());
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
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.CLAIM, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		switch (selection) {
		case FAILED_NOEXIST:
			// Return: No exist claim
			pp.sendMessage(Messages.ADDCMD_CLAIM_CLAIMNOEXISTS);
			break;
		case FAILED_NOMANAGER:
			// Return: No manager
			pp.sendMessage(Messages.ADDCMD_CLAIM_NOMANAGER);
			break;
		case FAILED_GENERAL:
			// Return: Wrong command
			pp.sendMessage(Messages.ADDCMD_CLAIM_WRONGCMD);
			break;
		default:
			GriefPreventionHandler.addPartyPermission(commandData.getPlayer(), party, selection.getGPPermission());
			pp.sendMessage(Messages.ADDCMD_CLAIM_CLAIMED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_CLAIM
					.replace("{player}", pp.getName())
					.replace("{value}", commandData.getArgs()[1].toLowerCase()), true);
		}
	}
	
	
	
	private enum Selection {
		TRUST, CONTAINER, ACCESS, REMOVE, FAILED_GENERAL, FAILED_NOEXIST, FAILED_NOMANAGER;
		
		public PermissionType getGPPermission() {
			PermissionType ret;
			switch (this) {
			case TRUST:
				ret = PermissionType.BUILD;
				break;
			case CONTAINER:
				ret = PermissionType.INVENTORY;
				break;
			case ACCESS:
				ret = PermissionType.ACCESS;
				break;
			default:
				ret = PermissionType.REMOVE;
			}
			return ret;
		}
	}
}
