package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageListener;
import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandHome;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandSetHome;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPartiesMessageListener extends BukkitMessageListener {
	
	public BukkitPartiesMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin, false);
	}
	
	@Override
	public void handlePacket(byte[] bytes) {
		PartiesPacket packet = PartiesPacket.read(plugin, bytes);
		if (packet != null) {
			PartyImpl party;
			PartyPlayerImpl partyPlayer;
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_RECEIVED, packet.getType().name()), true);
			switch (packet.getType()) {
				case UPDATE_PARTY:
					if (((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId())) {
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PARTY,
								packet.getPartyId().toString()), true);
					}
					break;
				case UPDATE_PLAYER:
					if (((PartiesPlugin) plugin).getPlayerManager().reloadPlayer(packet.getPlayerUuid())) {
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER,
								packet.getPlayerUuid().toString()), true);
					}
					break;
				case LOAD_PLAYER:
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().loadPlayer(packet.getPlayerUuid());
					if (partyPlayer != null) {
						if (partyPlayer.isInParty()) {
							// Load party
							((PartiesPlugin) plugin).getPartyManager().loadParty(partyPlayer.getPartyId());
						}
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LOAD_PLAYER,
								packet.getPlayerUuid().toString()), true);
					}
					break;
				case UNLOAD_PARTY:
					party = ((PartiesPlugin) plugin).getPartyManager().getCacheParties().get(packet.getPartyId());
					if (party != null) {
						((PartiesPlugin) plugin).getPartyManager().unloadParty(party);
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UNLOAD_PARTY,
								packet.getPartyId().toString()), true);
					}
					break;
				case UNLOAD_PLAYER:
					((PartiesPlugin) plugin).getPlayerManager().unloadPlayer(packet.getPlayerUuid());
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UNLOAD_PLAYER,
							packet.getPlayerUuid().toString()), true);
					break;
				case PLAY_SOUND:
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (partyPlayer != null) {
						((BukkitPartyPlayerImpl) partyPlayer).playPacketSound(packet.getPayloadRaw());
					}
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_PLAY_SOUND,
							packet.getPlayerUuid().toString()), true);
					break;
				case CREATE_PARTY:
					PartyImpl finalParty = ((PartiesPlugin) plugin).getPartyManager().loadParty(packet.getPartyId());
					if (finalParty != null) {
						((PartiesPlugin) plugin).getPlayerManager().reloadPlayer(packet.getPlayerUuid());
						PartyPlayerImpl leader = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
						
						// Calling API Event
						IPartyPostCreateEvent partiesPostCreateEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostCreateEvent(leader, finalParty);
						((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostCreateEvent);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_CREATE_PARTY,
								packet.getPartyId().toString(), leader != null ? leader.getPlayerUUID().toString() : "none"), true);
					}
					break;
				case DELETE_PARTY:
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					if (party != null) {
						try {
							ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
							DeleteCause cause = DeleteCause.valueOf(input.readUTF());
							PartyPlayerImpl kicked = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(input.readUTF()));
							String uuidCommandSender = input.readUTF();
							PartyPlayerImpl commandSender = uuidCommandSender.isEmpty() ? null : ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(uuidCommandSender));
							
							// Reload players
							party.getMembers().forEach(u -> {
								((PartiesPlugin) plugin).getPlayerManager().reloadPlayer(u);
							});
							
							// Unload party
							((PartiesPlugin) plugin).getPartyManager().removePartyFromCache(party);
							
							// Calling API Event
							IPartyPostDeleteEvent partiesPostDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostDeleteEvent(party, cause, kicked, commandSender);
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostDeleteEvent);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_DELETE_PARTY,
									party.getId().toString(), cause.name(), kicked != null ? kicked.getPlayerUUID().toString() : "none", commandSender != null ? commandSender.getPlayerUUID().toString() : "none"), true);
						} catch (Exception ex) {
							plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_DELETE_PARTY_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
						}
					}
					break;
				case RENAME_PARTY:
					((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId());
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					if (party != null) {
						try {
							ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
							String oldName = input.readUTF();
							String newName = input.readUTF();
							String uuidPlayer = input.readUTF();
							PartyPlayerImpl player = uuidPlayer.isEmpty() ? null : ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(uuidPlayer));
							boolean isAdmin = input.readBoolean();
							
							// Calling API Event
							IPartyPostRenameEvent partiesPostRenameEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostRenameEvent(party, oldName, newName, player, isAdmin);
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostRenameEvent);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_RENAME_PARTY,
									party.getId().toString(), oldName, newName, player != null ? player.getPlayerUUID().toString() : "none"), true);
						} catch (Exception ex) {
							plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_RENAME_PARTY_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
						}
					}
					break;
				case ADD_MEMBER_PARTY:
					((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId());
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					if (party != null) {
						try {
							ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
							PartyPlayerImpl player = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(input.readUTF()));
							JoinCause cause = JoinCause.valueOf(input.readUTF());
							String uuidInviter = input.readUTF();
							PartyPlayerImpl inviter = uuidInviter.isEmpty() ? null : ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(uuidInviter));
							
							// Calling API Event
							IPlayerPostJoinEvent partiesPostJoinEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostJoinEvent(player, party, cause, inviter);
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostJoinEvent);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_MEMBER_PARTY,
									player.getPlayerUUID().toString(), party.getId().toString(), cause.name(), inviter != null ? inviter.getPlayerUUID().toString() : "none"), true);
						} catch (Exception ex) {
							plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_MEMBER_PARTY_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
						}
					}
					break;
				case REMOVE_MEMBER_PARTY:
					((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId());
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					if (party != null) {
						try {
							ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
							PartyPlayerImpl player = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(input.readUTF()));
							LeaveCause cause = LeaveCause.valueOf(input.readUTF());
							String uuidInviter = input.readUTF();
							PartyPlayerImpl inviter = uuidInviter.isEmpty() ? null : ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(uuidInviter));
							
							// Calling API Event
							IPlayerPostLeaveEvent partiesPostLeaveEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostLeaveEvent(player, party, cause, inviter);
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostLeaveEvent);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_REMOVE_MEMBER_PARTY,
									player.getPlayerUUID().toString(), party.getId().toString(), cause.name(), inviter != null ? inviter.getPlayerUUID().toString() : "none"), true);
						} catch (Exception ex) {
							plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_REMOVE_MEMBER_PARTY_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
						}
					}
					break;
				case CHAT_MESSAGE:
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (party != null && partyPlayer != null) {
						// Calling API event
						IPlayerPostChatEvent partiesPostChatEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostChatEvent(partyPlayer, party, packet.getPayload());
						((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostChatEvent);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE,
								partyPlayer.getPlayerUUID().toString(), packet.getPartyId().toString(), packet.getPayload()), true);
					}
					break;
				case INVITE_PLAYER:
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					if (party != null) {
						try {
							ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
							PartyPlayerImpl invitedPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(input.readUTF()));
							String uuidInviter = input.readUTF();
							PartyPlayerImpl inviter = uuidInviter.isEmpty() ? null : ((PartiesPlugin) plugin).getPlayerManager().getPlayer(UUID.fromString(uuidInviter));
							
							// Calling API Event
							IPlayerPostInviteEvent partiesPostInviteEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostInviteEvent(invitedPlayer, inviter, party);
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostInviteEvent);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY,
									invitedPlayer.getPlayerUUID().toString(), party.getId().toString(), inviter != null ? inviter.getPlayerUUID().toString() : "none"), true);
						} catch (Exception ex) {
							plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
						}
					}
					break;
				case ADD_HOME:
					((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId());
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (party != null && partyPlayer != null) {
						try {
							ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
							String name = input.readUTF();
							String server = input.readUTF();
							
							PartyHomeImpl home = BukkitCommandSetHome.getHomeLocationOfPlayer(partyPlayer, name, server);
							if (home != null) {
								
								((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddHome(party, home.toString());
								
								plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_HOME,
										home, party.getId().toString(), partyPlayer.getPlayerUUID().toString(), server), true);
							}
						} catch (Exception ex) {
							plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_HOME_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
						}
					}
					break;
				case HOME_TELEPORT:
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (partyPlayer != null) {
						User user = plugin.getPlayer(packet.getPlayerUuid());
						
						if (user != null) {
							try {
								ByteArrayDataInput input = ByteStreams.newDataInput(packet.getPayloadRaw());
								String homeSerialized = input.readUTF();
								String message = input.readUTF();
								
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
									
									BukkitCommandHome.teleportToPartyHome((PartiesPlugin) plugin, partyPlayer, (BukkitUser) user, location, message);
									
									plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_HOME_TELEPORT,
											packet.getPlayerUuid().toString(), homeSerialized), true);
								}
							} catch (Exception ex) {
								plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_HOME_TELEPORT_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
							}
						}
					}
					break;
				case TELEPORT:
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (partyPlayer != null) {
						User user = plugin.getPlayer(packet.getPlayerUuid());
						
						if (user != null) {
							
							try {
								UUID targetUuid = UUID.fromString(packet.getPayload());
								
								Player bukkitTargetPlayer = Bukkit.getPlayer(targetUuid);
								if (bukkitTargetPlayer != null) {
									plugin.getScheduler().getSyncExecutor().execute(() -> ((BukkitUser) user).teleportAsync(bukkitTargetPlayer.getLocation()));
								}
								
								plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_TELEPORT,
										packet.getPlayerUuid().toString(), packet.getPayload()), true);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					break;
				case EXPERIENCE:
					if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
						((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId());
						party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
						partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
						if (party != null) {
							IPartyGetExperienceEvent partiesGetExperienceEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyGetExperienceEvent(party, packet.getPayloadNumber(), partyPlayer);
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesGetExperienceEvent);
							
						}
					}
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_EXPERIENCE,
							CommonUtils.formatDouble(packet.getPayloadNumber()), packet.getPartyId().toString(), packet.getPlayerUuid() != null ? packet.getPlayerUuid().toString() : "none"), true);
					break;
				case LEVEL_UP:
					if (ConfigMain.ADDITIONAL_EXP_ENABLE && ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
						((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyId());
						party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
						if (party != null) {
							IPartyLevelUpEvent partiesLevelUpEvent = ((PartiesPlugin) plugin).getEventManager().prepareLevelUpEvent(party, (int) packet.getPayloadNumber());
							((PartiesPlugin) plugin).getEventManager().callEvent(partiesLevelUpEvent);
							
						}
					}
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LEVEL_UP,
							packet.getPartyId().toString(), (int) packet.getPayloadNumber()), true);
					break;
				case CONFIGS:
					if (ConfigMain.PARTIES_BUNGEECORD_CONFIG_SYNC) {
						((PartiesConfigurationManager) plugin.getConfigurationManager()).parseConfigsPacket(packet.getPayloadRaw());
						
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_CONFIGS, true);
					}
					break;
				default:
					// Not supported packet type
			}
		} else {
			plugin.getLoggerManager().printError(PartiesConstants.DEBUG_MESSAGING_RECEIVED_WRONG);
		}
	}
}
