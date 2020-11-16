package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.parties.common.PartiesPlugin;

import java.util.List;

public abstract class Messages extends ConfigurationFile {
	// Parties messages
	@ConfigOption(path = "parties.update-available")
	public static String PARTIES_UPDATEAVAILABLE;
	@ConfigOption(path = "parties.configuration-outdated")
	public static String PARTIES_CONFIGURATION_OUTDATED;
	
	@ConfigOption(path = "parties.common-messages.invalid-command")
	public static String PARTIES_COMMON_INVALIDCMD;
	@ConfigOption(path = "parties.common-messages.configuration-reloaded")
	public static String PARTIES_COMMON_CONFIGRELOAD;
	@ConfigOption(path = "parties.common-messages.not-in-party")
	public static String PARTIES_COMMON_NOTINPARTY;
	@ConfigOption(path = "parties.common-messages.already-in-party")
	public static String PARTIES_COMMON_ALREADYINPARTY;
	@ConfigOption(path = "parties.common-messages.party-not-found")
	public static String PARTIES_COMMON_PARTYNOTFOUND;
	@ConfigOption(path = "parties.common-messages.party-full")
	public static String PARTIES_COMMON_PARTYFULL;
	
	@ConfigOption(path = "parties.options.enabled")
	public static String PARTIES_OPTIONS_ENABLED;
	@ConfigOption(path = "parties.options.disabled")
	public static String PARTIES_OPTIONS_DISABLED;
	@ConfigOption(path = "parties.options.toggled-on")
	public static String PARTIES_OPTIONS_TOGGLED_ON;
	@ConfigOption(path = "parties.options.toggled-off")
	public static String PARTIES_OPTIONS_TOGGLED_OFF;
	@ConfigOption(path = "parties.options.word-yes")
	public static String PARTIES_OPTIONS_WORD_YES;
	@ConfigOption(path = "parties.options.word-no")
	public static String PARTIES_OPTIONS_WORD_NO;
	@ConfigOption(path = "parties.options.empty")
	public static String PARTIES_OPTIONS_EMPTY;
	@ConfigOption(path = "parties.options.none")
	public static String PARTIES_OPTIONS_NONE;
	
	@ConfigOption(path = "parties.syntax.wrong-message")
	public static String PARTIES_SYNTAX_WRONG_MESSAGE;
	@ConfigOption(path = "parties.syntax.color")
	public static String PARTIES_SYNTAX_COLOR;
	@ConfigOption(path = "parties.syntax.description")
	public static String PARTIES_SYNTAX_DESCRIPTION;
	@ConfigOption(path = "parties.syntax.experience")
	public static String PARTIES_SYNTAX_EXPERIENCE;
	@ConfigOption(path = "parties.syntax.kills")
	public static String PARTIES_SYNTAX_KILLS;
	@ConfigOption(path = "parties.syntax.members")
	public static String PARTIES_SYNTAX_MEMBERS;
	@ConfigOption(path = "parties.syntax.message")
	public static String PARTIES_SYNTAX_MESSAGE;
	@ConfigOption(path = "parties.syntax.motd")
	public static String PARTIES_SYNTAX_MOTD;
	@ConfigOption(path = "parties.syntax.name")
	public static String PARTIES_SYNTAX_NAME;
	@ConfigOption(path = "parties.syntax.online-members")
	public static String PARTIES_SYNTAX_ONLINE_MEMBERS;
	@ConfigOption(path = "parties.syntax.order")
	public static String PARTIES_SYNTAX_ORDER;
	@ConfigOption(path = "parties.syntax.page")
	public static String PARTIES_SYNTAX_PAGE;
	@ConfigOption(path = "parties.syntax.party")
	public static String PARTIES_SYNTAX_PARTY;
	@ConfigOption(path = "parties.syntax.password")
	public static String PARTIES_SYNTAX_PASSWORD;
	@ConfigOption(path = "parties.syntax.player")
	public static String PARTIES_SYNTAX_PLAYER;
	@ConfigOption(path = "parties.syntax.rank")
	public static String PARTIES_SYNTAX_RANK;
	@ConfigOption(path = "parties.syntax.tag")
	public static String PARTIES_SYNTAX_TAG;
	
