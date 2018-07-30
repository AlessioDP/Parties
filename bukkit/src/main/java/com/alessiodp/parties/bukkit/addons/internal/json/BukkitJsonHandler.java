package com.alessiodp.parties.bukkit.addons.internal.json;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitJsonHandler implements JsonHandler {
	private static String serverVersion;
	
	public BukkitJsonHandler() {
		serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	public static boolean isJSON(String str) {
		try {
			Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean sendMessage(Player player, String jsonMessage) {
		boolean ret = false;
		try {
			String nms = "net.minecraft.server." + serverVersion;
			String obc = "org.bukkit.craftbukkit." + serverVersion;
			
			Object parsedMessage = Class.forName(nms + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, jsonMessage);
			Object packetPlayOutChat = Class.forName(nms + ".PacketPlayOutChat")
					.getConstructor(Class.forName(nms + ".IChatBaseComponent")).newInstance(parsedMessage);
			
			Object craftPlayer = Class.forName(obc + ".entity.CraftPlayer").cast(player);
			Object craftHandle = Class.forName(obc + ".entity.CraftPlayer").getMethod("getHandle").invoke(craftPlayer);
			Object playerConnection = Class.forName(nms + ".EntityPlayer").getField("playerConnection").get(craftHandle);
			
			Class.forName(nms + ".PlayerConnection").getMethod("sendPacket",
					Class.forName(nms + ".Packet")).invoke(playerConnection, packetPlayOutChat);
			ret = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public boolean isJson(String jsonMessage) {
		boolean ret = false;
		try {
			Class.forName("net.minecraft.server." + serverVersion + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, jsonMessage);
			ret = true;
		} catch (Exception ignored) {}
		return ret;
	}
}
