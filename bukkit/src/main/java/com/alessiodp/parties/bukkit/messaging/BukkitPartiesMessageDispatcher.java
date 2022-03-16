package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.bukkit.messaging.bungee.BukkitPartiesBungeecordDispatcher;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BukkitPartiesMessageDispatcher extends BukkitMessageDispatcher {
	public BukkitPartiesMessageDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, new BukkitPartiesBungeecordDispatcher(plugin));
	}
	
	private void sendPacketToBungeecord(PartiesPacket packet) {
		bungeeDispatcher.sendPacket(packet, MessageChannel.MAIN);
	}
	
	
	public void sendUpdateParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.UPDATE_PARTY)
					.setParty(party.getId()));
		}
	}
	
	public void sendUpdatePlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.UPDATE_PLAYER)
					.setPlayer(partyPlayer.getPartyId()));
		}
	}
	
	public void sendCreateParty(PartyImpl party, PartyPlayerImpl leader) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.CREATE_PARTY)
					.setParty(party.getId())
					.setPlayer(leader != null ? leader.getPlayerUUID() : null));
		}
	}
	
	public void sendDeleteParty(PartyImpl party, DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.DELETE_PARTY)
					.setParty(party.getId())
					.setCause(cause)
					.setPlayer(kicked.getPlayerUUID())
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null));
		}
	}
	
	public void sendRenameParty(PartyImpl party, String oldName, String newName, @Nullable PartyPlayerImpl executor, boolean admin) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.RENAME_PARTY)
					.setParty(party.getId())
					.setText(oldName)
					.setSecondaryText(newName)
					.setPlayer(executor != null ? executor.getPlayerUUID() : null)
					.setBool(admin));
		}
	}
	
	public void sendAddMemberParty(PartyImpl party, PartyPlayerImpl player, JoinCause cause, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.ADD_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null));
		}
	}
	
	public void sendRemoveMemberParty(PartyImpl party, PartyPlayerImpl player, LeaveCause cause, @Nullable PartyPlayerImpl executor) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.REMOVE_MEMBER_PARTY)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setCause(cause)
					.setSecondaryPlayer(executor != null ? executor.getPlayerUUID() : null));
		}
	}
	
	public void sendChatMessage(PartyImpl party, PartyPlayerImpl player, String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CHAT) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(message));
		}
	}
	
	public void sendBroadcastMessage(PartyImpl party, PartyPlayerImpl player, String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_BROADCAST) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.BROADCAST_MESSAGE)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setText(message));
		}
	}
	
	public void sendInvitePlayer(PartyImpl party, PartyPlayerImpl player, @Nullable PartyPlayerImpl inviter) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.INVITE_PLAYER)
					.setParty(party.getId())
					.setPlayer(player.getPlayerUUID())
					.setSecondaryPlayer(inviter != null ? inviter.getPlayerUUID() : null));
		}
	}
	
	public void sendAddHome(PartyImpl party, String home) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.ADD_HOME)
					.setParty(party.getId())
					.setText(home));
		}
	}
	
	public void sendPartyExperience(PartyImpl party, double experience, PartyPlayerImpl killer, boolean gainMessage) {
		// Not duplication: this is used to alert BungeeCord that experience must be given
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.EXPERIENCE)
					.setParty(party.getId())
					.setPlayer(killer.getPlayerUUID())
					.setNumber(experience)
					.setBool(gainMessage));
		}
	}
	
	public void sendConfigsRequest() {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.REQUEST_CONFIGS));
		}
	}
	
	public void sendDebugBungeeCord(UUID temporaryUuid, UUID receiver, boolean replyToPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_DEBUG_BUNGEECORD) {
			sendPacketToBungeecord(makePacket(PartiesPacket.PacketType.DEBUG_BUNGEECORD)
					.setPlayer(temporaryUuid)
					.setSecondaryPlayer(receiver)
					.setBool(replyToPlayer));
		}
	}
	
	private PartiesPacket makePacket(PartiesPacket.PacketType type) {
		return (PartiesPacket) new PartiesPacket()
				.setVersion(plugin.getVersion())
				.setType(type);
	}
}
