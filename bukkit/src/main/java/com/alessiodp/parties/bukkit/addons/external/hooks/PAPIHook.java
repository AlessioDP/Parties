package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

@RequiredArgsConstructor
public class PAPIHook extends PlaceholderHook {
	@NonNull private final PartiesPlugin plugin;
	
	public boolean register() {
		boolean ret = false;
		try {
			Class.forName("me.clip.placeholderapi.PlaceholderHook").getMethod("onRequest", OfflinePlayer.class, String.class);
			
			if (PlaceholderAPI.isRegistered(plugin.getPluginFallbackName())) {
				PlaceholderAPI.unregisterPlaceholderHook(plugin.getPluginFallbackName());
			}
			ret = PlaceholderAPI.registerPlaceholderHook(plugin.getPluginFallbackName(), this);
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(Constants.DEBUG_ADDON_OUTDATED
					.replace("{addon}", "PlaceholderAPI"));
		}
		return ret;
	}
	
	public String setPlaceholders(OfflinePlayer player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
		PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyName());
		
		PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
		
		return ph != null ? ph.formatPlaceholder(partyPlayer, party, identifier) : "";
	}
}
