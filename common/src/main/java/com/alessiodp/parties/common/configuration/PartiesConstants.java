package com.alessiodp.parties.common.configuration;

import com.alessiodp.core.common.logging.ConsoleColor;

public class PartiesConstants {
	
	// Plugin settings
	public static final String PLUGIN_NAME = "Parties";
	public static final String PLUGIN_FALLBACK = "parties";
	public static final ConsoleColor PLUGIN_CONSOLECOLOR = ConsoleColor.CYAN;
	public static final String PLUGIN_SPIGOTCODE = "3709";
	
	
	// Versions
	public static final int VERSION_BUKKIT_CONFIG_MAIN = 7;
	public static final int VERSION_BUKKIT_CONFIG_PARTIES = 5;
	public static final int VERSION_BUKKIT_MESSAGES = 8;
	public static final int VERSION_BUNGEE_CONFIG_MAIN = 6;
	public static final int VERSION_BUNGEE_CONFIG_PARTIES = 4;
	public static final int VERSION_BUNGEE_MESSAGES = 6;
	public static final int VERSION_DATABASE_YAML = 1;
	public static final int VERSION_DATABASE_MYSQL = 7;
	public static final int VERSION_DATABASE_SQLITE = 5;
	
	// Fixed parties
	public static final String FIXED_VALUE_TEXT = "fixed";
	public static final String FIXED_VALUE_UUID = "00000000-0000-0000-0000-000000000000";
	
	// Placeholders
	public static final String PLACEHOLDER_PLAYER_NAME = "%player%";
	public static final String PLACEHOLDER_PLAYER_RANK_NAME = "%rank_name%";
	public static final String PLACEHOLDER_PLAYER_RANK_CHAT = "%rank_chat%";
	public static final String PLACEHOLDER_PLAYER_USER = "%user%";
	public static final String PLACEHOLDER_PLAYER_VICTIM = "%victim%";
	
	public static final String PLACEHOLDER_PARTY_COLOR_CODE = "%color_code%";
	public static final String PLACEHOLDER_PARTY_COLOR_COMMAND = "%color_command%";
	public static final String PLACEHOLDER_PARTY_COLOR_NAME = "%color_name%";
	public static final String PLACEHOLDER_PARTY_DESC = "%desc%";
	public static final String PLACEHOLDER_PARTY_EXPERIENCE_TOTAL = "%experience_total%";
	public static final String PLACEHOLDER_PARTY_EXPERIENCE_LEVEL = "%experience_level%";
	public static final String PLACEHOLDER_PARTY_EXPERIENCE_LEVELUP_CURRENT = "%experience_levelup_current%";
	public static final String PLACEHOLDER_PARTY_EXPERIENCE_LEVELUP_NECESSARY = "%experience_levelup_necessary%";
	public static final String PLACEHOLDER_PARTY_KILLS = "%kills%";
	public static final String PLACEHOLDER_PARTY_LIST = "%list_([^%]+)%";
	public static final String PLACEHOLDER_PARTY_LIST_ONLINE = "%online%";
	public static final String PLACEHOLDER_PARTY_MOTD = "%motd%";
	public static final String PLACEHOLDER_PARTY_ONLINENUMBER = "%onlinenumber%";
	public static final String PLACEHOLDER_PARTY_OUT = "%out_party%";
	
	public static final String PLACEHOLDER_PARTY_PARTY = "%party%";
	
	// SQL queries
	public static final String QUERY_PLAYER_INSERT_MYSQL = "INSERT INTO {table_players} (`uuid`, `party`, `rank`, `spy`, `mute`) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE `party`=VALUES(`party`), `rank`=VALUES(`rank`), `spy`=VALUES(`spy`), `mute`=VALUES(`mute`);";
	public static final String QUERY_PLAYER_INSERT_SQLITE = "INSERT OR REPLACE INTO {table_players} (`uuid`, `party`, `rank`, `spy`, `mute`) VALUES (?,?,?,?,?);";
	public static final String QUERY_PLAYER_DELETE = "DELETE FROM {table_players} WHERE `uuid`=?;";
	public static final String QUERY_PLAYER_GET = "SELECT * FROM {table_players} WHERE `uuid`=?;";
	public static final String QUERY_PLAYER_GETALL = "SELECT * FROM {table_players};";
	public static final String QUERY_PLAYER_GETBYPARTY = "SELECT * FROM {table_players} WHERE `party`=?;";
	
