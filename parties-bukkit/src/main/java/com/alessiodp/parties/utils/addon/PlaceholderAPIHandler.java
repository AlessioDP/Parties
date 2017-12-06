package com.alessiodp.parties.utils.addon;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.enums.PartiesPlaceholder;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PlaceholderAPIHandler extends EZPlaceholderHook {
	Parties plugin;
	
	public PlaceholderAPIHandler(Parties instance) {
		super(instance, "parties");
		plugin = instance;
	}

	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
			Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
			
			PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
			
			return ph != null ? ph.formatPlaceholder(tp, party) : "";
	}

}
