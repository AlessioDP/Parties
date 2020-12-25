package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyColorImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;

import java.util.ArrayList;
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
	@ConfigOption(path = "general.members.change-leader-on-leave")
	public static boolean	GENERAL_MEMBERS_CHANGE_LEADER_ON_LEAVE;
	
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
	
	@ConfigOption(path = "general.join-leave-messages")
	public static boolean		GENERAL_JOIN_LEAVE_MESSAGES;
	
	@ConfigOption(path = "general.invite.timeout")
	public static int			GENERAL_INVITE_TIMEOUT;
	@ConfigOption(path = "general.invite.revoke")
	public static boolean		GENERAL_INVITE_REVOKE;
	@ConfigOption(path = "general.invite.prevent-invite-player-no-permission-join")
	public static boolean		GENERAL_INVITE_PREVENTINVITEPERM;
	@ConfigOption(path = "general.invite.cooldown.enable")
	public static boolean		GENERAL_INVITE_COOLDOWN_ENABLE;
	@ConfigOption(path = "general.invite.cooldown.global")
	public static int			GENERAL_INVITE_COOLDOWN_GLOBAL;
	@ConfigOption(path = "general.invite.cooldown.individual")
	public static int			GENERAL_INVITE_COOLDOWN_INDIVIDUAL;
	
	@ConfigOption(path = "general.ask.enable")
	public static boolean		GENERAL_ASK_ENABLE;
	@ConfigOption(path = "general.ask.timeout")
	public static int			GENERAL_ASK_TIMEOUT;
	@ConfigOption(path = "general.ask.cooldown.enable")
	public static boolean		GENERAL_ASK_COOLDOWN_ENABLE;
	@ConfigOption(path = "general.ask.cooldown.global")
	public static int			GENERAL_ASK_COOLDOWN_GLOBAL;
	@ConfigOption(path = "general.ask.cooldown.individual")
	public static int			GENERAL_ASK_COOLDOWN_INDIVIDUAL;
	
	@ConfigOption(path = "general.chat.allow-colors")
	public static boolean		GENERAL_CHAT_ALLOWCOLORS;
	@ConfigOption(path = "general.chat.toggle-command")
	public static boolean		GENERAL_CHAT_TOGGLECOMMAND;
	@ConfigOption(path = "general.chat.cooldown")
	public static int			GENERAL_CHAT_COOLDOWN;
	@ConfigOption(path = "general.chat.censor-regex")
	public static String		GENERAL_CHAT_CENSORREGEX;
	@ConfigOption(path = "general.chat.prevent-muted-players")
	public static boolean		GENERAL_CHAT_PREVENT_MUTED_PLAYERS;
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
	public static Set<PartyRankImpl> RANK_LIST;
	public static int			RANK_SET_DEFAULT;
	public static int			RANK_SET_HIGHER;
	
	
	// Additional settings
	@ConfigOption(path = "additional.color.enable")
	public static boolean		ADDITIONAL_COLOR_ENABLE;
	@ConfigOption(path = "additional.color.color-command")
	public static boolean		ADDITIONAL_COLOR_COLORCMD;
	@ConfigOption(path = "additional.color.dynamic-color")
	public static boolean		ADDITIONAL_COLOR_DYNAMIC;
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
	public void loadDefaults() {
		loadDefaultConfigOptions();
		
		ArrayList<String> rankPermissions = new ArrayList<>(Arrays.asList(
				"party.sendmessage",
				"party.home",
				"party.desc",
				"party.motd",
				"party.claim"
		));
		
		RANK_LIST = new HashSet<>();
		RANK_LIST.add(new PartyRankImpl(
				"member", "Member", "&bMember", 5,
				rankPermissions, true)
		);
		rankPermissions = new ArrayList<>(rankPermissions);
		rankPermissions.addAll(Arrays.asList(
						"party.ask.accept",
						"party.ask.deny",
						"party.invite",
						"party.kick"
		));
		RANK_LIST.add(new PartyRankImpl(
				"moderator", "Moderator", "&cModerator", 10,
				rankPermissions, false)
		);
		RANK_LIST.add(new PartyRankImpl(
				"leader", "Leader", "&4&lLeader", 20,
				new ArrayList<>(Collections.singleton("*")), false)
		);
		RANK_SET_DEFAULT = 5;
		RANK_SET_HIGHER = 20;
		
		ADDITIONAL_COLOR_LIST = new HashSet<>(Arrays.asList(
				new PartyColorImpl("red", "red", "&c", -1, -1, -1),
				new PartyColorImpl("green", "green", "&a", -1, -1, -1),
				new PartyColorImpl("yourcustomcolor", "special", "&9&n", -1, -1, -1)
		));
	}
	
	@Override
	public void loadConfiguration() {
		loadConfigOptions();
		
		// Ranks settings
		handleRanks();
		handleColors();
	}
	
	private void handleRanks() {
		Set<PartyRankImpl> ranks = new HashSet<>();
		PartyRankImpl rank;
		ConfigurationSection csBlocks = configuration.getConfigurationSection("ranks");
		if (csBlocks != null) {
			PartyRankImpl def = null;
			PartyRankImpl lower = null;
			PartyRankImpl higher = null;
			for (String key : csBlocks.getKeys(false)) {
				rank = new PartyRankImpl(key);
				rank.setName(csBlocks.getString(key + ".name", key));
				rank.setChat(csBlocks.getString(key + ".chat", rank.getName()));
				rank.setLevel(csBlocks.getInt(key + ".level", 1));
				if (csBlocks.get("inheritence") != null) {
					Optional<PartyRankImpl> opt = ranks.stream().filter(r -> r.getConfigName().equals(csBlocks.getString("inheritence"))).findAny();
					if (opt.isPresent())
						rank.setPermissions(new ArrayList<>(opt.get().getPermissions()));
				}
				rank.getPermissions().addAll(csBlocks.getStringList(key + ".permissions"));
				rank.setDefault(csBlocks.getBoolean(key + ".default", false));
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
					plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_NODEFAULT);
				}
				
				// Save rank list
				ConfigParties.RANK_LIST = ranks;
				ConfigParties.RANK_SET_DEFAULT = def.getLevel();
				ConfigParties.RANK_SET_HIGHER = higher.getLevel();
			}  else if (ranks.size() == 1) {
				// At least 2 ranks needed
				plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_ONLYONE);
			} else {
				// Give error: no ranks found
				plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_EMPTY);
			}
		} else {
			// Give error: no ranks node found
			plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_RANK_NOTFOUND);
		}
	}
	
	private void handleColors() {
		Set<PartyColorImpl> colors = new HashSet<>();
		PartyColorImpl color;
		
		ConfigurationSection csBlocks = configuration.getConfigurationSection("additional.color.list-colors");
		if (csBlocks != null) {
			for (String key : csBlocks.getKeys(false)) {
				color = new PartyColorImpl(key);
				color.setCommand(csBlocks.getString(key + ".command", ""));
				color.setCode(csBlocks.getString(key + ".code", ""));
				color.setDynamicPriority(csBlocks.getInt(key + ".dynamic.priority", -1));
				color.setDynamicMembers(csBlocks.getInt(key + ".dynamic.members", -1));
				color.setDynamicKills(csBlocks.getInt(key + ".dynamic.kills", -1));
				colors.add(color);
			}
			ConfigParties.ADDITIONAL_COLOR_LIST = colors;
		} else {
			// Give error: no ranks node found
			plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_COLOR_NOTFOUND);
		}
	}
}
