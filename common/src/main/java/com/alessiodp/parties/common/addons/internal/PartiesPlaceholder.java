package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

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
	SUFFIX;
	
	private static PartiesPlugin plugin;
	private String format;
	
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
		return ret;
	}
	public String formatPlaceholder(PartyPlayerImpl pp, PartyImpl party) {
		String ret = "";
		if (party != null) {
			ret = plugin.getMessageUtils().convertAllPlaceholders(format, party, pp);
		}
		return ret;
	}
}
