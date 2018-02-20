package com.alessiodp.parties.addons.external;

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
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.objects.PartyPlayer;
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

import lombok.Getter;

public class ProtocolLibHandler {
	private Parties parties;
	private static final String ADDON_NAME = "ProtocolLib";
	@Getter private static boolean active = false;
	
	private static ProtocolLibUtil protocol;
	
	public ProtocolLibHandler(Parties instance) {
		parties = instance;
		init();
	}
	
	private void init() {
		if (ConfigMain.ADDONS_TABLIST_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				if (start())
					LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
							.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigMain.ADDONS_TABLIST_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	private boolean start() {
		protocol = new ProtocolLibUtil();
		return active;
	}
	
	public static void refreshParty(PartyEntity party) {
		if (ConfigMain.ADDONS_TABLIST_ENABLE && active) {
			// Refresh header and footer
			protocol.handleHF();
			
			if (party != null) {
				// Refresh party players
				for (Player pl : party.getOnlinePlayers())
					protocol.send(pl.getUniqueId());
			}
		}
	}
	
	public static void refreshPlayer(PartyPlayerEntity player) {
		if (ConfigMain.ADDONS_TABLIST_ENABLE && active) {
			// Refresh player
			protocol.send(player.getPlayerUUID());
		}
	}
	
	public class ProtocolLibUtil {
		private HashMap<UUID, PlayerInfoData> listPlayers;
		
		public ProtocolLibUtil() {
			active = true;
			try {
				listPlayers = new HashMap<UUID, PlayerInfoData>();
				handleClean();
			} catch(ClassNotFoundException ex) {
				active = false;
			}
		}
		
		public void send(UUID u) {
			if (active && listPlayers != null) {
				try {
					PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
					
					List<PlayerInfoData> list = new ArrayList<PlayerInfoData>();
					for (Entry<UUID, PlayerInfoData> e : listPlayers.entrySet()) {
						list.add(e.getValue());
					}
					packet.getPlayerInfoDataLists().write(0, list);
					packet.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
					
					ProtocolLibrary.getProtocolManager().sendServerPacket(Bukkit.getPlayer(u), packet);
				} catch (InvocationTargetException ex) {
					ex.printStackTrace();
				}
			}
		}
		public void handleClean() throws ClassNotFoundException {
			ProtocolLibrary.getProtocolManager()
					.addPacketListener(new PacketAdapter(parties, new PacketType[] { PacketType.Play.Server.PLAYER_INFO }) {
						public void onPacketSending(PacketEvent event) {
							if (!active || event.getPacketType() != PacketType.Play.Server.PLAYER_INFO)
								return;
							if (event.getPacket().getPlayerInfoAction().read(0) == EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
								
								// Adding PlayerInfoData into HashMap<UUID of player, PlayerDataInfo>
								for (PlayerInfoData playerInfo : (List<PlayerInfoData>) event.getPacket().getPlayerInfoDataLists().read(0)) {
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
										/*PartyPlayer eventPp = CompletableFuture.supplyAsync(() -> {
											return parties.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
										}, parties.getPartiesScheduler().getPrivateExecutor()).join();
										
										PartyPlayer playerInfoPp = CompletableFuture.supplyAsync(() -> {
											return parties.getPlayerHandler().getPlayer(playerInfo.getProfile().getUUID());
										}, parties.getPartiesScheduler().getPrivateExecutor()).join();
										*/
										PartyPlayer eventPp = parties.getPlayerManager().getListPartyPlayers().get(event.getPlayer().getUniqueId());
										PartyPlayer playerInfoPp = parties.getPlayerManager().getListPartyPlayers().get(playerInfo.getProfile().getUUID());
										
										if (eventPp != null && playerInfoPp != null) {
											if (!eventPp.getPartyName().isEmpty() && eventPp.getPartyName().equals(playerInfoPp.getPartyName())) {
												// Colorize name of the player
												//wp = new WrappedGameProfile(playerInfo.getProfile().getUUID(), "�b" + playerInfo.getProfile().getName());
												// Colorize tab list name of the player and adding new PlayerInfoData
												if (ConfigMain.ADDONS_TABLIST_INPARTY.isEmpty())
													newPlayerInfo.add(playerInfo);
												else {
													newPlayerInfo.add(new PlayerInfoData(wp, playerInfo.getLatency(),
															playerInfo.getGameMode(),
															WrappedChatComponent.fromJson(ConfigMain.ADDONS_TABLIST_INPARTY
																	.replace("%player%", playerInfo.getProfile().getName()))));
												}
											} else {
												// Skip playerInfo (Out of party)
												if (ConfigMain.ADDONS_TABLIST_OUTPARTY.isEmpty())
													newPlayerInfo.add(playerInfo);
												else
													newPlayerInfo.add(new PlayerInfoData(wp, playerInfo.getLatency(),
																	playerInfo.getGameMode(),
																	WrappedChatComponent.fromJson(ConfigMain.ADDONS_TABLIST_OUTPARTY
																			.replace("%player%", playerInfo.getProfile().getName()))));
											}
										}
									}
								}
								// Write on packet newPlayerInfo
								event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfo);
							} else if (event.getPacket().getPlayerInfoAction() .read(0) == EnumWrappers.PlayerInfoAction.REMOVE_PLAYER) {
								for (PlayerInfoData playerInfo : (List<PlayerInfoData>) event.getPacket().getPlayerInfoDataLists().read(0)) {
									listPlayers.remove(playerInfo.getProfile().getUUID());
								}
							}
						}
					});
		}
		public void handleHF() {
			if (active
					&& !(ConfigMain.ADDONS_TABLIST_HEADER_INPARTY.isEmpty() && ConfigMain.ADDONS_TABLIST_HEADER_OUTPARTY.isEmpty()
					&& ConfigMain.ADDONS_TABLIST_FOOTER_INPARTY.isEmpty() && ConfigMain.ADDONS_TABLIST_FOOTER_OUTPARTY.isEmpty())) {
				ProtocolManager proto = ProtocolLibrary.getProtocolManager();
				PacketContainer pc = proto.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
				
				for (Player pl : parties.getServer().getOnlinePlayers()) {
					String header = "", footer = "";
					
					PartyPlayerEntity pp = parties.getPlayerManager().getListPartyPlayers().get(pl.getUniqueId());
					
					PartyEntity party = parties.getPartyManager().getListParties().get(pp.getPartyName());
					
					
					if (party != null) {
						header = PartiesUtils.convertAllPlaceholders(ConfigMain.ADDONS_TABLIST_HEADER_INPARTY, party, pp);
						footer = PartiesUtils.convertAllPlaceholders(ConfigMain.ADDONS_TABLIST_FOOTER_INPARTY, party, pp);
					} else {
						header = parties.getPlayerManager().setTabText(ConfigMain.ADDONS_TABLIST_HEADER_OUTPARTY, pp);
						footer = parties.getPlayerManager().setTabText(ConfigMain.ADDONS_TABLIST_FOOTER_OUTPARTY, pp);
					}
					if (!header.isEmpty())
						pc.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', header)));
					if (!footer.isEmpty())
						pc.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', footer)));
					
					try {
						proto.sendServerPacket(pl, pc);
					} catch (InvocationTargetException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
}
