package com.alessiodp.parties.velocity.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandDebug;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.configuration.data.VelocityMessages;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;

import java.util.UUID;

public class VelocityCommandDebug extends CommandDebug {
	public VelocityCommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	public static void handleDebugBungeecord(PartiesPlugin plugin, UUID temporaryUuid, UUID receiver, boolean replyToPlayer, String sourceServer) {
		PartyPlayerImpl player = plugin.getDatabaseManager().getPlayer(temporaryUuid);
		User userReceiver = receiver != null ? plugin.getPlayer(receiver) : null;
		
		if (player != null) {
			TemporaryPartyPlayer temporaryPlayer = new TemporaryPartyPlayer(plugin, player.getPlayerUUID());
			temporaryPlayer.setPersistent(false);
			plugin.getDatabaseManager().updatePlayer(temporaryPlayer); // Remove the player
			
			if (userReceiver != null)
				((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDebugBungeecordReply(userReceiver, true, replyToPlayer);
			
			if (userReceiver != null && replyToPlayer)
				userReceiver.sendMessage(VelocityMessages.ADDCMD_DEBUG_BUNGEECORD_SYNC
						.replace("%server%", sourceServer), true);
			else
				plugin.getLoggerManager().log(VelocityMessages.ADDCMD_DEBUG_BUNGEECORD_SYNC
						.replace("%server%", sourceServer));
		} else {
			if (userReceiver != null)
				((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDebugBungeecordReply(userReceiver, false, replyToPlayer);
			
			if (userReceiver != null && replyToPlayer)
				userReceiver.sendMessage(VelocityMessages.ADDCMD_DEBUG_BUNGEECORD_NOT_SYNC
						.replace("%server%", sourceServer), true);
			else
				plugin.getLoggerManager().log(VelocityMessages.ADDCMD_DEBUG_BUNGEECORD_NOT_SYNC
						.replace("%server%", sourceServer));
		}
	}
}
