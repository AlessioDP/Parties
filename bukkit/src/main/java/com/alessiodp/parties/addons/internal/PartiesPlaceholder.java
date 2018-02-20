package com.alessiodp.parties.addons.internal;

import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public enum PartiesPlaceholder {
	COLOR_CODE(""),
	COLOR_COMMAND(""),
	COLOR_NAME(""),
	DESC(""),
	KILLS(""),
	MOTD(""),
	PARTY(""),
	PREFIX(""),
	RANK_CHAT(""),
	RANK_NAME(""),
	SUFFIX("");
	
	private String format;
	
	PartiesPlaceholder(String frmt) {
		format = frmt;
	}
	public static void setupFormats() {
		COLOR_CODE.format = ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_CODE;
		COLOR_COMMAND.format = ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_COMMAND;
		COLOR_NAME.format = ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_NAME;
		DESC.format = ConfigMain.ADDITIONAL_PLACEHOLDER_DESC;
		KILLS.format = ConfigMain.ADDITIONAL_PLACEHOLDER_KILLS;
		MOTD.format = ConfigMain.ADDITIONAL_PLACEHOLDER_MOTD;
		PARTY.format = ConfigMain.ADDITIONAL_PLACEHOLDER_PARTY;
		PREFIX.format = ConfigMain.ADDITIONAL_PLACEHOLDER_PREFIX;
		RANK_CHAT.format = ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_CHAT;
		RANK_NAME.format = ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_NAME;
		SUFFIX.format = ConfigMain.ADDITIONAL_PLACEHOLDER_SUFFIX;
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
	public String formatPlaceholder(PartyPlayerEntity pp, PartyEntity party) {
		String ret = "";
		if (party != null) {
			ret = PartiesUtils.convertAllPlaceholders(format, party, pp);
		}
		return ret;
	}
}
