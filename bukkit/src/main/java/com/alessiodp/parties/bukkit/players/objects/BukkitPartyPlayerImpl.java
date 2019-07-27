package com.alessiodp.parties.bukkit.players.objects;

import java.util.List;
import java.util.UUID;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessageDispatcher;
import com.alessiodp.parties.bukkit.utils.LastConfirmedCommand;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.interfaces.Rank;
import org.bukkit.Bukkit;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class BukkitPartyPlayerImpl extends PartyPlayerImpl {
	@Getter @Setter private CancellableTask homeDelayTask;
	@Getter @Setter private CancellableTask portalTimeoutTask;
	
	@Getter @Setter private LastConfirmedCommand lastConfirmedCommand;
	
	public BukkitPartyPlayerImpl(PartiesPlugin plugin, UUID uuid) {
		super(plugin, uuid);
	}
	
	@Override
	public void updatePlayer() {
		super.updatePlayer();
		((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPingUpdatePlayer(getPlayerUUID());
	}
	
	@Override
	public void removeFromParty(boolean saveDB) {
		lock.lock();
		if (getHomeDelayTask() != null) {
			getHomeDelayTask().cancel();
			setHomeDelayTask(null);
		}
		lock.unlock();
		super.removeFromParty(saveDB);
	}
	
	
	@Override
	public List<ADPCommand> getAllowedCommands() {
		List<ADPCommand> ret = super.getAllowedCommands();
		Rank rank = plugin.getRankManager().searchRankByLevel(getRank());
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (!getPartyName().isEmpty()) {
			// Other commands
			if (BukkitConfigParties.HOME_ENABLE) {
				if (player.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS.toString())
						|| (player.hasPermission(PartiesPermission.HOME.toString()) && rank.havePermission(PartiesPermission.PRIVATE_HOME.toString())))
					ret.add(BukkitCommands.HOME);
				if (player.hasPermission(PartiesPermission.SETHOME.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_HOME.toString()))
					ret.add(BukkitCommands.SETHOME);
			}
			if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE && player.hasPermission(PartiesPermission.CLAIM.toString()) && rank.havePermission(PartiesPermission.PRIVATE_CLAIM.toString()))
				ret.add(BukkitCommands.CLAIM);
			
			// Admin commands
			if (BukkitConfigParties.FRIENDLYFIRE_ENABLE
					&& BukkitConfigParties.FRIENDLYFIRE_TYPE.equalsIgnoreCase("command")
					&& player.hasPermission(PartiesPermission.PROTECTION.toString())
					&& rank.havePermission(PartiesPermission.PRIVATE_EDIT_PROTECTION.toString()))
				ret.add(BukkitCommands.PROTECTION);
			
		}
		return ret;
	}
	
	@Override
	public void performPartyMessage(String message) {
		if (BukkitConfigMain.ADDONS_BANMANAGER_ENABLE
				&& BukkitConfigMain.ADDONS_BANMANAGER_PREVENTCHAT
				&& BanManagerHandler.isMuted(getPlayerUUID())) {
			return;
		}
		
		super.performPartyMessage(message);
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
}
