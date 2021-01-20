package com.alessiodp.parties.bukkit.addons.external;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class SkriptHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "Skript";
	private static boolean loaded = false;
	
	public void init() {
		if (BukkitConfigMain.PARTIES_HOOK_INTO_SKRIPT) {
			BukkitConfigMain.PARTIES_HOOK_INTO_SKRIPT = false;
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				try {
					if (!loaded) {
						SkriptAddon addon = Skript.registerAddon((JavaPlugin) plugin.getBootstrap());
						addon.setLanguageFileDirectory("bukkit/skript");
						addon.loadClasses("com.alessiodp.parties.bukkit.addons.external.skript");
						loaded = true;
					}
					
					BukkitConfigMain.PARTIES_HOOK_INTO_SKRIPT = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			}
		}
	}
}
