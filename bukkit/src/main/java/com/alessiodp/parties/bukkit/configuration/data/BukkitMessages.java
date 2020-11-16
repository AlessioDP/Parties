package com.alessiodp.parties.bukkit.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.Getter;

public class BukkitMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "bukkit/messages.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUKKIT_MESSAGES;
	
	// Parties messages
	@ConfigOption(path = "parties.syntax.home")
	public static String PARTIES_SYNTAX_HOME;
	@ConfigOption(path = "parties.syntax.permission")
	public static String PARTIES_SYNTAX_PERMISSION;
	
	// Additional commands messages
	@ConfigOption(path = "additional-commands.claim.claimed")
	public static String ADDCMD_CLAIM_CLAIMED;
	@ConfigOption(path = "additional-commands.claim.removed")
	public static String ADDCMD_CLAIM_REMOVED;
	@ConfigOption(path = "additional-commands.claim.no-manager")
	public static String ADDCMD_CLAIM_NOMANAGER;
	@ConfigOption(path = "additional-commands.claim.claim-no-exists")
	public static String ADDCMD_CLAIM_CLAIMNOEXISTS;
	
	@ConfigOption(path = "additional-commands.exp.normal.gained-killer")
	public static String ADDCMD_EXP_NORMAL_GAINED_KILLER;
	@ConfigOption(path = "additional-commands.exp.normal.gained-others")
	public static String ADDCMD_EXP_NORMAL_GAINED_OTHERS;
	@ConfigOption(path = "additional-commands.exp.skillapi.gained-killer")
	public static String ADDCMD_EXP_SKILLAPI_GAINED_KILLER;
	@ConfigOption(path = "additional-commands.exp.skillapi.gained-others")
	public static String ADDCMD_EXP_SKILLAPI_GAINED_OTHERS;
	
	@ConfigOption(path = "additional-commands.home.teleported")
	public static String ADDCMD_HOME_TELEPORTED;
	@ConfigOption(path = "additional-commands.home.teleport-in")
	public static String ADDCMD_HOME_TELEPORTIN;
	@ConfigOption(path = "additional-commands.home.teleport-denied")
	public static String ADDCMD_HOME_TELEPORTDENIED;
	@ConfigOption(path = "additional-commands.home.teleport-waiting")
	public static String ADDCMD_HOME_TELEPORTWAITING;
	@ConfigOption(path = "additional-commands.home.no-home")
	public static String ADDCMD_HOME_NOHOME;
	@ConfigOption(path = "additional-commands.home.cooldown")
	public static String ADDCMD_HOME_COOLDOWN;
	@ConfigOption(path = "additional-commands.home.must-select-home")
	public static String ADDCMD_HOME_MUST_SELECT_HOME;
	@ConfigOption(path = "additional-commands.home.invalid-home")
	public static String ADDCMD_HOME_INVALID_HOME;
	@ConfigOption(path = "additional-commands.home.valid-homes")
	public static String ADDCMD_HOME_VALID_HOMES;
	@ConfigOption(path = "additional-commands.home.valid-home-line")
	public static String ADDCMD_HOME_HOME_VALID_HOME_LINE;
	
	@ConfigOption(path = "additional-commands.protection.toggle-on")
	public static String ADDCMD_PROTECTION_ON;
	@ConfigOption(path = "additional-commands.protection.toggle-off")
	public static String ADDCMD_PROTECTION_OFF;
	@ConfigOption(path = "additional-commands.protection.protected")
	public static String ADDCMD_PROTECTION_PROTECTED;
	@ConfigOption(path = "additional-commands.protection.warn-on-attack")
	public static String ADDCMD_PROTECTION_WARNHIT;
	
	@ConfigOption(path = "additional-commands.sethome.changed")
	public static String ADDCMD_SETHOME_CHANGED;
	@ConfigOption(path = "additional-commands.sethome.removed")
	public static String ADDCMD_SETHOME_REMOVED;
	@ConfigOption(path = "additional-commands.sethome.broadcast")
	public static String ADDCMD_SETHOME_BROADCAST;
	@ConfigOption(path = "additional-commands.sethome.cooldown")
	public static String ADDCMD_SETHOME_COOLDOWN;
	@ConfigOption(path = "additional-commands.sethome.max-homes")
	public static String ADDCMD_SETHOME_MAXHOMES;
	
	@ConfigOption(path = "additional-commands.vault.no-money.ask")
	public static String ADDCMD_VAULT_NOMONEY_ASK;
	@ConfigOption(path = "additional-commands.vault.no-money.claim")
	public static String ADDCMD_VAULT_NOMONEY_CLAIM;
	@ConfigOption(path = "additional-commands.vault.no-money.color")
	public static String ADDCMD_VAULT_NOMONEY_COLOR;
	@ConfigOption(path = "additional-commands.vault.no-money.create")
	public static String ADDCMD_VAULT_NOMONEY_CREATE;
	@ConfigOption(path = "additional-commands.vault.no-money.desc")
	public static String ADDCMD_VAULT_NOMONEY_DESC;
	@ConfigOption(path = "additional-commands.vault.no-money.follow")
	public static String ADDCMD_VAULT_NOMONEY_FOLLOW;
	@ConfigOption(path = "additional-commands.vault.no-money.home")
	public static String ADDCMD_VAULT_NOMONEY_HOME;
	@ConfigOption(path = "additional-commands.vault.no-money.join")
	public static String ADDCMD_VAULT_NOMONEY_JOIN;
	@ConfigOption(path = "additional-commands.vault.no-money.motd")
	public static String ADDCMD_VAULT_NOMONEY_MOTD;
	@ConfigOption(path = "additional-commands.vault.no-money.password")
	public static String ADDCMD_VAULT_NOMONEY_PASSWORD;
	@ConfigOption(path = "additional-commands.vault.no-money.protection")
	public static String ADDCMD_VAULT_NOMONEY_PROTECTION;
	@ConfigOption(path = "additional-commands.vault.no-money.rename")
	public static String ADDCMD_VAULT_NOMONEY_RENAME;
	@ConfigOption(path = "additional-commands.vault.no-money.sethome")
	public static String ADDCMD_VAULT_NOMONEY_SETHOME;
	@ConfigOption(path = "additional-commands.vault.no-money.tag")
	public static String ADDCMD_VAULT_NOMONEY_TAG;
	@ConfigOption(path = "additional-commands.vault.no-money.teleport")
	public static String ADDCMD_VAULT_NOMONEY_TELEPORT;
	@ConfigOption(path = "additional-commands.vault.confirm.warn-onbuy")
	public static String ADDCMD_VAULT_CONFIRM_WARNONBUY;
	@ConfigOption(path = "additional-commands.vault.confirm.confirmed")
	public static String ADDCMD_VAULT_CONFIRM_CONFIRMED;
	@ConfigOption(path = "additional-commands.vault.confirm.no-cmd")
	public static String ADDCMD_VAULT_CONFIRM_NOCMD;
	
	
	// Other messages
	@ConfigOption(path = "other.follow.following-world")
	public static String OTHER_FOLLOW_WORLD;
	
	
	// Help messages
	@ConfigOption(path = "help.additional.commands.claim")
	public static String HELP_ADDITIONAL_COMMANDS_CLAIM;
	@ConfigOption(path = "help.additional.commands.home")
	public static String HELP_ADDITIONAL_COMMANDS_HOME;
	@ConfigOption(path = "help.additional.commands.protection")
	public static String HELP_ADDITIONAL_COMMANDS_PROTECTION;
	@ConfigOption(path = "help.additional.commands.sethome")
	public static String HELP_ADDITIONAL_COMMANDS_SETHOME;
	
	@ConfigOption(path = "help.additional.descriptions.claim")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_CLAIM;
	@ConfigOption(path = "help.additional.descriptions.home")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_HOME;
	@ConfigOption(path = "help.additional.descriptions.protection")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_PROTECTION;
	@ConfigOption(path = "help.additional.descriptions.sethome")
	public static String HELP_ADDITIONAL_DESCRIPTIONS_SETHOME;
	
	
	public BukkitMessages(PartiesPlugin instance) {
		super(instance);
	}
}
