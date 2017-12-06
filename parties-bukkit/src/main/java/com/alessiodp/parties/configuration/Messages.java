package com.alessiodp.parties.configuration;

import java.util.ArrayList;
import java.util.List;

public class Messages {
	public static String nopermission;
	public static String nopermission_party;
	public static String invalidcommand;
	public static String noparty;
	public static String canthitmates;
	public static String warnondamage;
	public static String configurationreloaded;
	public static String updateavailable;
	public static String expgainother;
	public static String expgain;
	public static String defaultjoined;
	public static String serverjoin;
	public static String serverleave;

	public static String p_cooldown;
	public static String p_wrongcmd;
	
	public static String create_partycreated;
	public static String create_partycreated_fixed;
	public static String create_alreadyexist;
	public static String create_alreadyinparty;
	public static String create_toolongname;
	public static String create_tooshortname;
	public static String create_invalidname;
	public static String create_censoredname;
	public static String create_wrongcmd;

	public static String password_changed;
	public static String password_removed;
	public static String password_broadcast;
	public static String password_invalidchars;
	public static String password_wrongcmd;
	
	public static String join_joined;
	public static String join_playerjoined;
	public static String join_maxplayers;
	public static String join_alreadyinparty;
	public static String join_wrongpw;
	public static String join_noexist;
	public static String join_wrongcmd;
	
	public static String accept_welcomeplayer;
	public static String accept_playerjoined;
	public static String accept_inviteaccepted;
	public static String accept_accepted;
	public static String accept_maxplayers;
	public static String accept_alreadyinparty;
	public static String accept_noinvite;
	public static String accept_noexist;
	
	public static String deny_invitedenied;
	public static String deny_denied;
	public static String deny_noinvite;
	public static String deny_noexist;
	
	public static String ignore_header;
	public static String ignore_color;
	public static String ignore_separator;
	public static String ignore_empty;
	public static String ignore_ignored;
	public static String ignore_deignored;
	public static String ignore_noexist;
	public static String ignore_wrongcmd;

	public static String leave_byeplayer;
	public static String leave_playerleaved;
	public static String leave_disbanded;

	public static List<String> info_content;
	public static String info_separator;
	public static String info_online;
	public static String info_offline;
	public static String info_empty;
	public static String info_someone;
	public static String info_missing;
	public static String info_noexist;
	public static String info_wrongcmd;
	
	public static List<String> members_content;
	public static String members_separator;
	public static String members_online;
	public static String members_offline;
	public static String members_someone;
	public static String members_empty;
	public static String members_noexist;
	public static String members_wrongcmd;
	
	public static String desc_changed;
	public static String desc_removed;
	public static String desc_invalidchars;
	public static String desc_censored;
	public static String desc_wrongcmd;
	
	public static String motd_changed;
	public static String motd_removed;
	public static String motd_header;
	public static String motd_color;
	public static String motd_footer;
	public static String motd_invalidchars;
	public static String motd_censored;
	public static String motd_wrongcmd;

	public static String chat_enabled;
	public static String chat_disabled;
	public static String chat_wrongcmd;
	
	public static String list_header;
	public static String list_subheader;
	public static String list_offline;
	public static String list_formatparty;
	public static String list_footer;
	public static String list_wrongcmd;

	public static String invite_onlyonline;
	public static String invite_maxplayers;
	public static String invite_alreadyparty;
	public static String invite_alreadyinvited;
	public static String invite_playernopex;
	public static String invite_send;
	public static String invite_rec;
	public static String invite_timeout;
	public static String invite_noresponse;
	public static String invite_revoked_send;
	public static String invite_revoked_rec;
	public static String invite_wrongcmd;

	public static String color_info;
	public static String color_empty;
	public static String color_changed;
	public static String color_removed;
	public static String color_wrongcolor;
	public static String color_wrongcmd;

	public static String prefix_changed;
	public static String prefix_removed;
	public static String prefix_invalidchars;
	public static String prefix_censored;
	public static String prefix_wrongcmd;
	
