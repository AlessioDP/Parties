package com.alessiodp.parties.configuration.data;

import java.util.ArrayList;
import java.util.List;

public class ConfigMain {
	
	// Parties settings
	public static boolean		PARTIES_UPDATES_CHECK;
	public static boolean		PARTIES_UPDATES_WARN;
	public static boolean		PARTIES_SEEINVISIBLE;
	public static boolean		PARTIES_JLMESSAGES;
	public static boolean		PARTIES_BUNGEECORD;
	
	
	// Storage settings
	public static String		STORAGE_TYPE_LOG;
	public static String		STORAGE_TYPE_DATABASE;
	public static String		STORAGE_LOG_FORMAT;
	public static boolean		STORAGE_LOG_CHAT;
	public static boolean		STORAGE_LOG_PRINTCONSOLE;
	public static int			STORAGE_LOG_LEVEL;
	public static boolean		STORAGE_MIGRATE_INIT_YAML;
	public static boolean		STORAGE_MIGRATE_INIT_MYSQL;
	public static boolean		STORAGE_MIGRATE_INIT_SQLITE;
	public static boolean		STORAGE_MIGRATE_ONLYCONSOLE;
	public static String		STORAGE_MIGRATE_SUFFIX;
	
	public static String		STORAGE_SETTINGS_FILE_TXT_LOGNAME;
	public static String		STORAGE_SETTINGS_FILE_YAML_DBNAME;
	public static int			STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE;
	public static String		STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES;
	public static String		STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS;
	public static String		STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG;
	public static String		STORAGE_SETTINGS_SQL_GENERAL_TABLES_VERSIONS;
	public static String		STORAGE_SETTINGS_SQL_MYSQL_ADDRESS;
	public static String		STORAGE_SETTINGS_SQL_MYSQL_DATABASE;
	public static String		STORAGE_SETTINGS_SQL_MYSQL_USERNAME;
	public static String		STORAGE_SETTINGS_SQL_MYSQL_PASSWORD;
	public static int			STORAGE_SETTINGS_SQL_MYSQL_POOLSIZE;
	public static int			STORAGE_SETTINGS_SQL_MYSQL_CONNLIFETIME;
	public static int			STORAGE_SETTINGS_SQL_MYSQL_CONNTIMEOUT;
	public static boolean		STORAGE_SETTINGS_SQL_MYSQL_USESSL;
	public static String		STORAGE_SETTINGS_SQL_MYSQL_CHARSET;
	public static String		STORAGE_SETTINGS_SQL_SQLITE_DBNAME;
	public static boolean		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT;
	public static int			STORAGE_SETTINGS_NONE_DELAYDELETEPARTY;
	
	
	// Additional settings
	public static boolean		ADDITIONAL_AUTOCMD_ENABLE;
	public static List<String>	ADDITIONAL_AUTOCMD_BLACKLIST;
	public static List<String>	ADDITIONAL_AUTOCMD_WHITELIST;
	
	public static boolean		ADDITIONAL_CENSOR_ENABLE;
	public static boolean		ADDITIONAL_CENSOR_CASESENSITIVE;
	public static List<String>	ADDITIONAL_CENSOR_CONTAINS;
	public static List<String>	ADDITIONAL_CENSOR_STARTSWITH;
	public static List<String>	ADDITIONAL_CENSOR_ENDSWITH;
	
	public static boolean		ADDITIONAL_EXP_ENABLE;
	public static boolean		ADDITIONAL_EXP_DIVIDE;
	public static int			ADDITIONAL_EXP_SHAREIFMORE;
	public static String		ADDITIONAL_EXP_FORMULA;
	public static int			ADDITIONAL_EXP_RANGE;
	public static boolean		ADDITIONAL_EXP_HANDLE_VANILLA;
	public static boolean		ADDITIONAL_EXP_HANDLE_SKILLAPI;
	public static ExpType		ADDITIONAL_EXP_GIVEAS_VANILLA;
	public static ExpType		ADDITIONAL_EXP_GIVEAS_SKILLAPI;
	//public static boolean		ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE;
	//public static boolean		ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_VANILLA;
	//public static boolean		ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_SKILLAPI;
	public static boolean		ADDITIONAL_EXP_ADDONS_SKILLAPI_ENABLE;
	public static String		ADDITIONAL_EXP_ADDONS_SKILLAPI_SOURCE;
	
