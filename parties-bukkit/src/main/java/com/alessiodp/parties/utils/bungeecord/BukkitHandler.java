package com.alessiodp.parties.utils.bungeecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.bungeecord.PartiesBungee;
import com.alessiodp.parties.bungeecord.utils.Packet;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class BukkitHandler implements PluginMessageListener {
	private Parties plugin;
	private String partiesChannel;
	
	public BukkitHandler(Parties instance) {
		plugin = instance;
		partiesChannel = PartiesBungee.CHANNEL;
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, partiesChannel);
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, partiesChannel, this);
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(partiesChannel)) {
			return;
		}
		ByteArrayInputStream stream = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(stream);
		Packet packet;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(player.getUniqueId());
		Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
		try {
			packet = new Packet(in);
			if (packet.getVersion().equals(plugin.getDescription().getVersion())) {
				if (party != null) {
					if (tp.getRank() < packet.getRankNeeded())
						return;
					List<String> list = new ArrayList<String>();
					for (Player pl : party.getOnlinePlayers()) {
						if ((pl != player) && (plugin.getPlayerHandler().getPlayer(pl.getUniqueId()).getRank() >= packet.getRankMinimum())) {
							list.add(pl.getUniqueId().toString());
						}
					}
					packet.setInfo(list);
					packet.setMessage(ChatColor.translateAlternateColorCodes('&', Messages.follow_following_server.replace("%server%", packet.getServer())));
					/*
					 * Send output
					 */
					ByteArrayOutputStream outstream = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(outstream);
					packet.write(out);
					Player p = Bukkit.getServer().getOnlinePlayers().iterator().next();
					if (p != null) {
						PartiesBungee.debugLog("Parties packet sent back to the channel");
						p.sendPluginMessage(plugin, partiesChannel, outstream.toByteArray());
					}
				}
			} else {
				LogHandler.printError("Skipping Bungeecord Parties packet. Versions don't match (" + packet.getVersion() + ")");
			}
		} catch (IOException ex) {
			LogHandler.printError("Something gone wrong with Bungeecord handler: " + ex.getMessage());
		}
	}
}
