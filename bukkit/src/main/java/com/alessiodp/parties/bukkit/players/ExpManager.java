package com.alessiodp.parties.bukkit.players;

import com.alessiodp.parties.bukkit.addons.external.SkillAPIHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.ExpDrop;
import com.alessiodp.parties.common.parties.objects.ExpResult;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

public class ExpManager {
	private PartiesPlugin plugin;
	private ExpMode mode;
	private double rangeSquared;
	
	private ExpConvert convertNormal;
	private ExpConvert convertSkillapi;
	
	public ExpManager(PartiesPlugin instance) {
		plugin = instance;
		reload();
	}
	
	public void reload() {
		mode = ExpMode.parse(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_MODE);
		rangeSquared = BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_RANGE * BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_RANGE;
		
		convertNormal = ExpConvert.parse(BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_NORMAL);
		convertSkillapi = ExpConvert.parse(BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI);
	}
	
	/**
	 * Distribute the experience to the player
	 * @return Returns true if the distribution has been done
	 */
	public boolean distributeExp(ExpDrop drop) {
		boolean ret = false;
		
		// Flags used to messages
		int totalParty = 0;
		int totalNormal = 0;
		int totalSkillapi = 0;
		
		PartyImpl party = plugin.getPartyManager().getParty(drop.getKiller().getPartyName());
		
		if (party != null) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_RECEIVED
					.replace("{normal}", Integer.toString(drop.getNormal()))
					.replace("{skillapi}", Integer.toString(drop.getSkillapi())), true);
			if (drop.getNormal() > 0) {
				switch (convertNormal) {
					case PARTY:
						totalParty += drop.getNormal();
						break;
					case NORMAL:
						totalNormal += drop.getNormal();
						break;
					case SKILLAPI:
						totalSkillapi += drop.getNormal();
						break;
				}
				ret = true;
			}
			if (drop.getSkillapi() > 0) {
				switch (convertSkillapi) {
					case PARTY:
						totalParty += drop.getSkillapi();
						break;
					case NORMAL:
						totalNormal += drop.getSkillapi();
						break;
					case SKILLAPI:
						totalSkillapi += drop.getSkillapi();
						break;
				}
				ret = true;
			}
			
