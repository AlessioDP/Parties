package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.ConfigurationFile;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;

import java.util.ArrayList;
import java.util.List;


public abstract class ConfigMain extends ConfigurationFile {
	// Parties settings
	public static boolean		PARTIES_UPDATES_CHECK;
	public static boolean		PARTIES_UPDATES_WARN;
	public static boolean		PARTIES_JLMESSAGES;
	
	
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
	public static boolean		STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_SAVEOLD;
	public static String		STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_OLDSUFFIX;
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
	
	public static boolean		ADDITIONAL_MUTE_ENABLE;
	public static boolean		ADDITIONAL_MUTE_BLOCK_INVITE;
	
	public static String		ADDITIONAL_PLACEHOLDER_COLOR_NAME;
	public static String		ADDITIONAL_PLACEHOLDER_COLOR_CODE;
	public static String		ADDITIONAL_PLACEHOLDER_COLOR_COMMAND;
	public static String		ADDITIONAL_PLACEHOLDER_DESC;
	public static String		ADDITIONAL_PLACEHOLDER_EXPERIENCE_TOTAL;
	public static String		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVEL;
	public static String		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_CURRENT;
	public static String		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_NECESSARY;
	public static String		ADDITIONAL_PLACEHOLDER_KILLS;
	public static String		ADDITIONAL_PLACEHOLDER_MOTD;
	public static String		ADDITIONAL_PLACEHOLDER_PARTY;
	public static String		ADDITIONAL_PLACEHOLDER_RANK_NAME;
	public static String		ADDITIONAL_PLACEHOLDER_RANK_CHAT;
	
	
	// Commands settings
	public static int			COMMANDS_HELP_PERPAGE;
	
	public static String		COMMANDS_CMD_HELP;
	public static String		COMMANDS_CMD_PARTY;
	public static String		COMMANDS_CMD_P;
	public static String		COMMANDS_CMD_ACCEPT;
	public static String		COMMANDS_CMD_CHAT;
	public static String		COMMANDS_CMD_COLOR;
	public static String		COMMANDS_CMD_CREATE;
	public static String		COMMANDS_CMD_DELETE;
	public static String		COMMANDS_CMD_DENY;
	public static String		COMMANDS_CMD_DESC;
	public static String		COMMANDS_CMD_IGNORE;
	public static String		COMMANDS_CMD_INFO;
	public static String		COMMANDS_CMD_INVITE;
	public static String		COMMANDS_CMD_JOIN;
	public static String		COMMANDS_CMD_KICK;
	public static String		COMMANDS_CMD_LEAVE;
	public static String		COMMANDS_CMD_LIST;
	public static String		COMMANDS_CMD_MIGRATE;
	public static String		COMMANDS_CMD_MOTD;
	public static String		COMMANDS_CMD_MUTE;
	public static String		COMMANDS_CMD_PASSWORD;
	public static String		COMMANDS_CMD_RANK;
	public static String		COMMANDS_CMD_RELOAD;
	public static String		COMMANDS_CMD_RENAME;
	public static String		COMMANDS_CMD_SPY;
	
	public static String		COMMANDS_SUB_ON;
	public static String		COMMANDS_SUB_OFF;
	public static String		COMMANDS_SUB_SILENT;
	public static String		COMMANDS_SUB_FIXED;
	public static String		COMMANDS_SUB_REMOVE;
	
	public static List<String>	COMMANDS_ORDER;
	
	
	protected ConfigMain(PartiesPlugin instance) {
		super(instance);
	}
	
