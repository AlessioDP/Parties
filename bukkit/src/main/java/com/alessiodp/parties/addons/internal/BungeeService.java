package com.alessiodp.parties.addons.internal;

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
import com.alessiodp.parties.bungeecord.utils.Packet;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;

public class BungeeService implements PluginMessageListener {
	private Parties plugin;
	private String partiesChannel;
	
	public BungeeService(Parties instance) {
		plugin = instance;
		partiesChannel = com.alessiodp.parties.bungeecord.configuration.Constants.BUNGEE_CHANNEL;
		init();
	}
	
	private void init() {
		if (ConfigMain.PARTIES_BUNGEECORD) {
			plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, partiesChannel);
			plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, partiesChannel, this);
			
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_BUNGEE_READY, true, ConsoleColor.CYAN);
		}
	}
	
	
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(partiesChannel)) {
			return;
		}
		ByteArrayInputStream stream = new ByteArrayInputStream(message);
		DataInputStream in = new DataInputStream(stream);
		Packet packet;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
		try {
			packet = new Packet(in);
			if (packet.getVersion().equals(plugin.getDescription().getVersion())) {
				if (party != null) {
					if (pp.getRank() < packet.getRankNeeded())
						return;
					List<String> list = new ArrayList<String>();
					for (Player pl : party.getOnlinePlayers()) {
						if ((pl != player) && (plugin.getPlayerManager().getPlayer(pl.getUniqueId()).getRank() >= packet.getRankMinimum())) {
							list.add(pl.getUniqueId().toString());
						}
					}
					packet.setInfo(list);
					packet.setMessage(ChatColor.translateAlternateColorCodes('&', Messages.OTHER_FOLLOW_SERVER
							.replace("%server%", packet.getServer())));
					/*
					 * Send output
					 */
					ByteArrayOutputStream outstream = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(outstream);
					packet.write(out);
					Player p = Bukkit.getServer().getOnlinePlayers().iterator().next();
					if (p != null) {
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_BUNGEE_REPLY, true);
						p.sendPluginMessage(plugin, partiesChannel, outstream.toByteArray());
					}
				}
			} else {
				LoggerManager.printError(Constants.DEBUG_BUNGEE_VERSIONMISMATCH
						.replace("{packet}", packet.getVersion()));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
