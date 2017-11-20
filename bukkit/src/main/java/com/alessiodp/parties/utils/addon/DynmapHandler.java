package com.alessiodp.parties.utils.addon;

import org.bukkit.Location;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;

public class DynmapHandler {
	Parties plugin;
	DynmapAPI api;
	MarkerAPI markerapi;
	
	MarkerSet layer;
	
	public DynmapHandler(Parties instance) {
		plugin = instance;
		api = (DynmapAPI) plugin.getServer().getPluginManager().getPlugin("dynmap");
		markerapi = api.getMarkerAPI();
		
		layer = markerapi.getMarkerSet("Parties");
		
		if (layer != null) {
			if (!layer.getMarkerSetLabel().equals(Variables.dynmap_marker_layer)) {
				layer.setMarkerSetLabel(Variables.dynmap_marker_layer);
			}
		} else {
			layer = markerapi.createMarkerSet("Parties", Variables.dynmap_marker_layer, null, true);
			layer.setHideByDefault(Variables.dynmap_hidedefault);
		}
		
	}
	
	public void addMarker(String partyname, String label, Location loc) {
		for (Marker m : layer.getMarkers()) {
			if (m.getMarkerID().equals("party_"+partyname))
				m.deleteMarker();
		}
		layer.createMarker("party_" + partyname, label, true, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), api.getMarkerAPI().getMarkerIcon(Variables.dynmap_marker_icon), true);
	}
	public void removeMarker(String id) {
		for (Marker m : layer.getMarkers()) {
			if (m.getMarkerID().equals("party_"+id))
				m.deleteMarker();
		}
	}
}
