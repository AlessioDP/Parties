package com.alessiodp.parties.utils.addon;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.utils.api.PartiesAPI;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class ProtocolHandler {
	Parties plugin;
	static PacketContainer latest;
	
	static HashMap<UUID, PlayerInfoData> listPlayers;

	public ProtocolHandler(Parties instance) {
		plugin = instance;
		listPlayers = new HashMap<UUID, PlayerInfoData>();
	}
	public boolean start(){
		try{
			handleClean();
		}catch(ClassNotFoundException ex){
			return false;
		}
		return true;
	}

	public static void send(UUID u) {
		if(listPlayers == null)
			return;
		try {
			PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
			
			List<PlayerInfoData> list = new ArrayList<PlayerInfoData>();
			for(Entry<UUID, PlayerInfoData> e : listPlayers.entrySet()){
				list.add(e.getValue());
			}
			packet.getPlayerInfoDataLists().write(0, list);
			packet.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
			
			ProtocolLibrary.getProtocolManager().sendServerPacket(Bukkit.getPlayer(u), packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public void handleClean() throws ClassNotFoundException {
		ProtocolLibrary.getProtocolManager()
				.addPacketListener(new PacketAdapter(plugin, new PacketType[] { PacketType.Play.Server.PLAYER_INFO }) {
					public void onPacketSending(PacketEvent event) {
						if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO)
							return;
						if (event.getPacket().getPlayerInfoAction().read(0) == EnumWrappers.PlayerInfoAction.ADD_PLAYER) {

							// Adding PlayerInfoData into HashMap<UUID of player, PlayerDataInfo>
							for(PlayerInfoData playerInfo : (List<PlayerInfoData>) event.getPacket().getPlayerInfoDataLists().read(0)){
								listPlayers.put(playerInfo.getProfile().getUUID(), playerInfo);
							}
							
							List<PlayerInfoData> newPlayerInfo = new ArrayList<PlayerInfoData>();
							for (PlayerInfoData playerInfo : (List<PlayerInfoData>) event.getPacket().getPlayerInfoDataLists().read(0)) {
								if ((playerInfo == null) || (playerInfo.getProfile() == null) || ((plugin.getServer().getPlayer(playerInfo.getProfile().getUUID())) == null)) {
									// Skip empty playerInfo
									newPlayerInfo.add(playerInfo);
								} else {
									// Editing of PlayerInfoData
									WrappedGameProfile wp = new WrappedGameProfile(playerInfo.getProfile().getUUID(), playerInfo.getProfile().getName());
									
									// My plugin stuff (Checking if player is in a party)
									PartiesAPI api = new PartiesAPI();
									if (api.haveParty(event.getPlayer().getUniqueId()) && api.haveParty(playerInfo.getProfile().getUUID()) && api.getPartyName(playerInfo.getProfile().getUUID()).equals(api.getPartyName(event.getPlayer().getUniqueId()))) {
										// Colorize name of the player
										//wp = new WrappedGameProfile(playerInfo.getProfile().getUUID(),  "§b" + playerInfo.getProfile().getName());
										// Colorize tab list name of the player and adding new PlayerInfoData
										if(Variables.tablist_inparty.isEmpty())
											newPlayerInfo.add(playerInfo);
										else{
											newPlayerInfo.add(new PlayerInfoData(wp, playerInfo.getLatency(),
													playerInfo.getGameMode(),
													WrappedChatComponent.fromJson(Variables.tablist_inparty
															.replace("%player%", playerInfo.getProfile().getName()))));
										}
									} else {
										// Skip playerInfo (Out of party)
										if(Variables.tablist_outparty.isEmpty())
											newPlayerInfo.add(playerInfo);
										else
											newPlayerInfo.add(new PlayerInfoData(wp, playerInfo.getLatency(),
															playerInfo.getGameMode(),
															WrappedChatComponent.fromJson(Variables.tablist_outparty
																	.replace("%player%", playerInfo.getProfile().getName()))));
									}
								}
							}
							// Write on packet newPlayerInfo
							event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfo);
						} else if (event.getPacket().getPlayerInfoAction() .read(0) == EnumWrappers.PlayerInfoAction.REMOVE_PLAYER) {
							for(PlayerInfoData playerInfo : (List<PlayerInfoData>) event.getPacket().getPlayerInfoDataLists().read(0)){
								listPlayers.remove(playerInfo.getProfile().getUUID());
							}
						}
					}
				});
	}
	public static void handleHF(){
		if(Variables.tablist_header_inparty.isEmpty() && Variables.tablist_header_outparty.isEmpty()&& 
				Variables.tablist_footer_inparty.isEmpty() && Variables.tablist_footer_outparty.isEmpty())
			return;
		Parties plugin = Parties.getInstance();
		ProtocolManager proto = ProtocolLibrary.getProtocolManager();
		PacketContainer pc = proto.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
		
		for(Player pl : plugin.getServer().getOnlinePlayers()){
			String header = "", footer = "";
			Party party = plugin.getPlayerHandler().getPartyFromPlayer(pl);
			if(party!=null){
				header = party.convertText(Variables.tablist_header_inparty, pl);
				footer = party.convertText(Variables.tablist_footer_inparty, pl);
			} else {
				header = plugin.getPlayerHandler().convertText(Variables.tablist_header_outparty, pl);
				footer = plugin.getPlayerHandler().convertText(Variables.tablist_footer_outparty, pl);
			}
			if(!header.isEmpty())
				pc.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', header)));
			if(!footer.isEmpty())
				pc.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', footer)));
			try {
				proto.sendServerPacket(pl, pc);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
