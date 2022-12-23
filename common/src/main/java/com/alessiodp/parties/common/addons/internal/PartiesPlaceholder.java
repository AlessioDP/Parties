package com.alessiodp.parties.common.addons.internal;

import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.Pair;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	LIST_RANK (true, "list_rank_<rank>"),
	LIST_RANK_NUMBER (true, "list_rank_<rank>_<number>"),
	LIST_RANK_NUMBER_PLACEHOLDER (true, "list_rank_<rank>_<number>_<placeholder>"),
	LIST_RANK_TOTAL (true, "list_rank_<rank>_total"),
	LIST_RANK_ONLINE (true, "list_rank_<rank>_online"),
	LIST_RANK_ONLINE_NUMBER (true, "list_rank_<rank>_online_<number>"),
	LIST_RANK_ONLINE_NUMBER_PLACEHOLDER (true, "list_rank_<rank>_online_<number>_<placeholder>"),
	LIST_RANK_ONLINE_TOTAL (true, "list_rank_<rank>_online_total"),
	LIST_RANK_OFFLINE (true, "list_rank_<rank>_offline"),
	LIST_RANK_OFFLINE_NUMBER (true, "list_rank_<rank>_offline_<number>"),
	LIST_RANK_OFFLINE_NUMBER_PLACEHOLDER (true, "list_rank_<rank>_offline_<number>_<placeholder>"),
	LIST_RANK_OFFLINE_TOTAL (true, "list_rank_<rank>_offline_total"),
	MEMBERS,
	MEMBERS_NUMBER (true, "members_<number>"),
	MEMBERS_NUMBER_PLACEHOLDER (true, "members_<number>_<placeholder>"),
	MEMBERS_TOTAL,
	MEMBERS_ONLINE,
	MEMBERS_ONLINE_NUMBER (true, "members_online_<number>"),
	MEMBERS_ONLINE_NUMBER_PLACEHOLDER (true, "members_online_<number>_<placeholder>"),
	MEMBERS_ONLINE_TOTAL,
	MEMBERS_OFFLINE,
	MEMBERS_OFFLINE_NUMBER (true, "members_offline_<number>"),
	MEMBERS_OFFLINE_NUMBER_PLACEHOLDER (true, "members_offline_<number>_<placeholder>"),
	MEMBERS_OFFLINE_TOTAL,
	MOTD,
	NAME,
	OUT_PARTY,
	PARTY,
	PLAYER_DISPLAY_NAME,
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
	private static final String PREFIX_LIST_RANK = "list_rank_";
	private static final String PREFIX_MEMBERS = "members_";
	private static final String PREFIX_CUSTOM = "custom_";
	
	private static final Pattern PATTERN_LIST_PARTIES_BY_NAME = Pattern.compile("list_parties_by_name_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_ONLINE_MEMBERS = Pattern.compile("list_parties_by_online_members_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_MEMBERS = Pattern.compile("list_parties_by_members_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_KILLS = Pattern.compile("list_parties_by_kills_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_PARTIES_BY_EXPERIENCE = Pattern.compile("list_parties_by_experience_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_RANK = Pattern.compile("list_rank_([a-z]+)(_([a-z0-9]+)(_([a-z0-9_]+))?)?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_RANK_ONLINE = Pattern.compile("list_rank_([a-z]+)_online(_([a-z0-9]+)(_([a-z0-9_]+))?)?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_LIST_RANK_OFFLINE = Pattern.compile("list_rank_([a-z]+)_offline(_([a-z0-9]+)(_([a-z0-9_]+))?)?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_MEMBERS = Pattern.compile("members_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_MEMBERS_ONLINE = Pattern.compile("members_online_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN_MEMBERS_OFFLINE = Pattern.compile("members_offline_([0-9]+)(_([a-z0-9_]+))?", Pattern.CASE_INSENSITIVE);
	
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
		
		if (identifierLower.startsWith(PREFIX_LIST_RANK)) {
			Matcher matcher = PATTERN_LIST_RANK_ONLINE.matcher(identifier);
			if (matcher.find()) {
				if (matcher.group(3) != null) {
					if (matcher.group(3).equalsIgnoreCase("total"))
						return LIST_RANK_ONLINE_TOTAL;
					return matcher.group(5) != null ? LIST_RANK_ONLINE_NUMBER_PLACEHOLDER : LIST_RANK_ONLINE_NUMBER;
				}
				return LIST_RANK_ONLINE;
			}
			
			matcher = PATTERN_LIST_RANK_OFFLINE.matcher(identifier);
			if (matcher.find()) {
				if (matcher.group(3) != null) {
					if (matcher.group(3).equalsIgnoreCase("total"))
						return LIST_RANK_OFFLINE_TOTAL;
					return matcher.group(5) != null ? LIST_RANK_OFFLINE_NUMBER_PLACEHOLDER : LIST_RANK_OFFLINE_NUMBER;
				}
				return LIST_RANK_OFFLINE;
			}
			
			matcher = PATTERN_LIST_RANK.matcher(identifier);
			if (matcher.find()) {
				if (matcher.group(3) != null) {
					if (matcher.group(3).equalsIgnoreCase("total"))
						return LIST_RANK_TOTAL;
					return matcher.group(5) != null ? LIST_RANK_NUMBER_PLACEHOLDER : LIST_RANK_NUMBER;
				}
				return LIST_RANK;
			}
		}
		
		if (identifierLower.startsWith(PREFIX_MEMBERS)) {
			Matcher matcher = PATTERN_MEMBERS.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? MEMBERS_NUMBER_PLACEHOLDER : MEMBERS_NUMBER;
			}
			
			matcher = PATTERN_MEMBERS_ONLINE.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? MEMBERS_ONLINE_NUMBER_PLACEHOLDER : MEMBERS_ONLINE_NUMBER;
			}
			
			matcher = PATTERN_MEMBERS_OFFLINE.matcher(identifier);
			if (matcher.find()) {
				return matcher.group(3) != null ? MEMBERS_OFFLINE_NUMBER_PLACEHOLDER : MEMBERS_OFFLINE_NUMBER;
			}
		}
		
		return null;
	}
	
	public String formatPlaceholder(PartyPlayerImpl player, PartyImpl party, String identifier) {
		return formatPlaceholder(player, party, identifier, "");
	}
	
	public String formatPlaceholder(PartyPlayerImpl player, PartyImpl party, String identifier, String emptyPlaceholder) {
		Matcher matcher;
		StringBuilder sb;
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
				return parseListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_NAME, PartiesDatabaseManager.ListOrder.NAME);
			case LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER:
			case LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER_PLACEHOLDER:
				return parseListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_ONLINE_MEMBERS, PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS);
			case LIST_PARTIES_BY_MEMBERS_NUMBER:
			case LIST_PARTIES_BY_MEMBERS_NUMBER_PLACEHOLDER:
				return parseListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_MEMBERS, PartiesDatabaseManager.ListOrder.MEMBERS);
			case LIST_PARTIES_BY_KILLS_NUMBER:
			case LIST_PARTIES_BY_KILLS_NUMBER_PLACEHOLDER:
				return parseListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_KILLS, PartiesDatabaseManager.ListOrder.KILLS);
			case LIST_PARTIES_BY_EXPERIENCE_NUMBER:
			case LIST_PARTIES_BY_EXPERIENCE_NUMBER_PLACEHOLDER:
				return parseListPartiesBy(player, identifier, emptyPlaceholder, PATTERN_LIST_PARTIES_BY_EXPERIENCE, PartiesDatabaseManager.ListOrder.EXPERIENCE);
			case LIST_RANK:
			case LIST_RANK_ONLINE:
			case LIST_RANK_OFFLINE:
				matcher = PATTERN_LIST_RANK.matcher(identifier);
				if (matcher.find()) {
					if (party != null) {
						List<PartyPlayerImpl> members = getMembersByRank(party, matcher.group(1));
						if (members != null) {
							if (this.equals(LIST_RANK_ONLINE))
								filterMembers(members, true);
							else if (this.equals(LIST_RANK_OFFLINE))
								filterMembers(members, false);
							
							sb = new StringBuilder();
							for (PartyPlayerImpl pl : members) {
								if (sb.length() > 0) {
									sb.append(Messages.PARTIES_LIST_SEPARATOR);
								}
								OfflineUser offlinePlayer = plugin.getOfflinePlayer(pl.getPlayerUUID());
								if (offlinePlayer != null) {
									if (!pl.getName().isEmpty()) {
										if (offlinePlayer.isOnline() && !pl.isVanished()) {
											sb.append(
													plugin.getMessageUtils().convertPlaceholders(
															Messages.PARTIES_LIST_ONLINEFORMAT,
															pl,
															party
													)
											);
										} else {
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
						} else {
							return null;
						}
					}
					return emptyPlaceholder;
				}
				return null;
			case LIST_RANK_TOTAL:
			case LIST_RANK_ONLINE_TOTAL:
			case LIST_RANK_OFFLINE_TOTAL:
				matcher = PATTERN_LIST_RANK.matcher(identifier);
				if (matcher.find()) {
					if (party != null) {
						List<PartyPlayerImpl> list;
						switch (this) {
							case LIST_RANK_TOTAL:
								list = getMembersByRank(party, matcher.group(1));
								return list != null ? Integer.toString(list.size()) : "0";
							case LIST_RANK_ONLINE_TOTAL:
								list = getMembersByRank(party, matcher.group(1));
								return list != null ? Long.toString(filterMembers(list, true).size()) : null;
							case LIST_RANK_OFFLINE_TOTAL:
								list = getMembersByRank(party, matcher.group(1));
								return list != null ? Long.toString(filterMembers(list, false).size()) : null;
							default:
								// Nothing to do
						}
					}
					return emptyPlaceholder;
				}
				return null;
			case LIST_RANK_NUMBER:
			case LIST_RANK_NUMBER_PLACEHOLDER:
				return parseListRank(party, identifier, emptyPlaceholder, PATTERN_LIST_RANK);
			case LIST_RANK_ONLINE_NUMBER:
			case LIST_RANK_ONLINE_NUMBER_PLACEHOLDER:
				return parseListRank(party, identifier, emptyPlaceholder, PATTERN_LIST_RANK_ONLINE);
			case LIST_RANK_OFFLINE_NUMBER:
			case LIST_RANK_OFFLINE_NUMBER_PLACEHOLDER:
				return parseListRank(party, identifier, emptyPlaceholder, PATTERN_LIST_RANK_OFFLINE);
			case MEMBERS:
				sb = new StringBuilder();
				if (party != null) {
					List<Pair<PartyPlayerImpl, Boolean>> list = getMembers(party);
					if (list.size() == 0)
						sb.append(Messages.PARTIES_LIST_EMPTY);
					else {
						for (Pair<PartyPlayerImpl, Boolean> p : list) {
							if (sb.length() > 0) {
								sb.append(Messages.PARTIES_LIST_SEPARATOR);
							}
							sb.append(plugin.getMessageUtils().convertPlaceholders(
									p.getValue() ? Messages.PARTIES_LIST_ONLINEFORMAT : Messages.PARTIES_LIST_OFFLINEFORMAT,
									p.getKey(),
									party
							));
						}
					}
					return sb.toString();
				}
				return emptyPlaceholder;
			case MEMBERS_NUMBER:
			case MEMBERS_NUMBER_PLACEHOLDER:
				return parseMembers(party, identifier, emptyPlaceholder, PATTERN_MEMBERS);
			case MEMBERS_TOTAL:
				return party != null ? Integer.toString(getMembers(party).size()) : "0";
			case MEMBERS_ONLINE:
				sb = new StringBuilder();
				if (party != null) {
					List<Pair<PartyPlayerImpl, Boolean>> list = getMembers(party);
					list = list.stream().filter(Pair::getValue).collect(Collectors.toList());
					if (list.size() == 0)
						sb.append(Messages.PARTIES_LIST_EMPTY);
					else {
						for (Pair<PartyPlayerImpl, Boolean> p : list) {
							if (sb.length() > 0) {
								sb.append(Messages.PARTIES_LIST_SEPARATOR);
							}
							sb.append(plugin.getMessageUtils().convertPlaceholders(
									Messages.PARTIES_LIST_ONLINEFORMAT,
									p.getKey(),
									party
							));
						}
					}
					return sb.toString();
				}
				return emptyPlaceholder;
			case MEMBERS_ONLINE_NUMBER:
			case MEMBERS_ONLINE_NUMBER_PLACEHOLDER:
				return parseMembers(party, identifier, emptyPlaceholder, PATTERN_MEMBERS_ONLINE);
			case MEMBERS_ONLINE_TOTAL:
				if (party != null) {
					List<Pair<PartyPlayerImpl, Boolean>> list = getMembers(party);
					list = list.stream().filter(Pair::getValue).collect(Collectors.toList());
					return Integer.toString(list.size());
				}
				return "0";
			case MEMBERS_OFFLINE:
				sb = new StringBuilder();
				if (party != null) {
					List<Pair<PartyPlayerImpl, Boolean>> list = getMembers(party);
					list = list.stream().filter(p -> !p.getValue()).collect(Collectors.toList());
					if (list.size() == 0)
						sb.append(Messages.PARTIES_LIST_EMPTY);
					else {
						for (Pair<PartyPlayerImpl, Boolean> p : list) {
							if (sb.length() > 0) {
								sb.append(Messages.PARTIES_LIST_SEPARATOR);
							}
							sb.append(plugin.getMessageUtils().convertPlaceholders(
									Messages.PARTIES_LIST_OFFLINEFORMAT,
									p.getKey(),
									party
							));
						}
					}
					return sb.toString();
				}
				return emptyPlaceholder;
			case MEMBERS_OFFLINE_NUMBER:
			case MEMBERS_OFFLINE_NUMBER_PLACEHOLDER:
				return parseMembers(party, identifier, emptyPlaceholder, PATTERN_MEMBERS_OFFLINE);
			case MEMBERS_OFFLINE_TOTAL:
				if (party != null) {
					List<Pair<PartyPlayerImpl, Boolean>> list = getMembers(party);
					list = list.stream().filter(p -> !p.getValue()).collect(Collectors.toList());
					return Integer.toString(list.size());
				}
				return "0";
			case MOTD:
				return party != null && party.getMotd() != null ? party.getMotd() : emptyPlaceholder;
			case NAME:
			case PARTY:
				return party != null && party.getName() != null ? party.getName() : emptyPlaceholder;
			case OUT_PARTY:
				return party == null ? Messages.PARTIES_OUT_PARTY : emptyPlaceholder;
			case PLAYER_DISPLAY_NAME:
				if (player != null) {
					User user = plugin.getPlayer(player.getPlayerUUID());
					return user != null ? user.getDisplayName() : player.getName();
				}
				return emptyPlaceholder;
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
	
	private String parseListPartiesBy(PartyPlayerImpl player, String identifier, String emptyPlaceholder, Pattern pattern, PartiesDatabaseManager.ListOrder order) {
		Matcher matcher = pattern.matcher(identifier);
		if (matcher.find() && matcher.group(1) != null) {
			try {
				int number = Integer.parseInt(matcher.group(1));
				Optional<PartyImpl> partyOptional = plugin.getDatabaseManager().getListParties(order, 1, number - 1).stream().findAny();
				
				if (partyOptional.isPresent()) {
					if (matcher.group(3) != null) {
						return plugin.getMessageUtils().convertRawPlaceholder(matcher.group(3), player, partyOptional.get(), emptyPlaceholder);
					}
					
					return partyOptional.get().getName() != null ? partyOptional.get().getName() : emptyPlaceholder;
				}
				return emptyPlaceholder;
			} catch (NumberFormatException ignored) {}
		}
		return null;
	}
	
	private String parseListRank(PartyImpl party, String identifier, String emptyPlaceholder, Pattern pattern) {
		Matcher matcher = pattern.matcher(identifier);
		if (matcher.find() && party != null) {
			ArrayList<PartyPlayerImpl> members = getMembersByRank(party, matcher.group(1));
			if (members != null) {
				if (pattern.equals(PATTERN_LIST_RANK_ONLINE))
					filterMembers(members, true);
				else if (pattern.equals(PATTERN_LIST_RANK_OFFLINE))
					filterMembers(members, false);
				
				try {
					int number = Integer.parseInt(matcher.group(3));
					PartyPlayerImpl target = members.get(number - 1);
					
					if (target != null) {
						if (matcher.group(5) != null) {
							return plugin.getMessageUtils().convertRawPlaceholder(matcher.group(5), target, party, emptyPlaceholder);
						}
						
						return target.getName();
					}
				} catch (NumberFormatException ignored) {
				} catch (IndexOutOfBoundsException ignored) {
					return emptyPlaceholder;
				}
			}
		}
		return null;
	}
	
	private String parseMembers(PartyImpl party, String identifier, String emptyPlaceholder, Pattern pattern) {
		if (party != null) {
			Matcher matcher = pattern.matcher(identifier);
			if (matcher.find()) {
				ArrayList<Pair<PartyPlayerImpl, Boolean>> members = getMembers(party);
				if (pattern.equals(PATTERN_MEMBERS_ONLINE)) {
					members.removeIf(pair -> !pair.getValue());
				} else if (pattern.equals(PATTERN_MEMBERS_OFFLINE)) {
					members.removeIf(Pair::getValue);
				}
				try {
					int number = Integer.parseInt(matcher.group(1));
					PartyPlayerImpl target = members.get(number - 1).getKey();
					
					if (target != null) {
						if (matcher.group(3) != null) {
							return plugin.getMessageUtils().convertRawPlaceholder(matcher.group(3), target, party, emptyPlaceholder);
						}
						
						return target.getName();
					}
				} catch (NumberFormatException ignored) {}
				catch (IndexOutOfBoundsException ignored) {
					return emptyPlaceholder;
				}
			}
			return null;
		}
		return emptyPlaceholder;
	}
	
	private ArrayList<Pair<PartyPlayerImpl, Boolean>> getMembers(PartyImpl party) {
		ArrayList<Pair<PartyPlayerImpl, Boolean>> ret = new ArrayList<>();
		for (UUID member : party.getMembers()) {
			OfflineUser ou = plugin.getOfflinePlayer(member);
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(member);
			if (pp != null) {
				ret.add(new Pair<>(pp, ou != null && ou.isOnline() && !pp.isVanished()));
			}
		}
		return ret;
	}
	
	private ArrayList<PartyPlayerImpl> getMembersByRank(PartyImpl party, String rankName) {
		PartyRankImpl rank = rankName != null ? plugin.getRankManager().searchRankByHardName(rankName) : null;
		if (rank != null) {
			ArrayList<PartyPlayerImpl> ret = new ArrayList<>();
			for (UUID playerUUID : party.getMembers()) {
				PartyPlayerImpl pl = plugin.getPlayerManager().getPlayer(playerUUID);
				
				if (rank.getLevel() == pl.getRank())
					ret.add(pl);
			}
			return ret;
		}
		return null;
	}
	
	private List<PartyPlayerImpl> filterMembers(List<PartyPlayerImpl> members, boolean mustBeOnline) {
		if (mustBeOnline) {
			members.removeIf(pl -> {
				OfflineUser offlinePlayer = plugin.getOfflinePlayer(pl.getPlayerUUID());
				return (offlinePlayer != null && !offlinePlayer.isOnline()) || pl.isVanished();
			});
		} else {
			members.removeIf(pl -> {
				OfflineUser offlinePlayer = plugin.getOfflinePlayer(pl.getPlayerUUID());
				return (offlinePlayer != null && offlinePlayer.isOnline()) && !pl.isVanished();
			});
		}
		return members;
	}
}
