package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.parties.bukkit.addons.external.ClaimHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustType;
import com.griefdefender.api.claim.TrustTypes;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GriefDefenderHook {
	
	public static ClaimHandler.Result isManager(Player claimer) {
		final Claim claim = GriefDefender.getCore().getClaimAt(claimer.getLocation());
		if (claim == null) {
			return ClaimHandler.Result.NOEXIST;
		}
		if (claim.isWilderness()) {
			return ClaimHandler.Result.NOEXIST;
		}
		if (!claimer.getUniqueId().equals(claim.getOwnerUniqueId())
				&& (BukkitConfigMain.ADDONS_CLAIM_NEEDOWNER || !claim.getUserTrusts(TrustTypes.MANAGER).contains(claimer.getUniqueId()))) {
			return ClaimHandler.Result.NOMANAGER;
		}
		return ClaimHandler.Result.SUCCESS;
	}
	
	public static void addPartyPermission(Player claimer, PartyImpl party, ClaimHandler.PermissionType perm) {
		final Claim claim = GriefDefender.getCore().getClaimAt(claimer.getLocation());
		if (claim == null) {
			return;
		}
		for (UUID uuid : party.getMembers()) {
			if (claimer.getUniqueId().equals(uuid))
				continue;
			if (perm.isRemove())
				claim.removeUserTrust(uuid, convertPermission(perm));
			else
				claim.addUserTrust(uuid, convertPermission(perm));
		}
	}
	
	public static TrustType convertPermission(ClaimHandler.PermissionType type) {
		TrustType ret;
		switch (type) {
			case ACCESS:
				ret = TrustTypes.ACCESSOR;
				break;
			case BUILD:
				ret = TrustTypes.BUILDER;
				break;
			case INVENTORY:
				ret = TrustTypes.CONTAINER;
				break;
			case MANAGE:
				ret = TrustTypes.MANAGER;
				break;
			default:
				ret = null;
				break;
		}
		return ret;
	}
}
