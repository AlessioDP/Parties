package com.alessiodp.parties.common.configuration;

import com.alessiodp.core.common.logging.ConsoleColor;

public class PartiesConstants {
	
	// Plugin settings
	public static final String PLUGIN_NAME = "Parties";
	public static final String PLUGIN_FALLBACK = "parties";
	public static final ConsoleColor PLUGIN_CONSOLECOLOR = ConsoleColor.CYAN;
	public static final String PLUGIN_PACKAGENAME = "com.alessiodp.parties";
	public static final String PLUGIN_SPIGOTCODE = "3709";
	public static final int PLUGIN_BSTATS_BUKKIT_ID = 501;
	public static final int PLUGIN_BSTATS_BUNGEE_ID = 502;
	
	
	// Versions
	public static final int VERSION_BUKKIT_CONFIG_MAIN = 8;
	public static final int VERSION_BUKKIT_CONFIG_PARTIES = 5;
	public static final int VERSION_BUKKIT_MESSAGES = 10;
	public static final int VERSION_BUNGEE_CONFIG_MAIN = 7;
	public static final int VERSION_BUNGEE_CONFIG_PARTIES = 4;
	public static final int VERSION_BUNGEE_MESSAGES = 8;
	public static final int VERSION_DATABASE_YAML = 1;
	
	
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
	
	public static final String DEBUG_CMD_ACCEPT_ASK = "%s accepted ask request of %s for %s";
	public static final String DEBUG_CMD_ACCEPT_INVITE = "%s accepted invite request for %s";
	public static final String DEBUG_CMD_ASK = "%s asked to join the party %s";
	public static final String DEBUG_CMD_CHAT = "%s changed party chat to %s";
	public static final String DEBUG_CMD_CLAIM = "%s claimed with permission %s";
	public static final String DEBUG_CMD_COLOR = "%s changed the color of %s into %s";
	public static final String DEBUG_CMD_COLOR_REM = "%s removed color of %s";
	public static final String DEBUG_CMD_CREATE = "%s created the party %s";
	public static final String DEBUG_CMD_CREATE_FIXED = "%s created the fixed party %s";
	public static final String DEBUG_CMD_CREATE_REGEXERROR_ALLOWEDCHARS = "Failed to parse allowed characters regex of create command";
	public static final String DEBUG_CMD_CREATE_REGEXERROR_CENSORED = "Failed to parse censor regex of create command";
	public static final String DEBUG_CMD_DELETE = "%s deleted the party %s";
	public static final String DEBUG_CMD_DENY_ASK = "%s denied ask request of %s for %s";
	public static final String DEBUG_CMD_DENY_INVITE = "%s denied invite request for %s";
	public static final String DEBUG_CMD_DESC = "%s changed the description of %s";
	public static final String DEBUG_CMD_DESC_REM = "%s removed the description of %s";
	public static final String DEBUG_CMD_DESC_REGEXERROR_ALLOWEDCHARS = "Failed to parse allowed characters regex of desc command";
	public static final String DEBUG_CMD_DESC_REGEXERROR_CENSORED = "Failed to parse censor regex of desc command";
	public static final String DEBUG_CMD_FOLLOW = "%s set follow to %b for %s";
	public static final String DEBUG_CMD_HOME = "%s used command home on %s to %s";
	public static final String DEBUG_CMD_IGNORE_START = "%s started to ignore the party %s";
	public static final String DEBUG_CMD_IGNORE_STOP = "%s stopped to ignore the party %s";
	public static final String DEBUG_CMD_INFO = "%s used command info on %s";
	public static final String DEBUG_CMD_INVITE = "%s invited %s into %s (revoke %b)";
	public static final String DEBUG_CMD_JOIN = "%s joined into %s";
	public static final String DEBUG_CMD_KICK = "%s kicked out %s from %s (other %b) (disband %b)";
	public static final String DEBUG_CMD_KICK_LEADER_CHANGE = "%s kicked out %s from %s, new leader is %s";
	public static final String DEBUG_CMD_LEAVE = "%s left the party %s (disband %b)";
	public static final String DEBUG_CMD_LEAVE_LEADER_CHANGE = "%s left the party %s, new leader is %s";
	public static final String DEBUG_CMD_MOTD = "%s changed the motd of %s";
	public static final String DEBUG_CMD_MOTD_REM = "%s removed the motd of %s";
	public static final String DEBUG_CMD_MOTD_REGEXERROR_ALLOWEDCHARS = "Failed to parse allowed characters regex of motd command";
	public static final String DEBUG_CMD_MOTD_REGEXERROR_CENSORED = "Failed to parse censor regex of motd command";
	public static final String DEBUG_CMD_MUTE = "%s set mute on %b";
	public static final String DEBUG_CMD_P = "%s sent a party message to %s: %s";
	public static final String DEBUG_CMD_P_REGEXERROR = "Failed to parse censor regex of p command";
	public static final String DEBUG_CMD_PASSWORD = "%s changed the password of %s";
	public static final String DEBUG_CMD_PASSWORD_REM = "%s removed the password of %s";
	public static final String DEBUG_CMD_PROTECTION = "%s set party %s protection to %b";
	public static final String DEBUG_CMD_RANK = "%s changed the %s rank of party %s from %d to %d (other %b)";
	public static final String DEBUG_CMD_RELOADED = "%s reloaded the configuration";
	public static final String DEBUG_CMD_RENAME = "%s renamed the party %s to %s";
	public static final String DEBUG_CMD_SETHOME = "%s set the home of %s with name %s";
	public static final String DEBUG_CMD_SETHOME_REM = "%s removed the home of %s with name %s";
	public static final String DEBUG_CMD_SPY = "%s set spy on %b";
	public static final String DEBUG_CMD_TAG = "%s changed the tag of %s";
	public static final String DEBUG_CMD_TAG_REM = "%s removed the tag of %s";
	public static final String DEBUG_CMD_TAG_REGEXERROR_AC = "Failed to parse allowed characters regex of tag command";
	public static final String DEBUG_CMD_TAG_REGEXERROR_CEN = "Failed to parse censor regex of tag command";
	public static final String DEBUG_CMD_TELEPORT = "%s used teleport command on party %s";
	
