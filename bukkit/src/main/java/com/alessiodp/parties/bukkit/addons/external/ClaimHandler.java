package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.addons.external.hooks.GriefDefenderHook;
import com.alessiodp.parties.bukkit.addons.external.hooks.GriefPreventionHook;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ClaimHandler {
	@NotNull protected final PartiesPlugin plugin;
	protected static boolean active;
	protected static Plugin activePlugin;
	private static final String ADDON_NAME_GD = "GriefDefender";
	private static final String ADDON_NAME_GP = "GriefPrevention";
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDONS_CLAIM_ENABLE) {
			if (BukkitConfigMain.ADDONS_CLAIM_PLUGIN.equalsIgnoreCase(ADDON_NAME_GD)) {
				if (Bukkit.getPluginManager().getPlugin(ADDON_NAME_GD) != null) {
					active = true;
					activePlugin = Plugin.GRIEFDEFENDER;
					
					plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME_GD), true);
				} else {
					BukkitConfigMain.ADDONS_CLAIM_ENABLE = false;
					plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME_GD), true);
				}
			} else if (BukkitConfigMain.ADDONS_CLAIM_PLUGIN.equalsIgnoreCase(ADDON_NAME_GP)) {
				if (Bukkit.getPluginManager().getPlugin(ADDON_NAME_GP) != null) {
					active = true;
					activePlugin = Plugin.GRIEFPREVENTION;
					
					plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME_GP), true);
				} else {
					BukkitConfigMain.ADDONS_CLAIM_ENABLE = false;
					plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME_GP), true);
				}
			}
		}
	}
	
	public static Result isManager(Player claimer) {
		if (active) {
			return activePlugin == Plugin.GRIEFDEFENDER ? GriefDefenderHook.isManager(claimer) : GriefPreventionHook.isManager(claimer);
		}
		return Result.NOEXIST;
	}
	
	public static void addPartyPermission(Player claimer, PartyImpl party, PermissionType perm) {
		if (active) {
			if (activePlugin == Plugin.GRIEFDEFENDER)
				GriefDefenderHook.addPartyPermission(claimer, party, perm);
			else
				GriefPreventionHook.addPartyPermission(claimer, party, perm);
		}
	}
	
	public static boolean canChangeManager() {
		return activePlugin == Plugin.GRIEFDEFENDER;
	}
	
	public enum Plugin {
		GRIEFDEFENDER, GRIEFPREVENTION
	}
	
	public enum PermissionType {
		ACCESS, BUILD, INVENTORY, MANAGE, REMOVE;
		
		public boolean isRemove() {
			return this.equals(PermissionType.REMOVE);
		}
	}
	
	public enum Result {
		NOEXIST, NOMANAGER, SUCCESS
	}
}
