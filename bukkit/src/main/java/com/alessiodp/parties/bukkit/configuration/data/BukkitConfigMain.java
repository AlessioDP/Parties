package com.alessiodp.parties.bukkit.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BukkitConfigMain extends ConfigMain {
	// Parties settings
	public static boolean		PARTIES_BUNGEECORDSYNC_ENABLE;
	public static boolean		PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS;
	public static boolean		PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT;
	
	// Additional settings
	public static int			ADDITIONAL_FOLLOW_TYPE;
	public static int			ADDITIONAL_FOLLOW_RANKNEEDED;
	public static int			ADDITIONAL_FOLLOW_RANKMINIMUM;
	public static int			ADDITIONAL_FOLLOW_TIMEOUT;
	public static List<String>	ADDITIONAL_FOLLOW_LISTWORLDS;
	
	public static boolean		ADDITIONAL_EXP_ENABLE;
	public static boolean		ADDITIONAL_EXP_LEVELS_ENABLE;
	public static String		ADDITIONAL_EXP_LEVELS_MODE;
	public static double		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START;
	public static String		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_MULTIPLIER;
	public static List<Double>	ADDITIONAL_EXP_LEVELS_FIXED;
	public static String		ADDITIONAL_EXP_LEVELS_CUSTOM_FORMULA;
	public static boolean		ADDITIONAL_EXP_DROP_ENABLE;
	public static boolean		ADDITIONAL_EXP_DROP_SHARING_ENABLE;
	public static int			ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN;
	public static int			ADDITIONAL_EXP_DROP_SHARING_RANGE;
	public static String		ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA;
	public static boolean		ADDITIONAL_EXP_DROP_GET_NORMAL;
	public static boolean		ADDITIONAL_EXP_DROP_GET_SKILLAPI;
	public static String		ADDITIONAL_EXP_DROP_CONVERT_NORMAL;
	public static String		ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI;
	public static boolean		ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP;
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE;
	public static String		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE;
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE;
	public static boolean		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_HANDLEONLYMMMOBS;
	
	// Addons settings
	public static boolean		ADDONS_BANMANAGER_ENABLE;
	public static boolean		ADDONS_BANMANAGER_PREVENTCHAT;
	public static boolean		ADDONS_BANMANAGER_AUTOKICK;
	
	public static boolean		ADDONS_DYNMAP_ENABLE;
	public static boolean		ADDONS_DYNMAP_HIDEDEFAULT;
	public static int			ADDONS_DYNMAP_MINPLAYERS;
	public static String		ADDONS_DYNMAP_MARKER_LAYER;
	public static String		ADDONS_DYNMAP_MARKER_LABEL;
	public static String		ADDONS_DYNMAP_MARKER_ICON;
	
	public static boolean		ADDONS_GRIEFPREVENTION_ENABLE;
	public static boolean		ADDONS_GRIEFPREVENTION_NEEDOWNER;
	public static String		ADDONS_GRIEFPREVENTION_CMD_TRUST;
	public static String		ADDONS_GRIEFPREVENTION_CMD_CONTAINER;
	public static String		ADDONS_GRIEFPREVENTION_CMD_ACCESS;
	public static String		ADDONS_GRIEFPREVENTION_CMD_REMOVE;
	
	public static boolean		ADDONS_VAULT_ENABLE;
	public static boolean		ADDONS_VAULT_CONFIRM_ENABLE;
	public static int			ADDONS_VAULT_CONFIRM_TIMEOUT;
	public static double		ADDONS_VAULT_PRICE_CLAIM;
	public static double		ADDONS_VAULT_PRICE_COLOR;
	public static double		ADDONS_VAULT_PRICE_CREATE;
	public static double		ADDONS_VAULT_PRICE_DESC;
	public static double		ADDONS_VAULT_PRICE_HOME;
	public static double		ADDONS_VAULT_PRICE_JOIN;
	public static double		ADDONS_VAULT_PRICE_MOTD;
	public static double		ADDONS_VAULT_PRICE_SETHOME;
	public static double		ADDONS_VAULT_PRICE_TELEPORT;
	
	
	// Commands settings
	public static String		COMMANDS_DESC_PARTY;
	public static String		COMMANDS_DESC_P;
	
	public static String		COMMANDS_CMD_CLAIM;
	public static String		COMMANDS_CMD_CONFIRM;
	public static String		COMMANDS_CMD_HOME;
	public static String		COMMANDS_CMD_PROTECTION;
	public static String		COMMANDS_CMD_SETHOME;
	
	
	public BukkitConfigMain(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bukkit configuration
		// Parties
		PARTIES_BUNGEECORDSYNC_ENABLE = false;
		PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS = true;
		PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT = true;
		
		// Additional features
		ADDITIONAL_FOLLOW_TYPE = 1;
		ADDITIONAL_FOLLOW_RANKNEEDED = 0;
		ADDITIONAL_FOLLOW_RANKMINIMUM = 0;
		ADDITIONAL_FOLLOW_TIMEOUT = 100;
		ADDITIONAL_FOLLOW_LISTWORLDS = new ArrayList<>();
		ADDITIONAL_FOLLOW_LISTWORLDS.add("*");
		
		ADDITIONAL_EXP_LEVELS_ENABLE = true;
		ADDITIONAL_EXP_LEVELS_MODE = "progressive";
		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START = 100;
		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_MULTIPLIER = "*2";
		ADDITIONAL_EXP_LEVELS_FIXED = new LinkedList<>();
		ADDITIONAL_EXP_LEVELS_FIXED.add(100.0);
		ADDITIONAL_EXP_LEVELS_FIXED.add(500.0);
		ADDITIONAL_EXP_LEVELS_FIXED.add(1000.0);
		ADDITIONAL_EXP_LEVELS_CUSTOM_FORMULA = "2 + (Math.log(%total_exp%/100) / Math.log(2))";
		ADDITIONAL_EXP_DROP_ENABLE = true;
		ADDITIONAL_EXP_DROP_SHARING_ENABLE = false;
		ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN = 1;
		ADDITIONAL_EXP_DROP_SHARING_RANGE = 30;
		ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA = "%exp% / %number_players%";
		ADDITIONAL_EXP_DROP_GET_NORMAL = true;
		ADDITIONAL_EXP_DROP_GET_SKILLAPI = false;
		ADDITIONAL_EXP_DROP_CONVERT_NORMAL = "party";
		ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI = "party";
		ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP = false;
		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE = false;
		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE = "MOB";
		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE = false;
		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_HANDLEONLYMMMOBS = true;
		
		
		// Addons settings
		ADDONS_BANMANAGER_ENABLE = false;
		ADDONS_BANMANAGER_PREVENTCHAT = true;
		ADDONS_BANMANAGER_AUTOKICK = true;
		
		ADDONS_DYNMAP_ENABLE = false;
		ADDONS_DYNMAP_HIDEDEFAULT = false;
		ADDONS_DYNMAP_MINPLAYERS = 3;
		ADDONS_DYNMAP_MARKER_LAYER = "Party homes";
		ADDONS_DYNMAP_MARKER_LABEL = "<b>%party%</b>'s home";
		ADDONS_DYNMAP_MARKER_ICON = "house";
		
		ADDONS_GRIEFPREVENTION_ENABLE = false;
		ADDONS_GRIEFPREVENTION_NEEDOWNER = false;
		ADDONS_GRIEFPREVENTION_CMD_TRUST = "trust";
		ADDONS_GRIEFPREVENTION_CMD_CONTAINER = "container";
		ADDONS_GRIEFPREVENTION_CMD_ACCESS = "access";
		ADDONS_GRIEFPREVENTION_CMD_REMOVE = "remove";
		
		ADDONS_VAULT_ENABLE = false;
		ADDONS_VAULT_CONFIRM_ENABLE = true;
		ADDONS_VAULT_CONFIRM_TIMEOUT = 10000;
		ADDONS_VAULT_PRICE_CLAIM = 0.0;
		ADDONS_VAULT_PRICE_COLOR = 0.0;
		ADDONS_VAULT_PRICE_CREATE = 0.0;
		ADDONS_VAULT_PRICE_DESC = 0.0;
		ADDONS_VAULT_PRICE_HOME = 0.0;
		ADDONS_VAULT_PRICE_JOIN = 0.0;
		ADDONS_VAULT_PRICE_MOTD = 0.0;
		ADDONS_VAULT_PRICE_SETHOME = 0.0;
		ADDONS_VAULT_PRICE_TELEPORT = 0.0;
		
		
		// Commands settings
		COMMANDS_DESC_PARTY = "Parties help page";
		COMMANDS_DESC_P = "Send a party message";
		
		COMMANDS_CMD_CLAIM = "claim";
		COMMANDS_CMD_CONFIRM = "confirm";
		COMMANDS_CMD_HOME = "home";
		COMMANDS_CMD_PROTECTION = "protection";
		COMMANDS_CMD_SETHOME = "sethome";
		
		ConfigMain.COMMANDS_ORDER = new ArrayList<>();
		ConfigMain.COMMANDS_ORDER.add("help");
		ConfigMain.COMMANDS_ORDER.add("create");
		ConfigMain.COMMANDS_ORDER.add("accept");
		ConfigMain.COMMANDS_ORDER.add("deny");
		ConfigMain.COMMANDS_ORDER.add("join");
		ConfigMain.COMMANDS_ORDER.add("ignore");
		ConfigMain.COMMANDS_ORDER.add("mute");
		ConfigMain.COMMANDS_ORDER.add("p");
		ConfigMain.COMMANDS_ORDER.add("leave");
		ConfigMain.COMMANDS_ORDER.add("invite");
		ConfigMain.COMMANDS_ORDER.add("info");
		ConfigMain.COMMANDS_ORDER.add("list");
		ConfigMain.COMMANDS_ORDER.add("chat");
		ConfigMain.COMMANDS_ORDER.add("desc");
		ConfigMain.COMMANDS_ORDER.add("motd");
		ConfigMain.COMMANDS_ORDER.add("protection");
		ConfigMain.COMMANDS_ORDER.add("follow");
		ConfigMain.COMMANDS_ORDER.add("home");
		ConfigMain.COMMANDS_ORDER.add("sethome");
		ConfigMain.COMMANDS_ORDER.add("color");
		ConfigMain.COMMANDS_ORDER.add("claim");
		ConfigMain.COMMANDS_ORDER.add("teleport");
		ConfigMain.COMMANDS_ORDER.add("password");
		ConfigMain.COMMANDS_ORDER.add("rank");
		ConfigMain.COMMANDS_ORDER.add("rename");
		ConfigMain.COMMANDS_ORDER.add("kick");
		ConfigMain.COMMANDS_ORDER.add("spy");
		ConfigMain.COMMANDS_ORDER.add("delete");
		ConfigMain.COMMANDS_ORDER.add("reload");
		ConfigMain.COMMANDS_ORDER.add("version");
		ConfigMain.COMMANDS_ORDER.add("migrate");
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bukkit configuration
		// Parties
		PARTIES_BUNGEECORDSYNC_ENABLE = confAdapter.getBoolean("parties.bungeecord-sync.enable", PARTIES_BUNGEECORDSYNC_ENABLE);
		PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS = confAdapter.getBoolean("parties.bungeecord-sync.dispatch.broadcasts", PARTIES_BUNGEECORDSYNC_DISPATCH_BROADCASTS);
		PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT = confAdapter.getBoolean("parties.bungeecord-sync.dispatch.chat", PARTIES_BUNGEECORDSYNC_DISPATCH_CHAT);
		
		// Additional settings
		ADDITIONAL_FOLLOW_TYPE = confAdapter.getInt("additional.follow-party.type-of-teleport", ADDITIONAL_FOLLOW_TYPE);
		ADDITIONAL_FOLLOW_RANKNEEDED = confAdapter.getInt("additional.follow-party.rank-needed", ADDITIONAL_FOLLOW_RANKNEEDED);
		ADDITIONAL_FOLLOW_RANKMINIMUM = confAdapter.getInt("additional.follow-party.minimum-rank-to-follow", ADDITIONAL_FOLLOW_RANKMINIMUM);
		ADDITIONAL_FOLLOW_TIMEOUT = confAdapter.getInt("additional.follow-party.timeout-portal", ADDITIONAL_FOLLOW_TIMEOUT);
		ADDITIONAL_FOLLOW_LISTWORLDS = confAdapter.getStringList("additional.follow-party.list-worlds", ADDITIONAL_FOLLOW_LISTWORLDS);
		
		ADDITIONAL_EXP_ENABLE = confAdapter.getBoolean("additional.exp-system.enable", ADDITIONAL_EXP_ENABLE);
		ADDITIONAL_EXP_LEVELS_ENABLE = confAdapter.getBoolean("additional.exp-system.levels.enable", ADDITIONAL_EXP_LEVELS_ENABLE);
		ADDITIONAL_EXP_LEVELS_MODE = confAdapter.getString("additional.exp-system.levels.mode", ADDITIONAL_EXP_LEVELS_MODE);
		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START = confAdapter.getDouble("additional.exp-system.levels.progressive.level-start", ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START);
		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_MULTIPLIER = confAdapter.getString("additional.exp-system.levels.progressive.level-multiplier", ADDITIONAL_EXP_LEVELS_PROGRESSIVE_MULTIPLIER);
		for (String level : confAdapter.getStringList("additional.exp-system.levels.fixed", new LinkedList<>())) {
			ADDITIONAL_EXP_LEVELS_FIXED.add(Double.parseDouble(level));
		}
		ADDITIONAL_EXP_LEVELS_CUSTOM_FORMULA = confAdapter.getString("additional.exp-system.levels.custom.formula", ADDITIONAL_EXP_LEVELS_CUSTOM_FORMULA);
		ADDITIONAL_EXP_DROP_ENABLE = confAdapter.getBoolean("additional.exp-system.exp-drop.enable", ADDITIONAL_EXP_DROP_ENABLE);
		ADDITIONAL_EXP_DROP_SHARING_ENABLE = confAdapter.getBoolean("additional.exp-system.exp-drop.sharing.enable", ADDITIONAL_EXP_DROP_SHARING_ENABLE);
		ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN = confAdapter.getInt("additional.exp-system.exp-drop.sharing.if-more-than", ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN);
		ADDITIONAL_EXP_DROP_SHARING_RANGE = confAdapter.getInt("additional.exp-system.exp-drop.sharing.range", ADDITIONAL_EXP_DROP_SHARING_RANGE);
		ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA = confAdapter.getString("additional.exp-system.exp-drop.sharing.divide-formula", ADDITIONAL_EXP_DROP_SHARING_DIVIDEFORMULA);
		ADDITIONAL_EXP_DROP_GET_NORMAL = confAdapter.getBoolean("additional.exp-system.exp-drop.exp-to-get.normal", ADDITIONAL_EXP_DROP_GET_NORMAL);
		ADDITIONAL_EXP_DROP_GET_SKILLAPI = confAdapter.getBoolean("additional.exp-system.exp-drop.exp-to-get.skillapi", ADDITIONAL_EXP_DROP_GET_SKILLAPI);
		ADDITIONAL_EXP_DROP_CONVERT_NORMAL = confAdapter.getString("additional.exp-system.exp-drop.convert-exp-into.normal", ADDITIONAL_EXP_DROP_CONVERT_NORMAL);
		ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI = confAdapter.getString("additional.exp-system.exp-drop.convert-exp-into.skillapi", ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI);
		ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP = confAdapter.getBoolean("additional.exp-system.exp-drop.convert-exp-into.remove-real-exp", ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP);
		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE = confAdapter.getBoolean("additional.exp-system.exp-drop.addons.skillapi.enable", ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE);
		ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE = confAdapter.getString("additional.exp-system.exp-drop.addons.skillapi.exp-source", ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE);
		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE = confAdapter.getBoolean("additional.exp-system.exp-drop.addons.mythicmobs.enable", ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE);
		ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_HANDLEONLYMMMOBS = confAdapter.getBoolean("additional.exp-system.exp-drop.addons.mythicmobs.handle-only-mm-mobs", ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_HANDLEONLYMMMOBS);
		
		
		// Addons settings
		ADDONS_BANMANAGER_ENABLE = confAdapter.getBoolean("addons.banmanager.enable", ADDONS_BANMANAGER_ENABLE);
		ADDONS_BANMANAGER_PREVENTCHAT = confAdapter.getBoolean("addons.banmanager.prevent-chat-muted", ADDONS_BANMANAGER_PREVENTCHAT);
		ADDONS_BANMANAGER_AUTOKICK = confAdapter.getBoolean("addons.banmanager.auto-kick-banned", ADDONS_BANMANAGER_AUTOKICK);
		
		ADDONS_DYNMAP_ENABLE = confAdapter.getBoolean("addons.dynmap.enable", ADDONS_DYNMAP_ENABLE);
		ADDONS_DYNMAP_HIDEDEFAULT = confAdapter.getBoolean("addons.dynmap.hide-by-default", ADDONS_DYNMAP_HIDEDEFAULT);
		ADDONS_DYNMAP_MINPLAYERS = confAdapter.getInt("addons.dynmap.settings.minimum-players", ADDONS_DYNMAP_MINPLAYERS);
		ADDONS_DYNMAP_MARKER_LAYER = confAdapter.getString("addons.dynmap.markers.layer", ADDONS_DYNMAP_MARKER_LAYER);
		ADDONS_DYNMAP_MARKER_LABEL = confAdapter.getString("addons.dynmap.markers.label", ADDONS_DYNMAP_MARKER_LABEL);
		ADDONS_DYNMAP_MARKER_ICON = confAdapter.getString("addons.dynmap.markers.icon", ADDONS_DYNMAP_MARKER_ICON);
		
		ADDONS_GRIEFPREVENTION_ENABLE = confAdapter.getBoolean("addons.griefprevention.enable", ADDONS_GRIEFPREVENTION_ENABLE);
		ADDONS_GRIEFPREVENTION_NEEDOWNER = confAdapter.getBoolean("addons.griefprevention.need-to-be-owner-claim", ADDONS_GRIEFPREVENTION_NEEDOWNER);
		ADDONS_GRIEFPREVENTION_CMD_TRUST = confAdapter.getString("addons.griefprevention.sub-commands.trust", ADDONS_GRIEFPREVENTION_CMD_TRUST);
		ADDONS_GRIEFPREVENTION_CMD_CONTAINER = confAdapter.getString("addons.griefprevention.sub-commands.container", ADDONS_GRIEFPREVENTION_CMD_CONTAINER);
		ADDONS_GRIEFPREVENTION_CMD_ACCESS = confAdapter.getString("addons.griefprevention.sub-commands.access", ADDONS_GRIEFPREVENTION_CMD_ACCESS);
		ADDONS_GRIEFPREVENTION_CMD_REMOVE = confAdapter.getString("addons.griefprevention.sub-commands.remove", ADDONS_GRIEFPREVENTION_CMD_REMOVE);
		
		ADDONS_VAULT_ENABLE = confAdapter.getBoolean("addons.vault.enable", ADDONS_VAULT_ENABLE);
		ADDONS_VAULT_CONFIRM_ENABLE = confAdapter.getBoolean("addons.vault.confirm-command.enable", ADDONS_VAULT_CONFIRM_ENABLE);
		ADDONS_VAULT_CONFIRM_TIMEOUT = confAdapter.getInt("addons.vault.confirm-command.timeout", ADDONS_VAULT_CONFIRM_TIMEOUT);
		ADDONS_VAULT_PRICE_CLAIM = confAdapter.getDouble("addons.vault.price-commands.claim", ADDONS_VAULT_PRICE_CLAIM);
		ADDONS_VAULT_PRICE_COLOR = confAdapter.getDouble("addons.vault.price-commands.color", ADDONS_VAULT_PRICE_COLOR);
		ADDONS_VAULT_PRICE_CREATE = confAdapter.getDouble("addons.vault.price-commands.create", ADDONS_VAULT_PRICE_CREATE);
		ADDONS_VAULT_PRICE_DESC = confAdapter.getDouble("addons.vault.price-commands.desc", ADDONS_VAULT_PRICE_DESC);
		ADDONS_VAULT_PRICE_HOME = confAdapter.getDouble("addons.vault.price-commands.home", ADDONS_VAULT_PRICE_HOME);
		ADDONS_VAULT_PRICE_JOIN = confAdapter.getDouble("addons.vault.price-commands.join", ADDONS_VAULT_PRICE_JOIN);
		ADDONS_VAULT_PRICE_MOTD = confAdapter.getDouble("addons.vault.price-commands.motd", ADDONS_VAULT_PRICE_MOTD);
		ADDONS_VAULT_PRICE_SETHOME = confAdapter.getDouble("addons.vault.price-commands.set-home", ADDONS_VAULT_PRICE_SETHOME);
		ADDONS_VAULT_PRICE_TELEPORT = confAdapter.getDouble("addons.vault.price-commands.teleport", ADDONS_VAULT_PRICE_TELEPORT);
		
		
		// Commands settings
		COMMANDS_DESC_PARTY = confAdapter.getString("commands.descriptions.party", COMMANDS_DESC_PARTY);
		COMMANDS_DESC_P = confAdapter.getString("commands.descriptions.p", COMMANDS_DESC_P);
		
		COMMANDS_CMD_CLAIM = confAdapter.getString("commands.main-commands.claim", COMMANDS_CMD_CLAIM);
		COMMANDS_CMD_CONFIRM = confAdapter.getString("commands.main-commands.confirm", COMMANDS_CMD_CONFIRM);
		COMMANDS_CMD_HOME = confAdapter.getString("commands.main-commands.home", COMMANDS_CMD_HOME);
		COMMANDS_CMD_PROTECTION = confAdapter.getString("commands.main-commands.protection", COMMANDS_CMD_PROTECTION);
		COMMANDS_CMD_SETHOME = confAdapter.getString("commands.main-commands.sethome", COMMANDS_CMD_SETHOME);
	}
	
	@Override
	public String getFileName() {
		return "config.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bukkit/config.yml";
	}
}
