package com.alessiodp.parties.configuration.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alessiodp.parties.parties.objects.ColorImpl;
import com.alessiodp.parties.players.objects.RankImpl;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.interfaces.Rank;

public class ConfigParties {
	
	// General settings
	public static int			GENERAL_MEMBERSLIMIT;
	public static String		GENERAL_NAME_ALLOWEDCHARS;
	public static int			GENERAL_NAME_MINLENGTH;
	public static int			GENERAL_NAME_MAXLENGTH;
	public static int			GENERAL_NAME_RENAMECD;
	
	public static int			GENERAL_INVITE_TIMEOUT;
	public static boolean		GENERAL_INVITE_REVOKE;
	public static boolean		GENERAL_INVITE_PREVENTINVITEPERM;
	
	public static boolean		GENERAL_CHAT_ALLOWCOLORS;
	public static boolean		GENERAL_CHAT_TOGGLECHATCMD;
	public static int			GENERAL_CHAT_CHATCD;
	public static String		GENERAL_CHAT_FORMAT_PARTY;
	public static String		GENERAL_CHAT_FORMAT_SPY;
	public static String		GENERAL_CHAT_FORMAT_BROADCAST;
	
	
	// Ranks settings
	public static Set<Rank>		RANK_LIST;
	public static int			RANK_SET_DEFAULT;
	public static int			RANK_SET_HIGHER;

	
	// Additional settings
	public static boolean		COLOR_ENABLE;
	public static boolean		COLOR_COLORCMD;
	public static boolean		COLOR_DYNAMIC;
	public static Set<Color>	COLOR_LIST;
	
	public static boolean		DESC_ENABLE;
	public static int			DESC_MINLENGTH;
	public static int			DESC_MAXLENGTH;
	public static String		DESC_ALLOWEDCHARS;
	
	public static boolean		FIXED_ENABLE;
	public static boolean		FIXED_DEFAULT_ENABLE;
	public static String		FIXED_DEFAULT_PARTY;
	
	public static boolean		FRIENDLYFIRE_ENABLE;
	public static boolean		FRIENDLYFIRE_WARNONFIGHT;
	public static List<String>	FRIENDLYFIRE_LISTWORLDS;
	public static boolean		FRIENDLYFIRE_CRACKSHOT_ENABLE;
	
	public static boolean		HOME_ENABLE;
	public static int			HOME_DELAY;
	public static boolean		HOME_HIT;
	public static boolean		HOME_MOVING;
	public static int			HOME_DISTANCE;
	
	public static boolean		PASSWORD_ENABLE;
	public static String		PASSWORD_ALLOWECHARS;
	public static String		PASSWORD_HASH;
	public static String		PASSWORD_ENCODE;
	public static int			PASSWORD_MINLENGTH;
	public static int			PASSWORD_MAXLENGTH;
	
	public static boolean		KILLS_ENABLE;
	public static boolean		KILLS_SAVEKILLS;
	public static boolean		KILLS_MOB_NEUTRAL;
	public static boolean		KILLS_MOB_HOSTILE;
	public static boolean		KILLS_MOB_PLAYERS;
	
	public static boolean		LIST_ENABLE;
	public static String		LIST_ORDEREDBY;
	public static int			LIST_FILTERMIN;
	public static int			LIST_PERPAGE;
	public static int			LIST_LIMITPARTIES;
	public static List<String>	LIST_HIDDENPARTIES;
	
	public static boolean		MOTD_ENABLE;
	public static int			MOTD_MINLENGTH;
	public static int			MOTD_MAXLENGTH;
	public static int			MOTD_DELAY;
	public static String		MOTD_ALLOWEDCHARS;
	public static String		MOTD_NEWLINECODE;
	
	public static boolean		TELEPORT_ENABLE;
	public static int			TELEPORT_COOLDOWN;
	
	
	public ConfigParties() {
		loadDefaults();
	}
	