	public static boolean		ADDITIONAL_FOLLOW_ENABLE;
	public static int			ADDITIONAL_FOLLOW_TYPE;
	public static int			ADDITIONAL_FOLLOW_RANKNEEDED;
	public static int			ADDITIONAL_FOLLOW_RANKMINIMUM;
	public static int			ADDITIONAL_FOLLOW_TIMEOUT;
	public static List<String>	ADDITIONAL_FOLLOW_LISTWORLDS;
	
	public static boolean		ADDITIONAL_NOTIFY_ENABLE;
	public static boolean		ADDITIONAL_NOTIFY_BLOCK_INVITE;
	
	public static String		ADDITIONAL_PLACEHOLDER_COLOR_NAME;
	public static String		ADDITIONAL_PLACEHOLDER_COLOR_CODE;
	public static String		ADDITIONAL_PLACEHOLDER_COLOR_COMMAND;
	public static String		ADDITIONAL_PLACEHOLDER_DESC;
	public static String		ADDITIONAL_PLACEHOLDER_KILLS;
	public static String		ADDITIONAL_PLACEHOLDER_MOTD;
	public static String		ADDITIONAL_PLACEHOLDER_PARTY;
	public static String		ADDITIONAL_PLACEHOLDER_PREFIX;
	public static String		ADDITIONAL_PLACEHOLDER_RANK_NAME;
	public static String		ADDITIONAL_PLACEHOLDER_RANK_CHAT;
	public static String		ADDITIONAL_PLACEHOLDER_SUFFIX;
	
	public static boolean		ADDITIONAL_TAG_ENABLE;
	public static TagEngine		ADDITIONAL_TAG_ENGINE;
	public static TagType		ADDITIONAL_TAG_TYPE;
	public static String		ADDITIONAL_TAG_BASE_FORMATPREFIX;
	public static String		ADDITIONAL_TAG_BASE_FORMATSUFFIX;
	public static boolean		ADDITIONAL_TAG_CUSTOM_PREFIX;
	public static String		ADDITIONAL_TAG_CUSTOM_FORMATPREFIX;
	public static boolean		ADDITIONAL_TAG_CUSTOM_SUFFIX;
	public static String		ADDITIONAL_TAG_CUSTOM_FORMATSUFFIX;
	public static String		ADDITIONAL_TAG_CUSTOM_ALLOWEDCHARS;
	public static int			ADDITIONAL_TAG_CUSTOM_MINLENGTH;
	public static int			ADDITIONAL_TAG_CUSTOM_MAXLENGTH;
	
	
	// Addons settings
	public static boolean		ADDONS_BANMANAGER_ENABLE;
	public static boolean		ADDONS_BANMANAGER_PREVENTCHAT;
	public static boolean		ADDONS_BANMANAGER_AUTOKICK;
	
	public static boolean		ADDONS_DYNMAP_ENABLE;
	public static boolean		ADDONS_DYNMAP_HIDEDEFAULT;
	public static int			ADDONS_DYNMAP_MINPLAYERS;
	public static String		ADDONS_DYNMAP_MARKER_LAYER;
	public static String		ADDONS_DYNMAP_MARKER_LABEL;
	public static String		ADDONS_DYNMAP_MARKER_ICON;
	
	public static boolean		ADDONS_GRIEFPREVENTION_ENABLE;
	public static boolean		ADDONS_GRIEFPREVENTION_NEEDOWNER;
	public static String		ADDONS_GRIEFPREVENTION_CMD_TRUST;
	public static String		ADDONS_GRIEFPREVENTION_CMD_CONTAINER;
	public static String		ADDONS_GRIEFPREVENTION_CMD_ACCESS;
	public static String		ADDONS_GRIEFPREVENTION_CMD_REMOVE;
	
	public static boolean		ADDONS_TABLIST_ENABLE;
	public static String		ADDONS_TABLIST_INPARTY;
	public static String		ADDONS_TABLIST_OUTPARTY;
	public static String		ADDONS_TABLIST_HEADER_INPARTY;
	public static String		ADDONS_TABLIST_HEADER_OUTPARTY;
	public static String		ADDONS_TABLIST_FOOTER_INPARTY;
	public static String		ADDONS_TABLIST_FOOTER_OUTPARTY;
	
