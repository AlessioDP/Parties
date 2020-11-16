package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

import java.util.UUID;

public class BukkitPartiesMessageDispatcher extends BukkitMessageDispatcher {
	public BukkitPartiesMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin, false);
	}
	
	private boolean isEnabled() {
		return ((PartiesPlugin) plugin).isBungeeCordEnabled();
	}
	
	public void sendPartyExperience(PartyImpl party, double experience, PartyPlayerImpl killer) {
		// Not duplication: this is used to alert BungeeCord that experience must be given
		sendPacket(makePacket(PartiesPacket.PacketType.EXPERIENCE)
				.setPartyId(party.getId())
				.setPlayerUuid(killer.getPlayerUUID())
				.setPayloadNumber(experience)
		);
	}
	
	public void sendConfigsRequest() {
		sendPacket(makePacket(PartiesPacket.PacketType.REQUEST_CONFIGS));
	}
	
	private PartiesPacket makePacket(PartiesPacket.PacketType type) {
		return new PartiesPacket(plugin.getVersion()).setType(type);
	}
	
	// WIP NEW
	
	public void sendChatMessage(String message, PartyImpl party) {
	
	}
	
	// WIP NEW
	public void sendPingUpdatePlayer(UUID player) {
		if (isEnabled()) {
			// Prepare packet update player
			/*
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PLAYER_UPDATED)
					.setPlayerUuid(player)
			);*/
		}
	}
	
	public void sendPingUpdateParty(UUID party) {
		if (isEnabled()) {
			// Prepare packet update party
			/*
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PARTY_UPDATED)
					.setPartyId(party)
			);*/
		}
	}
	
	public void sendPingRenameParty(UUID party, String newName) {
		if (isEnabled()) {
			// Prepare packet rename party
			/*
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PARTY_RENAMED)
					.setPartyId(party)
					.setPayload(newName)
			);*/
		}
	}
	
	public void sendPingRemoveParty(UUID party) {
		if (isEnabled()) {
			// Prepare packet remove party
			/*
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PARTY_REMOVED)
					.setPartyId(party)
			);*/
		}
	}
	
	public void sendPingChatMessage(UUID party, UUID sender, String message) {
		/*if (isEnabled() && BukkitConfigMain.PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT) {
			// Prepare packet broadcast message
			
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setPartyId(party)
					.setPlayerUuid(sender)
					.setPayload(message)
			);
		}*/
	}
	
	public void sendPingBroadcastMessage(UUID party, String message) {
		/*if (isEnabled() && BukkitConfigMain.PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS) {
			// Prepare packet broadcast message
			
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.BROADCAST_MESSAGE)
					.setPartyId(party)
					.setPayload(message)
			);
		}*/
	}
}
