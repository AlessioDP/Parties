package com.alessiodp.parties.bukkit.players.objects;

import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.utils.LastConfirmedCommand;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.api.interfaces.Rank;
import org.bukkit.Bukkit;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.metadata.MetadataValue;

public class BukkitPartyPlayerImpl extends PartyPlayerImpl {
	@Getter @Setter private HomeCooldown homeCooldown;
	
	@Getter @Setter private int portalTimeoutTask;
	
	@Getter @Setter private LastConfirmedCommand lastConfirmedCommand;
	
	public BukkitPartyPlayerImpl(PartiesPlugin instance, UUID uuid) {
		super(instance, uuid);
		homeCooldown = null;
		portalTimeoutTask = -1;
		lastConfirmedCommand = null;
	}
	
	@Override
	public void cleanupPlayer(boolean saveDB) {
		homeCooldown = null; // Reset home command (avoiding telepoting to another party home)
		super.cleanupPlayer(saveDB);
	}
	
	@Override
	public List<PartiesCommand> getAllowedCommands() {
		List<PartiesCommand> ret = super.getAllowedCommands();
		Rank rank = plugin.getRankManager().searchRankByLevel(getRank());
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (!getPartyName().isEmpty()) {
			// Other commands
			if (BukkitConfigParties.HOME_ENABLE) {
				if (player.hasPermission(PartiesPermission.HOME_OTHERS.toString())
						|| (player.hasPermission(PartiesPermission.HOME.toString()) && rank.havePermission(PartiesPermission.PRIVATE_HOME.toString())))
					ret.add(BukkitCommands.HOME);
				if (player.hasPermission(PartiesPermission.SETHOME.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_HOME.toString()))
					ret.add(BukkitCommands.SETHOME);
			}
			if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE && player.hasPermission(PartiesPermission.CLAIM.toString()) && rank.havePermission(PartiesPermission.PRIVATE_CLAIM.toString()))
				ret.add(BukkitCommands.CLAIM);
			
			// Admin commands
			if (BukkitConfigParties.TELEPORT_ENABLE && player.hasPermission(PartiesPermission.TELEPORT.toString()) && rank.havePermission(PartiesPermission.PRIVATE_ADMIN_TELEPORT.toString()))
				ret.add(BukkitCommands.TELEPORT);
			
		}
		return ret;
	}
	
	@Override
	public boolean isVanished() {
		for (MetadataValue meta : Bukkit.getPlayer(this.getPlayerUUID()).getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}
	
	@Override
	public void sendDirect(String message) {
		// Overriding superclass send
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendMessage(message, true);
		}
	}
}
