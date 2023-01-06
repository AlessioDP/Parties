package com.alessiodp.parties.velocity.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.velocity.addons.external.VelocityRedisBungeeHandler;
import com.alessiodp.core.velocity.messaging.VelocityMessageDispatcher;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.messaging.bungee.VelocityPartiesBungeecordDispatcher;
import com.alessiodp.parties.velocity.messaging.redis.VelocityPartiesRedisBungeeDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class VelocityPartiesMessageDispatcher extends VelocityMessageDispatcher {
	public VelocityPartiesMessageDispatcher(@NotNull ADPPlugin plugin) {
		super(
				plugin,
				new VelocityPartiesBungeecordDispatcher(plugin),
				new VelocityPartiesRedisBungeeDispatcher(plugin)
		);
	}
	
	private void sendPacketToBungeecord(@NotNull PartiesPacket packet) {
		bungeeDispatcher.sendPacket(packet, MessageChannel.SUB);
	}
	
	private void sendPacketToBungeecordUser(@NotNull PartiesPacket packet, @NotNull User user) {
		bungeeDispatcher.sendPacketToUser(packet, user, MessageChannel.SUB);
	}
	
	private void sendPacketToRedis(@NotNull PartiesPacket packet) {
		if (redisBungeeDispatcher.isRegistered())
			redisBungeeDispatcher.sendPacket(packet.setSource(VelocityRedisBungeeHandler.getProxyId()));
	}
	
	public void sendUpdateParty(@NotNull PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UPDATE_PARTY)
					.setParty(party.getId());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendUpdatePlayer(@NotNull PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UPDATE_PLAYER)
					.setPlayer(partyPlayer.getPartyId());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendLoadParty(@NotNull PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.LOAD_PARTY)
					.setParty(party.getId());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendLoadPlayer(@NotNull PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.LOAD_PLAYER)
					.setPlayer(partyPlayer.getPlayerUUID());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendUnloadParty(@NotNull PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UNLOAD_PARTY)
					.setParty(party.getId());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendUnloadPlayer(@NotNull PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.UNLOAD_PLAYER)
					.setPlayer(partyPlayer.getPlayerUUID());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendPlaySound(@NotNull User user, @NotNull String sound, double volume, double pitch) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_SOUNDS) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.PLAY_SOUND)
					.setPlayer(user.getUUID())
					.setText(sound)
					.setNumber(volume)
					.setSecondaryNumber(pitch);
			sendPacketToBungeecordUser(packet, user);
		}
	}
	
	public void sendCreateParty(@NotNull PartyImpl party, @Nullable PartyPlayerImpl leader) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.CREATE_PARTY)
					.setParty(party.getId())
					.setPlayer(leader != null ? leader.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendDeleteParty(@NotNull PartyImpl party, @NotNull DeleteCause cause, @Nullable PartyPlayerImpl kicked, @Nullable PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.DELETE_PARTY)
					.setParty(party.getId())
					.setCause(cause)
					.setPlayer(kicked != null ? kicked.getPlayerUUID(): null)
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendRenameParty(@NotNull PartyImpl party, @Nullable String oldName, @Nullable String newName, @Nullable PartyPlayerImpl executor, boolean admin) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.RENAME_PARTY)
					.setParty(party.getId())
					.setText(oldName)
					.setSecondaryText(newName)
					.setPlayer(executor != null ? executor.getPlayerUUID() : null)
					.setBool(admin);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendAddMemberParty(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull JoinCause cause, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.ADD_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendRemoveMemberParty(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull LeaveCause cause, @Nullable PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.REMOVE_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendChatMessage(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull String formattedMessage, @NotNull String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CHAT) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(formattedMessage)
					.setSecondaryText(message);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendBroadcastMessage(@NotNull PartyImpl party, @Nullable PartyPlayerImpl player, @NotNull String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_BROADCAST) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.BROADCAST_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player != null ? player.getPlayerUUID() : null)
					.setText(message);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendInvitePlayer(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.INVITE_PLAYER)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendAddHome(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull String name, @NotNull String server) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			// The home is set by Bukkit, send name + server name.
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.ADD_HOME)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(name)
					.setSecondaryText(server);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendHomeTeleport(@NotNull User user, @NotNull PartyHomeImpl home, @NotNull String message) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.HOME_TELEPORT)
				.setPlayer(user.getUUID())
				.setText(home.toString())
				.setSecondaryText(message);
		sendPacketToBungeecordUser(packet, user);
	}
	
	public void sendTeleport(@NotNull User user, @NotNull PartyPlayerImpl target) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.TELEPORT)
				.setPlayer(user.getUUID())
				.setSecondaryPlayer(target.getPlayerUUID());
		sendPacketToBungeecordUser(packet, user);
	}
	
	public void sendPartyExperience(@NotNull PartyImpl party, @Nullable PartyPlayerImpl killer, double experience, boolean gainMessage) {
		// Not duplication: this is used to make an event in bukkit servers
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.EXPERIENCE)
					.setParty(party.getId())
					.setPlayer(killer != null ? killer.getPlayerUUID() : null)
					.setNumber(experience)
					.setBool(gainMessage);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendLevelUp(@NotNull PartyImpl party, int newLevel) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.LEVEL_UP)
					.setParty(party.getId())
					.setNumber(newLevel);
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendConfigs() {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CONFIG_SYNC) {
			PartiesPacket packet = makePacket(PartiesPacket.PacketType.CONFIGS)
					.setConfigData(((PartiesConfigurationManager) plugin.getConfigurationManager()).makePacketConfigData());
			sendPacketToBungeecord(packet);
		}
	}
	
	public void sendRedisMessage(@NotNull User receiver, @NotNull String message, boolean colorTranslation) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.REDIS_MESSAGE)
				.setPlayer(receiver.getUUID())
				.setText(message)
				.setBool(colorTranslation);
		sendPacketToRedis(packet);
	}
	
	public void sendRedisTitle(@NotNull User receiver, @NotNull String message, int fadeInTime, int showTime, int fadeOutTime) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.REDIS_TITLE)
				.setPlayer(receiver.getUUID())
				.setText(message)
				.setSecondaryText(String.format("%d:%d:%d", fadeInTime, showTime, fadeOutTime));
		sendPacketToRedis(packet);
	}
	
	public void sendRedisChat(@NotNull User receiver, @NotNull String message) {
		PartiesPacket packet = makePacket(PartiesPacket.PacketType.REDIS_CHAT)
				.setPlayer(receiver.getUUID())
				.setText(message);
		sendPacketToRedis(packet);
	}
	
	private PartiesPacket makePacket(@NotNull PartiesPacket.PacketType type) {
		return (PartiesPacket) new PartiesPacket()
				.setVersion(plugin.getVersion())
				.setType(type);
	}
}
