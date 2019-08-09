package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationSectionAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationFile;
import com.alessiodp.parties.common.configuration.PartiesConstants;

import java.util.HashMap;
import java.util.List;


public abstract class ConfigMain extends PartiesConfigurationFile {
	// Parties settings
	public static boolean		PARTIES_UPDATES_CHECK;
	public static boolean		PARTIES_UPDATES_WARN;
	public static boolean		PARTIES_LOGGING_DEBUG;
	public static boolean		PARTIES_LOGGING_SAVE_ENABLE;
	public static String		PARTIES_LOGGING_SAVE_FORMAT;
	public static String		PARTIES_LOGGING_SAVE_FILE;
	public static boolean		PARTIES_JOINLEAVEMESSAGES;
	
	
	// Storage settings
	public static String		STORAGE_TYPE_DATABASE;
	public static boolean		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT;
	public static int			STORAGE_SETTINGS_NONE_DELAYDELETEPARTY;
	public static String		STORAGE_SETTINGS_YAML_DBFILE;
	public static int			STORAGE_SETTINGS_SQLGENERAL_VARCHARSIZE;
	public static boolean		STORAGE_SETTINGS_SQLGENERAL_UPGRADE_SAVEOLD;
	public static String		STORAGE_SETTINGS_SQLGENERAL_UPGRADE_OLDSUFFIX;
	public static String		STORAGE_SETTINGS_SQLGENERAL_TABLES_PARTIES;
	public static String		STORAGE_SETTINGS_SQLGENERAL_TABLES_PLAYERS;
	public static String		STORAGE_SETTINGS_SQLGENERAL_TABLES_VERSIONS;
	public static String		STORAGE_SETTINGS_SQLITE_DBFILE;
	public static String		STORAGE_SETTINGS_MYSQL_ADDRESS;
	public static String		STORAGE_SETTINGS_MYSQL_PORT;
	public static String		STORAGE_SETTINGS_MYSQL_DATABASE;
	public static String		STORAGE_SETTINGS_MYSQL_USERNAME;
	public static String		STORAGE_SETTINGS_MYSQL_PASSWORD;
	public static int			STORAGE_SETTINGS_MYSQL_POOLSIZE;
	public static int			STORAGE_SETTINGS_MYSQL_CONNLIFETIME;
	public static boolean		STORAGE_SETTINGS_MYSQL_USESSL;
	public static String		STORAGE_SETTINGS_MYSQL_CHARSET;
	
	
	// Additional settings
	public static boolean		ADDITIONAL_AUTOCMD_ENABLE;
	public static String		ADDITIONAL_AUTOCMD_REGEXWHITELIST;
	
	public static boolean		ADDITIONAL_FOLLOW_ENABLE;
	public static boolean		ADDITIONAL_FOLLOW_TOGGLECMD;
	
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
	public static String		ADDITIONAL_PLACEHOLDER_OUTPARTY;
	public static String		ADDITIONAL_PLACEHOLDER_PARTY;
	public static String		ADDITIONAL_PLACEHOLDER_RANK_NAME;
	public static String		ADDITIONAL_PLACEHOLDER_RANK_CHAT;
	public static HashMap<String, String> ADDITIONAL_PLACEHOLDER_CUSTOMS;
	
	
	// Commands settings
	public static boolean		COMMANDS_TABSUPPORT;
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
	public static String		COMMANDS_CMD_FOLLOW;
	public static String		COMMANDS_CMD_IGNORE;
	public static String		COMMANDS_CMD_INFO;
	public static String		COMMANDS_CMD_INVITE;
	public static String		COMMANDS_CMD_JOIN;
	public static String		COMMANDS_CMD_KICK;
	public static String		COMMANDS_CMD_LEAVE;
	public static String		COMMANDS_CMD_LIST;
	public static String		COMMANDS_CMD_MOTD;
	public static String		COMMANDS_CMD_MUTE;
	public static String		COMMANDS_CMD_PASSWORD;
	public static String		COMMANDS_CMD_RANK;
	public static String		COMMANDS_CMD_RELOAD;
	public static String		COMMANDS_CMD_RENAME;
	public static String		COMMANDS_CMD_SPY;
	public static String		COMMANDS_CMD_TELEPORT;
	public static String		COMMANDS_CMD_VERSION;
	
