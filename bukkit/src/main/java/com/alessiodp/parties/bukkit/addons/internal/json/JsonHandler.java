package com.alessiodp.parties.bukkit.addons.internal.json;

import org.bukkit.entity.Player;

public interface JsonHandler {
	boolean sendMessage(Player player, String jsonMessage);
	boolean isJson(String jsonMessage);
}
