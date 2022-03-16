package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.tasks.TeleportDelayTask;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public abstract class CommandTeleport extends PartiesSubCommand {
	
	public CommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.TELEPORT,
				PartiesPermission.USER_TELEPORT,
				ConfigMain.COMMANDS_SUB_TELEPORT,
				false
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_TELEPORT;
		help = Messages.HELP_ADDITIONAL_COMMANDS_TELEPORT;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		boolean ret = handlePreRequisitesFullWithParty(commandData, true, 1, 1, RankPermission.ADMIN_TELEPORT);
		if (ret) {
			commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_TELEPORT_BYPASS);
		}
		return ret;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		boolean mustStartCooldown = false;
		if (ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN > 0 && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_TELEPORT_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = getPlugin().getCooldownManager().canAction(CooldownManager.Action.TELEPORT, partyPlayer.getPlayerUUID(), ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown) {
			getPlugin().getCooldownManager().startAction(CooldownManager.Action.TELEPORT, partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
		}
		
		// Command starts
		int delay = ConfigParties.ADDITIONAL_TELEPORT_DELAY;
		String teleportDelayPermission = sender.getDynamicPermission(PartiesPermission.USER_TELEPORT + ".");
		if (teleportDelayPermission != null) {
			try {
				delay = Integer.parseInt(teleportDelayPermission);
			} catch (Exception ignored) {}
		}
		
		performTeleport(party, partyPlayer, delay);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_TELEPORT,
				partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
	}
	
	// Perform the teleport command and handle cooldown
	protected abstract void performTeleport(PartyImpl party, PartyPlayerImpl player, int delay);
	
	// The player accepted the teleport, handling delay
	protected void handleSinglePlayerTeleport(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, int delay) {
		User user = plugin.getPlayer(player.getPlayerUUID());
		if (user != null && player.getPendingTeleportDelay() == null) {
			if (delay > 0) {
				TeleportDelayTask teleportDelayTask = teleportSinglePlayerWithDelay(plugin, player, targetPlayer, delay);
				
				CancellableTask task = plugin.getScheduler().scheduleAsyncRepeating(teleportDelayTask, 0, 300, TimeUnit.MILLISECONDS);
				player.setPendingTeleportDelay(task);
				
				player.sendMessage(Messages.ADDCMD_TELEPORT_PLAYER_TELEPORTIN
						.replace("%seconds%", Integer.toString(delay)));
			} else {
				teleportSinglePlayer(plugin, player, targetPlayer);
			}
		}
	}
	
	// Teleport the player
	public abstract void teleportSinglePlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer);
	
	// Get the task for teleport delay
	public abstract TeleportDelayTask teleportSinglePlayerWithDelay(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, int delay);
	
	
}
