package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PartiesPlaceholder {
	COLOR_CODE,
	COLOR_COMMAND,
	COLOR_NAME,
	DESC,
	EXPERIENCE_TOTAL,
	EXPERIENCE_LEVEL,
	EXPERIENCE_LEVEL_EXPERIENCE,
	EXPERIENCE_LEVELUP_CURRENT,
	EXPERIENCE_LEVELUP_NECESSARY,
	KILLS,
	LEADER_NAME,
	LEADER_UUID,
	LIST_RANK (true, "list_<rank>"),
	LIST_RANK_NUMBER (true, "list_<rank>_number"),
	LIST_RANK_ONLINE (true, "list_<rank>_online"),
	MOTD,
	ONLINE,
	ONLINE_NUMBER,
	OUT_PARTY,
	PARTY,
	RANK_CHAT,
	RANK_NAME,
	TAG,
	
	// Additional
	CUSTOM (true, "custom_<name>");
	
	private final PartiesPlugin plugin;
	private final boolean custom; // Ignore placeholder auto-matching by name
	@Getter private final String syntax;
	
	private static final String PREFIX_LIST_RANK = "list_";
	private static final String SUFFIX_LIST_RANK_NUMBER = "_number";
	private static final String SUFFIX_LIST_RANK_ONLINE = "_online";
	private static final String PREFIX_CUSTOM = "custom_";
	
	private static final Pattern PATTERN_LIST_RANK = Pattern.compile("list_([a-zA-Z]+)", Pattern.CASE_INSENSITIVE);
	
	PartiesPlaceholder() {
		this(false);
	}
	
	PartiesPlaceholder(boolean custom) {
		this(custom, null);
	}
	
	PartiesPlaceholder(boolean custom, String syntax){
		this.custom = custom;
		this.syntax = syntax != null ? syntax : CommonUtils.toLowerCase(this.name());
		plugin = ((PartiesPlugin) PartiesPlugin.getInstance());
	}
	
	public static PartiesPlaceholder getPlaceholder(String identifier) {
		String identifierLower = CommonUtils.toLowerCase(identifier);
		for (PartiesPlaceholder en : PartiesPlaceholder.values()) {
			if (!en.custom && en.syntax.equals(identifierLower)) {
				return en;
			}
		}
		
		if (identifierLower.startsWith(PREFIX_CUSTOM))
			return CUSTOM;
		
		if (identifierLower.startsWith(PREFIX_LIST_RANK)) {
			if (identifierLower.endsWith(SUFFIX_LIST_RANK_NUMBER))
				return LIST_RANK_NUMBER;
			else if (identifierLower.endsWith(SUFFIX_LIST_RANK_ONLINE))
				return LIST_RANK_ONLINE;
			else
				return LIST_RANK;
		}
		return null;
	}
	
	public String formatPlaceholder(PartyPlayerImpl player, PartyImpl party, String identifier) {
		return formatPlaceholder(player, party, identifier, "");
	}
	
	public String formatPlaceholder(PartyPlayerImpl player, PartyImpl party, String identifier, String emptyPlaceholder) {
		Matcher matcher;
		switch (this) {
			case COLOR_CODE:
				return party != null && party.getCurrentColor() != null ? party.getCurrentColor().getCode() : emptyPlaceholder;
			case COLOR_COMMAND:
				return party != null && party.getCurrentColor() != null ? party.getCurrentColor().getCommand() : emptyPlaceholder;
			case COLOR_NAME:
				return party != null && party.getCurrentColor() != null ? party.getCurrentColor().getName() : emptyPlaceholder;
			case DESC:
				return party != null && party.getDescription() != null ? party.getDescription() : emptyPlaceholder;
			case EXPERIENCE_TOTAL:
				return party != null ? Integer.toString(Double.valueOf(party.getExperience()).intValue()) : emptyPlaceholder;
			case EXPERIENCE_LEVEL:
				return party != null ? Integer.toString(party.getLevel()) : emptyPlaceholder;
			case EXPERIENCE_LEVEL_EXPERIENCE:
				return party != null ? Integer.toString(party.getExpResult().getLevelExperience()) : emptyPlaceholder;
			case EXPERIENCE_LEVELUP_CURRENT:
				return party != null ? Integer.toString(party.getExpResult().getCurrentExperience()) : emptyPlaceholder;
			case EXPERIENCE_LEVELUP_NECESSARY:
				return party != null ? Integer.toString(party.getExpResult().getNecessaryExperience()) : emptyPlaceholder;
			case KILLS:
				return party != null ? Integer.toString(party.getKills()) : emptyPlaceholder;
			case LEADER_NAME:
				PartyPlayerImpl leader = party != null ? plugin.getPlayerManager().getPlayer(party.getLeader()) : null;
				return leader != null ? leader.getName() : emptyPlaceholder;
			case LEADER_UUID:
				return (party != null && party.getLeader() != null) ?  party.getLeader().toString() : emptyPlaceholder;
			case LIST_RANK:
			case LIST_RANK_ONLINE:
				matcher = PATTERN_LIST_RANK.matcher(identifier);
				if (matcher.find()) {
					if (party != null) {
						ArrayList<PartyPlayerImpl> members = getAllMembers(party, matcher.group(1));
						StringBuilder sb = new StringBuilder();
						for (PartyPlayerImpl pl : members) {
							if (sb.length() > 0) {
								sb.append(Messages.PARTIES_LIST_SEPARATOR);
							}
							OfflineUser offlinePlayer = plugin.getOfflinePlayer(pl.getPlayerUUID());
							if (offlinePlayer != null) {
								if (offlinePlayer.isOnline() && !pl.isVanished()) {
									sb.append(
											plugin.getMessageUtils().convertPlaceholders(
													Messages.PARTIES_LIST_ONLINEFORMAT,
													pl,
													party
											)
									);
								} else if (this.equals(LIST_RANK)) {
									sb.append(
											plugin.getMessageUtils().convertPlaceholders(
													Messages.PARTIES_LIST_OFFLINEFORMAT,
													pl,
													party
											)
									);
								}
							} else
								sb.append(Messages.PARTIES_LIST_UNKNOWN);
						}
						return sb.toString().isEmpty() ? Messages.PARTIES_LIST_EMPTY : sb.toString();
					}
					return emptyPlaceholder;
				}
				return null;
			case LIST_RANK_NUMBER:
				matcher = PATTERN_LIST_RANK.matcher(identifier);
				if (matcher.find()) {
					if (party != null) {
						return Integer.toString(getAllMembers(party, matcher.group(1)).size());
					}
					return emptyPlaceholder;
				}
				return null;
			case MOTD:
				return party != null && party.getMotd() != null ? party.getMotd() : emptyPlaceholder;
			case ONLINE:
				StringBuilder sb = new StringBuilder();
				if (party != null) {
					Set<PartyPlayer> list = party.getOnlineMembers(false);
					if (list.size() == 0)
						sb.append(Messages.PARTIES_LIST_EMPTY);
					else {
						for (PartyPlayer pp : list) {
							if (sb.length() > 0) {
								sb.append(Messages.PARTIES_LIST_SEPARATOR);
							}
							sb.append(plugin.getMessageUtils().convertPlaceholders(
									Messages.PARTIES_LIST_ONLINEFORMAT,
									(PartyPlayerImpl) pp,
									party
							));
						}
					}
					return sb.toString();
				}
				return emptyPlaceholder;
			case ONLINE_NUMBER:
				return party != null ? Integer.toString(party.getOnlineMembers(false).size()) : emptyPlaceholder;
			case OUT_PARTY:
				return party == null ? Messages.PARTIES_OUT_PARTY : emptyPlaceholder;
			case PARTY:
				return party != null ? party.getName() : emptyPlaceholder;
			case RANK_CHAT:
				return player != null && player.isInParty() ? plugin.getRankManager().searchRankByLevel(player.getRank()).getChat() : emptyPlaceholder;
			case RANK_NAME:
				return player != null && player.isInParty() ? plugin.getRankManager().searchRankByLevel(player.getRank()).getName() : emptyPlaceholder;
			case TAG:
				return party != null && party.getTag() != null ? party.getTag() : emptyPlaceholder;
			case CUSTOM:
				String custom = ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS.get(identifier.substring(PREFIX_CUSTOM.length()));
				if (custom != null) {
					if (party != null)
						return plugin.getMessageUtils().convertPlaceholders(custom, player, party);
					else
						return emptyPlaceholder;
				}
			default:
				return null;
		}
	}
	
	private ArrayList<PartyPlayerImpl> getAllMembers(PartyImpl party, String rankName) {
		ArrayList<PartyPlayerImpl> ret = new ArrayList<>();
		PartyRankImpl rank = rankName != null ? plugin.getRankManager().searchRankByHardName(rankName) : null;
		for (UUID playerUUID : party.getMembers()) {
			PartyPlayerImpl pl = plugin.getPlayerManager().getPlayer(playerUUID);
			
			if (rank == null || rank.getLevel() == pl.getRank())
				ret.add(pl);
		}
		return ret;
	}
}