	public static String		COMMANDS_SUB_ON;
	public static String		COMMANDS_SUB_OFF;
	public static String		COMMANDS_SUB_SILENT;
	public static String		COMMANDS_SUB_FIXED;
	public static String		COMMANDS_SUB_REMOVE;
	
	public static List<String>	COMMANDS_ORDER;
	
	
	protected ConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		// Parties settings
		PARTIES_UPDATES_CHECK = true;
		PARTIES_UPDATES_WARN = true;
		PARTIES_LOGGING_DEBUG = false;
		PARTIES_LOGGING_SAVE_ENABLE = false;
		PARTIES_LOGGING_SAVE_FORMAT = "%date% [%time%] %message%\n";
		PARTIES_LOGGING_SAVE_FILE = "log.txt";
		PARTIES_JOINLEAVEMESSAGES = false;
		
		
		// Storage settings
		STORAGE_TYPE_DATABASE = "yaml";
		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT = true;
		STORAGE_SETTINGS_NONE_DELAYDELETEPARTY = 600;
		STORAGE_SETTINGS_YAML_DBFILE = "database.yml";
		STORAGE_SETTINGS_SQLGENERAL_VARCHARSIZE = 255;
		STORAGE_SETTINGS_SQLGENERAL_UPGRADE_SAVEOLD = true;
		STORAGE_SETTINGS_SQLGENERAL_UPGRADE_OLDSUFFIX = "_backup";
		STORAGE_SETTINGS_SQLGENERAL_TABLES_PARTIES = "parties_parties";
		STORAGE_SETTINGS_SQLGENERAL_TABLES_PLAYERS = "parties_players";
		STORAGE_SETTINGS_SQLGENERAL_TABLES_VERSIONS = "parties_versions";
		STORAGE_SETTINGS_SQLITE_DBFILE = "database.db";
		STORAGE_SETTINGS_MYSQL_ADDRESS = "localhost";
		STORAGE_SETTINGS_MYSQL_PORT = "3306";
		STORAGE_SETTINGS_MYSQL_DATABASE = "database";
		STORAGE_SETTINGS_MYSQL_USERNAME = "username";
		STORAGE_SETTINGS_MYSQL_PASSWORD = "password";
		STORAGE_SETTINGS_MYSQL_POOLSIZE = 10;
		STORAGE_SETTINGS_MYSQL_CONNLIFETIME = 1800000;
		STORAGE_SETTINGS_MYSQL_USESSL = false;
		STORAGE_SETTINGS_MYSQL_CHARSET = "utf8";
		
		
		// Additional features
		ADDITIONAL_AUTOCMD_ENABLE = false;
		ADDITIONAL_AUTOCMD_REGEXWHITELIST = "^(?!(\\/party|\\/pay|\\/money)).*";
		
