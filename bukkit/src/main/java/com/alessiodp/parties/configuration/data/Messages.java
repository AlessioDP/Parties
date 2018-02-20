package com.alessiodp.parties.configuration.data;

import java.util.ArrayList;
import java.util.List;

public class Messages {
	
	// Parties messages
	public static String PARTIES_UPDATEAVAILABLE;
	
	public static String PARTIES_COMMON_INVALIDCMD;
	public static String PARTIES_COMMON_CONFIGRELOAD;
	public static String PARTIES_COMMON_NOTINPARTY;
	public static String PARTIES_COMMON_PARTYNOTFOUND;
	public static String PARTIES_COMMON_PARTYFULL;
	
	public static String PARTIES_PERM_NOPERM;
	public static String PARTIES_PERM_NORANK;
	
	
	// Main commands messages
	public static String MAINCMD_ACCEPT_BROADCAST;
	public static String MAINCMD_ACCEPT_ACCEPTED;
	public static String MAINCMD_ACCEPT_ACCEPTRECEIPT;
	public static String MAINCMD_ACCEPT_ALREADYINPARTY;
	public static String MAINCMD_ACCEPT_NOINVITE;
	public static String MAINCMD_ACCEPT_NOEXISTS;
	
	public static String MAINCMD_CHAT_ENABLED;
	public static String MAINCMD_CHAT_DISABLED;
	public static String MAINCMD_CHAT_WRONGCMD;
	
	public static String MAINCMD_CREATE_CREATED;
	public static String MAINCMD_CREATE_CREATEDFIXED;
	public static String MAINCMD_CREATE_NAMEEXISTS;
	public static String MAINCMD_CREATE_ALREADYINPARTY;
	public static String MAINCMD_CREATE_NAMETOOLONG;
	public static String MAINCMD_CREATE_NAMETOOSHORT;
	public static String MAINCMD_CREATE_INVALIDNAME;
	public static String MAINCMD_CREATE_CENSORED;
	public static String MAINCMD_CREATE_WRONGCMD;
	
	public static String MAINCMD_DELETE_DELETED;
	public static String MAINCMD_DELETE_DELETEDSILENTLY;
	public static String MAINCMD_DELETE_BROADCAST;
	public static String MAINCMD_DELETE_WRONGCMD;
	
	public static String MAINCMD_DENY_DENIED;
	public static String MAINCMD_DENY_DENYRECEIPT;
	public static String MAINCMD_DENY_NOINVITE;
	public static String MAINCMD_DENY_NOEXISTS;
	
	public static String MAINCMD_IGNORE_START;
	public static String MAINCMD_IGNORE_STOP;
	public static String MAINCMD_IGNORE_LIST_HEADER;
	public static String MAINCMD_IGNORE_LIST_PARTYPREFIX;
	public static String MAINCMD_IGNORE_LIST_SEPARATOR;
	public static String MAINCMD_IGNORE_LIST_EMPTY;
	public static String MAINCMD_IGNORE_WRONGCMD;
	
	public static List<String> MAINCMD_INFO_CONTENT;
	public static String MAINCMD_INFO_LIST_ONLINEPREFIX;
	public static String MAINCMD_INFO_LIST_OFFLINEPREFIX;
	public static String MAINCMD_INFO_LIST_SEPARATOR;
	public static String MAINCMD_INFO_LIST_EMPTY;
	public static String MAINCMD_INFO_LIST_UNKNOWN;
	public static String MAINCMD_INFO_LIST_MISSING;
	public static String MAINCMD_INFO_WRONGCMD;
	
	public static String MAINCMD_INVITE_SENT;
	public static String MAINCMD_INVITE_PLAYERINVITED;
	public static String MAINCMD_INVITE_TIMEOUT_NORESPONSE;
	public static String MAINCMD_INVITE_TIMEOUT_TIMEOUT;
	public static String MAINCMD_INVITE_REVOKE_SENT;
	public static String MAINCMD_INVITE_REVOKE_REVOKED;
	public static String MAINCMD_INVITE_PLAYEROFFLINE;
	public static String MAINCMD_INVITE_PLAYERNOPERM;
	public static String MAINCMD_INVITE_PLAYERINPARTY;
	public static String MAINCMD_INVITE_ALREADYINVITED;
	public static String MAINCMD_INVITE_WRONGCMD;
	
	public static String MAINCMD_KICK_SENT;
	public static String MAINCMD_KICK_PLAYERKICKED;
	public static String MAINCMD_KICK_BROADCAST;
	public static String MAINCMD_KICK_BROADCAST_DISBAND;
	public static String MAINCMD_KICK_PLAYERHIGHERRANK;
	public static String MAINCMD_KICK_PLAYERNOTINPARTY;
	public static String MAINCMD_KICK_PLAYERNOTINPARTY_OTHER;
	public static List<String> MAINCMD_KICK_CONFLICT_CONTENT;
	public static String MAINCMD_KICK_CONFLICT_PLAYER;
	public static String MAINCMD_KICK_WRONGCMD;
	
