package com.alessiodp.parties.bungeecord.players.objects;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.addons.external.BungeeChatHandler;
import com.alessiodp.parties.bungeecord.addons.external.PremiumVanishHandler;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

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
					makePlaySoundRaw(sound, volume, pitch)
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
	
	private byte[] makePlaySoundRaw(String sound, double volume, double pitch) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(sound);
		raw.writeDouble(volume);
		raw.writeDouble(pitch);
		return raw.toByteArray();
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
		return BungeeChatHandler.isPlayerMuted(getPlayerUUID());
	}
}
