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
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitPartyImpl extends PartyImpl {
	
	private double experienceStampCalculateLevel;
	
	public BukkitPartyImpl(PartiesPlugin plugin, String name) {
		super(plugin, name);
		experienceStampCalculateLevel = -1;
	}
	
	@Override
	public void updateParty() {
		DynmapHandler.updatePartyMarker(this);
		super.updateParty();
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingUpdateParty(getName());
	}
	
	@Override
	public void delete() {
		DynmapHandler.removeMarker(getName());
		super.delete();
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingRemoveParty(getName());
	}
	
	@Override
	public void rename(@NonNull String newName) {
		String oldName = getName();
		DynmapHandler.removeMarker(oldName);
		
		super.rename(newName);
		
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingRenameParty(getName(), oldName);
	}
	
	@Override
	public void callChange() {
		// Update experience
		if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
			// A sort of cached level to avoid useless re-calculation of the level
			if (experienceStampCalculateLevel == -1 || experienceStampCalculateLevel != getExperience()) {
				try {
					// Set the new level of the party
					expResult = ((BukkitPartiesPlugin) plugin).getExpManager().calculateLevel(getExperience());
					// Update experience stamp
					experienceStampCalculateLevel = getExperience();
				} catch (Exception ex) {
					plugin.getLoggerManager().printError(PartiesConstants.DEBUG_EXP_LEVELERROR
							.replace("{party}", getName())
							.replace("{message}", ex.getMessage() != null ? ex.getMessage() : ex.toString()));
				}
			}
		}
	}
	
	@Override
	public void broadcastDirectMessage(String message, boolean dispatchBetweenServers) {
		super.broadcastDirectMessage(message, dispatchBetweenServers);
		
		if (dispatchBetweenServers && message != null && !message.isEmpty()) {
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingBroadcastMessage(getName(), message);
		}
	}
	
	@Override
	public void dispatchChatMessage(PartyPlayerImpl sender, String message, boolean dispatchBetweenServers) {
		super.dispatchChatMessage(sender, message, dispatchBetweenServers);
		
		if (dispatchBetweenServers && message != null && !message.isEmpty()) {
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingChatMessage(getName(), sender.getPlayerUUID(), message);
		}
	}
	
	@Override
	public boolean isFriendlyFireProtected() {
		boolean ret = false;
		if (BukkitConfigParties.FRIENDLYFIRE_ENABLE ) {
			if (BukkitConfigParties.FRIENDLYFIRE_TYPE.equalsIgnoreCase("command")) {
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
		if (BukkitConfigParties.FRIENDLYFIRE_WARNONFIGHT) {
			String message = BukkitMessages.ADDCMD_PROTECTION_WARNHIT
					.replace(PartiesConstants.PLACEHOLDER_PLAYER_NAME, attacker.getName())
					.replace(PartiesConstants.PLACEHOLDER_PLAYER_VICTIM, victim.getName());
			
			for (PartyPlayer onlineP : getOnlineMembers(true)) {
				if (!onlineP.getPlayerUUID().equals(attacker.getPlayerUUID())
						&& !plugin.getRankManager().checkPlayerRank((PartyPlayerImpl) onlineP, PartiesPermission.PRIVATE_WARNONDAMAGE)) {
					User user = plugin.getPlayer(onlineP.getPlayerUUID());
					user.sendMessage(plugin.getMessageUtils().convertAllPlaceholders(message, this, (PartyPlayerImpl) onlineP), true);
				}
			}
		}
	}
}
