package com.alessiodp.parties.common.configuration;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.ConfigurationManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.ArrayList;

public abstract class PartiesConfigurationManager extends ConfigurationManager {
	
	public PartiesConfigurationManager(ADPPlugin plugin) {
		super(plugin);
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
	
	public byte[] makeConfigsPacket() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		
		// Storage check
		output.writeUTF(ConfigMain.STORAGE_TYPE_DATABASE);
		
		// Experience
		output.writeBoolean(ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE);
		output.writeUTF(ConfigMain.ADDITIONAL_EXP_LEVELS_MODE);
		output.writeDouble(ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START);
		output.writeUTF(ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP);
		output.writeBoolean(ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_SAFE_CALCULATION);
		output.writeBoolean(ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_REPEAT);
		output.writeInt(ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST.size());
		for (Object db : ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST) {
			double val;
			if (db instanceof Integer)
				val = ((Integer) db).doubleValue();
			else
				val = (double) db;
			output.writeDouble(val);
		}
		
		// Friendly fire
		output.writeBoolean(ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE);
		output.writeUTF(ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE);
		output.writeBoolean(ConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT);
		output.writeBoolean(ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_FISH_HOOK);
		
		// Kills
		output.writeBoolean(ConfigParties.ADDITIONAL_KILLS_ENABLE);
		output.writeBoolean(ConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL);
		output.writeBoolean(ConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE);
		output.writeBoolean(ConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS);
		
		return output.toByteArray();
	}
	
	public void parseConfigsPacket(byte[] raw) {
		ByteArrayDataInput input = ByteStreams.newDataInput(raw);
		
		// Storage check
		String storage = input.readUTF();
		if (!storage.equalsIgnoreCase(ConfigMain.STORAGE_TYPE_DATABASE))
			plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_SYNC_DIFFERENT_STORAGE, storage, ConfigMain.STORAGE_TYPE_DATABASE), true);
		
		// Experience
		ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE = input.readBoolean();
		ConfigMain.ADDITIONAL_EXP_LEVELS_MODE = input.readUTF();
		ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START = input.readDouble();
		ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP = input.readUTF();
		ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_SAFE_CALCULATION = input.readBoolean();
		ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_REPEAT = input.readBoolean();
		ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST = new ArrayList<>();
		int fixedSize = input.readInt();
		for (int c = 0; c < fixedSize; c++) {
			ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST.add(input.readDouble());
		}
		
		// Friendly fire
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE = input.readBoolean();
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE = input.readUTF();
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT = input.readBoolean();
		ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_FISH_HOOK = input.readBoolean();
		
		// Kills
		ConfigParties.ADDITIONAL_KILLS_ENABLE = input.readBoolean();
		ConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL = input.readBoolean();
		ConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE = input.readBoolean();
		ConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS = input.readBoolean();
		
		((PartiesPlugin) plugin).getExpManager().reloadAll(); // Reload ExpManager
	}
}
