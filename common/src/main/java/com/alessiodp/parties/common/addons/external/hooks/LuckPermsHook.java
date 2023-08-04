package com.alessiodp.parties.common.addons.external.hooks;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class LuckPermsHook {
	@NotNull private final ADPPlugin plugin;
	private LuckPerms api;
	
	public boolean register() {
		boolean ret = false;
		try {
			api = LuckPermsProvider.get();
			ret = true;
		} catch (IllegalStateException ex) {
			plugin.getLoggerManager().logError(String.format(Constants.DEBUG_ADDON_OUTDATED, "LuckPerms"), ex);
		}
		return ret;
	}
	
	public String getPlayerPrefix(UUID uuid) {
		User user = api.getUserManager().getUser(uuid);
		if (user != null) {
			return user.getCachedData().getMetaData().getPrefix();
		}
		return "";
	}
	
	public String getPlayerSuffix(UUID uuid) {
		User user = api.getUserManager().getUser(uuid);
		if (user != null) {
			return user.getCachedData().getMetaData().getSuffix();
		}
		return "";
	}
}
