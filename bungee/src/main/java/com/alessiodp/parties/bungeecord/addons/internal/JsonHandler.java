package com.alessiodp.parties.bungeecord.addons.internal;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

public class JsonHandler {
	private JsonParser jsonParser;
	
	public JsonHandler() {
		jsonParser = new JsonParser();
	}
	
	public boolean sendMessage(ProxiedPlayer player, String jsonMessage) {
		boolean ret = false;
		try {
			player.sendMessage(ComponentSerializer.parse(jsonMessage));
			ret = true;
		} catch (Exception ignored) {
		}
		return ret;
	}
	
	public boolean isJson(String jsonMessage) {
		boolean ret = false;
		try {
			jsonParser.parse(jsonMessage);
			ret = true;
		} catch (JsonParseException ignored) {}
		return ret;
	}
}
