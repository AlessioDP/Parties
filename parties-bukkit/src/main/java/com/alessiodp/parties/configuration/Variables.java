package com.alessiodp.parties.configuration;

import java.util.ArrayList;
import java.util.List;

import com.alessiodp.parties.objects.RankObj;
import com.alessiodp.partiesapi.interfaces.Rank;

public class Variables {
	
	public static boolean downloadupdates;
	public static boolean warnupdates;
	public static boolean perpermissionhelp;
	public static int commandsperpage;
	public static boolean commandtab;
	
	public static boolean fixedparty;
	public static boolean default_enable;
	public static String default_party;
	public static boolean invisibleallies;
	public static boolean bungeecord;
	public static boolean joinleavemessages;
	
	public static boolean log_enable;
	public static String log_format;
	public static boolean log_chat;
	public static boolean log_printconsole;
	public static int log_mode;
	public static String log_type;
	public static String log_file_name;
	public static String log_sql_url;
	public static String log_sql_username;
	public static String log_sql_password;
	public static int log_sql_varcharsize;
	public static String log_sql_logtable;
	
	public static String database_type;
	public static boolean database_migrate_console;
	public static String database_migrate_suffix;
	public static boolean database_none_leaderleft;
	public static int database_none_delay;
	public static String database_file_name;
	public static boolean database_sql_enable;
	public static String database_sql_url;
	public static String database_sql_username;
	public static String database_sql_password;
	public static int database_sql_varcharsize;
	public static String database_sql_tables_spies;
	public static String database_sql_tables_players;
	public static String database_sql_tables_parties;
	
	public static List<Rank> rank_list;
	public static int rank_default;
	public static int rank_last;
	
	public static int party_maxmembers;
	public static String party_allowedchars;
	public static int party_maxlengthname;
	public static int party_minlengthname;
	public static int party_renamecooldown;
	public static String party_placeholder;
	
	public static boolean friendlyfire_enable;
	public static boolean friendlyfire_warn;
	public static List<String> friendlyfire_listworlds;
	
	public static int invite_timeout;
	public static boolean invite_revoke;
	public static boolean invite_nopex;
	
	public static int home_cooldown;
	public static boolean home_cancelmove;
	public static int home_distance;
	
	public static boolean teleport_enable;
	public static int teleport_delay;
	
	public static boolean password_enable;
	public static boolean password_bypassleave;
	public static String password_allowedchars;
	public static String password_hash;
	public static String password_encode;
	public static int password_lengthmin;
	public static int password_lengthmax;
	
	public static int desc_min;
	public static int desc_max;
	public static String desc_allowedchars;
	public static String desc_removeword;
	public static List<String> desc_censored;
	
	public static int motd_min;
	public static int motd_max;
	public static int motd_delay;
	public static String motd_allowedchars;
	public static String motd_newline;
	public static String motd_removeword;
	public static List<String> motd_censored;
	
	public static boolean kill_enable;
	public static boolean kill_save_mobsneutral;
	public static boolean kill_save_mobshostile;
	public static boolean kill_save_players;
	
	public static boolean tablist_enable;
	public static String tablist_inparty;
	public static String tablist_outparty;
	public static String tablist_header_inparty;
	public static String tablist_header_outparty;
	public static String tablist_footer_inparty;
	public static String tablist_footer_outparty;
	
	public static boolean tag_enable;
	public static boolean tag_system;
	public static String tag_base_formatprefix;
	public static String tag_base_formatsuffix;
	public static boolean tag_custom_prefix;
	public static String tag_custom_formatprefix;
	public static boolean tag_custom_suffix;
	public static String tag_custom_formatsuffix;
	public static String tag_custom_removeword;
	public static String tag_custom_allowedchars;
	public static int tag_custom_maxlength;
	public static int tag_custom_minlength;
	public static List<String> tag_custom_censor;
	
	public static String chat_chatformat;
	public static boolean chat_allowcolors;
	public static int chat_chatcooldown;
	public static String chat_spychatformat;
	public static String chat_partybroadcastformat;
	public static String chat_formatgroup;
	