			// Send experience
			if (totalParty > 0) {
				// If sent to the party, don't share experience
				party.giveExperience(totalParty);
				party.updateParty();
				drop.getKiller().sendMessage(BukkitMessages.ADDCMD_EXP_PARTY_GAINED
						.replace("%exp%",Integer.toString(totalParty)));
			}
			if (totalNormal > 0 && !shareExperience(totalNormal, drop.getKiller(), party, drop.getEntityKilled(), ExpConvert.NORMAL)) {
				Bukkit.getPlayer(drop.getKiller().getPlayerUUID()).giveExp(totalNormal);
			}
			if (totalSkillapi > 0 && !shareExperience(totalSkillapi, drop.getKiller(), party, drop.getEntityKilled(), ExpConvert.SKILLAPI)) {
				SkillAPIHandler.giveExp(drop.getKiller().getPlayerUUID(), totalSkillapi);
			}
		}
		return ret;
	}
	
	/**
	 * Share the experience if sharing system enabled
	 *
	 * @return Returns false if the player that should get the experience is only one
	 */
	private boolean shareExperience(int experience, PartyPlayerImpl killer, PartyImpl party, Entity killedMob, ExpConvert type) {
		boolean ret = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_ENABLE) {
			List<PartyPlayerImpl> playersToShare = new ArrayList<>();
			Location mobKilledLocation = killedMob.getLocation();
			
			// Get nearest player
			for (PartyPlayerImpl player : party.getOnlinePlayers()) {
				try {
					if (player.getPlayerUUID().equals(killer.getPlayerUUID())
							|| (Bukkit.getPlayer(player.getPlayerUUID()).getLocation().distanceSquared(mobKilledLocation) <= rangeSquared)) {
						playersToShare.add(player);
					}
				} catch (IllegalArgumentException ignored) {
				} // Ignoring different world
			}
			
			if (playersToShare.size() > BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN) {
				// Calculate experience for each player
				String formula = BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA
						.replace("%exp%", Integer.toString(experience))
						.replace("%number_players%", Integer.toString(playersToShare.size()))
						.replace("%level%", Integer.toString(party.getLevel()));
				try {
					int singlePlayerExperience = (int) calculateExpression(formula);
					
					for (PartyPlayerImpl player : playersToShare) {
						String message = "";
						switch (type) {
							case NORMAL:
								Bukkit.getPlayer(player.getPlayerUUID()).giveExp(singlePlayerExperience);
								message = player.getPlayerUUID().equals(killer.getPlayerUUID()) ? BukkitMessages.ADDCMD_EXP_NORMAL_GAINED_KILLER : BukkitMessages.ADDCMD_EXP_NORMAL_GAINED_OTHERS;
								break;
							case SKILLAPI:
								SkillAPIHandler.giveExp(player.getPlayerUUID(), singlePlayerExperience);
								message = player.getPlayerUUID().equals(killer.getPlayerUUID()) ? BukkitMessages.ADDCMD_EXP_SKILLAPI_GAINED_KILLER : BukkitMessages.ADDCMD_EXP_SKILLAPI_GAINED_OTHERS;
								break;
						}
						player.sendMessage(message
								.replace("%exp%",Integer.toString(singlePlayerExperience))
								.replace("%total_exp%",Integer.toString(experience))
								, killer);
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_SENT
								.replace("{exp}", Integer.toString(singlePlayerExperience))
								.replace("{type}", type.name())
								.replace("{player}", player.getName()), true);
					}
					ret = true;
				} catch (Exception ex) {
					LoggerManager.printError(Constants.DEBUG_EXP_EXPRESSIONERROR
							.replace("{value}", formula)
							.replace("{message}", ex.getMessage()));
				}
			}
			
		}
		return ret;
	}
	
	public ExpResult calculateLevel(double experience) throws Exception {
		ExpResult ret = new ExpResult();
		if (BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
			switch (mode) {
				case PROGRESSIVE:
					ret = calculateLevelProgressive(experience);
					break;
				case FIXED:
					ret = calculateLevelFixed(experience);
					break;
				case CUSTOM:
					ret = calculateLevelCustom(experience);
			}
		}
		return ret;
	}
	
	private ExpResult calculateLevelProgressive(double experience) throws Exception {
		ExpResult ret = new ExpResult();
		int levelCount = 1;
		double progessiveLevelExp = BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START;
		double total = progessiveLevelExp;
		while (experience >= total) {
			// Calculate new level exp
			progessiveLevelExp = calculateExpression(Double.toString(progessiveLevelExp).concat(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_MULTIPLIER));
			// Add new level exp to the total
			total = total + progessiveLevelExp;
			levelCount++;
		}
		
		ret.setLevel(levelCount);
		ret.setCurrentExperience((int) (experience - (total - progessiveLevelExp)));
		ret.setNecessaryExperience((int) (progessiveLevelExp));
		return ret;
	}
	
	private ExpResult calculateLevelFixed(double experience) {
		ExpResult ret = new ExpResult();
		int levelCount = 0;
		double total = 0;
		for (Double level : BukkitConfigMain.ADDITIONAL_EXP_LEVELS_FIXED) {
			total = total + level;
			levelCount++;
			
			if (experience < total) {
				ret.setLevel(levelCount);
				ret.setCurrentExperience((int) (experience - (total - level)));
				ret.setNecessaryExperience(level.intValue());
				break;
			}
		}
		return ret;
	}
	
	private ExpResult calculateLevelCustom(double experience) throws Exception {
		ExpResult ret = new ExpResult();
		if (experience > 0) {
			ret.setLevel((int) calculateExpression(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_CUSTOM_FORMULA
					.replace("%total_exp%", Double.toString(experience))));
		}
		return ret;
	}
	
	
	private double calculateExpression(String expression) throws Exception {
		return Double.valueOf(new ScriptEngineManager().getEngineByName("nashorn").eval(expression).toString());
	}
	
	public enum ExpMode {
		PROGRESSIVE, FIXED, CUSTOM;
		
		public static ExpMode parse(String name) {
			ExpMode ret = PROGRESSIVE;
			switch (name.toLowerCase()) {
				case "fixed":
					ret = FIXED;
					break;
				case "custom":
					ret = CUSTOM;
					break;
			}
			return ret;
		}
	}
	
	public enum ExpConvert {
		NORMAL, SKILLAPI, PARTY;
		
		public static ExpConvert parse(String name) {
			ExpConvert ret = PARTY;
			switch (name.toLowerCase()) {
				case "normal":
					ret = NORMAL;
					break;
				case "skillapi":
					ret = SKILLAPI;
					break;
			}
			return ret;
		}
	}
}
