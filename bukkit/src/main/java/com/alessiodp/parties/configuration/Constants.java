package com.alessiodp.parties.configuration;

public class Constants {
	
	public static final boolean DEBUG_ENABLED = false;
	public static final boolean DEBUG_TIMESTAMPS = false;
	
	public static final int VERSION_CONFIG_MAIN = 17;
	public static final int VERSION_CONFIG_PARTIES = 1;
	public static final int VERSION_MESSAGES = 15;
	public static final int VERSION_DATABASE_YAML = 1;
	public static final int VERSION_DATABASE_MYSQL = 3;
	public static final int VERSION_DATABASE_SQLITE = 1;
	
	public static final String SCOREBOARD_PREFIX = "PARTY";
	public static final int CURSE_PROJECT_ID = 90889;
	
	public static final String FIXED_VALUE_TEXT = "fixed";
	public static final String FIXED_VALUE_UUID = "00000000-0000-0000-0000-000000000000";
	
	public static final String DATABASE_SCHEMA_DIVIDER = "\\/\\*START\\_([a-z]+)\\*\\/([^\\/\\*]*)\\/\\*END\\_\\1\\*\\/";
	public static final String DATABASE_MYSQL_SCHEMA = "mysql.sql";
	public static final String DATABASE_SQLITE_SCHEMA = "sqlite.sql";
	
	public static final String LIBRARY_FOLDER = "libs/";
	
	public static final String ONLY_PLAYERS = "You must be a player to use this command.";
	
	/*
	 * Updater
	 */
	public static final String UPDATER_FOUND = "Parties v{currentVersion} found a new version: {newVersion}";
	public static final String UPDATER_FAILED_IO = "Parties could not contact alessiodp.com for updating.";
	public static final String UPDATER_FAILED_GENERAL = "Parties could not check for updates.";
	public static final String UPDATER_FALLBACK = "Parties will use Gravity Updater to check for updates.";
	public static final String UPDATER_URL = "https://api.alessiodp.com/version.php?plugin=parties&version={version}";
	public static final String UPDATER_FIELD_VERSION = "version";
	public static final String UPDATER_DELIMITER_TYPE = "\\-";
	public static final String UPDATER_DELIMITER_VERSION = "\\.";
	
	/*
	 * Placeholders
	 */
	public static final String PLACEHOLDER_PLAYER_PLAYER = "%player%";
	public static final String PLACEHOLDER_PLAYER_RANK_NAME = "%rank_name%";
	public static final String PLACEHOLDER_PLAYER_RANK_CHAT = "%rank_chat%";
	public static final String PLACEHOLDER_PLAYER_SENDER = "%sender%";
	public static final String PLACEHOLDER_PLAYER_VICTIM = "%victim%";
	
	public static final String PLACEHOLDER_PARTY_COLOR_CODE = "%color_code%";
	public static final String PLACEHOLDER_PARTY_COLOR_COMMAND = "%color_command%";
	public static final String PLACEHOLDER_PARTY_COLOR_NAME = "%color_name%";
	public static final String PLACEHOLDER_PARTY_DESC = "%desc%";
	public static final String PLACEHOLDER_PARTY_KILLS = "%kills%";
	public static final String PLACEHOLDER_PARTY_MOTD = "%motd%";
	public static final String PLACEHOLDER_PARTY_ONLINENUMBER = "%onlinenumber%";
	public static final String PLACEHOLDER_PARTY_PARTY = "%party%";
	public static final String PLACEHOLDER_PARTY_PREFIX = "%prefix%";
	public static final String PLACEHOLDER_PARTY_SUFFIX = "%suffix%";
	
	
	/*
	 * Log
	 */
	public static final String CLASS_INIT = "Initializing {class}";
	
	public static final String DEBUG_PARTIES_ENABLING = "Initializing Parties {version}";
	public static final String DEBUG_PARTIES_ENABLED = "Parties v{version} enabled";
	public static final String DEBUG_PARTIES_DISABLING = "Disabling Parties";
	public static final String DEBUG_PARTIES_DISABLED = "Parties disabled";
	public static final String DEBUG_PARTIES_DISABLED_LOG = "========== Parties disabled - End of Log ==========";
	