	public static boolean		ADDONS_VAULT_ENABLE;
	public static boolean		ADDONS_VAULT_CONFIRM_ENABLE;
	public static int			ADDONS_VAULT_CONFIRM_TIMEOUT;
	public static double		ADDONS_VAULT_PRICE_CLAIM;
	public static double		ADDONS_VAULT_PRICE_COLOR;
	public static double		ADDONS_VAULT_PRICE_CREATE;
	public static double		ADDONS_VAULT_PRICE_DESC;
	public static double		ADDONS_VAULT_PRICE_HOME;
	public static double		ADDONS_VAULT_PRICE_JOIN;
	public static double		ADDONS_VAULT_PRICE_MOTD;
	public static double		ADDONS_VAULT_PRICE_PREFIX;
	public static double		ADDONS_VAULT_PRICE_SETHOME;
	public static double		ADDONS_VAULT_PRICE_SUFFIX;
	public static double		ADDONS_VAULT_PRICE_TELEPORT;
	
	
	// Commands settings
	public static boolean		COMMANDS_TABSUPPORT;
	public static boolean		COMMANDS_HELP_PERMBASED;
	public static int			COMMANDS_HELP_PERPAGE;
	public static String		COMMANDS_DESC_PARTY;
	public static String		COMMANDS_DESC_P;
	
	public static String		COMMANDS_CMD_HELP;
	public static String		COMMANDS_CMD_PARTY;
	public static String		COMMANDS_CMD_P;
	public static String		COMMANDS_CMD_ACCEPT;
	public static String		COMMANDS_CMD_CHAT;
	public static String		COMMANDS_CMD_CLAIM;
	public static String		COMMANDS_CMD_COLOR;
	public static String		COMMANDS_CMD_CONFIRM;
	public static String		COMMANDS_CMD_CREATE;
	public static String		COMMANDS_CMD_DELETE;
	public static String		COMMANDS_CMD_DENY;
	public static String		COMMANDS_CMD_DESC;
	public static String		COMMANDS_CMD_HOME;
	public static String		COMMANDS_CMD_IGNORE;
	public static String		COMMANDS_CMD_INFO;
	public static String		COMMANDS_CMD_INVITE;
	public static String		COMMANDS_CMD_JOIN;
	public static String		COMMANDS_CMD_KICK;
	public static String		COMMANDS_CMD_LEAVE;
	public static String		COMMANDS_CMD_LIST;
	public static String		COMMANDS_CMD_MIGRATE;
	public static String		COMMANDS_CMD_MOTD;
	public static String		COMMANDS_CMD_NOTIFY;
	public static String		COMMANDS_CMD_PASSWORD;
	public static String		COMMANDS_CMD_PREFIX;
	public static String		COMMANDS_CMD_RANK;
	public static String		COMMANDS_CMD_RELOAD;
	public static String		COMMANDS_CMD_RENAME;
	public static String		COMMANDS_CMD_SETHOME;
	public static String		COMMANDS_CMD_SPY;
	public static String		COMMANDS_CMD_SUFFIX;
	public static String		COMMANDS_CMD_TELEPORT;
	
	public static String		COMMANDS_SUB_ON;
	public static String		COMMANDS_SUB_OFF;
	public static String		COMMANDS_SUB_SILENT;
	public static String		COMMANDS_SUB_FIXED;
	public static String		COMMANDS_SUB_REMOVE;
	
	
	public ConfigMain() {
		loadDefaults();
	}
	
