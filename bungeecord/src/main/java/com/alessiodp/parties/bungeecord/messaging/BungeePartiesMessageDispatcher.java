package com.alessiodp.parties.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;


public class BungeePartiesMessageDispatcher extends BungeeMessageDispatcher {
	public BungeePartiesMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
	
	public void sendUpdateParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.UPDATE_PARTY)
					.setPartyId(party.getId())
			, getSubChannel());
		}
	}
	
	public void sendUpdatePlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.UPDATE_PLAYER)
					.setPlayerUuid(partyPlayer.getPartyId())
			, getSubChannel());
		}
	}
	
	public void sendLoadParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES) {
			sendPacket(makePacket(PartiesPacket.PacketType.LOAD_PARTY)
							.setPartyId(party.getId())
					, getSubChannel());
		}
	}
	
	public void sendLoadPlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS) {
			sendPacket(makePacket(PartiesPacket.PacketType.LOAD_PLAYER)
							.setPlayerUuid(partyPlayer.getPlayerUUID())
					, getSubChannel());
		}
	}
	
	public void sendUnloadParty(PartyImpl party) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES) {
			sendPacket(makePacket(PartiesPacket.PacketType.UNLOAD_PARTY)
							.setPartyId(party.getId())
					, getSubChannel());
		}
	}
	
	public void sendUnloadPlayer(PartyPlayerImpl partyPlayer) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS) {
			sendPacket(makePacket(PartiesPacket.PacketType.UNLOAD_PLAYER)
							.setPlayerUuid(partyPlayer.getPlayerUUID())
					, getSubChannel());
		}
	}
	
	public void sendPlaySound(User user, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_SOUNDS) {
			sendPacketToUser(makePacket(PartiesPacket.PacketType.PLAY_SOUND)
							.setPlayerUuid(user.getUUID())
							.setPayloadRaw(raw)
					, user
			, getSubChannel());
		}
	}
	
	public void sendCreateParty(PartyImpl party, PartyPlayerImpl leader) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.CREATE_PARTY)
					.setPartyId(party.getId())
					.setPlayerUuid(leader.getPlayerUUID())
			, getSubChannel());
		}
	}
	
	public void sendDeleteParty(PartyImpl party, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.DELETE_PARTY)
					.setPartyId(party.getId())
					.setPayloadRaw(raw)
			, getSubChannel());
		}
	}
	
	public void sendRenameParty(PartyImpl party, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.RENAME_PARTY)
					.setPartyId(party.getId())
					.setPayloadRaw(raw)
			, getSubChannel());
		}
	}
	
	public void sendAddMemberParty(PartyImpl party, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.ADD_MEMBER_PARTY)
					.setPartyId(party.getId())
					.setPayloadRaw(raw)
			, getSubChannel());
		}
	}
	
	public void sendRemoveMemberParty(PartyImpl party, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.REMOVE_MEMBER_PARTY)
					.setPartyId(party.getId())
					.setPayloadRaw(raw)
			, getSubChannel());
		}
	}
	
	public void sendChatMessage(PartyImpl party, PartyPlayerImpl player, String message) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CHAT) {
			sendPacket(makePacket(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setPartyId(party.getId())
					.setPlayerUuid(player.getPlayerUUID())
					.setPayload(message)
			, getSubChannel());
		}
	}
	
	public void sendInvitePlayer(PartyImpl party, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.INVITE_PLAYER)
					.setPartyId(party.getId())
					.setPayloadRaw(raw)
			, getSubChannel());
		}
	}
	
	public void sendAddHome(PartyImpl party, PartyPlayerImpl player, byte[] raw) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.ADD_HOME)
					.setPartyId(party.getId())
					.setPlayerUuid(player.getPlayerUUID())
					.setPayloadRaw(raw)
			, getSubChannel());
		}
	}
	
	public void sendHomeTeleport(User user, byte[] raw) {
		sendPacketToUser(makePacket(PartiesPacket.PacketType.HOME_TELEPORT)
						.setPlayerUuid(user.getUUID())
						.setPayloadRaw(raw)
				, user
		, getSubChannel());
	}
	
	public void sendTeleport(User user, PartyPlayerImpl target) {
		sendPacketToUser(makePacket(PartiesPacket.PacketType.TELEPORT)
				.setPlayerUuid(user.getUUID())
				.setPayload(target.getPlayerUUID().toString())
		, user
		, getSubChannel());
	}
	
	public void sendPartyExperience(PartyImpl party, PartyPlayerImpl killer, double experience) {
		// Not duplication: this is used to make an event in bukkit servers
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.EXPERIENCE)
					.setPartyId(party.getId())
					.setPlayerUuid(killer.getPlayerUUID())
					.setPayloadNumber(experience)
			, getSubChannel());
		}
	}
	
	public void sendLevelUp(PartyImpl party, int newLevel) {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.LEVEL_UP)
					.setPartyId(party.getId())
					.setPayloadNumber(newLevel)
			, getSubChannel());
		}
	}
	
	public void sendConfigs() {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CONFIG_SYNC) {
			sendPacket(makePacket(PartiesPacket.PacketType.CONFIGS)
					.setPayloadRaw(((PartiesConfigurationManager) plugin.getConfigurationManager()).makeConfigsPacket())
			, getSubChannel());
		}
	}
	
	private PartiesPacket makePacket(PartiesPacket.PacketType type) {
		return new PartiesPacket(plugin.getVersion()).setType(type);
	}
}
