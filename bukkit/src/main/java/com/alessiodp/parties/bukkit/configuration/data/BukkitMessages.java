package com.alessiodp.parties.bukkit.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.Messages;

public class BukkitMessages extends Messages {
	// Additional commands messages
	public static String ADDCMD_CLAIM_CLAIMED;
	public static String ADDCMD_CLAIM_REMOVED;
	public static String ADDCMD_CLAIM_NOMANAGER;
	public static String ADDCMD_CLAIM_CLAIMNOEXISTS;
	public static String ADDCMD_CLAIM_WRONGCMD;
	
	public static String ADDCMD_EXP_PARTY_GAINED;
	public static String ADDCMD_EXP_NORMAL_GAINED_KILLER;
	public static String ADDCMD_EXP_NORMAL_GAINED_OTHERS;
	public static String ADDCMD_EXP_SKILLAPI_GAINED_KILLER;
	public static String ADDCMD_EXP_SKILLAPI_GAINED_OTHERS;
	
	public static String ADDCMD_HOME_TELEPORTED;
	public static String ADDCMD_HOME_TELEPORTIN;
	public static String ADDCMD_HOME_TELEPORTDENIED;
	public static String ADDCMD_HOME_TELEPORTWAITING;
	public static String ADDCMD_HOME_NOHOME;
	public static String ADDCMD_HOME_NOEXISTS;
	public static String ADDCMD_HOME_WRONGCMD;
	public static String ADDCMD_HOME_WRONGCMD_ADMIN;
	
	public static String ADDCMD_PROTECTION_ON;
	public static String ADDCMD_PROTECTION_OFF;
	public static String ADDCMD_PROTECTION_PROTECTED;
	public static String ADDCMD_PROTECTION_WARNHIT;
	public static String ADDCMD_PROTECTION_WRONGCMD;
	