	public void loadDefaults() {
		// Parties settings
		PARTIES_UPDATES_CHECK = true;
		PARTIES_UPDATES_WARN = true;
		PARTIES_SEEINVISIBLE = false;
		PARTIES_JLMESSAGES = false;
		PARTIES_BUNGEECORD = false;
		
		
		// Storage settings
		STORAGE_TYPE_LOG = "none";
		STORAGE_TYPE_DATABASE = "yaml";
		STORAGE_LOG_FORMAT = "%date% [%time%] (%level%) {%position%} %message%";
		STORAGE_LOG_CHAT = true;
		STORAGE_LOG_PRINTCONSOLE = true;
		STORAGE_LOG_LEVEL = 1;
		STORAGE_MIGRATE_INIT_YAML = false;
		STORAGE_MIGRATE_INIT_MYSQL = false;
		STORAGE_MIGRATE_INIT_SQLITE = false;
		STORAGE_MIGRATE_ONLYCONSOLE = true;
		STORAGE_MIGRATE_SUFFIX = "_backup";
		
		STORAGE_SETTINGS_FILE_TXT_LOGNAME = "log.txt";
		STORAGE_SETTINGS_FILE_YAML_DBNAME = "data.yml";
		STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE = 255;
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES = "parties_parties";
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS = "parties_players";
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG = "parties_log";
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_VERSIONS = "parties_versions";
		STORAGE_SETTINGS_SQL_MYSQL_ADDRESS = "localhost:3306";
		STORAGE_SETTINGS_SQL_MYSQL_DATABASE = "database";
		STORAGE_SETTINGS_SQL_MYSQL_USERNAME = "username";
		STORAGE_SETTINGS_SQL_MYSQL_PASSWORD = "password";
		STORAGE_SETTINGS_SQL_MYSQL_POOLSIZE = 10;
		STORAGE_SETTINGS_SQL_MYSQL_CONNLIFETIME = 1800000;
		STORAGE_SETTINGS_SQL_MYSQL_CONNTIMEOUT = 10000;
		STORAGE_SETTINGS_SQL_MYSQL_USESSL = true;
		STORAGE_SETTINGS_SQL_MYSQL_CHARSET = "utf8";
		STORAGE_SETTINGS_SQL_SQLITE_DBNAME = "database.db";
		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT = true;
		STORAGE_SETTINGS_NONE_DELAYDELETEPARTY = 600;
		
		
		// Additional features
		ADDITIONAL_AUTOCMD_ENABLE = false;
		ADDITIONAL_AUTOCMD_BLACKLIST = new ArrayList<String>();
		ADDITIONAL_AUTOCMD_BLACKLIST.add("/pay");
		ADDITIONAL_AUTOCMD_BLACKLIST.add("/money");
		ADDITIONAL_AUTOCMD_WHITELIST = new ArrayList<String>();
		
		ADDITIONAL_CENSOR_ENABLE = false;
		ADDITIONAL_CENSOR_CASESENSITIVE = false;
		ADDITIONAL_CENSOR_CONTAINS = new ArrayList<String>();
		ADDITIONAL_CENSOR_STARTSWITH = new ArrayList<String>();
		ADDITIONAL_CENSOR_ENDSWITH = new ArrayList<String>();
		
		ADDITIONAL_EXP_ENABLE = false;
		ADDITIONAL_EXP_DIVIDE = true;
		ADDITIONAL_EXP_SHAREIFMORE = 1;
		ADDITIONAL_EXP_FORMULA = "%exp% / %number%";
		ADDITIONAL_EXP_RANGE = 15;
		ADDITIONAL_EXP_HANDLE_VANILLA = true;
		ADDITIONAL_EXP_HANDLE_SKILLAPI = true;
		ADDITIONAL_EXP_GIVEAS_VANILLA = ExpType.VANILLA;
		ADDITIONAL_EXP_GIVEAS_SKILLAPI = ExpType.SKILLAPI;
		//ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE = true;
		//ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_VANILLA = true;
		//ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_SKILLAPI = true;
		ADDITIONAL_EXP_ADDONS_SKILLAPI_ENABLE = false;
		ADDITIONAL_EXP_ADDONS_SKILLAPI_SOURCE = "MOB";
		
		ADDITIONAL_FOLLOW_ENABLE = false;
		ADDITIONAL_FOLLOW_TYPE = 1;
		ADDITIONAL_FOLLOW_RANKNEEDED = 0;
		ADDITIONAL_FOLLOW_RANKMINIMUM = 0;
		ADDITIONAL_FOLLOW_TIMEOUT = 100;
		ADDITIONAL_FOLLOW_LISTWORLDS = new ArrayList<String>();
		ADDITIONAL_FOLLOW_LISTWORLDS.add("*");
		
		ADDITIONAL_NOTIFY_ENABLE = false;
		ADDITIONAL_NOTIFY_BLOCK_INVITE = true;
		
		ADDITIONAL_PLACEHOLDER_COLOR_NAME = "%color_name%";
		ADDITIONAL_PLACEHOLDER_COLOR_CODE = "%color_code%";
		ADDITIONAL_PLACEHOLDER_COLOR_COMMAND = "%color_command%";
		ADDITIONAL_PLACEHOLDER_DESC = "%desc%";
		ADDITIONAL_PLACEHOLDER_KILLS = "%kills%";
		ADDITIONAL_PLACEHOLDER_MOTD = "%motd%";
		ADDITIONAL_PLACEHOLDER_PARTY = "%party%";
		ADDITIONAL_PLACEHOLDER_PREFIX = "%prefix%";
		ADDITIONAL_PLACEHOLDER_RANK_NAME = "%rank_name%";
		ADDITIONAL_PLACEHOLDER_RANK_CHAT = "%rank_chat%";
		ADDITIONAL_PLACEHOLDER_SUFFIX = "%suffix%";
		
		ADDITIONAL_TAG_ENABLE = false;
		ADDITIONAL_TAG_ENGINE = TagEngine.SCOREBOARD;
		ADDITIONAL_TAG_TYPE = TagType.BASE;
		ADDITIONAL_TAG_BASE_FORMATPREFIX = "[%party%] ";
		ADDITIONAL_TAG_BASE_FORMATSUFFIX = "";
		ADDITIONAL_TAG_CUSTOM_PREFIX = true;
		ADDITIONAL_TAG_CUSTOM_FORMATPREFIX = "[%prefix%] ";
		ADDITIONAL_TAG_CUSTOM_SUFFIX = false;
		ADDITIONAL_TAG_CUSTOM_FORMATSUFFIX = " [%suffix%]";
		ADDITIONAL_TAG_CUSTOM_ALLOWEDCHARS = "[a-zA-Z0-9\\ \\.\\,\\_\\-]+";
		ADDITIONAL_TAG_CUSTOM_MINLENGTH = 3;
		ADDITIONAL_TAG_CUSTOM_MAXLENGTH = 6;
		
		
		// Addons settings
		ADDONS_BANMANAGER_ENABLE = false;
		ADDONS_BANMANAGER_PREVENTCHAT = true;
		ADDONS_BANMANAGER_AUTOKICK = true;
		
		ADDONS_DYNMAP_ENABLE = false;
		ADDONS_DYNMAP_HIDEDEFAULT = false;
		ADDONS_DYNMAP_MINPLAYERS = 3;
		ADDONS_DYNMAP_MARKER_LAYER = "Party homes";
		ADDONS_DYNMAP_MARKER_LABEL = "<b>%party%</b>'s home";
		ADDONS_DYNMAP_MARKER_ICON = "house";
		
		ADDONS_GRIEFPREVENTION_ENABLE = false;
		ADDONS_GRIEFPREVENTION_NEEDOWNER = false;
		ADDONS_GRIEFPREVENTION_CMD_TRUST = "trust";
		ADDONS_GRIEFPREVENTION_CMD_CONTAINER = "container";
		ADDONS_GRIEFPREVENTION_CMD_ACCESS = "access";
		ADDONS_GRIEFPREVENTION_CMD_REMOVE = "remove";
		
		ADDONS_TABLIST_ENABLE = false;
		ADDONS_TABLIST_INPARTY = "{\"text\":\"%player%\",\"color\":\"aqua\"}";
		ADDONS_TABLIST_OUTPARTY = "";
		ADDONS_TABLIST_HEADER_INPARTY = "&b> %party% <";
		ADDONS_TABLIST_HEADER_OUTPARTY = "&cOut of party";
		ADDONS_TABLIST_FOOTER_INPARTY = "&b&lParties";
		ADDONS_TABLIST_FOOTER_OUTPARTY = "&b&lParties";
		
		ADDONS_VAULT_ENABLE = false;
		ADDONS_VAULT_CONFIRM_ENABLE = true;
		ADDONS_VAULT_CONFIRM_TIMEOUT = 10000;
		ADDONS_VAULT_PRICE_CLAIM = 0.0;
		ADDONS_VAULT_PRICE_COLOR = 0.0;
		ADDONS_VAULT_PRICE_CREATE = 0.0;
		ADDONS_VAULT_PRICE_DESC = 0.0;
		ADDONS_VAULT_PRICE_HOME = 0.0;
		ADDONS_VAULT_PRICE_JOIN = 0.0;
		ADDONS_VAULT_PRICE_MOTD = 0.0;
		ADDONS_VAULT_PRICE_PREFIX = 0.0;
		ADDONS_VAULT_PRICE_SETHOME = 0.0;
		ADDONS_VAULT_PRICE_SUFFIX = 0.0;
		ADDONS_VAULT_PRICE_TELEPORT = 0.0;
		
		
		// Commands settings
		COMMANDS_TABSUPPORT = true;
		COMMANDS_HELP_PERMBASED = true;
		COMMANDS_HELP_PERPAGE = 9;
		COMMANDS_DESC_PARTY = "Parties help page";
		COMMANDS_DESC_P = "Send a party message";
		
		COMMANDS_CMD_HELP = "help";
		COMMANDS_CMD_PARTY = "party";
		COMMANDS_CMD_P = "p";
		COMMANDS_CMD_ACCEPT = "accept";
		COMMANDS_CMD_CHAT = "chat";
		COMMANDS_CMD_CLAIM = "claim";
		COMMANDS_CMD_COLOR = "color";
		COMMANDS_CMD_CONFIRM = "confirm";
		COMMANDS_CMD_CREATE = "create";
		COMMANDS_CMD_DELETE = "delete";
		COMMANDS_CMD_DENY = "deny";
		COMMANDS_CMD_DESC = "desc";
		COMMANDS_CMD_HOME = "home";
		COMMANDS_CMD_IGNORE = "ignore";
		COMMANDS_CMD_INFO = "info";
		COMMANDS_CMD_INVITE = "invite";
		COMMANDS_CMD_JOIN = "join";
		COMMANDS_CMD_KICK = "kick";
		COMMANDS_CMD_LEAVE = "leave";
		COMMANDS_CMD_LIST = "list";
		COMMANDS_CMD_MIGRATE = "migrate";
		COMMANDS_CMD_MOTD = "motd";
		COMMANDS_CMD_NOTIFY = "notify";
		COMMANDS_CMD_PASSWORD = "password";
		COMMANDS_CMD_PREFIX = "prefix";
		COMMANDS_CMD_RANK = "rank";
		COMMANDS_CMD_RELOAD = "reload";
		COMMANDS_CMD_RENAME = "rename";
		COMMANDS_CMD_SETHOME = "sethome";
		COMMANDS_CMD_SPY = "spy";
		COMMANDS_CMD_SUFFIX = "suffix";
		COMMANDS_CMD_TELEPORT = "teleport";
		
		COMMANDS_SUB_ON = "on";
		COMMANDS_SUB_OFF = "off";
		COMMANDS_SUB_SILENT = "silent";
		COMMANDS_SUB_FIXED = "fixed";
		COMMANDS_SUB_REMOVE = "remove";
	}
	
	public enum TagType {
		BASE, CUSTOM;
		
		public boolean isBase() {
			return this.equals(BASE);
		}
		public boolean isCustom() {
			return this.equals(CUSTOM);
		}
		public static TagType getValue(String name) {
			TagType ret = BASE;
			if (name.equalsIgnoreCase("custom"))
				ret = CUSTOM;
			return ret;
		}
	}
	
	public enum TagEngine {
		SCOREBOARD;
		
		public boolean isScoreboard() {
			return this.equals(SCOREBOARD);
		}
		public static TagEngine getValue(String name) {
			TagEngine ret = SCOREBOARD;
			return ret;
		}
	}
	
	public enum ExpType {
		VANILLA, SKILLAPI;
		
		public boolean isVanilla() {
			return this.equals(VANILLA);
		}
		public boolean isSkillAPI() {
			return this.equals(SKILLAPI);
		}
		public static ExpType getValue(String name) {
			ExpType ret = VANILLA;
			if (name.equalsIgnoreCase("skillapi"))
				ret = SKILLAPI;
			return ret;
		}
	}
}
