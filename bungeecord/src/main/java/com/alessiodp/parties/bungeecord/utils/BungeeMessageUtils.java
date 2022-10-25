package com.alessiodp.parties.bungeecord.utils;

import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.bungeecord.addons.external.LuckPermsHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.MessageUtils;

import java.util.regex.Matcher;

public class BungeeMessageUtils extends MessageUtils {
	
	public BungeeMessageUtils(PartiesPlugin plugin) {
		super(plugin);
	}

	@Override
	public String convertPlaceholders(String message, PartyPlayerImpl player, PartyImpl party) {
		String ret = super.convertPlaceholders(message, player, party);
		String prefix = LuckPermsHandler.getPlayerPrefix(player.getPlayerUUID());
		if (!prefix.isEmpty()) {
			String replacement;
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
			while (matcher.find()) {
				String identifier = matcher.group(1);
				// Nothing to do
				if ("%prefix%".equalsIgnoreCase(CommonUtils.toLowerCase(identifier))) {
					replacement = prefix;
					ret = ret.replace(identifier, replacement);
				}
			}
		}
		return ret;
	}
}
