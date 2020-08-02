package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.PlaceholderAPI;

@RequiredArgsConstructor
public class PAPIHook extends PlaceholderExpansion {
	@NonNull private final PartiesPlugin plugin;
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public String getName() {
		return plugin.getPluginName();
	}
	
	@Override
	public String getIdentifier() {
		return "parties";
	}
	
	@Override
	public String getAuthor() {
		return "AlessioDP";
	}
	
	@Override
	public String getVersion() {
		return plugin.getVersion();
	}
	
	@Override
	public boolean persist(){
		return true;
	}
	
	public String parsePlaceholders(OfflinePlayer player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
		PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyName());
		
		PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
		
		return ph != null ? ph.formatPlaceholder(partyPlayer, party, identifier) : null;
	}
}
