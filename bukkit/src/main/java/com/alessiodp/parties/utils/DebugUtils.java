package com.alessiodp.parties.utils;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;

public class DebugUtils {
	private static long partiesTiming = 0;
	
	public static void startDebugTask(Parties plugin) {
		if (Constants.DEBUG_ENABLED) {
			// Debug task
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
				int players = plugin.getPlayerManager().getListPartyPlayers().size();
				String parties = plugin.getPartyManager().getListParties().keySet().toString();
				debugLog("Entities = Players: " + Integer.toString(players) + " - Parties: " + parties);
			}, 200, 20*15);
		}
	}
	
	public static void debugLog(String message) {
		if (Constants.DEBUG_ENABLED) {
			System.out.println(ConsoleColor.PURPLE.getCode() + "[Parties Debug] " + message + ConsoleColor.RESET.getCode());
		}
	}
	
	public static void debugDataTiming(long nsTime) {
		if (Constants.DEBUG_ENABLED) {
			long timeDifference = System.nanoTime() - nsTime;
			debugLog(String.format("End data call in %.2fms", timeDifference / 1000000.0));
			if (Constants.DEBUG_TIMESTAMPS) {
				partiesTiming += timeDifference;
				debugLog(String.format("### Current timings: %.2fms", partiesTiming / 1000000.0));
			}
		}
	}
}
