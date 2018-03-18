package com.alessiodp.parties.addons.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		
		// Load entities
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		try {
			// Get packet in input
			Packet packet = new Packet(in);
			if (packet.getVersion().equals(plugin.getDescription().getVersion())) {
				
				// Does the player have a party
				if (party != null) {
					
					// Check for rank needed to follow
					if (pp.getRank() < packet.getRankNeeded())
						return;
					
					// Preparing a list that contains the name of each player to teleport
					List<String> list = new ArrayList<String>();
					for (Player pl : party.getOnlinePlayers()) {
						UUID pUuid = pl.getUniqueId();
						if (!pUuid.equals(player.getUniqueId())) {
							// Have the player the minimum rank to follow?
							if (plugin.getPlayerManager().getPlayer(pUuid).getRank() >= packet.getRankMinimum()) {
								list.add(pUuid.toString());
							}
						}
					}
					// Set the info list
					packet.setInfo(list);
					
					// Set the message for the user
					packet.setMessage(ChatColor.translateAlternateColorCodes('&', Messages.OTHER_FOLLOW_SERVER
							.replace("%server%", packet.getServer())));
					
					// Send the output
					ByteArrayOutputStream outstream = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(outstream);
					packet.write(out);
					
					try {
						Player p = Bukkit.getServer().getOnlinePlayers().iterator().next();
						if (p != null) {
							LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_BUNGEE_REPLY, true);
							p.sendPluginMessage(plugin, partiesChannel, outstream.toByteArray());
						}
					} catch (Exception ex) {
						// Server empty
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
