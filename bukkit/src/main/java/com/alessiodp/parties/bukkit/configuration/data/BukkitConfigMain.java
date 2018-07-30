package com.alessiodp.parties.bukkit.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

import java.util.ArrayList;
import java.util.List;

public class BukkitConfigMain extends ConfigMain {
	// Additional settings
	public static boolean		ADDITIONAL_FOLLOW_ENABLE;
	public static int			ADDITIONAL_FOLLOW_TYPE;
	public static int			ADDITIONAL_FOLLOW_RANKNEEDED;
	public static int			ADDITIONAL_FOLLOW_RANKMINIMUM;
	public static int			ADDITIONAL_FOLLOW_TIMEOUT;
	public static List<String>	ADDITIONAL_FOLLOW_LISTWORLDS;
	
	
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
	public static boolean		COMMANDS_TABSUPPORT;
	public static String		COMMANDS_DESC_PARTY;
	public static String		COMMANDS_DESC_P;
	
	public static String		COMMANDS_CMD_CLAIM;
	public static String		COMMANDS_CMD_CONFIRM;
	public static String		COMMANDS_CMD_HOME;
	public static String		COMMANDS_CMD_SETHOME;
	public static String		COMMANDS_CMD_TELEPORT;
	
	
	public BukkitConfigMain(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bukkit configuration
		// Additional features
		ADDITIONAL_FOLLOW_ENABLE = false;
		ADDITIONAL_FOLLOW_TYPE = 1;
		ADDITIONAL_FOLLOW_RANKNEEDED = 0;
		ADDITIONAL_FOLLOW_RANKMINIMUM = 0;
		ADDITIONAL_FOLLOW_TIMEOUT = 100;
		ADDITIONAL_FOLLOW_LISTWORLDS = new ArrayList<>();
		ADDITIONAL_FOLLOW_LISTWORLDS.add("*");
		
		
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
		COMMANDS_TABSUPPORT = true;
		COMMANDS_DESC_PARTY = "Parties help page";
		COMMANDS_DESC_P = "Send a party message";
		
		COMMANDS_CMD_CLAIM = "claim";
		COMMANDS_CMD_CONFIRM = "confirm";
		COMMANDS_CMD_HOME = "home";
		COMMANDS_CMD_SETHOME = "sethome";
		COMMANDS_CMD_TELEPORT = "teleport";
		
		ConfigMain.COMMANDS_ORDER = new ArrayList<>();
		ConfigMain.COMMANDS_ORDER.add("help");
		ConfigMain.COMMANDS_ORDER.add("create");
		ConfigMain.COMMANDS_ORDER.add("accept");
		ConfigMain.COMMANDS_ORDER.add("deny");
		ConfigMain.COMMANDS_ORDER.add("join");
		ConfigMain.COMMANDS_ORDER.add("ignore");
		ConfigMain.COMMANDS_ORDER.add("notify");
		ConfigMain.COMMANDS_ORDER.add("p");
		ConfigMain.COMMANDS_ORDER.add("leave");
		ConfigMain.COMMANDS_ORDER.add("invite");
		ConfigMain.COMMANDS_ORDER.add("info");
		ConfigMain.COMMANDS_ORDER.add("list");
		ConfigMain.COMMANDS_ORDER.add("chat");
		ConfigMain.COMMANDS_ORDER.add("desc");
		ConfigMain.COMMANDS_ORDER.add("motd");
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
		ConfigMain.COMMANDS_ORDER.add("migrate");
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bukkit configuration
		// Additional settings
		ADDITIONAL_FOLLOW_ENABLE = confAdapter.getBoolean("additional.follow-party.enable", ADDITIONAL_FOLLOW_ENABLE);
		ADDITIONAL_FOLLOW_TYPE = confAdapter.getInt("additional.follow-party.type-of-teleport", ADDITIONAL_FOLLOW_TYPE);
		ADDITIONAL_FOLLOW_RANKNEEDED = confAdapter.getInt("additional.follow-party.rank-needed", ADDITIONAL_FOLLOW_RANKNEEDED);
		ADDITIONAL_FOLLOW_RANKMINIMUM = confAdapter.getInt("additional.follow-party.minimum-rank-to-follow", ADDITIONAL_FOLLOW_RANKMINIMUM);
		ADDITIONAL_FOLLOW_TIMEOUT = confAdapter.getInt("additional.follow-party.timeout-portal", ADDITIONAL_FOLLOW_TIMEOUT);
		ADDITIONAL_FOLLOW_LISTWORLDS = confAdapter.getStringList("additional.follow-party.list-worlds", ADDITIONAL_FOLLOW_LISTWORLDS);
		
		
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
		COMMANDS_TABSUPPORT = confAdapter.getBoolean("commands.tab-support", COMMANDS_TABSUPPORT);
		COMMANDS_DESC_PARTY = confAdapter.getString("commands.descriptions.party", COMMANDS_DESC_PARTY);
		COMMANDS_DESC_P = confAdapter.getString("commands.descriptions.p", COMMANDS_DESC_P);
		
		COMMANDS_CMD_CLAIM = confAdapter.getString("commands.main-commands.claim", COMMANDS_CMD_CLAIM);
		COMMANDS_CMD_CONFIRM = confAdapter.getString("commands.main-commands.confirm", COMMANDS_CMD_CONFIRM);
		COMMANDS_CMD_HOME = confAdapter.getString("commands.main-commands.home", COMMANDS_CMD_HOME);
		COMMANDS_CMD_SETHOME = confAdapter.getString("commands.main-commands.sethome", COMMANDS_CMD_SETHOME);
		COMMANDS_CMD_TELEPORT = confAdapter.getString("commands.main-commands.teleport", COMMANDS_CMD_TELEPORT);
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