	@ConfigOption(path = "parties.permissions.no-permission")
	public static String PARTIES_PERM_NOPERM;
	@ConfigOption(path = "parties.permissions.no-permission-in-party-general")
	public static String PARTIES_PERM_NORANK_GENERAL;
	@ConfigOption(path = "parties.permissions.no-permission-in-party-rank")
	public static String PARTIES_PERM_NORANK_UPRANK;
	
	@ConfigOption(path = "parties.out-party")
	public static String PARTIES_OUT_PARTY;
	
	@ConfigOption(path = "parties.list.player-online-format")
	public static String PARTIES_LIST_ONLINEFORMAT;
	@ConfigOption(path = "parties.list.player-offline-format")
	public static String PARTIES_LIST_OFFLINEFORMAT;
	@ConfigOption(path = "parties.list.player-separator")
	public static String PARTIES_LIST_SEPARATOR;
	@ConfigOption(path = "parties.list.player-empty")
	public static String PARTIES_LIST_EMPTY;
	@ConfigOption(path = "parties.list.player-unknown")
	public static String PARTIES_LIST_UNKNOWN;
	@ConfigOption(path = "parties.list.missing-value")
	public static String PARTIES_LIST_MISSING;
	
	@ConfigOption(path = "parties.formats.party-chat")
	public static String PARTIES_FORMATS_PARTY_CHAT;
	@ConfigOption(path = "parties.formats.spy.party-chat")
	public static String PARTIES_FORMATS_SPY_PARTY_CHAT;
	@ConfigOption(path = "parties.formats.spy.broadcast")
	public static String PARTIES_FORMATS_SPY_BROADCAST;
	
	// Main commands messages
	@ConfigOption(path = "main-commands.accept.no-request")
	public static String MAINCMD_ACCEPT_NOREQUEST;
	@ConfigOption(path = "main-commands.accept.no-exists")
	public static String MAINCMD_ACCEPT_NOEXISTS;
	@ConfigOption(path = "main-commands.accept.multiple-requests")
	public static String MAINCMD_ACCEPT_MULTIPLEREQUESTS;
	@ConfigOption(path = "main-commands.accept.multiple-requests-party")
	public static String MAINCMD_ACCEPT_MULTIPLEREQUESTS_PARTY;
	@ConfigOption(path = "main-commands.accept.multiple-requests-player")
	public static String MAINCMD_ACCEPT_MULTIPLEREQUESTS_PLAYER;
	
	@ConfigOption(path = "main-commands.chat.enabled")
	public static String MAINCMD_CHAT_ENABLED;
	@ConfigOption(path = "main-commands.chat.disabled")
	public static String MAINCMD_CHAT_DISABLED;
	
	@ConfigOption(path = "main-commands.create.created")
	public static String MAINCMD_CREATE_CREATED;
	@ConfigOption(path = "main-commands.create.created-fixed")
	public static String MAINCMD_CREATE_CREATEDFIXED;
	@ConfigOption(path = "main-commands.create.console-must-fixed")
	public static String MAINCMD_CREATE_CONSOLE_MUST_FIXED;
	@ConfigOption(path = "main-commands.create.name-already-exists")
	public static String MAINCMD_CREATE_NAMEEXISTS;
	@ConfigOption(path = "main-commands.create.name-too-long")
	public static String MAINCMD_CREATE_NAMETOOLONG;
	@ConfigOption(path = "main-commands.create.name-too-short")
	public static String MAINCMD_CREATE_NAMETOOSHORT;
	@ConfigOption(path = "main-commands.create.invalid-name")
	public static String MAINCMD_CREATE_INVALIDNAME;
	@ConfigOption(path = "main-commands.create.censored")
	public static String MAINCMD_CREATE_CENSORED;
	
	@ConfigOption(path = "main-commands.delete.deleted")
	public static String MAINCMD_DELETE_DELETED;
	@ConfigOption(path = "main-commands.delete.deleted-silently")
	public static String MAINCMD_DELETE_DELETEDSILENTLY;
	@ConfigOption(path = "main-commands.delete.broadcast")
	public static String MAINCMD_DELETE_BROADCAST;
	
	@ConfigOption(path = "main-commands.deny.no-request")
	public static String MAINCMD_DENY_NOREQUEST;
	@ConfigOption(path = "main-commands.deny.no-exists")
	public static String MAINCMD_DENY_NOEXISTS;
	@ConfigOption(path = "main-commands.deny.multiple-requests")
	public static String MAINCMD_DENY_MULTIPLEREQUESTS;
	@ConfigOption(path = "main-commands.deny.multiple-requests-party")
	public static String MAINCMD_DENY_MULTIPLEREQUESTS_PARTY;
	@ConfigOption(path = "main-commands.deny.multiple-requests-player")
	public static String MAINCMD_DENY_MULTIPLEREQUESTS_PLAYER;
	
