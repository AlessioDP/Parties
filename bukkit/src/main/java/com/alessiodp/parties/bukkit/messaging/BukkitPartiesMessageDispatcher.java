package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import lombok.NonNull;

import java.util.UUID;

public class BukkitPartiesMessageDispatcher extends BukkitMessageDispatcher {
	public BukkitPartiesMessageDispatcher(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	private boolean isEnabled() {
		return ((PartiesPlugin) plugin).isBungeeCordEnabled();
	}
	
	public void sendPingUpdatePlayer(UUID player) {
		if (isEnabled()) {
			// Prepare packet update player
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PLAYER_UPDATED)
					.setPlayerUuid(player)
			);
		}
	}
	
	public void sendPingUpdateParty(UUID party) {
		if (isEnabled()) {
			// Prepare packet update party
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PARTY_UPDATED)
					.setPartyId(party)
			);
		}
	}
	
	public void sendPingRenameParty(UUID party, String newName) {
		if (isEnabled()) {
			// Prepare packet rename party
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PARTY_RENAMED)
					.setPartyId(party)
					.setPayload(newName)
			);
		}
	}
	
	public void sendPingRemoveParty(UUID party) {
		if (isEnabled()) {
			// Prepare packet remove party
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.PARTY_REMOVED)
					.setPartyId(party)
			);
		}
	}
	
	public void sendPingChatMessage(UUID party, UUID sender, String message) {
		if (isEnabled() && BukkitConfigMain.PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT) {
			// Prepare packet broadcast message
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.CHAT_MESSAGE)
					.setPartyId(party)
					.setPlayerUuid(sender)
					.setPayload(message)
			);
		}
	}
	
	public void sendPingBroadcastMessage(UUID party, String message) {
		if (isEnabled() && BukkitConfigMain.PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS) {
			// Prepare packet broadcast message
			sendForwardPacket(new PartiesPacket(plugin.getVersion())
					.setType(PartiesPacket.PacketType.BROADCAST_MESSAGE)
					.setPartyId(party)
					.setPayload(message)
			);
		}
	}
}