	public static String suffix_changed;
	public static String suffix_removed;
	public static String suffix_invalidchars;
	public static String suffix_censored;
	public static String suffix_wrongcmd;
	
	public static String kick_kicksend;
	public static String kick_uprank;
	public static String kick_kickedfrom;
	public static String kick_kickedplayer;
	public static String kick_kicksendother;
	public static String kick_nomemberother;
	public static String kick_nomember;
	public static String kick_wrongcmd;
	public static ArrayList<String> kick_playersconflict_content;
	public static String kick_playersconflict_player;
	
	public static String delete_deleted;
	public static String delete_deleted_silent;
	public static String delete_warn;
	public static String delete_noexist;
	public static String delete_wrongcmd;
	
	public static String rename_renamed;
	public static String rename_broadcast;
	public static String rename_noexist;
	public static String rename_wrongcmd;
	public static String rename_wrongcmd_admin;
	
	public static String rank_promoted;
	public static String rank_nomember;
	public static String rank_noparty;
	public static String rank_alreadyrank;
	public static String rank_wrongrank;
	public static String rank_lowrank;
	public static String rank_tohigherrank;
	public static String rank_nodegradeyourself;
	public static String rank_nopromoteyourself;
	public static String rank_wrongcmd;
	
	public static String sethome_set;
	public static String sethome_wrongcmd;
	
	public static String home_teleported;
	public static String home_in;
	public static String home_denied;
	public static String home_nohome;
	public static String home_noexist;
	public static String home_wrongcmd;
	public static String home_wrongcmd_admin;
	
	public static String teleport_teleporting;
	public static String teleport_playerteleported;
	public static String teleport_delay;
	public static String teleport_wrongcmd;
	
	public static String claim_done;
	public static String claim_removed;
	public static String claim_nomanager;
	public static String claim_noexistclaim;
	public static String claim_wrongcmd;
	
	public static String spy_active;
	public static String spy_disable;

	public static String migrate_info;
	public static String migrate_complete;
	public static String migrate_failed_offline;
	public static String migrate_failed_migration;
	public static String migrate_failed_same;
	public static String migrate_wrongdatabase;
	public static String migrate_wrongcmd;
	
	public static String vault_create_nomoney;
	public static String vault_join_nomoney;
	public static String vault_home_nomoney;
	public static String vault_sethome_nomoney;
	public static String vault_desc_nomoney;
	public static String vault_motd_nomoney;
	public static String vault_prefix_nomoney;
	public static String vault_suffix_nomoney;
	public static String vault_teleport_nomoney;
	public static String vault_claim_nomoney;
	public static String vault_confirm_anycmd;
	public static String vault_confirm_warnonbuy;
	public static String vault_confirm_confirmed;
	public static String vault_confirm_wrongcmd;
	
	public static String follow_following_world;
	public static String follow_following_server;
	
	public static String help_header;
	public static String help_help;
	public static String help_p;
	public static String help_create;
	public static String help_createfixed;
	public static String help_join;
	public static String help_accept;
	public static String help_deny;
	public static String help_ignore;
	public static String help_leave;
	public static String help_info;
	public static String help_members;
	public static String help_password;
	public static String help_rank;
	public static String help_home;
	public static String help_home_others;
	public static String help_sethome;
	public static String help_teleport;
	public static String help_desc;
	public static String help_motd;
	public static String help_chat;
	public static String help_list;
	public static String help_invite;
	public static String help_color;
	public static String help_prefix;
	public static String help_suffix;
	public static String help_kick;
	public static String help_spy;
	public static String help_delete;
	public static String help_rename;
	public static String help_rename_others;
	public static String help_reload;
	public static String help_migrate;
	public static String help_claim;

	public Messages() {
		loadDefaults();
	}