	public void loadDefaults() {
		// Parties settings
		PARTIES_UPDATES_CHECK = true;
		PARTIES_UPDATES_WARN = true;
		PARTIES_JLMESSAGES = false;
		
		
		// Storage settings
		STORAGE_TYPE_LOG = "none";
		STORAGE_TYPE_DATABASE = "yaml";
		STORAGE_LOG_FORMAT = "%date% [%time%] (%level%) {%position%} %message%\n";
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
		STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_SAVEOLD = true;
		STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_OLDSUFFIX = "_backup";
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
		STORAGE_SETTINGS_SQL_MYSQL_USESSL = true;
		STORAGE_SETTINGS_SQL_MYSQL_CHARSET = "utf8";
		STORAGE_SETTINGS_SQL_SQLITE_DBNAME = "database.db";
		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT = true;
		STORAGE_SETTINGS_NONE_DELAYDELETEPARTY = 600;
		
		
		// Additional features
		ADDITIONAL_AUTOCMD_ENABLE = false;
		ADDITIONAL_AUTOCMD_BLACKLIST = new ArrayList<>();
		ADDITIONAL_AUTOCMD_BLACKLIST.add("/pay");
		ADDITIONAL_AUTOCMD_BLACKLIST.add("/money");
		ADDITIONAL_AUTOCMD_WHITELIST = new ArrayList<>();
		
		ADDITIONAL_CENSOR_ENABLE = false;
		ADDITIONAL_CENSOR_CASESENSITIVE = false;
		ADDITIONAL_CENSOR_CONTAINS = new ArrayList<>();
		ADDITIONAL_CENSOR_STARTSWITH = new ArrayList<>();
		ADDITIONAL_CENSOR_ENDSWITH = new ArrayList<>();
		
		ADDITIONAL_MUTE_ENABLE = false;
		ADDITIONAL_MUTE_BLOCK_INVITE = true;
		
		ADDITIONAL_PLACEHOLDER_COLOR_NAME = "%color_name%";
		ADDITIONAL_PLACEHOLDER_COLOR_CODE = "%color_code%";
		ADDITIONAL_PLACEHOLDER_COLOR_COMMAND = "%color_command%";
		ADDITIONAL_PLACEHOLDER_DESC = "%desc%";
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_TOTAL = "%experience_total%";
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVEL = "%experience_level%";
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_CURRENT = "%experience_levelup_current%";
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_NECESSARY = "%experience_levelup_necessary%";
		ADDITIONAL_PLACEHOLDER_KILLS = "%kills%";
		ADDITIONAL_PLACEHOLDER_MOTD = "%motd%";
		ADDITIONAL_PLACEHOLDER_PARTY = "%party%";
		ADDITIONAL_PLACEHOLDER_RANK_NAME = "%rank_name%";
		ADDITIONAL_PLACEHOLDER_RANK_CHAT = "%rank_chat%";
		
		
		// Commands settings
		COMMANDS_HELP_PERPAGE = 9;
		
		COMMANDS_CMD_HELP = "help";
		COMMANDS_CMD_PARTY = "party";
		COMMANDS_CMD_P = "p";
		COMMANDS_CMD_ACCEPT = "accept";
		COMMANDS_CMD_CHAT = "chat";
		COMMANDS_CMD_COLOR = "color";
		COMMANDS_CMD_CREATE = "create";
		COMMANDS_CMD_DELETE = "delete";
		COMMANDS_CMD_DENY = "deny";
		COMMANDS_CMD_DESC = "desc";
		COMMANDS_CMD_IGNORE = "ignore";
		COMMANDS_CMD_INFO = "info";
		COMMANDS_CMD_INVITE = "invite";
		COMMANDS_CMD_JOIN = "join";
		COMMANDS_CMD_KICK = "kick";
		COMMANDS_CMD_LEAVE = "leave";
		COMMANDS_CMD_LIST = "list";
		COMMANDS_CMD_MIGRATE = "migrate";
		COMMANDS_CMD_MOTD = "motd";
		COMMANDS_CMD_MUTE = "mute";
		COMMANDS_CMD_PASSWORD = "password";
		COMMANDS_CMD_RANK = "rank";
		COMMANDS_CMD_RELOAD = "reload";
		COMMANDS_CMD_RENAME = "rename";
		COMMANDS_CMD_SPY = "spy";
		
		COMMANDS_SUB_ON = "on";
		COMMANDS_SUB_OFF = "off";
		COMMANDS_SUB_SILENT = "silent";
		COMMANDS_SUB_FIXED = "fixed";
		COMMANDS_SUB_REMOVE = "remove";
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		// Parties settings
		PARTIES_UPDATES_CHECK = confAdapter.getBoolean("parties.updates.check", PARTIES_UPDATES_CHECK);
		PARTIES_UPDATES_WARN = confAdapter.getBoolean("parties.updates.warn", PARTIES_UPDATES_WARN);
		PARTIES_JLMESSAGES = confAdapter.getBoolean("parties.join-leave-messages", PARTIES_JLMESSAGES);
		
		
		// Storage settings
		STORAGE_TYPE_LOG = confAdapter.getString("storage.log-storage-type", STORAGE_TYPE_LOG);
		STORAGE_TYPE_DATABASE = confAdapter.getString("storage.database-storage-type", STORAGE_TYPE_DATABASE);
		STORAGE_LOG_FORMAT = confAdapter.getString("storage.log-settings.format", STORAGE_LOG_FORMAT);
		STORAGE_LOG_CHAT = confAdapter.getBoolean("storage.log-settings.chat", STORAGE_LOG_CHAT);
		STORAGE_LOG_PRINTCONSOLE = confAdapter.getBoolean("storage.log-settings.print-console", STORAGE_LOG_PRINTCONSOLE);
		STORAGE_LOG_LEVEL = confAdapter.getInt("storage.log-settings.log-level", STORAGE_LOG_LEVEL);
		STORAGE_MIGRATE_INIT_YAML = confAdapter.getBoolean("storage.migrate-settings.initialize-storage.yaml", STORAGE_MIGRATE_INIT_YAML);
		STORAGE_MIGRATE_INIT_MYSQL = confAdapter.getBoolean("storage.migrate-settings.initialize-storage.mysql", STORAGE_MIGRATE_INIT_MYSQL);
		STORAGE_MIGRATE_INIT_SQLITE = confAdapter.getBoolean("storage.migrate-settings.initialize-storage.sqlite", STORAGE_MIGRATE_INIT_SQLITE);
		STORAGE_MIGRATE_ONLYCONSOLE = confAdapter.getBoolean("storage.migrate-settings.migrate-only-console", STORAGE_MIGRATE_ONLYCONSOLE);
		STORAGE_MIGRATE_SUFFIX = confAdapter.getString("storage.migrate-settings.migration-suffix", STORAGE_MIGRATE_SUFFIX);
		
		STORAGE_SETTINGS_FILE_TXT_LOGNAME = confAdapter.getString("storage.storage-settings.file-based.txt.log-name", STORAGE_SETTINGS_FILE_TXT_LOGNAME);
		STORAGE_SETTINGS_FILE_YAML_DBNAME = confAdapter.getString("storage.storage-settings.file-based.yaml.database-name", STORAGE_SETTINGS_FILE_YAML_DBNAME);
		STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE = confAdapter.getInt("storage.storage-settings.sql-based.sql-based.general-settings.varchar-size", STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE);
		STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_SAVEOLD = confAdapter.getBoolean("storage.storage-settings.sql-based.sql-based.general-settings.upgrade.save-old-table", STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_SAVEOLD);
		STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_OLDSUFFIX = confAdapter.getString("storage.storage-settings.sql-based.sql-based.general-settings.upgrade.old-table-suffix", STORAGE_SETTINGS_SQL_GENERAL_UPGRADE_OLDSUFFIX);
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES = confAdapter.getString("storage.storage-settings.sql-based.general-settings.tables.parties", STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES);
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS = confAdapter.getString("storage.storage-settings.sql-based.general-settings.tables.players", STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS);
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG = confAdapter.getString("storage.storage-settings.sql-based.general-settings.tables.log", STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG);
		STORAGE_SETTINGS_SQL_GENERAL_TABLES_VERSIONS = confAdapter.getString("storage.storage-settings.sql-based.general-settings.tables.versions", STORAGE_SETTINGS_SQL_GENERAL_TABLES_VERSIONS);
		STORAGE_SETTINGS_SQL_MYSQL_ADDRESS = confAdapter.getString("storage.storage-settings.sql-based.mysql.address", STORAGE_SETTINGS_SQL_MYSQL_ADDRESS);
		STORAGE_SETTINGS_SQL_MYSQL_DATABASE = confAdapter.getString("storage.storage-settings.sql-based.mysql.database", STORAGE_SETTINGS_SQL_MYSQL_DATABASE);
		STORAGE_SETTINGS_SQL_MYSQL_USERNAME = confAdapter.getString("storage.storage-settings.sql-based.mysql.username", STORAGE_SETTINGS_SQL_MYSQL_USERNAME);
		STORAGE_SETTINGS_SQL_MYSQL_PASSWORD = confAdapter.getString("storage.storage-settings.sql-based.mysql.password", STORAGE_SETTINGS_SQL_MYSQL_PASSWORD);
		STORAGE_SETTINGS_SQL_MYSQL_POOLSIZE = confAdapter.getInt("storage.storage-settings.sql-based.mysql.pool-size", STORAGE_SETTINGS_SQL_MYSQL_POOLSIZE);
		STORAGE_SETTINGS_SQL_MYSQL_CONNLIFETIME = confAdapter.getInt("storage.storage-settings.sql-based.mysql.connection-lifetime", STORAGE_SETTINGS_SQL_MYSQL_CONNLIFETIME);
		STORAGE_SETTINGS_SQL_MYSQL_USESSL = confAdapter.getBoolean("storage.storage-settings.sql-based.mysql.use-ssl", STORAGE_SETTINGS_SQL_MYSQL_USESSL);
		STORAGE_SETTINGS_SQL_MYSQL_CHARSET = confAdapter.getString("storage.storage-settings.sql-based.mysql.charset", STORAGE_SETTINGS_SQL_MYSQL_CHARSET);
		STORAGE_SETTINGS_SQL_SQLITE_DBNAME = confAdapter.getString("storage.storage-settings.sql-based.sqlite.database-name", STORAGE_SETTINGS_SQL_SQLITE_DBNAME);
		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT = confAdapter.getBoolean("storage.storage-settings.none.disband-on-leader-left", STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT);
		STORAGE_SETTINGS_NONE_DELAYDELETEPARTY = confAdapter.getInt("storage.storage-settings.none.delay-delete-party", STORAGE_SETTINGS_NONE_DELAYDELETEPARTY);
		
		
		// Additional settings
		ADDITIONAL_AUTOCMD_ENABLE = confAdapter.getBoolean("additional.auto-command.enable", ADDITIONAL_AUTOCMD_ENABLE);
		ADDITIONAL_AUTOCMD_BLACKLIST = confAdapter.getStringList("additional.auto-command.blacklist", ADDITIONAL_AUTOCMD_BLACKLIST);
		ADDITIONAL_AUTOCMD_WHITELIST = confAdapter.getStringList("additional.auto-command.whitelist", ADDITIONAL_AUTOCMD_WHITELIST);
		
		ADDITIONAL_CENSOR_ENABLE = confAdapter.getBoolean("additional.censor-system.enable", ADDITIONAL_CENSOR_ENABLE);
		ADDITIONAL_CENSOR_CASESENSITIVE = confAdapter.getBoolean("additional.censor-system.case-sensitive", ADDITIONAL_CENSOR_CASESENSITIVE);
		ADDITIONAL_CENSOR_CONTAINS = confAdapter.getStringList("additional.censor-system.contains", ADDITIONAL_CENSOR_CONTAINS);
		ADDITIONAL_CENSOR_STARTSWITH = confAdapter.getStringList("additional.censor-system.starts-with", ADDITIONAL_CENSOR_STARTSWITH);
		ADDITIONAL_CENSOR_ENDSWITH = confAdapter.getStringList("additional.censor-system.ends-with", ADDITIONAL_CENSOR_ENDSWITH);
		
		ADDITIONAL_MUTE_ENABLE = confAdapter.getBoolean("additional.mute.enable", ADDITIONAL_MUTE_ENABLE);
		ADDITIONAL_MUTE_BLOCK_INVITE = confAdapter.getBoolean("additional.mute.block.INVITE", ADDITIONAL_MUTE_BLOCK_INVITE);
		
		ADDITIONAL_PLACEHOLDER_COLOR_NAME = confAdapter.getString("additional.placeholders.color-name", ADDITIONAL_PLACEHOLDER_COLOR_NAME);
		ADDITIONAL_PLACEHOLDER_COLOR_CODE = confAdapter.getString("additional.placeholders.color-code", ADDITIONAL_PLACEHOLDER_COLOR_CODE);
		ADDITIONAL_PLACEHOLDER_COLOR_COMMAND = confAdapter.getString("additional.placeholders.color-command", ADDITIONAL_PLACEHOLDER_COLOR_COMMAND);
		ADDITIONAL_PLACEHOLDER_DESC = confAdapter.getString("additional.placeholders.desc", ADDITIONAL_PLACEHOLDER_DESC);
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_TOTAL = confAdapter.getString("additional.placeholders.experience-total", ADDITIONAL_PLACEHOLDER_EXPERIENCE_TOTAL);
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVEL = confAdapter.getString("additional.placeholders.experience-level", ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVEL);
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_CURRENT = confAdapter.getString("additional.placeholders.experience-levelup-current", ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_CURRENT);
		ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_NECESSARY = confAdapter.getString("additional.placeholders.experience-levelup-necessary", ADDITIONAL_PLACEHOLDER_EXPERIENCE_LEVELUP_NECESSARY);
		ADDITIONAL_PLACEHOLDER_KILLS = confAdapter.getString("additional.placeholders.kills", ADDITIONAL_PLACEHOLDER_KILLS);
		ADDITIONAL_PLACEHOLDER_MOTD = confAdapter.getString("additional.placeholders.motd", ADDITIONAL_PLACEHOLDER_MOTD);
		ADDITIONAL_PLACEHOLDER_PARTY = confAdapter.getString("additional.placeholders.party", ADDITIONAL_PLACEHOLDER_PARTY);
		ADDITIONAL_PLACEHOLDER_RANK_NAME = confAdapter.getString("additional.placeholders.rank-name", ADDITIONAL_PLACEHOLDER_RANK_NAME);
		ADDITIONAL_PLACEHOLDER_RANK_CHAT = confAdapter.getString("additional.placeholders. rank-chat", ADDITIONAL_PLACEHOLDER_RANK_CHAT);
		
		
		// Commands settings
		COMMANDS_HELP_PERPAGE = confAdapter.getInt("commands.help-pages.commands-per-page", COMMANDS_HELP_PERPAGE);
		
		COMMANDS_CMD_HELP = confAdapter.getString("commands.main-commands.help", COMMANDS_CMD_HELP);
		COMMANDS_CMD_PARTY = confAdapter.getString("commands.main-commands.party", COMMANDS_CMD_PARTY);
		COMMANDS_CMD_P = confAdapter.getString("commands.main-commands.p", COMMANDS_CMD_P);
		COMMANDS_CMD_ACCEPT = confAdapter.getString("commands.main-commands.accept", COMMANDS_CMD_ACCEPT);
		COMMANDS_CMD_CHAT = confAdapter.getString("commands.main-commands.chat", COMMANDS_CMD_CHAT);
		COMMANDS_CMD_COLOR = confAdapter.getString("commands.main-commands.color", COMMANDS_CMD_COLOR);
		COMMANDS_CMD_CREATE = confAdapter.getString("commands.main-commands.create", COMMANDS_CMD_CREATE);
		COMMANDS_CMD_DELETE = confAdapter.getString("commands.main-commands.delete", COMMANDS_CMD_DELETE);
		COMMANDS_CMD_DENY = confAdapter.getString("commands.main-commands.deny", COMMANDS_CMD_DENY);
		COMMANDS_CMD_DESC = confAdapter.getString("commands.main-commands.desc", COMMANDS_CMD_DESC);
		COMMANDS_CMD_IGNORE = confAdapter.getString("commands.main-commands.ignore", COMMANDS_CMD_IGNORE);
		COMMANDS_CMD_INFO = confAdapter.getString("commands.main-commands.info", COMMANDS_CMD_INFO);
		COMMANDS_CMD_INVITE = confAdapter.getString("commands.main-commands.invite", COMMANDS_CMD_INVITE);
		COMMANDS_CMD_JOIN = confAdapter.getString("commands.main-commands.join", COMMANDS_CMD_JOIN);
		COMMANDS_CMD_KICK = confAdapter.getString("commands.main-commands.kick", COMMANDS_CMD_KICK);
		COMMANDS_CMD_LEAVE = confAdapter.getString("commands.main-commands.leave", COMMANDS_CMD_LEAVE);
		COMMANDS_CMD_LIST = confAdapter.getString("commands.main-commands.list", COMMANDS_CMD_LIST);
		COMMANDS_CMD_MIGRATE = confAdapter.getString("commands.main-commands.migrate", COMMANDS_CMD_MIGRATE);
		COMMANDS_CMD_MOTD = confAdapter.getString("commands.main-commands.motd", COMMANDS_CMD_MOTD);
		COMMANDS_CMD_MUTE = confAdapter.getString("commands.main-commands.mute", COMMANDS_CMD_MUTE);
		COMMANDS_CMD_PASSWORD = confAdapter.getString("commands.main-commands.password", COMMANDS_CMD_PASSWORD);
		COMMANDS_CMD_RANK = confAdapter.getString("commands.main-commands.rank", COMMANDS_CMD_RANK);
		COMMANDS_CMD_RELOAD = confAdapter.getString("commands.main-commands.reload", COMMANDS_CMD_RELOAD);
		COMMANDS_CMD_RENAME = confAdapter.getString("commands.main-commands.rename", COMMANDS_CMD_RENAME);
		COMMANDS_CMD_SPY = confAdapter.getString("commands.main-commands.spy", COMMANDS_CMD_SPY);
		
		COMMANDS_SUB_ON = confAdapter.getString("commands.sub-commands.on", COMMANDS_SUB_ON);
		COMMANDS_SUB_OFF = confAdapter.getString("commands.sub-commands.off", COMMANDS_SUB_OFF);
		COMMANDS_SUB_SILENT = confAdapter.getString("commands.sub-commands.silent", COMMANDS_SUB_SILENT);
		COMMANDS_SUB_FIXED = confAdapter.getString("commands.sub-commands.fixed", COMMANDS_SUB_FIXED);
		COMMANDS_SUB_REMOVE = confAdapter.getString("commands.sub-commands.remove", COMMANDS_SUB_REMOVE);
		
		COMMANDS_ORDER = confAdapter.getStringList("commands.order", COMMANDS_ORDER);
	}
}
