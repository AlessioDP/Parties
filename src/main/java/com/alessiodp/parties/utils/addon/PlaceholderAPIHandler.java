package com.alessiodp.parties.utils.addon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PlaceholderAPIHandler extends EZPlaceholderHook{
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
				return plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(p).getRank()).getName();
				
			case "rank_formatted":
				return ChatColor.translateAlternateColorCodes('&', plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(p).getRank()).getChat());
				
			case "party":
				tp = plugin.getPlayerHandler().getThePlayer(p);
				if(!tp.haveParty())
					return "";
				return ChatColor.translateAlternateColorCodes('&', Variables.party_placeholder).replace("%party%", tp.getPartyName());
				
			case "desc":
				tp = plugin.getPlayerHandler().getThePlayer(p);
				if(!tp.haveParty())
					return "";
				party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				if(party == null)
					return "";
				return party.getDescription();
				
			case "motd":
				tp = plugin.getPlayerHandler().getThePlayer(p);
				if(!tp.haveParty())
					return "";
				party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				if(party == null)
					return "";
				return party.getMOTD();
				
			case "prefix":
				tp = plugin.getPlayerHandler().getThePlayer(p);
				if(!tp.haveParty())
					return "";
				party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				if(party == null)
					return "";
				return party.getPrefix();
				
			case "suffix":
				tp = plugin.getPlayerHandler().getThePlayer(p);
				if(!tp.haveParty())
					return "";
				party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				if(party == null)
					return "";
				return party.getSuffix();
				
			case "kills":
				tp = plugin.getPlayerHandler().getThePlayer(p);
				if(!tp.haveParty())
					return "";
				party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				if(party == null)
					return "";
				return party.getKills()+"";							
			}
		return null;
	}

}