	public static String MAINCMD_LEAVE_LEFT;
	public static String MAINCMD_LEAVE_BROADCAST;
	public static String MAINCMD_LEAVE_DISBANDED;
	
	public static String MAINCMD_MIGRATE_INFO;
	public static String MAINCMD_MIGRATE_COMPLETED;
	public static String MAINCMD_MIGRATE_FAILED_DBOFFLINE;
	public static String MAINCMD_MIGRATE_FAILED_FAILED;
	public static String MAINCMD_MIGRATE_FAILED_SAMEDB;
	public static String MAINCMD_MIGRATE_WRONGDB;
	public static String MAINCMD_MIGRATE_WRONGCMD;
	
	public static String MAINCMD_P_COOLDOWN;
	public static String MAINCMD_P_WRONGCMD;
	
	public static String MAINCMD_RANK_CHANGED;
	public static String MAINCMD_RANK_BROADCAST;
	public static String MAINCMD_RANK_WRONGRANK;
	public static String MAINCMD_RANK_SAMERANK;
	public static String MAINCMD_RANK_LOWRANK;
	public static String MAINCMD_RANK_TOHIGHERRANK;
	public static String MAINCMD_RANK_CHANGINGYOURSELF;
	public static String MAINCMD_RANK_PLAYERNOTINPARTY;
	public static String MAINCMD_RANK_PLAYERNOTINPARTY_OTHER;
	public static String MAINCMD_RANK_WRONGCMD;
	
	public static String MAINCMD_RENAME_RENAMED;
	public static String MAINCMD_RENAME_BROADCAST;
	public static String MAINCMD_RENAME_WRONGCMD;
	public static String MAINCMD_RENAME_WRONGCMD_ADMIN;
	
	public static String MAINCMD_SPY_ENABLED;
	public static String MAINCMD_SPY_DISABLED;

	
	// Additional commands messages
	public static String ADDCMD_CLAIM_CLAIMED;
	public static String ADDCMD_CLAIM_REMOVED;
	public static String ADDCMD_CLAIM_NOMANAGER;
	public static String ADDCMD_CLAIM_CLAIMNOEXISTS;
	public static String ADDCMD_CLAIM_WRONGCMD;
	
	public static String ADDCMD_COLOR_INFO;
	public static String ADDCMD_COLOR_EMPTY;
	public static String ADDCMD_COLOR_CHANGED;
	public static String ADDCMD_COLOR_REMOVED;
	public static String ADDCMD_COLOR_BROADCAST;
	public static String ADDCMD_COLOR_WRONGCOLOR;
	public static String ADDCMD_COLOR_WRONGCMD;
	
	public static String ADDCMD_DESC_CHANGED;
	public static String ADDCMD_DESC_REMOVED;
	public static String ADDCMD_DESC_BROADCAST;
	public static String ADDCMD_DESC_INVALID;
	public static String ADDCMD_DESC_CENSORED;
	public static String ADDCMD_DESC_WRONGCMD;
	
	public static String ADDCMD_HOME_TELEPORTED;
	public static String ADDCMD_HOME_TELEPORTIN;
	public static String ADDCMD_HOME_TELEPORTDENIED;
	public static String ADDCMD_HOME_NOHOME;
	public static String ADDCMD_HOME_NOEXISTS;
	public static String ADDCMD_HOME_WRONGCMD;
	public static String ADDCMD_HOME_WRONGCMD_ADMIN;
	
	
	public static String ADDCMD_JOIN_JOINED;
	public static String ADDCMD_JOIN_PLAYERJOINED;
	public static String ADDCMD_JOIN_ALREADYINPARTY;
	public static String ADDCMD_JOIN_WRONGPASSWORD;
	public static String ADDCMD_JOIN_WRONGCMD;
	
	public static String ADDCMD_LIST_HEADER;
	public static String ADDCMD_LIST_FOOTER;
	public static String ADDCMD_LIST_NOONE;
	public static String ADDCMD_LIST_FORMATPARTY;
	public static String ADDCMD_LIST_WRONGCMD;
	
	public static String ADDCMD_MOTD_CHANGED;
	public static String ADDCMD_MOTD_REMOVED;
	public static String ADDCMD_MOTD_BROADCAST;
	public static List<String> ADDCMD_MOTD_CONTENT;
	public static String ADDCMD_MOTD_INVALID;
	public static String ADDCMD_MOTD_CENSORED;
	public static String ADDCMD_MOTD_WRONGCMD;
	
	public static String ADDCMD_NOTIFY_ON;
	public static String ADDCMD_NOTIFY_OFF;
	
	public static String ADDCMD_PASSWORD_CHANGED;
	public static String ADDCMD_PASSWORD_REMOVED;
	public static String ADDCMD_PASSWORD_BROADCAST;
	public static String ADDCMD_PASSWORD_INVALID;
	public static String ADDCMD_PASSWORD_WRONGCMD;
	
