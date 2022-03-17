package com.alessiodp.parties.bukkit.messaging.bungee;

import com.alessiodp.core.bukkit.messaging.bungee.BukkitBungeecordListener;
import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandHome;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandSetHome;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.messaging.CommonListener;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitPartiesBungeecordListener extends BukkitBungeecordListener {
	private final CommonListener commonListener;
	
	public BukkitPartiesBungeecordListener(@NotNull ADPPlugin plugin) {
		super(plugin, false, true, false);
		commonListener = new CommonListener((PartiesPlugin) plugin);
	}
	
	@Override
	protected void handlePacket(byte[] bytes, @NotNull MessageChannel messageChannel) {
		if (messageChannel != MessageChannel.SUB)
			return; // Handle only packets for sub channel
		
		PartiesPacket packet = PartiesPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_BUNGEE_RECEIVED, packet.getType().name(), messageChannel.getId()), true);
			switch ((PartiesPacket.PacketType) packet.getType()) {
				case UPDATE_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handleUpdateParty(packet.getParty());
					break;
				case UPDATE_PLAYER:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC)
						commonListener.handleUpdatePlayer(packet.getPlayer());
					break;
				case LOAD_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES)
						handleLoadParty(packet);
					break;
				case LOAD_PLAYER:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS)
						handleLoadPlayer(packet);
					break;
				case UNLOAD_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES)
						handleUnloadParty(packet);
					break;
				case UNLOAD_PLAYER:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS)
						handleUnloadPlayer(packet);
					break;
				case PLAY_SOUND:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_SOUNDS)
						handlePlaySound(packet);
					break;
				case CREATE_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handlePostPartyCreate(packet.getParty(), packet.getPlayer(), true);
					break;
				case DELETE_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handlePostPartyDelete(packet.getParty(), (DeleteCause) packet.getCause(), packet.getPlayer(), packet.getSecondaryPlayer());
					break;
				case RENAME_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handlePostPartyRename(packet.getParty(), packet.getText(), packet.getSecondaryText(), packet.getPlayer(), packet.isBool());
					break;
				case ADD_MEMBER_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handlePostPartyAddMember(packet.getParty(), packet.getPlayer(), (JoinCause) packet.getCause(), packet.getPlayer());
					break;
				case REMOVE_MEMBER_PARTY:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handlePostPartyRemoveMember(packet.getParty(), packet.getPlayer(), (LeaveCause) packet.getCause(), packet.getPlayer());
					break;
				case CHAT_MESSAGE:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CHAT)
						commonListener.handlePostChat(packet.getParty(), packet.getPlayer(), packet.getText(), packet.getSecondaryText());
					break;
				case BROADCAST_MESSAGE:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_BROADCAST)
						commonListener.handlePostBroadcast(packet.getParty(), packet.getPlayer(), packet.getText());
					break;
				case INVITE_PLAYER:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						commonListener.handlePostInvitePlayer(packet.getParty(), packet.getPlayer(), packet.getSecondaryPlayer());
					break;
				case ADD_HOME:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC)
						handleAddHome(packet);
					break;
				case HOME_TELEPORT:
					handleHomeTeleport(packet);
					break;
				case TELEPORT:
					handleTeleport(packet);
					break;
				case EXPERIENCE:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC && ConfigMain.ADDITIONAL_EXP_ENABLE)
						commonListener.handlePostExperience(packet.getParty(), packet.getPlayer(), packet.getNumber(), true);
					break;
				case LEVEL_UP:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC && ConfigMain.ADDITIONAL_EXP_ENABLE)
						commonListener.handlePostLevelUp(packet.getParty(), (int) packet.getNumber(), true);
					break;
				case REQUEST_CONFIGS:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CONFIG_SYNC)
						handleRequestConfigs(packet);
					break;
				case DEBUG_BUNGEECORD:
					if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_DEBUG_BUNGEECORD)
						handleDebugBungeecordReply(packet);
					break;
				default:
					// Nothing to do
					break;
			}
		}
	}
	
	public void handleLoadParty(PartiesPacket packet) {
		((PartiesPlugin) plugin).getPartyManager().loadParty(packet.getParty());
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LOAD_PARTY,
				packet.getParty().toString()), true);
	}
	
	public void handleLoadPlayer(PartiesPacket packet) {
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().loadPlayer(packet.getPlayer());
		if (partyPlayer != null) {
			if (partyPlayer.isInParty()) {
				// Load party
				((PartiesPlugin) plugin).getPartyManager().loadParty(partyPlayer.getPartyId());
			}
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LOAD_PLAYER,
					packet.getPlayer().toString()), true);
		}
	}
	
	public void handleUnloadParty(PartiesPacket packet) {
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getCacheParties().get(packet.getParty());
		if (party != null) {
			((PartiesPlugin) plugin).getPartyManager().unloadParty(party);
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UNLOAD_PARTY,
					packet.getParty().toString()), true);
		}
	}
	
	public void handleUnloadPlayer(PartiesPacket packet) {
		((PartiesPlugin) plugin).getPlayerManager().unloadPlayer(packet.getPlayer());
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UNLOAD_PLAYER,
				packet.getPlayer().toString()), true);
	}
	
	public void handlePlaySound(PartiesPacket packet) {
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayer());
		if (partyPlayer != null) {
			partyPlayer.playSound(
					packet.getText(),
					packet.getNumber(),
					packet.getSecondaryNumber()
			);
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_PLAY_SOUND,
				packet.getPlayer().toString()), true);
	}
	
	public void handleAddHome(PartiesPacket packet) {
		((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getParty());
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getParty());
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayer());
		if (party != null && partyPlayer != null) {
			try {
				String name = packet.getText();
				String server = packet.getSecondaryText();
				
				PartyHomeImpl home = BukkitCommandSetHome.getHomeLocationOfPlayer(partyPlayer, name, server);
				if (home != null) {
					if (((PartiesPlugin) plugin).isBungeeCordEnabled())
						((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddHome(party, home.toString());
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_HOME,
							home, party.getId(), partyPlayer.getPlayerUUID(), server), true);
				}
			} catch (Exception ex) {
				plugin.getLoggerManager().logError(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_HOME_ERROR, ex);
			}
		}
	}
	
	public void handleHomeTeleport(PartiesPacket packet) {
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayer());
		if (partyPlayer != null) {
			User user = plugin.getPlayer(packet.getPlayer());
			
			if (user != null) {
				try {
					String homeSerialized = packet.getText();
					String message = packet.getSecondaryText();
					
					PartyHomeImpl home = PartyHomeImpl.deserialize(homeSerialized);
					
					if (home != null) {
						Location location = new Location(
								Bukkit.getWorld(home.getWorld()),
								home.getX(),
								home.getY(),
								home.getZ(),
								home.getYaw(),
								home.getPitch()
						);
						
						BukkitCommandHome.teleportToPartyHome((PartiesPlugin) plugin, partyPlayer, (BukkitUser) user, home, location, message);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_HOME_TELEPORT,
								packet.getPlayer().toString(), homeSerialized), true);
					}
				} catch (Exception ex) {
					plugin.getLoggerManager().logError(PartiesConstants.DEBUG_MESSAGING_LISTEN_HOME_TELEPORT_ERROR, ex);
				}
			}
		}
	}
	
	public void handleTeleport(PartiesPacket packet) {
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayer());
		if (partyPlayer != null) {
			User user = plugin.getPlayer(packet.getPlayer());
			
			if (user != null) {
				
				try {
					UUID targetUuid = packet.getSecondaryPlayer();
					
					Player bukkitTargetPlayer = Bukkit.getPlayer(targetUuid);
					if (bukkitTargetPlayer != null) {
						plugin.getScheduler().getSyncExecutor().execute(() -> ((BukkitUser) user).teleportAsync(bukkitTargetPlayer.getLocation()));
					}
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_TELEPORT,
							packet.getPlayer().toString(), packet.getSecondaryPlayer()), true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void handleRequestConfigs(PartiesPacket packet) {
		((PartiesConfigurationManager) plugin.getConfigurationManager()).parsePacketConfigData(packet.getConfigData());
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_CONFIGS, true);
	}
	
	public void handleDebugBungeecordReply(PartiesPacket packet) {
		User user = packet.getPlayer() != null ? plugin.getPlayer(packet.getPlayer()) : null;
		if (user != null) {
			if (packet.isBool())
				user.sendMessage(Messages.ADDCMD_DEBUG_BUNGEECORD_SYNC, true);
			else
				user.sendMessage(Messages.ADDCMD_DEBUG_BUNGEECORD_NOT_SYNC, true);
		} else {
			if (packet.isBool())
				plugin.getLoggerManager().log(Messages.ADDCMD_DEBUG_BUNGEECORD_SYNC, true);
			else
				plugin.getLoggerManager().log(Messages.ADDCMD_DEBUG_BUNGEECORD_NOT_SYNC, true);
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_BUNGEECORD_REPLY, packet.isBool()), true);
	}
}
