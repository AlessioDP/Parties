package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class MessageUtils {
	private PartiesPlugin plugin;
	
	protected MessageUtils(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract String convertColors(String message);
	
	public String convertPartyPlaceholders(String message, PartyImpl party) {
		return convertPartyPlaceholders(message, party, "");
	}
	public String convertPartyPlaceholders(String message, PartyImpl party, String emptyPlaceholder) {
		String ret = message;
		if (party != null) {
			ret = ret
					.replace(Constants.PLACEHOLDER_PARTY_COLOR_CODE,	party.getCurrentColor() != null ? party.getCurrentColor().getCode() : emptyPlaceholder)
					.replace(Constants.PLACEHOLDER_PARTY_COLOR_COMMAND,	party.getCurrentColor() != null ? party.getCurrentColor().getCommand() : emptyPlaceholder)
					.replace(Constants.PLACEHOLDER_PARTY_COLOR_NAME,	party.getCurrentColor() != null ? party.getCurrentColor().getName() : emptyPlaceholder)
					.replace(Constants.PLACEHOLDER_PARTY_DESC,			party.getDescription().isEmpty() ? emptyPlaceholder : party.getDescription())
					.replace(Constants.PLACEHOLDER_PARTY_EXPERIENCE_TOTAL, Integer.toString(Double.valueOf(party.getExperience()).intValue()))
					.replace(Constants.PLACEHOLDER_PARTY_EXPERIENCE_LEVEL, Integer.toString(party.getLevel()))
					.replace(Constants.PLACEHOLDER_PARTY_EXPERIENCE_LEVELUP_CURRENT, Integer.toString(party.getExpResult().getCurrentExperience()))
					.replace(Constants.PLACEHOLDER_PARTY_EXPERIENCE_LEVELUP_NECESSARY, Integer.toString(party.getExpResult().getNecessaryExperience()))
					.replace(Constants.PLACEHOLDER_PARTY_KILLS,			Integer.toString(party.getKills()))
					.replace(Constants.PLACEHOLDER_PARTY_MOTD,			party.getMotd().isEmpty() ? emptyPlaceholder : party.getMotd())
					.replace(Constants.PLACEHOLDER_PARTY_ONLINENUMBER,	Integer.toString(party.getNumberOnlinePlayers()))
					.replace(Constants.PLACEHOLDER_PARTY_PARTY,			party.getName());
		}
		return ret;
	}
	
	public String convertPlayerPlaceholders(String message, PartyPlayerImpl player) {
		String ret = message;
		if (player != null) {
			ret = ret
					.replace(Constants.PLACEHOLDER_PLAYER_PLAYER,	player.getName())
					.replace(Constants.PLACEHOLDER_PLAYER_SENDER,	player.getName())
					.replace(Constants.PLACEHOLDER_PLAYER_RANK_NAME,plugin.getRankManager().searchRankByLevel(player.getRank()).getName())
					.replace(Constants.PLACEHOLDER_PLAYER_RANK_CHAT,plugin.getRankManager().searchRankByLevel(player.getRank()).getChat());
		}
		return ret;
	}
	
	public String convertAllPlaceholders(String message, PartyImpl party, PartyPlayerImpl player) {
		return convertPlayerPlaceholders(convertPartyPlaceholders(message, party), player);
	}
}
