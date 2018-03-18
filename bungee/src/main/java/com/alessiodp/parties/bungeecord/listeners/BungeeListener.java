package com.alessiodp.parties.bungeecord.listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.bungeecord.PartiesBungee;
import com.alessiodp.parties.bungeecord.configuration.ConfigMain;
import com.alessiodp.parties.bungeecord.configuration.Constants;
import com.alessiodp.parties.bungeecord.utils.Packet;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListener implements Listener {
	private PartiesBungee plugin;
	private String partiesChannel;
	
	public BungeeListener(PartiesBungee instance) {
		plugin = instance;
		
		partiesChannel = Constants.BUNGEE_CHANNEL;
		plugin.getProxy().registerChannel(partiesChannel);
		plugin.getProxy().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onConnect(ServerConnectEvent event) {
		/*
		 * Connect chain starts here,
		 * this method will sent a PartiesPacket to the player server
		 */
		if (event.isCancelled())
			return;
		ProxiedPlayer proxyPlayer = event.getPlayer();
		// Return if its not a player
		if (proxyPlayer.getServer() == null)
			return;
		
		// Return if the player is already in the server
		if (proxyPlayer.getServer().getInfo().equals(event.getTarget()))
			return;
		
		// Return if the server is not into the follow list
		if (!listContains(ConfigMain.follow_listserver, proxyPlayer.getServer().getInfo().getName()))
			return;
		/*
		 * 
		 */
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			
			// Initialize packet
			Packet packet = new Packet(plugin.getDescription().getVersion(),
					event.getTarget().getName(),
					ConfigMain.follow_neededrank,
					ConfigMain.follow_minimumrank);
			
			// Write to the DataOutputStream the data
			packet.write(out);
			
			if (proxyPlayer.getServer() != null) {
				PartiesBungee.debugLog("Parties packet sent to " + proxyPlayer.getServer().getInfo().getName());
				proxyPlayer.getServer().sendData(partiesChannel, stream.toByteArray());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean listContains(List<String> list, String str) {
		for (String s : list) {
			if (str.equalsIgnoreCase(s) || s.equals("*"))
				return true;
		}
		return false;
	}
	
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
		/*
		 * This method is the listener for the PartiesPacket callback
		 */
		if (!event.getTag().equalsIgnoreCase(partiesChannel))
			return;
		
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(event.getData()));
		try {
			Packet packet = new Packet(data);
			if (packet.getVersion().equals(plugin.getDescription().getVersion())) {
				for (String str : packet.getInfo()) {
					if (!str.isEmpty())
						sendPlayerServer(str, packet.getServer(), packet.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendPlayerServer(String uuid, String server, String message) {
		ServerInfo s = plugin.getProxy().getServerInfo(server);
		if (message != null && !message.isEmpty())
			plugin.getProxy().getPlayer(UUID.fromString(uuid)).sendMessage(TextComponent.fromLegacyText(message));
		plugin.getProxy().getPlayer(UUID.fromString(uuid)).connect(s);
	}
}
