package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.scheduling.ADPScheduler;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandSetHome extends PartiesSubCommand {
	
	public CommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.SETHOME,
				PartiesPermission.USER_SETHOME,
				ConfigMain.COMMANDS_SUB_SETHOME,
				false
		);
		
		if (ConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
			syntax = String.format("%s [%s] <%s>",
					baseSyntax(),
					ConfigMain.COMMANDS_MISC_REMOVE,
					Messages.PARTIES_SYNTAX_HOME
			);
		} else {
			syntax = String.format("%s [%s]",
					baseSyntax(),
					ConfigMain.COMMANDS_MISC_REMOVE
			);
		}
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_SETHOME;
		help = Messages.HELP_ADDITIONAL_COMMANDS_SETHOME;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		boolean ret = handlePreRequisitesFullWithParty(commandData, true, Integer.MIN_VALUE, Integer.MAX_VALUE, RankPermission.EDIT_HOME);
		if (ret) {
			commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_SETHOME_BYPASS);
		}
		return ret;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		String selectedHome;
		boolean isRemove = commandData.getArgs().length > 1 && commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE);
		
		if (ConfigParties.ADDITIONAL_HOME_MAX_HOMES > 1) {
			if (commandData.getArgs().length == 1) {
				// Home not selected
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntax()));
				return;
			} else if (commandData.getArgs().length == 2) {
				if (isRemove) {
					// Home not selected
					sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", getSyntax()));
					return;
				} else {
					selectedHome = commandData.getArgs()[1];
				}
			} else if (commandData.getArgs().length == 3 && isRemove) {
				selectedHome = commandData.getArgs()[2];
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntax()));
				return;
			}
		} else {
			if (commandData.getArgs().length == 1 || (commandData.getArgs().length == 2 && isRemove)) {
				selectedHome = "default";
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntax()));
				return;
			}
		}
		
		if (!isRemove
				&& party.getHomes().size() >= ConfigParties.ADDITIONAL_HOME_MAX_HOMES) {
			final String finalSelectedHome = selectedHome;
			if (finalSelectedHome == null || party.getHomes().stream().noneMatch((h) -> finalSelectedHome.equalsIgnoreCase(h.getName()))) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_SETHOME_MAXHOMES);
				return;
			}
		}
		
		if (!isRemove) {
			boolean mustStartCooldown = false;
			int cooldown = ConfigParties.ADDITIONAL_HOME_COOLDOWN_SETHOME;
			if (cooldown > 0 && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_SETHOME_BYPASS)) {
				String customCooldown = sender.getDynamicPermission(PartiesPermission.USER_SETHOME + ".cooldown.");
				if (customCooldown != null) {
					try {
						cooldown = Integer.parseInt(customCooldown);
					} catch (Exception ignored) {}
				}
				
				if (cooldown > 0) {
					mustStartCooldown = true;
					long remainingCooldown = getPlugin().getCooldownManager().canAction(CooldownManager.Action.SETHOME, sender.getUUID(), cooldown);
					
					if (remainingCooldown > 0) {
						sendMessage(sender, partyPlayer, Messages.ADDCMD_SETHOME_COOLDOWN
								.replace("%seconds%", String.valueOf(remainingCooldown)));
						return;
					}
				}
			}
			
			if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.SETHOME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
			
			if (mustStartCooldown) {
				getPlugin().getCooldownManager().startAction(CooldownManager.Action.SETHOME, sender.getUUID(), cooldown);
			}
		}
		
		// Command starts
		if (isRemove) {
			boolean removed;
			if (selectedHome == null || ConfigParties.ADDITIONAL_HOME_MAX_HOMES <= 1) {
				party.getHomes().clear();
				removed = true;
			} else {
				final String finalSelectedHome = selectedHome;
				removed = party.getHomes().removeIf(h -> h.getName() != null && h.getName().equalsIgnoreCase(finalSelectedHome));
			}
			
			if (removed) {
				party.updateParty();
				
				sendMessage(sender, partyPlayer, Messages.ADDCMD_SETHOME_REMOVED);
			} else {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_SETHOME_REMOVED_NONE);
			}
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_SETHOME_REM,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_", CommonUtils.getNoEmptyOr(selectedHome, "default")), true);
		} else {
			getLocationAndSave(partyPlayer, party, selectedHome);
			
			sendMessage(sender, partyPlayer, Messages.ADDCMD_SETHOME_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_SETHOME_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_SETHOME,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_", CommonUtils.getNoEmptyOr(selectedHome, "default")), true);
		}
	}
	
	protected abstract void getLocationAndSave(@NotNull PartyPlayerImpl sender, @NotNull PartyImpl party, @NotNull String name);
	
	public static void savePartyHome(PartyImpl party, PartyHomeImpl home) {
		if (home != null) {
			if (ConfigParties.ADDITIONAL_HOME_MAX_HOMES <= 1)
				party.getHomes().clear();
			else {
				party.getHomes().removeIf(h -> h.getName() != null && h.getName().equalsIgnoreCase(home.getName()));
			}
			party.getHomes().add(home);
			party.updateParty().thenRun(party::sendPacketUpdate).exceptionally(ADPScheduler.exceptionally());;
		}
	}
	
	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2 && ConfigMain.COMMANDS_MISC_REMOVE.startsWith(args[1])) {
			ret.add(ConfigMain.COMMANDS_MISC_REMOVE);
		}
		return ret;
	}
}