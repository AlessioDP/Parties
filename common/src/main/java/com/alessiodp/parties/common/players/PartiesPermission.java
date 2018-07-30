package com.alessiodp.parties.common.players;

public enum PartiesPermission {
	
	ACCEPT("parties.accept"),
	CHAT("parties.chat"),
	CLAIM("parties.claim"),
	COLOR("parties.color"),
	CREATE("parties.create"),
	DENY("parties.deny"),
	DESC("parties.desc"),
	HELP("parties.help"),
	HOME("parties.home"),
	IGNORE("parties.ignore"),
	INFO("parties.info"),
	INFO_OTHERS("parties.info.others"),
	INVITE("parties.invite"),
	JOIN("parties.join"),
	JOINDEFAULT("parties.joindefault"),
	KICK("parties.kick"),
	LEAVE("parties.leave"),
	LIST("parties.list"),
	MOTD("parties.motd"),
	NOTIFY("parties.notify"),
	PASSWORD("parties.password"),
	RANK("parties.rank"),
	RENAME("parties.rename"),
	SENDMESSAGE("parties.sendmessage"),
	SETHOME("parties.sethome"),
	TELEPORT("parties.teleport"),
	
	HOME_OTHERS("parties.home.others"),
	JOIN_BYPASS("parties.join.bypass"),
	KICK_OTHERS("parties.kick.others"),
	RANK_OTHERS("parties.rank.others"),
	RENAME_OTHERS("parties.rename.others"),
	ADMIN_CREATE_FIXED("parties.admin.create.fixed"),
	ADMIN_DELETE("parties.admin.delete"),
	ADMIN_DELETE_SILENT("parties.admin.delete.silent"),
	ADMIN_JOINDEFAULT("parties.admin.joindefault.bypass"),
	ADMIN_MIGRATE("parties.admin.migrate"),
	ADMIN_RANKBYPASS("parties.admin.rankbypass"),
	ADMIN_RELOAD("parties.admin.reload"),
	ADMIN_SPY("parties.admin.spy"),
	ADMIN_UPDATES("parties.admin.updates"),
	ADMIN_VAULTBYPASS("parties.admin.vaultbypass"),
	
	
	PRIVATE_CLAIM("party.claim"),
	PRIVATE_HOME("party.home"),
	PRIVATE_INVITE("party.invite"),
	PRIVATE_KICK("party.kick"),
	PRIVATE_SENDMESSAGE("party.sendmessage"),
	
	PRIVATE_EDIT_HOME("party.edit.home"),
	PRIVATE_EDIT_DESC("party.edit.desc"),
	PRIVATE_EDIT_MOTD("party.edit.motd"),
	PRIVATE_EDIT_COLOR("party.edit.color"),
	PRIVATE_EDIT_PASSWORD("party.edit.password"),
	
	PRIVATE_ADMIN_RANK("party.admin.rank"),
	PRIVATE_ADMIN_RENAME("party.admin.rename"),
	PRIVATE_ADMIN_TELEPORT("party.admin.teleport"),
	
	PRIVATE_WARNONDAMAGE("party.warnondamage"),
	PRIVATE_AUTOCOMMAND("party.autocommand"),
	PRIVATE_BYPASSCOOLDOWN("party.bypasscooldown");
	
	private final String perm;
	PartiesPermission(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
