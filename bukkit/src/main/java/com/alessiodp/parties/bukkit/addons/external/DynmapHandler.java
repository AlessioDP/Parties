package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DynmapHandler {
	private static PartiesPlugin plugin = null;
	private static final String ADDON_NAME = "Dynmap";
	private static boolean active;
	
	private static DynmapCommonAPI api;
	private static MarkerSet layer;
	
	public DynmapHandler(@NotNull PartiesPlugin parties) {
		plugin = parties;
	}
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDONS_DYNMAP_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				api = (DynmapCommonAPI) Bukkit.getPluginManager().getPlugin(ADDON_NAME);
				if (api != null) {
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
					
					plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
				}
				active = true;
			}
			
			if (!active) {
				BukkitConfigMain.ADDONS_DYNMAP_ENABLE = false;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
			}
		}
	}
	
	
	private static void addMarker(String id, String label, Location loc) {
		if (loc.getWorld() != null) { // Check if the world is loaded
			layer.createMarker(id,
					label,
					true,
					loc.getWorld().getName(),
					loc.getX(),
					loc.getY(),
					loc.getZ(),
					api.getMarkerAPI().getMarkerIcon(BukkitConfigMain.ADDONS_DYNMAP_MARKER_ICON),
					true
			);
		}
	}
	
	public static void cleanupMarkers(PartyImpl party) {
		if (active) {
			for (Marker m : layer.getMarkers()) {
				if (m.getMarkerID().startsWith("party_" + party.getId()))
					m.deleteMarker();
			}
		}
	}
	
	public static void updatePartyMarker(PartyImpl party) {
		if (active) {
			cleanupMarkers(party);
			if (party.getMembers().size() >= BukkitConfigMain.ADDONS_DYNMAP_MINPLAYERS) {
				for (PartyHome home : party.getHomes()) {
					if (!plugin.isBungeeCordEnabled()
							|| home.getServer()== null
							|| home.getServer().equalsIgnoreCase(BukkitConfigMain.PARTIES_BUNGEECORD_SERVER_ID)) {
						Location loc = new Location(
								Bukkit.getWorld(home.getWorld()),
								home.getX(),
								home.getY(),
								home.getZ(),
								home.getYaw(),
								home.getPitch()
						);
						String id = party.getHomes().size() > 1 ?
								"party_" + party.getId() + "_" + home.getName()
								: "party_" + party.getId();
						
						addMarker(
								plugin.getMessageUtils().convertPlaceholders(id, null, party),
								plugin.getMessageUtils().convertPlaceholders(
										party.getHomes().size() > 1
												? BukkitConfigMain.ADDONS_DYNMAP_MARKER_LABEL_MULTIPLE
												.replace("%home%", CommonUtils.getNoEmptyOr(home.getName(), ""))
												: BukkitConfigMain.ADDONS_DYNMAP_MARKER_LABEL_SINGLE,
										null,
										party
								), loc
						);
					}
				}
			}
		}
	}
	
	public static void updatePartyMarker(UUID partyId) {
		if (active) {
			PartyImpl party = plugin.getPartyManager().getParty(partyId);
			if (party != null) {
				updatePartyMarker(party);
			}
		}
	}
}
