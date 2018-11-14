package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MessageListener implements PluginMessageListener {
	private BukkitPartiesPlugin plugin;
	
	public MessageListener(PartiesPlugin instance) {
		plugin = (BukkitPartiesPlugin) instance;
		plugin.getBootstrap().getServer().getMessenger().registerOutgoingPluginChannel(plugin.getBootstrap(), Constants.MESSAGING_CHANNEL);
		plugin.getBootstrap().getServer().getMessenger().registerIncomingPluginChannel(plugin.getBootstrap(), Constants.MESSAGING_CHANNEL, this);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		if (channel.equals(Constants.MESSAGING_CHANNEL)) {
			ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
			String subchannel = input.readUTF();
			// Check subchannel
			if (subchannel.equals(Constants.MESSAGING_SUBCHANNEL)) {
				// Goes async
				CompletableFuture.supplyAsync(() -> {
					handlePacket(input);
					return true;
				}, plugin.getPartiesScheduler().getMessagingExecutor())
						.exceptionally(ex -> {
							ex.printStackTrace();
							return false;
						});
			}
		}
	}
	
	private void handlePacket(ByteArrayDataInput input) {
		short len = input.readShort();
		byte[] msgBytes = new byte[len];
		input.readFully(msgBytes);
		
		// Read Parties packet
		PartiesPacket packet = PartiesPacket.readPacket(plugin.getVersion(), msgBytes);
		if (packet != null) {
			PartyImpl party;
			PartyPlayerImpl sender;
			
			switch (packet.getType()) {
				case PLAYER_UPDATED:
					if (plugin.getPlayerManager().reloadPlayer(packet.getPlayerUuid())) {
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_MESSAGING_LISTEN_PLAYER_UPDATED
								.replace("{uuid}", packet.getPlayerUuid().toString()), true);
					}
					break;
				case PARTY_UPDATED:
					if (plugin.getPartyManager().reloadParty(packet.getPartyName())) {
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_MESSAGING_LISTEN_PARTY_UPDATED
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case PARTY_RENAMED:
					// Payload is the old party name
					party = plugin.getPartyManager().getListParties().get(packet.getPayload());
					if (party != null) {
						// Packet getPartyName is the new name
						party.renameParty(packet.getPartyName());
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_MESSAGING_LISTEN_PARTY_RENAMED
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case PARTY_REMOVED:
					party = plugin.getPartyManager().getListParties().get(packet.getPartyName());
					if (party != null) {
						plugin.getPartyManager().getListParties().remove(packet.getPartyName());
						for (UUID uuid : party.getMembers()) {
							PartyPlayerImpl pl = plugin.getPlayerManager().getListPartyPlayers().get(uuid);
							if (pl != null) {
								pl.cleanupPlayer(false);
							}
						}
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_MESSAGING_LISTEN_PARTY_REMOVED
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case CHAT_MESSAGE:
					party = plugin.getPartyManager().getParty(packet.getPartyName());
					sender = plugin.getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (party != null
							&& party.getOnlinePlayers().size() > 0
							&& sender != null) {
						party.sendDirectChatMessage(sender, packet.getPayload(), false);
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case BROADCAST_MESSAGE:
					party = plugin.getPartyManager().getParty(packet.getPartyName());
					if (party != null && party.getOnlinePlayers().size() > 0) {
						party.sendDirectBroadcast(packet.getPayload(), false);
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_MESSAGING_LISTEN_BROADCAST_MESSAGE
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
			}
		}
	}
}