	public static final String QUERY_PARTY_INSERT_MYSQL = "INSERT INTO {table_parties} (`name`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`) VALUES (?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `leader`=VALUES(`leader`), `description`=VALUES(`description`), `motd`=VALUES(`motd`), `color`=VALUES(`color`), `kills`=VALUES(`kills`), `password`=VALUES(`password`), `home`=VALUES(`home`), `protection`=VALUES(`protection`), `experience`=VALUES(`experience`), `follow`=VALUES(`follow`);";
	public static final String QUERY_PARTY_INSERT_SQLITE = "INSERT OR REPLACE INTO {table_parties} (`name`, `leader`, `description`, `motd`, `color`, `kills`, `password`, `home`, `protection`, `experience`, `follow`) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	public static final String QUERY_PARTY_GET_MYSQL = "SELECT * FROM {table_parties} WHERE `name`=?;";
	public static final String QUERY_PARTY_GET_SQLITE = "SELECT * FROM {table_parties} WHERE `name`=? COLLATE NOCASE;";
	public static final String QUERY_PARTY_GETALL = "SELECT * FROM {table_parties};";
	public static final String QUERY_PARTY_GETALLFIXED = "SELECT * FROM {table_parties} WHERE `leader`='fixed';";
	public static final String QUERY_PARTY_RENAME_PARTIES = "UPDATE {table_parties} SET `name`=? WHERE `name`=?;";
	public static final String QUERY_PARTY_RENAME_PLAYERS = "UPDATE {table_players} SET `party`=? WHERE `party`=?;";
	public static final String QUERY_PARTY_REMOVE_PARTIES = "DELETE FROM {table_parties} WHERE `name`=?;";
	
	
	// Debug messages
	public static final String DEBUG_API_CHATEVENT_DENY = "PartiesChatEvent is cancelled, ignoring chat of {player}: {message}";
	public static final String DEBUG_API_CREATEEVENT_DENY = "PartiesCreateEvent is cancelled, ignoring create of {party} by {player}";
	public static final String DEBUG_API_DELETEEVENT_DENY = "PartiesDeleteEvent is cancelled, ignoring delete of {party} by {player}";
	public static final String DEBUG_API_DELETEEVENT_DENY_GENERIC = "PartiesDeleteEvent is cancelled, ignoring delete of {party}";
	public static final String DEBUG_API_FRIENDLYFIREEVENT_DENY = "PartiesFriendlyFireEvent is cancelled, ignoring fight (type {type}) by '{player}' to '{victim}'";
	public static final String DEBUG_API_JOINEVENT_DENY = "PartiesJoinEvent is cancelled, ignoring join of {player} into {party}";
	public static final String DEBUG_API_LEAVEEVENT_DENY = "PartiesLeaveEvent is cancelled, ignoring leave of {player} from {party}";
	public static final String DEBUG_API_RENAMEEVENT_DENY = "PartiesRenameEvent is cancelled, ignoring rename of {party} by {player}";
	
	
	public static final String DEBUG_AUTOCMD_PERFORM = "Performing autocommand to {player} with '{command}'";
	public static final String DEBUG_AUTOCMD_REGEXERROR = "Wrong regex pattern for auto command system";
	
