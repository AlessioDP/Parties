package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.HomeLocationImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandSetHome extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public BukkitCommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.SETHOME.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.SETHOME);
			return false;
		}
		
		PartyImpl party = partyPlayer.getPartyName().isEmpty() ? null : ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyName());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_HOME))
			return false;
		
		if (commandData.getArgs().length > 1 && !commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_WRONGCMD);
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
		boolean isRemove = false;
		Location home = null;
		if (commandData.getArgs().length > 1 && commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.SETHOME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
			
			home = Bukkit.getPlayer(commandData.getSender().getUUID()).getLocation();
		}
		
		// Command starts
		if (isRemove) {
			party.setHome(null);
			party.updateParty();
			
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_REMOVED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_SETHOME_REM
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else {
			party.setHome(new HomeLocationImpl(
					home.getWorld() != null ? home.getWorld().getName() : "null",
					home.getX(),
					home.getY(),
					home.getZ(),
					home.getYaw(),
					home.getPitch()
			));
			party.updateParty();
			
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_CHANGED);
			party.broadcastMessage(BukkitMessages.ADDCMD_SETHOME_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_SETHOME
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_SUB_REMOVE);
		}
		return ret;
	}
}