	@ConfigOption(path = "main-commands.ignore.start-ignore")
	public static String MAINCMD_IGNORE_START;
	@ConfigOption(path = "main-commands.ignore.stop-ignore")
	public static String MAINCMD_IGNORE_STOP;
	@ConfigOption(path = "main-commands.ignore.ignore-list.header")
	public static String MAINCMD_IGNORE_LIST_HEADER;
	@ConfigOption(path = "main-commands.ignore.ignore-list.party-prefix")
	public static String MAINCMD_IGNORE_LIST_PARTYPREFIX;
	@ConfigOption(path = "main-commands.ignore.ignore-list.separator")
	public static String MAINCMD_IGNORE_LIST_SEPARATOR;
	@ConfigOption(path = "main-commands.ignore.ignore-list.empty")
	public static String MAINCMD_IGNORE_LIST_EMPTY;
	
	@ConfigOption(path = "main-commands.info.content")
	public static List<String> MAINCMD_INFO_CONTENT;
	
	@ConfigOption(path = "main-commands.invite.sent")
	public static String MAINCMD_INVITE_SENT;
	@ConfigOption(path = "main-commands.invite.player-invited")
	public static String MAINCMD_INVITE_PLAYERINVITED;
	@ConfigOption(path = "main-commands.invite.accept.broadcast")
	public static String MAINCMD_INVITE_ACCEPT_BROADCAST;
	@ConfigOption(path = "main-commands.invite.accept.accepted")
	public static String MAINCMD_INVITE_ACCEPT_ACCEPTED;
	@ConfigOption(path = "main-commands.invite.accept.receipt")
	public static String MAINCMD_INVITE_ACCEPT_RECEIPT;
	@ConfigOption(path = "main-commands.invite.deny.broadcast")
	public static String MAINCMD_INVITE_DENY_BROADCAST;
	@ConfigOption(path = "main-commands.invite.deny.denied")
	public static String MAINCMD_INVITE_DENY_DENIED;
	@ConfigOption(path = "main-commands.invite.deny.receipt")
	public static String MAINCMD_INVITE_DENY_RECEIPT;
	@ConfigOption(path = "main-commands.invite.timeout.no-response")
	public static String MAINCMD_INVITE_TIMEOUT_NORESPONSE;
	@ConfigOption(path = "main-commands.invite.timeout.timeout")
	public static String MAINCMD_INVITE_TIMEOUT_TIMEOUT;
	@ConfigOption(path = "main-commands.invite.revoke.sent-revoked")
	public static String MAINCMD_INVITE_REVOKE_SENT;
	@ConfigOption(path = "main-commands.invite.revoke.player-invite-revoked")
	public static String MAINCMD_INVITE_REVOKE_REVOKED;
	@ConfigOption(path = "main-commands.invite.cooldown.global")
	public static String MAINCMD_INVITE_COOLDOWN_GLOBAL;
	@ConfigOption(path = "main-commands.invite.cooldown.individual")
	public static String MAINCMD_INVITE_COOLDOWN_INDIVIDUAL;
	@ConfigOption(path = "main-commands.invite.player-offline")
	public static String MAINCMD_INVITE_PLAYEROFFLINE;
	@ConfigOption(path = "main-commands.invite.player-no-permission")
	public static String MAINCMD_INVITE_PLAYERNOPERM;
	@ConfigOption(path = "main-commands.invite.player-in-party")
	public static String MAINCMD_INVITE_PLAYERINPARTY;
	@ConfigOption(path = "main-commands.invite.already-invited")
	public static String MAINCMD_INVITE_ALREADYINVITED;
	