	public static final String DEBUG_CMD_ACCEPT = "{player} accepted invite for {party}";
	public static final String DEBUG_CMD_CHAT = "{player} changed party chat to {value}";
	public static final String DEBUG_CMD_CLAIM = "{player} claimed with {value}";
	public static final String DEBUG_CMD_COLOR = "{player} set color of the party {party} to {value}";
	public static final String DEBUG_CMD_COLOR_REM = "{player} removed color of the party {party}";
	public static final String DEBUG_CMD_CREATE = "{player} created the party {party}";
	public static final String DEBUG_CMD_CREATE_FIXED = "{player} created the fixed party {party}";
	public static final String DEBUG_CMD_CREATE_REGEXERROR_AC = "Failed to parse allowed characters regex of create command";
	public static final String DEBUG_CMD_CREATE_REGEXERROR_CEN = "Failed to parse censor regex of create command";
	public static final String DEBUG_CMD_DELETE = "{player} deleted the party {party}";
	public static final String DEBUG_CMD_DENY = "{player} deleted the party {party}";
	public static final String DEBUG_CMD_DESC = "{player} changed the description of {party}";
	public static final String DEBUG_CMD_DESC_REM = "{player} removed the description of {party}";
	public static final String DEBUG_CMD_DESC_REGEXERROR_AC = "Failed to parse allowed characters regex of desc command";
	public static final String DEBUG_CMD_DESC_REGEXERROR_CEN = "Failed to parse censor regex of desc command";
	public static final String DEBUG_CMD_FOLLOW_ON = "{player} has enabled follow for {party}";
	public static final String DEBUG_CMD_FOLLOW_OFF = "{player} has disabled follow for {party}";
	public static final String DEBUG_CMD_HELP = "{player} performed help command with page '{page}'";
	public static final String DEBUG_CMD_HOME = "{player} used command home, for {party}";
	public static final String DEBUG_CMD_IGNORE_START = "{player} started to ignore the party {party}";
	public static final String DEBUG_CMD_IGNORE_STOP = "{player} stopped to ignore the party {party}";
	public static final String DEBUG_CMD_INFO = "{player} used the command info, for {party}";
	public static final String DEBUG_CMD_INVITE = "{player} invited {victim} into {party} [revoke={revoke}]";
	public static final String DEBUG_CMD_JOIN = "{player} joined into {party}";
	public static final String DEBUG_CMD_KICK = "{player} got kicked out from {party} by {user} [other={other}]";
	public static final String DEBUG_CMD_KICK_DISBAND = "{player} got kicked out (disband) from {party} by {user} [other={other}]";
	public static final String DEBUG_CMD_LEAVE = "{player} leaved the party {party}";
	public static final String DEBUG_CMD_LEAVE_DISBAND = "{player} leaved (disbanding) the party {party}";
	public static final String DEBUG_CMD_LIST = "{player} used the command list";
	public static final String DEBUG_CMD_MOTD = "{player} changed the motd of {party}";
	public static final String DEBUG_CMD_MOTD_REM = "{player} removed the motd of {party}";
	public static final String DEBUG_CMD_MOTD_REGEXERROR_AC = "Failed to parse allowed characters regex of motd command";
	public static final String DEBUG_CMD_MOTD_REGEXERROR_CEN = "Failed to parse censor regex of motd command";
	public static final String DEBUG_CMD_MUTE_ON = "{player} is now muted";
	public static final String DEBUG_CMD_MUTE_OFF = "{player} is not muted anymore";
	public static final String DEBUG_CMD_P = "Chat of {party} by {player}: {message}";
	public static final String DEBUG_CMD_P_TASK = "Started ChatTask for {value} by {player}";
	public static final String DEBUG_CMD_P_REGEXERROR = "Failed to parse censor regex of p command";
	public static final String DEBUG_CMD_PASSWORD = "{player} changed the password of {party}";
	public static final String DEBUG_CMD_PASSWORD_REM = "{player} removed the password of {party}";
	public static final String DEBUG_CMD_PROTECTION_ON = "{player} has enabled protection for {party}";
	public static final String DEBUG_CMD_PROTECTION_OFF = "{player} has disabled protection for {party}";
	public static final String DEBUG_CMD_TELEPORT = "{player} used command teleport";
	public static final String DEBUG_CMD_RANK = "{player} rank changed from {value1} to {value2} by {user} [other={other}]";
	public static final String DEBUG_CMD_RELOAD = "{player} performed reload command";
	public static final String DEBUG_CMD_RELOAD_CONSOLE = "Console performed reload command";
	public static final String DEBUG_CMD_RELOADED = "Configuration reloaded by {player}";
	public static final String DEBUG_CMD_RELOADED_CONSOLE = "Configuration reloaded";
	public static final String DEBUG_CMD_RENAME = "{player} renamed the party {value} to {party}";
	public static final String DEBUG_CMD_SETHOME = "{player} changed the home of {party}";
	public static final String DEBUG_CMD_SETHOME_REM = "{player} removed the home of {party}";
	public static final String DEBUG_CMD_SPY_ENABLE = "{player} now is a spy";
	public static final String DEBUG_CMD_SPY_DISABLE = "{player} isn't a spy anymore";
	public static final String DEBUG_CMD_VERSION = "{player} performed version command";
	public static final String DEBUG_CMD_VERSION_CONSOLE = "Performed version command";
	
