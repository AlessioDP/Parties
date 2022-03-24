package com.alessiodp.parties.common.commands.utils;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPPermission;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.NotNull;

public abstract class PartiesSubCommand extends ADPSubCommand {
	
	public PartiesSubCommand(@NotNull ADPPlugin plugin, @NotNull ADPMainCommand mainCommand, @NotNull ADPCommand command, ADPPermission permission, @NotNull String commandName, boolean executableByConsole) {
		super(plugin, mainCommand, command, permission, commandName, executableByConsole);
	}
	
	public PartiesPlugin getPlugin() {
		return (PartiesPlugin) plugin;
	}
	
	public void sendNoPermissionMessage(PartyPlayerImpl partyPlayer, ADPPermission permission) {
		if (partyPlayer != null)
			partyPlayer.sendMessage(Messages.PARTIES_PERM_NOPERM
					.replace("%permission%", permission.toString()));
	}
	
	public void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message);
		else
			getPlugin().getMessageUtils().sendMessage(receiver, message, playerReceiver, playerReceiver != null ? getPlugin().getPartyManager().getPartyOfPlayer(playerReceiver) : null);
	}
	
	public void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message, PartyPlayerImpl victim) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message, victim);
		else
			getPlugin().getMessageUtils().sendMessage(receiver, message, victim, victim != null ? getPlugin().getPartyManager().getPartyOfPlayer(victim) : null);
	}
	
	public void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message, PartyImpl party) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message, party);
		else
			getPlugin().getMessageUtils().sendMessage(receiver, message, playerReceiver, party);
	}
	
	public void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message, PartyPlayerImpl victim, PartyImpl party) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message, victim, party);
		else
			getPlugin().getMessageUtils().sendMessage(receiver, message, victim, party);
	}
	
	protected boolean handlePreRequisitesFull(CommandData commandData, Boolean inParty) {
		return handlePreRequisitesFull(commandData, inParty, 0, Integer.MAX_VALUE);
	}
	
	protected boolean handlePreRequisitesFull(CommandData commandData, Boolean inParty, int argMin, int argMax) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = getPlugin().getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			if (inParty != null) {
				if (inParty && partyPlayer.getPartyId() == null) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				} else if (!inParty && partyPlayer.getPartyId() != null) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
					return false;
				}
			}
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		}
		
		if (commandData.getArgs().length < argMin || commandData.getArgs().length > argMax) {
			sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), getSyntaxForUser(sender));
			return false;
		}
		return true;
	}
	
	protected boolean handlePreRequisitesFullWithParty(CommandData commandData, Boolean inParty, int argMin, int argMax, RankPermission requiredRank) {
		boolean ret = handlePreRequisitesFull(commandData, inParty, argMin, argMax);
		
		if (ret && commandData.getSender().isPlayer()) {
			PartyImpl party = getPlugin().getPartyManager().getPartyOfPlayer(((PartiesCommandData) commandData).getPartyPlayer());
			if (party == null) {
				sendMessage(commandData.getSender(), ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (requiredRank != null && !getPlugin().getRankManager().checkPlayerRankAlerter(((PartiesCommandData) commandData).getPartyPlayer(), requiredRank))
				return false;
			
			((PartiesCommandData) commandData).setParty(party);
		}
		return ret;
	}
}
