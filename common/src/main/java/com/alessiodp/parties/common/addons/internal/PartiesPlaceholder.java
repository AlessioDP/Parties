package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.Map;

public enum PartiesPlaceholder {
	COLOR_CODE,
	COLOR_COMMAND,
	COLOR_NAME,
	DESC,
	EXPERIENCE_TOTAL,
	EXPERIENCE_LEVEL,
	EXPERIENCE_LEVELUP_CURRENT,
	EXPERIENCE_LEVELUP_NECESSARY,
	KILLS,
	MOTD,
	PARTY,
	PREFIX,
	RANK_CHAT,
	RANK_NAME,
	SUFFIX,
	
	CUSTOM;
	
	private static PartiesPlugin plugin;
	private String format;
	private static final String CUSTOM_PREFIX = "custom_";
	
	PartiesPlaceholder() {
		format = "";
	}
	public static void setupFormats(PartiesPlugin instance) {
		plugin = instance;
		
		COLOR_CODE.format = ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_CODE;
		COLOR_COMMAND.format = ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_COMMAND;
		COLOR_NAME.format = ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_NAME;
		DESC.format = ConfigMain.ADDITIONAL_PLACEHOLDER_DESC;
		EXPERIENCE_TOTAL.format = ConfigMain.ADDITIONAL_PLACEHOLDER_EXPERIENCE_TOTAL;
		EXPERIENCE_LEVEL.format = ConfigMain.ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVEL;
		EXPERIENCE_LEVELUP_CURRENT.format = ConfigMain.ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_CURRENT;
		EXPERIENCE_LEVELUP_NECESSARY.format = ConfigMain.ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_NECESSARY;
		KILLS.format = ConfigMain.ADDITIONAL_PLACEHOLDER_KILLS;
		MOTD.format = ConfigMain.ADDITIONAL_PLACEHOLDER_MOTD;
		PARTY.format = ConfigMain.ADDITIONAL_PLACEHOLDER_PARTY;
		RANK_CHAT.format = ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_CHAT;
		RANK_NAME.format = ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_NAME;
	}
	
	
	public static PartiesPlaceholder getPlaceholder(String identifier) {
		PartiesPlaceholder ret = null;
		for (PartiesPlaceholder en : PartiesPlaceholder.values()) {
			if (en.name().equalsIgnoreCase(identifier)) {
				ret = en;
				break;
			}
		}
		
		if (identifier.toLowerCase().startsWith(CUSTOM_PREFIX)) {
			// Custom prefix
			ret = CUSTOM;
		}
		return ret;
	}
	public String formatPlaceholder(PartyPlayerImpl pp, PartyImpl party, String identifier) {
		String ret = "";
		if (party != null) {
			if (this.equals(CUSTOM)) {
				// Custom
				for (Map.Entry<String, String> entry : ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS.entrySet()) {
					if (identifier.equalsIgnoreCase(CUSTOM_PREFIX + entry.getKey())) {
						ret = plugin.getMessageUtils().convertAllPlaceholders(entry.getValue(), party, pp);
					}
				}
			} else {
				// Normal
				ret = plugin.getMessageUtils().convertAllPlaceholders(format, party, pp);
			}
		}
		return ret;
	}
}
