package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	LEADER_NAME,
	LEADER_UUID,
	LIST_RANK,
	LIST_RANK_NUMBER,
	LIST_RANK_ONLINE,
	MOTD,
	ONLINE,
	ONLINE_NUMBER,
	OUT_PARTY,
	PARTY,
	RANK_CHAT,
	RANK_NAME,
	
	// Additional
	CUSTOM;
	
	private static PartiesPlugin plugin;
	private String format;
	private static final String LIST_RANK_PREFIX = "list_";
	private static final String LIST_RANK_NUMBER_SUFFIX = "_number";
	private static final String LIST_RANK_ONLINE_SUFFIX = "_online";
	private static final String CUSTOM_PREFIX = "custom_";
	
	private static final Pattern LIST_RANK_PATTERN = Pattern.compile("list_([a-zA-Z]+)", Pattern.CASE_INSENSITIVE);
	
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
		LEADER_NAME.format = ConfigMain.ADDITIONAL_PLACEHOLDER_LEADER_NAME;
		LEADER_UUID.format = ConfigMain.ADDITIONAL_PLACEHOLDER_LEADER_UUID;
		LIST_RANK.format = ConfigMain.ADDITIONAL_PLACEHOLDER_LIST_RANK;
		LIST_RANK_NUMBER.format = ConfigMain.ADDITIONAL_PLACEHOLDER_LIST_RANK_NUMBER;
		LIST_RANK_ONLINE.format = ConfigMain.ADDITIONAL_PLACEHOLDER_LIST_RANK_ONLINE;
		MOTD.format = ConfigMain.ADDITIONAL_PLACEHOLDER_MOTD;
		ONLINE.format = ConfigMain.ADDITIONAL_PLACEHOLDER_ONLINE;
		ONLINE_NUMBER.format = ConfigMain.ADDITIONAL_PLACEHOLDER_ONLINE_NUMBER;
		OUT_PARTY.format = ConfigMain.ADDITIONAL_PLACEHOLDER_OUTPARTY;
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
		
		if (identifier.toLowerCase(Locale.ENGLISH).startsWith(CUSTOM_PREFIX)) {
			// Custom prefix
			ret = CUSTOM;
		}
		if (identifier.toLowerCase(Locale.ENGLISH).startsWith(LIST_RANK_PREFIX)) {
			if (identifier.toLowerCase(Locale.ENGLISH).endsWith(LIST_RANK_NUMBER_SUFFIX))
				ret = LIST_RANK_NUMBER;
			else if (identifier.toLowerCase(Locale.ENGLISH).endsWith(LIST_RANK_ONLINE_SUFFIX))
				ret = LIST_RANK_ONLINE;
			else
				ret = LIST_RANK;
		}
		return ret;
	}
	public String formatPlaceholder(PartyPlayerImpl pp, PartyImpl party, String identifier) {
		String ret = "";
		if (this.equals(CUSTOM)) {
			// Custom
			String custom = ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS.get(identifier.substring(CUSTOM_PREFIX.length()));
			if (custom != null) {
				if (party != null) {
					ret = plugin.getMessageUtils().convertAllPlaceholders(custom, party, pp);
				}
			} else
				ret = null;
		} else if (this.equals(LIST_RANK)
				|| this.equals(LIST_RANK_NUMBER)
				|| this.equals(LIST_RANK_ONLINE)) {
			// List rank
			Matcher matcher = LIST_RANK_PATTERN.matcher(identifier);
			if (matcher.find()) {
				String rank = matcher.group(1);
				ret = plugin.getMessageUtils().convertAllPlaceholders(format
						.replace("{rank}", rank), party, pp);
			}
		} else {
			// Normal
			ret = plugin.getMessageUtils().convertAllPlaceholders(format, party, pp);
		}
		return ret;
	}
}
