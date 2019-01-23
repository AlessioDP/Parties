package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.UUID;

public class MessageManager {
	private BukkitPartiesPlugin plugin;
	private MessageListener messageListener;
	
	public MessageManager(PartiesPlugin instance) {
		plugin = (BukkitPartiesPlugin) instance;
		messageListener = new MessageListener(plugin);
		reload();
	}
	
	public void reload() {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE)
			messageListener.register();
		else
			messageListener.unregister();
	}
	
	private void sendPacket(PartiesPacket packet) {
		Player dummyPlayer = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		if (dummyPlayer != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			try {
				out.writeUTF("Forward");
				out.writeUTF("ALL");
				out.writeUTF(Constants.MESSAGING_SUBCHANNEL);
				
				byte[] bytes = packet.makePacket();
				out.writeShort(bytes.length);
				out.write(bytes);
				
				dummyPlayer.sendPluginMessage(plugin.getBootstrap(), Constants.MESSAGING_CHANNEL, baos.toByteArray());
			} catch (Exception ex) {
				LoggerManager.printError(Constants.DEBUG_MESSAGING_PACKET_SENDERROR);
				ex.printStackTrace();
			}
		}
	}
	
	public void sendPingUpdatePlayer(UUID player) {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE) {
			// Prepare packet update player
			PartiesPacket packet = new PartiesPacket(plugin.getVersion());
			packet.setType(PartiesPacket.Type.PLAYER_UPDATED);
			packet.setPlayerUuid(player);
			
			sendPacket(packet);
		}
	}
	
	public void sendPingUpdateParty(String party) {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE) {
			// Prepare packet update party
			PartiesPacket packet = new PartiesPacket(plugin.getVersion());
			packet.setType(PartiesPacket.Type.PARTY_UPDATED);
			packet.setPartyName(party);
			
			sendPacket(packet);
		}
	}
	
	public void sendPingRenameParty(String party, String oldName) {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE) {
			// Prepare packet rename party
			PartiesPacket packet = new PartiesPacket(plugin.getVersion());
			packet.setType(PartiesPacket.Type.PARTY_RENAMED);
			packet.setPartyName(party);
			packet.setPayload(oldName);
			
			sendPacket(packet);
		}
	}
	
	public void sendPingRemoveParty(String party) {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE) {
			// Prepare packet remove party
			PartiesPacket packet = new PartiesPacket(plugin.getVersion());
			packet.setType(PartiesPacket.Type.PARTY_REMOVED);
			packet.setPartyName(party);
			
			sendPacket(packet);
		}
	}
	
	public void sendPingChatMessage(String party, UUID sender, String message) {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE && BukkitConfigMain.PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT) {
			// Prepare packet broadcast message
			PartiesPacket packet = new PartiesPacket(plugin.getVersion());
			packet.setType(PartiesPacket.Type.CHAT_MESSAGE);
			packet.setPartyName(party);
			packet.setPlayerUuid(sender);
			packet.setPayload(message);
			
			sendPacket(packet);
		}
	}
	
	public void sendPingBroadcastMessage(String party, String message) {
		if (BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE && BukkitConfigMain.PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS) {
			// Prepare packet broadcast message
			PartiesPacket packet = new PartiesPacket(plugin.getVersion());
			packet.setType(PartiesPacket.Type.BROADCAST_MESSAGE);
			packet.setPartyName(party);
			packet.setPayload(message);
			
			sendPacket(packet);
		}
	}
}
