package com.alessiodp.parties.bungeecord.players.objects;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.addons.external.BungeeAdvancedBanHandler;
import com.alessiodp.parties.bungeecord.addons.external.BungeeChatHandler;
import com.alessiodp.parties.bungeecord.addons.external.PremiumVanishHandler;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.UUID;

public class BungeePartyPlayerImpl extends PartyPlayerImpl {
	
	public BungeePartyPlayerImpl(PartiesPlugin plugin, UUID uuid) {
		super(plugin, uuid);
	}
	
	@Override
	public void playSound(String sound, double volume, double pitch) {
		User user = plugin.getPlayer(getPlayerUUID());
		if (user != null) {
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPlaySound(
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
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdatePlayer(this);
	}
	
	@Override
	public boolean isVanished() {
		return PremiumVanishHandler.isPlayerVanished(getPlayerUUID());
	}
	
	@Override
	public boolean isChatMuted() {
		if (ConfigMain.ADDITIONAL_MODERATION_ENABLE
				&& ConfigMain.ADDITIONAL_MODERATION_PREVENTCHAT) {
			return BungeeChatHandler.isPlayerMuted(getPlayerUUID())
					|| BungeeAdvancedBanHandler.isMuted(getPlayerUUID());
		}
		return false;
	}
}
