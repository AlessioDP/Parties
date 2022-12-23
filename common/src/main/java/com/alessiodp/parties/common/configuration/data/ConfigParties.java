package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.addons.external.simpleyaml.configuration.MemorySection;
import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyColorImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class ConfigParties extends ConfigurationFile {
	// General settings
	@ConfigOption(path = "general.members.limit")
	public static int		GENERAL_MEMBERS_LIMIT;
	@ConfigOption(path = "general.members.on-party-leave-change-leader")
	public static boolean	GENERAL_MEMBERS_ON_PARTY_LEAVE_CHANGE_LEADER;
	@ConfigOption(path = "general.members.disband-parties-on-disable")
	public static boolean	GENERAL_MEMBERS_DISBAND_PARTIES_ON_DISABLE;
	@ConfigOption(path = "general.members.on-player-leave-from-server.change-leader")
	public static boolean	GENERAL_MEMBERS_ON_LEAVE_SERVER_CHANGE_LEADER;
	@ConfigOption(path = "general.members.on-player-leave-from-server.kick-from-party")
	public static boolean	GENERAL_MEMBERS_ON_LEAVE_SERVER_KICK_FROM_PARTY;
	@ConfigOption(path = "general.members.on-player-leave-from-server.delay")
	public static int		GENERAL_MEMBERS_ON_LEAVE_SERVER_DELAY;
	
	@ConfigOption(path = "general.broadcast.titles.enable")
	public static boolean		GENERAL_BROADCAST_TITLES_ENABLE;
	@ConfigOption(path = "general.broadcast.titles.send-normal-message")
	public static boolean		GENERAL_BROADCAST_TITLES_SEND_NORMAL_MESSAGE;
	@ConfigOption(path = "general.broadcast.titles.fade-in-time")
	public static int			GENERAL_BROADCAST_TITLES_FADE_IN_TIME;
	@ConfigOption(path = "general.broadcast.titles.show-time")
	public static int			GENERAL_BROADCAST_TITLES_SHOW_TIME;
	@ConfigOption(path = "general.broadcast.titles.fade-out-time")
	public static int			GENERAL_BROADCAST_TITLES_FADE_OUT_TIME;
	
	@ConfigOption(path = "general.name.allowed-characters")
	public static String		GENERAL_NAME_ALLOWEDCHARS;
	@ConfigOption(path = "general.name.censor-regex")
	public static String		GENERAL_NAME_CENSORREGEX;
	@ConfigOption(path = "general.name.minimum-length")
	public static int			GENERAL_NAME_MINLENGTH;
	@ConfigOption(path = "general.name.maximum-length")
	public static int			GENERAL_NAME_MAXLENGTH;
	@ConfigOption(path = "general.name.rename-cooldown")
	public static int			GENERAL_NAME_RENAME_COOLDOWN;
	@ConfigOption(path = "general.name.dynamic.enable")
	public static boolean		GENERAL_NAME_DYNAMIC_ENABLE;
	@ConfigOption(path = "general.name.dynamic.format")
	public static String		GENERAL_NAME_DYNAMIC_FORMAT;
	@ConfigOption(path = "general.name.dynamic.allow-in-create")
	public static boolean		GENERAL_NAME_DYNAMIC_ALLOW_IN_CREATE;
	@ConfigOption(path = "general.name.dynamic.if-already-exists-format")
	public static String		GENERAL_NAME_DYNAMIC_IF_ALREADY_EXISTS_FORMAT;
	
	@ConfigOption(path = "general.join-leave-messages")
	public static boolean		GENERAL_JOIN_LEAVE_MESSAGES;
	
	@ConfigOption(path = "general.invite.timeout")
	public static int			GENERAL_INVITE_TIMEOUT;
	@ConfigOption(path = "general.invite.revoke")
	public static boolean		GENERAL_INVITE_REVOKE;
	@ConfigOption(path = "general.invite.prevent-invite-player-no-permission-join")
	public static boolean		GENERAL_INVITE_PREVENTINVITEPERM;
	@ConfigOption(path = "general.invite.auto-create-party-upon-invite")
	public static boolean		GENERAL_INVITE_AUTO_CREATE_PARTY_UPON_INVITE;
	@ConfigOption(path = "general.invite.cooldown.enable")
	public static boolean		GENERAL_INVITE_COOLDOWN_ENABLE;
	@ConfigOption(path = "general.invite.cooldown.global")
	public static int			GENERAL_INVITE_COOLDOWN_GLOBAL;
	@ConfigOption(path = "general.invite.cooldown.individual")
	public static int			GENERAL_INVITE_COOLDOWN_INDIVIDUAL;
	@ConfigOption(path = "general.invite.cooldown.on-leave.global")
	public static int			GENERAL_INVITE_COOLDOWN_ON_LEAVE_GLOBAL;
	@ConfigOption(path = "general.invite.cooldown.on-leave.individual")
	public static int			GENERAL_INVITE_COOLDOWN_ON_LEAVE_INDIVIDUAL;
	
	@ConfigOption(path = "general.chat.allow-colors")
	public static boolean		GENERAL_CHAT_ALLOWCOLORS;
	@ConfigOption(path = "general.chat.toggle-command")
	public static boolean		GENERAL_CHAT_TOGGLECOMMAND;
	@ConfigOption(path = "general.chat.cooldown")
	public static int			GENERAL_CHAT_COOLDOWN;
	@ConfigOption(path = "general.chat.censor-regex")
	public static String		GENERAL_CHAT_CENSORREGEX;
	@ConfigOption(path = "general.chat.direct.enable")
	public static boolean		GENERAL_CHAT_DIRECT_ENABLED;
	@ConfigOption(path = "general.chat.direct.prefix")
	public static String		GENERAL_CHAT_DIRECT_PREFIX;
	
	@ConfigOption(path = "general.sounds.on-chat.enable")
	public static boolean		GENERAL_SOUNDS_ON_CHAT_ENABLE;
	@ConfigOption(path = "general.sounds.on-chat.name")
	public static String		GENERAL_SOUNDS_ON_CHAT_NAME;
	@ConfigOption(path = "general.sounds.on-chat.volume")
	public static double		GENERAL_SOUNDS_ON_CHAT_VOLUME;
	@ConfigOption(path = "general.sounds.on-chat.pitch")
	public static double		GENERAL_SOUNDS_ON_CHAT_PITCH;
	@ConfigOption(path = "general.sounds.on-broadcast.enable")
	public static boolean		GENERAL_SOUNDS_ON_BROADCAST_ENABLE;
	@ConfigOption(path = "general.sounds.on-broadcast.name")
	public static String		GENERAL_SOUNDS_ON_BROADCAST_NAME;
	@ConfigOption(path = "general.sounds.on-broadcast.volume")
	public static double		GENERAL_SOUNDS_ON_BROADCAST_VOLUME;
	@ConfigOption(path = "general.sounds.on-broadcast.pitch")
	public static double		GENERAL_SOUNDS_ON_BROADCAST_PITCH;
	
	
	// Ranks settings
	@ConfigOption(path = "ranks")
	public static MemorySection RAW_RANK_LIST;
	public static Set<PartyRankImpl> RANK_LIST;
	public static int			RANK_SET_DEFAULT;
	public static int			RANK_SET_HIGHER;
	
	
	// Additional settings
	@ConfigOption(path = "additional.ask.enable")
	public static boolean		ADDITIONAL_ASK_ENABLE;
	@ConfigOption(path = "additional.ask.timeout")
	public static int			ADDITIONAL_ASK_TIMEOUT;
	@ConfigOption(path = "additional.ask.cooldown.enable")
	public static boolean		ADDITIONAL_ASK_COOLDOWN_ENABLE;
	@ConfigOption(path = "additional.ask.cooldown.global")
	public static int			ADDITIONAL_ASK_COOLDOWN_GLOBAL;
	@ConfigOption(path = "additional.ask.cooldown.individual")
	public static int			ADDITIONAL_ASK_COOLDOWN_INDIVIDUAL;
	
	@ConfigOption(path = "additional.color.enable")
	public static boolean		ADDITIONAL_COLOR_ENABLE;
	@ConfigOption(path = "additional.color.color-command")
	public static boolean		ADDITIONAL_COLOR_COLORCMD;
	@ConfigOption(path = "additional.color.dynamic-color")
	public static boolean		ADDITIONAL_COLOR_DYNAMIC;
	@ConfigOption(path = "additional.color.list-colors")
	public static MemorySection RAW_ADDITIONAL_COLOR_LIST;
	public static Set<PartyColorImpl> ADDITIONAL_COLOR_LIST;
	
	@ConfigOption(path = "additional.description.enable")
	public static boolean		ADDITIONAL_DESC_ENABLE;
	@ConfigOption(path = "additional.description.minimum-length")
	public static int			ADDITIONAL_DESC_MINLENGTH;
	@ConfigOption(path = "additional.description.maximum-length")
	public static int			ADDITIONAL_DESC_MAXLENGTH;
	@ConfigOption(path = "additional.description.allowed-characters")
	public static String		ADDITIONAL_DESC_ALLOWEDCHARS;
	@ConfigOption(path = "additional.description.censor-regex")
	public static String		ADDITIONAL_DESC_CENSORREGEX;
	
	@ConfigOption(path = "additional.fixed.enable")
	public static boolean		ADDITIONAL_FIXED_ENABLE;
	@ConfigOption(path = "additional.fixed.default-party.enable")
	public static boolean		ADDITIONAL_FIXED_DEFAULT_ENABLE;
	@ConfigOption(path = "additional.fixed.default-party.party")
	public static String		ADDITIONAL_FIXED_DEFAULT_PARTY;
	
	@ConfigOption(path = "additional.friendly-fire.enable")
	public static boolean		ADDITIONAL_FRIENDLYFIRE_ENABLE;
	@ConfigOption(path = "additional.friendly-fire.type")
	public static String		ADDITIONAL_FRIENDLYFIRE_TYPE;
	@ConfigOption(path = "additional.friendly-fire.warn-players-on-fight")
	public static boolean		ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT;
	@ConfigOption(path = "additional.friendly-fire.prevent-fish-hook")
	public static boolean		ADDITIONAL_FRIENDLYFIRE_PREVENT_FISH_HOOK;
	@ConfigOption(path = "additional.friendly-fire.prevent-damage-with-magic")
	public static boolean		ADDITIONAL_FRIENDLYFIRE_PREVENT_DAMAGE_MAGIC;
	
	@ConfigOption(path = "additional.home.enable")
	public static boolean		ADDITIONAL_HOME_ENABLE;
	@ConfigOption(path = "additional.home.delay")
	public static int			ADDITIONAL_HOME_DELAY;
	@ConfigOption(path = "additional.home.cooldown-home")
	public static int			ADDITIONAL_HOME_COOLDOWN_HOME;
	@ConfigOption(path = "additional.home.cooldown-sethome")
	public static int			ADDITIONAL_HOME_COOLDOWN_SETHOME;
	@ConfigOption(path = "additional.home.max-homes")
	public static int			ADDITIONAL_HOME_MAX_HOMES;
	
	@ConfigOption(path = "additional.join.enable")
	public static boolean		ADDITIONAL_JOIN_ENABLE;
	@ConfigOption(path = "additional.join.open-close.enable")
	public static boolean		ADDITIONAL_JOIN_OPENCLOSE_ENABLE;
	@ConfigOption(path = "additional.join.open-close.hide-opposite-command")
	public static boolean		ADDITIONAL_JOIN_OPENCLOSE_HIDE_OPPOSITE;
	@ConfigOption(path = "additional.join.open-close.open-by-default")
	public static boolean		ADDITIONAL_JOIN_OPENCLOSE_OPEN_BY_DEFAULT;
	@ConfigOption(path = "additional.join.open-close.cooldown-open")
	public static int			ADDITIONAL_JOIN_OPENCLOSE_COOLDOWN_OPEN;
	@ConfigOption(path = "additional.join.open-close.cooldown-close")
	public static int			ADDITIONAL_JOIN_OPENCLOSE_COOLDOWN_CLOSE;
	@ConfigOption(path = "additional.join.open-close.auto-create-party-upon-open")
	public static boolean		ADDITIONAL_JOIN_OPENCLOSE_AUTO_CREATE;
	@ConfigOption(path = "additional.join.password.enable")
	public static boolean		ADDITIONAL_JOIN_PASSWORD_ENABLE;
	@ConfigOption(path = "additional.join.password.allowed-characters")
	public static String		ADDITIONAL_JOIN_PASSWORD_ALLOWEDCHARS;
	@ConfigOption(path = "additional.join.password.hash")
	public static String		ADDITIONAL_JOIN_PASSWORD_HASH;
	@ConfigOption(path = "additional.join.password.encode")
	public static String		ADDITIONAL_JOIN_PASSWORD_ENCODE;
	@ConfigOption(path = "additional.join.password.minimum-length")
	public static int			ADDITIONAL_JOIN_PASSWORD_MINLENGTH;
	@ConfigOption(path = "additional.join.password.maximum-length")
	public static int			ADDITIONAL_JOIN_PASSWORD_MAXLENGTH;
	
	@ConfigOption(path = "additional.kills.enable")
	public static boolean		ADDITIONAL_KILLS_ENABLE;
	@ConfigOption(path = "additional.kills.which-save.neutral-mobs")
	public static boolean		ADDITIONAL_KILLS_MOB_NEUTRAL;
	@ConfigOption(path = "additional.kills.which-save.hostile-mobs")
	public static boolean		ADDITIONAL_KILLS_MOB_HOSTILE;
	@ConfigOption(path = "additional.kills.which-save.players")
	public static boolean		ADDITIONAL_KILLS_MOB_PLAYERS;
	
	@ConfigOption(path = "additional.list.enable")
	public static boolean		ADDITIONAL_LIST_ENABLE;
	@ConfigOption(path = "additional.list.order-by")
	public static String		ADDITIONAL_LIST_ORDERBY;
	@ConfigOption(path = "additional.list.change-order")
	public static boolean		ADDITIONAL_LIST_CHANGE_ORDER;
	@ConfigOption(path = "additional.list.parties-per-page")
	public static int			ADDITIONAL_LIST_PERPAGE;
	@ConfigOption(path = "additional.list.limit-parties")
	public static int			ADDITIONAL_LIST_LIMITPARTIES;
	@ConfigOption(path = "additional.list.hidden-parties")
	public static List<String>	ADDITIONAL_LIST_HIDDENPARTIES;
	
	@ConfigOption(path = "additional.motd.enable")
	public static boolean		ADDITIONAL_MOTD_ENABLE;
	@ConfigOption(path = "additional.motd.minimum-length")
	public static int			ADDITIONAL_MOTD_MINLENGTH;
	@ConfigOption(path = "additional.motd.maximum-length")
	public static int			ADDITIONAL_MOTD_MAXLENGTH;
	@ConfigOption(path = "additional.motd.delay")
	public static long			ADDITIONAL_MOTD_DELAY;
	@ConfigOption(path = "additional.motd.allowed-characters")
	public static String		ADDITIONAL_MOTD_ALLOWEDCHARS;
	@ConfigOption(path = "additional.motd.censor-regex")
	public static String		ADDITIONAL_MOTD_CENSORREGEX;
	@ConfigOption(path = "additional.motd.new-line-code")
	public static String		ADDITIONAL_MOTD_NEWLINECODE;
	
	@ConfigOption(path = "additional.nickname.enable")
	public static boolean		ADDITIONAL_NICKNAME_ENABLE;
	@ConfigOption(path = "additional.nickname.minimum-length")
	public static int			ADDITIONAL_NICKNAME_MINLENGTH;
	@ConfigOption(path = "additional.nickname.maximum-length")
	public static int			ADDITIONAL_NICKNAME_MAXLENGTH;
	@ConfigOption(path = "additional.nickname.allowed-characters")
	public static String		ADDITIONAL_NICKNAME_ALLOWEDCHARS;
	@ConfigOption(path = "additional.nickname.censor-regex")
	public static String		ADDITIONAL_NICKNAME_CENSORREGEX;
	@ConfigOption(path = "additional.nickname.format")
	public static String		ADDITIONAL_NICKNAME_FORMAT;
	
	@ConfigOption(path = "additional.tag.enable")
	public static boolean		ADDITIONAL_TAG_ENABLE;
	@ConfigOption(path = "additional.tag.must-be-unique")
	public static boolean		ADDITIONAL_TAG_MUST_BE_UNIQUE;
	@ConfigOption(path = "additional.tag.minimum-length")
	public static int			ADDITIONAL_TAG_MINLENGTH;
	@ConfigOption(path = "additional.tag.maximum-length")
	public static int			ADDITIONAL_TAG_MAXLENGTH;
	@ConfigOption(path = "additional.tag.allowed-characters")
	public static String		ADDITIONAL_TAG_ALLOWEDCHARS;
	@ConfigOption(path = "additional.tag.censor-regex")
	public static String		ADDITIONAL_TAG_CENSORREGEX;
	
	@ConfigOption(path = "additional.teleport.enable")
	public static boolean		ADDITIONAL_TELEPORT_ENABLE;
	@ConfigOption(path = "additional.teleport.delay")
	public static int			ADDITIONAL_TELEPORT_DELAY;
	@ConfigOption(path = "additional.teleport.cooldown")
	public static int			ADDITIONAL_TELEPORT_COOLDOWN;
	@ConfigOption(path = "additional.teleport.accept-request.enable")
	public static boolean		ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE;
	@ConfigOption(path = "additional.teleport.accept-request.time")
	public static int			ADDITIONAL_TELEPORT_ACCEPT_REQUEST_TIME;
	
	
	protected ConfigParties(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadCustomDefaultOptions() {
		RANK_LIST = new HashSet<>();
		PartyRankImpl rankMember = new PartyRankImpl(
				"member", "Member", "&bMember", 5, 0, null,
				Arrays.asList(
						"party.sendmessage",
						"party.home",
						"party.desc",
						"party.motd",
						"party.claim"
				), true);
		PartyRankImpl rankModerator = new PartyRankImpl(
				"moderator", "Moderator", "&cModerator", 10, 0, rankMember,
				Arrays.asList(
						"party.ask.accept",
						"party.ask.deny",
						"party.invite",
						"party.kick"
				), false);
		PartyRankImpl rankLeader = new PartyRankImpl(
				"member", "Member", "&bMember", 5, 0, null,
				Collections.singletonList("*"), true);
		RANK_LIST.add(rankMember);
		RANK_LIST.add(rankModerator);
		RANK_LIST.add(rankLeader);
		
		RANK_SET_DEFAULT = 5;
		RANK_SET_HIGHER = 20;
		
		ADDITIONAL_COLOR_LIST = new HashSet<>(Arrays.asList(
				new PartyColorImpl("red", "red", "&c", -1, -1, -1),
				new PartyColorImpl("green", "green", "&a", -1, -1, -1),
				new PartyColorImpl("yourcustomcolor", "special", "&9&n", -1, -1, -1)
		));
	}
	
	@Override
	public void loadCustomFileOptions() {
		// Ranks settings
		loadRanks();
		loadColors();
	}
	
	@Override
	public void saveCustomOptions() {
		saveRanks();
		saveColors();
	}
	
	private void loadRanks() {
		Set<PartyRankImpl> ranks = new HashSet<>();
		PartyRankImpl rank;
		//ConfigurationSection csBlocks = configuration.getConfigurationSection("ranks");
		if (ConfigParties.RAW_RANK_LIST != null) {
			PartyRankImpl def = null;
			PartyRankImpl lower = null;
			PartyRankImpl higher = null;
			for (String key : ConfigParties.RAW_RANK_LIST.getKeys(false)) {
				rank = new PartyRankImpl(key);
				rank.setName(ConfigParties.RAW_RANK_LIST.getString(key + ".name", key));
				rank.setChat(ConfigParties.RAW_RANK_LIST.getString(key + ".chat", rank.getName()));
				rank.setLevel(ConfigParties.RAW_RANK_LIST.getInt(key + ".level", 1));
				if (ConfigParties.RAW_RANK_LIST.get(key + ".inheritence") != null) {
					Optional<PartyRankImpl> opt = ranks.stream().filter(r -> r.getConfigName().equals(ConfigParties.RAW_RANK_LIST.getString(key + ".inheritence"))).findAny();
					if (opt.isPresent())
						rank.setInheritence(opt.get());
				}
				rank.getPermissions().addAll(ConfigParties.RAW_RANK_LIST.getStringList(key + ".permissions"));
				rank.setDefault(ConfigParties.RAW_RANK_LIST.getBoolean(key + ".default", false));
				if (!rank.isDefault())
					rank.setSlot(ConfigParties.RAW_RANK_LIST.getInt(key + ".slot", 0));
				ranks.add(rank);
				
				if (rank.isDefault())
					def = rank;
				
				if (lower == null || rank.getLevel() < lower.getLevel())
					lower = rank;
				
				if (higher == null || rank.getLevel() > higher.getLevel())
					higher = rank;
			}
			
			if (ranks.size() > 1) {
				if (def == null) {
					// Give error: default rank not found
					def = lower;
					plugin.getLoggerManager().logError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_NODEFAULT);
				}
				
				// Save rank list
				ConfigParties.RANK_LIST = ranks;
				ConfigParties.RANK_SET_DEFAULT = def.getLevel();
				ConfigParties.RANK_SET_HIGHER = higher.getLevel();
			}  else if (ranks.size() == 1) {
				// At least 2 ranks needed
				plugin.getLoggerManager().logError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_ONLYONE);
			} else {
				// Give error: no ranks found
				plugin.getLoggerManager().logError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_EMPTY);
			}
		} else {
			// Give error: no ranks node found
			plugin.getLoggerManager().logError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_NOTFOUND);
		}
	}
	
	private void saveRanks() {
		if (ConfigParties.RAW_RANK_LIST != null) {
			// Cleanup unused colors
			for (String rank : ConfigParties.RAW_RANK_LIST.getKeys(false)) {
				boolean found = false;
				for (PartyRankImpl partyRank : ((PartiesPlugin) plugin).getRankManager().getRankList()) {
					if (partyRank.getConfigName().equals(rank)) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					ConfigParties.RAW_RANK_LIST.remove(rank);
				}
			}
			
			// Update colors
			for (PartyRankImpl partyRank : ((PartiesPlugin) plugin).getRankManager().getRankList()) {
				ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".level", partyRank.getLevel());
				ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".name", partyRank.getName());
				ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".chat", partyRank.getChat());
				if (partyRank.isDefault())
					ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".default", partyRank.isDefault());
				if (partyRank.getSlot() > 0)
					ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".slot", partyRank.getSlot());
				if (partyRank.getInheritence() != null)
					ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".inheritence", partyRank.getInheritence().getConfigName());
				ConfigParties.RAW_RANK_LIST.set(partyRank.getConfigName() + ".permissions", partyRank.getPermissions());
			}
		}
	}
	
	private void loadColors() {
		Set<PartyColorImpl> colors = new HashSet<>();
		
		if (ConfigParties.RAW_ADDITIONAL_COLOR_LIST != null) {
			for (String key : ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getKeys(false)) {
				PartyColorImpl color = new PartyColorImpl(key);
				color.setCommand(ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getString(key + ".command", ""));
				color.setCode(ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getString(key + ".code", ""));
				color.setDynamicPriority(ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getInt(key + ".dynamic.priority", -1));
				color.setDynamicMembers(ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getInt(key + ".dynamic.members", -1));
				color.setDynamicKills(ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getInt(key + ".dynamic.kills", -1));
				colors.add(color);
			}
			ConfigParties.ADDITIONAL_COLOR_LIST = colors;
		} else {
			plugin.getLoggerManager().logError(PartiesConstants.DEBUG_CONFIG_FAILED_COLOR_NOTFOUND);
		}
	}
	
	private void saveColors() {
		if (ConfigParties.RAW_ADDITIONAL_COLOR_LIST != null) {
			// Cleanup unused colors
			for (String color : ConfigParties.RAW_ADDITIONAL_COLOR_LIST.getKeys(false)) {
				boolean found = false;
				for (PartyColorImpl partyColor : ((PartiesPlugin) plugin).getColorManager().getColorList()) {
					if (partyColor.getName().equals(color)) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					ConfigParties.RAW_ADDITIONAL_COLOR_LIST.remove(color);
				}
			}
			
			// Update colors
			for (PartyColorImpl partyColor : ((PartiesPlugin) plugin).getColorManager().getColorList()) {
				ConfigParties.RAW_ADDITIONAL_COLOR_LIST.set(partyColor.getName() + ".command", partyColor.getCommand());
				ConfigParties.RAW_ADDITIONAL_COLOR_LIST.set(partyColor.getName() + ".code", partyColor.getCode());
				if (partyColor.getDynamicPriority() > 0
						&& partyColor.getDynamicKills() > 0
						&& partyColor.getDynamicMembers() > 0) {
					ConfigParties.RAW_ADDITIONAL_COLOR_LIST.set(partyColor.getName() + ".dynamic.priority", partyColor.getDynamicPriority());
					ConfigParties.RAW_ADDITIONAL_COLOR_LIST.set(partyColor.getName() + ".dynamic.kills", partyColor.getDynamicKills());
					ConfigParties.RAW_ADDITIONAL_COLOR_LIST.set(partyColor.getName() + ".dynamic.members", partyColor.getDynamicMembers());
				}
			}
		}
	}
}
