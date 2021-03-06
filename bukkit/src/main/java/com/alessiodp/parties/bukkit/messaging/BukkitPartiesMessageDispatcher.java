package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

public class BukkitPartiesMessageDispatcher extends BukkitMessageDispatcher {
	public BukkitPartiesMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin, true, false, false);
	}
	
	public void sendUpdateParty(PartyImpl party) {
		sendPacket(makePacket(PartiesPacket.PacketType.UPDATE_PARTY)
				.setPartyId(party.getId())
		, getMainChannel());
	}
	
	public void sendUpdatePlayer(PartyPlayerImpl partyPlayer) {
		sendPacket(makePacket(PartiesPacket.PacketType.UPDATE_PLAYER)
				.setPlayerUuid(partyPlayer.getPartyId())
		, getMainChannel());
	}
	
	public void sendCreateParty(PartyImpl party, PartyPlayerImpl leader) {
		sendPacket(makePacket(PartiesPacket.PacketType.CREATE_PARTY)
				.setPartyId(party.getId())
				.setPlayerUuid(leader.getPlayerUUID())
		, getMainChannel());
	}
	
	public void sendDeleteParty(PartyImpl party, byte[] raw) {
		sendPacket(makePacket(PartiesPacket.PacketType.DELETE_PARTY)
				.setPartyId(party.getId())
				.setPayloadRaw(raw)
		, getMainChannel());
	}
	
	public void sendRenameParty(PartyImpl party, byte[] raw) {
		sendPacket(makePacket(PartiesPacket.PacketType.RENAME_PARTY)
				.setPartyId(party.getId())
				.setPayloadRaw(raw)
		, getMainChannel());
	}
	
	public void sendAddMemberParty(PartyImpl party, byte[] raw) {
		sendPacket(makePacket(PartiesPacket.PacketType.ADD_MEMBER_PARTY)
				.setPartyId(party.getId())
				.setPayloadRaw(raw)
		, getMainChannel());
	}
	
	public void sendRemoveMemberParty(PartyImpl party, byte[] raw) {
		sendPacket(makePacket(PartiesPacket.PacketType.REMOVE_MEMBER_PARTY)
				.setPartyId(party.getId())
				.setPayloadRaw(raw)
		, getMainChannel());
	}
	
	public void sendBroadcastMessage(PartyImpl party, PartyPlayerImpl sender, String message) {
		sendPacket(makePacket(PartiesPacket.PacketType.BROADCAST_MESSAGE)
				.setPartyId(party.getId())
				.setPlayerUuid(sender != null ? sender.getPlayerUUID() : null)
				.setPayload(message)
		, getMainChannel());
	}
	
	public void sendInvitePlayer(PartyImpl party, byte[] raw) {
		sendPacket(makePacket(PartiesPacket.PacketType.INVITE_PLAYER)
				.setPartyId(party.getId())
				.setPayloadRaw(raw)
		, getMainChannel());
	}
	
	public void sendAddHome(PartyImpl party, String home) {
		sendPacket(makePacket(PartiesPacket.PacketType.ADD_HOME)
				.setPartyId(party.getId())
				.setPayload(home)
		, getMainChannel());
	}
	
	public void sendPartyExperience(PartyImpl party, double experience, PartyPlayerImpl killer) {
		// Not duplication: this is used to alert BungeeCord that experience must be given
		sendPacket(makePacket(PartiesPacket.PacketType.EXPERIENCE)
				.setPartyId(party.getId())
				.setPlayerUuid(killer.getPlayerUUID())
				.setPayloadNumber(experience)
		, getMainChannel());
	}
	
	public void sendConfigsRequest() {
		sendPacket(makePacket(PartiesPacket.PacketType.REQUEST_CONFIGS), getMainChannel());
	}
	
	private PartiesPacket makePacket(PartiesPacket.PacketType type) {
		return new PartiesPacket(plugin.getVersion()).setType(type);
	}
}
