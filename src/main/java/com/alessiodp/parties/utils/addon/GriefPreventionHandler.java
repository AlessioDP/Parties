package com.alessiodp.parties.utils.addon;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHandler {
	Parties plugin;
	public GriefPreventionHandler(Parties instance){
		plugin = instance;
	}
	
	public void addPartyAccess(Player claimer, Party party){
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(claimer);
		if(claim == null){
			tp.sendMessage(Messages.claim_noexistclaim);
			return;
		}
		if(!claim.getOwnerName().equalsIgnoreCase(claimer.getName())){
			if(Variables.griefprevention_needowner){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}
			if(!claim.managers.contains(claimer.getUniqueId().toString())){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}	
		}
		
		for(UUID uuid : party.getMembers()){
			if(Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.setPermission(uuid.toString(), ClaimPermission.Access);
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
		tp.sendMessage(Messages.claim_done);
	}
	public void addPartyTrust(Player claimer, Party party){
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(claimer);
		if(claim == null){
			tp.sendMessage(Messages.claim_noexistclaim);
			return;
		}
		if(!claim.getOwnerName().equalsIgnoreCase(claimer.getName())){
			if(Variables.griefprevention_needowner){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}
			if(!claim.managers.contains(claimer.getUniqueId().toString())){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}	
		}
		
		for(UUID uuid : party.getMembers()){
			if(Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.setPermission(uuid.toString(), ClaimPermission.Build);
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
		tp.sendMessage(Messages.claim_done);
	}
	public void addPartyContainer(Player claimer, Party party){
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(claimer);
		if(claim == null){
			tp.sendMessage(Messages.claim_noexistclaim);
			return;
		}
		if(!claim.getOwnerName().equalsIgnoreCase(claimer.getName())){
			if(Variables.griefprevention_needowner){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}
			if(!claim.managers.contains(claimer.getUniqueId().toString())){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}	
		}
		
		for(UUID uuid : party.getMembers()){
			if(Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.setPermission(uuid.toString(), ClaimPermission.Inventory);
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
		tp.sendMessage(Messages.claim_done);
	}
	public void dropParty(Player claimer, Party party){
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(claimer.getLocation(), false, null);
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(claimer);
		if(claim == null){
			tp.sendMessage(Messages.claim_noexistclaim);
			return;
		}
		if(!claim.getOwnerName().equalsIgnoreCase(claimer.getName())){
			if(Variables.griefprevention_needowner){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}
			if(!claim.managers.contains(claimer.getUniqueId().toString())){
				tp.sendMessage(Messages.claim_nomanager);
				return;
			}	
		}
		for(UUID uuid : party.getMembers()){
			if(Bukkit.getOfflinePlayer(uuid).getName().equalsIgnoreCase(claimer.getName()))
				continue;
			claim.dropPermission(uuid.toString());
		}
		GriefPrevention.instance.dataStore.saveClaim(claim);
		tp.sendMessage(Messages.claim_done);
	}
}
