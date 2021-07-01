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
	public static final int VERSION_BUKKIT_CONFIG_MAIN = 13;
	public static final int VERSION_BUKKIT_CONFIG_PARTIES = 8;
	public static final int VERSION_BUKKIT_MESSAGES = 12;
	public static final int VERSION_BUNGEE_CONFIG_MAIN = 12;
	public static final int VERSION_BUNGEE_CONFIG_PARTIES = 7;
	public static final int VERSION_BUNGEE_MESSAGES = 10;
	public static final int VERSION_DATABASE_YAML = 2;
	
	
	// Debug messages
	public static final String DEBUG_API_CHATEVENT_DENY = "PartiesChatEvent is cancelled, ignoring chat of player %s: %s";
	public static final String DEBUG_API_CREATEEVENT_DENY = "PartiesCreateEvent is cancelled, ignoring create of %s by player %s (uuid: %s)";
	public static final String DEBUG_API_DELETEEVENT_DENY = "PartiesDeleteEvent is cancelled, ignoring delete of party %s by player %s (uuid: %s)";
	public static final String DEBUG_API_DELETEEVENT_DENY_GENERIC = "PartiesDeleteEvent is cancelled, ignoring delete of party %s";
	public static final String DEBUG_API_RENAMEEVENT_DENY = "PartiesRenameEvent is cancelled, ignoring rename of party %s into %s by player %s";
	public static final String DEBUG_API_HOMEEVENT_DENY = "PartiesHomeEvent is cancelled, ignoring home teleport of player %s: %s";
	public static final String DEBUG_API_JOINEVENT_DENY = "PartiesJoinEvent is cancelled, ignoring join of player %s into party %s";
	public static final String DEBUG_API_LEAVEEVENT_DENY = "PartiesLeaveEvent is cancelled, ignoring leave of player %s from party %s";
	public static final String DEBUG_API_INVITEEVENT_DENY = "PartiesInviteEvent is cancelled, ignoring invite of player %s in party %s by %s";
	public static final String DEBUG_API_FRIENDLYFIREEVENT_DENY = "PartiesFriendlyFireEvent is cancelled, ignoring fight (type %s) of player %s to player %s";
	public static final String DEBUG_API_TELEPORTEVENT_DENY = "PartiesTeleportEvent is cancelled, ignoring teleport of player %s: %s";
	
	
	public static final String DEBUG_AUTOCMD_PERFORM = "Performing autocommand to player %s with '%s'";
	public static final String DEBUG_AUTOCMD_REGEXERROR = "Wrong regex pattern for auto command system";
	
	public static final String DEBUG_CMD_ACCEPT_ASK = "%s accepted ask request of %s for %s";
	public static final String DEBUG_CMD_ACCEPT_INVITE = "%s accepted invite request for %s";
	public static final String DEBUG_CMD_ACCEPT_TELEPORT = "%s accepted teleport request of %s";
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
	public static final String DEBUG_CMD_DENY_TELEPORT = "%s denied teleport request of %s";
	public static final String DEBUG_CMD_DESC = "%s changed the description of %s";
	public static final String DEBUG_CMD_DESC_REM = "%s removed the description of %s";
	public static final String DEBUG_CMD_DESC_REGEXERROR_ALLOWEDCHARS = "Failed to parse allowed characters regex of desc command";
	public static final String DEBUG_CMD_DESC_REGEXERROR_CENSORED = "Failed to parse censor regex of desc command";
	public static final String DEBUG_CMD_FOLLOW = "%s set follow to %b for %s";
	public static final String DEBUG_CMD_HOME = "%s used command home on %s to %s";
	public static final String DEBUG_CMD_HOME_NO_SERVER = "Failed to find the server for the home: %s";
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
	public static final String DEBUG_CMD_NICKNAME = "%s changed nickname of %s";
	public static final String DEBUG_CMD_NICKNAME_REM = "%s removed nickname of %s";
	public static final String DEBUG_CMD_NICKNAME_REGEXERROR_ALLOWEDCHARS = "Failed to parse allowed characters regex of nickname command";
	public static final String DEBUG_CMD_NICKNAME_REGEXERROR_CENSORED = "Failed to parse censor regex of nickname command";
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
	
	
	public static final String DEBUG_DB_UPDATEPLAYER = "Update player request for %s (uuid: %s)";
	public static final String DEBUG_DB_GETPLAYER = "Get player request for %s";
	public static final String DEBUG_DB_GETALLPLAYERS_NUMBER = "Get the number of all players";
	public static final String DEBUG_DB_UPDATEPARTY = "Update party request for %s (uuid: %s)";
	public static final String DEBUG_DB_GETPARTY = "Get party request for %s";
	public static final String DEBUG_DB_REMOVEPARTY = "Remove party request for %s (uuid: %s)";
	public static final String DEBUG_DB_EXISTSPARTY = "Exists party request for %s";
	public static final String DEBUG_DB_EXISTSTAG = "Exists tag request for '%s'";
	public static final String DEBUG_DB_GETALLFIXEDPARTIES = "Get all fixed parties request";
	public static final String DEBUG_DB_GETALLPARTIES = "Get all parties request";
	public static final String DEBUG_DB_GETALLPARTIES_NUMBER = "Get the number of all parties";
	
	public static final String DEBUG_EXP_RECEIVED = "Received a distribute exp request. Normal experience: %s, SkillAPI Experience: %s";
	public static final String DEBUG_EXP_SENT = "Sent %s %s to player %s (uuid: %s)";
	public static final String DEBUG_EXP_LEVELERROR = "Something gone wrong on calculate the level of party %s: %s";
	public static final String DEBUG_EXP_EXPRESSIONERROR = "Something gone wrong on calculate the formulas '%s' (killer) and '%s' (others): %s";
	public static final String DEBUG_EXP_REMOVINGEXP = "Removing exp from event. Normal=%b and skillapi=%b";
	public static final String DEBUG_EXP_MMBYPASS = "Bypassing vanilla event due to MythicMob kill";
	public static final String DEBUG_EXP_MMHANDLING = "Handling MythicMob mob '%s' killed by player %s (uuid: %s)";
	public static final String DEBUG_EXP_SEND_PARTY = "Sending %s party experience to the party %s";
	public static final String DEBUG_EXP_SAFE_CALCULATION = "Triggered and prevented an infinite calculation in progressive experience (start: %f, formula: '%s')";
	public static final String DEBUG_EXP_START_EXP_0 = "The starting progressive experience cannot be 0";
	
	public static final String DEBUG_FOLLOW_SERVER_REGEXERROR = "Wrong regex pattern for allowed servers of follow-server feature";
	public static final String DEBUG_FOLLOW_WORLD_REGEXERROR = "Wrong regex pattern for allowed worlds of follow-server feature";
	
	public static final String DEBUG_FRIENDLYFIRE_DENIED = "Denied friendly fire (type %s) of player %s to player %s";
	public static final String DEBUG_FRIENDLYFIRE_FISH_NOT_SUPPORTED = "The prevent fish hook friendly fire is not supported in this MC version";
	
	public static final String DEBUG_HOME_NO_SERVER = "Home command executed but it doesn't contains the server destination (party: %s)";
	
	public static final String DEBUG_KILL_ADD = "Adding a kill to the party %s by player %s";
	
	public static final String DEBUG_MESSAGING_RECEIVED = "Received a Parties packet of type '%s' from channel '%s'";
	public static final String DEBUG_MESSAGING_RECEIVED_WRONG = "Received a wrong Parties packet from channel '%s'";
	public static final String DEBUG_MESSAGING_LISTEN_UPDATE_PARTY = "Received a Parties packet, updated party %s";
	public static final String DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER = "Received a Parties packet, updated player %s";
	public static final String DEBUG_MESSAGING_LISTEN_LOAD_PLAYER = "Received a Parties packet, loaded player %s";
	public static final String DEBUG_MESSAGING_LISTEN_UNLOAD_PARTY = "Received a Parties packet, unloaded party %s";
	public static final String DEBUG_MESSAGING_LISTEN_UNLOAD_PLAYER = "Received a Parties packet, unloaded player %s";
	public static final String DEBUG_MESSAGING_LISTEN_PLAY_SOUND = "Received a Parties packet, play sound player %s";
	public static final String DEBUG_MESSAGING_LISTEN_PLAY_SOUND_ERROR = "Received a Parties packet, play sound parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_CREATE_PARTY = "Received a Parties packet, created party %s by player %s";
	public static final String DEBUG_MESSAGING_LISTEN_DELETE_PARTY = "Received a Parties packet, deleted party %s (cause: %s, kicked: %s) by player %s";
	public static final String DEBUG_MESSAGING_LISTEN_DELETE_PARTY_ERROR = "Received a Parties packet, deleted party parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_RENAME_PARTY = "Received a Parties packet, renamed party %s from %s to %s by player %s";
	public static final String DEBUG_MESSAGING_LISTEN_RENAME_PARTY_ERROR = "Received a Parties packet, renamed party parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_ADD_MEMBER_PARTY = "Received a Parties packet, add member %s to party %s (cause: %s, by: %s)";
	public static final String DEBUG_MESSAGING_LISTEN_ADD_MEMBER_PARTY_ERROR = "Received a Parties packet, add member party parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_REMOVE_MEMBER_PARTY = "Received a Parties packet, remove member %s to party %s (cause: %s, by: %s)";
	public static final String DEBUG_MESSAGING_LISTEN_REMOVE_MEMBER_PARTY_ERROR = "Received a Parties packet, remove member party parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE = "Received a Parties packet, sending a chat message by player %s in party %s: %s";
	public static final String DEBUG_MESSAGING_LISTEN_INVITE_PARTY = "Received a Parties packet, invited player %s to party %s by player %s";
	public static final String DEBUG_MESSAGING_LISTEN_INVITE_PARTY_ERROR = "Received a Parties packet, renamed party parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_ADD_HOME = "Received a Parties packet, add home %s to party %s by player %s in server %s";
	public static final String DEBUG_MESSAGING_LISTEN_ADD_HOME_ERROR = "Received a Parties packet, add home parsing failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_ADD_HOME_BUNGEE = "Received a Parties packet, add new home to party %s";
	public static final String DEBUG_MESSAGING_LISTEN_HOME_TELEPORT = "Received a Parties packet, home teleporting player %s to %s";
	public static final String DEBUG_MESSAGING_LISTEN_HOME_TELEPORT_ERROR = "Received a Parties packet, home teleporting failed: %s";
	public static final String DEBUG_MESSAGING_LISTEN_TELEPORT = "Received a Parties packet, teleport player %s to %s";
	public static final String DEBUG_MESSAGING_LISTEN_EXPERIENCE = "Received a Parties packet, gave %s experience to party %s (killer: %s)";
	public static final String DEBUG_MESSAGING_LISTEN_LEVEL_UP = "Received a Parties packet, %s leveled up to %d";
	public static final String DEBUG_MESSAGING_LISTEN_CONFIGS = "Received a Parties packet, configs sync";
	public static final String DEBUG_MESSAGING_LISTEN_REQUEST_CONFIGS = "Received a Parties packet, request for configs sync";
	
	public static final String DEBUG_MIGRATE_MYSQL = "Upgrading MySQL database from version 0";
	public static final String DEBUG_MIGRATE_SQLITE = "Upgrading SQLite database from version 0";
	public static final String DEBUG_MIGRATE_YAML = "Upgrading YAML database from version %d";
	
	public static final String DEBUG_PARTY_CREATE = "Created party %s";
	public static final String DEBUG_PARTY_DELETE = "Deleted party %s";
	public static final String DEBUG_PARTY_RENAME = "Renamed party %s into %s";
	public static final String DEBUG_PARTY_RELOADED = "Reloaded party %s";
	public static final String DEBUG_PARTY_GET_DATABASE = "Got party %s from database";
	public static final String DEBUG_PARTY_GET_LIST = "Got party %s from list";
	public static final String DEBUG_PARTY_TIMEOUT_CHANGE_LEADER = "Party %s leader is changed into %s due to timeout";
	public static final String DEBUG_PARTY_TIMEOUT_DELETE = "Party %s timeout deleted due to player %s quit";
	public static final String DEBUG_PARTY_TIMEOUT_KICK = "Player %s timeout kicked from the party %s";
	public static final String DEBUG_PARTY_TIMEOUT_STOP = "Stopped player %s timeout";
	
	public static final String DEBUG_PLAYER_PARTY_JOIN = "Added player %s into party %s (p-uuid: %s)";
	public static final String DEBUG_PLAYER_PARTY_LEAVE = "Removed player %s from party %s (p-uuid: %s)";
	public static final String DEBUG_PLAYER_RELOADED = "Reloaded player %s";
	public static final String DEBUG_PLAYER_CLEANUP = "Cleaning up player %s (p-uuid: %s)";
	public static final String DEBUG_PLAYER_GET_DATABASE = "Got player %s from database (party: %s) (p-uuid: %s)";
	public static final String DEBUG_PLAYER_GET_LIST = "Got player %s from list (party: %s) (p-uuid: %s)";
	public static final String DEBUG_PLAYER_GET_NEW = "Got a new player %s (p-uuid: %s)";
	public static final String DEBUG_PLAYER_JOIN = "%s entered in the game (party: %s)";
	public static final String DEBUG_PLAYER_JOIN_DEFAULTJOIN = "%s joined without party, default join into party %s";
	public static final String DEBUG_PLAYER_JOIN_DEFAULTFAIL = "Failed to load default party %s";
	
	public static final String DEBUG_PLUGIN_BUNGEECORD_MODE = "BungeeCord mode enabled";
	public static final String DEBUG_PLUGIN_RELOADING = "Reloading plugin...";
	public static final String DEBUG_SYNC_DIFFERENT_STORAGE = "Found a different storage in BungeeCord (%s), you are using %s";
	
	public static final String DEBUG_TASK_ASK_COOLDOWN_EXPIRED = "Ask cooldown expired for player %s";
	public static final String DEBUG_TASK_CHAT_EXPIRED = "Chat cooldown expired for player %s";
	public static final String DEBUG_TASK_HOME_DENIED_FIGHT = "Denied home teleport of player %s due to fight";
	public static final String DEBUG_TASK_HOME_DENIED_MOVING = "Denied home teleport of player %s due to moving";
	public static final String DEBUG_TASK_HOME_EXPIRED = "Home cooldown expired for player %s";
	public static final String DEBUG_TASK_SETHOME_EXPIRED = "Set home cooldown expired for player %s";
	public static final String DEBUG_TASK_INVITE_COOLDOWN_EXPIRED = "Invite cooldown expired for player %s";
	public static final String DEBUG_TASK_INVITE_COOLDOWN_ON_LEAVE_EXPIRED = "Invite on leave cooldown expired for player %s";
	public static final String DEBUG_TASK_RENAME_EXPIRED = "Rename cooldown expired for party %s";
	public static final String DEBUG_TASK_TELEPORT_DONE = "Teleported the player %s to the party home";
	public static final String DEBUG_TASK_TELEPORT_EXPIRED = "Teleport cooldown expired for player %s";
	public static final String DEBUG_TASK_TELEPORT_DENIED_FIGHT = "Denied teleport cmd of player %s due to fight";
	public static final String DEBUG_TASK_TELEPORT_DENIED_MOVING = "Denied teleport cmd of player %s due to moving";
	
	public static final String DEBUG_TELEPORT_ASYNC = "Failed to async teleport the player %s";
}
