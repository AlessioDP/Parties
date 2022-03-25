package com.alessiodp.parties.bungeecord.messaging;

import com.alessiodp.core.bungeecord.addons.external.RedisBungeeHandler;
import com.alessiodp.core.bungeecord.messaging.BungeeMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.messaging.bungee.BungeePartiesBungeecordDispatcher;
import com.alessiodp.parties.bungeecord.messaging.redis.PartiesRedisBungeeDispatcher;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BungeePartiesMessageDispatcher extends BungeeMessageDispatcher {
	public BungeePartiesMessageDispatcher(@NotNull ADPPlugin plugin) {
		super(
				plugin,
				new BungeePartiesBungeecordDispatcher(plugin),
				new PartiesRedisBungeeDispatcher(plugin)
		);
	}
	
	private void sendPacketToBungeecord(PartiesPacket packet) {
		bungeeDispatcher.sendPacket(packet, MessageChannel.SUB);
	}
	
	private void sendPacketToBungeecordUser(PartiesPacket packet, User user) {
		bungeeDispatcher.sendPacketToUser(packet, user, MessageChannel.SUB);
	}
	
	private void sendPacketToRedis(PartiesPacket packet) {
		if (redisBungeeDispatcher.isRegistered())
			redisBungeeDispatcher.sendPacket(packet.setSource(RedisBungeeHandler.getCurrentServer()));
	}
	
	public void sendUpdateParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UPDATE_PARTY)
					.setParty(party.getId());
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendUpdatePlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UPDATE_PLAYER)
					.setPlayer(partyPlayer.getPlayerUUID());
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendLoadParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.LOAD_PARTY)
					.setParty(party.getId());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendLoadPlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.LOAD_PLAYER)
					.setPlayer(partyPlayer.getPlayerUUID());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendUnloadParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UNLOAD_PARTY)
					.setParty(party.getId());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendUnloadPlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UNLOAD_PLAYER)
					.setPlayer(partyPlayer.getPlayerUUID());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendPlaySound(User user, String sound, double volume, double pitch) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_SOUNDS) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.PLAY_SOUND)
					.setPlayer(user.getUUID())
					.setText(sound)
					.setNumber(volume)
					.setSecondaryNumber(pitch);
			sendPacketToBungeecordUser(packet, user);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendCreateParty(PartyImpl party, PartyPlayerImpl leader) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.CREATE_PARTY)
					.setParty(party.getId())
					.setPlayer(leader.getPlayerUUID());
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendDeleteParty(PartyImpl party, DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.DELETE_PARTY)
					.setParty(party.getId())
					.setCause(cause)
					.setPlayer(kicked.getPlayerUUID())
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendRenameParty(PartyImpl party, String oldName, String newName, @Nullable PartyPlayerImpl executor, boolean admin) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.RENAME_PARTY)
					.setParty(party.getId())
					.setText(oldName)
					.setSecondaryText(newName)
					.setPlayer(executor != null ? executor.getPlayerUUID() : null)
					.setBool(admin);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendAddMemberParty(PartyImpl party, PartyPlayerImpl player, JoinCause cause, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.ADD_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendRemoveMemberParty(PartyImpl party, PartyPlayerImpl player, LeaveCause cause, @Nullable PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.REMOVE_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendChatMessage(PartyImpl party, PartyPlayerImpl player, String formattedMessage, String message, boolean dispatchRedis) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CHAT) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(formattedMessage)
					.setSecondaryText(message);
			sendPacketToBungeecord(packet);
			if (dispatchRedis)
				sendPacketToRedis(packet);
		}
	}
	
	public void sendBroadcastMessage(PartyImpl party, PartyPlayerImpl player, String message, boolean dispatchRedis) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_BROADCAST) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.BROADCAST_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(message);
			sendPacketToBungeecord(packet);
			if (dispatchRedis)
				sendPacketToRedis(packet);
		}
	}
	
	public void sendInvitePlayer(PartyImpl party, PartyPlayerImpl player, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.INVITE_PLAYER)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null)
					.setNumber(ConfigParties.GENERAL_INVITE_TIMEOUT);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendAddHome(User user, PartyImpl party, String name, String server) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			// The home is set by Bukkit, send name + server name.
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.ADD_HOME)
					.setParty(party.getId())
					.setPlayer(user.getUUID())
					.setText(name)
					.setSecondaryText(server);
			sendPacketToBungeecordUser(packet, user);
		}
	}
	
	public void sendHomeTeleport(User user, PartyHomeImpl home, String message, ServerInfo targetServer) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.HOME_TELEPORT)
				.setPlayer(user.getUUID())
				.setText(home.toString())
				.setSecondaryText(message);
		
		if (((BungeePartiesBootstrap) plugin.getBootstrap()).getServersList().contains(targetServer.getName())) {
			sendPacketToBungeecordUser(packet, user);
		} else {
			// If the server is not in this network
			sendPacketToRedis(packet);
		}
	}
	
	public void sendTeleport(User user, PartyPlayerImpl target, ServerInfo targetServer) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.TELEPORT)
				.setPlayer(user.getUUID())
				.setSecondaryPlayer(target.getPlayerUUID());
		
		if (((BungeePartiesBootstrap) plugin.getBootstrap()).getServersList().contains(targetServer.getName())) {
			sendPacketToBungeecordUser(packet, user);
		} else {
			// If the server is not in this network
			sendPacketToRedis(packet);
		}
	}
	
	public void sendPartyExperience(PartyImpl party, PartyPlayerImpl killer, double experience, boolean gainMessage) {
		// Not duplication: this is used to make an event in bukkit servers
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.EXPERIENCE)
					.setParty(party.getId())
					.setPlayer(killer.getPlayerUUID())
					.setNumber(experience)
					.setBool(gainMessage);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendLevelUp(PartyImpl party, int newLevel) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.LEVEL_UP)
					.setParty(party.getId())
					.setNumber(newLevel);
			sendPacketToBungeecord(packet);
			sendPacketToRedis(packet);
		}
	}
	
	public void sendConfigs() {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CONFIG_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.CONFIGS);
			packet.setConfigData(packet.makePacketConfigData());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendDebugBungeecordReply(User receiver, boolean result, boolean replyToPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_DEBUG_BUNGEECORD) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.DEBUG_BUNGEECORD)
					.setPlayer(replyToPlayer ? receiver.getUUID() : null)
					.setBool(result);
			sendPacketToBungeecordUser(packet, receiver);
		}
	}
	
	public void sendRedisMessage(User receiver, String message, boolean colorTranslation) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.REDIS_MESSAGE)
				.setPlayer(receiver.getUUID())
				.setText(message)
				.setBool(colorTranslation);
		sendPacketToRedis(packet);
	}
	
	public void sendRedisTitle(User receiver, String message, int fadeInTime, int showTime, int fadeOutTime) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.REDIS_TITLE)
				.setPlayer(receiver.getUUID())
				.setText(message)
				.setSecondaryText(String.format("%d:%d:%d", fadeInTime, showTime, fadeOutTime));
		sendPacketToRedis(packet);
	}
	
	public void sendRedisChat(User receiver, String message) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.REDIS_CHAT)
				.setPlayer(receiver.getUUID())
				.setText(message);
		sendPacketToRedis(packet);
	}
	
	private PartiesPacket makePacket(PartiesPacket.PacketType type) {
		return (PartiesPacket) new PartiesPacket()
				.setVersion(plugin.getVersion())
				.setType(type);
	}
}