	public static final String DEBUG_PLAYER_JOIN = "{player} entered in the game, party '{party}'";
	public static final String DEBUG_PLAYER_JOIN_DEFAULTJOIN = "{player} joined without party, default join into {party}";
	public static final String DEBUG_PLAYER_JOIN_DEFAULTFAIL = "Failed to load default party {party}";
	public static final String DEBUG_PLAYER_INIT = "Initialized player {player} ({uuid})";
	public static final String DEBUG_PLAYER_UPDATED = "Updated player {player}";
	public static final String DEBUG_PLAYER_CLEANUP = "Cleaned up player {player}";
	public static final String DEBUG_PLAYER_COMPARENAME_CHANGE = "Player name changed from '{old}' to '{new}' ({uuid})";
	public static final String DEBUG_PLAYER_COMPARENAME_NOTFOUND = "Player name of {uuid} not found into the server, getting one from database: '{name}'";
	public static final String DEBUG_PLAYER_GET_DATABASE = "Got player {player} from database (party: {party})";
	public static final String DEBUG_PLAYER_GET_LIST = "Got player {player} from list (party: {party})";
	
	public static final String DEBUG_PARTY_INIT = "Initialized party {party}";
	public static final String DEBUG_PARTY_INIT_COPY = "Initialized a copy of party {party}";
	public static final String DEBUG_PARTY_UPDATED = "Updated party {party}";
	public static final String DEBUG_PARTY_REMOVED = "Removed party {party}";
	public static final String DEBUG_PARTY_FIXED_LOAD = "Loading fixed party {party}";
	public static final String DEBUG_PARTY_GET_DATABASE = "Got party {party} from database";
	public static final String DEBUG_PARTY_GET_LIST = "Got party {party} from list";
	public static final String DEBUG_PARTY_DELETE_CAUSE = "Deleted party {party} due {cause}";
	
