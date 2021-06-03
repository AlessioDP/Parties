package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Optional;
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
	EXPERIENCE_LEVEL_ROMAN,
	EXPERIENCE_LEVEL_EXPERIENCE,
	EXPERIENCE_LEVELUP_CURRENT,
	EXPERIENCE_LEVELUP_NECESSARY,
	KILLS,
	ID,
	LEADER_NAME,
	LEADER_UUID,
	LIST_PARTIES_TOTAL,
	LIST_PARTIES_BY_NAME_NUMBER(true, "list_parties_by_name_<number>"),
	LIST_PARTIES_BY_NAME_NUMBER_PLACEHOLDER(true, "list_parties_by_name_<number>_<placeholder>"),
	LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER(true, "list_parties_by_online_members_<number>"),
	LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER_PLACEHOLDER(true, "list_parties_by_online_members_<number>_<placeholder>"),
	LIST_PARTIES_BY_MEMBERS_NUMBER(true, "list_parties_by_members_<number>"),
	LIST_PARTIES_BY_MEMBERS_NUMBER_PLACEHOLDER(true, "list_parties_by_members_<number>_<placeholder>"),
	LIST_PARTIES_BY_KILLS_NUMBER(true, "list_parties_by_kills_<number>"),
	LIST_PARTIES_BY_KILLS_NUMBER_PLACEHOLDER(true, "list_parties_by_kills_<number>_<placeholder>"),
	LIST_PARTIES_BY_EXPERIENCE_NUMBER(true, "list_parties_by_experience_<number>"),
	LIST_PARTIES_BY_EXPERIENCE_NUMBER_PLACEHOLDER(true, "list_parties_by_experience_<number>_<placeholder>"),
	LIST_PLAYERS_NUMBER(true, "list_players_<number>"),
	LIST_PLAYERS_NUMBER_PLACEHOLDER(true, "list_players_<number>_<placeholder>"),
	LIST_PLAYERS_TOTAL,
	LIST_RANK (true, "list_rank_<rank>"),
	LIST_RANK_NUMBER (true, "list_rank_<rank>_number"),
	LIST_RANK_ONLINE (true, "list_rank_<rank>_online"),
	LIST_RANK_ONLINE_NUMBER (true, "list_rank_<rank>_online_number"),
	MOTD,
	NAME,
	ONLINE,
	ONLINE_NUMBER,
	OUT_PARTY,
	PARTY,
	PLAYER_ID,
	PLAYER_NAME,
	PLAYER_NICKNAME,
	PLAYER_RANK_CHAT,
	PLAYER_RANK_NAME,
	SERVER_ID,
	SERVER_NAME,
	TAG,
	
	// Additional
	CUSTOM (true, "custom_<name>");
	
	private final PartiesPlugin plugin;
	private final boolean custom; // Ignore placeholder auto-matching by name
	@Getter private final String syntax;
	
	private static final String PREFIX_LIST_PARTIES = "list_parties_by";
	private static final String PREFIX_LIST_PLAYERS = "list_players";
	private static final String PREFIX_LIST_RANK = "list_rank_";
	private static final String SUFFIX_LIST_RANK_NUMBER = "_number";
	private static final String SUFFIX_LIST_RANK_ONLINE = "_online";
	private static final String SUFFIX_LIST_RANK_ONLINE_NUMBER = "_online_number";
	private static final String PREFIX_CUSTOM = "custom_";
	
	private static final Pattern PATTERN_LIST_PARTIES_BY_NAME = Pattern.compile("list_parties_by_name_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_ONLINE_MEMBERS = Pattern.compile("list_parties_by_online_members_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_MEMBERS = Pattern.compile("list_parties_by_members_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_KILLS = Pattern.compile("list_parties_by_kills_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_EXPERIENCE = Pattern.compile("list_parties_by_experience_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PLAYERS = Pattern.compile("list_players_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_RANK = Pattern.compile("list_rank_([a-z]+)", Pattern.CASE_INSENSITIVE);
	
	PartiesPlaceholder() {
		this(false, null);
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
			if (identifier.endsWith(SUFFIX_LIST_RANK_ONLINE_NUMBER))
				return LIST_RANK_ONLINE_NUMBER;
			else if (identifierLower.endsWith(SUFFIX_LIST_RANK_ONLINE))
				return LIST_RANK_ONLINE;
			if (identifierLower.endsWith(SUFFIX_LIST_RANK_NUMBER))
				return LIST_RANK_NUMBER;
			else
				return LIST_RANK;
		}
		
		if (identifierLower.startsWith(PREFIX_LIST_PARTIES)) {
			Matcher matcher = PATTERN_LIST_PARTIES_BY_NAME.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? LIST_PARTIES_BY_NAME_NUMBER_PLACEHOLDER : LIST_PARTIES_BY_NAME_NUMBER;
			}
			
			matcher = PATTERN_LIST_PARTIES_BY_ONLINE_MEMBERS.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER_PLACEHOLDER : LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER;
			}
			
			matcher = PATTERN_LIST_PARTIES_BY_MEMBERS.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? LIST_PARTIES_BY_MEMBERS_NUMBER_PLACEHOLDER : LIST_PARTIES_BY_MEMBERS_NUMBER;
			}
			
			matcher = PATTERN_LIST_PARTIES_BY_KILLS.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? LIST_PARTIES_BY_KILLS_NUMBER_PLACEHOLDER : LIST_PARTIES_BY_KILLS_NUMBER;
			}
			
			matcher = PATTERN_LIST_PARTIES_BY_EXPERIENCE.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? LIST_PARTIES_BY_EXPERIENCE_NUMBER_PLACEHOLDER : LIST_PARTIES_BY_EXPERIENCE_NUMBER;
			}
		}
		
		if (identifierLower.startsWith(PREFIX_LIST_PLAYERS)) {
			Matcher matcher = PATTERN_LIST_PLAYERS.matcher(identifier);
			if (matcher.find())
				return matcher.group(3) != null ? LIST_PLAYERS_NUMBER_PLACEHOLDER : LIST_PLAYERS_NUMBER;
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
			case EXPERIENCE_LEVEL_ROMAN:
				return party != null ? plugin.getMessageUtils().formatNumberAsRoman(party.getLevel()) : emptyPlaceholder;
			case EXPERIENCE_LEVEL_EXPERIENCE:
				// Casting to int to avoid printing x.0/x.5 in placeholder
				return party != null ? Integer.toString((int) party.getLevelExperience()) : emptyPlaceholder;
			case EXPERIENCE_LEVELUP_CURRENT:
				return party != null ? Integer.toString((int) party.getLevelUpCurrent()) : emptyPlaceholder;
			case EXPERIENCE_LEVELUP_NECESSARY:
				return party != null ? Integer.toString((int) party.getLevelUpNecessary()) : emptyPlaceholder;
			case KILLS:
				return party != null ? Integer.toString(party.getKills()) : emptyPlaceholder;
			case ID:
				return party != null ? party.getId().toString() : emptyPlaceholder;
			case LEADER_NAME:
				PartyPlayerImpl leader = party != null ? plugin.getPlayerManager().getPlayer(party.getLeader()) : null;
				return leader != null ? leader.getName() : emptyPlaceholder;
			case LEADER_UUID:
				return (party != null && party.getLeader() != null) ? party.getLeader().toString() : emptyPlaceholder;
			case LIST_PARTIES_TOTAL:
				return Integer.toString(plugin.getDatabaseManager().getListPartiesNumber());
			case LIST_PARTIES_BY_NAME_NUMBER:
			case LIST_PARTIES_BY_NAME_NUMBER_PLACEHOLDER:
				return getListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_NAME, PartiesDatabaseManager.ListOrder.NAME);
			case LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER:
			case LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER_PLACEHOLDER:
				return getListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_ONLINE_MEMBERS, PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS);
			case LIST_PARTIES_BY_MEMBERS_NUMBER:
			case LIST_PARTIES_BY_MEMBERS_NUMBER_PLACEHOLDER:
				return getListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_MEMBERS, PartiesDatabaseManager.ListOrder.MEMBERS);
			case LIST_PARTIES_BY_KILLS_NUMBER:
			case LIST_PARTIES_BY_KILLS_NUMBER_PLACEHOLDER:
				return getListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_KILLS, PartiesDatabaseManager.ListOrder.KILLS);
			case LIST_PARTIES_BY_EXPERIENCE_NUMBER:
			case LIST_PARTIES_BY_EXPERIENCE_NUMBER_PLACEHOLDER:
				return getListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_EXPERIENCE, PartiesDatabaseManager.ListOrder.EXPERIENCE);
			case LIST_PLAYERS_NUMBER:
			case LIST_PLAYERS_NUMBER_PLACEHOLDER:
				return getListPlayers(party, identifier, emptyPlaceholder);
			case LIST_PLAYERS_TOTAL:
				return Integer.toString(plugin.getDatabaseManager().getListPlayersInPartyNumber());
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
								if (pl.getName() != null && !pl.getName().isEmpty()) {
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
							} else
								sb.append(Messages.PARTIES_LIST_UNKNOWN);
						}
						return sb.toString().isEmpty() ? Messages.PARTIES_LIST_EMPTY : sb.toString();
					}
					return emptyPlaceholder;
				}
				return null;
			case LIST_RANK_NUMBER:
			case LIST_RANK_ONLINE_NUMBER:
				matcher = PATTERN_LIST_RANK.matcher(identifier);
				if (matcher.find()) {
					if (party != null) {
						if (this.equals(LIST_RANK_NUMBER)) {
							return Integer.toString(getAllMembers(party, matcher.group(1)).size());
						} else {
							return Long.toString(getAllMembers(party, matcher.group(1)).stream().filter(pp -> {
								OfflineUser offlinePlayer = plugin.getOfflinePlayer(pp.getPlayerUUID());
								return offlinePlayer.isOnline();
							}).count());
						}
					}
					return emptyPlaceholder;
				}
				return null;
			case MOTD:
				return party != null && party.getMotd() != null ? party.getMotd() : emptyPlaceholder;
			case NAME:
			case PARTY:
				return party != null && party.getName() != null ? party.getName() : emptyPlaceholder;
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
			case PLAYER_ID:
				if (player != null) {
					return player.getPlayerUUID().toString();
				}
				return emptyPlaceholder;
			case PLAYER_NAME:
				if (player != null) {
					return player.getName();
				}
				return emptyPlaceholder;
			case PLAYER_NICKNAME:
				if (player != null) {
					if (player.getNickname() != null)
						return ConfigParties.ADDITIONAL_NICKNAME_FORMAT.replace("%nickname%", player.getNickname());
					return player.getName();
				}
				return emptyPlaceholder;
			case PLAYER_RANK_CHAT:
				return player != null && player.isInParty() ? plugin.getRankManager().searchRankByLevel(player.getRank()).getChat() : emptyPlaceholder;
			case PLAYER_RANK_NAME:
				return player != null && player.isInParty() ? plugin.getRankManager().searchRankByLevel(player.getRank()).getName() : emptyPlaceholder;
			case SERVER_ID:
				return plugin.getServerId(player);
			case SERVER_NAME:
				return plugin.getServerName(player);
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
	
	private String getListPartiesBy(PartyPlayerImpl player, String identifier, String emptyPlaceholder, Pattern pattern, PartiesDatabaseManager.ListOrder order) {
		Matcher matcher = pattern.matcher(identifier);
		if (matcher.find()) {
			try {
				int number = Integer.parseInt(matcher.group(1));
				Optional<PartyImpl> partyOptional = plugin.getDatabaseManager().getListParties(order, 1, number - 1).stream().findAny();
				
				if (partyOptional.isPresent()) {
					if (matcher.group(3) != null) {
						PartiesPlaceholder newPlaceholder = PartiesPlaceholder.getPlaceholder(matcher.group(3));
						
						return newPlaceholder != null ? newPlaceholder.formatPlaceholder(player, partyOptional.get(), matcher.group(3), emptyPlaceholder) : null;
					}
					
					return partyOptional.get().getName() != null ? partyOptional.get().getName() : emptyPlaceholder;
				}
				return emptyPlaceholder;
			} catch (Exception ignored) {}
		}
		return null;
	}
	
	private String getListPlayers(PartyImpl party, String identifier, String emptyPlaceholder) {
		if (party != null) {
			Matcher matcher = PATTERN_LIST_PLAYERS.matcher(identifier);
			if (matcher.find()) {
				try {
					int number = Integer.parseInt(matcher.group(1));
					int n = 1;
					for (UUID uuid : party.getMembers()) {
						if (n == number) {
							PartyPlayerImpl pl = plugin.getPlayerManager().getPlayer(uuid);
							if (pl != null) {
								if (matcher.group(3) != null) {
									PartiesPlaceholder newPlaceholder = PartiesPlaceholder.getPlaceholder(matcher.group(3));
									
									return newPlaceholder != null ? newPlaceholder.formatPlaceholder(pl, party, matcher.group(3), emptyPlaceholder) : null;
								}
								
								return pl.getName() != null ? pl.getName() : emptyPlaceholder;
							}
							break;
						}
						n++;
					}
					return emptyPlaceholder;
				} catch (Exception ignored) {}
			}
			return null;
		}
		return emptyPlaceholder;
	}
}
