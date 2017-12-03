package com.alessiodp.parties.utils.addon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

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
			/*
			 * Placeholders: %parties_IDENTIFIER%
			 */
			String ret = "";
			switch (identifier) {
			case "color_name":
				if (party != null && party.getColor() != null)
					ret = party.getColor().getName();
				break;
			case "color_command":
				if (party != null && party.getColor() != null)
					ret = party.getColor().getCommand();
				break;
			case "color_code":
				if (party != null && party.getColor() != null)
					ret = party.getColor().getCode();
				break;
			case "desc":
				if (party != null)
					ret = party.getDescription();
				break;
			case "kills":
				if (party != null)
					ret = Integer.toString(party.getKills());
				break;
			case "motd":
				if (party != null)
					ret = party.getMOTD();
				break;
			case "party":
				if (party != null)
					ret = party.getName();
				break;
			case "prefix":
				if (party != null)
					ret = party.getPrefix();
				break;
			case "rank":
				ret = plugin.getPartyHandler().searchRank(tp.getRank()).getName();
				break;
			case "rank_formatted":
				ret = ChatColor.translateAlternateColorCodes('&', plugin.getPartyHandler().searchRank(tp.getRank()).getChat());
				break;
			case "suffix":
				if (party != null)
					ret = party.getSuffix();
				break;
			}
		return ret;
	}

}