	public static final String DEBUG_CONFIG_FAILED_PLACEHOLDERS_NOTFOUND = "Cannot find custom placeholders list, restoring default one.";
	public static final String DEBUG_CONFIG_FAILED_RANK_NODEFAULT = "Cannot find default rank, set lower one.";
	public static final String DEBUG_CONFIG_FAILED_RANK_ONLYONE = "Rank list contains only 1 rank, at least 2 are required.";
	public static final String DEBUG_CONFIG_FAILED_RANK_EMPTY = "Rank list empty, restoring default one.";
	public static final String DEBUG_CONFIG_FAILED_RANK_NOTFOUND = "Cannot find rank list, restoring default one.";
	public static final String DEBUG_CONFIG_FAILED_COLOR_NOTFOUND = "Cannot find color list, restoring default one.";
	
	
	public static final String DEBUG_DB_UPDATEPLAYER = "Update player request for {player} [{uuid}]";
	public static final String DEBUG_DB_GETPLAYER = "Get player request request for '{uuid}'";
	public static final String DEBUG_DB_UPDATEPARTY = "Update party request for '{party}'";
	public static final String DEBUG_DB_GETPARTY = "Get party request for '{party}'";
	public static final String DEBUG_DB_RENAMEPARTY = "Rename party request for '{party}' into '{name}'";
	public static final String DEBUG_DB_REMOVEPARTY = "Remove party request for '{party}'";
	public static final String DEBUG_DB_EXISTSPARTY = "Exists party request for '{party}'";
	public static final String DEBUG_DB_GETALLFIXEDPARTIES = "Get all fixed parties request";
	public static final String DEBUG_DB_GETALLPARTIES = "Get all parties request";
	public static final String DEBUG_DB_GETALLPLAYERS = "Get all players parties request";
	
	public static final String DEBUG_EXP_RECEIVED = "Received a distribute exp request. Normal experience: {normal}, SkillAPI Experience: {skillapi}";
	public static final String DEBUG_EXP_SENT = "Sent {exp} {type} to {player}";
	public static final String DEBUG_EXP_LEVELERROR = "Something gone wrong on calculate the level of '{party}': {message}";
	public static final String DEBUG_EXP_EXPRESSIONERROR = "Something gone wrong on calculate the formula '{value}': {message}";
	public static final String DEBUG_EXP_REMOVINGEXP = "Removing exp from normal={value1} and skillapi={value2}";
	public static final String DEBUG_EXP_MMBYPASS = "Bypassing vanilla event due to MythicMob kill";
	public static final String DEBUG_EXP_MMHANDLING = "Handling MythicMob mob '{name}' killed by {player}";
	
	public static final String DEBUG_FOLLOW_SERVER_REGEXERROR = "Wrong regex pattern for allowed servers of follow-server feature";
	
	public static final String DEBUG_FRIENDLYFIRE_DENIED = "Denied friendly fire (type {type}) by '{player}' to '{victim}'";
	
	public static final String DEBUG_KILL_ADD = "Adding a kill to the party {party} by {player}";
	
