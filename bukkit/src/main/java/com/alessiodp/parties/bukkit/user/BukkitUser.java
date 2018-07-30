package com.alessiodp.parties.bukkit.user;

import com.alessiodp.parties.bukkit.addons.internal.json.BukkitJsonHandler;
import com.alessiodp.parties.bukkit.addons.internal.json.JsonHandler;
import com.alessiodp.parties.bukkit.addons.internal.json.SpigotJsonHandler;
import com.alessiodp.parties.common.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitUser implements User {
	private CommandSender sender;
	private JsonHandler jsonHandler;
	
	public BukkitUser(CommandSender commandSender) {
		sender = commandSender;
		if (isSpigot())
			jsonHandler = new SpigotJsonHandler();
		else
			jsonHandler = new BukkitJsonHandler();
	}
	
	// Used to check if is running Spigot
	private boolean isSpigot() {
		boolean ret = false;
		try {
			Class.forName("net.md_5.bungee.chat.ComponentSerializer");
			ret = true;
		} catch (ClassNotFoundException ignored) {}
		return ret;
	}
	
	@Override
	public UUID getUUID() {
		return isPlayer() ? ((Player) sender).getUniqueId() : null;
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return sender.hasPermission(permission);
	}
	
	@Override
	public boolean isPlayer() {
		return (sender instanceof Player);
	}
	
	@Override
	public String getName() {
		return sender.getName();
	}
	
	@Override
	public void sendMessage(String message, boolean colorTranslation) {
		if (isPlayer() && jsonHandler.isJson(message))
			jsonHandler.sendMessage((Player) sender, message);
		else
			sender.sendMessage(colorTranslation ? ChatColor.translateAlternateColorCodes('&', message) : message);
	}
	
	@Override
	public void chat(String messageToSend) {
		if (isPlayer())
			((Player) sender).chat(messageToSend);
	}
}
