package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

import java.util.UUID;

public class BukkitPartiesMessageListener extends BukkitMessageListener {
	
	public BukkitPartiesMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	
	@Override
	public void handlePacket(byte[] bytes) {
		PartiesPacket packet = PartiesPacket.read(plugin, bytes);
		if (packet != null) {
			PartyImpl party;
			PartyPlayerImpl sender;
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_RECEIVED
					.replace("{type}", packet.getType().name()), true);
			switch (packet.getType()) {
				case PLAYER_UPDATED:
					if (((PartiesPlugin) plugin).getPlayerManager().reloadPlayer(packet.getPlayerUuid())) {
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_PLAYER_UPDATED
								.replace("{uuid}", packet.getPlayerUuid().toString()), true);
					}
					break;
				case PARTY_UPDATED:
					if (((PartiesPlugin) plugin).getPartyManager().reloadParty(packet.getPartyName())) {
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_PARTY_UPDATED
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case PARTY_RENAMED:
					// Payload is the old party name
					party = ((PartiesPlugin) plugin).getPartyManager().getListParties().get(packet.getPayload());
					if (party != null) {
						// Packet getPartyName is the new name
						party.rename(packet.getPartyName());
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_PARTY_RENAMED
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case PARTY_REMOVED:
					party = ((PartiesPlugin) plugin).getPartyManager().getListParties().get(packet.getPartyName());
					if (party != null) {
						((PartiesPlugin) plugin).getPartyManager().getListParties().remove(packet.getPartyName());
						for (UUID uuid : party.getMembers()) {
							PartyPlayerImpl pl = ((PartiesPlugin) plugin).getPlayerManager().getListPartyPlayers().get(uuid);
							if (pl != null) {
								pl.removeFromParty(false);
							}
						}
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_PARTY_REMOVED
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case CHAT_MESSAGE:
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyName());
					sender = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (party != null
							&& party.getOnlineMembers(true).size() > 0
							&& sender != null) {
						party.dispatchChatMessage(sender, packet.getPayload(), false);
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE
								.replace("{party}", packet.getPartyName()), true);
					}
					break;
				case BROADCAST_MESSAGE:
					party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyName());
					if (party != null && party.getOnlineMembers(true).size() > 0) {
						party.broadcastDirectMessage(packet.getPayload(), false);
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_BROADCAST_MESSAGE
								.replace("{party}", packet.getPartyName()), true);
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
