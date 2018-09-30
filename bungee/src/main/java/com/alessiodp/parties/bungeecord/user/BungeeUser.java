package com.alessiodp.parties.bungeecord.user;

import com.alessiodp.parties.bungeecord.addons.internal.JsonHandler;
import com.alessiodp.parties.common.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeUser implements User {
	private CommandSender sender;
	private JsonHandler jsonHandler;
	
	public BungeeUser(CommandSender commandSender) {
		sender = commandSender;
		jsonHandler = new JsonHandler();
	}
	
	@Override
	public UUID getUUID() {
		return isPlayer() ? ((ProxiedPlayer)sender).getUniqueId() : null;
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return sender.hasPermission(permission);
	}
	
	@Override
	public boolean isPlayer() {
		return (sender instanceof ProxiedPlayer);
	}
	
	@Override
	public String getName() {
		return sender.getName();
	}
	
	@Override
	public void sendMessage(String message, boolean colorTranslation) {
		if (isPlayer() && jsonHandler.isJson(message))
			jsonHandler.sendMessage((ProxiedPlayer) sender, message);
		else
			sender.sendMessage(TextComponent.fromLegacyText(colorTranslation ? ChatColor.translateAlternateColorCodes('&', message) : message));
	}
	
	@Override
	public void chat(String messageToSend) {
		if (isPlayer())
			((ProxiedPlayer) sender).chat(messageToSend);
	}
	
	public String getServerName() {
		return ((ProxiedPlayer) sender).getServer().getInfo().getName();
	}
	
	public void connectTo(ServerInfo server) {
		((ProxiedPlayer) sender).connect(server);
	}
}
