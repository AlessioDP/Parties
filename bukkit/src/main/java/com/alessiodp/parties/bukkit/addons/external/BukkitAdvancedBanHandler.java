package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.AdvancedBanHandler;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class BukkitAdvancedBanHandler extends AdvancedBanHandler implements Listener {
	
	public BukkitAdvancedBanHandler(@NotNull PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected boolean isEnabled() {
		return BukkitConfigMain.ADDITIONAL_MODERATION_PLUGINS_ADVANCEDBAN;
	}
	
	@Override
	public void registerListener() {
		((BukkitPartiesBootstrap) partiesPlugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, (BukkitPartiesBootstrap) partiesPlugin.getBootstrap());
	}
	
	@EventHandler
	public void onPlayerBan(PunishmentEvent event) {
		onBan(event.getPunishment());
	}
}
