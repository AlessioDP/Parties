package com.alessiodp.parties.addons.external;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;

public class EssentialsChatHandler implements Listener {
	private Parties plugin;
	private static final String ADDON_NAME = "EssentialsChat";
	
	public EssentialsChatHandler(Parties parties) {
		plugin = parties;
		init();
	}
	
	private void init() {
		if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
					.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
		}
	}
	
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent event) {
		String old = event.getFormat();
		if (old.toLowerCase().contains("{parties_")) {
			// Bypass useless checks if parties doesn't exist
			boolean somethingChanged = false;
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
			
			Pattern pat = Pattern.compile("\\{parties_([a-z\\_]+)\\}", Pattern.CASE_INSENSITIVE);
			Matcher mat = pat.matcher(old);
			while (mat.find()) {
				String base = mat.group(0);
				String identifier = mat.group(1);
				if (identifier != null) {
					PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
					if (ph != null) {
						old = old.replace(base, ph.formatPlaceholder(pp, party));
						somethingChanged = true;
					}
				}
			}
			
			if(somethingChanged)
				event.setFormat(old);
		}
	}
}