	public static String ADDCMD_PREFIX_CHANGED;
	public static String ADDCMD_PREFIX_REMOVED;
	public static String ADDCMD_PREFIX_BROADCAST;
	public static String ADDCMD_PREFIX_INVALID;
	public static String ADDCMD_PREFIX_CENSORED;
	public static String ADDCMD_PREFIX_WRONGCMD;
	
	public static String ADDCMD_SETHOME_CHANGED;
	public static String ADDCMD_SETHOME_REMOVED;
	public static String ADDCMD_SETHOME_BROADCAST;
	public static String ADDCMD_SETHOME_WRONGCMD;
	
	public static String ADDCMD_SUFFIX_CHANGED;
	public static String ADDCMD_SUFFIX_REMOVED;
	public static String ADDCMD_SUFFIX_BROADCAST;
	public static String ADDCMD_SUFFIX_INVALID;
	public static String ADDCMD_SUFFIX_CENSORED;
	public static String ADDCMD_SUFFIX_WRONGCMD;
	
	public static String ADDCMD_TELEPORT_TELEPORTING;
	public static String ADDCMD_TELEPORT_TELEPORTED;
	public static String ADDCMD_TELEPORT_COOLDOWN;
	
	public static String ADDCMD_VAULT_NOMONEY_CLAIM;
	public static String ADDCMD_VAULT_NOMONEY_COLOR;
	public static String ADDCMD_VAULT_NOMONEY_CREATE;
	public static String ADDCMD_VAULT_NOMONEY_DESC;
	public static String ADDCMD_VAULT_NOMONEY_HOME;
	public static String ADDCMD_VAULT_NOMONEY_JOIN;
	public static String ADDCMD_VAULT_NOMONEY_MOTD;
	public static String ADDCMD_VAULT_NOMONEY_PREFIX;
	public static String ADDCMD_VAULT_NOMONEY_SETHOME;
	public static String ADDCMD_VAULT_NOMONEY_SUFFIX;
	public static String ADDCMD_VAULT_NOMONEY_TELEPORT;
	public static String ADDCMD_VAULT_CONFIRM_WARNONBUY;
	public static String ADDCMD_VAULT_CONFIRM_CONFIRMED;
	public static String ADDCMD_VAULT_CONFIRM_NOCMD;
	
	
	// Other messages
	public static String OTHER_FOLLOW_WORLD;
	public static String OTHER_FOLLOW_SERVER;
	
	public static String OTHER_FRIENDLYFIRE_CANTHIT;
	public static String OTHER_FRIENDLYFIRE_WARN;
	
	public static String OTHER_EXP_VANILLA_GAINED;
	public static String OTHER_EXP_SKILLAPI_GAINED;
	public static String OTHER_EXP_VANILLA_GAINEDFROMOTHER;
	public static String OTHER_EXP_SKILLAPI_GAINEDFROMOTHER;
	
	public static String OTHER_FIXED_DEFAULTJOIN;
	
	public static String OTHER_JOINLEAVE_SERVERJOIN;
	public static String OTHER_JOINLEAVE_SERVERLEAVE;
	
	
	// Help messages
	public static String HELP_HEADER;
	public static String HELP_FOOTER;
	public static List<String> HELP_CONSOLEHELP;
	
	public static String HELP_MAINCMD_HELP;
	public static String HELP_MAINCMD_ACCEPT;
	public static String HELP_MAINCMD_CHAT;
	public static String HELP_MAINCMD_CREATE;
	public static String HELP_MAINCMD_CREATE_FIXED;
	public static String HELP_MAINCMD_DELETE;
	public static String HELP_MAINCMD_DENY;
	public static String HELP_MAINCMD_IGNORE;
	public static String HELP_MAINCMD_INFO;
	public static String HELP_MAINCMD_INVITE;
	public static String HELP_MAINCMD_KICK;
	public static String HELP_MAINCMD_LEAVE;
	public static String HELP_MAINCMD_MIGRATE;
	public static String HELP_MAINCMD_P;
	public static String HELP_MAINCMD_RANK;
	public static String HELP_MAINCMD_RELOAD;
	public static String HELP_MAINCMD_RENAME;
	public static String HELP_MAINCMD_RENAME_OTHERS;
	public static String HELP_MAINCMD_SPY;
	
	public static String HELP_ADDCMD_CLAIM;
	public static String HELP_ADDCMD_COLOR;
	public static String HELP_ADDCMD_DESC;
	public static String HELP_ADDCMD_HOME;
	public static String HELP_ADDCMD_HOME_OTHERS;
	public static String HELP_ADDCMD_JOIN;
	public static String HELP_ADDCMD_LIST;
	public static String HELP_ADDCMD_MOTD;
	public static String HELP_ADDCMD_PASSWORD;
	public static String HELP_ADDCMD_PREFIX;
	public static String HELP_ADDCMD_SETHOME;
	public static String HELP_ADDCMD_SUFFIX;
	public static String HELP_ADDCMD_TELEPORT;
	
	
	public Messages() {
		loadDefaults();
	}
	