	@ConfigOption(path = "main-commands.kick.sent")
	public static String MAINCMD_KICK_SENT;
	@ConfigOption(path = "main-commands.kick.player-kicked")
	public static String MAINCMD_KICK_PLAYERKICKED;
	@ConfigOption(path = "main-commands.kick.broadcast")
	public static String MAINCMD_KICK_BROADCAST;
	@ConfigOption(path = "main-commands.kick.broadcast-disband")
	public static String MAINCMD_KICK_BROADCAST_DISBAND;
	@ConfigOption(path = "main-commands.kick.broadcast-leader-changed")
	public static String MAINCMD_KICK_BROADCAST_LEADER_CHANGED;
	@ConfigOption(path = "main-commands.kick.player-higher-rank")
	public static String MAINCMD_KICK_PLAYERHIGHERRANK;
	@ConfigOption(path = "main-commands.kick.player-not-in-party")
	public static String MAINCMD_KICK_PLAYERNOTINPARTY;
	@ConfigOption(path = "main-commands.kick.player-not-in-other-party")
	public static String MAINCMD_KICK_PLAYERNOTINPARTY_OTHER;
	@ConfigOption(path = "main-commands.kick.players-conflict.content")
	public static List<String> MAINCMD_KICK_CONFLICT_CONTENT;
	@ConfigOption(path = "main-commands.kick.players-conflict.player")
	public static String MAINCMD_KICK_CONFLICT_PLAYER;
	
	@ConfigOption(path = "main-commands.leave.left")
	public static String MAINCMD_LEAVE_LEFT;
	@ConfigOption(path = "main-commands.leave.broadcast")
	public static String MAINCMD_LEAVE_BROADCAST;
	@ConfigOption(path = "main-commands.leave.party-disbanded")
	public static String MAINCMD_LEAVE_DISBANDED;
	@ConfigOption(path = "main-commands.leave.leader-changed")
	public static String MAINCMD_LEAVE_LEADER_CHANGED;
	
	@ConfigOption(path = "main-commands.p.cooldown")
	public static String MAINCMD_P_COOLDOWN;
	@ConfigOption(path = "main-commands.p.censored")
	public static String MAINCMD_P_CENSORED;
	@ConfigOption(path = "main-commands.p.muted")
	public static String MAINCMD_P_MUTED;
	
	@ConfigOption(path = "main-commands.rank.changed")
	public static String MAINCMD_RANK_CHANGED;
	@ConfigOption(path = "main-commands.rank.broadcast")
	public static String MAINCMD_RANK_BROADCAST;
	@ConfigOption(path = "main-commands.rank.wrong-rank")
	public static String MAINCMD_RANK_WRONGRANK;
	@ConfigOption(path = "main-commands.rank.same-rank")
	public static String MAINCMD_RANK_SAMERANK;
	@ConfigOption(path = "main-commands.rank.low-rank")
	public static String MAINCMD_RANK_LOWRANK;
	@ConfigOption(path = "main-commands.rank.to-higher-rank")
	public static String MAINCMD_RANK_TOHIGHERRANK;
	@ConfigOption(path = "main-commands.rank.fixed-leader")
	public static String MAINCMD_RANK_FIXEDLEADER;
	@ConfigOption(path = "main-commands.rank.changing-yourself")
	public static String MAINCMD_RANK_CHANGINGYOURSELF;
	@ConfigOption(path = "main-commands.rank.player-not-in-party")
	public static String MAINCMD_RANK_PLAYERNOTINPARTY;
	@ConfigOption(path = "main-commands.rank.player-not-in-other-party")
	public static String MAINCMD_RANK_PLAYERNOTINPARTY_OTHER;
	@ConfigOption(path = "main-commands.rank.players-conflict.content")
	public static List<String> MAINCMD_RANK_CONFLICT_CONTENT;
	@ConfigOption(path = "main-commands.rank.players-conflict.player")
	public static String MAINCMD_RANK_CONFLICT_PLAYER;
	
	@ConfigOption(path = "main-commands.rename.renamed")
	public static String MAINCMD_RENAME_RENAMED;
	@ConfigOption(path = "main-commands.rename.broadcast")
	public static String MAINCMD_RENAME_BROADCAST;
	@ConfigOption(path = "main-commands.rename.cooldown")
	public static String MAINCMD_RENAME_COOLDOWN;
	
	@ConfigOption(path = "main-commands.spy.enabled")
	public static String MAINCMD_SPY_ENABLED;
	@ConfigOption(path = "main-commands.spy.disabled")
	public static String MAINCMD_SPY_DISABLED;
	
