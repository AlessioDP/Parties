package com.alessiodp.parties.bukkit.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.Getter;

import java.util.List;

public class BukkitConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bukkit/config.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUKKIT_CONFIG_MAIN;
	
	// Parties settings
	@ConfigOption(path = "parties.hook-into-skript")
	public static boolean		PARTIES_HOOK_INTO_SKRIPT;
	
	@ConfigOption(path = "parties.bungeecord.enable")
	public static boolean		PARTIES_BUNGEECORD_ENABLE;
	@ConfigOption(path = "parties.bungeecord.server-name")
	public static String		PARTIES_BUNGEECORD_SERVER_NAME;
	@ConfigOption(path = "parties.bungeecord.server-id")
	public static String		PARTIES_BUNGEECORD_SERVER_ID;
	
	
	// Additional settings
	@ConfigOption(path = "additional.exp-system.exp-drop.enable")
	public static boolean		ADDITIONAL_EXP_DROP_ENABLE;
	@ConfigOption(path = "additional.exp-system.exp-drop.sharing.enable")
	public static boolean		ADDITIONAL_EXP_DROP_SHARING_ENABLE;
	@ConfigOption(path = "additional.exp-system.exp-drop.sharing.if-more-than")
	public static int			ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN;
	@ConfigOption(path = "additional.exp-system.exp-drop.sharing.range")
	public static int			ADDITIONAL_EXP_DROP_SHARING_RANGE;
	@ConfigOption(path = "additional.exp-system.exp-drop.sharing.round-exp-drop")
	public static boolean		ADDITIONAL_EXP_DROP_SHARING_ROUND_EXP_DROP;
	@ConfigOption(path = "additional.exp-system.exp-drop.sharing.divide-formula-killer")
	public static String		ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA_KILLER;
	@ConfigOption(path = "additional.exp-system.exp-drop.sharing.divide-formula-others")
	public static String		ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA_OTHERS;
	@ConfigOption(path = "additional.exp-system.exp-drop.exp-to-get.normal")
	public static boolean		ADDITIONAL_EXP_DROP_GET_NORMAL;
	@ConfigOption(path = "additional.exp-system.exp-drop.exp-to-get.skillapi")
	public static boolean		ADDITIONAL_EXP_DROP_GET_SKILLAPI;
	@ConfigOption(path = "additional.exp-system.exp-drop.convert-exp-into.normal")
	public static String		ADDITIONAL_EXP_DROP_CONVERT_NORMAL;
	@ConfigOption(path = "additional.exp-system.exp-drop.convert-exp-into.skillapi")
	public static String		ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI;
	@ConfigOption(path = "additional.exp-system.exp-drop.convert-exp-into.remove-real-exp")
	public static boolean		ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP;
	@ConfigOption(path = "additional.exp-system.exp-drop.addons.levelpoints.enable")
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_LEVELPOINTS_ENABLE;
	@ConfigOption(path = "additional.exp-system.exp-drop.addons.mmocore.enable")
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_MMOCORE_ENABLE;
	@ConfigOption(path = "additional.exp-system.exp-drop.addons.skillapi.enable")
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE;
	@ConfigOption(path = "additional.exp-system.exp-drop.addons.skillapi.exp-source")
	public static String		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE;
	@ConfigOption(path = "additional.exp-system.exp-drop.addons.mythicmobs.enable")
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE;
	@ConfigOption(path = "additional.exp-system.exp-drop.addons.mythicmobs.handle-only-mm-mobs")
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_HANDLEONLYMMMOBS;
	
	@ConfigOption(path = "additional.follow.teleport-to-the-same-location")
	public static boolean		ADDITIONAL_FOLLOW_TELEPORT_TO_SAME_LOCATION;
	@ConfigOption(path = "additional.follow.timeout-portal")
	public static int			ADDITIONAL_FOLLOW_TIMEOUT;
	@ConfigOption(path = "additional.follow.blocked-worlds")
	public static List<String>	ADDITIONAL_FOLLOW_BLOCKEDWORLDS;
	
	// Addons settings
	@ConfigOption(path = "addons.banmanager.enable")
	public static boolean		ADDONS_BANMANAGER_ENABLE;
	@ConfigOption(path = "addons.banmanager.prevent-chat-muted")
	public static boolean		ADDONS_BANMANAGER_PREVENTCHAT;
	@ConfigOption(path = "addons.banmanager.auto-kick-banned")
	public static boolean		ADDONS_BANMANAGER_AUTOKICK;
	
	@ConfigOption(path = "addons.dynmap.enable")
	public static boolean		ADDONS_DYNMAP_ENABLE;
	@ConfigOption(path = "addons.dynmap.hide-by-default")
	public static boolean		ADDONS_DYNMAP_HIDEDEFAULT;
	@ConfigOption(path = "addons.dynmap.settings.minimum-players")
	public static int			ADDONS_DYNMAP_MINPLAYERS;
	@ConfigOption(path = "addons.dynmap.markers.layer")
	public static String		ADDONS_DYNMAP_MARKER_LAYER;
	@ConfigOption(path = "addons.dynmap.markers.label-single")
	public static String		ADDONS_DYNMAP_MARKER_LABEL_SINGLE;
	@ConfigOption(path = "addons.dynmap.markers.label-multiple")
	public static String		ADDONS_DYNMAP_MARKER_LABEL_MULTIPLE;
	@ConfigOption(path = "addons.dynmap.markers.icon")
	public static String		ADDONS_DYNMAP_MARKER_ICON;
	
	@ConfigOption(path = "addons.griefprevention.enable")
	public static boolean		ADDONS_GRIEFPREVENTION_ENABLE;
	@ConfigOption(path = "addons.griefprevention.need-to-be-owner-claim")
	public static boolean		ADDONS_GRIEFPREVENTION_NEEDOWNER;
	@ConfigOption(path = "addons.griefprevention.sub-commands.trust")
	public static String		ADDONS_GRIEFPREVENTION_CMD_TRUST;
	@ConfigOption(path = "addons.griefprevention.sub-commands.container")
	public static String		ADDONS_GRIEFPREVENTION_CMD_CONTAINER;
	@ConfigOption(path = "addons.griefprevention.sub-commands.access")
	public static String		ADDONS_GRIEFPREVENTION_CMD_ACCESS;
	@ConfigOption(path = "addons.griefprevention.sub-commands.remove")
	public static String		ADDONS_GRIEFPREVENTION_CMD_REMOVE;
	
	@ConfigOption(path = "addons.vault.enable")
	public static boolean		ADDONS_VAULT_ENABLE;
	@ConfigOption(path = "addons.vault.confirm-command.enable")
	public static boolean		ADDONS_VAULT_CONFIRM_ENABLE;
	@ConfigOption(path = "addons.vault.confirm-command.timeout")
	public static int			ADDONS_VAULT_CONFIRM_TIMEOUT;
	@ConfigOption(path = "addons.vault.price-commands.ask")
	public static double		ADDONS_VAULT_PRICE_ASK;
	@ConfigOption(path = "addons.vault.price-commands.claim")
	public static double		ADDONS_VAULT_PRICE_CLAIM;
	@ConfigOption(path = "addons.vault.price-commands.color")
	public static double		ADDONS_VAULT_PRICE_COLOR;
	@ConfigOption(path = "addons.vault.price-commands.create")
	public static double		ADDONS_VAULT_PRICE_CREATE;
	@ConfigOption(path = "addons.vault.price-commands.desc")
	public static double		ADDONS_VAULT_PRICE_DESC;
	@ConfigOption(path = "addons.vault.price-commands.follow")
	public static double		ADDONS_VAULT_PRICE_FOLLOW;
	@ConfigOption(path = "addons.vault.price-commands.home")
	public static double		ADDONS_VAULT_PRICE_HOME;
	@ConfigOption(path = "addons.vault.price-commands.join")
	public static double		ADDONS_VAULT_PRICE_JOIN;
	@ConfigOption(path = "addons.vault.price-commands.motd")
	public static double		ADDONS_VAULT_PRICE_MOTD;
	@ConfigOption(path = "addons.vault.price-commands.nickname")
	public static double		ADDONS_VAULT_PRICE_NICKNAME;
	@ConfigOption(path = "addons.vault.price-commands.password")
	public static double		ADDONS_VAULT_PRICE_PASSWORD;
	@ConfigOption(path = "addons.vault.price-commands.protection")
	public static double		ADDONS_VAULT_PRICE_PROTECTION;
	@ConfigOption(path = "addons.vault.price-commands.rename")
	public static double		ADDONS_VAULT_PRICE_RENAME;
	@ConfigOption(path = "addons.vault.price-commands.set-home")
	public static double		ADDONS_VAULT_PRICE_SETHOME;
	@ConfigOption(path = "addons.vault.price-commands.tag")
	public static double		ADDONS_VAULT_PRICE_TAG;
	@ConfigOption(path = "addons.vault.price-commands.teleport")
	public static double		ADDONS_VAULT_PRICE_TELEPORT;
	
	
	// Commands settings
	@ConfigOption(path = "commands.descriptions.party")
	public static String		COMMANDS_DESC_PARTY;
	@ConfigOption(path = "commands.descriptions.p")
	public static String		COMMANDS_DESC_P;
	
	@ConfigOption(path = "commands.main-commands.claim")
	public static String		COMMANDS_CMD_CLAIM;
	@ConfigOption(path = "commands.main-commands.confirm")
	public static String		COMMANDS_CMD_CONFIRM;
	
	
	public BukkitConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
}
