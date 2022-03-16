package com.alessiodp.parties.common.addons.external.hooks;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.lastloginapi.api.LastLogin;
import com.alessiodp.lastloginapi.api.interfaces.LastLoginAPI;
import com.alessiodp.lastloginapi.api.interfaces.LastLoginPlayer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class LLAPIHook {
	@NotNull private final ADPPlugin plugin;
	private LastLoginAPI api;
	
	public boolean register() {
		boolean ret = false;
		try {
			api = LastLogin.getApi();
			ret = true;
		} catch (Exception ex) {
			plugin.getLoggerManager().logError(String.format(Constants.DEBUG_ADDON_OUTDATED, "LastLoginAPI"), ex);
		}
		return ret;
	}
	
	public String getPlayerName(UUID uuid) {
		return api.getPlayer(uuid).getName();
	}
	
	public Set<UUID> getPlayerByName(String name) {
		Set<UUID> ret = new HashSet<>();
		Set<? extends LastLoginPlayer> players = api.getPlayerByName(name);
		for (LastLoginPlayer pl : players) {
			ret.add(pl.getPlayerUUID());
		}
		return ret;
	}
}
