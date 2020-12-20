package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class GriefPreventionHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "GriefPrevention";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE = false;
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
			}
		}
	}
	
	
	public static Result isManager(Player claimer) {
		if (active) {
			Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
			if (claim == null) {
				return Result.NOEXIST;
			}
			if (!claim.getOwnerName().equalsIgnoreCase(claimer.getName())) {
				if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_NEEDOWNER || !claim.managers.contains(claimer.getUniqueId().toString())) {
					return Result.NOMANAGER;
				}
			}
			return Result.SUCCESS;
		}
		return Result.NOEXIST;
	}
	
	public static void addPartyPermission(Player claimer, PartyImpl party, PermissionType perm) {
		if (active) {
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
	}
	
	public enum PermissionType {
		ACCESS, BUILD, INVENTORY, REMOVE;
		
		public boolean isRemove() {
			return this.equals(PermissionType.REMOVE);
		}
		
		ClaimPermission convertPermission() {
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
