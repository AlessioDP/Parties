package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PAPIHook extends PlaceholderExpansion {
	@NotNull private final PartiesPlugin plugin;
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public @NotNull String getName() {
		return plugin.getPluginName();
	}
	
	@Override
	public @NotNull String getIdentifier() {
		return "parties";
	}
	
	@Override
	public @NotNull String getAuthor() {
		return "AlessioDP";
	}
	
	@Override
	public @NotNull String getVersion() {
		return plugin.getVersion();
	}
	
	@Override
	public boolean persist(){
		return true;
	}
	
	@Override
	public @NotNull List<String> getPlaceholders() {
		List<String> ret = new ArrayList<>();
		for (PartiesPlaceholder placeholder : PartiesPlaceholder.values()) {
			ret.add("%" + getIdentifier() + "_" + placeholder.getSyntax() + "%");
		}
		return ret;
	}
	
	public String parsePlaceholders(OfflinePlayer player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	@Override
	public String onRequest(OfflinePlayer offlinePlayer, @NotNull String identifier) {
		if (offlinePlayer != null) {
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
			PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
			
			PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder(identifier);
			
			return placeholder != null ? placeholder.formatPlaceholder(partyPlayer, party, identifier) : null;
		}
		return null;
	}
}
