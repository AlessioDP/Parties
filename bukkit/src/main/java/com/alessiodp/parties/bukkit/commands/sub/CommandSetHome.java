package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.HomeLocationImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CommandSetHome extends AbstractCommand {
	
	public CommandSetHome(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.SETHOME.toString())) {
			pp.sendNoPermission(PartiesPermission.SETHOME);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_HOME))
			return false;
		
		if (commandData.getArgs().length > 1 && !commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			pp.sendMessage(BukkitMessages.ADDCMD_SETHOME_WRONGCMD);
			return false;
		}
		
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
		boolean isRemove = false;
		Location home = null;
		if (commandData.getArgs().length > 1 && commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.SETHOME, pp, commandData.getCommandLabel(), commandData.getArgs()))
				return;
			
			home = Bukkit.getPlayer(commandData.getSender().getUUID()).getLocation();
		}
		
		/*
		 * Command starts
		 */
		if (isRemove) {
			party.setHome(null);
			party.updateParty();
			
			pp.sendMessage(BukkitMessages.ADDCMD_SETHOME_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SETHOME_REM
					.replace("{player}", pp.getName())
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
			
			pp.sendMessage(BukkitMessages.ADDCMD_SETHOME_CHANGED);
			party.sendBroadcast(pp, BukkitMessages.ADDCMD_SETHOME_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SETHOME
					.replace("{player}", pp.getName())
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