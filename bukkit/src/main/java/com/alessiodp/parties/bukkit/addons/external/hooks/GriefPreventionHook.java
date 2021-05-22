package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.parties.bukkit.addons.external.ClaimHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GriefPreventionHook {
	
	public static ClaimHandler.Result isManager(Player claimer) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		if (claim == null) {
			return ClaimHandler.Result.NOEXIST;
		}
		if (!claim.getOwnerName().equalsIgnoreCase(claimer.getName())
				&& (BukkitConfigMain.ADDONS_CLAIM_NEEDOWNER || !claim.managers.contains(claimer.getUniqueId().toString()))) {
			return ClaimHandler.Result.NOMANAGER;
		}
		return ClaimHandler.Result.SUCCESS;
	}
	
	public static void addPartyPermission(Player claimer, PartyImpl party, ClaimHandler.PermissionType perm) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		for (UUID uuid : party.getMembers()) {
			if (claimer.getUniqueId().equals(uuid))
				continue;
			if (perm.isRemove())
				claim.dropPermission(uuid.toString());
			else
				claim.setPermission(uuid.toString(), convertPermission(perm));
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
	}
	
	public static ClaimPermission convertPermission(ClaimHandler.PermissionType type) {
		ClaimPermission ret;
		switch (type) {
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
				break;
		}
		return ret;
	}
}
