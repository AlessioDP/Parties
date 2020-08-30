package com.alessiodp.parties.common.addons.external.hooks;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.lastloginapi.api.LastLogin;
import com.alessiodp.lastloginapi.api.interfaces.LastLoginAPI;
import com.alessiodp.lastloginapi.api.interfaces.LastLoginPlayer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class LLAPIHook {
	@NonNull private final ADPPlugin plugin;
	private LastLoginAPI api;
	
	public boolean register() {
		boolean ret = false;
		try {
			api = LastLogin.getApi();
			ret = true;
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(Constants.DEBUG_ADDON_OUTDATED
					.replace("{addon}", "LastLoginAPI"));
			ex.printStackTrace();
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
