package com.alessiodp.parties.utils.addon;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHandler {
	
	Parties plugin;
	public enum Result {
		NOEXIST, NOMANAGER, SUCCESS
	}
	public GriefPreventionHandler(Parties instance) {
		plugin = instance;
	}
	
	public Result isManager(Player claimer) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		if (claim == null) {
			return Result.NOEXIST;
		}
		if (!claim.getOwnerName().equalsIgnoreCase(claimer.getName())) {
			if (Variables.griefprevention_needowner || !claim.managers.contains(claimer.getUniqueId().toString())) {
				return Result.NOMANAGER;
			}
		}
		return Result.SUCCESS;
	}
	
	public void addPartyAccess(Player claimer, Party party) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		
		for (UUID uuid : party.getMembers()) {
			String name = Bukkit.getOfflinePlayer(uuid).getName() != null ? Bukkit.getOfflinePlayer(uuid).getName() : plugin.getDatabaseDispatcher().getOldPlayerName(uuid);
			if (name.equalsIgnoreCase(claimer.getName()))
				continue;
			claim.setPermission(uuid.toString(), ClaimPermission.Access);
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
	}
	public void addPartyTrust(Player claimer, Party party) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		
		for (UUID uuid : party.getMembers()) {
			if (Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.setPermission(uuid.toString(), ClaimPermission.Build);
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
	}
	public void addPartyContainer(Player claimer, Party party) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		
		for (UUID uuid : party.getMembers()) {
			if (Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.setPermission(uuid.toString(), ClaimPermission.Inventory);
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
	}
	public void dropParty(Player claimer, Party party) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		
		for (UUID uuid : party.getMembers()) {
			if (Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.dropPermission(uuid.toString());
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
	}
}