	@ConfigOption(path = "main-commands.version.updated")
	public static String MAINCMD_VERSION_UPDATED;
	@ConfigOption(path = "main-commands.version.outdated")
	public static String MAINCMD_VERSION_OUTDATED;

	
	// Additional commands messages
	@ConfigOption(path = "additional-commands.ask.sent")
	public static String ADDCMD_ASK_SENT;
	@ConfigOption(path = "additional-commands.ask.received")
	public static String ADDCMD_ASK_RECEIVED;
	@ConfigOption(path = "additional-commands.ask.accept.broadcast")
	public static String ADDCMD_ASK_ACCEPT_BROADCAST;
	@ConfigOption(path = "additional-commands.ask.accept.accepted")
	public static String ADDCMD_ASK_ACCEPT_ACCEPTED;
	@ConfigOption(path = "additional-commands.ask.accept.receipt")
	public static String ADDCMD_ASK_ACCEPT_RECEIPT;
	@ConfigOption(path = "additional-commands.ask.deny.broadcast")
	public static String ADDCMD_ASK_DENY_BROADCAST;
	@ConfigOption(path = "additional-commands.ask.deny.denied")
	public static String ADDCMD_ASK_DENY_DENIED;
	@ConfigOption(path = "additional-commands.ask.deny.receipt")
	public static String ADDCMD_ASK_DENY_RECEIPT;
	@ConfigOption(path = "additional-commands.ask.timeout.no-response")
	public static String ADDCMD_ASK_TIMEOUT_NORESPONSE;
	@ConfigOption(path = "additional-commands.ask.cooldown.global")
	public static String ADDCMD_ASK_COOLDOWN_GLOBAL;
	@ConfigOption(path = "additional-commands.ask.cooldown.individual")
	public static String ADDCMD_ASK_COOLDOWN_INDIVIDUAL;
	
	@ConfigOption(path = "additional-commands.color.info")
	public static String ADDCMD_COLOR_INFO;
	@ConfigOption(path = "additional-commands.color.empty")
	public static String ADDCMD_COLOR_EMPTY;
	@ConfigOption(path = "additional-commands.color.changed")
	public static String ADDCMD_COLOR_CHANGED;
	@ConfigOption(path = "additional-commands.color.removed")
	public static String ADDCMD_COLOR_REMOVED;
	@ConfigOption(path = "additional-commands.color.broadcast")
	public static String ADDCMD_COLOR_BROADCAST;
	@ConfigOption(path = "additional-commands.color.wrong-color")
	public static String ADDCMD_COLOR_WRONGCOLOR;
	
	@ConfigOption(path = "additional-commands.debug.config.header")
	public static String ADDCMD_DEBUG_CONFIG_HEADER;
	@ConfigOption(path = "additional-commands.debug.config.text")
	public static List<String> ADDCMD_DEBUG_CONFIG_TEXT;
	@ConfigOption(path = "additional-commands.debug.config.rank-format")
	public static String ADDCMD_DEBUG_CONFIG_RANK_FORMAT;
	@ConfigOption(path = "additional-commands.debug.config.rank-separator")
	public static String ADDCMD_DEBUG_CONFIG_RANK_SEPARATOR;
	
	@ConfigOption(path = "additional-commands.debug.exp.header")
	public static String ADDCMD_DEBUG_EXP_HEADER;
	@ConfigOption(path = "additional-commands.debug.exp.text")
	public static List<String> ADDCMD_DEBUG_EXP_TEXT;
	@ConfigOption(path = "additional-commands.debug.exp.level-options.progressive")
	public static String ADDCMD_DEBUG_EXP_LEVEL_OPTIONS_PROGRESSIVE;
	@ConfigOption(path = "additional-commands.debug.exp.level-options.fixed")
	public static String ADDCMD_DEBUG_EXP_LEVEL_OPTIONS_FIXED;
	
	@ConfigOption(path = "additional-commands.debug.party.header")
	public static String ADDCMD_DEBUG_PARTY_HEADER;
	@ConfigOption(path = "additional-commands.debug.party.text")
	public static List<String> ADDCMD_DEBUG_PARTY_TEXT;
	
	@ConfigOption(path = "additional-commands.debug.player.header")
	public static String ADDCMD_DEBUG_PLAYER_HEADER;
	@ConfigOption(path = "additional-commands.debug.player.text")
	public static List<String> ADDCMD_DEBUG_PLAYER_TEXT;
	@ConfigOption(path = "additional-commands.debug.player.player-offline")
	public static String ADDCMD_DEBUG_PLAYER_PLAYER_OFFLINE;
	
