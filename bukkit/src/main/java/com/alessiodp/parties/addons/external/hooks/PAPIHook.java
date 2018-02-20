package com.alessiodp.parties.addons.external.hooks;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class PAPIHook extends PlaceholderHook {
	private Parties plugin;
	
	public PAPIHook(Parties instance) {
		plugin = instance;
	}
	
	public boolean register() {
		if (PlaceholderAPI.isRegistered("parties")) {
			PlaceholderAPI.unregisterPlaceholderHook("parties");
		}
		return PlaceholderAPI.registerPlaceholderHook("parties", this);
	}
	
	public String setPlaceholders(Player player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
			PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
			
			PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
			
			return ph != null ? ph.formatPlaceholder(pp, party) : "";
	}
}
