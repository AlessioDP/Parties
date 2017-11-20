package com.alessiodp.parties.utils.addon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
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
			ThePlayer tp;
			Party party;
			/*
			 * Placeholders: %parties_IDENTIFIER%
			 */
			switch (identifier) {
			case "rank":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				return plugin.getPartyHandler().searchRank(tp.getRank()).getName();
			case "rank_formatted":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				return ChatColor.translateAlternateColorCodes('&', plugin.getPartyHandler().searchRank(tp.getRank()).getChat());
			case "party":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				if (!tp.getPartyName().isEmpty())
					return ChatColor.translateAlternateColorCodes('&', tp.convertPartyText(Variables.party_placeholder, tp.getPlayer(), null));
				return "";
				
			case "desc":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				if (!tp.getPartyName().isEmpty()) {
					party = plugin.getPartyHandler().getParty(tp.getPartyName());
					if (party != null)
						return party.getDescription();
				}
				return "";
			case "motd":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				if (!tp.getPartyName().isEmpty()) {
					party = plugin.getPartyHandler().getParty(tp.getPartyName());
					if (party != null)
						return party.getMOTD();
				}
				return "";
			case "prefix":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				if (!tp.getPartyName().isEmpty()) {
					party = plugin.getPartyHandler().getParty(tp.getPartyName());
					if (party != null)
						return party.getPrefix();
				}
				return "";
			case "suffix":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				if (!tp.getPartyName().isEmpty()) {
					party = plugin.getPartyHandler().getParty(tp.getPartyName());
					if (party != null)
						return party.getSuffix();
				}
				return "";
			case "kills":
				tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
				if (!tp.getPartyName().isEmpty()) {
					party = plugin.getPartyHandler().getParty(tp.getPartyName());
					if (party != null)
						return Integer.toString(party.getKills());
				}
				return "";
			}
		return null;
	}

}
