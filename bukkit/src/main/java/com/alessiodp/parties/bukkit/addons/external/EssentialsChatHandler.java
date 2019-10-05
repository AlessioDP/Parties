package com.alessiodp.parties.bukkit.addons.external;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class EssentialsChatHandler implements Listener {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "EssentialsChat";
	private final Pattern PATTERN = Pattern.compile("\\{parties_([a-z_]+)}", Pattern.CASE_INSENSITIVE);
	
	public void init() {
		if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
			((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, ((BukkitPartiesBootstrap) plugin.getBootstrap()));
			
			plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
					.replace("{addon}", ADDON_NAME), true);
		}
	}
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent event) {
		String old = event.getFormat();
		if (old.toLowerCase(Locale.ENGLISH).contains("{parties_")) {
			// Bypass useless checks if this isn't an Parties placeholder
			boolean somethingChanged = false;
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyName());
			
			Matcher mat = PATTERN.matcher(old);
			while (mat.find()) {
				String base = mat.group(0);
				String identifier = mat.group(1);
				if (identifier != null) {
					identifier = identifier.toLowerCase(Locale.ENGLISH);
					PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder(identifier);
					if (placeholder != null) {
						String parsed = placeholder.formatPlaceholder(partyPlayer, party, identifier);
						if (parsed != null) {
							old = old.replace(base, plugin.getColorUtils().convertColors(parsed));
							somethingChanged = true;
						}
					}
				}
			}
			
			if(somethingChanged)
				event.setFormat(old);
		}
	}
}