	@ConfigOption(path = "additional-commands.desc.changed")
	public static String ADDCMD_DESC_CHANGED;
	@ConfigOption(path = "additional-commands.desc.removed")
	public static String ADDCMD_DESC_REMOVED;
	@ConfigOption(path = "additional-commands.desc.broadcast")
	public static String ADDCMD_DESC_BROADCAST;
	@ConfigOption(path = "additional-commands.desc.invalid-chars")
	public static String ADDCMD_DESC_INVALID;
	@ConfigOption(path = "additional-commands.desc.censored")
	public static String ADDCMD_DESC_CENSORED;
	
	@ConfigOption(path = "additional-commands.exp.party.gained")
	public static String ADDCMD_EXP_PARTY_GAINED;
	@ConfigOption(path = "additional-commands.exp.level.level-up")
	public static String ADDCMD_EXP_PARTY_LEVEL_LEVEL_UP;
	
	@ConfigOption(path = "additional-commands.follow.toggle-on")
	public static String ADDCMD_FOLLOW_ON;
	@ConfigOption(path = "additional-commands.follow.toggle-off")
	public static String ADDCMD_FOLLOW_OFF;
	
	@ConfigOption(path = "additional-commands.join.joined")
	public static String ADDCMD_JOIN_JOINED;
	@ConfigOption(path = "additional-commands.join.player-joined")
	public static String ADDCMD_JOIN_PLAYERJOINED;
	@ConfigOption(path = "additional-commands.join.wrong-password")
	public static String ADDCMD_JOIN_WRONGPASSWORD;
	
	@ConfigOption(path = "additional-commands.list.header")
	public static String ADDCMD_LIST_HEADER;
	@ConfigOption(path = "additional-commands.list.footer")
	public static String ADDCMD_LIST_FOOTER;
	@ConfigOption(path = "additional-commands.list.no-one")
	public static String ADDCMD_LIST_NOONE;
	@ConfigOption(path = "additional-commands.list.format-party")
	public static String ADDCMD_LIST_FORMATPARTY;
	@ConfigOption(path = "additional-commands.list.invalid-order")
	public static String ADDCMD_LIST_INVALID_ORDER;
	
	@ConfigOption(path = "additional-commands.motd.changed")
	public static String ADDCMD_MOTD_CHANGED;
	@ConfigOption(path = "additional-commands.motd.removed")
	public static String ADDCMD_MOTD_REMOVED;
	@ConfigOption(path = "additional-commands.motd.broadcast")
	public static String ADDCMD_MOTD_BROADCAST;
	@ConfigOption(path = "additional-commands.motd.content")
	public static List<String> ADDCMD_MOTD_CONTENT;
	@ConfigOption(path = "additional-commands.motd.invalid-chars")
	public static String ADDCMD_MOTD_INVALID;
	@ConfigOption(path = "additional-commands.motd.censored")
	public static String ADDCMD_MOTD_CENSORED;
	
	@ConfigOption(path = "additional-commands.mute.toggle-on")
	public static String ADDCMD_MUTE_ON;
	@ConfigOption(path = "additional-commands.mute.toggle-off")
	public static String ADDCMD_MUTE_OFF;
	
	@ConfigOption(path = "additional-commands.password.changed")
	public static String ADDCMD_PASSWORD_CHANGED;
	@ConfigOption(path = "additional-commands.password.removed")
	public static String ADDCMD_PASSWORD_REMOVED;
	@ConfigOption(path = "additional-commands.password.broadcast")
	public static String ADDCMD_PASSWORD_BROADCAST;
	@ConfigOption(path = "additional-commands.password.invalid-chars")
	public static String ADDCMD_PASSWORD_INVALID;
	
	@ConfigOption(path = "additional-commands.tag.changed")
	public static String ADDCMD_TAG_CHANGED;
	@ConfigOption(path = "additional-commands.tag.removed")
	public static String ADDCMD_TAG_REMOVED;
	@ConfigOption(path = "additional-commands.tag.broadcast")
	public static String ADDCMD_TAG_BROADCAST;
	@ConfigOption(path = "additional-commands.tag.invalid-chars")
	public static String ADDCMD_TAG_INVALID;
	@ConfigOption(path = "additional-commands.tag.censored")
	public static String ADDCMD_TAG_CENSORED;
	@ConfigOption(path = "additional-commands.tag.already-used")
	public static String ADDCMD_TAG_ALREADY_USED;
	
