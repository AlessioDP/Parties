package com.alessiodp.parties.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.configuration.BungeePartiesConfigurationManager;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

public class BungeePartiesMessageListener extends BungeeMessageListener {
	
	public BungeePartiesMessageListener(@NonNull ADPPlugin plugin) {
		super(plugin, false);
	}
	
	@Override
	protected void handlePacket(byte[] bytes) {
		PartiesPacket packet = PartiesPacket.read(plugin, bytes);
		if (packet != null) {
			PartyImpl party;
			PartyPlayerImpl partyPlayer;
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_RECEIVED, packet.getType().name()), true);
			switch (packet.getType()) {
				case EXPERIENCE:
					if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
						party = ((PartiesPlugin) plugin).getPartyManager().getParty(packet.getPartyId());
						partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(packet.getPlayerUuid());
						if (party != null) {
							party.giveExperience(packet.getPayloadNumber(), partyPlayer);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_EXPERIENCE,
									CommonUtils.formatDouble(packet.getPayloadNumber()), packet.getPartyId().toString(), packet.getPlayerUuid() != null ? packet.getPlayerUuid().toString() : "none"), true);
						}
					}
					break;
				case REQUEST_CONFIGS:
					if (ConfigMain.PARTIES_BUNGEECORD_CONFIG_SYNC) {
						((BungeePartiesConfigurationManager) plugin.getConfigurationManager()).makeConfigsSync();
						
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_REQUEST_CONFIGS, true);
					}
					break;
				default:
					// Nothing to do
					break;
			}
		}
	}
}
