package com.alessiodp.parties.utils.enums;

public enum PartiesPermissions {
	SENDMESSAGE("parties.sendmessage"),
	HELP("parties.help"),
	
	CREATE("parties.create"),
	LEAVE("parties.leave"),
	IGNORE("parties.ignore"),
	ACCEPT("parties.accept"),
	DENY("parties.deny"),
	JOIN("parties.join"),
	JOIN_OTHERS("parties.join.others"),
	INFO("parties.info"),
	INFO_OTHERS("parties.info.others"),
	MEMBERS("parties.members"),
	MEMBERS_OTHERS("parties.members.others"),
	
	INVITE("parties.invite"),
	PASSWORD("parties.password"),
	CHAT("parties.chat"),
	LIST("parties.list"),
	HOME("parties.home"),
	HOME_OTHERS("parties.home.others"),
	SETHOME("parties.sethome"),
	TELEPORT("parties.teleport"),
	DESC("parties.desc"),
	MOTD("parties.motd"),
	COLOR("parties.color"),
	PREFIX("parties.prefix"),
	SUFFIX("parties.suffix"),
	RENAME("parties.rename"),
	RENAME_OTHERS("parties.rename.others"),
	KICK("parties.kick"),
	KICK_OTHERS("parties.kick.others"),
	RANK("parties.rank"),
	RANK_OTHERS("parties.rank.others"),
	CLAIM("parties.claim"),
	JOINDEFAULT("parties.joindefault"),
	
	ADMIN_VAULTBYPASS("parties.admin.vaultbypass"),
	ADMIN_RANKBYPASS("parties.admin.rankbypass"),
	ADMIN_CREATE_FIXED("parties.admin.fixed"),
	ADMIN_DELETE("parties.admin.delete"),
	ADMIN_DELETE_SILENT("parties.admin.delete.silent"),
	ADMIN_SPY("parties.admin.spy"),
	ADMIN_JOINDEFAULT("parties.admin.joindefault.bypass"),
	ADMIN_RELOAD("parties.admin.reload"),
	ADMIN_UPDATES("parties.admin.updates"),
	ADMIN_MIGRATE("parties.admin.migrate"),
	
	PRIVATE_SENDMESSAGE("party.sendmessage"),
	PRIVATE_HOME("party.home"),
	PRIVATE_DESC("party.desc"),
	PRIVATE_MOTD("party.motd"),
	PRIVATE_INVITE("party.invite"),
	PRIVATE_KICK("party.kick"),
	PRIVATE_CLAIM("party.claim"),
	
	PRIVATE_EDIT_HOME("party.edit.home"),
	PRIVATE_EDIT_DESC("party.edit.desc"),
	PRIVATE_EDIT_MOTD("party.edit.motd"),
	PRIVATE_EDIT_COLOR("party.edit.color"),
	PRIVATE_EDIT_PREFIX("party.edit.prefix"),
	PRIVATE_EDIT_SUFFIX("party.edit.suffix"),
	PRIVATE_EDIT_PASSWORD("party.edit.password"),
	
	PRIVATE_ADMIN_RENAME("party.admin.rename"),
	PRIVATE_ADMIN_RANK("party.admin.rank"),
	PRIVATE_ADMIN_TELEPORT("party.admin.teleport"),
	
	PRIVATE_WARNONDAMAGE("party.warnondamage"),
	PRIVATE_AUTOCOMMAND("party.autocommand"),
	PRIVATE_BYPASSCOOLDOWN("party.bypasscooldown");
	
	private final String perm;
	PartiesPermissions(String t) {
		perm = t;
	}
	
	@Override
	public String toString() {
		return perm;
	}
}
