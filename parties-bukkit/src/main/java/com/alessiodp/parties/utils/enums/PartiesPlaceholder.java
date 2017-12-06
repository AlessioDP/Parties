package com.alessiodp.parties.utils.enums;

import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public enum PartiesPlaceholder {
	COLOR_NAME(""),
	COLOR_CODE(""),
	COLOR_COMMAND(""),
	DESC(""),
	KILLS(""),
	MOTD(""),
	PARTY(""),
	PREFIX(""),
	RANK_NAME(""),
	RANK_CHAT(""),
	SUFFIX("");
	
	private String format;
	
	PartiesPlaceholder(String frmt) {
		format = frmt;
	}
	public static void setupFormats() {
		COLOR_NAME.format = Variables.placeholders_colorname;
		COLOR_CODE.format = Variables.placeholders_colorcode;
		COLOR_COMMAND.format = Variables.placeholders_colorcommand;
		DESC.format = Variables.placeholders_desc;
		KILLS.format = Variables.placeholders_kills;
		MOTD.format = Variables.placeholders_motd;
		PARTY.format = Variables.placeholders_party;
		PREFIX.format = Variables.placeholders_prefix;
		RANK_NAME.format = Variables.placeholders_rankname;
		RANK_CHAT.format = Variables.placeholders_rankchat;
		SUFFIX.format = Variables.placeholders_suffix;
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
	public String formatPlaceholder(ThePlayer tp, Party party) {
		String ret = "";
		if (party != null) {
			ret = party.convertText(format, tp.getPlayer());
		}
		return ret;
	}
}