		ADDITIONAL_FOLLOW_ENABLE = false;
		ADDITIONAL_FOLLOW_TOGGLECMD = false;
		
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
		ADDITIONAL_PLACEHOLDER_OUTPARTY = "%out_party%";
		ADDITIONAL_PLACEHOLDER_PARTY = "%party%";
		ADDITIONAL_PLACEHOLDER_RANK_NAME = "%rank_name%";
		ADDITIONAL_PLACEHOLDER_RANK_CHAT = "%rank_chat%";
		ADDITIONAL_PLACEHOLDER_CUSTOMS = new HashMap<>();
		ADDITIONAL_PLACEHOLDER_CUSTOMS.put("example1", "[%color_code%%party%] ");
		ADDITIONAL_PLACEHOLDER_CUSTOMS.put("example2", "[%rank_chat% %party%] ");
		
		
		// Commands settings
		COMMANDS_TABSUPPORT = true;
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
		COMMANDS_CMD_FOLLOW = "follow";
		COMMANDS_CMD_IGNORE = "ignore";
		COMMANDS_CMD_INFO = "info";
		COMMANDS_CMD_INVITE = "invite";
		COMMANDS_CMD_JOIN = "join";
		COMMANDS_CMD_KICK = "kick";
		COMMANDS_CMD_LEAVE = "leave";
		COMMANDS_CMD_LIST = "list";
		COMMANDS_CMD_MOTD = "motd";
		COMMANDS_CMD_MUTE = "mute";
		COMMANDS_CMD_PASSWORD = "password";
		COMMANDS_CMD_RANK = "rank";
		COMMANDS_CMD_RELOAD = "reload";
		COMMANDS_CMD_RENAME = "rename";
		COMMANDS_CMD_SPY = "spy";
		COMMANDS_CMD_TELEPORT = "teleport";
		COMMANDS_CMD_VERSION = "version";
		
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
		PARTIES_LOGGING_DEBUG = confAdapter.getBoolean("parties.logging.debug", PARTIES_LOGGING_DEBUG);
		PARTIES_LOGGING_SAVE_ENABLE = confAdapter.getBoolean("parties.logging.save-file.enable", PARTIES_LOGGING_SAVE_ENABLE);
		PARTIES_LOGGING_SAVE_FORMAT = confAdapter.getString("parties.logging.save-file.format", PARTIES_LOGGING_SAVE_FORMAT);
		PARTIES_LOGGING_SAVE_FILE = confAdapter.getString("parties.logging.save-file.file", PARTIES_LOGGING_SAVE_FILE);
		PARTIES_JOINLEAVEMESSAGES = confAdapter.getBoolean("parties.join-leave-messages", PARTIES_JOINLEAVEMESSAGES);
		
		
		// Storage settings
		STORAGE_TYPE_DATABASE = confAdapter.getString("storage.database-storage-type", STORAGE_TYPE_DATABASE);
		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT = confAdapter.getBoolean("storage.storage-settings.none.disband-on-leader-left", STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT);
		STORAGE_SETTINGS_NONE_DELAYDELETEPARTY = confAdapter.getInt("storage.storage-settings.none.delay-delete-party", STORAGE_SETTINGS_NONE_DELAYDELETEPARTY);
		STORAGE_SETTINGS_YAML_DBFILE = confAdapter.getString("storage.storage-settings.yaml.database-file", STORAGE_SETTINGS_YAML_DBFILE);
		STORAGE_SETTINGS_SQLGENERAL_VARCHARSIZE = confAdapter.getInt("storage.storage-settings.general-sql-settings.varchar-size", STORAGE_SETTINGS_SQLGENERAL_VARCHARSIZE);
		STORAGE_SETTINGS_SQLGENERAL_UPGRADE_SAVEOLD = confAdapter.getBoolean("storage.storage-settings.general-sql-settings.upgrade.save-old-table", STORAGE_SETTINGS_SQLGENERAL_UPGRADE_SAVEOLD);
		STORAGE_SETTINGS_SQLGENERAL_UPGRADE_OLDSUFFIX = confAdapter.getString("storage.storage-settings.general-sql-settings.upgrade.old-table-suffix", STORAGE_SETTINGS_SQLGENERAL_UPGRADE_OLDSUFFIX);
		STORAGE_SETTINGS_SQLGENERAL_TABLES_PARTIES = confAdapter.getString("storage.storage-settings.general-sql-settings.tables.parties", STORAGE_SETTINGS_SQLGENERAL_TABLES_PARTIES);
		STORAGE_SETTINGS_SQLGENERAL_TABLES_PLAYERS = confAdapter.getString("storage.storage-settings.general-sql-settings.tables.players", STORAGE_SETTINGS_SQLGENERAL_TABLES_PLAYERS);
		STORAGE_SETTINGS_SQLGENERAL_TABLES_VERSIONS = confAdapter.getString("storage.storage-settings.general-sql-settings.tables.versions", STORAGE_SETTINGS_SQLGENERAL_TABLES_VERSIONS);
		STORAGE_SETTINGS_SQLITE_DBFILE = confAdapter.getString("storage.storage-settings.sqlite.database-file", STORAGE_SETTINGS_SQLITE_DBFILE);
		STORAGE_SETTINGS_MYSQL_ADDRESS = confAdapter.getString("storage.storage-settings.mysql.address", STORAGE_SETTINGS_MYSQL_ADDRESS);
		STORAGE_SETTINGS_MYSQL_PORT = confAdapter.getString("storage.storage-settings.mysql.port", STORAGE_SETTINGS_MYSQL_PORT);
		STORAGE_SETTINGS_MYSQL_DATABASE = confAdapter.getString("storage.storage-settings.mysql.database", STORAGE_SETTINGS_MYSQL_DATABASE);
		STORAGE_SETTINGS_MYSQL_USERNAME = confAdapter.getString("storage.storage-settings.mysql.username", STORAGE_SETTINGS_MYSQL_USERNAME);
		STORAGE_SETTINGS_MYSQL_PASSWORD = confAdapter.getString("storage.storage-settings.mysql.password", STORAGE_SETTINGS_MYSQL_PASSWORD);
		STORAGE_SETTINGS_MYSQL_POOLSIZE = confAdapter.getInt("storage.storage-settings.mysql.pool-size", STORAGE_SETTINGS_MYSQL_POOLSIZE);
		STORAGE_SETTINGS_MYSQL_CONNLIFETIME = confAdapter.getInt("storage.storage-settings.mysql.connection-lifetime", STORAGE_SETTINGS_MYSQL_CONNLIFETIME);
		STORAGE_SETTINGS_MYSQL_USESSL = confAdapter.getBoolean("storage.storage-settings.mysql.use-ssl", STORAGE_SETTINGS_MYSQL_USESSL);
		STORAGE_SETTINGS_MYSQL_CHARSET = confAdapter.getString("storage.storage-settings.mysql.charset", STORAGE_SETTINGS_MYSQL_CHARSET);
		
		
		// Additional settings
		ADDITIONAL_AUTOCMD_ENABLE = confAdapter.getBoolean("additional.auto-command.enable", ADDITIONAL_AUTOCMD_ENABLE);
		ADDITIONAL_AUTOCMD_REGEXWHITELIST = confAdapter.getString("additional.auto-command.regex-whitelist", ADDITIONAL_AUTOCMD_REGEXWHITELIST);
		
