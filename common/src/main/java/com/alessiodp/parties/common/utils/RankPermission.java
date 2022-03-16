package com.alessiodp.parties.common.utils;

public enum RankPermission {
	ASK_ACCEPT			("party.ask.accept"),
	ASK_DENY			("party.ask.deny"),
	CLAIM				("party.claim"),
	HOME				("party.home"),
	INVITE				("party.invite"),
	KICK				("party.kick"),
	SENDMESSAGE			("party.sendmessage"),
	SENDMESSAGE_COLOR	("party.sendmessage.color"),
	TELEPORT_ACCEPT		("party.teleport.accept"),
	TELEPORT_DENY		("party.teleport.deny"),
	
	EDIT_CLOSE			("party.edit.close"),
	EDIT_COLOR			("party.edit.color"),
	EDIT_DESC			("party.edit.desc"),
	EDIT_FOLLOW			("party.edit.follow"),
	EDIT_HOME			("party.edit.home"),
	EDIT_MOTD			("party.edit.motd"),
	EDIT_NICKNAME_OWN	("party.edit.nickname.own"),
	EDIT_NICKNAME_OTHERS("party.edit.nickname.others"),
	EDIT_OPEN			("party.edit.open"),
	EDIT_PASSWORD		("party.edit.password"),
	EDIT_PROTECTION		("party.edit.protection"),
	EDIT_TAG			("party.edit.tag"),
	
	ADMIN_RANK			("party.admin.rank"),
	ADMIN_RENAME		("party.admin.rename"),
	ADMIN_TELEPORT		("party.admin.teleport"),
	
	WARNONDAMAGE		("party.warnondamage"),
	AUTOCOMMAND			("party.autocommand");
	
	private final String perm;
	RankPermission(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
