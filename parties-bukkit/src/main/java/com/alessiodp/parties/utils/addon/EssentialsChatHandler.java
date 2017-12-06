package com.alessiodp.parties.utils.addon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.enums.PartiesPlaceholder;

public class EssentialsChatHandler implements Listener {
	Parties plugin;
	
	public EssentialsChatHandler(Parties parties) {
		plugin = parties;
	}
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent event) {
		String old = event.getFormat();
		if (old.toLowerCase().contains("{parties_")) {
			// Bypass useless checks if parties doesn't exist
			boolean somethingChanged = false;
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
			Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
			
			Pattern pat = Pattern.compile("\\{parties_([a-z\\_]+)\\}", Pattern.CASE_INSENSITIVE);
			Matcher mat = pat.matcher(old);
			while (mat.find()) {
				String base = mat.group(0);
				String identifier = mat.group(1);
				if (identifier != null) {
					PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
					if (ph != null) {
						old = old.replace(base, ph.formatPlaceholder(tp, party));
						somethingChanged = true;
					}
				}
			}
			
			if(somethingChanged)
				event.setFormat(old);
		}
	}
}
