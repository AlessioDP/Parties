package com.alessiodp.parties.addons.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JSONHandler {
	private static String serverVersion;
	
	private static String getServerVersion() throws Exception {
		if (serverVersion == null)
			serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		return serverVersion;
	}
	
	public static boolean isJSON(String str) {
		try {
			Class.forName("net.minecraft.server." + getServerVersion() + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static void sendJSON(String str, Player player) {
		try {
			String ver = getServerVersion();
			Object parsedMessage = Class.forName("net.minecraft.server." + ver + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, str);
			Object packetPlayOutChat = Class.forName("net.minecraft.server." + ver + ".PacketPlayOutChat").getConstructor(Class.forName("net.minecraft.server." + ver + ".IChatBaseComponent")).newInstance(parsedMessage);
			
			Object craftPlayer = Class.forName("org.bukkit.craftbukkit." + ver + ".entity.CraftPlayer").cast(player);
			Object craftHandle = Class.forName("org.bukkit.craftbukkit." + ver + ".entity.CraftPlayer").getMethod("getHandle").invoke(craftPlayer);
			Object playerConnection = Class.forName("net.minecraft.server." + ver + ".EntityPlayer").getField("playerConnection").get(craftHandle);
			
			Class.forName("net.minecraft.server." + ver + ".PlayerConnection").getMethod("sendPacket", Class.forName("net.minecraft.server." + ver + ".Packet")).invoke(playerConnection, packetPlayOutChat);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
