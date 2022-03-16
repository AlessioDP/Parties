package com.alessiodp.parties.velocity.players.objects;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;

import java.util.UUID;

public class VelocityPartyPlayerImpl extends PartyPlayerImpl {
	
	public VelocityPartyPlayerImpl(PartiesPlugin plugin, UUID uuid) {
		super(plugin, uuid);
	}
	
	@Override
	public void playSound(String sound, double volume, double pitch) {
		User user = plugin.getPlayer(getPlayerUUID());
		if (user != null) {
			((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPlaySound(
					user,
					sound,
					volume,
					pitch
			);
		}
	}
	
	@Override
	public void playChatSound() {
		if (ConfigParties.GENERAL_SOUNDS_ON_CHAT_ENABLE) {
			playSound(ConfigParties.GENERAL_SOUNDS_ON_CHAT_NAME, ConfigParties.GENERAL_SOUNDS_ON_CHAT_VOLUME, ConfigParties.GENERAL_SOUNDS_ON_CHAT_PITCH);
		}
	}
	
	@Override
	public void playBroadcastSound() {
		if (ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_ENABLE) {
			playSound(ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_NAME, ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_VOLUME, ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_PITCH);
		}
	}
	
	@Override
	public void sendPacketUpdate() {
		((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdatePlayer(this);
	}
	
	@Override
	public boolean isVanished() {
		return false;
	}
}
