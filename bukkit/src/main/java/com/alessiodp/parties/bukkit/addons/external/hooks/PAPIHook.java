package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
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
		if (PlaceholderAPI.isRegistered("parties")) {
			PlaceholderAPI.unregisterPlaceholderHook("parties");
		}
		return PlaceholderAPI.registerPlaceholderHook("parties", this);
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
