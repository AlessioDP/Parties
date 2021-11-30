package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.AdvancedBanHandler;
import lombok.NonNull;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BukkitAdvancedBanHandler extends AdvancedBanHandler implements Listener {
	
	public BukkitAdvancedBanHandler(@NonNull PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void registerListener() {
		((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, (BukkitPartiesBootstrap) plugin.getBootstrap());
	}
	
	@EventHandler
	public void onPlayerBan(PunishmentEvent event) {
		onBan(event.getPunishment());
	}
}
