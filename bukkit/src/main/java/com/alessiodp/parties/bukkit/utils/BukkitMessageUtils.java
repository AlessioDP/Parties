package com.alessiodp.parties.bukkit.utils;

import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.MessageUtils;
import lombok.NonNull;

public class BukkitMessageUtils extends MessageUtils {
	
	public BukkitMessageUtils(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public String convertPlayerPlaceholders(String message, PartyPlayerImpl player, String emptyPlaceholder) {
		String ret = super.convertPlayerPlaceholders(message, player, emptyPlaceholder);
		if (player != null) {
			ret = PlaceholderAPIHandler.getPlaceholders(player.getPlayerUUID(), ret);
		}
		return ret;
	}
}