	@ConfigOption(path = "additional-commands.teleport.teleporting")
	public static String ADDCMD_TELEPORT_TELEPORTING;
	@ConfigOption(path = "additional-commands.teleport.player-teleported")
	public static String ADDCMD_TELEPORT_TELEPORTED;
	@ConfigOption(path = "additional-commands.teleport.cooldown")
	public static String ADDCMD_TELEPORT_COOLDOWN;
	
	
	// Other messages
	@ConfigOption(path = "other.fixed-parties.default-join")
	public static String OTHER_FIXED_DEFAULTJOIN;
	
	@ConfigOption(path = "other.join-leave.server-join")
	public static String OTHER_JOINLEAVE_SERVERJOIN;
	@ConfigOption(path = "other.join-leave.server-leave")
	public static String OTHER_JOINLEAVE_SERVERLEAVE;
	
	
	// Help messages
	@ConfigOption(path = "help.header")
	public static String HELP_HEADER;
	@ConfigOption(path = "help.footer")
	public static String HELP_FOOTER;
	@ConfigOption(path = "help.perform-command")
	public static String HELP_PERFORM_COMMAND;
	@ConfigOption(path = "help.console-help.header")
	public static String HELP_CONSOLEHELP_HEADER;
	@ConfigOption(path = "help.console-help.command")
	public static String HELP_CONSOLEHELP_COMMAND;
	
	@ConfigOption(path = "help.main.commands.help")
	public static String HELP_MAIN_COMMANDS_HELP;
	@ConfigOption(path = "help.main.commands.accept")
	public static String HELP_MAIN_COMMANDS_ACCEPT;
	@ConfigOption(path = "help.main.commands.chat")
	public static String HELP_MAIN_COMMANDS_CHAT;
	@ConfigOption(path = "help.main.commands.create")
	public static String HELP_MAIN_COMMANDS_CREATE;
	@ConfigOption(path = "help.main.commands.delete")
	public static String HELP_MAIN_COMMANDS_DELETE;
	@ConfigOption(path = "help.main.commands.deny")
	public static String HELP_MAIN_COMMANDS_DENY;
	@ConfigOption(path = "help.main.commands.ignore")
	public static String HELP_MAIN_COMMANDS_IGNORE;
	@ConfigOption(path = "help.main.commands.info")
	public static String HELP_MAIN_COMMANDS_INFO;
	@ConfigOption(path = "help.main.commands.invite")
	public static String HELP_MAIN_COMMANDS_INVITE;
	@ConfigOption(path = "help.main.commands.kick")
	public static String HELP_MAIN_COMMANDS_KICK;
	@ConfigOption(path = "help.main.commands.leave")
	public static String HELP_MAIN_COMMANDS_LEAVE;
	@ConfigOption(path = "help.main.commands.p")
	public static String HELP_MAIN_COMMANDS_P;
	@ConfigOption(path = "help.main.commands.rank")
	public static String HELP_MAIN_COMMANDS_RANK;
	@ConfigOption(path = "help.main.commands.reload")
	public static String HELP_MAIN_COMMANDS_RELOAD;
	@ConfigOption(path = "help.main.commands.rename")
	public static String HELP_MAIN_COMMANDS_RENAME;
	@ConfigOption(path = "help.main.commands.spy")
	public static String HELP_MAIN_COMMANDS_SPY;
	@ConfigOption(path = "help.main.commands.version")
	public static String HELP_MAIN_COMMANDS_VERSION;
	