		ADDITIONAL_FOLLOW_ENABLE = confAdapter.getBoolean("additional.follow-party.enable", ADDITIONAL_FOLLOW_ENABLE);
		ADDITIONAL_FOLLOW_TOGGLECMD = confAdapter.getBoolean("additional.follow-party.toggle-command", ADDITIONAL_FOLLOW_TOGGLECMD);
		
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
		ADDITIONAL_PLACEHOLDER_OUTPARTY = confAdapter.getString("additional.placeholders.out-party", ADDITIONAL_PLACEHOLDER_OUTPARTY);
		ADDITIONAL_PLACEHOLDER_PARTY = confAdapter.getString("additional.placeholders.party", ADDITIONAL_PLACEHOLDER_PARTY);
		ADDITIONAL_PLACEHOLDER_RANK_NAME = confAdapter.getString("additional.placeholders.rank-name", ADDITIONAL_PLACEHOLDER_RANK_NAME);
		ADDITIONAL_PLACEHOLDER_RANK_CHAT = confAdapter.getString("additional.placeholders. rank-chat", ADDITIONAL_PLACEHOLDER_RANK_CHAT);
		handlePlaceholders(confAdapter);
		
		
		// Commands settings
		COMMANDS_TABSUPPORT = confAdapter.getBoolean("commands.tab-support", COMMANDS_TABSUPPORT);
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
		COMMANDS_CMD_FOLLOW = confAdapter.getString("commands.main-commands.follow", COMMANDS_CMD_FOLLOW);
		COMMANDS_CMD_IGNORE = confAdapter.getString("commands.main-commands.ignore", COMMANDS_CMD_IGNORE);
		COMMANDS_CMD_INFO = confAdapter.getString("commands.main-commands.info", COMMANDS_CMD_INFO);
		COMMANDS_CMD_INVITE = confAdapter.getString("commands.main-commands.invite", COMMANDS_CMD_INVITE);
		COMMANDS_CMD_JOIN = confAdapter.getString("commands.main-commands.join", COMMANDS_CMD_JOIN);
		COMMANDS_CMD_KICK = confAdapter.getString("commands.main-commands.kick", COMMANDS_CMD_KICK);
		COMMANDS_CMD_LEAVE = confAdapter.getString("commands.main-commands.leave", COMMANDS_CMD_LEAVE);
		COMMANDS_CMD_LIST = confAdapter.getString("commands.main-commands.list", COMMANDS_CMD_LIST);
		COMMANDS_CMD_MOTD = confAdapter.getString("commands.main-commands.motd", COMMANDS_CMD_MOTD);
		COMMANDS_CMD_MUTE = confAdapter.getString("commands.main-commands.mute", COMMANDS_CMD_MUTE);
		COMMANDS_CMD_PASSWORD = confAdapter.getString("commands.main-commands.password", COMMANDS_CMD_PASSWORD);
		COMMANDS_CMD_RANK = confAdapter.getString("commands.main-commands.rank", COMMANDS_CMD_RANK);
		COMMANDS_CMD_RELOAD = confAdapter.getString("commands.main-commands.reload", COMMANDS_CMD_RELOAD);
		COMMANDS_CMD_RENAME = confAdapter.getString("commands.main-commands.rename", COMMANDS_CMD_RENAME);
		COMMANDS_CMD_SPY = confAdapter.getString("commands.main-commands.spy", COMMANDS_CMD_SPY);
		COMMANDS_CMD_TELEPORT = confAdapter.getString("commands.main-commands.teleport", COMMANDS_CMD_TELEPORT);
		COMMANDS_CMD_VERSION = confAdapter.getString("commands.main-commands.version", COMMANDS_CMD_VERSION);
		
