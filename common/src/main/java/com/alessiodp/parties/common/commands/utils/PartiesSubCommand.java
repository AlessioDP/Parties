package com.alessiodp.parties.common.commands.utils;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.ADPSubCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

public abstract class PartiesSubCommand extends ADPSubCommand {
	public PartiesSubCommand(@NonNull ADPPlugin plugin, @NonNull ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	protected void sendNoPermissionMessage(PartyPlayerImpl partyPlayer, PartiesPermission permission) {
		if (partyPlayer != null)
			partyPlayer.sendMessage(Messages.PARTIES_PERM_NOPERM
					.replace("%permission%", permission.toString()));
	}
	
	protected void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message);
		else
			((PartiesPlugin) plugin).getMessageUtils().sendMessage(receiver, message, playerReceiver, playerReceiver != null ? ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(playerReceiver) : null);
	}
	
	protected void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message, PartyPlayerImpl victim) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message, victim);
		else
			((PartiesPlugin) plugin).getMessageUtils().sendMessage(receiver, message, victim, victim != null ? ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(victim) : null);
	}
	
	protected void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message, PartyImpl party) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message, party);
		else
			((PartiesPlugin) plugin).getMessageUtils().sendMessage(receiver, message, playerReceiver, party);
	}
	
	protected void sendMessage(User receiver, PartyPlayerImpl playerReceiver, String message, PartyPlayerImpl victim, PartyImpl party) {
		if (receiver.isPlayer())
			playerReceiver.sendMessage(message, victim, party);
		else
			((PartiesPlugin) plugin).getMessageUtils().sendMessage(receiver, message, victim, party);
	}
}
