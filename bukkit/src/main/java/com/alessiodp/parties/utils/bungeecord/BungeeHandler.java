package com.alessiodp.parties.utils.bungeecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.PartiesBungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeHandler implements Listener {
	PartiesBungee plugin;
	
	public BungeeHandler(PartiesBungee instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onConnect(ServerConnectEvent ev) {
		if (ev.isCancelled())
			return;
		ProxiedPlayer p = ev.getPlayer();
		if (p.getServer() == null)
			return;
		if (p.getServer().getInfo().equals(ev.getTarget()))
			return;
		if (!listContains(VariablesBungee.follow_listserver, p.getServer().getInfo().getName()))
			return;
		/*
		 * 
		 */
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			Packet packet = new Packet(plugin.getDescription().getVersion(), ev.getTarget().getName(), VariablesBungee.follow_neededrank, VariablesBungee.follow_minimumrank, "", null);
			packet.write(out);
			if (ev.getPlayer().getServer() != null)
				ev.getPlayer().getServer().sendData(PartiesBungee.channel, stream.toByteArray());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent ev) {
		if (!ev.getTag().equalsIgnoreCase(PartiesBungee.channel))
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
	private boolean listContains(List<String> l, String str) {
		for (String s : l) {
			if (str.equalsIgnoreCase(s) || s.equals("*"))
				return true;
		}
		return false;
	}
}