	public void loadDefaults() {
		// General settings
		GENERAL_MEMBERSLIMIT = -1;
		GENERAL_NAME_ALLOWEDCHARS = "[a-zA-Z0-9]+";
		GENERAL_NAME_MINLENGTH = 3;
		GENERAL_NAME_MAXLENGTH = 10;
		GENERAL_NAME_RENAMECD = 300;
		
		GENERAL_INVITE_TIMEOUT = 20;
		GENERAL_INVITE_REVOKE = true;
		GENERAL_INVITE_PREVENTINVITEPERM = true;
		
		GENERAL_CHAT_ALLOWCOLORS = false;
		GENERAL_CHAT_TOGGLECHATCMD = true;
		GENERAL_CHAT_CHATCD = 0;
		GENERAL_CHAT_FORMAT_PARTY = "&b[Party] %rank_chat% %player%&r&7: &b%message%";
		GENERAL_CHAT_FORMAT_SPY = "&7[SPY] [Party:%party%] %player%: %message%";
		GENERAL_CHAT_FORMAT_BROADCAST = "&b[Party] %message%";
		
		
		// Ranks settings
		RANK_LIST = new HashSet<Rank>();
		List<String> perms = new ArrayList<String>();
		perms.add("party.sendmessage");
		perms.add("party.home");
		perms.add("party.desc");
		perms.add("party.motd");
		perms.add("party.claim");
		Rank rank = new RankImpl(5, "member", "Member", "&bMember", true, perms);
		RANK_LIST.add(rank);
		RANK_SET_DEFAULT = 5;
		
		perms = new ArrayList<String>();
		perms.add("-party.edit.home");
		perms.add("-party.edit.desc");
		perms.add("-party.edit.motd");
		perms.add("-party.edit.color");
		perms.add("-party.edit.prefix");
		perms.add("-party.edit.suffix");
		perms.add("-party.edit.password");
		perms.add("-party.admin.rename");
		perms.add("-party.admin.rank");
		perms.add("-party.admin.teleport");
		perms.add("-party.autocommand");
		perms.add("*");
		rank = new RankImpl(10, "moderator", "Moderator", "&cModerator", false, perms);
		RANK_LIST.add(rank);
		
		perms = new ArrayList<String>();
		perms.add("*");
		rank = new RankImpl(20, "leader", "Leader", "&4&lLeader", false, perms);
		RANK_LIST.add(rank);
		RANK_SET_HIGHER = 20;

		
		// Additional settings
		COLOR_ENABLE = false;
		COLOR_COLORCMD = true;
		COLOR_DYNAMIC = false;
		COLOR_LIST = new HashSet<Color>();
		COLOR_LIST.add(new ColorImpl("red", "red", "&c", -1, -1, -1));
		COLOR_LIST.add(new ColorImpl("green", "green", "&a", -1, -1, -1));
		COLOR_LIST.add(new ColorImpl("yourcustomcolor", "special", "&9&n", -1, -1, -1));
		
		DESC_ENABLE = true;
		DESC_MINLENGTH = 3;
		DESC_MAXLENGTH = 16;
		DESC_ALLOWEDCHARS = "[a-zA-Z0-9\\ \\.\\,\\-\\_]+";
		
		FIXED_ENABLE = false;
		FIXED_DEFAULT_ENABLE = false;
		FIXED_DEFAULT_PARTY = "default";
		
		FRIENDLYFIRE_ENABLE = true;
		FRIENDLYFIRE_WARNONFIGHT = true;
		FRIENDLYFIRE_LISTWORLDS = new ArrayList<String>();
		FRIENDLYFIRE_CRACKSHOT_ENABLE = false;
		
		HOME_ENABLE = false;
		HOME_DELAY = 0;
		HOME_HIT = true;
		HOME_MOVING = true;
		HOME_DISTANCE = 3;
		
		PASSWORD_ENABLE = false;
		PASSWORD_ALLOWECHARS = "[a-zA-Z0-9]+";
		PASSWORD_HASH = "MD5";
		PASSWORD_ENCODE = "UTF-8";
		PASSWORD_MINLENGTH = 1;
		PASSWORD_MAXLENGTH = 16;
		
		KILLS_ENABLE = false;
		KILLS_SAVEKILLS = true;
		KILLS_MOB_NEUTRAL = false;
		KILLS_MOB_HOSTILE = false;
		KILLS_MOB_PLAYERS = true;
		
		LIST_ENABLE = true;
		LIST_ORDEREDBY = "players";
		LIST_FILTERMIN = 1;
		LIST_PERPAGE = 8;
		LIST_LIMITPARTIES = -1;
		LIST_HIDDENPARTIES = new ArrayList<String>();
		
		MOTD_ENABLE = true;
		MOTD_MINLENGTH = 3;
		MOTD_MAXLENGTH = 100;
		MOTD_DELAY = 20;
		MOTD_ALLOWEDCHARS = "[a-zA-Z0-9\\ \\.\\,\\-\\_]+";
		MOTD_NEWLINECODE = "\\\\n";
		
		TELEPORT_ENABLE = false;
		TELEPORT_COOLDOWN = 60;
	}
}