	public static String list_orderedby;
	public static int list_filter;
	public static int list_maxpages;
	public static List<String> list_hiddenparty;
	
	public static boolean follow_enable;
	public static int follow_type;
	public static int follow_neededrank;
	public static int follow_minimumrank;
	public static int follow_timeoutportal;
	public static List<String> follow_listworlds;
	
	public static boolean autocommand_enable;
	public static List<String> autocommand_blacklist;
	public static List<String> autocommand_whitelist;
	
	public static boolean censor_enable;
	public static boolean censor_casesensitive;
	public static List<String> censor_contains;
	public static List<String> censor_startswith;
	public static List<String> censor_endswith;
	
	public static boolean exp_enable;
	public static boolean exp_divide;
	public static int exp_sharemorethan;
	public static String exp_formula;
	public static int exp_range;
	public static boolean exp_skillapi_enable;
	public static String exp_skillapi_source;
	/*public static boolean exp_mythicmobs_enable;
	public static boolean exp_mythicmobs_normalexp;
	public static boolean exp_mythicmobs_skillapiexp;*/
	
	public static boolean banmanager_enable;
	public static boolean banmanager_muted;
	public static boolean banmanager_kickban;
	
	public static boolean dynmap_enable;
	public static boolean dynmap_showpartyhomes;
	public static boolean dynmap_hidedefault;
	public static int dynmap_settings_minimumplayers;
	public static String dynmap_marker_layer;
	public static String dynmap_marker_label;
	public static String dynmap_marker_icon;
	
	public static boolean griefprevention_enable;
	public static boolean griefprevention_needowner;
	public static String griefprevention_command_trust;
	public static String griefprevention_command_container;
	public static String griefprevention_command_access;
	public static String griefprevention_command_remove;
	
	public static boolean vault_enable;
	public static boolean vault_confirm_enable;
	public static int vault_confirm_timeout;
	public static double vault_command_create_price;
	public static double vault_command_join_price;
	public static double vault_command_home_price;
	public static double vault_command_sethome_price;
	public static double vault_command_desc_price;
	public static double vault_command_motd_price;
	public static double vault_command_prefix_price;
	public static double vault_command_suffix_price;
	public static double vault_command_teleport_price;
	public static double vault_command_claim_price;
	
	public static String command_party;
	public static String command_party_desc;
	public static String command_help;
	public static String command_p;
	public static String command_p_desc;
	public static String command_create;
	public static String command_password;
	public static String command_join;
	public static String command_accept;
	public static String command_deny;
	public static String command_ignore;
	public static String command_leave;
	public static String command_list;
	public static String command_info;
	public static String command_members;
	public static String command_home;
	public static String command_sethome;
	public static String command_teleport;
	public static String command_desc;
	public static String command_motd;
	public static String command_chat;
	public static String command_invite;
	public static String command_prefix;
	public static String command_suffix;
	public static String command_rank;
	public static String command_kick;
	public static String command_delete;
	public static String command_rename;
	public static String command_silent;
	public static String command_leader;
	public static String command_spy;
	public static String command_reload;
	public static String command_sub_on;
	public static String command_sub_off;
	public static String command_sub_fixed;
	public static String command_migrate;
	public static String command_claim;
	public static String command_confirm;
	
