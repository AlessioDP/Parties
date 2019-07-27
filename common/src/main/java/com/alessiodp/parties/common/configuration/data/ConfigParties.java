package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationSectionAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.ColorImpl;
import com.alessiodp.parties.common.players.objects.RankImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ConfigParties extends ConfigurationFile {
	// General settings
	public static int GENERAL_MEMBERSLIMIT;
	public static String GENERAL_NAME_ALLOWEDCHARS;
	public static String GENERAL_NAME_CENSORREGEX;
	public static int GENERAL_NAME_MINLENGTH;
	public static int GENERAL_NAME_MAXLENGTH;
	
	public static int GENERAL_INVITE_TIMEOUT;
	public static boolean GENERAL_INVITE_REVOKE;
	public static boolean GENERAL_INVITE_PREVENTINVITEPERM;
	public static boolean GENERAL_INVITE_COOLDOWN_ENABLE;
	public static int GENERAL_INVITE_COOLDOWN_GLOBAL;
	public static int GENERAL_INVITE_COOLDOWN_INDIVIDUAL;
	
	public static boolean GENERAL_CHAT_ALLOWCOLORS;
	public static boolean GENERAL_CHAT_TOGGLECHATCMD;
	public static int GENERAL_CHAT_CHATCD;
	public static String GENERAL_CHAT_CENSORREGEX;
	public static boolean GENERAL_CHAT_LOG;
	public static boolean GENERAL_CHAT_LOGTOCONSOLE;
	public static boolean GENERAL_CHAT_DIRECT_ENABLED;
	public static String GENERAL_CHAT_DIRECT_PREFIX;
	public static String GENERAL_CHAT_FORMAT_PARTY;
	public static String GENERAL_CHAT_FORMAT_SPY;
	public static String GENERAL_CHAT_FORMAT_BROADCAST;
	
	
	// Ranks settings
	public static Set<RankImpl> RANK_LIST;
	public static int RANK_SET_DEFAULT;
	public static int RANK_SET_HIGHER;
	
	
	// Additional settings
	public static boolean COLOR_ENABLE;
	public static boolean COLOR_COLORCMD;
	public static boolean COLOR_DYNAMIC;
	public static Set<ColorImpl> COLOR_LIST;
	
	public static boolean DESC_ENABLE;
	public static int DESC_MINLENGTH;
	public static int DESC_MAXLENGTH;
	public static String DESC_ALLOWEDCHARS;
	public static String DESC_CENSORREGEX;
	
	public static boolean FIXED_ENABLE;
	public static boolean FIXED_DEFAULT_ENABLE;
	public static String FIXED_DEFAULT_PARTY;
	
	public static boolean PASSWORD_ENABLE;
	public static String PASSWORD_ALLOWEDCHARS;
	public static String PASSWORD_HASH;
	public static String PASSWORD_ENCODE;
	public static int PASSWORD_MINLENGTH;
	public static int PASSWORD_MAXLENGTH;
	
	public static boolean LIST_ENABLE;
	public static String LIST_ORDEREDBY;
	public static int LIST_FILTERMIN;
	public static int LIST_PERPAGE;
	public static int LIST_LIMITPARTIES;
	public static List<String> LIST_HIDDENPARTIES;
	
	public static boolean MOTD_ENABLE;
	public static int MOTD_MINLENGTH;
	public static int MOTD_MAXLENGTH;
	public static int MOTD_DELAY;
	public static String MOTD_ALLOWEDCHARS;
	public static String MOTD_NEWLINECODE;
	public static String MOTD_CENSORREGEX;
	
	public static boolean TELEPORT_ENABLE;
	public static int TELEPORT_COOLDOWN;
	
	
	protected ConfigParties(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		// General settings
		GENERAL_MEMBERSLIMIT = -1;
		GENERAL_NAME_ALLOWEDCHARS = "[a-zA-Z0-9]+";
		GENERAL_NAME_CENSORREGEX = "";
		GENERAL_NAME_MINLENGTH = 3;
		GENERAL_NAME_MAXLENGTH = 10;
		
		GENERAL_INVITE_TIMEOUT = 20;
		GENERAL_INVITE_REVOKE = true;
		GENERAL_INVITE_PREVENTINVITEPERM = true;
		GENERAL_INVITE_COOLDOWN_ENABLE = false;
		GENERAL_INVITE_COOLDOWN_GLOBAL = 60;
		GENERAL_INVITE_COOLDOWN_INDIVIDUAL = 0;
		
		GENERAL_CHAT_ALLOWCOLORS = false;
		GENERAL_CHAT_TOGGLECHATCMD = true;
		GENERAL_CHAT_CHATCD = 0;
		GENERAL_CHAT_CENSORREGEX = "";
		GENERAL_CHAT_LOG = false;
		GENERAL_CHAT_LOGTOCONSOLE = false;
		GENERAL_CHAT_DIRECT_ENABLED = false;
		GENERAL_CHAT_DIRECT_PREFIX = "@";
		GENERAL_CHAT_FORMAT_PARTY = "&b[Party] %rank_chat% %player%&r&7: &b%message%";
		GENERAL_CHAT_FORMAT_SPY = "&7[SPY] [Party:%party%] %player%: %message%";
		GENERAL_CHAT_FORMAT_BROADCAST = "&b[Party] %message%";
		
		
		// Ranks settings
		RANK_LIST = new HashSet<>();
		List<String> perms = new ArrayList<>();
		perms.add("party.sendmessage");
		perms.add("party.home");
		perms.add("party.desc");
		perms.add("party.motd");
		perms.add("party.claim");
		RankImpl rank = new RankImpl("member", "Member", "&bMember", 5, perms, true);
		RANK_LIST.add(rank);
		RANK_SET_DEFAULT = 5;
		
		perms = new ArrayList<>();
		perms.add("-party.edit.home");
		perms.add("-party.edit.desc");
		perms.add("-party.edit.motd");
		perms.add("-party.edit.color");
		perms.add("-party.edit.password");
		perms.add("-party.admin.rename");
		perms.add("-party.admin.rank");
		perms.add("-party.admin.teleport");
		perms.add("-party.autocommand");
		perms.add("*");
		rank = new RankImpl("moderator", "Moderator", "&cModerator", 10, perms, false);
		RANK_LIST.add(rank);
		
		perms = new ArrayList<>();
		perms.add("*");
		rank = new RankImpl("leader", "Leader", "&4&lLeader", 20, perms, false);
		RANK_LIST.add(rank);
		RANK_SET_HIGHER = 20;
		
		
		// Additional settings
		COLOR_ENABLE = false;
		COLOR_COLORCMD = true;
		COLOR_DYNAMIC = false;
		COLOR_LIST = new HashSet<>();
		COLOR_LIST.add(new ColorImpl("red", "red", "&c", -1, -1, -1));
		COLOR_LIST.add(new ColorImpl("green", "green", "&a", -1, -1, -1));
		COLOR_LIST.add(new ColorImpl("yourcustomcolor", "special", "&9&n", -1, -1, -1));
		
		DESC_ENABLE = true;
		DESC_MINLENGTH = 3;
		DESC_MAXLENGTH = 16;
		DESC_ALLOWEDCHARS = "[a-zA-Z0-9\\ \\.\\,\\-\\_]+";
		DESC_CENSORREGEX = "";
		
		FIXED_ENABLE = false;
		FIXED_DEFAULT_ENABLE = false;
		FIXED_DEFAULT_PARTY = "default";
		
		PASSWORD_ENABLE = false;
		PASSWORD_ALLOWEDCHARS = "[a-zA-Z0-9]+";
		PASSWORD_HASH = "MD5";
		PASSWORD_ENCODE = "UTF-8";
		PASSWORD_MINLENGTH = 1;
		PASSWORD_MAXLENGTH = 16;
		
		LIST_ENABLE = true;
		LIST_ORDEREDBY = "players";
		LIST_FILTERMIN = 1;
		LIST_PERPAGE = 8;
		LIST_LIMITPARTIES = -1;
		LIST_HIDDENPARTIES = new ArrayList<>();
		
		MOTD_ENABLE = true;
		MOTD_MINLENGTH = 3;
		MOTD_MAXLENGTH = 100;
		MOTD_DELAY = 1000;
		MOTD_ALLOWEDCHARS = "[a-zA-Z0-9\\ \\.\\,\\-\\_]+";
		MOTD_NEWLINECODE = "\\\\n";
		MOTD_CENSORREGEX = "";
		
		TELEPORT_ENABLE = false;
		TELEPORT_COOLDOWN = 60;
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		// General settings
		GENERAL_MEMBERSLIMIT = confAdapter.getInt("general.members-limit", GENERAL_MEMBERSLIMIT);
		GENERAL_NAME_ALLOWEDCHARS = confAdapter.getString("general.name.allowed-characters", GENERAL_NAME_ALLOWEDCHARS);
		GENERAL_NAME_CENSORREGEX = confAdapter.getString("general.name.censor-regex", GENERAL_NAME_CENSORREGEX);
		GENERAL_NAME_MINLENGTH = confAdapter.getInt("general.name.minimum-length", GENERAL_NAME_MINLENGTH);
		GENERAL_NAME_MAXLENGTH = confAdapter.getInt("general.name.maximum-length", GENERAL_NAME_MAXLENGTH);
		
		GENERAL_INVITE_TIMEOUT = confAdapter.getInt("general.invite.timeout", GENERAL_INVITE_TIMEOUT);
		GENERAL_INVITE_REVOKE = confAdapter.getBoolean("general.invite.revoke", GENERAL_INVITE_REVOKE);
		GENERAL_INVITE_PREVENTINVITEPERM = confAdapter.getBoolean("general.invite.prevent-invite-player-no-permission-join", GENERAL_INVITE_PREVENTINVITEPERM);
		GENERAL_INVITE_COOLDOWN_ENABLE = confAdapter.getBoolean("general.invite.cooldown.enable", GENERAL_INVITE_COOLDOWN_ENABLE);
		GENERAL_INVITE_COOLDOWN_GLOBAL = confAdapter.getInt("general.invite.cooldown.global", GENERAL_INVITE_COOLDOWN_GLOBAL);
		GENERAL_INVITE_COOLDOWN_INDIVIDUAL = confAdapter.getInt("general.invite.cooldown.individual", GENERAL_INVITE_COOLDOWN_INDIVIDUAL);
		
		GENERAL_CHAT_ALLOWCOLORS = confAdapter.getBoolean("general.chat.allow-colors", GENERAL_CHAT_ALLOWCOLORS);
		GENERAL_CHAT_TOGGLECHATCMD = confAdapter.getBoolean("general.chat.enable-toggle-command", GENERAL_CHAT_TOGGLECHATCMD);
		GENERAL_CHAT_CHATCD = confAdapter.getInt("general.chat.chat-cooldown", GENERAL_CHAT_CHATCD);
		GENERAL_CHAT_CENSORREGEX = confAdapter.getString("general.chat.censor-regex", GENERAL_CHAT_CENSORREGEX);
		GENERAL_CHAT_LOG = confAdapter.getBoolean("general.chat.log", GENERAL_CHAT_LOG);
		GENERAL_CHAT_LOGTOCONSOLE = confAdapter.getBoolean("general.chat.log-to-console", GENERAL_CHAT_LOGTOCONSOLE);
		GENERAL_CHAT_DIRECT_ENABLED = confAdapter.getBoolean("general.chat.direct.enable", GENERAL_CHAT_DIRECT_ENABLED);
		GENERAL_CHAT_DIRECT_PREFIX = confAdapter.getString("general.chat.direct.prefix", GENERAL_CHAT_DIRECT_PREFIX);
		GENERAL_CHAT_FORMAT_PARTY = confAdapter.getString("general.chat.formats.party-chat", GENERAL_CHAT_FORMAT_PARTY);
		GENERAL_CHAT_FORMAT_SPY = confAdapter.getString("general.chat.formats.spy-alerts", GENERAL_CHAT_FORMAT_SPY);
		GENERAL_CHAT_FORMAT_BROADCAST = confAdapter.getString("general.chat.formats.broadcast", GENERAL_CHAT_FORMAT_BROADCAST);
		
		
		// Ranks settings
		handleRanks(confAdapter);
		
		
		// Additional settings
		COLOR_ENABLE = confAdapter.getBoolean("additional.color.enable", COLOR_ENABLE);
		COLOR_COLORCMD = confAdapter.getBoolean("additional.color.color-command", COLOR_COLORCMD);
		COLOR_DYNAMIC = confAdapter.getBoolean("additional.color.dynamic-color", COLOR_DYNAMIC);
		handleColors(confAdapter);
		
		DESC_ENABLE = confAdapter.getBoolean("additional.description.enable", DESC_ENABLE);
		DESC_MINLENGTH = confAdapter.getInt("additional.description.minimum-length", DESC_MINLENGTH);
		DESC_MAXLENGTH = confAdapter.getInt("additional.description.maximum-length", DESC_MAXLENGTH);
		DESC_ALLOWEDCHARS = confAdapter.getString("additional.description.allowed-characters", DESC_ALLOWEDCHARS);
		DESC_CENSORREGEX = confAdapter.getString("additional.description.censor-regex", DESC_CENSORREGEX);
		
		FIXED_ENABLE = confAdapter.getBoolean("additional.fixed-system.enable", FIXED_ENABLE);
		FIXED_DEFAULT_ENABLE = confAdapter.getBoolean("additional.fixed-system.default-party.enable", FIXED_DEFAULT_ENABLE);
		FIXED_DEFAULT_PARTY = confAdapter.getString("additional.fixed-system.default-party.party", FIXED_DEFAULT_PARTY);
		
		PASSWORD_ENABLE = confAdapter.getBoolean("additional.join-password.enable", PASSWORD_ENABLE);
		PASSWORD_ALLOWEDCHARS = confAdapter.getString("additional.join-password.allowed-characters", PASSWORD_ALLOWEDCHARS);
		PASSWORD_HASH = confAdapter.getString("additional.join-password.hash", PASSWORD_HASH);
		PASSWORD_ENCODE = confAdapter.getString("additional.join-password.encode", PASSWORD_ENCODE);
		PASSWORD_MINLENGTH = confAdapter.getInt("additional.join-password.minimum-length", PASSWORD_MINLENGTH);
		PASSWORD_MAXLENGTH = confAdapter.getInt("additional.join-password.maximum-length", PASSWORD_MAXLENGTH);
		
		LIST_ENABLE = confAdapter.getBoolean("additional.list.enable", LIST_ENABLE);
		LIST_ORDEREDBY = confAdapter.getString("additional.list.ordered-by", LIST_ORDEREDBY);
		LIST_FILTERMIN = confAdapter.getInt("additional.list.filter-min", LIST_FILTERMIN);
		LIST_PERPAGE = confAdapter.getInt("additional.list.parties-per-page", LIST_PERPAGE);
		LIST_LIMITPARTIES = confAdapter.getInt("additional.list.limit-parties", LIST_LIMITPARTIES);
		LIST_HIDDENPARTIES = confAdapter.getStringList("additional.list.hidden-parties", LIST_HIDDENPARTIES);
		
		MOTD_ENABLE = confAdapter.getBoolean("additional.motd.enable", MOTD_ENABLE);
		MOTD_MINLENGTH = confAdapter.getInt("additional.motd.minimum-length", MOTD_MINLENGTH);
		MOTD_MAXLENGTH = confAdapter.getInt("additional.motd.maximum-length", MOTD_MAXLENGTH);
		MOTD_DELAY = confAdapter.getInt("additional.motd.delay", MOTD_DELAY);
		MOTD_ALLOWEDCHARS = confAdapter.getString("additional.motd.allowed-characters", MOTD_ALLOWEDCHARS);
		MOTD_NEWLINECODE = confAdapter.getString("additional.motd.new-line-code", MOTD_NEWLINECODE);
		MOTD_CENSORREGEX = confAdapter.getString("additional.motd.censor-regex", MOTD_CENSORREGEX);
		
		TELEPORT_ENABLE = confAdapter.getBoolean("additional.teleport.enable", TELEPORT_ENABLE);
		TELEPORT_COOLDOWN = confAdapter.getInt("additional.teleport.cooldown", TELEPORT_COOLDOWN);
	}
	
	private void handleRanks(ConfigurationAdapter confAdapter) {
		Set<RankImpl> ranks = new HashSet<>();
		RankImpl rank;
		
		ConfigurationSectionAdapter csBlocks = confAdapter.getConfigurationSection("ranks");
		if (csBlocks != null) {
			RankImpl def = null;
			RankImpl lower = null;
			RankImpl higher = null;
			for (String key : csBlocks.getKeys()) {
				rank = new RankImpl(key);
				rank.setName(csBlocks.getString(key + ".name", key));
				rank.setChat(csBlocks.getString(key + ".chat", rank.getName()));
				rank.setLevel(csBlocks.getInt(key + ".rank", 1));
				rank.setPermissions(csBlocks.getStringList(key + ".permissions", new ArrayList<>()));
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
	
	private void handleColors(ConfigurationAdapter confAdapter) {
		Set<ColorImpl> colors = new HashSet<>();
		ColorImpl color;
		
		ConfigurationSectionAdapter csBlocks = confAdapter.getConfigurationSection("additional.color.list-colors");
		if (csBlocks != null) {
			for (String key : csBlocks.getKeys()) {
				color = new ColorImpl(key);
				color.setCommand(csBlocks.getString(key + ".command", ""));
				color.setCode(csBlocks.getString(key + ".code", ""));
				color.setDynamicPriority(csBlocks.getInt(key + ".dynamic.priority", -1));
				color.setDynamicMembers(csBlocks.getInt(key + ".dynamic.members", -1));
				color.setDynamicKills(csBlocks.getInt(key + ".dynamic.kills", -1));
				colors.add(color);
			}
			ConfigParties.COLOR_LIST = colors;
		} else {
			// Give error: no ranks node found
			plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_COLOR_NOTFOUND);
		}
	}
}
