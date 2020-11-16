package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

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
				case LOAD_PARTY:
					if (((PartiesPlugin) plugin).getPartyManager().loadParty(packet.getPartyId()) != null) {
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LOAD_PARTY,
								packet.getPartyId().toString()), true);
					}
					break;
				case LOAD_PLAYER:
					if (((PartiesPlugin) plugin).getPlayerManager().loadPlayer(packet.getPlayerUuid()) != null) {
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
				case RENAME_PARTY:
					// wip rename event + reload party
					break;
				case PLAY_SOUND:
					partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
					if (partyPlayer != null) {
						((BukkitPartyPlayerImpl) partyPlayer).playPacketSound(packet.getPayloadRaw());
					}
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_PLAY_SOUND,
							packet.getPlayerUuid().toString()), true);
					break;
				case EXPERIENCE:
					if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
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
