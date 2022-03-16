package com.alessiodp.parties.velocity.messaging.bungee;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.MessageChannel;
import com.alessiodp.core.velocity.messaging.bungee.VelocityBungeecordListener;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.messaging.CommonListener;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.velocity.configuration.VelocityPartiesConfigurationManager;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesBungeecordListener extends VelocityBungeecordListener {
	private final CommonListener commonListener;
	
	public VelocityPartiesBungeecordListener(@NotNull ADPPlugin plugin) {
		super(plugin, true, false);
		commonListener = new CommonListener((PartiesPlugin) plugin);
	}
	
	@Override
	protected void handlePacket(byte[] bytes, @NotNull MessageChannel messageChannel) {
		if (messageChannel != MessageChannel.MAIN)
			return; // Handle only packets for main channel
		
		PartiesPacket packet = PartiesPacket.read(plugin, bytes);
		if (packet != null) {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_BUNGEE_RECEIVED, packet.getType().name(), messageChannel.getId()), true);
			switch ((PartiesPacket.PacketType) packet.getType()) {
				case UPDATE_PARTY:
					commonListener.handleUpdateParty(packet.getParty());
					break;
				case UPDATE_PLAYER:
					commonListener.handleUpdateParty(packet.getPlayer());
					break;
				case CREATE_PARTY:
					commonListener.handlePostPartyCreate(packet.getParty(), packet.getPlayer(), true);
					break;
				case DELETE_PARTY:
					commonListener.handlePostPartyDelete(packet.getParty(), (DeleteCause) packet.getCause(), packet.getPlayer(), packet.getSecondaryPlayer());
					break;
				case RENAME_PARTY:
					commonListener.handlePostPartyRename(packet.getParty(), packet.getText(), packet.getSecondaryText(), packet.getPlayer(), packet.isBool());
					break;
				case ADD_MEMBER_PARTY:
					commonListener.handlePostPartyAddMember(packet.getParty(), packet.getPlayer(), (JoinCause) packet.getCause(), packet.getSecondaryPlayer());
					break;
				case REMOVE_MEMBER_PARTY:
					commonListener.handlePostPartyRemoveMember(packet.getParty(), packet.getPlayer(), (LeaveCause) packet.getCause(), packet.getSecondaryPlayer());
					break;
				case CHAT_MESSAGE:
					commonListener.handleChatMessage(packet.getParty(), packet.getPlayer(), packet.getText());
					break;
				case BROADCAST_MESSAGE:
					commonListener.handleBroadcastMessage(packet.getParty(), packet.getPlayer(), packet.getText());
					break;
				case INVITE_PLAYER:
					commonListener.handleInvitePlayer(packet.getParty(), packet.getPlayer(), packet.getSecondaryPlayer());
					break;
				case ADD_HOME:
					commonListener.handleAddHome(packet.getParty(), packet.getText());
					break;
				case EXPERIENCE:
					commonListener.handleExperience(packet.getParty(), packet.getPlayer(), packet.getNumber(), packet.isBool());
					break;
				case REQUEST_CONFIGS:
					handleRequestConfigs();
					break;
				default:
					// Nothing to do
					break;
			}
		} else {
			plugin.getLoggerManager().logError(String.format(PartiesConstants.DEBUG_MESSAGING_BUNGEE_RECEIVED_WRONG, messageChannel.getId()));
		}
	}
	
	public void handleRequestConfigs() {
		if (ConfigMain.PARTIES_BUNGEECORD_PACKETS_CONFIG_SYNC) {
			((VelocityPartiesConfigurationManager) plugin.getConfigurationManager()).makeConfigsSync();
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_MESSAGING_LISTEN_REQUEST_CONFIGS, true);
		}
	}
}
