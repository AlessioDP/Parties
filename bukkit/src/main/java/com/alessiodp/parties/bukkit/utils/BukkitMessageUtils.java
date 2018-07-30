package com.alessiodp.parties.bukkit.utils;

import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.MessageUtils;
import org.bukkit.ChatColor;

public class BukkitMessageUtils extends MessageUtils {
	
	public BukkitMessageUtils(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public String convertColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String convertPlayerPlaceholders(String message, PartyPlayerImpl player) {
		String ret = super.convertPlayerPlaceholders(message, player);
		ret = PlaceholderAPIHandler.getPlaceholders(player.getPlayerUUID(), ret);
		return ret;
	}
}
