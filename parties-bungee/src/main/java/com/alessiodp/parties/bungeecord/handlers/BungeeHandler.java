package com.alessiodp.parties.bungeecord.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.bungeecord.PartiesBungee;
import com.alessiodp.parties.bungeecord.configuration.Variables;
import com.alessiodp.parties.bungeecord.utils.Packet;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeHandler implements Listener {
	private PartiesBungee plugin;
	private String partiesChannel;
	
	public BungeeHandler(PartiesBungee instance) {
		plugin = instance;
		
		partiesChannel = PartiesBungee.CHANNEL;
		plugin.getProxy().registerChannel(partiesChannel);
		plugin.getProxy().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onConnect(ServerConnectEvent ev) {
		/*
		 * Connect chain starts here,
		 * this method will sent a PartiesPacket to the player server
		 */
		if (ev.isCancelled())
			return;
		ProxiedPlayer p = ev.getPlayer();
		if (p.getServer() == null)
			return;
		if (p.getServer().getInfo().equals(ev.getTarget()))
			return;
		if (!listContains(Variables.follow_listserver, p.getServer().getInfo().getName()))
			return;
		/*
		 * 
		 */
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			Packet packet = new Packet(plugin.getDescription().getVersion(), ev.getTarget().getName(), Variables.follow_neededrank, Variables.follow_minimumrank, "", null);
			packet.write(out);
			if (ev.getPlayer().getServer() != null) {
				PartiesBungee.debugLog("Parties packet sent to " + ev.getPlayer().getServer().getInfo().getName());
				ev.getPlayer().getServer().sendData(partiesChannel, stream.toByteArray());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	private boolean listContains(List<String> l, String str) {
		for (String s : l) {
			if (str.equalsIgnoreCase(s) || s.equals("*"))
				return true;
		}
		return false;
	}
	
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent ev) {
		/*
		 * This method is the listener for the PartiesPacket callback
		 */
		if (!ev.getTag().equalsIgnoreCase(partiesChannel))
			return;
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(ev.getData()));
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