	public Variables() {
		loadDefaults();
	}
	public void loadDefaults() {
		downloadupdates = false;
		warnupdates = true;
		bungeecord = false;
		perpermissionhelp = true;
		commandsperpage = 9;
		commandtab = true;
		
		fixedparty = false;
		default_enable = false;
		default_party = "default";
		invisibleallies = false;
		joinleavemessages = false;
		
		log_enable = false;
		log_format = "%date% [%time%] (%level%) {%position%} %message%";
		log_chat = true;
		log_printconsole = true;
		log_mode = 1;
		log_type = "file";
		log_file_name = "log.txt";
		log_sql_url = "jdbc:mysql://localhost:3306/database";
		log_sql_username = "username";
		log_sql_password = "password";
		log_sql_varcharsize = 255;
		log_sql_logtable = "log";
		
		database_type = "file";
		database_migrate_console = true;
		database_migrate_suffix = "_backup";
		database_none_leaderleft = true;
		database_none_delay = 600;
		database_file_name = "data.yml";
		database_sql_enable = false;
		database_sql_url = "jdbc:mysql://localhost:3306/database";
		database_sql_username = "username";
		database_sql_password = "password";
		database_sql_varcharsize = 255;
		database_sql_tables_spies = "spies";
		database_sql_tables_players = "players";
		database_sql_tables_parties = "parties";
		
		List<String> perms = new ArrayList<String>();
		rank_list = new ArrayList<Rank>();
		perms.add("party.sendmessage");
		perms.add("party.home");
		perms.add("party.desc");
		perms.add("party.motd");
		perms.add("party.claim");
		rank_list.add(new RankObj(5, "member", "Member", "&bMember", true, perms));
		perms = new ArrayList<String>();
		perms.add("-party.edit.home");
		perms.add("-party.edit.desc");
		perms.add("-party.edit.motd");
		perms.add("-party.edit.prefix");
		perms.add("-party.edit.suffix");
		perms.add("-party.edit.password");
		perms.add("-party.admin.rename");
		perms.add("-party.admin.rank");
		perms.add("-party.admin.teleport");
		perms.add("-party.autocommand");
		perms.add("*");
		rank_list.add(new RankObj(10, "moderator", "Moderator", "&cModerator", false, perms));
		perms = new ArrayList<String>();
		perms.add("*");
		rank_list.add(new RankObj(20, "leader", "Leader", "&4&lLeader", false, perms));
		rank_default = 5;
		rank_last = 20;
		
		party_maxmembers = -1;
		party_allowedchars = "[a-zA-Z0-9]+";
		party_minlengthname = 3;
		party_maxlengthname = 10;
		party_renamecooldown = 300;
		party_placeholder = "[%party%] ";
		
		friendlyfire_enable = true;
		friendlyfire_warn = true;
		List<String> list = new ArrayList<String>();
		list.add("*");
		friendlyfire_listworlds = list;
		
		invite_timeout = 20;
		invite_revoke = true;
		invite_nopex = true;
		
		home_cooldown = 0;
		home_cancelmove = true;
		home_distance = 3;
		
		teleport_enable = false;
		teleport_delay = 60;
		
		password_enable = false;
		password_bypassleave = false;
		password_allowedchars = "[a-zA-Z0-9]+";
		password_hash = "MD5";
		password_encode = "UTF-8";
		password_lengthmin = 1;
		password_lengthmax = 16;
		
		desc_min = 3;
		desc_max = 16;
		desc_allowedchars = "[a-zA-Z0-9 .,]+";
		desc_removeword = "remove";
		desc_censored = new ArrayList<String>();
		
		motd_min = 3;
		motd_max = 100;
		motd_delay = 20;
		motd_allowedchars = "[a-zA-Z0-9\\ .,]+";
		motd_newline = "\\n";
		motd_removeword = "remove";
		motd_censored = new ArrayList<String>();
		
		kill_enable = false;
		kill_save_mobsneutral = false;
		kill_save_mobshostile = false;
		kill_save_players = true;
		
		tablist_enable = false;
		tablist_inparty = "{\"text\":\"%player%\",\"color\":\"aqua\"}";
		tablist_outparty = "";
		tablist_header_inparty = "&b> %party% <";
		tablist_header_outparty = "&cOut of party";
		tablist_footer_inparty = "&b&lParties";
		tablist_footer_outparty = "&b&lParties";
		
		tag_enable = false;
		tag_system = true;
		tag_base_formatprefix = "[%name%] ";
		tag_base_formatsuffix = "";
		tag_custom_prefix = true;
		tag_custom_formatprefix = "[%prefix%] ";
		tag_custom_suffix = false;
		tag_custom_formatsuffix = " [%suffix%]";
		tag_custom_removeword = "remove";
		tag_custom_allowedchars = "[a-zA-Z0-9 .,]+";
		tag_custom_maxlength = 6;
		tag_custom_minlength = 3;
		tag_custom_censor = new ArrayList<String>();
		
		chat_chatformat = "&b[Party] %rank% %player%&r&7: &b%message%";
		chat_allowcolors = false;
		chat_chatcooldown = 0;
		chat_spychatformat = "&7[SPY] [Party:%party%] %player%: %message%";
		chat_partybroadcastformat = "&b[Party] %message%";
		chat_formatgroup = "[%group%] ";
		
		list_orderedby = "players";
		list_filter = 1;
		list_maxpages = 8;
		list_hiddenparty = new ArrayList<String>();
		
		follow_enable = false;
		follow_type = 1;
		follow_neededrank = 0;
		follow_minimumrank = 0;
		follow_timeoutportal = 100;
		list = new ArrayList<String>();
		list.add("*");
		follow_listworlds = list;
		
		autocommand_enable = false;
		list = new ArrayList<String>();
		list.add("/pay");
		list.add("/money");
		autocommand_blacklist = list;
		list = new ArrayList<String>();
		autocommand_whitelist = list;
		
		censor_enable = false;
		censor_casesensitive = false;
		censor_contains = new ArrayList<String>();
		censor_startswith = new ArrayList<String>();
		censor_endswith = new ArrayList<String>();
		
		exp_enable = false;
		exp_divide = true;
		exp_sharemorethan = 1;
		exp_formula = "%exp% / %number%";
		exp_range = 15;
		exp_skillapi_enable = false;
		exp_skillapi_source = "MOB";
		/*exp_mythicmobs_enable = false;
		exp_mythicmobs_normalexp = true;
		exp_mythicmobs_skillapiexp = true;*/
		
		banmanager_enable = false;
		banmanager_muted = true;
		banmanager_kickban = true;
		
		dynmap_enable = false;
		dynmap_showpartyhomes = true;
		dynmap_hidedefault = false;
		dynmap_settings_minimumplayers = 3;
		dynmap_marker_layer = "Party homes";
		dynmap_marker_label = "%party%'s home";
		dynmap_marker_icon = "house";
		
		griefprevention_enable = false;
		griefprevention_needowner = false;
		griefprevention_command_trust = "trust";
		griefprevention_command_container = "container";
		griefprevention_command_access = "access";
		griefprevention_command_remove = "remove";
		
		vault_enable = false;
		vault_confirm_enable = true;
		vault_confirm_timeout = 10000;
		vault_command_create_price = 0;
		vault_command_join_price = 0;
		vault_command_home_price = 0;
		vault_command_sethome_price = 0;
		vault_command_desc_price = 0;
		vault_command_motd_price = 0;
		vault_command_prefix_price = 0;
		vault_command_suffix_price = 0;
		vault_command_teleport_price = 0;
		vault_command_claim_price = 0;
		
		command_party = "party";
		command_party_desc = "Parties help page";
		command_help = "help";
		command_p = "p";
		command_p_desc = "Send a party message";
		command_create = "create";
		command_password = "password";
		command_join = "join";
		command_accept = "accept";
		command_deny = "deny";
		command_ignore = "ignore";
		command_leave = "leave";
		command_list = "list";
		command_info = "info";
		command_members = "members";
		command_home = "home";
		command_sethome = "sethome";
		command_teleport = "teleport";
		command_desc = "desc";
		command_motd = "motd";
		command_chat = "chat";
		command_invite = "invite";
		command_prefix = "prefix";
		command_suffix = "suffix";
		command_rank = "rank";
		command_kick = "kick";
		command_delete = "delete";
		command_rename = "rename";
		command_silent = "silent";
		command_spy = "spy";
		command_reload = "reload";
		command_sub_on = "on";
		command_sub_off = "off";
		command_sub_fixed = "fixed";
		command_migrate = "migrate";
		command_claim = "claim";
		command_confirm = "confirm";
	}
}
