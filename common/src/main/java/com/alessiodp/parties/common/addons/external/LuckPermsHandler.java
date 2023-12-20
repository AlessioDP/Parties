package com.alessiodp.parties.common.addons.external;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.addons.external.hooks.LuckPermsHook;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuckPermsHandler {
	private static ADPPlugin plugin;
	private static final String ADDON_NAME = "LuckPerms";
	private static boolean active;
	private static LuckPermsHook hook;
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(%luckperms_[^%]+%)", Pattern.CASE_INSENSITIVE);
	
	public LuckPermsHandler(@NotNull ADPPlugin adpPlugin) {
		plugin = adpPlugin;
	}
	
	public void init() {
		active = false;
		if (plugin.isPluginEnabled(ADDON_NAME)) {
			hook = new LuckPermsHook(plugin);
			if (hook.register()) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			}
		}
	}
	
	@NotNull
	public static String parsePlaceholders(@NotNull String text, @NotNull PartyPlayerImpl player) {
		String ret = text;
		if (active) {
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
			while (matcher.find()) {
				String identifier = matcher.group(1);
				switch (CommonUtils.toLowerCase(identifier)) {
					case "%luckperms_prefix%":
						ret = ret.replace(identifier, hook.getPlayerPrefix(player.getPlayerUUID()));
						break;
					case "%luckperms_suffix%":
						ret = ret.replace(identifier, hook.getPlayerSuffix(player.getPlayerUUID()));
						break;
					default: // Nothing to do
				}
			}
		}
		return ret;
	}
}