	public static String ADDCMD_SETHOME_CHANGED;
	public static String ADDCMD_SETHOME_REMOVED;
	public static String ADDCMD_SETHOME_BROADCAST;
	public static String ADDCMD_SETHOME_WRONGCMD;
	
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
	public static String ADDCMD_VAULT_NOMONEY_SETHOME;
	public static String ADDCMD_VAULT_NOMONEY_TELEPORT;
	public static String ADDCMD_VAULT_CONFIRM_WARNONBUY;
	public static String ADDCMD_VAULT_CONFIRM_CONFIRMED;
	public static String ADDCMD_VAULT_CONFIRM_NOCMD;
	
	
	// Other messages
	public static String OTHER_FOLLOW_WORLD;
	
	
	// Help messages
	public static String HELP_ADDCMD_CLAIM;
	public static String HELP_ADDCMD_HOME;
	public static String HELP_ADDCMD_PROTECTION;
	public static String HELP_ADDCMD_SETHOME;
	public static String HELP_ADDCMD_TELEPORT;
	
	
	public BukkitMessages(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bukkit configurations
		// Additional commands messages
		ADDCMD_CLAIM_CLAIMED = "&aGranted permission to the party";
		ADDCMD_CLAIM_REMOVED = "&aRemoved permission to the party";
		ADDCMD_CLAIM_NOMANAGER = "&cYou need to be the manager of the claim";
		ADDCMD_CLAIM_CLAIMNOEXISTS = "&cClaim not found";
		ADDCMD_CLAIM_WRONGCMD = "&cWrong variables: Type &7/party claim <permission>\n&cPermissions: trust, container & access";
		
		ADDCMD_EXP_PARTY_GAINED = "&bGained %exp% party experience for killing the mob";
		ADDCMD_EXP_NORMAL_GAINED_KILLER = "&bGained %exp% experience for killing the mob";
		ADDCMD_EXP_NORMAL_GAINED_OTHERS = "&b%player% has killed a mob, you gained %exp% experience";
		ADDCMD_EXP_SKILLAPI_GAINED_KILLER = "&bGained %exp% experience for killing the mob";
		ADDCMD_EXP_SKILLAPI_GAINED_OTHERS = "&b%player% has killed a mob, you gained %exp% experience";
		
		ADDCMD_HOME_TELEPORTED = "&7Teleported to the party home";
		ADDCMD_HOME_TELEPORTIN = "&7You will be teleported in %time% seconds...";
		ADDCMD_HOME_TELEPORTDENIED = "&7Teleport denied";
		ADDCMD_HOME_TELEPORTWAITING = "&cYou are already waiting for the teleport!";
		ADDCMD_HOME_NOHOME = "&cThere isn't a home yet";
		ADDCMD_HOME_NOEXISTS = "&cThe party %party% doesn't exist";
		ADDCMD_HOME_WRONGCMD = "&cWrong variables: Type &7/party home";
		ADDCMD_HOME_WRONGCMD_ADMIN = "&cWrong variables: Type &7/party home [party]";
		
		ADDCMD_PROTECTION_ON = "&aNow your party is protected by friendly fire";
		ADDCMD_PROTECTION_OFF = "&aYour party is not protected anymore by friendly fire";
		ADDCMD_PROTECTION_PROTECTED = "&cYou can't hit your partymates";
		ADDCMD_PROTECTION_WARNHIT = "&c%player% tried to hit %victim%!";
		ADDCMD_PROTECTION_WRONGCMD = "&cWrong variables: Type &7/party protection [on/off]";
		
		ADDCMD_SETHOME_CHANGED = "";
		ADDCMD_SETHOME_REMOVED = "&bParty home removed";
		ADDCMD_SETHOME_BROADCAST = "&aThe party has a new home!";
		ADDCMD_SETHOME_WRONGCMD = "&cWrong variables: Type &7/party sethome [remove]";
		
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
		ADDCMD_VAULT_NOMONEY_SETHOME = "&cYou don't have enough money to set the home [%price%$]";
		ADDCMD_VAULT_NOMONEY_TELEPORT = "&cYou don't have enough money to perform a teleport [%price%$]";
		ADDCMD_VAULT_CONFIRM_WARNONBUY = "[{\"text\":\"Command %cmd% costs %price%$.\\n\",\"color\":\"green\"},{\"text\":\"Click here to confirm\",\"color\":\"dark_green\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Pay the command\",\"color\":\"gold\"}]}}},{\"text\":\" or type \",\"color\":\"green\",\"bold\":false},{\"text\":\"/party confirm\",\"color\":\"gray\"},{\"text\":\" to use it.\",\"color\":\"green\"}]";
		ADDCMD_VAULT_CONFIRM_CONFIRMED = "&aPerforming the command.";
		ADDCMD_VAULT_CONFIRM_NOCMD = "&cThere is no command to confirm";
		
		
		// Other messages
		OTHER_FOLLOW_WORLD = "&7Following %player% in %world%";
		
		
		// Help messages
		HELP_ADDCMD_CLAIM = "{\"text\":\"\",\"extra\":[{\"text\":\"/party claim <permission>\",\"color\":\"aqua\"},{\"text\":\" - Grant permissions to the claim\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party claim \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_HOME = "{\"text\":\"\",\"extra\":[{\"text\":\"/party home [party]\",\"color\":\"aqua\"},{\"text\":\" - Teleport to the party home\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party home \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_PROTECTION = "{\"text\":\"\",\"extra\":[{\"text\":\"/party protection [on/off]\",\"color\":\"aqua\"},{\"text\":\" - Toggle friendly fire protection\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party protection \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_SETHOME = "{\"text\":\"\",\"extra\":[{\"text\":\"/party sethome [remove]\",\"color\":\"aqua\"},{\"text\":\" - Set the party home\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party sethome \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_ADDCMD_TELEPORT = "{\"text\":\"\",\"extra\":[{\"text\":\"/party teleport\",\"color\":\"aqua\"},{\"text\":\" - Teleport your party to you\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/party teleport\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bukkit configuration
		// Additional commands messages
		ADDCMD_CLAIM_CLAIMED = confAdapter.getString("additional-commands.claim.claimed", ADDCMD_CLAIM_CLAIMED);
		ADDCMD_CLAIM_REMOVED = confAdapter.getString("additional-commands.claim.removed", ADDCMD_CLAIM_REMOVED);
		ADDCMD_CLAIM_NOMANAGER = confAdapter.getString("additional-commands.claim.no-manager", ADDCMD_CLAIM_NOMANAGER);
		ADDCMD_CLAIM_CLAIMNOEXISTS = confAdapter.getString("additional-commands.claim.claim-no-exists", ADDCMD_CLAIM_CLAIMNOEXISTS);
		ADDCMD_CLAIM_WRONGCMD = confAdapter.getString("additional-commands.claim.wrong-command", ADDCMD_CLAIM_WRONGCMD);
		
		ADDCMD_EXP_PARTY_GAINED = confAdapter.getString("additional-commands.exp.party.gained", ADDCMD_EXP_PARTY_GAINED);
		ADDCMD_EXP_NORMAL_GAINED_KILLER = confAdapter.getString("additional-commands.exp.normal.gained-killer", ADDCMD_EXP_NORMAL_GAINED_KILLER);
		ADDCMD_EXP_NORMAL_GAINED_OTHERS = confAdapter.getString("additional-commands.exp.normal.gained-others", ADDCMD_EXP_NORMAL_GAINED_OTHERS);
		ADDCMD_EXP_SKILLAPI_GAINED_KILLER = confAdapter.getString("additional-commands.exp.skillapi.gained-killer", ADDCMD_EXP_SKILLAPI_GAINED_KILLER);
		ADDCMD_EXP_SKILLAPI_GAINED_OTHERS = confAdapter.getString("additional-commands.exp.skillapi.gained-others", ADDCMD_EXP_SKILLAPI_GAINED_OTHERS);
		
		ADDCMD_HOME_TELEPORTED = confAdapter.getString("additional-commands.home.teleported", ADDCMD_HOME_TELEPORTED);
		ADDCMD_HOME_TELEPORTIN = confAdapter.getString("additional-commands.home.teleport-in", ADDCMD_HOME_TELEPORTIN);
		ADDCMD_HOME_TELEPORTDENIED = confAdapter.getString("additional-commands.home.teleport-denied", ADDCMD_HOME_TELEPORTDENIED);
		ADDCMD_HOME_TELEPORTWAITING = confAdapter.getString("additional-commands.home.teleport-waiting", ADDCMD_HOME_TELEPORTWAITING);
		ADDCMD_HOME_NOHOME = confAdapter.getString("additional-commands.home.no-home", ADDCMD_HOME_NOHOME);
		ADDCMD_HOME_NOEXISTS = confAdapter.getString("additional-commands.home.no-exists", ADDCMD_HOME_NOEXISTS);
		ADDCMD_HOME_WRONGCMD = confAdapter.getString("additional-commands.home.wrong-command", ADDCMD_HOME_WRONGCMD);
		ADDCMD_HOME_WRONGCMD_ADMIN = confAdapter.getString("additional-commands.home.wrong-command-admin", ADDCMD_HOME_WRONGCMD_ADMIN);
		
		ADDCMD_PROTECTION_ON = confAdapter.getString("additional-commands.protection.toggle-on", ADDCMD_PROTECTION_ON);
		ADDCMD_PROTECTION_OFF = confAdapter.getString("additional-commands.protection.toggle-off", ADDCMD_PROTECTION_OFF);
		ADDCMD_PROTECTION_PROTECTED = confAdapter.getString("additional-commands.protection.protected", ADDCMD_PROTECTION_PROTECTED);
		ADDCMD_PROTECTION_WARNHIT = confAdapter.getString("additional-commands.protection.warn-on-attack", ADDCMD_PROTECTION_WARNHIT);
		ADDCMD_PROTECTION_WRONGCMD = confAdapter.getString("additional-commands.protection.wrong-command", ADDCMD_PROTECTION_WRONGCMD);
		
		ADDCMD_SETHOME_CHANGED = confAdapter.getString("additional-commands.sethome.changed", ADDCMD_SETHOME_CHANGED);
		ADDCMD_SETHOME_REMOVED = confAdapter.getString("additional-commands.sethome.removed", ADDCMD_SETHOME_REMOVED);
		ADDCMD_SETHOME_BROADCAST = confAdapter.getString("additional-commands.sethome.broadcast", ADDCMD_SETHOME_BROADCAST);
		ADDCMD_SETHOME_WRONGCMD = confAdapter.getString("additional-commands.sethome.wrong-command", ADDCMD_SETHOME_WRONGCMD);
		
		ADDCMD_TELEPORT_TELEPORTING = confAdapter.getString("additional-commands.teleport.teleporting", ADDCMD_TELEPORT_TELEPORTING);
		ADDCMD_TELEPORT_TELEPORTED = confAdapter.getString("additional-commands.teleport.player-teleported", ADDCMD_TELEPORT_TELEPORTED);
		ADDCMD_TELEPORT_COOLDOWN = confAdapter.getString("additional-commands.teleport.cooldown", ADDCMD_TELEPORT_COOLDOWN);
		
		ADDCMD_VAULT_NOMONEY_CLAIM = confAdapter.getString("additional-commands.vault.no-money.claim", ADDCMD_VAULT_NOMONEY_CLAIM);
		ADDCMD_VAULT_NOMONEY_COLOR = confAdapter.getString("additional-commands.vault.no-money.color", ADDCMD_VAULT_NOMONEY_COLOR);
		ADDCMD_VAULT_NOMONEY_CREATE = confAdapter.getString("additional-commands.vault.no-money.create", ADDCMD_VAULT_NOMONEY_CREATE);
		ADDCMD_VAULT_NOMONEY_DESC = confAdapter.getString("additional-commands.vault.no-money.desc", ADDCMD_VAULT_NOMONEY_DESC);
		ADDCMD_VAULT_NOMONEY_HOME = confAdapter.getString("additional-commands.vault.no-money.home", ADDCMD_VAULT_NOMONEY_HOME);
		ADDCMD_VAULT_NOMONEY_JOIN = confAdapter.getString("additional-commands.vault.no-money.join", ADDCMD_VAULT_NOMONEY_JOIN);
		ADDCMD_VAULT_NOMONEY_MOTD = confAdapter.getString("additional-commands.vault.no-money.motd", ADDCMD_VAULT_NOMONEY_MOTD);
		ADDCMD_VAULT_NOMONEY_SETHOME = confAdapter.getString("additional-commands.vault.no-money.sethome", ADDCMD_VAULT_NOMONEY_SETHOME);
		ADDCMD_VAULT_NOMONEY_TELEPORT = confAdapter.getString("additional-commands.vault.no-money.teleport", ADDCMD_VAULT_NOMONEY_TELEPORT);
		ADDCMD_VAULT_CONFIRM_WARNONBUY = confAdapter.getString("additional-commands.vault.confirm.warn-onbuy", ADDCMD_VAULT_CONFIRM_WARNONBUY);
		ADDCMD_VAULT_CONFIRM_CONFIRMED = confAdapter.getString("additional-commands.vault.confirm.confirmed", ADDCMD_VAULT_CONFIRM_CONFIRMED);
		ADDCMD_VAULT_CONFIRM_NOCMD = confAdapter.getString("additional-commands.vault.confirm.no-cmd", ADDCMD_VAULT_CONFIRM_NOCMD);
		
		
		// Other messages
		OTHER_FOLLOW_WORLD = confAdapter.getString("other.follow.following-world", OTHER_FOLLOW_WORLD);
		
		// Help messages
		HELP_ADDCMD_HOME = confAdapter.getString("help.additional-commands.home", HELP_ADDCMD_HOME);
		HELP_ADDCMD_PROTECTION = confAdapter.getString("help.additional-commands.protection", HELP_ADDCMD_PROTECTION);
		HELP_ADDCMD_SETHOME = confAdapter.getString("help.additional-commands.sethome", HELP_ADDCMD_SETHOME);
		HELP_ADDCMD_TELEPORT = confAdapter.getString("help.additional-commands.teleport", HELP_ADDCMD_TELEPORT);
	}
	
	@Override
	public String getFileName() {
		return "messages.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bukkit/messages.yml";
	}
}
