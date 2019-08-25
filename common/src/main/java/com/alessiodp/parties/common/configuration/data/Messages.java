package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationFile;

import java.util.ArrayList;
import java.util.List;

public abstract class Messages extends PartiesConfigurationFile {
	// Parties messages
	public static String PARTIES_UPDATEAVAILABLE;
	public static String PARTIES_CONFIGURATION_OUTDATED;
	
	public static String PARTIES_COMMON_INVALIDCMD;
	public static String PARTIES_COMMON_CONFIGRELOAD;
	public static String PARTIES_COMMON_NOTINPARTY;
	public static String PARTIES_COMMON_ALREADYINPARTY;
	public static String PARTIES_COMMON_PARTYNOTFOUND;
	public static String PARTIES_COMMON_PARTYFULL;
	
	public static String PARTIES_PERM_NOPERM;
	public static String PARTIES_PERM_NORANK;
	
	public static String PARTIES_OUT_PARTY;
	
	public static String PARTIES_LIST_ONLINEFORMAT;
	public static String PARTIES_LIST_OFFLINEFORMAT;
	public static String PARTIES_LIST_SEPARATOR;
	public static String PARTIES_LIST_EMPTY;
	public static String PARTIES_LIST_UNKNOWN;
	public static String PARTIES_LIST_MISSING;
	
	
	// Main commands messages
	public static String MAINCMD_ACCEPT_BROADCAST;
	public static String MAINCMD_ACCEPT_ACCEPTED;
	public static String MAINCMD_ACCEPT_ACCEPTRECEIPT;
	public static String MAINCMD_ACCEPT_NOINVITE;
	public static String MAINCMD_ACCEPT_NOEXISTS;
	public static String MAINCMD_ACCEPT_MULTIPLEINVITES;
	public static String MAINCMD_ACCEPT_MULTIPLEINVITES_PARTY;
	public static String MAINCMD_ACCEPT_WRONGCMD;
	
	public static String MAINCMD_CHAT_ENABLED;
	public static String MAINCMD_CHAT_DISABLED;
	public static String MAINCMD_CHAT_WRONGCMD;
	
	public static String MAINCMD_CREATE_CREATED;
	public static String MAINCMD_CREATE_CREATEDFIXED;
	public static String MAINCMD_CREATE_NAMEEXISTS;
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
	public static String MAINCMD_DENY_MULTIPLEINVITES;
	public static String MAINCMD_DENY_MULTIPLEINVITES_PARTY;
	public static String MAINCMD_DENY_WRONGCMD;
	
	public static String MAINCMD_IGNORE_START;
	public static String MAINCMD_IGNORE_STOP;
	public static String MAINCMD_IGNORE_LIST_HEADER;
	public static String MAINCMD_IGNORE_LIST_PARTYPREFIX;
	public static String MAINCMD_IGNORE_LIST_SEPARATOR;
	public static String MAINCMD_IGNORE_LIST_EMPTY;
	public static String MAINCMD_IGNORE_WRONGCMD;
	
	public static List<String> MAINCMD_INFO_CONTENT;
	
	public static String MAINCMD_INVITE_SENT;
	public static String MAINCMD_INVITE_PLAYERINVITED;
	public static String MAINCMD_INVITE_TIMEOUT_NORESPONSE;
	public static String MAINCMD_INVITE_TIMEOUT_TIMEOUT;
	public static String MAINCMD_INVITE_REVOKE_SENT;
	public static String MAINCMD_INVITE_REVOKE_REVOKED;
	public static String MAINCMD_INVITE_COOLDOWN_GLOBAL;
	public static String MAINCMD_INVITE_COOLDOWN_INDIVIDUAL;
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
	
	public static String MAINCMD_P_COOLDOWN;
	public static String MAINCMD_P_CENSORED;
	public static String MAINCMD_P_WRONGCMD;
	
	public static String MAINCMD_RANK_CHANGED;
	public static String MAINCMD_RANK_BROADCAST;
	public static String MAINCMD_RANK_WRONGRANK;
	public static String MAINCMD_RANK_SAMERANK;
	public static String MAINCMD_RANK_LOWRANK;
	public static String MAINCMD_RANK_TOHIGHERRANK;
	public static String MAINCMD_RANK_FIXEDLEADER;
	public static String MAINCMD_RANK_CHANGINGYOURSELF;
	public static String MAINCMD_RANK_PLAYERNOTINPARTY;
	public static String MAINCMD_RANK_PLAYERNOTINPARTY_OTHER;
	public static List<String> MAINCMD_RANK_CONFLICT_CONTENT;
	public static String MAINCMD_RANK_CONFLICT_PLAYER;
	public static String MAINCMD_RANK_WRONGCMD;
	
	public static String MAINCMD_RENAME_RENAMED;
	public static String MAINCMD_RENAME_BROADCAST;
	public static String MAINCMD_RENAME_WRONGCMD;
	public static String MAINCMD_RENAME_WRONGCMD_ADMIN;
	
	public static String MAINCMD_SPY_ENABLED;
	public static String MAINCMD_SPY_DISABLED;
	public static String MAINCMD_SPY_WRONGCMD;
	
	public static String MAINCMD_VERSION_UPDATED;
	public static String MAINCMD_VERSION_OUTDATED;

	
	// Additional commands messages
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
	
	public static String ADDCMD_FOLLOW_ON;
	public static String ADDCMD_FOLLOW_OFF;
	public static String ADDCMD_FOLLOW_WRONGCMD;
	
	public static String ADDCMD_JOIN_JOINED;
	public static String ADDCMD_JOIN_PLAYERJOINED;
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
	
	public static String ADDCMD_MUTE_ON;
	public static String ADDCMD_MUTE_OFF;
	public static String ADDCMD_MUTE_WRONGCMD;
	
	public static String ADDCMD_PASSWORD_CHANGED;
	public static String ADDCMD_PASSWORD_REMOVED;
	public static String ADDCMD_PASSWORD_BROADCAST;
	public static String ADDCMD_PASSWORD_INVALID;
	public static String ADDCMD_PASSWORD_WRONGCMD;
	
	public static String ADDCMD_TELEPORT_TELEPORTING;
	public static String ADDCMD_TELEPORT_TELEPORTED;
	public static String ADDCMD_TELEPORT_COOLDOWN;
	
	
	// Other messages
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
	public static String HELP_MAINCMD_DELETE;
	public static String HELP_MAINCMD_DENY;
	public static String HELP_MAINCMD_IGNORE;
	public static String HELP_MAINCMD_INFO;
	public static String HELP_MAINCMD_INVITE;
	public static String HELP_MAINCMD_KICK;
	public static String HELP_MAINCMD_LEAVE;
	public static String HELP_MAINCMD_P;
	public static String HELP_MAINCMD_RANK;
	public static String HELP_MAINCMD_RELOAD;
	public static String HELP_MAINCMD_RENAME;
	public static String HELP_MAINCMD_SPY;
	public static String HELP_MAINCMD_VERSION;
	
