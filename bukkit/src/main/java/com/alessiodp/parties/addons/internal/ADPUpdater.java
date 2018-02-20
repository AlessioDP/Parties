package com.alessiodp.parties.addons.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.GravityUpdaterHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.utils.ConsoleColor;

import lombok.Getter;

public class ADPUpdater {
	private static Parties plugin;
	
	@Getter private static String foundVersion = "";
	
	public ADPUpdater(Parties instance) {
		plugin = instance;
	}
	
	
	public static void alertPlayers() {
		if (ConfigMain.PARTIES_UPDATES_WARN && !foundVersion.isEmpty()) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				if (p.hasPermission(PartiesPermission.ADMIN_UPDATES.toString())) {
					plugin.getPlayerManager().getPlayer(p.getUniqueId()).sendMessage(Messages.PARTIES_UPDATEAVAILABLE
							.replace("%version%", foundVersion)
							.replace("%thisversion%", plugin.getDescription().getVersion()));
				}
			}
		}
	}
	
	public static void asyncTaskCheckUpdates() {
		if (ConfigMain.PARTIES_UPDATES_CHECK) {
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
				checkUpdates();
			}, 20, 20*60*60*24); // 24 hours
		}
	}
	
	public static void asyncCheckUpdates() {
		if (ConfigMain.PARTIES_UPDATES_CHECK) {
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
				checkUpdates();
			}); // 24 hours
		}
	}
	
	
	private static void checkUpdates() {
		foundVersion = "";
		String version;
		JSONObject data = getVersionInfo();
		if (data != null) {
			version = (String) data.get(Constants.UPDATER_FIELD_VERSION);
		} else {
			// ADP Updater failed
			LoggerManager.log(LogLevel.BASE, Constants.UPDATER_FALLBACK, true);
			version = getUpdatesFromFallback();
		}
		
		if (checkVersion(version, plugin.getDescription().getVersion())) {
			// New version found
			foundVersion = version;
			
			LoggerManager.log(LogLevel.BASE, Constants.UPDATER_FOUND
					.replace("{currentVersion}", plugin.getDescription().getVersion())
					.replace("{newVersion}", foundVersion), true, ConsoleColor.CYAN);
			alertPlayers();
		}
	}
	
	private static String getUpdatesFromFallback() {
		String ret = "";
		GravityUpdaterHandler updater = new GravityUpdaterHandler(plugin,
				Constants.CURSE_PROJECT_ID,
				null,
				GravityUpdaterHandler.UpdateType.NO_DOWNLOAD,
				false);
		if (updater.getResult() == GravityUpdaterHandler.UpdateResult.UPDATE_AVAILABLE) {
			String[] split = updater.getLatestName().split(GravityUpdaterHandler.DELIMETER);
			ret = split[split.length - 1];
		}
		return ret;
	}
	
	private static JSONObject getVersionInfo() {
		JSONObject ret = null;
		try {
			URLConnection conn = new URL(Constants.UPDATER_URL
					.replace("{version}", plugin.getDescription().getVersion())).openConnection();
			conn.setConnectTimeout(10000);
			conn.addRequestProperty("User-Agent", "ADP Updater");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			ret = (JSONObject) JSONValue.parse(br.readLine());
		} catch (IOException ex) {
			LoggerManager.printError(Constants.UPDATER_FAILED_IO);
		} catch (Exception ex) {
			LoggerManager.printError(Constants.UPDATER_FAILED_GENERAL);
		}
		return ret;
	}
	
	private static boolean checkVersion(String ver, String compareWith) {
		boolean ret = false;
		String[] splitVer = splitVersion(ver);
		String[] splitCompareWith = splitVersion(compareWith);
		
		try {
			for (int c=0; c < splitVer.length && !ret; c++) {
				int a = Integer.parseInt(splitVer[c]);
				int b = c < splitCompareWith.length ? Integer.parseInt(splitCompareWith[c]) : 0;
				if (a > b)
					ret = true;
				else if (a < b)
					break;
			}
		} catch (Exception ex) {
			ret = true;
		}
		return ret;
	}
	
	private static String[] splitVersion(String value) {
		value = value.split(Constants.UPDATER_DELIMITER_TYPE)[0];
		return value.split(Constants.UPDATER_DELIMITER_VERSION);
	}
}
