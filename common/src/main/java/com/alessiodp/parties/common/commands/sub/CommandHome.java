package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeDelayTask;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class CommandHome extends PartiesSubCommand {
	private final String syntaxOthers;
	
	public CommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.HOME,
				PartiesPermission.USER_HOME,
				ConfigMain.COMMANDS_SUB_HOME,
				false
		);
		
		if (ConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
			syntax = String.format("%s [%s]",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_HOME
			);
			syntaxOthers = String.format("%s [%s] <%s>",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_PARTY,
					Messages.PARTIES_SYNTAX_HOME
			);
		} else {
			syntax = baseSyntax();
			syntaxOthers = String.format("%s [%s]",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_PARTY
			);
		}
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_HOME;
		help = Messages.HELP_ADDITIONAL_COMMANDS_HOME;
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
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
		
		// Command handling
		PartyHomeImpl partyHome = null;
		
		if (commandData.getArgs().length == 1) {
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
			
			if (party.getHomes().size() == 0) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_NOHOME, party);
				return;
			}
		}
		
		if (ConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
			// Multiple home
			if (commandData.getArgs().length == 1) {
				if (party.getHomes().size() > 1) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_MUST_SELECT_HOME, party);
					printValidHomes(sender, partyPlayer, party);
					return;
				}
				
				Optional<PartyHome> opt = party.getHomes().stream().findFirst();
				if (opt.isPresent())
					partyHome = (PartyHomeImpl) opt.get();
				else {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_NOHOME, party);
					printValidHomes(sender, partyPlayer, party);
					return;
				}
			} else if (commandData.getArgs().length == 2) {
				
				// Not admin - Not in party
				if (!sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS) && party == null) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return;
				}
				
				// Get the partyHome of the current party
				if (party != null) {
					Optional<PartyHome> opt = party.getHomes().stream().filter((ph) -> ph.getName() != null && ph.getName().equalsIgnoreCase(commandData.getArgs()[1])).findAny();
					if (opt.isPresent())
						partyHome = (PartyHomeImpl) opt.get();
				}
				
				// If no home but home.others permission
				if (partyHome == null
						&& sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)) {
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
					
					if (party != null) {
						if (party.getHomes().size() > 1) {
							sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_MUST_SELECT_HOME, party);
							printValidHomes(sender, partyPlayer, party);
							return;
						}
						
						Optional<PartyHome> opt = party.getHomes().stream().findFirst();
						if (opt.isPresent())
							partyHome = (PartyHomeImpl) opt.get();
					}
				}
				
				// Not in party
				if (party == null) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return;
				}
				
				// No party home found
				if (partyHome == null) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_INVALID_HOME, party);
					printValidHomes(sender, partyPlayer, party);
					return;
				}
			} else if (commandData.getArgs().length == 3 && sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)) {
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
				
				if (party != null) {
					Optional<PartyHome> opt = party.getHomes().stream().filter((ph) -> ph.getName() != null && ph.getName().equalsIgnoreCase(commandData.getArgs()[1])).findAny();
					if (opt.isPresent())
						partyHome = (PartyHomeImpl) opt.get();
					else {
						sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_NOHOME, party);
						printValidHomes(sender, partyPlayer, party);
						return;
					}
				} else {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND.replace("%party%", commandData.getArgs()[1]));
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntaxForUser(sender)));
				return;
			}
		} else {
			// Single home
			if (commandData.getArgs().length == 1) {
				Optional<PartyHome> opt = party.getHomes().stream().findFirst();
				if (opt.isPresent())
					partyHome = (PartyHomeImpl) opt.get();
				else {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_NOHOME, party);
					return;
				}
			} else if (commandData.getArgs().length == 2 && sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)) {
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
				
				if (party != null) {
					Optional<PartyHome> opt = party.getHomes().stream().findFirst();
					if (opt.isPresent())
						partyHome = (PartyHomeImpl) opt.get();
					else {
						sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_NOHOME, party);
						return;
					}
				} else {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND.replace("%party%", commandData.getArgs()[1]));
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntaxForUser(sender)));
				return;
			}
		}
		
		if (!sender.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)
				&& !((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_HOME))
			return;
		
		if (partyPlayer.getPendingHomeDelay() != null) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_TELEPORTWAITING, party);
			return;
		}
		
		boolean mustStartCooldown = false;
		if (ConfigParties.ADDITIONAL_HOME_COOLDOWN_HOME > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_HOME_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = ((PartiesPlugin) plugin).getCooldownManager().canHome(sender.getUUID(), ConfigParties.ADDITIONAL_HOME_COOLDOWN_HOME);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.HOME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown)
			((PartiesPlugin) plugin).getCooldownManager().startHomeCooldown(sender.getUUID(), ConfigParties.ADDITIONAL_HOME_COOLDOWN_HOME);
		
		// Command starts
		int delay = ConfigParties.ADDITIONAL_HOME_DELAY;
		String homeDelayPermission = sender.getDynamicPermission(PartiesPermission.USER_HOME.toString() + ".");
		if (homeDelayPermission != null) {
			try {
				delay = Integer.parseInt(homeDelayPermission);
			} catch (Exception ignored) {}
		}
		
		if (delay > 0) {
			HomeDelayTask homeDelayTask = teleportPlayerWithDelay(partyPlayer, partyHome, delay);
			
			CancellableTask task = plugin.getScheduler().scheduleAsyncRepeating(homeDelayTask, 0, 300, TimeUnit.MILLISECONDS);
			partyPlayer.setPendingHomeDelay(task);
			
			sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_TELEPORTIN
					.replace("%seconds%", Integer.toString(delay)));
		} else {
			teleportPlayer(sender, partyPlayer, partyHome);
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_HOME,
				partyPlayer.getName(), party.getName() != null ? party.getName() : "_", CommonUtils.getNoEmptyOr(partyHome.getName(), "default")), true);
	}
	

	private void printValidHomes(User sender, PartyPlayerImpl partyPlayer, PartyImpl party) {
		if (party.getHomes().size() > 0) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_VALID_HOMES, party);
			
			for (PartyHome h : party.getHomes()) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_HOME_VALID_HOME_LINE
						.replace("%name%", CommonUtils.getOr(h.getName(), ""))
						.replace("%world%", h.getWorld())
						.replace("%x%", Integer.toString((int) h.getX()))
						.replace("%y%", Integer.toString((int) h.getY()))
						.replace("%z%", Integer.toString((int) h.getZ()))
						.replace("%yaw%", Integer.toString((int) h.getYaw()))
						.replace("%pitch%", Integer.toString((int) h.getPitch()))
						.replace("%server%", CommonUtils.getOr(h.getServer(), "")), party);
			}
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_HOME_NOHOME);
		}
	}
	
	// Teleport the player
	protected abstract void teleportPlayer(User player, PartyPlayerImpl partyPlayer, PartyHomeImpl home);
	
	// Get the task for home delay
	protected abstract HomeDelayTask teleportPlayerWithDelay(PartyPlayerImpl partyPlayer, PartyHomeImpl home, int delay);
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_MISC_REMOVE);
		}
		return ret;
	}
}