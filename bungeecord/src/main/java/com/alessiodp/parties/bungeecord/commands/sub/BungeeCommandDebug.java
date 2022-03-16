package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandDebug;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.UUID;

public class BungeeCommandDebug extends CommandDebug {
	public BungeeCommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	public static void handleDebugBungeecord(PartiesPlugin plugin, UUID temporaryUuid, UUID receiver, boolean replyToPlayer) {
		PartyPlayerImpl player = plugin.getDatabaseManager().getPlayer(temporaryUuid);
		User userReceiver = receiver != null ? plugin.getPlayer(receiver) : null;
		
		if (player != null) {
			TemporaryPartyPlayer temporaryPlayer = new TemporaryPartyPlayer(plugin, player.getPlayerUUID());
			temporaryPlayer.setPersistent(false);
			plugin.getDatabaseManager().updatePlayer(temporaryPlayer); // Remove the player
			
			if (userReceiver != null) {
				((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDebugBungeecordReply(userReceiver, true, replyToPlayer);
				
				userReceiver.sendMessage(BungeeMessages.ADDCMD_DEBUG_BUNGEECORD_SYNC, true);
			} else
				plugin.getLoggerManager().log(BungeeMessages.ADDCMD_DEBUG_BUNGEECORD_SYNC);
		} else {
			if (userReceiver != null) {
				((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDebugBungeecordReply(userReceiver, false, replyToPlayer);
				
				userReceiver.sendMessage(BungeeMessages.ADDCMD_DEBUG_BUNGEECORD_NOT_SYNC, true);
			} else
				plugin.getLoggerManager().log(BungeeMessages.ADDCMD_DEBUG_BUNGEECORD_NOT_SYNC);
		}
	}
}
