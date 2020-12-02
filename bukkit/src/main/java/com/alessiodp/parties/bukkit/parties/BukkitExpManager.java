package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.FormulaUtils;
import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPreExperienceDropEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.addons.external.MMOCoreHandler;
import com.alessiodp.parties.bukkit.addons.external.SkillAPIHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.players.objects.ExpDrop;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.ExpManager;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BukkitExpManager extends ExpManager {
	private double rangeSquared;
	
	private ExpConvert convertNormal;
	private ExpConvert convertSkillapi;
	
	public BukkitExpManager(@NonNull PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void reload() {
		super.reload();
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
		double totalParty = 0;
		double totalNormal = 0;
		double totalSkillapi = 0;
		
		PartyImpl party = plugin.getPartyManager().getParty(drop.getKiller().getPartyId());
		
		if (party != null) {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_EXP_RECEIVED, drop.getNormal(), drop.getSkillApi()), true);
			
			Player player = Bukkit.getPlayer(drop.getKiller().getPlayerUUID());
			User user = plugin.getPlayer(drop.getKiller().getPlayerUUID());
			
			if (player != null && user != null) {
				
				// Calling API event
				BukkitPartiesPreExperienceDropEvent partiesPreExperienceDropEvent = ((BukkitEventManager) plugin.getEventManager()).preparePreExperienceDropEvent(party, drop.getKiller(), drop.getEntityKilled(), drop.getNormal(), drop.getSkillApi());
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
						party.giveExperience(totalParty, drop.getKiller());
						
						user.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_EXP_PARTY_GAINED
								.replace("%exp%", CommonUtils.formatDouble(totalParty)), drop.getKiller(), party), true);
					}
					if (totalNormal > 0 && !shareExperience(totalNormal, drop.getKiller(), party, drop.getEntityKilled(), ExpConvert.NORMAL)) {
						player.giveExp((int) totalNormal);
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
	private boolean shareExperience(double experience, PartyPlayerImpl killer, PartyImpl party, Entity killedMob, ExpConvert type) {
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
						.replace("%exp%", Double.toString(experience))
						.replace("%number_players%", Integer.toString(playersToShare.size()))
						.replace("%level%", Integer.toString(party.getLevel()));
				String formulaOthers = BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA_OTHERS
						.replace("%exp%", Double.toString(experience))
						.replace("%number_players%", Integer.toString(playersToShare.size()))
						.replace("%level%", Integer.toString(party.getLevel()));
				try {
					for (Pair<PartyPlayerImpl, Integer> pShare : playersToShare) {
						Player bukkitPlayer = Bukkit.getPlayer(pShare.getKey().getPlayerUUID());
						User user = plugin.getPlayer(pShare.getKey().getPlayerUUID());
						if (bukkitPlayer != null && user != null) {
							double effectiveExperience;
							if (pShare.getKey().getPlayerUUID().equals(killer.getPlayerUUID())) {
								effectiveExperience = Double.parseDouble(FormulaUtils.calculate(formulaKiller
										.replace("%distance%", Integer.toString(pShare.getValue()))));
							} else {
								effectiveExperience = Double.parseDouble(FormulaUtils.calculate(formulaOthers
										.replace("%distance%", Integer.toString(pShare.getValue()))));
							}
							if (BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_ROUND_EXP_DROP)
								effectiveExperience = (int) effectiveExperience;
							
							String message = "";
							switch (type) {
								case NORMAL:
									bukkitPlayer.giveExp((int) effectiveExperience);
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
											.replace("%exp%", CommonUtils.formatDouble(effectiveExperience))
											.replace("%total_exp%", CommonUtils.formatDouble(experience))
									, killer, party), true);
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_EXP_SENT, effectiveExperience, type.name(), pShare.getKey().getName(), pShare.getKey().getPlayerUUID()), true);
						}
					}
					ret = true;
				} catch (Exception ex) {
					plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_EXP_EXPRESSIONERROR, formulaKiller, formulaOthers, ex.getMessage()));
				}
			}
			
		}
		return ret;
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
