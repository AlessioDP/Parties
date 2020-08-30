package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.commands.utils.ADPPermission;

public enum PartiesPermission implements ADPPermission {
	
	// User
	USER_ACCEPT			("parties.user.accept"),
	USER_ASK			("parties.user.ask"),
	USER_CHAT			("parties.user.chat"),
	USER_CHAT_COLOR		("parties.user.chat.color"),
	USER_COLOR			("parties.user.color"),
	USER_CREATE			("parties.user.create"),
	USER_DENY			("parties.user.deny"),
	USER_DESC			("parties.user.desc"),
	USER_FOLLOW			("parties.user.follow"),
	USER_HELP			("parties.user.help"),
	USER_IGNORE			("parties.user.ignore"),
	USER_INFO			("parties.user.info"),
	USER_INFO_OTHERS	("parties.user.info.others"),
	USER_INVITE			("parties.user.invite"),
	USER_JOIN			("parties.user.join"),
	USER_JOINDEFAULT	("parties.user.joindefault"),
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
	USER_PASSWORD		("parties.user.password"),
	USER_RANK			("parties.user.rank"),
	USER_RENAME			("parties.user.rename"),
	USER_SENDMESSAGE	("parties.user.sendmessage"),
	USER_TAG			("parties.user.tag"),
	// Bukkit
	USER_CLAIM			("parties.user.claim"),
	USER_HOME			("parties.user.home"),
	USER_PROTECTION		("parties.user.protection"),
	USER_SETHOME		("parties.user.sethome"),
	USER_TELEPORT		("parties.user.teleport"),
	
	// Admin
	ADMIN_ALERTS				("parties.admin.alerts"),
	ADMIN_COOLDOWN_ASK_BYPASS	("parties.admin.cooldown.ask.bypass"),
	ADMIN_COOLDOWN_CHAT_BYPASS	("parties.admin.cooldown.chat.bypass"),
	ADMIN_COOLDOWN_HOME_BYPASS	("parties.admin.cooldown.home.bypass"),
	ADMIN_COOLDOWN_INVITE_BYPASS ("parties.admin.cooldown.invite.bypass"),
	ADMIN_COOLDOWN_RENAME_BYPASS ("parties.admin.cooldown.rename.bypass"),
	ADMIN_COOLDOWN_SETHOME_BYPASS ("parties.admin.cooldown.sethome.bypass"),
	ADMIN_COOLDOWN_TELEPORT_BYPASS ("parties.admin.cooldown.teleport.bypass"),
	ADMIN_CREATE_FIXED			("parties.admin.create.fixed"),
	ADMIN_DEBUG					("parties.admin.debug"),
	ADMIN_DELETE				("parties.admin.delete"),
	ADMIN_DELETE_SILENT			("parties.admin.delete.silent"),
	ADMIN_JOIN_BYPASS			("parties.admin.join.bypass"),
	ADMIN_JOINDEFAULT_BYPASS	("parties.admin.joindefault.bypass"),
	ADMIN_KICK_OTHERS			("parties.admin.kick.others"),
	ADMIN_RANK_BYPASS			("parties.admin.rank.bypass"),
	ADMIN_RANK_OTHERS			("parties.admin.rank.others"),
	ADMIN_RELOAD				("parties.admin.reload"),
	ADMIN_RENAME_OTHERS			("parties.admin.rename.others"),
	ADMIN_SPY					("parties.admin.spy"),
	ADMIN_TAG_OTHERS			("parties.admin.tag.others"),
	ADMIN_VERSION				("parties.admin.version"),
	// Bukkit
	ADMIN_HOME_OTHERS			("parties.admin.home.others"),
	ADMIN_PROTECTION_BYPASS		("parties.admin.protection.bypass"),
	ADMIN_VAULTBYPASS			("parties.admin.vaultbypass"),
	
	
	PRIVATE_ASK_ACCEPT			("party.ask.accept"),
	PRIVATE_ASK_DENY			("party.ask.deny"),
	PRIVATE_CLAIM				("party.claim"),
	PRIVATE_HOME				("party.home"),
	PRIVATE_INVITE				("party.invite"),
	PRIVATE_KICK				("party.kick"),
	PRIVATE_SENDMESSAGE			("party.sendmessage"),
	PRIVATE_SENDMESSAGE_COLOR	("party.sendmessage.color"),
	
	PRIVATE_EDIT_HOME			("party.edit.home"),
	PRIVATE_EDIT_DESC			("party.edit.desc"),
	PRIVATE_EDIT_FOLLOW			("party.edit.follow"),
	PRIVATE_EDIT_MOTD			("party.edit.motd"),
	PRIVATE_EDIT_COLOR			("party.edit.color"),
	PRIVATE_EDIT_PASSWORD		("party.edit.password"),
	PRIVATE_EDIT_PROTECTION		("party.edit.protection"),
	PRIVATE_EDIT_TAG			("party.edit.tag"),
	
	PRIVATE_ADMIN_RANK			("party.admin.rank"),
	PRIVATE_ADMIN_RENAME		("party.admin.rename"),
	PRIVATE_ADMIN_TELEPORT		("party.admin.teleport"),
	
	PRIVATE_WARNONDAMAGE		("party.warnondamage"),
	PRIVATE_AUTOCOMMAND			("party.autocommand");
	
	private final String perm;
	PartiesPermission(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