	public void loadDefaults() {
		nopermission = "&cYou do not have access to that command";
		nopermission_party = "&cYou need to be %rank_name% to perform that command";
		invalidcommand = "&cInvalid command";
		noparty = "&cYou are not in a party";
		canthitmates = "&cYou can't hit your partymates";
		warnondamage = "&c%player% tried to hit %victim%!";
		configurationreloaded = "&aConfiguration reloaded";
		updateavailable = "&9New version of Parties found: %version% (Current: %thisversion%)";
		expgain = "&bYou killed a %mob%, you gained %exp% experience";
		expgainother = "&b%player% has killed a %mob%, you gained %exp% experience";
		defaultjoined = "&bYou entered into %party%";
		serverjoin = "&b%player% is online!";
		serverleave = "&7%player% is offline!";
		
		p_cooldown = "&cYou still have to wait %seconds% seconds";
		p_wrongcmd = "&cWrong variables: Type &7/p <message>";

		create_partycreated = "&l&bYou have created the party %party%\n&r&bType &7/party invite &bfor invite your friends";
		create_partycreated_fixed = "&l&bYou have created the fixed party %party%";
		create_alreadyexist = "&cThe party name %party% already exists, choose a different name";
		create_alreadyinparty = "&cYou are already in a party!";
		create_toolongname = "&cThe party name is too long!";
		create_tooshortname = "&cThe party name is too short!";
		create_invalidname = "&cInvalid characters. Use: a-Z or 0-9.";
		create_censoredname = "&cThe party name contains censored words!";
		create_wrongcmd = "&cWrong variables: Type &7/party create <name>";

		password_changed = "&aYou changed the password of the party";
		password_removed = "&aYou removed the password of the party";
		password_broadcast = "%player% changed the password of the party";
		password_invalidchars = "&cInvalid characters. Use: a-Z or 0-9. Min 1 and max 16 chars.";
		password_wrongcmd = "&cWrong variables: Type &7/party password <password>";
		
		join_joined = "&aYou joined the party %party%";
		join_playerjoined = "&b&l%player% joined the party";
		join_maxplayers = "&cThe party is full!";
		join_alreadyinparty = "&cYou are already in a party!";
		join_wrongpw = "&cWrong password!";
		join_noexist = "&cThis party doesn't exist";
		join_wrongcmd = "&cWrong variables: Type &7/party join <party> [password]";
		
		accept_welcomeplayer = "&b&lYou joined party %party%";
		accept_playerjoined = "&b&l%player% joined the party";
		accept_inviteaccepted = "&a%player% accepted your invite";
		accept_accepted = "&aYou accepted the party invite";
		accept_maxplayers = "&cThe party is full!";
		accept_alreadyinparty = "&cYou are already in a party!";
		accept_noinvite = "&cYou have not pending invitation";
		accept_noexist = "&cThe party dont exist anymore";
		
		deny_invitedenied = "&7%player% denied your invite";
		deny_denied = "&7You denied the party invite";
		deny_noinvite = "&cYou have not pending invitation";
		deny_noexist = "&cThe party dont exist anymore";
		
		ignore_header = "&b&lList ignored parties:";
		ignore_color = "&c";
		ignore_separator = "&7, ";
		ignore_empty = "&7Nobody";
		ignore_ignored = "&7Ignoring %party% invites";
		ignore_deignored = "&7You stopped ignoring %party%";
		ignore_noexist = "&cThis party doesnt exist";
		ignore_wrongcmd = "&cWrong variables: Type &7/party ignore [party]";

		leave_byeplayer = "&b&lYou left the party %party%";
		leave_playerleaved = "&b&l%player% left the party";
		leave_disbanded = "&6&lThe party has been disbanded because the leader left";

		info_content = new ArrayList<String>();
		info_content.add("&b============ &l%party%'s Info &r&b============");
		info_content.add("&b&lDescription&r&7: %desc%");
		info_content.add("&b&lMotd&r&7: %motd%");
		info_content.add("&bLeader&7: %list_leader%");
		info_content.add("&bMods&7: %list_moderator%");
		info_content.add("&bMembers&7: %list_member%");
		info_content.add("&bOnline&7: %online%");
		info_online = "&b";
		info_offline = "&7";
		info_separator = "&7, ";
		info_empty = "&7Nobody";
		members_someone = "&6Someone";
		info_missing = "&7Miss";
		info_noexist = "&cThe party %party% doesn't exist";
		info_wrongcmd = "&cWrong variables: Type &7/party info [party]";
		
		members_content = new ArrayList<String>();
		members_content.add("&b&lMember list of the party %party%");
		members_content.add("&b&lLeader&r&7: %list_leader%");
		members_content.add("&bMods&7: %list_moderator%");
		members_content.add("&bMembers&7: %list_member%");
		members_online = "&b";
		members_offline = "&7";
		members_someone = "&6Someone";
		members_separator = "&7, ";
		members_empty = "&7Nobody";
		members_noexist = "&cThe party %party% doesn't exist";
		members_wrongcmd = "&cWrong variables: Type &7/party members [party]";
		
		desc_changed = "&bParty description changed into %desc%";
		desc_removed = "&bParty description removed";
		desc_invalidchars = "&cInvalid characters. Use: a-Z or 0-9. Min 3 and max 16 chars.";
		desc_censored = "&cThe description contains censored words!";
		desc_wrongcmd = "&cWrong variables: Type &7/party desc <description/remove>";
		
		motd_changed = "&bParty MOTD changed";
		motd_removed = "&bParty MOTD removed";
		motd_header = "&bParty MOTD:";
		motd_color = "&b";
		motd_footer = "";
		motd_invalidchars = "&cInvalid characters. You can use '. , /'. Min 3 and max 100 chars.";
		motd_censored = "&cThe motd contains censored words!";
		motd_wrongcmd = "&cWrong variables: Type &7/party motd <motd/remove>";

		chat_enabled = "&aChat set to party";
		chat_disabled = "&aChat set to public";
		chat_wrongcmd = "&cWrong variables: Type &7/party chat [on/off]";

		list_header = "&b============ &lOnline Parties List &r&b============";
		list_subheader = "";
		list_offline = "&7Empty";
		list_formatparty = "&b%party% &7[&6Online %players%&7] %desc%";
		list_footer = "&b================ &lPage %page% of %maxpages% &r&b================";
		list_wrongcmd = "&cWrong variables: Type &7/party list [page]";

		invite_onlyonline = "&cYou can invite only online players";
		invite_maxplayers = "&cThe party is full!";
		invite_alreadyparty = "&c%player% is already in a party";
		invite_alreadyinvited = "&c%player% was already invited";
		invite_playernopex = "&c%player% doesn't have the permission to join";
		invite_send = "&bYou invited %player% in your party";
		invite_rec = "&b%player% has invited you in his party";
		invite_timeout = "&7You didn't accept the party invite";
		invite_noresponse = "&7%player% didn't accept the party invite";
		invite_revoked_send = "&7Revoked invite sended to %player%";
		invite_revoked_rec = "&7Invite received from %party% has been revoked";
		invite_wrongcmd = "&cWrong variables: Type &7/party invite <name>";

		color_info = "&bYour party color is: %color_code%%color_command%";
		color_empty = "&bYour party doesn't have a color";
		color_changed = "&bParty color changed into %color_command%";
		color_removed = "&bParty color removed";
		color_wrongcolor = "&cColor not found. You can select: red, green or special!";
		color_wrongcmd = "&cWrong variables: Type &7/party color <color>";

		prefix_changed = "&bParty prefix changed into %prefix%";
		prefix_removed = "&bParty prefix removed";
		prefix_invalidchars = "&cInvalid characters. Use: a-Z or 0-9. Min 3 and max 6 chars.";
		prefix_censored = "&cThe prefix contains censored words!";
		prefix_wrongcmd = "&cWrong variables: Type &7/party prefix <prefix/remove>";
		
		suffix_changed = "&bParty suffix changed into %suffix%";
		suffix_removed = "&bParty prefix removed";
		suffix_invalidchars = "&cInvalid characters. Use: a-Z or 0-9. Min 3 and max 6 chars.";
		suffix_censored = "&cThe suffix contains censored words!";
		suffix_removed = "&cOnly leaders can set the suffix";
		
		kick_kicksend = "&aYou kicked %player% from your party!";
		kick_uprank = "&cYou cannot kick your superior!";
		kick_kickedfrom = "&bYou have been kicked from %party%";
		kick_kickedplayer = "&b&l%player% has been kicked from the party";
		kick_kicksendother = "&cYou kicked %player% from the party";
		kick_nomemberother = "&c%player% is not in a party";
		kick_nomember = "&c%player% is not in your party";
		kick_wrongcmd = "&cWrong variables: Type &7/party kick <name>";
		kick_playersconflict_content = new ArrayList<String>();
		kick_playersconflict_content.add("&cWe have found some players with that name:");
		kick_playersconflict_content.add("%list_players%");
		kick_playersconflict_content.add("&cUse '&7/party kick <username> <number>&c' to kick the right player");
		kick_playersconflict_player = "&7[%number%] &6%username%&7 [&b%party%&7]: last login %time% %date%";
		

		delete_deleted = "&aParty %party% deleted";
		delete_deleted_silent = "&aParty %party% deleted silently";
		delete_warn = "&6&lYour party has been deleted";
		delete_noexist = "&cThe party %party% doesn't exist";
		delete_wrongcmd = "&cWrong variables: Type &7/party delete <party> [silent]";
		
		rename_renamed = "&aThe party %old% has been renamed into %party%";
		rename_broadcast = "&6Your party has been renamed into %party%!";
		rename_noexist = "&cThe party %party% doesn't exist";
		rename_wrongcmd = "&cWrong variables: Type &7/party rename <newname>";
		rename_wrongcmd_admin = "&cWrong variables: Type &7/party rename <party> <newname>";
		
		rank_promoted = "&b&l%player% rank changed into %rank_name%";
		rank_nomember = "&c%player% is not in your party";
		rank_noparty = "&c%player% doesn't have a party";
		rank_alreadyrank = "&c%player% is already %rank_name%!";
		rank_lowrank = "&cYou cannot edit players with an equivalent or higher rank!";
		rank_tohigherrank = "&cYou cannot promote to a rank equivalent or higher than yours!";
		rank_wrongrank = "&cRank '%rank_name%' doesn't exist!";
		rank_nodegradeyourself = "&cYou cannot degrade yourself!";
		rank_nopromoteyourself = "&cYou cannot promote yourself!";
		rank_wrongcmd = "&cWrong variables: Type &7/party rank <player> <rank>";
		
		sethome_set = "&aThe party has a new home!";
		sethome_wrongcmd = "&cWrong variables: Type &7/party sethome";
		
		home_teleported = "&7Teleported to the party home";
		home_in = "&7You will be teleported in %time% seconds...";
		home_denied = "&7Teleport denied";
		home_nohome = "&cThere isn't a home yet";
		home_noexist = "&cThe party %party% doesn't exist";
		home_wrongcmd = "&cWrong variables: Type &7/party home";
		home_wrongcmd_admin = "&cWrong variables: Type &7/party home [party]";
		
		teleport_teleporting = "&7Teleporting your partymates here!";
		teleport_playerteleported = "&bTeleported to %player%";
		teleport_delay = "&cYou have to wait %seconds% seconds!";
		teleport_wrongcmd = "&cWrong variables: Type &7/party teleport";
		
		claim_done = "&aGranted permission to the party";
		claim_removed = "&aRemoved permission to the party";
		claim_nomanager = "&cYou need to be manager of the claim";
		claim_noexistclaim = "&cClaim not found";
		claim_wrongcmd = "&cWrong variables: Type &7/party claim <permission>\n&cPermissions: trust, container & access";
		
		spy_active = "&7Now you are a spy!";
		spy_disable = "&7You are no longer a spy";

		migrate_info = "&aYou are currently using: %database%\n&aYou can migrate with: &7/party migrate <from> <to>";
		migrate_complete = "&aCopied data from %database_from% to %database_to%";
		migrate_failed_offline = "&cThe %database% database must be online!";
		migrate_failed_migration = "&cDatabase migration from %database_from% to %database_to% failed!";
		migrate_failed_same = "&cYou need to choose 2 different databases!";
		migrate_wrongdatabase = "&cDatabase not found. You can select: YAML, MySQL!";
		migrate_wrongcmd = "&cWrong variables: Type &7/party migrate <from> <to>";
		
		vault_create_nomoney = "&cYou don't have enough money to create a party [%price%$]";
		vault_join_nomoney = "&cYou don't have enough money to join a party [%price%$]";
		vault_home_nomoney = "&cYou don't have enough money to use the home command [%price%$]";
		vault_sethome_nomoney = "&cYou don't have enough money to set the home [%price%$]";
		vault_desc_nomoney = "&cYou don't have enough money to set the description [%price%$]";
		vault_motd_nomoney = "&cYou don't have enough money to set the MOTD [%price%$]";
		vault_prefix_nomoney = "&cYou don't have enough money to set the prefix [%price%$]";
		vault_suffix_nomoney = "&cYou don't have enough money to set the suffix [%price%$]";
		vault_teleport_nomoney = "&cYou don't have enough money to perform a teleport [%price%$]";
		vault_claim_nomoney = "&cYou don't have enough money to perform a claim [%price%$]";
		vault_confirm_anycmd = "&cThere is no command to confirm";
		vault_confirm_warnonbuy = "&aCommand '%cmd%' costs %price%$.\nType &7/party confirm&a to use it.";
		vault_confirm_confirmed = "&aPerforming the command.";
		vault_confirm_wrongcmd = "&cWrong variables: Type &7/party confirm";
		
		follow_following_world = "&7Following %player% in %world%";
		follow_following_server = "&7Following party in %server%";
		
		help_header = "&b================= &lParty Help %page%/%maxpages% &r&b=================";
		help_help = "&b/party help [page] &7- Show help pages";
		help_p = "&b/p <message> &7- Send a message to the party";
		help_create = "&b/party create <name> &7- Create a new party";
		help_createfixed = "&b/party create <name> [fixed] &7- Create a new party";
		help_join = "&b/party join <party> [password] &7- Join into a party";
		help_accept = "&b/party accept &7- Accept a party invitation";
		help_deny = "&b/party deny &7- Deny a party invitation";
		help_ignore = "&b/party ignore [party] &7- Add/remove/show parties ignored";
		help_leave = "&b/party leave &7- Leave your party";
		help_info = "&b/party info [party] &7- Show party information";
		help_members = "&b/party members [party] &7- Show the members list";
		help_home = "&b/party home &7- Teleport to the party home";
		help_home_others = "&b/party home [party] &7- Teleport to the party home";
		help_sethome = "&b/party sethome &7- Set the party home";
		help_teleport = "&b/party teleport &7- Teleport your partymates to you";
		help_desc = "&b/party desc <description/remove> &7- Add/remove description";
		help_motd = "&b/party motd <motd/remove> &7- Add/remove motd";
		help_chat = "&b/party chat [on/off]&7- Toggle the party chat";
		help_list = "&b/party list [page] &7- List of online parties";
		help_invite = "&b/party invite <player> &7- Invite a player to your party";
		help_password = "&b/party password <password/remove> &7- Change password of the party";
		help_rank = "&b/party rank <player> <rank> &7- Change rank of the player";
		help_color = "&b/party color <color> &7- Change color of the party";
		help_prefix = "&b/party prefix <prefix/remove> &7- Add/remove prefix tag";
		help_suffix = "&b/party suffix <suffix/remove> &7- Add/remove suffix tag";
		help_kick = "&b/party kick <player> &7- Kick a player from your party";
		help_spy = "&b/party spy &7- Spy messages other parties";
		help_delete = "&b/party delete <party> &7- Delete the party";
		help_rename = "&b/party rename <newname> &7- Rename the party";
		help_rename_others = "&b/party rename <party> <newname> &7- Renames a party";
		help_reload = "&b/party reload &7- Reload the configuration";
		help_migrate = "&b/party migrate <from> <to> &7- Copy database into a new one";
		help_claim = "&b/party claim <permission> &7- Grant permissions to the claim";
	}
}
