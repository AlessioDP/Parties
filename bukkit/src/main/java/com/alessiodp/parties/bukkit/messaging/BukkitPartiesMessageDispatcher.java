package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.messaging.bungee.BukkitPartiesBungeecordDispatcher;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BukkitPartiesMessageDispatcher extends BukkitMessageDispatcher {
	public BukkitPartiesMessageDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, new BukkitPartiesBungeecordDispatcher(plugin));
	}
	
	private void sendPacketToBungeecord(@NotNull PartiesPacket packet) {
		bungeeDispatcher.sendPacket(packet.setSource(BukkitConfigMain.PARTIES_BUNGEECORD_SERVER_ID), MessageChannel.MAIN);
	}
	
	
	public void sendUpdateParty(@NotNull PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.UPDATE_PARTY)
					.setParty(party.getId()));
		}
	}
	
	public void sendUpdatePlayer(@NotNull PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.UPDATE_PLAYER)
					.setPlayer(partyPlayer.getPartyId()));
		}
	}
	
	public void sendCreateParty(@NotNull PartyImpl party, @Nullable PartyPlayerImpl leader) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.CREATE_PARTY)
					.setParty(party.getId())
					.setPlayer(leader != null ? leader.getPlayerUUID() : null));
		}
	}
	
	public void sendDeleteParty(@NotNull PartyImpl party, @NotNull DeleteCause cause, @Nullable PartyPlayerImpl kicked, @Nullable PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.DELETE_PARTY)
					.setParty(party.getId())
					.setCause(cause)
					.setPlayer(kicked != null ? kicked.getPlayerUUID() : null)
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null));
		}
	}
	
	public void sendRenameParty(@NotNull PartyImpl party, @Nullable String oldName, @Nullable String newName, @Nullable PartyPlayerImpl executor, boolean admin) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.RENAME_PARTY)
					.setParty(party.getId())
					.setText(oldName)
					.setSecondaryText(newName)
					.setPlayer(executor != null ? executor.getPlayerUUID() : null)
					.setBool(admin));
		}
	}
	
	public void sendAddMemberParty(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull JoinCause cause, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.ADD_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null));
		}
	}
	
	public void sendRemoveMemberParty(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull LeaveCause cause, @Nullable PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.REMOVE_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null));
		}
	}
	
	public void sendChatMessage(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @NotNull String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CHAT) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(message));
		}
	}
	
	public void sendBroadcastMessage(@NotNull PartyImpl party, @Nullable PartyPlayerImpl player, @NotNull String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_BROADCAST) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.BROADCAST_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player != null ? player.getPlayerUUID() : null)
					.setText(message));
		}
	}
	
	public void sendInvitePlayer(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.INVITE_PLAYER)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null));
		}
	}
	
	public void sendAddHome(@NotNull PartyImpl party, @NotNull String home) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.ADD_HOME)
					.setParty(party.getId())
					.setText(home));
		}
	}
	
	public void sendPartyExperience(@NotNull PartyImpl party, double experience, @Nullable PartyPlayerImpl killer, boolean gainMessage) {
		// Not duplication: this is used to alert BungeeCord that experience must be given
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.EXPERIENCE)
					.setParty(party.getId())
					.setPlayer(killer != null ? killer.getPlayerUUID() : null)
					.setNumber(experience)
					.setBool(gainMessage));
		}
	}
	
	public void sendConfigsRequest() {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.REQUEST_CONFIGS));
		}
	}
	
	@Contract("_, null, true -> fail")
	public void sendDebugBungeeCord(@NotNull UUID temporaryUuid, @Nullable UUID receiver, boolean replyToPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_DEBUG_BUNGEECORD) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.DEBUG_BUNGEECORD)
					.setPlayer(temporaryUuid)
					.setSecondaryPlayer(receiver)
					.setBool(replyToPlayer));
		}
	}
	
	private PartiesPacket makePacket(@NotNull PartiesPacket.PacketType type) {
		return (PartiesPacket) new PartiesPacket()
				.setVersion(plugin.getVersion())
				.setType(type);
	}
}
