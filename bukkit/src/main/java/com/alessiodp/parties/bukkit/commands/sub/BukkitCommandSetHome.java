package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.common.commands.sub.CommandSetHome;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandSetHome extends CommandSetHome {
	
	public BukkitCommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void getLocationAndSave(@NotNull PartyPlayerImpl sender, @NotNull PartyImpl party, @NotNull String name) {
		Player bukkitPlayer = Bukkit.getPlayer(sender.getPlayerUUID());
		if (bukkitPlayer != null) {
			savePartyHome(party, getHomeLocationOfPlayer(sender, name, ""));
		}
	}
	
	public static PartyHomeImpl getHomeLocationOfPlayer(PartyPlayerImpl sender, String name, String server) {
		Player bukkitPlayer = Bukkit.getPlayer(sender.getPlayerUUID());
		if (bukkitPlayer != null) {
			Location location = bukkitPlayer.getLocation();
			return new PartyHomeImpl(
					name,
					location.getWorld() != null ? location.getWorld().getName() : "",
					location.getX(),
					location.getY(),
					location.getZ(),
					location.getYaw(),
					location.getPitch(),
					server
			);
		}
		return null;
	}
}