package com.alessiodp.parties.addons.external;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class PlaceholderAPIHandler {
	private Parties plugin;
	private static final String ADDON_NAME = "PlaceholderAPI";
	private static boolean active;
	
	public PlaceholderAPIHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	public void init() {
		active = false;
		if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			if (PlaceholderAPI.registerPlaceholderHook("parties", new PAPIHook())) {
				active = true;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			}
		}
	}
	
	public static String getPlaceholders(Player player, String message) {
		String ret = message;
		if (active)
			ret = PlaceholderAPI.setPlaceholders(player, message);
		return ret;
	}
	
	
	public class PAPIHook extends PlaceholderHook {
		@Override
		public String onPlaceholderRequest(Player p, String identifier) {
				PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
				PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
				
				PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
				
				return ph != null ? ph.formatPlaceholder(pp, party) : "";
		}
	}
}