	public static final String DEBUG_COMMANDS_REGISTER_PRE = "Registering commands";
	public static final String DEBUG_COMMANDS_REGISTER_POST = "Registered {value} commands";
	public static final String DEBUG_CMD_ACCEPT = "{player} accepted invite for {party}";
	public static final String DEBUG_CMD_CHAT = "{player} changed party chat to {value}";
	public static final String DEBUG_CMD_CLAIM = "{player} claimed with {value}";
	public static final String DEBUG_CMD_COLOR = "{player} set color of the party {party} to {value}";
	public static final String DEBUG_CMD_COLOR_REM = "{player} removed color of the party {party}";
	public static final String DEBUG_CMD_CREATE = "{player} created the party {party}";
	public static final String DEBUG_CMD_CREATE_FIXED = "{player} created the fixed party {party}";
	public static final String DEBUG_CMD_DELETE = "{player} deleted the party {party}";
	public static final String DEBUG_CMD_DENY = "{player} deleted the party {party}";
	public static final String DEBUG_CMD_DESC = "{player} changed the description of {party}";
	public static final String DEBUG_CMD_DESC_REM = "{player} removed the description of {party}";
	public static final String DEBUG_CMD_HOME = "{player} used command home, for {party}";
	public static final String DEBUG_CMD_IGNORE_START = "{player} started to ignore the party {party}";
	public static final String DEBUG_CMD_IGNORE_STOP = "{player} stopped to ignore the party {party}";
	public static final String DEBUG_CMD_INFO = "{player} used the command info, for {party}";
	public static final String DEBUG_CMD_INVITE = "{player} invited {victim} into {party}";
	public static final String DEBUG_CMD_JOIN = "{player} joined into {party}";
	public static final String DEBUG_CMD_KICK = "{player} got kicked out from {party} by {sender} [other={other}]";
	public static final String DEBUG_CMD_KICK_DISBAND = "{player} got kicked out (disband) from {party} by {sender} [other={other}]";
	public static final String DEBUG_CMD_LEAVE = "{player} leaved the party {party}";
	public static final String DEBUG_CMD_LEAVE_DISBAND = "{player} leaved (disbanding) the party {party}";
	public static final String DEBUG_CMD_LIST = "{player} used the command list";
	public static final String DEBUG_CMD_MOTD = "{player} changed the motd of {party}";
	public static final String DEBUG_CMD_MOTD_REM = "{player} removed the motd of {party}";
	public static final String DEBUG_CMD_NOTIFY_ON = "{player} has enabled notify";
	public static final String DEBUG_CMD_NOTIFY_OFF = "{player} has disabled notify";
	public static final String DEBUG_CMD_P = "Chat of {party} by {player}: {message}";
	public static final String DEBUG_CMD_P_TASK = "Started ChatTask for {value} by {player}";
	public static final String DEBUG_CMD_PASSWORD = "{player} changed the password of {party}";
	public static final String DEBUG_CMD_PASSWORD_REM = "{player} removed the password of {party}";
	public static final String DEBUG_CMD_PREFIX = "{player} changed the prefix of {party}";
	public static final String DEBUG_CMD_PREFIX_REM = "{player} removed the prefix of {party}";
	public static final String DEBUG_CMD_TELEPORT = "{player} used command teleport";
	public static final String DEBUG_CMD_RANK = "{player} rank changed from {value1} to {value2} by {sender}";
	public static final String DEBUG_CMD_RELOAD = "Configuration reloaded by {player}";
	public static final String DEBUG_CMD_RELOAD_CONSOLE = "Configuration reloaded";
	public static final String DEBUG_CMD_RENAME = "{player} renamed the party {value} to {party}";
	public static final String DEBUG_CMD_SETHOME = "{player} changed the home of {party}";
	public static final String DEBUG_CMD_SETHOME_REM = "{player} removed the home of {party}";
	public static final String DEBUG_CMD_SPY_ENABLE = "{player} now is a spy";
	public static final String DEBUG_CMD_SPY_DISABLE = "{player} isn't a spy anymore";
	public static final String DEBUG_CMD_SUFFIX = "{player} changed the suffix of {party}";
	public static final String DEBUG_CMD_SUFFIX_REM = "{player} removed the suffix of {party}";
	
	public static final String DEBUG_TASK_CHAT_EXPIRE = "Chat cooldown expired for {uuid}";
	public static final String DEBUG_TASK_DELETE_START = "Started PartyDeleteTask of {party} for {value} seconds";
	public static final String DEBUG_TASK_DELETE_STOP = "Stopped PartyDeleteTask of {party}";
	public static final String DEBUG_TASK_HOME_DENIED_FIGHT = "Denied home by {player} due to fight";
	public static final String DEBUG_TASK_HOME_DENIED_MOVING = "Denied home by {player} due to moving";
	public static final String DEBUG_TASK_INVITE_EXPIRED = "Expired party '{party}' invite for {uuid}";
	public static final String DEBUG_TASK_TELEPORT_START = "Started TeleportTask for {value} by {player}";
	public static final String DEBUG_TASK_TELEPORT_DONE = "{player} teleported to the party home";
	public static final String DEBUG_TASK_TELEPORT_EXPIRED = "Teleport for {player} expired";
	
	public static final String DEBUG_AUTOCMD_PERFORM = "Performing autocommand to {player} with '{command}'";
	
	public static final String DEBUG_EXP_DISTRIBUTE = "Distributing {type} exp of {entity} killed by {player}";
	public static final String DEBUG_EXP_VANILLA_GIVE = "Giving vanilla {exp} exp to {player}";
	public static final String DEBUG_EXP_SKILLAPI_GIVE = "Giving SkillAPI {exp} exp to {player}";
	public static final String DEBUG_EXP_MYTHICMOBS_HANDLING = "Handling a MythicMob entity";
	public static final String DEBUG_EXP_VANILLA_SET = "Vanilla exp giving set to 0";
	public static final String DEBUG_EXP_MYTHICMOBS_SET = "MythicMobs {type} exp giving set to 0";
	
