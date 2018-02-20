package com.alessiodp.parties.addons.external;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.parties.utils.PartiesUtils;

public class DynmapHandler {
	private Parties plugin;
	private static final String ADDON_NAME = "Dynmap";
	private static boolean active;
	private static DynmapAPI api;
	private static MarkerAPI markerapi;
	
	private static MarkerSet layer;
	
	public DynmapHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		active = false;
		if (ConfigMain.ADDONS_DYNMAP_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				active = true;
				api = (DynmapAPI) plugin.getServer().getPluginManager().getPlugin(ADDON_NAME);
				markerapi = api.getMarkerAPI();
				
				layer = markerapi.getMarkerSet("Parties");
				
				if (layer != null) {
					if (!layer.getMarkerSetLabel().equals(ConfigMain.ADDONS_DYNMAP_MARKER_LAYER)) {
						layer.setMarkerSetLabel(ConfigMain.ADDONS_DYNMAP_MARKER_LAYER);
					}
				} else {
					layer = markerapi.createMarkerSet("Parties", ConfigMain.ADDONS_DYNMAP_MARKER_LAYER, null, true);
					layer.setHideByDefault(ConfigMain.ADDONS_DYNMAP_HIDEDEFAULT);
				}
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigMain.ADDONS_DYNMAP_ENABLE = false;
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
		layer.createMarker("party_" + partyName, label, true, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), api.getMarkerAPI().getMarkerIcon(ConfigMain.ADDONS_DYNMAP_MARKER_ICON), true);
	}
	
	public static void removeMarker(String partyName) {
		if (active) {
			for (Marker m : layer.getMarkers()) {
				if (m.getMarkerID().equals("party_" + partyName))
					m.deleteMarker();
			}
		}
	}
	
	public static void updatePartyMarker(PartyEntity party) {
		if (active) {
			if (party.getMembers().size() >= ConfigMain.ADDONS_DYNMAP_MINPLAYERS
					&& party.getHome() != null) {
				addMarker(party.getName(), PartiesUtils.convertPartyPlaceholders(ConfigMain.ADDONS_DYNMAP_MARKER_LABEL, party), party.getHome());
			} else {
				removeMarker(party.getName());
			}
		}
	}
}
