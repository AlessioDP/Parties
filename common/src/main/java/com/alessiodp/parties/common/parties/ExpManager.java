package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.FormulaUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.ExpResult;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExpManager {
	@NonNull protected final PartiesPlugin plugin;
	@Getter protected ExpMode mode;
	
	public void reload() {
		mode = ExpMode.parse(ConfigMain.ADDITIONAL_EXP_LEVELS_MODE);
	}
	
	public void reloadAll() {
		reload();
		
		plugin.getPartyManager().getCacheParties().values().forEach(PartyImpl::calculateLevels);
	}
	
	/**
	 * Calculate the level
	 *
	 * @param experience The total experience
	 * @return The {@link ExpResult} of the calculation
	 * @throws Exception Something gone wrong
	 */
	public ExpResult calculateLevel(double experience) throws Exception {
		ExpResult ret = new ExpResult();
		if (ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
			switch (mode) {
				case PROGRESSIVE:
					ret = calculateLevelProgressive(experience);
					break;
				case FIXED:
					ret = ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_REPEAT ? calculateLevelFixedRepeat(experience) : calculateLevelFixedNoRepeat(experience);
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
		double progressiveLevelExp = ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START;
		double total = progressiveLevelExp;
		while (experience >= total) {
			// Calculate new level exp
			progressiveLevelExp = Double.parseDouble(FormulaUtils.calculate(
					ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP
							.replace("%previous%", Double.toString(progressiveLevelExp))
			));
			// Add new level exp to the total
			total = total + progressiveLevelExp;
			levelCount++;
		}
		
		ret.setLevel(levelCount);
		
		ret.setLevelExperience(progressiveLevelExp);
		ret.setLevelUpCurrent(experience - (total - progressiveLevelExp));
		ret.setLevelUpNecessary(progressiveLevelExp - ret.getLevelUpCurrent());
		return ret;
	}
	
	private ExpResult calculateLevelFixedNoRepeat(double experience) {
		ExpResult ret = new ExpResult();
		int levelCount = 1;
		double total = 0;
		double levelExperience = 0;
		
		for (int i = 0; i < ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST.size(); i++) {
			levelExperience = ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST.get(i);
			total = total + levelExperience;
			
			if (experience < total) {
				break;
			}
			levelCount++;
		}
		
		if (experience < total) {
			ret.setLevel(levelCount);
			ret.setLevelExperience(levelExperience);
			ret.setLevelUpCurrent(experience - (total - levelExperience));
			ret.setLevelUpNecessary(levelExperience - ret.getLevelUpCurrent());
		} else {
			ret.setLevel(levelCount);
			ret.setLevelExperience(0);
			ret.setLevelUpCurrent(experience - total);
			ret.setLevelUpNecessary(0);
		}
		
		return ret;
	}
	
	private ExpResult calculateLevelFixedRepeat(double experience) {
		ExpResult ret = new ExpResult();
		final int lastLevel = ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST.size() - 1;
		int levelCount = 1;
		double total = 0;
		double levelExperience = 0;
		
		int i = 0;
		while (i <= lastLevel) {
			levelExperience = ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST.get(i);
			total = total + levelExperience;
			
			if (experience < total)
				break;
			
			levelCount++;
			if (i < lastLevel)
				i++;
		}
		
		ret.setLevel(levelCount);
		ret.setLevelExperience(levelExperience);
		ret.setLevelUpCurrent(experience - (total - levelExperience));
		ret.setLevelUpNecessary(levelExperience - ret.getLevelUpCurrent());
		
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
}