	public static String HELP_ADDCMD_COLOR;
	public static String HELP_ADDCMD_DESC;
	public static String HELP_ADDCMD_FOLLOW;
	public static String HELP_ADDCMD_JOIN;
	public static String HELP_ADDCMD_LIST;
	public static String HELP_ADDCMD_MOTD;
	public static String HELP_ADDCMD_MUTE;
	public static String HELP_ADDCMD_PASSWORD;
	public static String HELP_ADDCMD_TELEPORT;
	
	
	protected Messages(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		// Parties messages
		PARTIES_UPDATEAVAILABLE = "&9New version of Parties found: %version% (Current: %thisversion%)";
		PARTIES_CONFIGURATION_OUTDATED = "&cThe configuration file '%config%' is outdated!";
		
		PARTIES_COMMON_INVALIDCMD = "&cInvalid command";
		PARTIES_COMMON_CONFIGRELOAD = "&aConfiguration reloaded";
		PARTIES_COMMON_NOTINPARTY = "&cYou are not in a party";
		PARTIES_COMMON_ALREADYINPARTY = "&cYou are already in a party!";
		PARTIES_COMMON_PARTYNOTFOUND = "&cThe party %party% doesn't exist";
		PARTIES_COMMON_PARTYFULL = "&cThe party is full!";
		
		PARTIES_PERM_NOPERM = "&cYou do not have access to that command";
		PARTIES_PERM_NORANK = "&cYou need to be %rank_name% to perform that command";
		
		PARTIES_OUT_PARTY = "Out party";
		
		PARTIES_LIST_ONLINEFORMAT = "&b%player%";
		PARTIES_LIST_OFFLINEFORMAT = "&7%player%";
		PARTIES_LIST_SEPARATOR = "&7, ";
		PARTIES_LIST_EMPTY = "&7Nobody";
		PARTIES_LIST_UNKNOWN = "&6Someone";
		PARTIES_LIST_MISSING = "&7Miss";
		
		
		// Main commands messages
		MAINCMD_ACCEPT_BROADCAST = "&b&l%player% joined the party";
		MAINCMD_ACCEPT_ACCEPTED = "&aYou accepted the party invite";
		MAINCMD_ACCEPT_ACCEPTRECEIPT = "&a%player% accepted your invite";
		MAINCMD_ACCEPT_NOINVITE = "&cYou have not pending invitation";
		MAINCMD_ACCEPT_NOEXISTS = "&cThe invite doesn't exist anymore";
		MAINCMD_ACCEPT_MULTIPLEINVITES = "&cChoose the party that you want accept:";
		MAINCMD_ACCEPT_MULTIPLEINVITES_PARTY = "[{\"text\":\"%party%\",\"color\":\"aqua\"},{\"text\":\" - Click here to accept\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party accept %party%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Accept the invitation\",\"color\":\"gold\"}]}}}]";
		MAINCMD_ACCEPT_WRONGCMD = "&cWrong variables: Type &7/party accept [party]";
		
		MAINCMD_CHAT_ENABLED = "&aChat set to party";
		MAINCMD_CHAT_DISABLED = "&aChat set to public";
		MAINCMD_CHAT_WRONGCMD = "&cWrong variables: Type &7/party chat [on/off]";
		
		MAINCMD_CREATE_CREATED = "[{\"text\":\"You have created the party %party%.\\n\",\"color\":\"aqua\",\"bold\":true},{\"text\":\"Type \",\"color\":\"aqua\",\"bold\":false},{\"text\":\"/party invite\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party invite \"}},{\"text\":\" to invite your friend.\",\"color\":\"aqua\"}]";
		MAINCMD_CREATE_CREATEDFIXED = "&b&lYou have created the fixed party %party%";
		MAINCMD_CREATE_NAMEEXISTS = "&cThe party name %party% already exists, choose a different name";
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
		MAINCMD_DENY_MULTIPLEINVITES = "&cChoose the party that you want deny:";
		MAINCMD_DENY_MULTIPLEINVITES_PARTY = "[{\"text\":\"%party%\",\"color\":\"aqua\"},{\"text\":\" - Click here to deny\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party deny %party%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Deny the invitation\",\"color\":\"gold\"}]}}}]";
		MAINCMD_DENY_WRONGCMD = "&cWrong variables: Type &7/party deny [party]";
		
		MAINCMD_IGNORE_START = "&7Ignoring %party% invites";
		MAINCMD_IGNORE_STOP = "&7You stopped ignoring %party%";
		MAINCMD_IGNORE_LIST_HEADER = "&b&lList ignored parties:";
		MAINCMD_IGNORE_LIST_PARTYPREFIX = "&c";
		MAINCMD_IGNORE_LIST_SEPARATOR = "&7, ";
		MAINCMD_IGNORE_LIST_EMPTY = "&7None";
		MAINCMD_IGNORE_WRONGCMD = "&cWrong variables: Type &7/party ignore [party]";
		
		MAINCMD_INFO_CONTENT = new ArrayList<>();
		MAINCMD_INFO_CONTENT.add("&b============ &l%party%'s Info &r&b============");
		MAINCMD_INFO_CONTENT.add("&b&lDescription&r&7: %desc%");
		MAINCMD_INFO_CONTENT.add("&b&lMotd&r&7: %motd%");
		MAINCMD_INFO_CONTENT.add("&bLeader&7: %list_leader%");
		MAINCMD_INFO_CONTENT.add("&bMods&7: %list_moderator%");
		MAINCMD_INFO_CONTENT.add("&bMembers&7: %list_member%");
		MAINCMD_INFO_CONTENT.add("&bOnline players&7: %online_number%");
		
		MAINCMD_INVITE_SENT = "&bYou invited %player% in your party";
		MAINCMD_INVITE_PLAYERINVITED = "[{\"text\":\"%player% has invited you to the party %party%.\\n\",\"color\":\"aqua\"},{\"text\":\"Do you want \",\"color\":\"aqua\"},{\"text\":\"accept\",\"color\":\"blue\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party accept %party%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Accept the invitation\",\"color\":\"gold\"}]}}},{\"text\":\" or \",\"color\":\"aqua\",\"bold\":false},{\"text\":\"deny\",\"color\":\"red\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party deny %party%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Deny the invitation\",\"color\":\"gold\"}]}}},{\"text\":\"?\\n\",\"color\":\"aqua\",\"bold\":false},{\"text\":\"You can use \",\"color\":\"aqua\"},{\"text\":\"/party <accept/deny>\",\"color\":\"gray\"},{\"text\":\" to choose.\",\"color\":\"aqua\"}]";
		MAINCMD_INVITE_TIMEOUT_NORESPONSE = "&7%player% didn't accept the party invite";
		MAINCMD_INVITE_TIMEOUT_TIMEOUT = "&7You didn't accept the party invite to %party%";
		MAINCMD_INVITE_REVOKE_SENT = "&7Revoked invite sent to %player%";
		MAINCMD_INVITE_REVOKE_REVOKED = "&7Invite received from %party% has been revoked";
		MAINCMD_INVITE_COOLDOWN_GLOBAL = "&cYou need to wait %seconds% before invite another player";
		MAINCMD_INVITE_COOLDOWN_INDIVIDUAL = "&cYou need to wait %seconds% before invite again the same player";
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
		MAINCMD_KICK_CONFLICT_CONTENT = new ArrayList<>();
		MAINCMD_KICK_CONFLICT_CONTENT.add("&cWe have found some players with that name:");
		MAINCMD_KICK_CONFLICT_CONTENT.add("%list_players%");
		MAINCMD_KICK_CONFLICT_CONTENT.add("&cUse '&7/party kick <username> <number>&c' to kick the right player");
		MAINCMD_KICK_CONFLICT_PLAYER = "{\"text\":\"\",\"extra\":[{\"text\":\"[%number%] \",\"color\":\"gray\"},{\"text\":\"%username%\",\"color\":\"gold\"},{\"text\":\" [\",\"color\":\"gray\"},{\"text\":\"%party%\",\"color\":\"aqua\"},{\"text\":\"]: last login %lastloginapi_last_logout_date%\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party kick %username% %number%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Kick this player\",\"color\":\"gold\"}}}";
		MAINCMD_KICK_WRONGCMD = "&cWrong variables: Type &7/party kick <name>";
		
		MAINCMD_LEAVE_LEFT = "&b&lYou left the party %party%";
		MAINCMD_LEAVE_BROADCAST = "&b&l%player% left the party";
		MAINCMD_LEAVE_DISBANDED = "&6&lThe party has been disbanded because the leader left";
		
		MAINCMD_P_COOLDOWN = "&cYou still have to wait %seconds% seconds";
		MAINCMD_P_CENSORED = "&cThe message contains censored words!";
		MAINCMD_P_WRONGCMD = "&cWrong variables: Type &7/p <message>";
		
		MAINCMD_RANK_CHANGED = "&a%player% rank changed into %rank_name%";
		MAINCMD_RANK_BROADCAST = "";
		MAINCMD_RANK_WRONGRANK = "&cRank '%rank_name%' doesn't exist!";
		MAINCMD_RANK_SAMERANK = "&c%player% is already %rank_name%!";
		MAINCMD_RANK_LOWRANK = "&cYou cannot edit players with an equivalent or higher rank than yours!";
		MAINCMD_RANK_TOHIGHERRANK = "&cYou cannot promote to a rank equivalent or higher than yours!";
		MAINCMD_RANK_FIXEDLEADER = "&cYou cannot make someone a leader of a fixed party!";
		MAINCMD_RANK_CHANGINGYOURSELF = "&cYou cannot change yourself rank!";
		MAINCMD_RANK_PLAYERNOTINPARTY = "&c%player% is not in your party";
		MAINCMD_RANK_PLAYERNOTINPARTY_OTHER = "&c%player% doesn't have a party";
		MAINCMD_RANK_CONFLICT_CONTENT = new ArrayList<>();
		MAINCMD_RANK_CONFLICT_CONTENT.add("&cWe have found some players with that name:");
		MAINCMD_RANK_CONFLICT_CONTENT.add("%list_players%");
		MAINCMD_RANK_CONFLICT_CONTENT.add("&cUse '&7/party rank <username> <rank> <number>&c' to change rank of the right player");
		MAINCMD_RANK_CONFLICT_PLAYER = "{\"text\":\"\",\"extra\":[{\"text\":\"[%number%] \",\"color\":\"gray\"},{\"text\":\"%username%\",\"color\":\"gold\"},{\"text\":\" [\",\"color\":\"gray\"},{\"text\":\"%party%\",\"color\":\"aqua\"},{\"text\":\"]: last login %lastloginapi_last_logout_date%\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party rank %username% %rank% %number%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Change rank of this player\",\"color\":\"gold\"}}}";
		MAINCMD_RANK_WRONGCMD = "&cWrong variables: Type &7/party rank <player> <rank>";
		
		MAINCMD_RENAME_RENAMED = "&aThe party %old% has been renamed into %party%";
		MAINCMD_RENAME_BROADCAST = "&6Your party has been renamed into %party%!";
		MAINCMD_RENAME_WRONGCMD = "&cWrong variables: Type &7/party rename <newname>";
		MAINCMD_RENAME_WRONGCMD_ADMIN = "&cWrong variables: Type &7/party rename <party> <newname>";
		
		MAINCMD_SPY_ENABLED = "&7Now you are a spy!";
		MAINCMD_SPY_DISABLED = "&7You are no longer a spy";
		MAINCMD_SPY_WRONGCMD = "&cWrong variables: Type &7/party spy [on/off]";
		
		MAINCMD_VERSION_UPDATED = "&b&lParties &b%version% &7(%platform%) - Developed by &6AlessioDP";
		MAINCMD_VERSION_OUTDATED = "&b&lParties &b%version% &7(%platform%) - Developed by &6AlessioDP\n&aNew version found: &2%newversion%";

		
		// Additional commands messages
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
		
		ADDCMD_FOLLOW_ON = "&aNow your party members will follow your leader";
		ADDCMD_FOLLOW_OFF = "&aYour party members will not follow your leader anymore";
		ADDCMD_FOLLOW_WRONGCMD = "&cWrong variables: Type &7/party follow [on/off]";
		
		ADDCMD_JOIN_JOINED = "&aYou joined the party %party%";
		ADDCMD_JOIN_PLAYERJOINED = "&b&l%player% joined in the party";
		ADDCMD_JOIN_WRONGPASSWORD = "&cWrong password!";
		ADDCMD_JOIN_WRONGCMD = "&cWrong variables: Type &7/party join <party> [password]";
		
		ADDCMD_LIST_HEADER = "&b============ &lOnline Parties List &r&b============";
		ADDCMD_LIST_FOOTER = "&b================ &lPage %page% of %maxpages% &r&b================";
		ADDCMD_LIST_NOONE = "&7No one";
		ADDCMD_LIST_FORMATPARTY = "[{\"text\":\"%party%\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party info %party%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Show info\",\"color\":\"gold\"}]}}},{\"text\":\" [\",\"color\":\"gray\"},{\"text\":\"Online %online_number%\",\"color\":\"gold\"},{\"text\":\"] %desc%\",\"color\":\"gray\"}]";
		ADDCMD_LIST_WRONGCMD = "&cWrong variables: Type &7/party list [page]";
		
		ADDCMD_MOTD_CHANGED = "&bParty MOTD changed";
		ADDCMD_MOTD_REMOVED = "&bParty MOTD removed";
		ADDCMD_MOTD_BROADCAST = "";
		ADDCMD_MOTD_CONTENT = new ArrayList<>();
		ADDCMD_MOTD_CONTENT.add("&bParty MOTD:");
		ADDCMD_MOTD_CONTENT.add("&b%motd%");
		ADDCMD_MOTD_INVALID = "&cInvalid characters. You can also use '. , /'. Min 3 and max 100 chars.";
		ADDCMD_MOTD_CENSORED = "&cThe MOTD contains censored words!";
		ADDCMD_MOTD_WRONGCMD = "&cWrong variables: Type &7/party motd <motd/remove>";
		
		ADDCMD_MUTE_ON = "&7You have disabled notifications!";
		ADDCMD_MUTE_OFF = "&7You have enabled notifications!";
		ADDCMD_MUTE_WRONGCMD = "&cWrong variables: Type &7/party mute [on/off]";
		
		ADDCMD_PASSWORD_CHANGED = "&aParty password changed";
		ADDCMD_PASSWORD_REMOVED = "&aParty password removed";
		ADDCMD_PASSWORD_BROADCAST = "";
		ADDCMD_PASSWORD_INVALID = "&cInvalid characters. Use: a-Z or 0-9. Min 1 and max 16 chars.";
		ADDCMD_PASSWORD_WRONGCMD = "&cWrong variables: Type &7/party password <password/remove>";
		
		ADDCMD_TELEPORT_TELEPORTING = "&7Teleporting your party here!";
		ADDCMD_TELEPORT_TELEPORTED = "&bTeleported to %player%";
		ADDCMD_TELEPORT_COOLDOWN = "&cYou have to wait %seconds% seconds!";
		
		// Other messages
		OTHER_FIXED_DEFAULTJOIN = "&bYou entered into %party%";
		
		OTHER_JOINLEAVE_SERVERJOIN = "&b%player% is online!";
		OTHER_JOINLEAVE_SERVERLEAVE = "&7%player% is offline!";
		
		
		// Help messages
		HELP_HEADER = "&b================= &lParty Help %page%/%maxpages% &r&b=================";
		HELP_FOOTER = "";
		HELP_CONSOLEHELP = new ArrayList<>();
		HELP_CONSOLEHELP.add("You can only make these commands:");
		HELP_CONSOLEHELP.add(" > party create <name> fixed - Create a new party");
		HELP_CONSOLEHELP.add(" > party delete <party> - Delete a party");
		HELP_CONSOLEHELP.add(" > party info <party> - Show party information");
		HELP_CONSOLEHELP.add(" > party list - List of online parties");
		HELP_CONSOLEHELP.add(" > party kick <player> - Kick a player from the party");
		HELP_CONSOLEHELP.add(" > party rank <player> <rank> - Change rank of the player");
		HELP_CONSOLEHELP.add(" > party reload - Reload Parties configuration files");
		HELP_CONSOLEHELP.add(" > party rename <party> <name> - Rename the party");
		HELP_CONSOLEHELP.add(" > party version - Show Parties information");
		
		HELP_MAINCMD_HELP = "{\"text\":\"\",\"extra\":[{\"text\":\"/party help [page]\",\"color\":\"aqua\"},{\"text\":\" - Show help pages\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party help \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_ACCEPT = "{\"text\":\"\",\"extra\":[{\"text\":\"/party accept\",\"color\":\"aqua\"},{\"text\":\" - Accept a party invitation\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party accept\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_CHAT = "{\"text\":\"\",\"extra\":[{\"text\":\"/party chat [on/off]\",\"color\":\"aqua\"},{\"text\":\" - Toggle party chat\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party chat \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_CREATE = "{\"text\":\"\",\"extra\":[{\"text\":\"/party create <name>\",\"color\":\"aqua\"},{\"text\":\" - Create a new party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party create \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_DELETE = "{\"text\":\"\",\"extra\":[{\"text\":\"/party delete <party>\",\"color\":\"aqua\"},{\"text\":\" - Delete the party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party delete \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_DENY = "{\"text\":\"\",\"extra\":[{\"text\":\"/party deny\",\"color\":\"aqua\"},{\"text\":\" - Deny a party invitation\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party deny\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_IGNORE = "{\"text\":\"\",\"extra\":[{\"text\":\"/party ignore [party]\",\"color\":\"aqua\"},{\"text\":\" - Add/remove/show ignored parties\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party ignore \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_INFO = "{\"text\":\"\",\"extra\":[{\"text\":\"/party info [party]\",\"color\":\"aqua\"},{\"text\":\" - Show party information\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party info \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_INVITE = "{\"text\":\"\",\"extra\":[{\"text\":\"/party invite <player>\",\"color\":\"aqua\"},{\"text\":\" - Invite a player to your party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party invite \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_KICK = "{\"text\":\"\",\"extra\":[{\"text\":\"/party kick <player>\",\"color\":\"aqua\"},{\"text\":\" - Kick a player from your party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party kick \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_LEAVE = "{\"text\":\"\",\"extra\":[{\"text\":\"/party leave\",\"color\":\"aqua\"},{\"text\":\" - Leave your party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party leave \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_P = "{\"text\":\"\",\"extra\":[{\"text\":\"/p <message>\",\"color\":\"aqua\"},{\"text\":\" - Send a message to the party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/p \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_RANK = "{\"text\":\"\",\"extra\":[{\"text\":\"/party rank <player> <rank>\",\"color\":\"aqua\"},{\"text\":\" - Change rank of the player\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party rank \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_RELOAD = "{\"text\":\"\",\"extra\":[{\"text\":\"/party reload\",\"color\":\"aqua\"},{\"text\":\" - Reload Parties configuration files\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party reload\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_RENAME = "{\"text\":\"\",\"extra\":[{\"text\":\"/party rename [party] <newname>\",\"color\":\"aqua\"},{\"text\":\" - Rename the party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party rename \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_SPY = "{\"text\":\"\",\"extra\":[{\"text\":\"/party spy [on/off]\",\"color\":\"aqua\"},{\"text\":\" - Spy messages of other parties\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party spy \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_MAINCMD_VERSION = "{\"text\":\"\",\"extra\":[{\"text\":\"/party version\",\"color\":\"aqua\"},{\"text\":\" - Show Parties information\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party version\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		
		HELP_ADDCMD_COLOR = "{\"text\":\"\",\"extra\":[{\"text\":\"/party color <color>\",\"color\":\"aqua\"},{\"text\":\" - Change party color\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party color \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_DESC = "{\"text\":\"\",\"extra\":[{\"text\":\"/party desc <description/remove>\",\"color\":\"aqua\"},{\"text\":\" - Set/remove party description\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party desc \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_FOLLOW = "{\"text\":\"\",\"extra\":[{\"text\":\"/party follow [on/off]\",\"color\":\"aqua\"},{\"text\":\" - Toggle follow leader\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party follow \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_JOIN = "{\"text\":\"\",\"extra\":[{\"text\":\"/party join <party> [password]\",\"color\":\"aqua\"},{\"text\":\" - Join into the party\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party join \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_LIST = "{\"text\":\"\",\"extra\":[{\"text\":\"/party list [page]\",\"color\":\"aqua\"},{\"text\":\" - List of online parties\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party list \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_MOTD = "{\"text\":\"\",\"extra\":[{\"text\":\"/party motd <motd/remove>\",\"color\":\"aqua\"},{\"text\":\" - Set/remove party motd\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party motd \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_MUTE = "{\"text\":\"\",\"extra\":[{\"text\":\"/party mute [on/off]\",\"color\":\"aqua\"},{\"text\":\" - Toggle notifications\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party mute \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_PASSWORD = "{\"text\":\"\",\"extra\":[{\"text\":\"/party password <pw/remove>\",\"color\":\"aqua\"},{\"text\":\" - Change party password\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party password \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_TELEPORT = "{\"text\":\"\",\"extra\":[{\"text\":\"/party teleport\",\"color\":\"aqua\"},{\"text\":\" - Teleport your party to you\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party teleport\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		// Parties messages
		PARTIES_UPDATEAVAILABLE = confAdapter.getString("parties.update-available", PARTIES_UPDATEAVAILABLE);
		PARTIES_CONFIGURATION_OUTDATED = confAdapter.getString("parties.configuration-outdated", PARTIES_CONFIGURATION_OUTDATED);
		
		PARTIES_COMMON_INVALIDCMD = confAdapter.getString("parties.common-messages.invalid-command", PARTIES_COMMON_INVALIDCMD);
		PARTIES_COMMON_CONFIGRELOAD = confAdapter.getString("parties.common-messages.configuration-reloaded", PARTIES_COMMON_CONFIGRELOAD);
		PARTIES_COMMON_NOTINPARTY = confAdapter.getString("parties.common-messages.not-in-party", PARTIES_COMMON_NOTINPARTY);
		PARTIES_COMMON_ALREADYINPARTY = confAdapter.getString("parties.common-messages.already-in-party", PARTIES_COMMON_ALREADYINPARTY);
		PARTIES_COMMON_PARTYNOTFOUND = confAdapter.getString("parties.common-messages.party-not-found", PARTIES_COMMON_PARTYNOTFOUND);
		PARTIES_COMMON_PARTYFULL = confAdapter.getString("parties.common-messages.party-full", PARTIES_COMMON_PARTYFULL);
		
		PARTIES_PERM_NOPERM = confAdapter.getString("parties.permissions.no-permission", PARTIES_PERM_NOPERM);
		PARTIES_PERM_NORANK = confAdapter.getString("parties.permissions.no-permission-in-party", PARTIES_PERM_NORANK);
		
		PARTIES_OUT_PARTY = confAdapter.getString("parties.out-party", PARTIES_OUT_PARTY);
		
		PARTIES_LIST_ONLINEFORMAT = confAdapter.getString("parties.list.player-online-format", PARTIES_LIST_ONLINEFORMAT);
		PARTIES_LIST_OFFLINEFORMAT = confAdapter.getString("parties.list.player-offline-format", PARTIES_LIST_OFFLINEFORMAT);
		PARTIES_LIST_SEPARATOR = confAdapter.getString("parties.list.player-separator", PARTIES_LIST_SEPARATOR);
		PARTIES_LIST_EMPTY = confAdapter.getString("parties.list.player-empty", PARTIES_LIST_EMPTY);
		PARTIES_LIST_UNKNOWN = confAdapter.getString("parties.list.player-unknown", PARTIES_LIST_UNKNOWN);
		PARTIES_LIST_MISSING = confAdapter.getString("parties.list.missing-value", PARTIES_LIST_MISSING);
		
		
		// Main commands messages
		MAINCMD_ACCEPT_BROADCAST = confAdapter.getString("main-commands.accept.broadcast", MAINCMD_ACCEPT_BROADCAST);
		MAINCMD_ACCEPT_ACCEPTED = confAdapter.getString("main-commands.accept.accepted", MAINCMD_ACCEPT_ACCEPTED);
		MAINCMD_ACCEPT_ACCEPTRECEIPT = confAdapter.getString("main-commands.accept.accept-receipt", MAINCMD_ACCEPT_ACCEPTRECEIPT);
		MAINCMD_ACCEPT_NOINVITE = confAdapter.getString("main-commands.accept.no-invite", MAINCMD_ACCEPT_NOINVITE);
		MAINCMD_ACCEPT_MULTIPLEINVITES= confAdapter.getString("main-commands.accept.multiple-invites", MAINCMD_ACCEPT_MULTIPLEINVITES);
		MAINCMD_ACCEPT_MULTIPLEINVITES_PARTY = confAdapter.getString("main-commands.accept.multiple-invites-party", MAINCMD_ACCEPT_MULTIPLEINVITES_PARTY);
		MAINCMD_ACCEPT_NOEXISTS = confAdapter.getString("main-commands.accept.no-exists", MAINCMD_ACCEPT_NOEXISTS);
		
		MAINCMD_CHAT_ENABLED = confAdapter.getString("main-commands.chat.enabled", MAINCMD_CHAT_ENABLED);
		MAINCMD_CHAT_DISABLED = confAdapter.getString("main-commands.chat.disabled", MAINCMD_CHAT_DISABLED);
		MAINCMD_CHAT_WRONGCMD = confAdapter.getString("main-commands.chat.wrong-command", MAINCMD_CHAT_WRONGCMD);
		
		MAINCMD_CREATE_CREATED = confAdapter.getString("main-commands.create.created", MAINCMD_CREATE_CREATED);
		MAINCMD_CREATE_CREATEDFIXED = confAdapter.getString("main-commands.create.created-fixed", MAINCMD_CREATE_CREATEDFIXED);
		MAINCMD_CREATE_NAMEEXISTS = confAdapter.getString("main-commands.create.name-already-exists", MAINCMD_CREATE_NAMEEXISTS);
		MAINCMD_CREATE_NAMETOOLONG = confAdapter.getString("main-commands.create.name-too-long", MAINCMD_CREATE_NAMETOOLONG);
		MAINCMD_CREATE_NAMETOOSHORT = confAdapter.getString("main-commands.create.name-too-short", MAINCMD_CREATE_NAMETOOSHORT);
		MAINCMD_CREATE_INVALIDNAME = confAdapter.getString("main-commands.create.invalid-name", MAINCMD_CREATE_INVALIDNAME);
		MAINCMD_CREATE_CENSORED = confAdapter.getString("main-commands.create.censored", MAINCMD_CREATE_CENSORED);
		MAINCMD_CREATE_WRONGCMD = confAdapter.getString("main-commands.create.wrong-command", MAINCMD_CREATE_WRONGCMD);
		
		MAINCMD_DELETE_DELETED = confAdapter.getString("main-commands.delete.deleted", MAINCMD_DELETE_DELETED);
		MAINCMD_DELETE_DELETEDSILENTLY = confAdapter.getString("main-commands.delete.deleted-silently", MAINCMD_DELETE_DELETEDSILENTLY);
		MAINCMD_DELETE_BROADCAST = confAdapter.getString("main-commands.delete.broadcast", MAINCMD_DELETE_BROADCAST);
		MAINCMD_DELETE_WRONGCMD = confAdapter.getString("main-commands.delete.wrong-command", MAINCMD_DELETE_WRONGCMD);
		
		MAINCMD_DENY_DENIED = confAdapter.getString("main-commands.deny.denied", MAINCMD_DENY_DENIED);
		MAINCMD_DENY_DENYRECEIPT = confAdapter.getString("main-commands.deny.deny-receipt", MAINCMD_DENY_DENYRECEIPT);
		MAINCMD_DENY_NOINVITE = confAdapter.getString("main-commands.deny.no-invite", MAINCMD_DENY_NOINVITE);
		MAINCMD_DENY_NOEXISTS = confAdapter.getString("main-commands.deny.no-exists", MAINCMD_DENY_NOEXISTS);
		MAINCMD_DENY_MULTIPLEINVITES = confAdapter.getString("main-commands.deny.multiple-invites", MAINCMD_DENY_MULTIPLEINVITES);
		MAINCMD_DENY_MULTIPLEINVITES_PARTY = confAdapter.getString("main-commands.deny.multiple-invites-party", MAINCMD_DENY_MULTIPLEINVITES_PARTY);
		MAINCMD_DENY_WRONGCMD = confAdapter.getString("main-commands.deny.wrong-command", MAINCMD_DENY_WRONGCMD);
		
		MAINCMD_IGNORE_START = confAdapter.getString("main-commands.ignore.start-ignore", MAINCMD_IGNORE_START);
		MAINCMD_IGNORE_STOP = confAdapter.getString("main-commands.ignore.stop-ignore", MAINCMD_IGNORE_STOP);
		MAINCMD_IGNORE_LIST_HEADER = confAdapter.getString("main-commands.ignore.ignore-list.header", MAINCMD_IGNORE_LIST_HEADER);
		MAINCMD_IGNORE_LIST_PARTYPREFIX = confAdapter.getString("main-commands.ignore.ignore-list.party-prefix", MAINCMD_IGNORE_LIST_PARTYPREFIX);
		MAINCMD_IGNORE_LIST_SEPARATOR = confAdapter.getString("main-commands.ignore.ignore-list.separator", MAINCMD_IGNORE_LIST_SEPARATOR);
		MAINCMD_IGNORE_LIST_EMPTY = confAdapter.getString("main-commands.ignore.ignore-list.empty", MAINCMD_IGNORE_LIST_EMPTY);
		MAINCMD_IGNORE_WRONGCMD = confAdapter.getString("main-commands.ignore.wrong-command", MAINCMD_IGNORE_WRONGCMD);
		
		MAINCMD_INFO_CONTENT = confAdapter.getStringList("main-commands.info.content", MAINCMD_INFO_CONTENT);
		
		MAINCMD_INVITE_SENT = confAdapter.getString("main-commands.invite.sent", MAINCMD_INVITE_SENT);
		MAINCMD_INVITE_PLAYERINVITED = confAdapter.getString("main-commands.invite.player-invited", MAINCMD_INVITE_PLAYERINVITED);
		MAINCMD_INVITE_TIMEOUT_NORESPONSE = confAdapter.getString("main-commands.invite.timeout.noresponse", MAINCMD_INVITE_TIMEOUT_NORESPONSE);
		MAINCMD_INVITE_TIMEOUT_TIMEOUT = confAdapter.getString("main-commands.invite.timeout.timeout", MAINCMD_INVITE_TIMEOUT_TIMEOUT);
		MAINCMD_INVITE_REVOKE_SENT = confAdapter.getString("main-commands.invite.revoke.sent-revoked", MAINCMD_INVITE_REVOKE_SENT);
		MAINCMD_INVITE_REVOKE_REVOKED = confAdapter.getString("main-commands.invite.revoke.player-invite-revoked", MAINCMD_INVITE_REVOKE_REVOKED);
		MAINCMD_INVITE_COOLDOWN_GLOBAL = confAdapter.getString("main-commands.invite.cooldown.global", MAINCMD_INVITE_COOLDOWN_GLOBAL);
		MAINCMD_INVITE_COOLDOWN_INDIVIDUAL = confAdapter.getString("main-commands.invite.cooldown.individual", MAINCMD_INVITE_COOLDOWN_INDIVIDUAL);
		MAINCMD_INVITE_PLAYEROFFLINE = confAdapter.getString("main-commands.invite.player-offline", MAINCMD_INVITE_PLAYEROFFLINE);
		MAINCMD_INVITE_PLAYERNOPERM = confAdapter.getString("main-commands.invite.player-no-permission", MAINCMD_INVITE_PLAYERNOPERM);
		MAINCMD_INVITE_PLAYERINPARTY = confAdapter.getString("main-commands.invite.player-in-party", MAINCMD_INVITE_PLAYERINPARTY);
		MAINCMD_INVITE_ALREADYINVITED = confAdapter.getString("main-commands.invite.already-invited", MAINCMD_INVITE_ALREADYINVITED);
		MAINCMD_INVITE_WRONGCMD = confAdapter.getString("main-commands.invite.wrong-command", MAINCMD_INVITE_WRONGCMD);
		
		MAINCMD_KICK_SENT = confAdapter.getString("main-commands.kick.sent", MAINCMD_KICK_SENT);
		MAINCMD_KICK_PLAYERKICKED = confAdapter.getString("main-commands.kick.player-kicked", MAINCMD_KICK_PLAYERKICKED);
		MAINCMD_KICK_BROADCAST = confAdapter.getString("main-commands.kick.broadcast", MAINCMD_KICK_BROADCAST);
		MAINCMD_KICK_BROADCAST_DISBAND = confAdapter.getString("main-commands.kick.broadcast-disband", MAINCMD_KICK_BROADCAST_DISBAND);
		MAINCMD_KICK_PLAYERHIGHERRANK = confAdapter.getString("main-commands.kick.player-higher-rank", MAINCMD_KICK_PLAYERHIGHERRANK);
		MAINCMD_KICK_PLAYERNOTINPARTY = confAdapter.getString("main-commands.kick.player-not-in-party", MAINCMD_KICK_PLAYERNOTINPARTY);
		MAINCMD_KICK_PLAYERNOTINPARTY_OTHER = confAdapter.getString("main-commands.kick.player-not-in-other-party", MAINCMD_KICK_PLAYERNOTINPARTY_OTHER);
		MAINCMD_KICK_CONFLICT_CONTENT = confAdapter.getStringList("main-commands.kick.players-conflict.content", MAINCMD_KICK_CONFLICT_CONTENT);
		MAINCMD_KICK_CONFLICT_PLAYER = confAdapter.getString("main-commands.kick.players-conflict.player", MAINCMD_KICK_CONFLICT_PLAYER);
		MAINCMD_KICK_WRONGCMD = confAdapter.getString("main-commands.kick.wrong-command", MAINCMD_KICK_WRONGCMD);
		
		MAINCMD_LEAVE_LEFT = confAdapter.getString("main-commands.leave.left", MAINCMD_LEAVE_LEFT);
		MAINCMD_LEAVE_BROADCAST = confAdapter.getString("main-commands.leave.broadcast", MAINCMD_LEAVE_BROADCAST);
		MAINCMD_LEAVE_DISBANDED = confAdapter.getString("main-commands.leave.party-disbanded", MAINCMD_LEAVE_DISBANDED);
		
		MAINCMD_P_COOLDOWN = confAdapter.getString("main-commands.p.cooldown", MAINCMD_P_COOLDOWN);
		MAINCMD_P_CENSORED = confAdapter.getString("main-commands.p.censored", MAINCMD_P_CENSORED);
		MAINCMD_P_WRONGCMD = confAdapter.getString("main-commands.p.wrong-command", MAINCMD_P_WRONGCMD);
		
		MAINCMD_RANK_CHANGED = confAdapter.getString("main-commands.rank.changed", MAINCMD_RANK_CHANGED);
		MAINCMD_RANK_BROADCAST = confAdapter.getString("main-commands.rank.broadcast", MAINCMD_RANK_BROADCAST);
		MAINCMD_RANK_WRONGRANK = confAdapter.getString("main-commands.rank.wrong-rank", MAINCMD_RANK_WRONGRANK);
		MAINCMD_RANK_SAMERANK = confAdapter.getString("main-commands.rank.same-rank", MAINCMD_RANK_SAMERANK);
		MAINCMD_RANK_LOWRANK = confAdapter.getString("main-commands.rank.low-rank", MAINCMD_RANK_LOWRANK);
		MAINCMD_RANK_TOHIGHERRANK = confAdapter.getString("main-commands.rank.to-higher-rank", MAINCMD_RANK_TOHIGHERRANK);
		MAINCMD_RANK_FIXEDLEADER = confAdapter.getString("main-commands.rank.fixed-leader", MAINCMD_RANK_FIXEDLEADER);
		MAINCMD_RANK_CHANGINGYOURSELF = confAdapter.getString("main-commands.rank.changing-yourself", MAINCMD_RANK_CHANGINGYOURSELF);
		MAINCMD_RANK_PLAYERNOTINPARTY = confAdapter.getString("main-commands.rank.player-not-in-party", MAINCMD_RANK_PLAYERNOTINPARTY);
		MAINCMD_RANK_PLAYERNOTINPARTY_OTHER = confAdapter.getString("main-commands.rank.player-not-in-other-party", MAINCMD_RANK_PLAYERNOTINPARTY_OTHER);
		MAINCMD_RANK_WRONGCMD = confAdapter.getString("main-commands.rank.wrong-command", MAINCMD_RANK_WRONGCMD);
		
		MAINCMD_RENAME_RENAMED = confAdapter.getString("main-commands.rename.renamed", MAINCMD_RENAME_RENAMED);
		MAINCMD_RENAME_BROADCAST = confAdapter.getString("main-commands.rename.broadcast", MAINCMD_RENAME_BROADCAST);
		MAINCMD_RENAME_WRONGCMD = confAdapter.getString("main-commands.rename.wrong-command", MAINCMD_RENAME_WRONGCMD);
		MAINCMD_RENAME_WRONGCMD_ADMIN = confAdapter.getString("main-commands.rename.wrong-command-admin", MAINCMD_RENAME_WRONGCMD_ADMIN);
		
		MAINCMD_SPY_ENABLED = confAdapter.getString("main-commands.spy.enabled", MAINCMD_SPY_ENABLED);
		MAINCMD_SPY_DISABLED = confAdapter.getString("main-commands.spy.disabled", MAINCMD_SPY_DISABLED);
		MAINCMD_SPY_WRONGCMD = confAdapter.getString("main-commands.spy.wrong-command", MAINCMD_SPY_WRONGCMD);
		
		MAINCMD_VERSION_UPDATED = confAdapter.getString("main-commands.version.updated", MAINCMD_VERSION_UPDATED);
		MAINCMD_VERSION_OUTDATED = confAdapter.getString("main-commands.version.outdated", MAINCMD_VERSION_OUTDATED);
		
		
		// Additional commands messages
		ADDCMD_COLOR_INFO = confAdapter.getString("additional-commands.color.info", ADDCMD_COLOR_INFO);
		ADDCMD_COLOR_EMPTY = confAdapter.getString("additional-commands.color.empty", ADDCMD_COLOR_EMPTY);
		ADDCMD_COLOR_CHANGED = confAdapter.getString("additional-commands.color.changed", ADDCMD_COLOR_CHANGED);
		ADDCMD_COLOR_REMOVED = confAdapter.getString("additional-commands.color.removed", ADDCMD_COLOR_REMOVED);
		ADDCMD_COLOR_BROADCAST = confAdapter.getString("additional-commands.color.broadcast", ADDCMD_COLOR_BROADCAST);
		ADDCMD_COLOR_WRONGCOLOR = confAdapter.getString("additional-commands.color.wrong-color", ADDCMD_COLOR_WRONGCOLOR);
		ADDCMD_COLOR_WRONGCMD = confAdapter.getString("additional-commands.color.wrong-command", ADDCMD_COLOR_WRONGCMD);
		
		ADDCMD_DESC_CHANGED = confAdapter.getString("additional-commands.desc.changed", ADDCMD_DESC_CHANGED);
		ADDCMD_DESC_REMOVED = confAdapter.getString("additional-commands.desc.removed", ADDCMD_DESC_REMOVED);
		ADDCMD_DESC_BROADCAST = confAdapter.getString("additional-commands.desc.broadcast", ADDCMD_DESC_BROADCAST);
		ADDCMD_DESC_INVALID = confAdapter.getString("additional-commands.desc.invalid-chars", ADDCMD_DESC_INVALID);
		ADDCMD_DESC_CENSORED = confAdapter.getString("additional-commands.desc.censored", ADDCMD_DESC_CENSORED);
		ADDCMD_DESC_WRONGCMD = confAdapter.getString("additional-commands.desc.wrong-command", ADDCMD_DESC_WRONGCMD);
		
		ADDCMD_FOLLOW_ON = confAdapter.getString("additional-commands.follow.toggle-on", ADDCMD_FOLLOW_ON);
		ADDCMD_FOLLOW_OFF = confAdapter.getString("additional-commands.follow.toggle-off", ADDCMD_FOLLOW_OFF);
		ADDCMD_FOLLOW_WRONGCMD = confAdapter.getString("additional-commands.follow.wrong-command", ADDCMD_FOLLOW_WRONGCMD);
		
		ADDCMD_JOIN_JOINED = confAdapter.getString("additional-commands.join.joined", ADDCMD_JOIN_JOINED);
		ADDCMD_JOIN_PLAYERJOINED = confAdapter.getString("additional-commands.join.player-joined", ADDCMD_JOIN_PLAYERJOINED);
		ADDCMD_JOIN_WRONGPASSWORD = confAdapter.getString("additional-commands.join.wrong-password", ADDCMD_JOIN_WRONGPASSWORD);
		ADDCMD_JOIN_WRONGCMD = confAdapter.getString("additional-commands.join.wrong-command", ADDCMD_JOIN_WRONGCMD);
		
		ADDCMD_LIST_HEADER = confAdapter.getString("additional-commands.list.header", ADDCMD_LIST_HEADER);
		ADDCMD_LIST_FOOTER = confAdapter.getString("additional-commands.list.footer", ADDCMD_LIST_FOOTER);
		ADDCMD_LIST_NOONE = confAdapter.getString("additional-commands.list.no-one", ADDCMD_LIST_NOONE);
		ADDCMD_LIST_FORMATPARTY = confAdapter.getString("additional-commands.list.format-party", ADDCMD_LIST_FORMATPARTY);
		ADDCMD_LIST_WRONGCMD = confAdapter.getString("additional-commands.list.wrong-command", ADDCMD_LIST_WRONGCMD);
		
		ADDCMD_MOTD_CHANGED = confAdapter.getString("additional-commands.motd.changed", ADDCMD_MOTD_CHANGED);
		ADDCMD_MOTD_REMOVED = confAdapter.getString("additional-commands.motd.removed", ADDCMD_MOTD_REMOVED);
		ADDCMD_MOTD_BROADCAST = confAdapter.getString("additional-commands.motd.broadcast", ADDCMD_MOTD_BROADCAST);
		ADDCMD_MOTD_CONTENT = confAdapter.getStringList("additional-commands.motd.content", ADDCMD_MOTD_CONTENT);
		ADDCMD_MOTD_INVALID = confAdapter.getString("additional-commands.motd.invalid-chars", ADDCMD_MOTD_INVALID);
		ADDCMD_MOTD_CENSORED = confAdapter.getString("additional-commands.motd.censored", ADDCMD_MOTD_CENSORED);
		ADDCMD_MOTD_WRONGCMD = confAdapter.getString("additional-commands.motd.wrong-command", ADDCMD_MOTD_WRONGCMD);
		
		ADDCMD_MUTE_ON = confAdapter.getString("additional-commands.mute.toggle-on", ADDCMD_MUTE_ON);
		ADDCMD_MUTE_OFF = confAdapter.getString("additional-commands.mute.toggle-off", ADDCMD_MUTE_OFF);
		ADDCMD_MUTE_WRONGCMD = confAdapter.getString("additional-commands.mute.wrong-command", ADDCMD_MUTE_WRONGCMD);
		
		ADDCMD_PASSWORD_CHANGED = confAdapter.getString("additional-commands.password.changed", ADDCMD_PASSWORD_CHANGED);
		ADDCMD_PASSWORD_REMOVED = confAdapter.getString("additional-commands.password.removed", ADDCMD_PASSWORD_REMOVED);
		ADDCMD_PASSWORD_BROADCAST = confAdapter.getString("additional-commands.password.broadcast", ADDCMD_PASSWORD_BROADCAST);
		ADDCMD_PASSWORD_INVALID = confAdapter.getString("additional-commands.password.invalid-chars", ADDCMD_PASSWORD_INVALID);
		ADDCMD_PASSWORD_WRONGCMD = confAdapter.getString("additional-commands.password.wrong-command", ADDCMD_PASSWORD_WRONGCMD);
		
		ADDCMD_TELEPORT_TELEPORTING = confAdapter.getString("additional-commands.teleport.teleporting", ADDCMD_TELEPORT_TELEPORTING);
		ADDCMD_TELEPORT_TELEPORTED = confAdapter.getString("additional-commands.teleport.player-teleported", ADDCMD_TELEPORT_TELEPORTED);
		ADDCMD_TELEPORT_COOLDOWN = confAdapter.getString("additional-commands.teleport.cooldown", ADDCMD_TELEPORT_COOLDOWN);
		
		
		// Other messages
		OTHER_FIXED_DEFAULTJOIN = confAdapter.getString("other.fixed-parties.default-join", OTHER_FIXED_DEFAULTJOIN);
		
		OTHER_JOINLEAVE_SERVERJOIN = confAdapter.getString("other.join-leave.server-join", OTHER_JOINLEAVE_SERVERJOIN);
		OTHER_JOINLEAVE_SERVERLEAVE = confAdapter.getString("other.join-leave.server-leave", OTHER_JOINLEAVE_SERVERLEAVE);
		
		
		// Help messages
		HELP_HEADER = confAdapter.getString("help.header", HELP_HEADER);
		HELP_FOOTER = confAdapter.getString("help.footer", HELP_FOOTER);
		HELP_CONSOLEHELP = confAdapter.getStringList("help.console-help", HELP_CONSOLEHELP);
		
		HELP_MAINCMD_HELP = confAdapter.getString("help.main-commands.help", HELP_MAINCMD_HELP);
		HELP_MAINCMD_ACCEPT = confAdapter.getString("help.main-commands.accept", HELP_MAINCMD_ACCEPT);
		HELP_MAINCMD_CHAT = confAdapter.getString("help.main-commands.chat", HELP_MAINCMD_CHAT);
		HELP_MAINCMD_CREATE = confAdapter.getString("help.main-commands.create", HELP_MAINCMD_CREATE);
		HELP_MAINCMD_DELETE = confAdapter.getString("help.main-commands.delete", HELP_MAINCMD_DELETE);
		HELP_MAINCMD_DENY = confAdapter.getString("help.main-commands.deny", HELP_MAINCMD_DENY);
		HELP_MAINCMD_IGNORE = confAdapter.getString("help.main-commands.ignore", HELP_MAINCMD_IGNORE);
		HELP_MAINCMD_INFO = confAdapter.getString("help.main-commands.info", HELP_MAINCMD_INFO);
		HELP_MAINCMD_INVITE = confAdapter.getString("help.main-commands.invite", HELP_MAINCMD_INVITE);
		HELP_MAINCMD_KICK = confAdapter.getString("help.main-commands.kick", HELP_MAINCMD_KICK);
		HELP_MAINCMD_LEAVE = confAdapter.getString("help.main-commands.leave", HELP_MAINCMD_LEAVE);
		HELP_MAINCMD_P = confAdapter.getString("help.main-commands.p", HELP_MAINCMD_P);
		HELP_MAINCMD_RANK = confAdapter.getString("help.main-commands.rank", HELP_MAINCMD_RANK);
		HELP_MAINCMD_RELOAD = confAdapter.getString("help.main-commands.reload", HELP_MAINCMD_RELOAD);
		HELP_MAINCMD_RENAME = confAdapter.getString("help.main-commands.rename", HELP_MAINCMD_RENAME);
		HELP_MAINCMD_SPY = confAdapter.getString("help.main-commands.spy", HELP_MAINCMD_SPY);
		HELP_MAINCMD_VERSION = confAdapter.getString("help.main-commands.version", HELP_MAINCMD_VERSION);
		
		HELP_ADDCMD_COLOR = confAdapter.getString("help.additional-commands.color", HELP_ADDCMD_COLOR);
		HELP_ADDCMD_DESC = confAdapter.getString("help.additional-commands.desc", HELP_ADDCMD_DESC);
		HELP_ADDCMD_FOLLOW = confAdapter.getString("help.additional-commands.follow", HELP_ADDCMD_FOLLOW);
		HELP_ADDCMD_JOIN = confAdapter.getString("help.additional-commands.join", HELP_ADDCMD_JOIN);
		HELP_ADDCMD_LIST = confAdapter.getString("help.additional-commands.list", HELP_ADDCMD_LIST);
		HELP_ADDCMD_MOTD = confAdapter.getString("help.additional-commands.motd", HELP_ADDCMD_MOTD);
		HELP_ADDCMD_MUTE = confAdapter.getString("help.additional-commands.mute", HELP_ADDCMD_MUTE);
		HELP_ADDCMD_PASSWORD = confAdapter.getString("help.additional-commands.password", HELP_ADDCMD_PASSWORD);
		HELP_ADDCMD_TELEPORT = confAdapter.getString("help.additional-commands.teleport", HELP_ADDCMD_TELEPORT);
	}
}
