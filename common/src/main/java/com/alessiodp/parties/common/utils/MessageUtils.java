package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class MessageUtils {
	private final PartiesPlugin plugin;
	private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([%][^%]+[%])");
	
	public String convertPartyPlaceholders(String message, PartyImpl party) {
		return convertPartyPlaceholders(message, party, "");
	}
	
	public String convertPartyPlaceholders(String message, PartyImpl party, String emptyPlaceholder) {
		String ret = message;
		String replacement;
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
		while(matcher.find()) {
			String identifier = matcher.group(0);
			switch (identifier.toLowerCase()) {
				case PartiesConstants.PLACEHOLDER_PARTY_COLOR_CODE:
					replacement = party != null && party.getCurrentColor() != null ? party.getCurrentColor().getCode() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_COLOR_COMMAND:
					replacement = party != null && party.getCurrentColor() != null ? party.getCurrentColor().getCommand() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_COLOR_NAME:
					replacement = party != null && party.getCurrentColor() != null ? party.getCurrentColor().getName() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_DESC:
					replacement = party != null && !party.getDescription().isEmpty() ? party.getDescription() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_EXPERIENCE_TOTAL:
					replacement = party != null ? Integer.toString(Double.valueOf(party.getExperience()).intValue()) : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_EXPERIENCE_LEVEL:
					replacement = party != null ? Integer.toString(party.getLevel()) : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_EXPERIENCE_LEVELUP_CURRENT:
					replacement = party != null ? Integer.toString(party.getExpResult().getCurrentExperience()) : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_EXPERIENCE_LEVELUP_NECESSARY:
					replacement = party != null ? Integer.toString(party.getExpResult().getNecessaryExperience()) : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_KILLS:
					replacement = party != null ? Integer.toString(party.getKills()) : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_MOTD:
					replacement = party != null && !party.getMotd().isEmpty() ? party.getMotd() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_ONLINENUMBER:
					replacement = party != null ? Integer.toString(party.getOnlineMembers(false).size()) : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PARTY_PARTY:
					replacement = party != null ? party.getName() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				
			}
		}
		return ret;
	}
	
	public String convertPlayerPlaceholders(String message, PartyPlayerImpl player) {
		return convertPlayerPlaceholders(message, player, "");
	}
	
	public String convertPlayerPlaceholders(String message, PartyPlayerImpl player, String emptyPlaceholder) {
		String ret = message;
		String replacement;
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
		while(matcher.find()) {
			String identifier = matcher.group(1);
			switch (identifier.toLowerCase()) {
				case PartiesConstants.PLACEHOLDER_PLAYER_NAME:
				case PartiesConstants.PLACEHOLDER_PLAYER_USER:
					replacement = player != null ? player.getName() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PLAYER_RANK_NAME:
					replacement = player != null && !player.getPartyName().isEmpty() ? plugin.getRankManager().searchRankByLevel(player.getRank()).getName() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
				case PartiesConstants.PLACEHOLDER_PLAYER_RANK_CHAT:
					replacement = player != null && !player.getPartyName().isEmpty() ? plugin.getRankManager().searchRankByLevel(player.getRank()).getChat() : emptyPlaceholder;
					ret = ret.replace(identifier, replacement);
					break;
			}
		}
		return ret;
	}
	
	public String convertAllPlaceholders(String message, PartyImpl party, PartyPlayerImpl player) {
		return convertPlayerPlaceholders(convertPartyPlaceholders(message, party), player);
	}
	
	public void sendMessage(User receiver, String message, PartyPlayerImpl victim, PartyImpl party) {
		if (receiver != null && message != null && !message.isEmpty()) {
			String formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(message, party, victim);
			
			if (receiver.isPlayer())
				formattedMessage = plugin.getColorUtils().convertColors(formattedMessage);
			else
				formattedMessage = plugin.getColorUtils().removeColors(formattedMessage);
			
			receiver.sendMessage(formattedMessage, false);
		}
	}
}