		COMMANDS_SUB_ON = confAdapter.getString("commands.sub-commands.on", COMMANDS_SUB_ON);
		COMMANDS_SUB_OFF = confAdapter.getString("commands.sub-commands.off", COMMANDS_SUB_OFF);
		COMMANDS_SUB_SILENT = confAdapter.getString("commands.sub-commands.silent", COMMANDS_SUB_SILENT);
		COMMANDS_SUB_FIXED = confAdapter.getString("commands.sub-commands.fixed", COMMANDS_SUB_FIXED);
		COMMANDS_SUB_REMOVE = confAdapter.getString("commands.sub-commands.remove", COMMANDS_SUB_REMOVE);
		
		COMMANDS_ORDER = confAdapter.getStringList("commands.order", COMMANDS_ORDER);
	}
	
	private void handlePlaceholders(ConfigurationAdapter confAdapter) {
		HashMap<String, String> customs = new HashMap<>();
		
		ConfigurationSectionAdapter csPlaceholders = confAdapter.getConfigurationSection("additional.placeholders.customs");
		if (csPlaceholders != null) {
			for (String key : csPlaceholders.getKeys()) {
				customs.put(key, csPlaceholders.getString(key, ""));
			}
			ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS = customs;
		} else {
			// Give error: no ranks node found
			plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_PLACEHOLDERS_NOTFOUND);
		}
	}
}
