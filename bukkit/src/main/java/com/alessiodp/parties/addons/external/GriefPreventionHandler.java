package com.alessiodp.parties.addons.external;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.utils.ConsoleColor;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHandler {
	private static final String ADDON_NAME = "GriefPrevention";
	
	public GriefPreventionHandler() {
		init();
	}
	
	private void init() {
		if (ConfigMain.ADDONS_GRIEFPREVENTION_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigMain.ADDONS_GRIEFPREVENTION_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	
	public static Result isManager(Player claimer) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		if (claim == null) {
			return Result.NOEXIST;
		}
		if (!claim.getOwnerName().equalsIgnoreCase(claimer.getName())) {
			if (ConfigMain.ADDONS_GRIEFPREVENTION_NEEDOWNER || !claim.managers.contains(claimer.getUniqueId().toString())) {
				return Result.NOMANAGER;
			}
		}
		return Result.SUCCESS;
	}
	
	public static void addPartyPermission(Player claimer, PartyEntity party, PermissionType perm) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		
		for (UUID uuid : party.getMembers()) {
			if (claimer.getUniqueId().equals(uuid))
				continue;
			if (perm.isRemove())
				claim.dropPermission(uuid.toString());
			else
				claim.setPermission(uuid.toString(), perm.convertPermission());
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
	}
	
	public enum PermissionType {
		ACCESS, BUILD, INVENTORY, REMOVE;
		
		public boolean isRemove() {
			return this.equals(PermissionType.REMOVE);
		}
		
		public ClaimPermission convertPermission() {
			ClaimPermission ret;
			switch (this) {
			case ACCESS:
				ret = ClaimPermission.Access;
				break;
			case BUILD:
				ret = ClaimPermission.Build;
				break;
			case INVENTORY:
				ret = ClaimPermission.Inventory;
				break;
			default:
					ret = null;
			}
			return ret;
		}
	}
	
	public enum Result {
		NOEXIST, NOMANAGER, SUCCESS
	}
}
