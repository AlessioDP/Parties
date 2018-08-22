package com.alessiodp.parties.bukkit.parties.objects;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.ExpResult;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public class BukkitPartyImpl extends PartyImpl {
	
	private double experienceStampCalculateLevel;
	
	public BukkitPartyImpl(PartiesPlugin instance, String name) {
		super(instance, name);
		experienceStampCalculateLevel = -1;
	}
	
	@Override
	public void updateParty() {
		DynmapHandler.updatePartyMarker(this);
		super.updateParty();
	}
	
	@Override
	public void renamingParty() {
		DynmapHandler.removeMarker(getName());
		super.renamingParty();
	}
	
	@Override
	public void removeParty() {
		DynmapHandler.removeMarker(getName());
		super.removeParty();
	}
	
	@Override
	public void callChange() {
		// Update experience
		if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
			// A sort of cached level to avoid useless re-calculation of the level
			if (experienceStampCalculateLevel == -1 || experienceStampCalculateLevel != getExperience()) {
				try {
					// Set the new level of the party
					setExpResult(((BukkitPartiesPlugin) plugin).getExpManager().calculateLevel(getExperience()));
					// Update experience stamp
					experienceStampCalculateLevel = getExperience();
				} catch (Exception ex) {
					LoggerManager.printError(Constants.DEBUG_EXP_LEVELERROR
							.replace("{party}", getName())
							.replace("{message}", ex.getMessage()));
				}
			}
		}
	}
	
	@Override
	public void sendChatMessage(PartyPlayerImpl sender, String playerMessage) {
		if (BukkitConfigMain.ADDONS_BANMANAGER_ENABLE) {
			if (BukkitConfigMain.ADDONS_BANMANAGER_PREVENTCHAT
					&& BanManagerHandler.isMuted(sender.getPlayerUUID())) {
				return;
			}
		}
		super.sendChatMessage(sender, playerMessage);
	}
	
	public void sendFriendlyFireWarn(PartyPlayerImpl victim, PartyPlayerImpl attacker) {
		if (BukkitConfigParties.FRIENDLYFIRE_WARNONFIGHT) {
			String message = BukkitMessages.ADDCMD_PVP_WARNHIT
					.replace(Constants.PLACEHOLDER_PLAYER_PLAYER, attacker.getName())
					.replace(Constants.PLACEHOLDER_PLAYER_VICTIM, victim.getName());
			
			for (PartyPlayerImpl onlineP : getOnlinePlayers()) {
				if (!onlineP.getPlayerUUID().equals(attacker.getPlayerUUID())) {
					if (!plugin.getRankManager().checkPlayerRank(onlineP, PartiesPermission.PRIVATE_WARNONDAMAGE)) {
						
						onlineP.sendMessage(message, this);
					}
				}
			}
		}
	}
	
	@Override
	public boolean isFriendlyFireProtected() {
		boolean ret = false;
		if (BukkitConfigParties.FRIENDLYFIRE_ENABLE ) {
			switch (BukkitConfigParties.FRIENDLYFIRE_TYPE.toLowerCase()){
				case "command":
					// Check for party protected field
					ret = super.isFriendlyFireProtected();
					break;
				default:
					// Global
					ret = true;
			}
		}
		return ret;
	}
}
