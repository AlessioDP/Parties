package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandSetHome extends PartiesSubCommand {
	
	public BukkitCommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				BukkitCommands.SETHOME,
				PartiesPermission.USER_SETHOME,
				BukkitConfigMain.COMMANDS_CMD_SETHOME,
				false
		);
		
		if (BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
			syntax = String.format("%s [%s] <%s>",
					baseSyntax(),
					ConfigMain.COMMANDS_SUB_REMOVE,
					BukkitMessages.PARTIES_SYNTAX_HOME
			);
		} else {
			syntax = String.format("%s [%s]",
					baseSyntax(),
					ConfigMain.COMMANDS_SUB_REMOVE
			);
		}
		
		description = BukkitMessages.HELP_ADDITIONAL_DESCRIPTIONS_SETHOME;
		help = BukkitMessages.HELP_ADDITIONAL_COMMANDS_SETHOME;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
			return false;
		}
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_HOME))
			return false;
		
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
		String selectedHome = null;
		boolean isRemove = false;
		
		if (commandData.getArgs().length == 1) {
			// Default set home
			if (BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
				// Home not selected
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntax()));
				return;
			}
		} else if (commandData.getArgs().length == 2) {
			if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
				if (BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
					// Home not selected
					sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", getSyntax()));
					return;
				}
				isRemove = true;
			} else {
				selectedHome = commandData.getArgs()[1];
			}
		} else if (commandData.getArgs().length == 3
				&& BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1
				&& commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			isRemove = true;
			selectedHome = commandData.getArgs()[2];
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntax()));
			return;
		}
		
		if (!isRemove
				&& party.getHomes().size() >= BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES) {
			final String finalSelectedHome = selectedHome;
			if (finalSelectedHome == null || party.getHomes().stream().noneMatch((h) -> finalSelectedHome.equalsIgnoreCase(h.getName()))) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_MAXHOMES);
				return;
			}
		}
		
		if (!isRemove) {
			boolean mustStartCooldown = false;
			if (BukkitConfigParties.ADDITIONAL_HOME_COOLDOWN_SETHOME > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_SETHOME_BYPASS)) {
				mustStartCooldown = true;
				long remainingCooldown = ((BukkitCooldownManager) ((PartiesPlugin) plugin).getCooldownManager()).canSetHome(sender.getUUID(), BukkitConfigParties.ADDITIONAL_HOME_COOLDOWN_SETHOME);
				
				if (remainingCooldown > 0) {
					sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_COOLDOWN
							.replace("%seconds%", String.valueOf(remainingCooldown)));
					return;
				}
			}
			
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.SETHOME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
			
			if (mustStartCooldown) {
				((BukkitCooldownManager) ((PartiesPlugin) plugin).getCooldownManager()).startSetHomeCooldown(sender.getUUID(), BukkitConfigParties.ADDITIONAL_HOME_COOLDOWN_SETHOME);
			}
		}
		
		// Command starts
		if (isRemove) {
			if (selectedHome == null)
				party.getHomes().clear();
			else {
				final String finalSelectedHome = selectedHome;
				party.getHomes().removeIf(h -> h.getName() != null && h.getName().equalsIgnoreCase(finalSelectedHome));
			}
			party.updateParty();
			
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_REMOVED);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_SETHOME_REM,
					partyPlayer.getName(), party.getName(), CommonUtils.getNoEmptyOr(selectedHome, "default")), true);
		} else {
			Location location = Bukkit.getPlayer(sender.getUUID()).getLocation();
			party.getHomes().add(new PartyHomeImpl(
					selectedHome,
					location.getWorld() != null ? location.getWorld().getName() : "",
					location.getX(),
					location.getY(),
					location.getZ(),
					location.getYaw(),
					location.getPitch()
			));
			// wip add bungeecord server to home
			party.updateParty();
			
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_SETHOME_CHANGED);
			party.broadcastMessage(BukkitMessages.ADDCMD_SETHOME_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_SETHOME,
					partyPlayer.getName(), party.getName(), CommonUtils.getNoEmptyOr(selectedHome, "default")), true);
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