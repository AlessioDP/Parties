package com.alessiodp.parties.utils.addon;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class EssentialsChatHandler implements Listener {
	Parties plugin;
	
	public EssentialsChatHandler(Parties parties) {
		plugin = parties;
	}
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent event) {
		String old = event.getFormat();
		if (old.contains("{PARTIES_")) {
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
			if (!tp.getPartyName().isEmpty()) {
				Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
				old = old.replaceAll("\\{PARTIES_PARTY\\}", party.convertText(Variables.party_placeholder, tp.getPlayer()))
						.replaceAll("\\{PARTIES_RANK\\}", plugin.getPartyHandler().searchRank(tp.getRank()).getName())
						.replaceAll("\\{PARTIES_RANK_FORMATTED\\}", ChatColor.translateAlternateColorCodes('&', plugin.getPartyHandler().searchRank(tp.getRank()).getChat()))
						.replaceAll("\\{PARTIES_DESC\\}", party.getDescription())
						.replaceAll("\\{PARTIES_MOTD\\}", party.getMOTD())
						.replaceAll("\\{PARTIES_PREFIX\\}", party.getPrefix())
						.replaceAll("\\{PARTIES_SUFFIX\\}", party.getSuffix())
						.replaceAll("\\{PARTIES_KILLS\\}", Integer.toString(party.getKills()));
				old = ChatColor.translateAlternateColorCodes('&', old);
			} else {
				old = old.replaceAll("\\{PARTIES_PARTY\\}", "")
						.replaceAll("\\{PARTIES_RANK\\}", "")
						.replaceAll("\\{PARTIES_RANK_FORMATTED\\}", "")
						.replaceAll("\\{PARTIES_DESC\\}", "")
						.replaceAll("\\{PARTIES_MOTD\\}", "")
						.replaceAll("\\{PARTIES_PREFIX\\}", "")
						.replaceAll("\\{PARTIES_SUFFIX\\}", "")
						.replaceAll("\\{PARTIES_KILLS\\}", "");
			}
			event.setFormat(old);
		}
	}
}
