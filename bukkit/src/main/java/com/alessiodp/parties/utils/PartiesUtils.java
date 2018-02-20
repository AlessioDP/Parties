package com.alessiodp.parties.utils;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.addons.external.SkillAPIHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.configuration.data.ConfigMain.ExpType;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.interfaces.Rank;

public class PartiesUtils {
	private static Parties plugin;
	
	public PartiesUtils() {
		plugin = Parties.getInstance();
	}
	
	
	/*
	 * Placeholders converter
	 */
	public static String convertPartyPlaceholders(String message, PartyEntity party) {
		return convertPartyPlaceholders(message, party, "");
	}
	public static String convertPartyPlaceholders(String message, PartyEntity party, String emptyPlaceholder) {
		String ret = message;
		if (party != null) {
			ret = ret
					.replace(Constants.PLACEHOLDER_PARTY_COLOR_CODE,	party.getCurrentColor() != null ? party.getCurrentColor().getCode() : emptyPlaceholder)
					.replace(Constants.PLACEHOLDER_PARTY_COLOR_COMMAND,	party.getCurrentColor() != null ? party.getCurrentColor().getCommand() : emptyPlaceholder)
					.replace(Constants.PLACEHOLDER_PARTY_COLOR_NAME,	party.getCurrentColor() != null ? party.getCurrentColor().getName() : emptyPlaceholder)
					.replace(Constants.PLACEHOLDER_PARTY_DESC,			party.getDescription().isEmpty() ? emptyPlaceholder : party.getDescription())
					.replace(Constants.PLACEHOLDER_PARTY_KILLS,			Integer.toString(party.getKills()))
					.replace(Constants.PLACEHOLDER_PARTY_MOTD,			party.getMotd().isEmpty() ? emptyPlaceholder : party.getMotd())
					.replace(Constants.PLACEHOLDER_PARTY_ONLINENUMBER,	Integer.toString(party.getNumberOnlinePlayers()))
					.replace(Constants.PLACEHOLDER_PARTY_PARTY,			party.getName())
					.replace(Constants.PLACEHOLDER_PARTY_PREFIX,		party.getPrefix().isEmpty() ? emptyPlaceholder : party.getPrefix())
					.replace(Constants.PLACEHOLDER_PARTY_SUFFIX,		party.getSuffix().isEmpty() ? emptyPlaceholder : party.getSuffix());
		}
		ret = PlaceholderAPIHandler.getPlaceholders(null, ret);
		return ret;
	}
	
	public static String convertPlayerPlaceholders(String message, PartyPlayerEntity player) {
		String ret = message;
		if (player != null) {
			ret = ret
					.replace(Constants.PLACEHOLDER_PLAYER_PLAYER,	player.getName())
					.replace(Constants.PLACEHOLDER_PLAYER_SENDER,	player.getName())
					.replace(Constants.PLACEHOLDER_PLAYER_RANK_NAME,plugin.getRankManager().searchRankByLevel(player.getRank()).getName())
					.replace(Constants.PLACEHOLDER_PLAYER_RANK_CHAT,plugin.getRankManager().searchRankByLevel(player.getRank()).getChat());
		}
		ret = PlaceholderAPIHandler.getPlaceholders(player.getPlayer(), ret);
		return ret;
	}
	
