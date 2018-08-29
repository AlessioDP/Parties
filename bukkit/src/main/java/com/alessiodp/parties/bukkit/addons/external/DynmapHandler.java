package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.ConsoleColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public class DynmapHandler {
	private static BukkitPartiesPlugin plugin;
	private static final String ADDON_NAME = "Dynmap";
	private static boolean active;
	private static DynmapAPI api;
	
	private static MarkerSet layer;
	
	public DynmapHandler(BukkitPartiesPlugin instance) {
		plugin = instance;
	}
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDONS_DYNMAP_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				active = true;
				api = (DynmapAPI) plugin.getBootstrap().getServer().getPluginManager().getPlugin(ADDON_NAME);
				MarkerAPI markerapi = api.getMarkerAPI();
				
				layer = markerapi.getMarkerSet("Parties");
				
				if (layer != null) {
					if (!layer.getMarkerSetLabel().equals(BukkitConfigMain.ADDONS_DYNMAP_MARKER_LAYER)) {
						layer.setMarkerSetLabel(BukkitConfigMain.ADDONS_DYNMAP_MARKER_LAYER);
					}
				} else {
					layer = markerapi.createMarkerSet("Parties", BukkitConfigMain.ADDONS_DYNMAP_MARKER_LAYER, null, true);
					layer.setHideByDefault(BukkitConfigMain.ADDONS_DYNMAP_HIDEDEFAULT);
				}
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				BukkitConfigMain.ADDONS_DYNMAP_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	
	private static void addMarker(String partyName, String label, Location loc) {
		for (Marker m : layer.getMarkers()) {
			if (m.getMarkerID().equals("party_" + partyName))
				m.deleteMarker();
		}
		layer.createMarker("party_" + partyName,
				label,
				true,
				loc.getWorld().getName(),
				loc.getX(),
				loc.getY(),
				loc.getZ(),
				api.getMarkerAPI().getMarkerIcon(BukkitConfigMain.ADDONS_DYNMAP_MARKER_ICON),
				true);
	}
	
	public static void removeMarker(String partyName) {
		if (active) {
			for (Marker m : layer.getMarkers()) {
				if (m.getMarkerID().equals("party_" + partyName))
					m.deleteMarker();
			}
		}
	}
	
	public static void updatePartyMarker(PartyImpl party) {
		if (active) {
			if (party.getMembers().size() >= BukkitConfigMain.ADDONS_DYNMAP_MINPLAYERS
					&& party.getHome() != null) {
				Location loc = new Location(
						Bukkit.getWorld(party.getHome().getWorld()),
						party.getHome().getX(),
						party.getHome().getY(),
						party.getHome().getZ(),
						party.getHome().getYaw(),
						party.getHome().getPitch()
				);
				addMarker(party.getName(), plugin.getMessageUtils().convertPartyPlaceholders(BukkitConfigMain.ADDONS_DYNMAP_MARKER_LABEL, party), loc);
			} else {
				removeMarker(party.getName());
			}
		}
	}
}