	public static final String DEBUG_LIB_BANMANAGER_BAN = "Party '{party}' deleted because leader got banned by {player}";
	
	public static final String DEBUG_MESSAGING_RECEIVED = "Received a Parties packet of type '{type}'";
	public static final String DEBUG_MESSAGING_RECEIVED_WRONG = "Received a wrong Parties packet";
	public static final String DEBUG_MESSAGING_LISTEN_PLAYER_UPDATED = "Received a Parties packet, updated player {uuid}";
	public static final String DEBUG_MESSAGING_LISTEN_PARTY_UPDATED = "Received a Parties packet, updated party {party}";
	public static final String DEBUG_MESSAGING_LISTEN_PARTY_RENAMED = "Received a Parties packet, renamed player {party}";
	public static final String DEBUG_MESSAGING_LISTEN_PARTY_REMOVED = "Received a Parties packet, removed player {party}";
	public static final String DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE = "Received a Parties packet, dispatched a chat message of the party {party}";
	public static final String DEBUG_MESSAGING_LISTEN_BROADCAST_MESSAGE = "Received a Parties packet, dispatched a broadcast message of the party {party}";
	
	public static final String DEBUG_PARTY_CREATE = "Created party {party}";
	public static final String DEBUG_PARTY_DELETE = "Deleted party {party}";
	public static final String DEBUG_PARTY_RENAME = "Renamed party {party} into {name}";
	public static final String DEBUG_PARTY_RELOADED = "Reloaded party {party}";
	public static final String DEBUG_PARTY_FIXED_LOAD = "Loading fixed party {party}";
	public static final String DEBUG_PARTY_GET_DATABASE = "Got party {party} from database";
	public static final String DEBUG_PARTY_GET_LIST = "Got party {party} from list";
	public static final String DEBUG_PARTY_DELETE_CAUSE = "Deleted party {party} due {cause}";
	
	public static final String DEBUG_PLAYER_PARTY_JOIN = "Added {player} into party {party} ({uuid})";
	public static final String DEBUG_PLAYER_PARTY_LEAVE = "Removed {player} from party {party} ({uuid})";
	public static final String DEBUG_PLAYER_RELOADED = "Reloaded player {player}";
	public static final String DEBUG_PLAYER_CLEANUP = "Cleaning up player {player}";
	public static final String DEBUG_PLAYER_GET_DATABASE = "Got player {player} from database (party: {party})";
	public static final String DEBUG_PLAYER_GET_LIST = "Got player {player} from list (party: {party})";
	public static final String DEBUG_PLAYER_GET_NEW = "Got a new player {player}";
	public static final String DEBUG_PLAYER_JOIN = "{player} entered in the game, party '{party}'";
	public static final String DEBUG_PLAYER_JOIN_DEFAULTJOIN = "{player} joined without party, default join into {party}";
	public static final String DEBUG_PLAYER_JOIN_DEFAULTFAIL = "Failed to load default party {party}";
	
	public static final String DEBUG_PLUGIN_RELOADING = "Reloading plugin...";
	
	public static final String DEBUG_TASK_CHAT_EXPIRE = "Chat cooldown expired for {uuid}";
	public static final String DEBUG_TASK_DELETE_START = "Started PartyDeleteTask of {party} for {value} seconds";
	public static final String DEBUG_TASK_DELETE_STOP = "Stopped PartyDeleteTask of {party}";
	public static final String DEBUG_TASK_HOME_DENIED_FIGHT = "Denied home by {player} due to fight";
	public static final String DEBUG_TASK_HOME_DENIED_MOVING = "Denied home by {player} due to moving";
	public static final String DEBUG_TASK_INVITE_EXPIRED = "Expired party '{party}' invite for {uuid}";
	public static final String DEBUG_TASK_TELEPORT_START = "Started TeleportTask for {value} by {player}";
	public static final String DEBUG_TASK_TELEPORT_DONE = "{player} teleported to the party home";
	public static final String DEBUG_TASK_TELEPORT_EXPIRED = "Teleport for {player} expired";
}
