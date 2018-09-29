package com.alessiodp.parties.bungeecord.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;

public class BungeeMessageUtils extends MessageUtils {
	
	public BungeeMessageUtils(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public String convertColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
