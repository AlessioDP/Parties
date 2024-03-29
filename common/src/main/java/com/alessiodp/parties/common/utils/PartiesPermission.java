package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.commands.utils.ADPPermission;

public enum PartiesPermission implements ADPPermission {
	// User
	USER_ACCEPT			("parties.user.accept"),
	USER_ASK			("parties.user.ask"),
	USER_CHAT			("parties.user.chat"),
	USER_CHAT_COLOR		("parties.user.chat.color"),
	USER_CLOSE			("parties.user.close"),
	USER_COLOR			("parties.user.color"),
	USER_CREATE			("parties.user.create"),
	USER_DENY			("parties.user.deny"),
	USER_DESC			("parties.user.desc"),
	USER_FOLLOW			("parties.user.follow"),
	USER_HELP			("parties.user.help"),
	USER_HOME			("parties.user.home"),
	USER_IGNORE			("parties.user.ignore"),
	USER_INFO			("parties.user.info"),
	USER_INFO_OTHERS	("parties.user.info.others"),
	USER_INVITE			("parties.user.invite"),
	USER_JOIN			("parties.user.join"),
	USER_KICK			("parties.user.kick"),
	USER_LEAVE			("parties.user.leave"),
	USER_LIST			("parties.user.list"),
	USER_LIST_NAME		("parties.user.list.name"),
	USER_LIST_ONLINE_MEMBERS ("parties.user.list.online.members"),
	USER_LIST_MEMBERS	("parties.user.list.members"),
	USER_LIST_KILLS		("parties.user.list.kills"),
	USER_LIST_EXPERIENCE ("parties.user.list.experience"),
	USER_MOTD			("parties.user.motd"),
	USER_MUTE			("parties.user.mute"),
	USER_NICKNAME		("parties.user.nickname"),
	USER_OPEN			("parties.user.open"),
	USER_PASSWORD		("parties.user.password"),
	USER_PROTECTION		("parties.user.protection"),
	USER_RANK			("parties.user.rank"),
	USER_RENAME			("parties.user.rename"),
	USER_SENDMESSAGE	("parties.user.sendmessage"),
	USER_SETHOME		("parties.user.sethome"),
	USER_TAG			("parties.user.tag"),
	USER_TELEPORT		("parties.user.teleport"),
	// Bukkit
	USER_CLAIM			("parties.user.claim"),
	
	// Admin
	ADMIN_ALERTS				("parties.admin.alerts"),
	ADMIN_CLOSE_OTHERS			("parties.admin.close.others"),
	ADMIN_COOLDOWN_ASK_BYPASS	("parties.admin.cooldown.ask.bypass"),
	ADMIN_COOLDOWN_CHAT_BYPASS	("parties.admin.cooldown.chat.bypass"),
	ADMIN_COOLDOWN_CLOSE_BYPASS ("parties.admin.cooldown.close.bypass"),
	ADMIN_COOLDOWN_HOME_BYPASS	("parties.admin.cooldown.home.bypass"),
	ADMIN_COOLDOWN_INVITE_BYPASS ("parties.admin.cooldown.invite.bypass"),
	ADMIN_COOLDOWN_OPEN_BYPASS ("parties.admin.cooldown.open.bypass"),
	ADMIN_COOLDOWN_RENAME_BYPASS ("parties.admin.cooldown.rename.bypass"),
	ADMIN_COOLDOWN_SETHOME_BYPASS ("parties.admin.cooldown.sethome.bypass"),
	ADMIN_COOLDOWN_TELEPORT_BYPASS ("parties.admin.cooldown.teleport.bypass"),
	ADMIN_CREATE_FIXED			("parties.admin.create.fixed"),
	ADMIN_DEBUG					("parties.admin.debug"),
	ADMIN_DELETE				("parties.admin.delete"),
	ADMIN_DELETE_SILENT			("parties.admin.delete.silent"),
	ADMIN_HOME_OTHERS			("parties.admin.home.others"),
	ADMIN_JOIN_BYPASS			("parties.admin.join.bypass"),
	ADMIN_JOIN_DEFAULT_BYPASS	("parties.admin.join.default.bypass"),
	ADMIN_KICK_OTHERS			("parties.admin.kick.others"),
	ADMIN_NICKNAME_OTHERS		("parties.admin.nickname.others"),
	ADMIN_OPEN_OTHERS			("parties.admin.open.others"),
	ADMIN_PROTECTION_BYPASS		("parties.admin.protection.bypass"),
	ADMIN_RANK_BYPASS			("parties.admin.rank.bypass"),
	ADMIN_RANK_OTHERS			("parties.admin.rank.others"),
	ADMIN_RELOAD				("parties.admin.reload"),
	ADMIN_RENAME_OTHERS			("parties.admin.rename.others"),
	ADMIN_SPY					("parties.admin.spy"),
	ADMIN_TAG_OTHERS			("parties.admin.tag.others"),
	ADMIN_VERSION				("parties.admin.version"),
	// Bukkit
	ADMIN_VAULT_BYPASS			("parties.admin.vault.bypass");
	
	private final String perm;
	PartiesPermission(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