	public static final String DEBUG_FRIENDLYFIRE_DENIED = "Denied friendly fire (type {type}) by '{player}' to '{victim}'";
	
	public static final String DEBUG_KILL_ADD = "Adding a kill to the party {party} by {player}";
	
	public static final String DEBUG_API_CHATEVENT_DENY = "PartiesChatEvent is cancelled, ignoring chat of {player}: {message}";
	public static final String DEBUG_API_CREATEEVENT_DENY = "PartiesCreateEvent is cancelled, ignoring create of {party} by {player}";
	public static final String DEBUG_API_DELETEEVENT_DENY = "PartiesDeleteEvent is cancelled, ignoring delete of {party} by {player}";
	public static final String DEBUG_API_DELETEEVENT_DENY_GENERIC = "PartiesDeleteEvent is cancelled, ignoring delete of {party}";
	public static final String DEBUG_API_FRIENDLYFIREEVENT_DENY = "PartiesFriendlyFireEvent is cancelled, ignoring fight (type {type}) by '{player}' to '{victim}'";
	public static final String DEBUG_API_JOINEVENT_DENY = "PartiesJoinEvent is cancelled, ignoring join of {player} into {party}";
	public static final String DEBUG_API_LEAVEEVENT_DENY = "PartiesLeaveEvent is cancelled, ignoring leave of {player} from {party}";
	public static final String DEBUG_API_RENAMEEVENT_DENY = "PartiesRenameEvent is cancelled, ignoring rename of {party} by {player}";
	
	public static final String DEBUG_BUNGEE_READY = "Ready for Bungeecord";
	public static final String DEBUG_BUNGEE_REPLY = "Parties packet sent back to the channel";
	public static final String DEBUG_BUNGEE_VERSIONMISMATCH = "Skipping Bungeecord Parties packet. Versions don't match ({packet})";
	
	public static final String DEBUG_LIB_INIT_INIT = "Initializing library '{lib}'";
	public static final String DEBUG_LIB_INIT_DL = "Downloading library '{lib}'";
	public static final String DEBUG_LIB_INIT_LOAD = "Loading library '{lib}'";
	public static final String DEBUG_LIB_INIT_FAIL = "Cannot load library '{lib}'";
	public static final String DEBUG_LIB_FAILED_DL = "Failed to download the library '{lib}': {message}";
	public static final String DEBUG_LIB_FAILED_LOAD = "Something gone wrong with library '{lib}' load, report this to the developer!";
	
	public static final String DEBUG_LIB_GENERAL_HOOKED = "{addon} Hooked";
	public static final String DEBUG_LIB_GENERAL_FAILED = "Failed to hook into {addon}, disabled its features";
	public static final String DEBUG_LIB_BANMANAGER_BAN = "Party '{party}' deleted because leader got banned by {player}";
	
	public static final String DEBUG_DB_INIT = "Initializing {class} with {db}/{log}";
	public static final String DEBUG_DB_FORCEINIT = "Forcing initialization of {value} storage";
	public static final String DEBUG_DB_INIT_FAILED_STOP = "Failed to initialize the storage, stopping Parties!";
	public static final String DEBUG_DB_INIT_FAILED_THROW = "Disabling plugin";
	public static final String DEBUG_DB_MIGRATION = "Performing a migration from {from} to {to}";
	
	public static final String DEBUG_FILE_CREATEFAIL = "Failed to create data file: {message}";
	public static final String DEBUG_FILE_ERROR = "Error in {class} at {method}_{line}: {message}";
	
