package com.alessiodp.parties.utils.addon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class EssentialsChatHandler implements Listener{
	Parties plugin;
	
	public EssentialsChatHandler(Parties parties){
		plugin = parties;
	}
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent event){
		String old = event.getFormat();
		if(old.contains("{PARTIES_")){
			ThePlayer tp = plugin.getPlayerHandler().getThePlayer(event.getPlayer());
			if(tp.haveParty()){
				Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				old = old.replaceAll("\\{PARTIES_PARTY\\}", tp.getPartyName())
						.replaceAll("\\{PARTIES_RANK\\}", plugin.getPartyHandler().searchRank(tp.getRank()).getName())
						.replaceAll("\\{PARTIES_RANK_FORMATTED\\}", plugin.getPartyHandler().searchRank(tp.getRank()).getChat())
						.replaceAll("\\{PARTIES_DESC\\}", party.getDescription())
						.replaceAll("\\{PARTIES_MOTD\\}", party.getMOTD())
						.replaceAll("\\{PARTIES_PREFIX\\}", party.getPrefix())
						.replaceAll("\\{PARTIES_SUFFIX\\}", party.getSuffix())
						.replaceAll("\\{PARTIES_KILLS\\}", party.getKills()+"");
			}else{
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