	@ConfigOption(path = "help.main.descriptions.help")
	public static String HELP_MAIN_DESCRIPTIONS_HELP;
	@ConfigOption(path = "help.main.descriptions.accept")
	public static String HELP_MAIN_DESCRIPTIONS_ACCEPT;
	@ConfigOption(path = "help.main.descriptions.chat")
	public static String HELP_MAIN_DESCRIPTIONS_CHAT;
	@ConfigOption(path = "help.main.descriptions.create")
	public static String HELP_MAIN_DESCRIPTIONS_CREATE;
	@ConfigOption(path = "help.main.descriptions.delete")
	public static String HELP_MAIN_DESCRIPTIONS_DELETE;
	@ConfigOption(path = "help.main.descriptions.deny")
	public static String HELP_MAIN_DESCRIPTIONS_DENY;
	@ConfigOption(path = "help.main.descriptions.ignore")
	public static String HELP_MAIN_DESCRIPTIONS_IGNORE;
	@ConfigOption(path = "help.main.descriptions.info")
	public static String HELP_MAIN_DESCRIPTIONS_INFO;
	@ConfigOption(path = "help.main.descriptions.invite")
	public static String HELP_MAIN_DESCRIPTIONS_INVITE;
	@ConfigOption(path = "help.main.descriptions.kick")
	public static String HELP_MAIN_DESCRIPTIONS_KICK;
	@ConfigOption(path = "help.main.descriptions.leave")
	public static String HELP_MAIN_DESCRIPTIONS_LEAVE;
	@ConfigOption(path = "help.main.descriptions.p")
	public static String HELP_MAIN_DESCRIPTIONS_P;
	@ConfigOption(path = "help.main.descriptions.rank")
	public static String HELP_MAIN_DESCRIPTIONS_RANK;
	@ConfigOption(path = "help.main.descriptions.reload")
	public static String HELP_MAIN_DESCRIPTIONS_RELOAD;
	@ConfigOption(path = "help.main.descriptions.rename")
	public static String HELP_MAIN_DESCRIPTIONS_RENAME;
	@ConfigOption(path = "help.main.descriptions.spy")
	public static String HELP_MAIN_DESCRIPTIONS_SPY;
	@ConfigOption(path = "help.main.descriptions.version")
	public static String HELP_MAIN_DESCRIPTIONS_VERSION;
	
	@ConfigOption(path = "help.additional.commands.ask")
	public static String HELP_ADDITIONAL_COMMANDS_ASK;
	@ConfigOption(path = "help.additional.commands.color")
	public static String HELP_ADDITIONAL_COMMANDS_COLOR;
	@ConfigOption(path = "help.additional.commands.debug")
	public static String HELP_ADDITIONAL_COMMANDS_DEBUG;
	@ConfigOption(path = "help.additional.commands.desc")
	public static String HELP_ADDITIONAL_COMMANDS_DESC;
	@ConfigOption(path = "help.additional.commands.follow")
	public static String HELP_ADDITIONAL_COMMANDS_FOLLOW;
	@ConfigOption(path = "help.additional.commands.join")
	public static String HELP_ADDITIONAL_COMMANDS_JOIN;
	@ConfigOption(path = "help.additional.commands.list")
	public static String HELP_ADDITIONAL_COMMANDS_LIST;
	@ConfigOption(path = "help.additional.commands.motd")
	public static String HELP_ADDITIONAL_COMMANDS_MOTD;
	@ConfigOption(path = "help.additional.commands.mute")
	public static String HELP_ADDITIONAL_COMMANDS_MUTE;
	@ConfigOption(path = "help.additional.commands.password")
	public static String HELP_ADDITIONAL_COMMANDS_PASSWORD;
	@ConfigOption(path = "help.additional.commands.teleport")
	public static String HELP_ADDITIONAL_COMMANDS_TELEPORT;
	@ConfigOption(path = "help.additional.commands.tag")
	public static String HELP_ADDITIONAL_COMMANDS_TAG;
	
	@ConfigOption(path = "help.additional.descriptions.ask")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_ASK;
	@ConfigOption(path = "help.additional.descriptions.color")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_COLOR;
	@ConfigOption(path = "help.additional.descriptions.debug")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_DEBUG;
	@ConfigOption(path = "help.additional.descriptions.desc")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_DESC;
	@ConfigOption(path = "help.additional.descriptions.follow")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_FOLLOW;
	@ConfigOption(path = "help.additional.descriptions.join")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_JOIN;
	@ConfigOption(path = "help.additional.descriptions.list")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_LIST;
	@ConfigOption(path = "help.additional.descriptions.motd")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_MOTD;
	@ConfigOption(path = "help.additional.descriptions.mute")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_MUTE;
	@ConfigOption(path = "help.additional.descriptions.password")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_PASSWORD;
	@ConfigOption(path = "help.additional.descriptions.tag")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_TAG;
	@ConfigOption(path = "help.additional.descriptions.teleport")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_TELEPORT;
	
	
	protected Messages(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		loadDefaultConfigOptions();
	}
	
	@Override
	public void loadConfiguration() {
		loadConfigOptions();
	}
}