	public void loadDefaults() {
		// Parties messages
		PARTIES_UPDATEAVAILABLE = "&9New version of Parties found: %version% (Current: %thisversion%)";
		
		PARTIES_COMMON_INVALIDCMD = "&cInvalid command";
		PARTIES_COMMON_CONFIGRELOAD = "&aConfiguration reloaded";
		PARTIES_COMMON_NOTINPARTY = "&cYou are not in a party";
		PARTIES_COMMON_PARTYNOTFOUND = "&cThe party %party% doesn't exist";
		PARTIES_COMMON_PARTYFULL = "&cThe party is full!";
		
		PARTIES_PERM_NOPERM = "&cYou do not have access to that command";
		PARTIES_PERM_NORANK = "&cYou need to be %rank_name% to perform that command";
		
		
		// Main commands messages
		MAINCMD_ACCEPT_BROADCAST = "&b&l%player% joined the party";
		MAINCMD_ACCEPT_ACCEPTED = "&aYou accepted the party invite";
		MAINCMD_ACCEPT_ACCEPTRECEIPT = "&a%player% accepted your invite";
		MAINCMD_ACCEPT_ALREADYINPARTY = "&cYou are already in a party!";
		MAINCMD_ACCEPT_NOINVITE = "&cYou have not pending invitation";
		MAINCMD_ACCEPT_NOEXISTS = "&cThe invite doesn't exist anymore";
		
		MAINCMD_CHAT_ENABLED = "&aChat set to party";
		MAINCMD_CHAT_DISABLED = "&aChat set to public";
		MAINCMD_CHAT_WRONGCMD = "&cWrong variables: Type &7/party chat [on/off]";
		
		MAINCMD_CREATE_CREATED = "&l&bYou have created the party %party%\n&r&bType &7/party invite &bto invite your friends";
		MAINCMD_CREATE_CREATEDFIXED = "&l&bYou have created the fixed party %party%";
		MAINCMD_CREATE_NAMEEXISTS = "&cThe party name %party% already exists, choose a different name";
		MAINCMD_CREATE_ALREADYINPARTY = "&cYou are already in a party!";
		MAINCMD_CREATE_NAMETOOLONG = "&cThe party name is too long!";
		MAINCMD_CREATE_NAMETOOSHORT = "&cThe party name is too short!";
		MAINCMD_CREATE_INVALIDNAME = "&cInvalid characters. Use: a-Z or 0-9.";
		MAINCMD_CREATE_CENSORED = "&cThe party name contains censored words!";
		MAINCMD_CREATE_WRONGCMD = "&cWrong variables: Type &7/party create <name>";
		
		MAINCMD_DELETE_DELETED = "&aParty %party% deleted";
		MAINCMD_DELETE_DELETEDSILENTLY = "&aParty %party% deleted silently";
		MAINCMD_DELETE_BROADCAST = "&6&lYour party has been deleted";
		MAINCMD_DELETE_WRONGCMD = "&cWrong variables: Type &7/party delete <party> [silent]";
		
		MAINCMD_DENY_DENIED = "&7You denied the party invite";
		MAINCMD_DENY_DENYRECEIPT = "&7%player% denied your invite";
		MAINCMD_DENY_NOINVITE = "&cYou have not pending invitation";
		MAINCMD_DENY_NOEXISTS = "&cThe invite doesn't exist anymore";
		
		MAINCMD_IGNORE_START = "&7Ignoring %party% invites";
		MAINCMD_IGNORE_STOP = "&7You stopped ignoring %party%";
		MAINCMD_IGNORE_LIST_HEADER = "&b&lList ignored parties:";
		MAINCMD_IGNORE_LIST_PARTYPREFIX = "&c";
		MAINCMD_IGNORE_LIST_SEPARATOR = "&7, ";
		MAINCMD_IGNORE_LIST_EMPTY = "&7None";
		MAINCMD_IGNORE_WRONGCMD = "&cWrong variables: Type &7/party ignore [party]";
		
		MAINCMD_INFO_CONTENT = new ArrayList<String>();
		MAINCMD_INFO_CONTENT.add("&b============ &l%party%'s Info &r&b============");
		MAINCMD_INFO_CONTENT.add("&b&lDescription&r&7: %desc%");
		MAINCMD_INFO_CONTENT.add("&b&lMotd&r&7: %motd%");
		MAINCMD_INFO_CONTENT.add("&bLeader&7: %leader%");
		MAINCMD_INFO_CONTENT.add("&bMods&7: %list_moderator%");
		MAINCMD_INFO_CONTENT.add("&bMembers&7: %list_member%");
		MAINCMD_INFO_CONTENT.add("&bOnline players&7: %onlinenumber%");
		MAINCMD_INFO_LIST_ONLINEPREFIX = "&b";
		MAINCMD_INFO_LIST_OFFLINEPREFIX = "&7";
		MAINCMD_INFO_LIST_SEPARATOR = "&7, ";
		MAINCMD_INFO_LIST_EMPTY = "&7Nobody";
		MAINCMD_INFO_LIST_UNKNOWN = "&6Someone";
		MAINCMD_INFO_LIST_MISSING = "&7Miss";
		MAINCMD_INFO_WRONGCMD = "&cWrong variables: Type &7/party info [party]";
		
		MAINCMD_INVITE_SENT = "&bYou invited %player% in your party";
		MAINCMD_INVITE_PLAYERINVITED = "&b%player% has invited you in his party\n&bTo accept/deny invitation type &7/party <accept/deny>";
		MAINCMD_INVITE_TIMEOUT_NORESPONSE = "&7%player% didn't accept the party invite";
		MAINCMD_INVITE_TIMEOUT_TIMEOUT = "&7You didn't accept the party invite";
		MAINCMD_INVITE_REVOKE_SENT = "&7Revoked invite sent to %player%";
		MAINCMD_INVITE_REVOKE_REVOKED = "&7Invite received from %party% has been revoked";
		MAINCMD_INVITE_PLAYEROFFLINE = "&cYou can invite only online players";
		MAINCMD_INVITE_PLAYERNOPERM = "&c%player% doesn't have the permission to join";
		MAINCMD_INVITE_PLAYERINPARTY = "&c%player% is already in a party";
		MAINCMD_INVITE_ALREADYINVITED = "&c%player% was already invited";
		MAINCMD_INVITE_WRONGCMD = "&cWrong variables: Type &7/party invite <name>";
		
		MAINCMD_KICK_SENT = "&aYou kicked %player% from your party!";
		MAINCMD_KICK_PLAYERKICKED = "&bYou have been kicked from %party%";
		MAINCMD_KICK_BROADCAST = "&b&l%player% has been kicked from the party";
		MAINCMD_KICK_BROADCAST_DISBAND = "&b&lThe party has been disbanded because the leader got kicked";
		MAINCMD_KICK_PLAYERHIGHERRANK = "&cYou cannot kick your superior!";
		MAINCMD_KICK_PLAYERNOTINPARTY = "&c%player% is not in your party";
		MAINCMD_KICK_PLAYERNOTINPARTY_OTHER = "&c%player% is not in a party";
		MAINCMD_KICK_CONFLICT_CONTENT = new ArrayList<String>();
		MAINCMD_KICK_CONFLICT_CONTENT.add("&cWe have found some players with that name:");
		MAINCMD_KICK_CONFLICT_CONTENT.add("%list_players%");
		MAINCMD_KICK_CONFLICT_CONTENT.add("&cUse '&7/party kick <username> <number>&c' to kick the right player");
		MAINCMD_KICK_CONFLICT_PLAYER = "&7[%number%] &6%username%&7 [&b%party%&7]: last login %time% %date%";
		MAINCMD_KICK_WRONGCMD = "&cWrong variables: Type &7/party kick <name>";
		
		MAINCMD_LEAVE_LEFT = "&b&lYou left the party %party%";
		MAINCMD_LEAVE_BROADCAST = "&b&l%player% left the party";
		MAINCMD_LEAVE_DISBANDED = "&6&lThe party has been disbanded because the leader left";
		
		MAINCMD_MIGRATE_INFO = "&aYou are currently using: %database%\n&aYou can migrate with: &7/party migrate <from> <to>";
		MAINCMD_MIGRATE_COMPLETED = "&aCopied data from %database_from% to %database_to%";
		MAINCMD_MIGRATE_FAILED_DBOFFLINE = "&cThe %database% database must be online!";
		MAINCMD_MIGRATE_FAILED_FAILED = "&cDatabase migration from %database_from% to %database_to% failed!";
		MAINCMD_MIGRATE_FAILED_SAMEDB = "&cYou need to choose 2 different databases!";
		MAINCMD_MIGRATE_WRONGDB = "&cDatabase not found. You can select: YAML, MySQL, SQLite!";
		MAINCMD_MIGRATE_WRONGCMD = "&cWrong variables: Type &7/party migrate <from> <to>";
		
		MAINCMD_P_COOLDOWN = "&cYou still have to wait %seconds% seconds";
		MAINCMD_P_WRONGCMD = "&cWrong variables: Type &7/p <message>";
		
		MAINCMD_RANK_CHANGED = "&a%player% rank changed into %rank_name%";
		MAINCMD_RANK_BROADCAST = "";
		MAINCMD_RANK_WRONGRANK = "&cRank '%rank_typed%' doesn't exist!";
		MAINCMD_RANK_SAMERANK = "&c%player% is already %rank_name%!";
		MAINCMD_RANK_LOWRANK = "&cYou cannot edit players with an equivalent or higher rank than yours!";
		MAINCMD_RANK_TOHIGHERRANK = "&cYou cannot promote to a rank equivalent or higher than yours!";
		MAINCMD_RANK_CHANGINGYOURSELF = "&cYou cannot change yourself rank!";
		MAINCMD_RANK_PLAYERNOTINPARTY = "&c%player% is not in your party";
		MAINCMD_RANK_PLAYERNOTINPARTY_OTHER = "&c%player% doesn't have a party";
		MAINCMD_RANK_WRONGCMD = "&cWrong variables: Type &7/party rank <player> <rank>";
		
		MAINCMD_RENAME_RENAMED = "&aThe party %old% has been renamed into %party%";
		MAINCMD_RENAME_BROADCAST = "&6Your party has been renamed into %party%!";
		MAINCMD_RENAME_WRONGCMD = "&cWrong variables: Type &7/party rename <newname>";
		MAINCMD_RENAME_WRONGCMD_ADMIN = "&cWrong variables: Type &7/party rename <party> <newname>";
		
		MAINCMD_SPY_ENABLED = "&7Now you are a spy!";
		MAINCMD_SPY_DISABLED = "&7You are no longer a spy";

		
		// Additional commands messages
		ADDCMD_CLAIM_CLAIMED = "&aGranted permission to the party";
		ADDCMD_CLAIM_REMOVED = "&aRemoved permission to the party";
		ADDCMD_CLAIM_NOMANAGER = "&cYou need to be the manager of the claim";
		ADDCMD_CLAIM_CLAIMNOEXISTS = "&cClaim not found";
		ADDCMD_CLAIM_WRONGCMD = "&cWrong variables: Type &7/party claim <permission>\n&cPermissions: trust, container & access";
		
		ADDCMD_COLOR_INFO = "&bYour party color is: %color_code%%color_command%";
		ADDCMD_COLOR_EMPTY = "&bYour party doesn't have a color";
		ADDCMD_COLOR_CHANGED = "&bParty color changed into %color_command%";
		ADDCMD_COLOR_REMOVED = "&bParty color removed";
		ADDCMD_COLOR_BROADCAST = "";
		ADDCMD_COLOR_WRONGCOLOR = "&cColor not found. You can select: red, green or special!";
		ADDCMD_COLOR_WRONGCMD = "&cWrong variables: Type &7/party color <color>";
		
		ADDCMD_DESC_CHANGED = "&bParty description changed";
		ADDCMD_DESC_REMOVED = "&bParty description removed";
		ADDCMD_DESC_BROADCAST = "";
		ADDCMD_DESC_INVALID = "&cInvalid characters. Use: a-Z or 0-9. Min 3 and max 16 chars.";
		ADDCMD_DESC_CENSORED = "&cThe description contains censored words!";
		ADDCMD_DESC_WRONGCMD = "&cWrong variables: Type &7/party desc <description/remove>";
		
		ADDCMD_HOME_TELEPORTED = "&7Teleported to the party home";
		ADDCMD_HOME_TELEPORTIN = "&7You will be teleported in %time% seconds...";
		ADDCMD_HOME_TELEPORTDENIED = "&7Teleport denied";
		ADDCMD_HOME_NOHOME = "&cThere isn't a home yet";
		ADDCMD_HOME_NOEXISTS = "&cThe party %party% doesn't exist";
		ADDCMD_HOME_WRONGCMD = "&cWrong variables: Type &7/party home";
		ADDCMD_HOME_WRONGCMD_ADMIN = "&cWrong variables: Type &7/party home [party]";
		
		
		ADDCMD_JOIN_JOINED = "&aYou joined the party %party%";
		ADDCMD_JOIN_PLAYERJOINED = "&b&l%player% joined in the party";
		ADDCMD_JOIN_ALREADYINPARTY = "&cYou are already in a party!";
		ADDCMD_JOIN_WRONGPASSWORD = "&cWrong password!";
		ADDCMD_JOIN_WRONGCMD = "&cWrong variables: Type &7/party join <party> [password]";
		
		ADDCMD_LIST_HEADER = "&b============ &lOnline Parties List &r&b============";
		ADDCMD_LIST_FOOTER = "&b================ &lPage %page% of %maxpages% &r&b================";
		ADDCMD_LIST_NOONE = "&7No one";
		ADDCMD_LIST_FORMATPARTY = "&b%party% &7[&6Online %onlinenumber%&7] %desc%";
		ADDCMD_LIST_WRONGCMD = "&cWrong variables: Type &7/party list [page]";
		
		ADDCMD_MOTD_CHANGED = "&bParty MOTD changed";
		ADDCMD_MOTD_REMOVED = "&bParty MOTD removed";
		ADDCMD_MOTD_BROADCAST = "";
		ADDCMD_MOTD_CONTENT = new ArrayList<String>();
		ADDCMD_MOTD_CONTENT.add("&bParty MOTD:");
		ADDCMD_MOTD_CONTENT.add("&b%motd%");
		ADDCMD_MOTD_INVALID = "&cInvalid characters. You can also use '. , /'. Min 3 and max 100 chars.";
		ADDCMD_MOTD_CENSORED = "&cThe MOTD contains censored words!";
		ADDCMD_MOTD_WRONGCMD = "&cWrong variables: Type &7/party motd <motd/remove>";
		
		ADDCMD_NOTIFY_ON = "&7You have disabled notifications!";
		ADDCMD_NOTIFY_OFF = "&7You have enabled notifications!";
		
		ADDCMD_PASSWORD_CHANGED = "&aParty password changed";
		ADDCMD_PASSWORD_REMOVED = "&aParty password removed";
		ADDCMD_PASSWORD_BROADCAST = "";
		ADDCMD_PASSWORD_INVALID = "&cInvalid characters. Use: a-Z or 0-9. Min 1 and max 16 chars.";
		ADDCMD_PASSWORD_WRONGCMD = "&cWrong variables: Type &7/party password <password>";
		
		ADDCMD_PREFIX_CHANGED = "&bParty prefix changed into %prefix%";
		ADDCMD_PREFIX_REMOVED = "&bParty prefix removed";
		ADDCMD_PREFIX_BROADCAST = "";
		ADDCMD_PREFIX_INVALID = "&cInvalid characters. Use: a-Z or 0-9. Min 3 and max 6 chars.";
		ADDCMD_PREFIX_CENSORED = "&cThe prefix contains censored words!";
		ADDCMD_PREFIX_WRONGCMD = "&cWrong variables: Type &7/party prefix <prefix/remove>";
		
		ADDCMD_SETHOME_CHANGED = "";
		ADDCMD_SETHOME_REMOVED = "&bParty home removed";
		ADDCMD_SETHOME_BROADCAST = "&aThe party has a new home!";
		ADDCMD_SETHOME_WRONGCMD = "&cWrong variables: Type &7/party sethome [remove]";
		
		ADDCMD_SUFFIX_CHANGED = "&bParty suffix changed into %suffix%";
		ADDCMD_SUFFIX_REMOVED = "&bParty suffix removed";
		ADDCMD_SUFFIX_BROADCAST = "";
		ADDCMD_SUFFIX_INVALID = "&cInvalid characters. Use: a-Z or 0-9. Min 3 and max 6 chars.";
		ADDCMD_SUFFIX_CENSORED = "&cThe suffix contains censored words!";
		ADDCMD_SUFFIX_WRONGCMD = "&cWrong variables: Type &7/party suffix <suffix/remove>";
		
		ADDCMD_TELEPORT_TELEPORTING = "&7Teleporting your party here!";
		ADDCMD_TELEPORT_TELEPORTED = "&bTeleported to %player%";
		ADDCMD_TELEPORT_COOLDOWN = "&cYou have to wait %seconds% seconds!";
		
		ADDCMD_VAULT_NOMONEY_CLAIM = "&cYou don't have enough money to perform a claim [%price%$]";
		ADDCMD_VAULT_NOMONEY_COLOR = "&cYou don't have enough money to perform the color command [%price%$]";
		ADDCMD_VAULT_NOMONEY_CREATE = "&cYou don't have enough money to create a party [%price%$]";
		ADDCMD_VAULT_NOMONEY_DESC = "&cYou don't have enough money to set the description [%price%$]";
		ADDCMD_VAULT_NOMONEY_HOME = "&cYou don't have enough money to use the home command [%price%$]";
		ADDCMD_VAULT_NOMONEY_JOIN = "&cYou don't have enough money to join a party [%price%$]";
		ADDCMD_VAULT_NOMONEY_MOTD = "&cYou don't have enough money to set the MOTD [%price%$]";
		ADDCMD_VAULT_NOMONEY_PREFIX = "&cYou don't have enough money to set the prefix [%price%$]";
		ADDCMD_VAULT_NOMONEY_SETHOME = "&cYou don't have enough money to set the home [%price%$]";
		ADDCMD_VAULT_NOMONEY_SUFFIX = "&cYou don't have enough money to set the suffix [%price%$]";
		ADDCMD_VAULT_NOMONEY_TELEPORT = "&cYou don't have enough money to perform a teleport [%price%$]";
		ADDCMD_VAULT_CONFIRM_WARNONBUY = "&aCommand '%cmd%' costs %price%$.\nType &7/party confirm&a to use it.";
		ADDCMD_VAULT_CONFIRM_CONFIRMED = "&aPerforming the command.";
		ADDCMD_VAULT_CONFIRM_NOCMD = "&cThere is no command to confirm";
		
		
		// Other messages
		OTHER_FOLLOW_WORLD = "&7Following %player% in %world%";
		OTHER_FOLLOW_SERVER = "&7Following party in %server%";
		
		OTHER_FRIENDLYFIRE_CANTHIT = "&cYou can't hit your partymates";
		OTHER_FRIENDLYFIRE_WARN = "&c%player% tried to hit %victim%!";
		
		OTHER_EXP_VANILLA_GAINED = "&bGained %exp% experience for killing a %mob%";
		OTHER_EXP_SKILLAPI_GAINED = "&b%player% has killed a %mob%, you gained %exp% experience";
		OTHER_EXP_VANILLA_GAINEDFROMOTHER = "&bGained %exp% SkillAPI experience for killing a %mob%";
		OTHER_EXP_SKILLAPI_GAINEDFROMOTHER = "&b%player% has killed a %mob%, you gained %exp% SkillAPI experience";
		
		OTHER_FIXED_DEFAULTJOIN = "&bYou entered into %party%";
		
		OTHER_JOINLEAVE_SERVERJOIN = "&b%player% is online!";
		OTHER_JOINLEAVE_SERVERLEAVE = "&7%player% is offline!";
		
		
		// Help messages
		HELP_HEADER = "&b================= &lParty Help %page%/%maxpages% &r&b=================";
		HELP_FOOTER = "";
		HELP_CONSOLEHELP = new ArrayList<String>();
		HELP_CONSOLEHELP.add("You can only make these commands:");
		HELP_CONSOLEHELP.add("/party migrate - Copy database into a new one");
		HELP_CONSOLEHELP.add("/party reload - Reload the configuration");
		
		HELP_MAINCMD_HELP = "&b/party help [page] &7- Show help pages";
		HELP_MAINCMD_ACCEPT = "&b/party accept &7- Accept a party invitation";
		HELP_MAINCMD_CHAT = "&b/party chat [on/off] &7- Toggle the party chat";
		HELP_MAINCMD_CREATE = "&b/party create <name> &7- Create a new party";
		HELP_MAINCMD_CREATE_FIXED = "&b/party create <name> [fixed] &7- Create a new party";
		HELP_MAINCMD_DELETE = "&b/party delete <party> &7- Delete the party";
		HELP_MAINCMD_DENY = "&b/party deny &7- Deny a party invitation";
		HELP_MAINCMD_IGNORE = "&b/party ignore [party] &7- Add/remove/show parties ignored";
		HELP_MAINCMD_INFO = "&b/party info [party] &7- Show party information";
		HELP_MAINCMD_INVITE = "&b/party invite <player> &7- Invite a player to your party";
		HELP_MAINCMD_KICK = "&b/party kick <player> &7- Kick a player from your party";
		HELP_MAINCMD_LEAVE = "&b/party leave &7- Leave your party";
		HELP_MAINCMD_MIGRATE = "&b/party migrate <from> <to> &7- Copy database into a new one";
		HELP_MAINCMD_P = "&b/p <message> &7- Send a message to the party";
		HELP_MAINCMD_RANK = "&b/party rank <player> <rank> &7- Change rank of the player";
		HELP_MAINCMD_RELOAD = "&b/party reload &7- Reload the configuration";
		HELP_MAINCMD_RENAME = "&b/party rename <newname> &7- Rename the party";
		HELP_MAINCMD_RENAME_OTHERS = "&b/party rename <party> <newname> &7- Renames a party";
		HELP_MAINCMD_SPY = "&b/party spy &7- See messages from other parties";
		
		HELP_ADDCMD_CLAIM = "&b/party claim <permission> &7- Grant permissions to the claim";
		HELP_ADDCMD_COLOR = "&b/party color <color> &7- Change color of the party";
		HELP_ADDCMD_DESC = "&b/party desc <description/remove> &7- Add/remove description";
		HELP_ADDCMD_HOME = "&b/party home &7- Teleport to the party home";
		HELP_ADDCMD_HOME_OTHERS = "&b/party home [party] &7- Teleport to the party home";
		HELP_ADDCMD_JOIN = "&b/party join <party> [password] &7- Join into a party";
		HELP_ADDCMD_LIST = "&b/party list [page] &7- List of online parties";
		HELP_ADDCMD_MOTD = "&b/party motd <motd/remove> &7- Add/remove motd";
		HELP_ADDCMD_PASSWORD = "&b/party password <password/remove> &7- Change password of the party";
		HELP_ADDCMD_PREFIX = "&b/party prefix <prefix/remove> &7- Add/remove prefix tag";
		HELP_ADDCMD_SETHOME = "&b/party sethome [remove] &7- Set the party home";
		HELP_ADDCMD_SUFFIX = "&b/party suffix <suffix/remove> &7- Add/remove suffix tag";
		HELP_ADDCMD_TELEPORT = "&b/party teleport &7- Teleport your party to you";
	}
}
