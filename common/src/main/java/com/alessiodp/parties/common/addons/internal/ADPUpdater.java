package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.ConsoleColor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ADPUpdater {
	private static PartiesPlugin plugin;
	
	@Getter private static String foundVersion = "";
	
	public ADPUpdater(PartiesPlugin instance) {
		plugin = instance;
	}
	
	
	private static void alertPlayers() {
		if (ConfigMain.PARTIES_UPDATES_WARN && !foundVersion.isEmpty()) {
			for (User player : plugin.getOnlinePlayers()) {
				if (player.hasPermission(PartiesPermission.ADMIN_UPDATES.toString())) {
					plugin.getPlayerManager().getPlayer(player.getUUID()).sendMessage(Messages.PARTIES_UPDATEAVAILABLE
							.replace("%version%", foundVersion)
							.replace("%thisversion%", plugin.getVersion()));
				}
			}
		}
	}
	
	public static void asyncTaskCheckUpdates() {
		if (ConfigMain.PARTIES_UPDATES_CHECK) {
			plugin.getPartiesScheduler().runAsyncTaskTimer(ADPUpdater::checkUpdates, 60*60*24);
		}
	}
	
	public static void asyncCheckUpdates() {
		if (ConfigMain.PARTIES_UPDATES_CHECK) {
			plugin.getPartiesScheduler().runAsync(ADPUpdater::checkUpdates);
		}
	}
	
	
	private static void checkUpdates() {
		foundVersion = "";
		String version = getVersionInfo();
		
		if (version == null) {
			plugin.logError(Constants.UPDATER_FALLBACK_WARN);
			version = getVersionFallback();
		}
		
		if (checkVersion(version, plugin.getVersion())) {
			// New version found
			foundVersion = version;
			
			LoggerManager.log(LogLevel.BASE, Constants.UPDATER_FOUND
					.replace("{currentVersion}", plugin.getVersion())
					.replace("{newVersion}", foundVersion), true, ConsoleColor.CYAN);
			alertPlayers();
		}
	}
	
	private static String getVersionInfo() {
		String ret = null;
		try {
			URLConnection conn = new URL(Constants.UPDATER_URL
					.replace("{version}", plugin.getVersion())).openConnection();
			conn.setConnectTimeout(10000);
			conn.addRequestProperty("User-Agent", "ADP Updater");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject response = new JsonParser().parse(br.readLine()).getAsJsonObject();
			// Get the version string
			if (response != null)
				ret = response.get(Constants.UPDATER_FIELD_VERSION).getAsString();
			
			br.close();
		} catch (IOException ex) {
			LoggerManager.printError(Constants.UPDATER_FAILED_IO);
		} catch (Exception ex) {
			LoggerManager.printError(Constants.UPDATER_FAILED_GENERAL);
		}
		return ret;
	}
	
	private static String getVersionFallback() {
		String ret = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(Constants.UPDATER_FALLBACK_URL
					.replace("{version}", plugin.getVersion())).openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(10000);
			conn.addRequestProperty("User-Agent", "ADP Updater");
			
			String postContent = "key=" +
					Constants.UPDATER_FALLBACK_KEY +
					"&resource=" +
					Constants.UPDATER_FALLBACK_RESOURCE;
			conn.getOutputStream().write(postContent.getBytes("UTF-8"));
			
			String response = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
			// Check if is a correct version and not a message
			if (response.length() < 10) {
				ret = response;
			}
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
