package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandClaim extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public BukkitCommandClaim(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.CLAIM.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.CLAIM);
			return false;
		}
		
		PartyImpl party = partyPlayer.getPartyName().isEmpty() ? null : ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyName());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_CLAIM))
			return false;
		
		if (commandData.getArgs().length != 2) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_WRONGCMD);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		((PartiesCommandData) commandData).setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
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
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.CLAIM, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		switch (selection) {
		case FAILED_NOEXIST:
			// Return: No exist claim
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_CLAIMNOEXISTS);
			break;
		case FAILED_NOMANAGER:
			// Return: No manager
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_NOMANAGER);
			break;
		case FAILED_GENERAL:
			// Return: Wrong command
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_WRONGCMD);
			break;
		default:
			GriefPreventionHandler.addPartyPermission(Bukkit.getPlayer(commandData.getSender().getUUID()), party, selection.getGPPermission());
			sendMessage(sender, partyPlayer, selection.getGPPermission().isRemove() ? BukkitMessages.ADDCMD_CLAIM_REMOVED : BukkitMessages.ADDCMD_CLAIM_CLAIMED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_CLAIM
					.replace("{player}", sender.getName())
					.replace("{value}", commandData.getArgs()[1].toLowerCase()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_ACCESS);
			ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_CONTAINER);
			ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_TRUST);
			ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_REMOVE);
		}
		return ret;
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
