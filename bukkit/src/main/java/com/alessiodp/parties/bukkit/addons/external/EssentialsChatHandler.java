package com.alessiodp.parties.bukkit.addons.external;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.ConsoleColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class EssentialsChatHandler implements Listener {
	private BukkitPartiesPlugin plugin;
	private static final String ADDON_NAME = "EssentialsChat";
	
	public EssentialsChatHandler(BukkitPartiesPlugin parties) {
		plugin = parties;
	}
	
	public void init() {
		if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
			plugin.getBootstrap().getServer().getPluginManager().registerEvents(this, plugin.getBootstrap());
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
					.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
		}
	}
	
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent event) {
		String old = event.getFormat();
		if (old.toLowerCase().contains("{parties_")) {
			// Bypass useless checks if this isn't a Parties placeholder
			boolean somethingChanged = false;
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
			
			Pattern pat = Pattern.compile("\\{parties_([a-z_]+)}", Pattern.CASE_INSENSITIVE);
			Matcher mat = pat.matcher(old);
			while (mat.find()) {
				String base = mat.group(0);
				String identifier = mat.group(1);
				if (identifier != null) {
					PartiesPlaceholder ph = PartiesPlaceholder.getPlaceholder(identifier);
					if (ph != null) {
						old = old.replace(base, ChatColor.translateAlternateColorCodes('&', ph.formatPlaceholder(pp, party)));
						somethingChanged = true;
					}
				}
			}
			
			if(somethingChanged)
				event.setFormat(old);
		}
	}
}