	public static final String DEBUG_SQL_FAILED = "Failed initialization of {type}, error: {message}";
	public static final String DEBUG_SQL_SCHEMA_INIT = "Handling {class} schema";
	public static final String DEBUG_SQL_SCHEMA_FOUND = "Found schema: {schema}";
	public static final String DEBUG_SQL_CONNECTIONERROR = "Failed to connect to {storage}: {message}";
	public static final String DEBUG_SQL_ERROR = "Error in {class} at {method}_{line}: {message}";
	public static final String DEBUG_SQL_ERROR_TABLE = "Error in {class} at {method}({table})_{line}: {message}";
	
	
	/*
	 * SQL Queries
	 */
	public static final String QUERY_MYSQL_CHECKVERSION = "SELECT table_comment FROM INFORMATION_SCHEMA.tables WHERE table_schema=? AND table_name=?;";
	public static final String QUERY_SQLITE_CHECKVERSION = "PRAGMA user_version;";
	
	public static final String QUERY_GENERIC_SELECTALL = "SELECT * FROM {table};";
	public static final String QUERY_GENERIC_DROP = "DROP TABLE {table};";
	public static final String QUERY_GENERIC_MYSQL_RENAME = "RENAME TABLE {table} TO {newtable};";
	public static final String QUERY_GENERIC_SQLITE_RENAME = "ALTER TABLE {table} RENAME TO {newtable};";
	
	public static final String QUERY_PLAYER_INSERT_MYSQL = "INSERT INTO {table_players} (uuid, party, rank, name, timestamp, spy, notify) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE party=VALUES(party), rank=VALUES(rank), name=VALUES(name), timestamp=VALUES(timestamp), spy=VALUES(spy), notify=VALUES(notify);";
	public static final String QUERY_PLAYER_INSERT_SQLITE = "INSERT OR REPLACE INTO {table_players} (uuid, party, rank, name, timestamp, spy, notify) VALUES (?,?,?,?,?,?,?);";
	public static final String QUERY_PLAYER_DELETE = "DELETE FROM {table_players} WHERE uuid=?;";
	public static final String QUERY_PLAYER_GET = "SELECT * FROM {table_players} WHERE uuid=?;";
	public static final String QUERY_PLAYER_GETALL = "SELECT * FROM {table_players};";
	public static final String QUERY_PLAYER_GETBYPARTY = "SELECT * FROM {table_players} WHERE party=?;";
	public static final String QUERY_PLAYER_GETBYNAME = "SELECT * FROM {table_players} WHERE name=?;";
	
	public static final String QUERY_PARTY_INSERT_MYSQL = "INSERT INTO {table_parties} (name, leader, description, motd, prefix, suffix, color, kills, password, home) VALUES (?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE leader=VALUES(leader), description=VALUES(description), motd=VALUES(motd), prefix=VALUES(prefix), suffix=VALUES(suffix), color=VALUES(color), kills=VALUES(kills), password=VALUES(password), home=VALUES(home);";
	public static final String QUERY_PARTY_INSERT_SQLITE = "INSERT OR REPLACE INTO {table_parties} (name, leader, description, motd, prefix, suffix, color, kills, password, home) VALUES (?,?,?,?,?,?,?,?,?,?);";
	public static final String QUERY_PARTY_GET = "SELECT * FROM {table_parties} WHERE name=?;";
	public static final String QUERY_PARTY_GETALL = "SELECT * FROM {table_parties};";
	public static final String QUERY_PARTY_GETALLFIXED = "SELECT * FROM {table_parties} WHERE leader='fixed';";
	public static final String QUERY_PARTY_RENAME_PARTIES = "UPDATE {table_parties} SET name=? WHERE name=?;";
	public static final String QUERY_PARTY_RENAME_PLAYERS = "UPDATE {table_players} SET party=? WHERE party=?;";
	public static final String QUERY_PARTY_REMOVE_PARTIES = "DELETE FROM {table_parties} WHERE name=?;";
	public static final String QUERY_PARTY_REMOVE_PLAYERS = "DELETE FROM {table_players} WHERE party=?;";
	
	public static final String QUERY_LOG_INSERT = "INSERT INTO {table_log} (date, level, position, message) VALUES (?,?,?,?);";
	public static final String QUERY_LOG_MIGRATE = "INSERT INTO {table_log} (id, date, level, position, message) VALUES (?,?,?,?,?);";
}