	public static final String DEBUG_CONFIG_FAILED_PLACEHOLDERS_NOTFOUND = "Cannot find custom placeholders list, restoring default one.";
	public static final String DEBUG_CONFIG_FAILED_RANK_NODEFAULT = "Cannot find default rank, set lower one.";
	public static final String DEBUG_CONFIG_FAILED_RANK_ONLYONE = "Rank list contains only 1 rank, at least 2 are required.";
	public static final String DEBUG_CONFIG_FAILED_RANK_EMPTY = "Rank list empty, restoring default one.";
	public static final String DEBUG_CONFIG_FAILED_RANK_NOTFOUND = "Cannot find rank list, restoring default one.";
	public static final String DEBUG_CONFIG_FAILED_COLOR_NOTFOUND = "Cannot find color list, restoring default one.";
	
	
	public static final String DEBUG_DB_UPDATEPLAYER = "Update player request for {player} [{uuid}]";
	public static final String DEBUG_DB_GETPLAYER = "Get player request for '{uuid}'";
	public static final String DEBUG_DB_UPDATEPARTY = "Update party request for '{party}'";
	public static final String DEBUG_DB_GETPARTY = "Get party request for '{party}'";
	public static final String DEBUG_DB_REMOVEPARTY = "Remove party request for '{party}'";
	public static final String DEBUG_DB_EXISTSPARTY = "Exists party request for '{party}'";
	public static final String DEBUG_DB_EXISTSTAG = "Exists tag request for '{tag}'";
	public static final String DEBUG_DB_GETALLFIXEDPARTIES = "Get all fixed parties request";
	public static final String DEBUG_DB_GETALLPARTIES = "Get all parties request";
	public static final String DEBUG_DB_GETALLPARTIES_NUMBER = "Get the number of all parties";
	
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
	
	public static final String DEBUG_TASK_ASK_COOLDOWN_EXPIRED = "Expired ask cooldown of {uuid}";
	public static final String DEBUG_TASK_CHAT_EXPIRED = "Chat cooldown expired for {uuid}";
	public static final String DEBUG_TASK_DELETE_START = "Started PartyDeleteTask of {party} for {value} seconds";
	public static final String DEBUG_TASK_DELETE_STOP = "Stopped PartyDeleteTask of {party}";
	public static final String DEBUG_TASK_HOME_DENIED_FIGHT = "Denied home by {player} due to fight";
	public static final String DEBUG_TASK_HOME_DENIED_MOVING = "Denied home by {player} due to moving";
	public static final String DEBUG_TASK_HOME_EXPIRED = "Home cooldown expired for {uuid}";
	public static final String DEBUG_TASK_SETHOME_EXPIRED = "Set home cooldown expired for {uuid}";
	public static final String DEBUG_TASK_INVITE_COOLDOWN_EXPIRED = "Expired invite cooldown of {uuid}";
	public static final String DEBUG_TASK_RENAME_EXPIRED = "Rename cooldown expired for the party {uuid}";
	public static final String DEBUG_TASK_TELEPORT_DONE = "{player} teleported to the party home";
	public static final String DEBUG_TASK_TELEPORT_EXPIRED = "Teleport for {player} expired";
	
	public static final String DEBUG_TELEPORT_ASYNC = "Failed to async teleport";
}
