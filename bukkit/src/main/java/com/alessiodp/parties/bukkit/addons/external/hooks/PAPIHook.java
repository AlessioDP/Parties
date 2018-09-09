package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class PAPIHook extends PlaceholderHook {
	private PartiesPlugin plugin;
	
	public PAPIHook(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public boolean register() {
		boolean ret = false;
		try {
			Class.forName("me.clip.placeholderapi.PlaceholderHook").getMethod("onRequest", OfflinePlayer.class, String.class);
			
			if (PlaceholderAPI.isRegistered("parties")) {
				PlaceholderAPI.unregisterPlaceholderHook("parties");
			}
			ret = PlaceholderAPI.registerPlaceholderHook("parties", this);
		} catch (Exception ex) {
			LoggerManager.printError(Constants.DEBUG_LIB_GENERAL_OUTDATED
					.replace("{addon}", "PlaceholderAPI"));
		}
		return ret;
	}
	
	public String setPlaceholders(OfflinePlayer player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onRequest(OfflinePlayer p, String identifier) {
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
			PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
			
			PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
			
			return ph != null ? ph.formatPlaceholder(pp, party) : "";
	}
}
