package com.alessiodp.parties.common.configuration;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.ConfigurationManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;

public abstract class PartiesConfigurationManager extends ConfigurationManager {
	
	public PartiesConfigurationManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected boolean isAutoUpgradeEnabled() {
		return ConfigMain.PARTIES_AUTOMATIC_UPGRADE_CONFIGS;
	}
	
	@Override
	protected void performChanges() {
		plugin.getDatabaseManager().setDatabaseType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_DATABASE));
		plugin.getLoginAlertsManager().setPermission(PartiesPermission.ADMIN_ALERTS);
		checkOutdatedConfigs(Messages.PARTIES_CONFIGURATION_OUTDATED);
	}
	
	public ConfigMain getConfigMain() {
		for (ConfigurationFile cf : getConfigs()) {
			if (cf instanceof ConfigMain)
				return (ConfigMain) cf;
		}
		throw new IllegalStateException("No ConfigMain configuration file found");
	}
	
	public ConfigParties getConfigParties() {
		for (ConfigurationFile cf : getConfigs()) {
			if (cf instanceof ConfigParties)
				return (ConfigParties) cf;
		}
		throw new IllegalStateException("No ConfigParties configuration file found");
	}
	
	public Messages getMessages() {
		for (ConfigurationFile cf : getConfigs()) {
			if (cf instanceof Messages)
				return (Messages) cf;
		}
		throw new IllegalStateException("No Messages configuration file found");
	}
	
	public PartiesPacket.ConfigData makePacketConfigData() {
		return new PartiesPacket.ConfigData(
				ConfigMain.STORAGE_TYPE_DATABASE,
				ConfigMain.ADDITIONAL_EXP_ENABLE,
				ConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS,
				ConfigMain.ADDITIONAL_EXP_MODE,
				ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_START,
				ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP,
				ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_SAFE_CALCULATION,
				ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT,
				ConfigMain.ADDITIONAL_EXP_FIXED_LIST,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_FISH_HOOK,
				ConfigParties.ADDITIONAL_KILLS_ENABLE,
				ConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL,
				ConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE,
				ConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS
		);
	}
	
	public void parsePacketConfigData(PartiesPacket.ConfigData configData) {
		// Storage check
		String storage = configData.getStorageTypeDatabase();
		if (!storage.equalsIgnoreCase(ConfigMain.STORAGE_TYPE_DATABASE))
			plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_SYNC_DIFFERENT_STORAGE, storage, ConfigMain.STORAGE_TYPE_DATABASE), true);
		
		// Experience
		ConfigMain.ADDITIONAL_EXP_ENABLE = configData.isAdditionalExpEnable();
		ConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS = configData.isAdditionalExpEarnFromMobs();
		ConfigMain.ADDITIONAL_EXP_MODE = configData.getAdditionalExpMode();
		ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_START = configData.getAdditionalExpProgressiveStart();
		ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP = configData.getAdditionalExpProgressiveLevelExp();
		ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_SAFE_CALCULATION = configData.isAdditionalExpProgressiveSafeCalculation();
		ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT = configData.isAdditionalExpFixedRepeat();
		ConfigMain.ADDITIONAL_EXP_FIXED_LIST = configData.getAdditionalExpFixedList();
		
		// Friendly fire
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE = configData.isAdditionalFriendlyFireEnable();
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE = configData.getAdditionalFriendlyFireType();
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT = configData.isAdditionalFriendlyFireWarnOnFight();
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_FISH_HOOK = configData.isAdditionalFriendlyFirePreventFishHook();
		
		// Kills
		ConfigParties.ADDITIONAL_KILLS_ENABLE = configData.isAdditionalKillsEnable();
		ConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL = configData.isAdditionalKillsMobNeutral();
		ConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE = configData.isAdditionalKillsMobHostile();
		ConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS = configData.isAdditionalKillsMobPlayers();
		
		((PartiesPlugin) plugin).getExpManager().reloadAll(); // Reload ExpManager
	}
}