	public static String convertAllPlaceholders(String message, PartyEntity party, PartyPlayerEntity player) {
		return convertPlayerPlaceholders(convertPartyPlaceholders(message, party), player);
	}
	
	
	/*
	 * Home utils
	 */
	public static String formatHome(Location loc) {
		String ret = "";
		if (loc != null) {
			ret = loc.getWorld().getName() + ","
					+ loc.getBlockX() + ","
					+ loc.getBlockY() + ","
					+ loc.getBlockZ() + ","
					+ loc.getYaw() + ","
					+ loc.getPitch();
		}
		return ret;
	}
	public static Location formatHome(String raw) {
		Location ret = null;
		try {
			String[] split = raw.split(",");
			World world = Bukkit.getWorld(split[0]);
			int x = Integer.parseInt(split[1]);
			int y = Integer.parseInt(split[2]);
			int z = Integer.parseInt(split[3]);
			float yaw = Float.parseFloat(split[4]);
			float pitch = Float.parseFloat(split[5]);
			ret = new Location(world, x, y, z, yaw, pitch);
		} catch(Exception ex) {}
		return ret;
	}
	
	
	/*
	 * Rank utils
	 */
	public static boolean checkPlayerRankAlerter(PartyPlayerEntity pp, PartiesPermission perm) {
		boolean ret = true;
		Rank r = plugin.getRankManager().searchRankByLevel(pp.getRank());
		
		if (r != null && !pp.getPlayer().hasPermission(PartiesPermission.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(perm.toString())) {
				Rank rr = plugin.getRankManager().searchUpRank(pp.getRank(), perm.toString());
				if (rr != null)
					pp.sendMessage(Messages.PARTIES_PERM_NORANK
							.replace(Constants.PLACEHOLDER_PLAYER_RANK_NAME, rr.getName())
							.replace(Constants.PLACEHOLDER_PLAYER_RANK_CHAT, rr.getChat()));
				else
					pp.sendNoPermission(perm);
				ret = false;
			}
		}
		return ret;
	}
	public static boolean checkPlayerRank(PartyPlayerEntity pp, PartiesPermission perm) {
		boolean ret = true;
		Rank r = plugin.getRankManager().searchRankByLevel(pp.getRank());
		
		if (r != null && !pp.getPlayer().getPlayer().hasPermission(PartiesPermission.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(perm.toString())) {
				ret = false;
			}
		}
		return ret;
	}
	
	
	/*
	 * Censor utils
	 */
	public static boolean checkCensor(String phrase) {
		boolean ret = false;
		if (ConfigMain.ADDITIONAL_CENSOR_ENABLE) {
			for (String cen : ConfigMain.ADDITIONAL_CENSOR_CONTAINS) {
				// Contains
				if (ret)
					break;
				
				if (!ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE) {
					if (phrase.toLowerCase().contains(cen.toLowerCase()))
						ret = true;
				} else if (phrase.contains(cen))
					ret = true;
			}
			for (String cen : ConfigMain.ADDITIONAL_CENSOR_STARTSWITH) {
				// Starts with
				if (ret)
					break;
				
				if (!ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE) {
					if (phrase.toLowerCase().startsWith(cen.toLowerCase()))
						ret = true;
				} else if (phrase.startsWith(cen))
					ret = true;
			}
			for (String cen : ConfigMain.ADDITIONAL_CENSOR_ENDSWITH) {
				// Ends with
				if (ret)
					break;
				
				if (!ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE) {
					if (phrase.toLowerCase().endsWith(cen.toLowerCase()))
						ret = true;
				} else if (phrase.endsWith(cen))
					ret = true;
			}
		}
		return ret;
	}
	
	
	/*
	 * Player vanish
	 */
	public static boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}
	
	
	/*
	 * Experience
	 */
	public static boolean handleExperienceDistribution(Player killer, Entity killedMob, String killedMobName, double vanillaExp, double skillApiExp) {
		boolean ret = false;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(killer.getUniqueId());
		PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		if (party != null) {
			// Start to handle the event
			
			// Get nearest players
			List<Player> playersList = new ArrayList<Player>();
			playersList.add(killer);
			for (Player partyPlayer : party.getOnlinePlayers()) {
				if (!partyPlayer.getUniqueId().equals(killer.getUniqueId())
						&& partyPlayer.getLocation().getWorld() == killer.getWorld()
						&& killer.getLocation().distance(partyPlayer.getLocation()) < ConfigMain.ADDITIONAL_EXP_RANGE) {
					playersList.add(partyPlayer);
				}
			}
			
			if (playersList.size() > 1) {
				// Merge experience
				switch (ConfigMain.ADDITIONAL_EXP_GIVEAS_VANILLA) {
				case VANILLA:
					// Same exp
					break;
				case SKILLAPI:
					skillApiExp += vanillaExp;
					vanillaExp = 0;
					break;
				}
				switch (ConfigMain.ADDITIONAL_EXP_GIVEAS_SKILLAPI) {
				case VANILLA:
					vanillaExp += skillApiExp;
					skillApiExp = 0;
					break;
				case SKILLAPI:
					// Same exp
					break;
				}
				
				// Give experience
				if (vanillaExp > 0) {
					PartiesUtils.distributeExperience(pp, playersList, killedMobName, vanillaExp, ExpType.VANILLA);
				}
				if (skillApiExp > 0) {
					PartiesUtils.distributeExperience(pp, playersList, killedMobName, skillApiExp, ExpType.SKILLAPI);
				}
				
				ret = true;
			}
		}
		return ret;
	}
	
	public static void distributeExperience(PartyPlayerEntity killer, List<Player> playersList, String entityName, double experience, ExpType expType) {
		double expForPlayer = 0;
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_DISTRIBUTE
				.replace("{type}", expType.toString())
				.replace("{entity}", entityName)
				.replace("{player}", killer.getName()), true);
		// Calculate experience
		try {
			ScriptEngine sem = new ScriptEngineManager().getEngineByName("JavaScript");
			String formula = ConfigMain.ADDITIONAL_EXP_FORMULA
					.replace("%exp%", Double.toString(experience))
					.replace("%number%", Integer.toString(playersList.size()));
			expForPlayer = (double) sem.eval(formula);
		} catch (Exception ex) {
			LoggerManager.printError("Failed to calculate shared exp: " + ex.getMessage()); // TODO
		}
		
		for (Player pl : playersList) {
			switch (expType) {
			case VANILLA:
				pl.giveExp((int) expForPlayer);
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_VANILLA_GIVE
						.replace("{exp}", Double.toString(expForPlayer))
						.replace("{player}", pl.getName()), true);
				break;
			case SKILLAPI:
				SkillAPIHandler.giveExp(pl, expForPlayer);
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_SKILLAPI_GIVE
						.replace("{exp}", Double.toString(expForPlayer))
						.replace("{player}", pl.getName()), true);
				break;
			}
			
			if (pl.getUniqueId().equals(killer.getPlayerUUID())) {
				killer.sendMessage(""); // TODO
			} else {
				plugin.getPlayerManager().getPlayer(pl.getUniqueId()).sendMessage(""); // TODO
			}
		}
	}
}
