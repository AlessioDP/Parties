package com.alessiodp.parties.bukkit.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.FormulaUtils;
import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreExperienceDropEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.addons.external.MMOCoreHandler;
import com.alessiodp.parties.bukkit.addons.external.SkillAPIHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.players.objects.ExpDrop;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.ExpResult;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpManager {
	private final PartiesPlugin plugin;
	@Getter private ExpMode mode;
	private double rangeSquared;
	
	private ExpConvert convertNormal;
	private ExpConvert convertSkillapi;
	
	public ExpManager(@NonNull PartiesPlugin instance) {
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
		
		PartyImpl party = plugin.getPartyManager().getParty(drop.getKiller().getPartyId());
		
		if (party != null) {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_EXP_RECEIVED
					.replace("{normal}", Integer.toString(drop.getNormal()))
					.replace("{skillapi}", Integer.toString(drop.getSkillApi())), true);
			
			Player player = Bukkit.getPlayer(drop.getKiller().getPlayerUUID());
			User user = plugin.getPlayer(drop.getKiller().getPlayerUUID());
			
			if (player != null && user != null) {
				
				// Calling API event
				BukkitPartiesPartyPreExperienceDropEvent partiesPreExperienceDropEvent = ((BukkitEventManager) plugin.getEventManager()).preparePartyPreExperienceDropEvent(party, drop.getKiller(), drop.getEntityKilled(), drop.getNormal(), drop.getSkillApi());
				plugin.getEventManager().callEvent(partiesPreExperienceDropEvent);
				if (!partiesPreExperienceDropEvent.isCancelled()) {
					drop.setNormal(partiesPreExperienceDropEvent.getNormalExperience());
					drop.setSkillApi(partiesPreExperienceDropEvent.getSkillApiExperience());
					
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
							default:
								// Convert option not set
								return false;
						}
						ret = true;
					}
					if (drop.getSkillApi() > 0) {
						switch (convertSkillapi) {
							case PARTY:
								totalParty += drop.getSkillApi();
								break;
							case NORMAL:
								totalNormal += drop.getSkillApi();
								break;
							case SKILLAPI:
								totalSkillapi += drop.getSkillApi();
								break;
							default:
								// Convert option not set
								return false;
						}
						ret = true;
					}
					
					// Send experience
					if (totalParty > 0) {
						// If sent to the party, don't share experience
						party.giveExperience(totalParty);
						
						user.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_EXP_PARTY_GAINED
								.replace("%exp%", Integer.toString(totalParty)), drop.getKiller(), party), true);
						
						// Calling API event
						BukkitPartiesPartyGetExperienceEvent partiesPartyGetExperienceEvent = ((BukkitEventManager) plugin.getEventManager()).preparePartyGetExperienceEvent(party, totalParty, drop.getKiller());
						plugin.getEventManager().callEvent(partiesPartyGetExperienceEvent);
					}
					if (totalNormal > 0 && !shareExperience(totalNormal, drop.getKiller(), party, drop.getEntityKilled(), ExpConvert.NORMAL)) {
						player.giveExp(totalNormal);
					}
					if (totalSkillapi > 0 && !shareExperience(totalSkillapi, drop.getKiller(), party, drop.getEntityKilled(), ExpConvert.SKILLAPI)) {
						SkillAPIHandler.giveExp(player.getUniqueId(), totalSkillapi);
					}
				}
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
			List<Pair<PartyPlayerImpl, Integer>> playersToShare = new ArrayList<>();
			Location mobKilledLocation = killedMob.getLocation();
			
			// Get nearest player
			for (PartyPlayer player : party.getOnlineMembers(true)) {
				try {
					if (player.getPlayerUUID().equals(killer.getPlayerUUID())) {
						double distance = Bukkit.getPlayer(player.getPlayerUUID()).getLocation().distanceSquared(mobKilledLocation);
						if (distance <= rangeSquared) {
							playersToShare.add(new Pair<>((PartyPlayerImpl) player, (int) distance));
						}
					}
				} catch (IllegalArgumentException ignored) {} // Ignoring different world
			}
			
			if (playersToShare.size() > BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN) {
				// Calculate experience for each player
				String formulaKiller = BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA_KILLER
						.replace("%exp%", Integer.toString(experience))
						.replace("%number_players%", Integer.toString(playersToShare.size()))
						.replace("%level%", Integer.toString(party.getLevel()));
				String formulaOthers = BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA_OTHERS
						.replace("%exp%", Integer.toString(experience))
						.replace("%number_players%", Integer.toString(playersToShare.size()))
						.replace("%level%", Integer.toString(party.getLevel()));
				try {
					for (Pair<PartyPlayerImpl, Integer> pShare : playersToShare) {
						Player bukkitPlayer = Bukkit.getPlayer(pShare.getKey().getPlayerUUID());
						User user = plugin.getPlayer(pShare.getKey().getPlayerUUID());
						if (bukkitPlayer != null && user != null) {
							int effectiveExperience;
							if (pShare.getKey().getPlayerUUID().equals(killer.getPlayerUUID())) {
								effectiveExperience = (int) Double.parseDouble(FormulaUtils.calculate(formulaKiller
										.replace("%distance%", Integer.toString(pShare.getValue()))));
							} else {
								effectiveExperience = (int) Double.parseDouble(FormulaUtils.calculate(formulaOthers
										.replace("%distance%", Integer.toString(pShare.getValue()))));
							}
							
							String message = "";
							switch (type) {
								case NORMAL:
									bukkitPlayer.giveExp(effectiveExperience);
									message = pShare.getKey().getPlayerUUID().equals(killer.getPlayerUUID()) ? BukkitMessages.ADDCMD_EXP_NORMAL_GAINED_KILLER : BukkitMessages.ADDCMD_EXP_NORMAL_GAINED_OTHERS;
									break;
								case SKILLAPI:
									SkillAPIHandler.giveExp(pShare.getKey().getPlayerUUID(), effectiveExperience);
									message = pShare.getKey().getPlayerUUID().equals(killer.getPlayerUUID()) ? BukkitMessages.ADDCMD_EXP_SKILLAPI_GAINED_KILLER : BukkitMessages.ADDCMD_EXP_SKILLAPI_GAINED_OTHERS;
									break;
								case MMOCORE:
									MMOCoreHandler.giveExp(pShare.getKey().getPlayerUUID(), bukkitPlayer.getLocation(), effectiveExperience);
									break;
								default:
									// Nothing to do
									break;
							}
							
							user.sendMessage(plugin.getMessageUtils().convertPlaceholders(message
											.replace("%exp%", Integer.toString(effectiveExperience))
											.replace("%total_exp%", Integer.toString(experience))
									, killer, party), true);
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_EXP_SENT
									.replace("{exp}", Integer.toString(effectiveExperience))
									.replace("{type}", type.name())
									.replace("{player}", pShare.getKey().getName()), true);
						}
					}
					ret = true;
				} catch (Exception ex) {
					plugin.getLoggerManager().printError(PartiesConstants.DEBUG_EXP_EXPRESSIONERROR
							.replace("{value}", String.format("\"%s\" and \"%s\"", formulaKiller, formulaOthers))
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
				default:
					// Nothing to do
					break;
			}
		}
		return ret;
	}
	
	private ExpResult calculateLevelProgressive(double experience) throws Exception {
		ExpResult ret = new ExpResult();
		int levelCount = 1;
		double progressiveLevelExp = BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START;
		double total = progressiveLevelExp;
		while (experience >= total) {
			// Calculate new level exp
			progressiveLevelExp = Double.parseDouble(FormulaUtils.calculate(
					BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP
							.replace("%previous%", Double.toString(progressiveLevelExp))
			));
			// Add new level exp to the total
			total = total + progressiveLevelExp;
			levelCount++;
		}
		
		ret.setLevel(levelCount);
		
		ret.setLevelExperience((int) (progressiveLevelExp));
		ret.setCurrentExperience((int) (experience - (total - progressiveLevelExp)));
		ret.setNecessaryExperience((int) (progressiveLevelExp - ret.getCurrentExperience()));
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
				ret.setLevelExperience(level.intValue());
				ret.setCurrentExperience((int) (experience - (total - level)));
				ret.setNecessaryExperience((int) (level - ret.getCurrentExperience()));
				break;
			}
		}
		return ret;
	}
	
	public enum ExpMode {
		PROGRESSIVE, FIXED;
		
		public static ExpMode parse(String name) {
			if (CommonUtils.toLowerCase(name).equals("fixed"))
				return FIXED;
			return PROGRESSIVE;
		}
	}
	
	public enum ExpConvert {
		PARTY, NORMAL, SKILLAPI, MMOCORE;
		
		public static ExpConvert parse(String name) {
			ExpConvert ret;
			switch (name.toLowerCase(Locale.ENGLISH)) {
				case "normal":
					ret = NORMAL;
					break;
				case "skillapi":
					ret = SKILLAPI;
					break;
				case "mmocore":
					ret = MMOCORE;
					break;
				default:
					ret = PARTY;
					break;
			}
			return ret;
		}
	}
}
