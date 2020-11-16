package com.alessiodp.parties.bukkit.parties.objects;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class BukkitPartyImpl extends PartyImpl {
	
	public BukkitPartyImpl(PartiesPlugin plugin, UUID id) {
		super(plugin, id);
	}
	
	@Override
	public void updateParty() {
		DynmapHandler.updatePartyMarker(this);
		super.updateParty();
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingUpdateParty(getId());
	}
	
	@Override
	public void delete() {
		DynmapHandler.cleanupMarkers(this);
		super.delete();
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingRemoveParty(getId());
	}
	
	@Override
	public void rename(@NonNull String newName) {
		DynmapHandler.cleanupMarkers(this);
		
		super.rename(newName);
		
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingRenameParty(getId(), newName);
	}
	
	@Override
	public void callChange() {
		// Nothing to do
	}
	
	@Override
	public void sendExperiencePacket(double newExperience, PartyPlayer killer) {
		// Send event to BungeeCord only
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPartyExperience(this, newExperience, (PartyPlayerImpl) killer);
	}
	
	@Override
	public void sendLevelUpPacket(int newLevel) {
		throw new IllegalStateException("this method should be executed on BungeeCord only");
	}
	
	@Override
	public void broadcastDirectMessage(String message, boolean dispatchBetweenServers) {
		super.broadcastDirectMessage(message, dispatchBetweenServers);
		
		if (dispatchBetweenServers && message != null && !message.isEmpty()) {
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingBroadcastMessage(getId(), message);
		}
	}
	
	@Override
	public void dispatchChatMessage(PartyPlayerImpl sender, String message, boolean dispatchBetweenServers) {
		super.dispatchChatMessage(sender, message, dispatchBetweenServers);
		
		if (dispatchBetweenServers && message != null && !message.isEmpty()) {
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingChatMessage(getId(), sender.getPlayerUUID(), message);
		}
	}
	
	@Override
	public boolean isFriendlyFireProtected() {
		boolean ret = false;
		if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE) {
			if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE.equalsIgnoreCase("command")) {
				// Command
				ret = super.getProtection();
			} else {
				// Global
				ret = true;
			}
		}
		return ret;
	}
	
	public void warnFriendlyFire(PartyPlayerImpl victim, PartyPlayerImpl attacker) {
		if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT) {
			String message = BukkitMessages.ADDCMD_PROTECTION_WARNHIT
					.replace("%player%", attacker.getName())
					.replace("%victim%", victim.getName());
			
			for (PartyPlayer onlineP : getOnlineMembers(true)) {
				if (!onlineP.getPlayerUUID().equals(attacker.getPlayerUUID())
						&& !plugin.getRankManager().checkPlayerRank((PartyPlayerImpl) onlineP, PartiesPermission.PRIVATE_WARNONDAMAGE)) {
					User user = plugin.getPlayer(onlineP.getPlayerUUID());
					user.sendMessage(plugin.getMessageUtils().convertPlaceholders(message, (PartyPlayerImpl) onlineP, this), true);
				}
			}
		}
	}
}
