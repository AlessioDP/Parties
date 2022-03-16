package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.AdvancedBanHandler;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class BungeeAdvancedBanHandler extends AdvancedBanHandler implements Listener {
	
	public BungeeAdvancedBanHandler(@NotNull PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected boolean isEnabled() {
		return BungeeConfigMain.ADDITIONAL_MODERATION_PLUGINS_ADVANCEDBAN;
	}
	
	@Override
	public void registerListener() {
		((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPluginManager().registerListener((BungeePartiesBootstrap) plugin.getBootstrap(), this);
	}
	
	@EventHandler
	public void onPlayerBan(PunishmentEvent event) {
		onBan(event.getPunishment());
	}
}
