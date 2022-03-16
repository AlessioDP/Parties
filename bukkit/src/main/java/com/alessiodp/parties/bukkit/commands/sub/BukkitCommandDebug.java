package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandDebug;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BukkitCommandDebug extends CommandDebug {
	private String syntaxBungeecord;
	
	public BukkitCommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
		
		if (getPlugin().isBungeeCordEnabled()) {
			syntaxBungeecord = String.format("%s %s",
					baseSyntax(),
					extraBungeecord
			);
		}
	}
	
	@Override
	protected void initializeExtraCommands() {
		if (getPlugin().isBungeeCordEnabled()) {
			extraBungeecord = BukkitConfigMain.COMMANDS_MISC_BUNGEECORD;
			CommandType.commandBungeecord = BukkitConfigMain.COMMANDS_MISC_BUNGEECORD;
		}
	}
	
	@Override
	protected boolean commandHandleExtra(CommandDebug.CommandType commandType, CommandData commandData, User sender, PartyPlayerImpl partyPlayer) {
		if (commandType == CommandType.BUNGEECORD) {
			if (commandData.getArgs().length != 2) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntaxBungeecord));
				return true;
			}
			return false;
		}
		return super.commandHandleExtra(commandType, commandData, sender, partyPlayer);
	}
	
	@Override
	protected void commandStart(CommandType commandType, User sender, PartyPlayerImpl partyPlayer, PartyImpl targetParty, PartyPlayerImpl targetPlayer) {
		if (commandType == CommandType.BUNGEECORD) {
			UUID temporaryUuid = UUID.randomUUID();
			TemporaryPartyPlayer temporaryPlayer = new TemporaryPartyPlayer((PartiesPlugin) plugin, temporaryUuid);
			
			UUID receiver = null;
			boolean replyToPlayer = false;
			
			if (partyPlayer != null) {
				receiver = partyPlayer.getPlayerUUID();
				replyToPlayer = true;
			} else {
				User anyUser = plugin.getOnlinePlayers().stream().findAny().orElse(null);
				if (anyUser != null) {
					receiver = anyUser.getUUID();
				}
			}
			
			final UUID finalReceiver = receiver;
			final boolean finalReplyToPlayer = replyToPlayer;
			((PartiesDatabaseManager) plugin.getDatabaseManager())
					.updatePlayer(temporaryPlayer)
					.thenRun(() -> {
						((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDebugBungeeCord(
								temporaryUuid,
								finalReceiver,
								finalReplyToPlayer
						);
						sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_DEBUG_BUNGEECORD_SENT);
						
						plugin.getScheduler().scheduleAsyncLater(() -> {
							temporaryPlayer.setPersistent(false); // Set it not persistent so it can be removed from db
							((PartiesDatabaseManager) plugin.getDatabaseManager()).updatePlayer(temporaryPlayer);
						}, 10, TimeUnit.SECONDS);
			});
		} else {
			super.commandStart(commandType, sender, partyPlayer, targetParty, targetPlayer);
		}
	}
}
