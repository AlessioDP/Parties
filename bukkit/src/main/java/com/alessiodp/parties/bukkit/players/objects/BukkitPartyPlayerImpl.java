package com.alessiodp.parties.bukkit.players.objects;

import java.util.Set;
import java.util.UUID;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.addons.external.BukkitAdvancedBanHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessageDispatcher;
import com.alessiodp.parties.bukkit.utils.LastConfirmedCommand;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class BukkitPartyPlayerImpl extends PartyPlayerImpl {
	@Getter @Setter private boolean portalPause = false;
	
	@Getter @Setter private LastConfirmedCommand lastConfirmedCommand;
	
	public BukkitPartyPlayerImpl(PartiesPlugin plugin, UUID uuid) {
		super(plugin, uuid);
	}
	
	@Override
	public void playSound(String sound, double volume, double pitch) {
		User user = plugin.getPlayer(getPlayerUUID());
		if (user != null)
			user.playSound(sound, volume, pitch);
	}
	
	@Override
	public void playChatSound() {
		if (ConfigParties.GENERAL_SOUNDS_ON_CHAT_ENABLE) {
			User user = plugin.getPlayer(getPlayerUUID());
			if (user != null) {
				user.playSound(ConfigParties.GENERAL_SOUNDS_ON_CHAT_NAME, ConfigParties.GENERAL_SOUNDS_ON_CHAT_VOLUME, ConfigParties.GENERAL_SOUNDS_ON_CHAT_PITCH);
			}
		}
	}
	
	@Override
	public void playBroadcastSound() {
		if (ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_ENABLE) {
			User user = plugin.getPlayer(getPlayerUUID());
			if (user != null)
				user.playSound(ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_NAME, ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_VOLUME, ConfigParties.GENERAL_SOUNDS_ON_BROADCAST_PITCH);
		}
	}
	
	public void playPacketSound(byte[] raw) {
		try {
			ByteArrayDataInput input = ByteStreams.newDataInput(raw);
			String sound = input.readUTF();
			double volume = input.readDouble();
			double pitch = input.readDouble();
			
			playSound(sound, volume, pitch);
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_PLAY_SOUND_ERROR, ex.getMessage() != null ? ex.getMessage() : ex.toString()));
		}
	}
	
	@Override
	public Set<ADPCommand> getAllowedCommands() {
		Set<ADPCommand> ret = super.getAllowedCommands();
		PartyRankImpl rank = plugin.getRankManager().searchRankByLevel(getRank());
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (isInParty()) {
			// Other commands
			if (BukkitConfigMain.ADDONS_CLAIM_ENABLE && player.hasPermission(PartiesPermission.USER_CLAIM) && rank.havePermission(PartiesPermission.PRIVATE_CLAIM))
				ret.add(BukkitCommands.CLAIM);
			
		}
		return ret;
	}
	
	@Override
	public void sendPacketUpdate() {
		if (plugin.isBungeeCordEnabled()) {
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdatePlayer(this);
		}
	}
	
	@Override
	public boolean isVanished() {
		Player player = Bukkit.getPlayer(this.getPlayerUUID());
		if (player != null) {
			for (MetadataValue meta : player.getMetadata("vanished")) {
				if (meta.asBoolean()) return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isChatMuted() {
		if (ConfigMain.ADDITIONAL_MODERATION_ENABLE
				&& ConfigMain.ADDITIONAL_MODERATION_PREVENTCHAT) {
			return BukkitAdvancedBanHandler.isMuted(getPlayerUUID())
					|| BanManagerHandler.isMuted(getPlayerUUID())
					|| EssentialsHandler.isPlayerMuted(getPlayerUUID());
		}
		return false;
	}
}
