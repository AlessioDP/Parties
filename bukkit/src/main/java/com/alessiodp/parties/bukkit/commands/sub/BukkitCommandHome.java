package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.tasks.HomeTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BukkitCommandHome extends PartiesSubCommand {
	private final String syntaxOthers;
	
	public BukkitCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				BukkitCommands.HOME,
				PartiesPermission.USER_HOME,
				BukkitConfigMain.COMMANDS_CMD_HOME,
				false
		);
		
		if (BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
			syntax = String.format("%s <%s>",
					baseSyntax(),
					BukkitMessages.PARTIES_SYNTAX_HOME
			);
			syntaxOthers = String.format("%s [%s] <%s>",
					baseSyntax(),
					BukkitMessages.PARTIES_SYNTAX_PARTY,
					BukkitMessages.PARTIES_SYNTAX_HOME
			);
		} else {
			syntax = baseSyntax();
			syntaxOthers = String.format("%s [%s]",
					baseSyntax(),
					BukkitMessages.PARTIES_SYNTAX_PARTY
			);
		}
		
		description = BukkitMessages.HELP_ADDITIONAL_DESCRIPTIONS_HOME;
		help = BukkitMessages.HELP_ADDITIONAL_COMMANDS_HOME;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS))
			return syntaxOthers;
		return syntax;
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
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		BukkitPartyPlayerImpl partyPlayer = (BukkitPartyPlayerImpl) ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
		PartyHomeImpl partyHome = null;
		
		if (commandData.getArgs().length == 1) {
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
			
			if (party.getHomes().size() == 0) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_NOHOME, party);
				return;
			}
			
			if (BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1 && party.getHomes().size() > 1) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_MUST_SELECT_HOME, party);
				printValidHomes(sender, partyPlayer, party);
				return;
			}
			
			Optional<PartyHome> opt = party.getHomes().stream().filter((ph) -> ph.getName() != null && ph.getName().equalsIgnoreCase(commandData.getArgs()[1])).findAny();
			if (opt.isPresent())
				partyHome = (PartyHomeImpl) opt.get();
			else {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_INVALID_HOME, party);
				printValidHomes(sender, partyPlayer, party);
				return;
			}
		} else if (commandData.getArgs().length == 2) {
			if (party == null && !sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)) {
				sendMessage(sender, partyPlayer, BukkitMessages.PARTIES_COMMON_NOTINPARTY, party);
				return;
			}
			
			if (party != null) {
				Optional<PartyHome> opt = party.getHomes().stream().filter((ph) -> ph.getName() != null && ph.getName().equalsIgnoreCase(commandData.getArgs()[1])).findAny();
				if (opt.isPresent())
					partyHome = (PartyHomeImpl) opt.get();
			}
			
			if (partyHome == null && sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS) && ((PartiesPlugin) plugin).getPartyManager().existsParty(commandData.getArgs()[1])) {
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
				
				if (BukkitConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1 && party.getHomes().size() > 1) {
					sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_MUST_SELECT_HOME, party);
					printValidHomes(sender, partyPlayer, party);
					return;
				}
				
				Optional<PartyHome> opt = party.getHomes().stream().filter((ph) -> ph.getName() != null && ph.getName().equalsIgnoreCase(commandData.getArgs()[1])).findAny();
				if (opt.isPresent())
					partyHome = (PartyHomeImpl) opt.get();
			}
			
			if (partyHome == null) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_INVALID_HOME, party);
				printValidHomes(sender, partyPlayer, party);
				return;
			}
		} else if (commandData.getArgs().length == 3 && sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)) {
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
			
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND, party);
				return;
			}
			
			Optional<PartyHome> opt = party.getHomes().stream().filter((ph) -> ph.getName() != null && ph.getName().equalsIgnoreCase(commandData.getArgs()[2])).findAny();
			if (opt.isPresent())
				partyHome = (PartyHomeImpl) opt.get();
			else {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_INVALID_HOME, party);
				printValidHomes(sender, partyPlayer, party);
				return;
			}
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntax()));
			return;
		}
		
		
		if (!sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)
				&& !((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_HOME))
			return;
		
		if (partyPlayer.getHomeTeleporting() != null) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_TELEPORTWAITING, party);
			return;
		}
		
		boolean mustStartCooldown = false;
		if (BukkitConfigParties.ADDITIONAL_HOME_COOLDOWN_HOME > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_HOME_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = ((BukkitCooldownManager) ((PartiesPlugin) plugin).getCooldownManager()).canHome(sender.getUUID(), BukkitConfigParties.ADDITIONAL_HOME_COOLDOWN_HOME);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.HOME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown)
			((BukkitCooldownManager) ((PartiesPlugin) plugin).getCooldownManager()).startHomeCooldown(sender.getUUID(), BukkitConfigParties.ADDITIONAL_HOME_COOLDOWN_HOME);
		
		// Command starts
		Player bukkitPlayer = Bukkit.getPlayer(commandData.getSender().getUUID());
		int delay = BukkitConfigParties.ADDITIONAL_HOME_DELAY;
		String homeDelayPermission = sender.getDynamicPermission(PartiesPermission.USER_HOME.toString() + ".");
		if (homeDelayPermission != null) {
			try {
				delay = Integer.parseInt(homeDelayPermission);
			} catch (Exception ignored) {}
		}
		
		Location loc = new Location(
				Bukkit.getWorld(partyHome.getWorld()),
				partyHome.getX(),
				partyHome.getY(),
				partyHome.getZ(),
				partyHome.getYaw(),
				partyHome.getPitch()
		);
		
		if (delay > 0) {
			HomeTask homeTask = new HomeTask(
					(PartiesPlugin) plugin,
					partyPlayer,
					bukkitPlayer,
					delay,
					loc
			);
			CancellableTask task = plugin.getScheduler().scheduleAsyncRepeating(homeTask, 0, 300, TimeUnit.MILLISECONDS);
			partyPlayer.setHomeTeleporting(task);
			
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_TELEPORTIN
					.replace("%time%", Integer.toString(delay)));
		} else {
			((BukkitUser) sender).teleportAsync(loc).thenAccept(result -> {
				if (result) {
					EssentialsHandler.updateLastTeleportLocation(sender.getUUID());
					sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_TELEPORTED);
				} else {
					plugin.getLoggerManager().printError(PartiesConstants.DEBUG_TELEPORT_ASYNC);
				}
			});
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_HOME,
				partyPlayer.getName(), party.getName(), CommonUtils.getNoEmptyOr(partyHome.getName(), "default")), true);
	}
	
	private void printValidHomes(User sender, PartyPlayerImpl partyPlayer, PartyImpl party) {
		sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_VALID_HOMES, party);
		for (PartyHome h : party.getHomes()) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_HOME_HOME_VALID_HOME_LINE
					.replace("%name%", CommonUtils.getOr(h.getName(), ""))
					.replace("%world%", h.getWorld())
					.replace("%x%", Integer.toString((int) h.getX()))
					.replace("%y%", Integer.toString((int) h.getY()))
					.replace("%z%", Integer.toString((int) h.getZ()))
					.replace("%yaw%", Integer.toString((int) h.getYaw()))
					.replace("%pitch%", Integer.toString((int) h.getPitch()))
					.replace("%server%", CommonUtils.getOr(h.getServer(), "")), party);
		}
	}
}