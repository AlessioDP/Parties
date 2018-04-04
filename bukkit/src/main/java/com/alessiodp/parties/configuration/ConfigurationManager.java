package com.alessiodp.parties.configuration;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.parties.objects.ColorImpl;
import com.alessiodp.parties.players.objects.RankImpl;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.interfaces.Rank;

public class ConfigurationManager {
	private Parties plugin;
	
	public ConfigurationManager(Parties instance) {
		plugin = instance;
	}
	
	public void reload() {
		new ConfigMain();
		new ConfigParties();
		new Messages();
		
		reloadConfigMain();
		plugin.getDatabaseManager().setDatabaseType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_DATABASE));
		plugin.getDatabaseManager().setLogType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_LOG));
		PartiesPlaceholder.setupFormats();
		
		reloadConfigParties();
		reloadMessages();
	}
	
	private void reloadConfigMain() {
		File file = new File(plugin.getDataFolder(), "config.yml");
		if (!file.exists()) {
			plugin.saveResource("config.yml", true);
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if (cfg.getInt("dont-edit-this.config-version") < Constants.VERSION_CONFIG_MAIN) {
			// Normal log, LogHandler will give error
			plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "File config.yml is outdated");
		}
		
		// Parties settings
		ConfigMain.PARTIES_UPDATES_CHECK = cfg.getBoolean("parties.updates.check", ConfigMain.PARTIES_UPDATES_CHECK);
		ConfigMain.PARTIES_UPDATES_WARN = cfg.getBoolean("parties.updates.warn", ConfigMain.PARTIES_UPDATES_WARN);
		ConfigMain.PARTIES_SEEINVISIBLE = cfg.getBoolean("parties.see-invisible-allies", ConfigMain.PARTIES_SEEINVISIBLE);
		ConfigMain.PARTIES_JLMESSAGES = cfg.getBoolean("parties.join-leave-messages", ConfigMain.PARTIES_JLMESSAGES);
		ConfigMain.PARTIES_BUNGEECORD = cfg.getBoolean("parties.bungeecord", ConfigMain.PARTIES_BUNGEECORD);
		
		
		// Storage settings
		ConfigMain.STORAGE_TYPE_LOG = cfg.getString("storage.log-storage-type", ConfigMain.STORAGE_TYPE_LOG);
		ConfigMain.STORAGE_TYPE_DATABASE = cfg.getString("storage.database-storage-type", ConfigMain.STORAGE_TYPE_DATABASE);
		ConfigMain.STORAGE_LOG_FORMAT = cfg.getString("storage.log-settings.format", ConfigMain.STORAGE_LOG_FORMAT);
		ConfigMain.STORAGE_LOG_CHAT = cfg.getBoolean("storage.log-settings.chat", ConfigMain.STORAGE_LOG_CHAT);
		ConfigMain.STORAGE_LOG_PRINTCONSOLE = cfg.getBoolean("storage.log-settings.print-console", ConfigMain.STORAGE_LOG_PRINTCONSOLE);
		ConfigMain.STORAGE_LOG_LEVEL = cfg.getInt("storage.log-settings.log-level", ConfigMain.STORAGE_LOG_LEVEL);
		ConfigMain.STORAGE_MIGRATE_INIT_YAML = cfg.getBoolean("storage.migrate-settings.initialize-storage.yaml", ConfigMain.STORAGE_MIGRATE_INIT_YAML);
		ConfigMain.STORAGE_MIGRATE_INIT_MYSQL = cfg.getBoolean("storage.migrate-settings.initialize-storage.mysql", ConfigMain.STORAGE_MIGRATE_INIT_MYSQL);
		ConfigMain.STORAGE_MIGRATE_INIT_SQLITE = cfg.getBoolean("storage.migrate-settings.initialize-storage.sqlite", ConfigMain.STORAGE_MIGRATE_INIT_SQLITE);
		ConfigMain.STORAGE_MIGRATE_ONLYCONSOLE = cfg.getBoolean("storage.migrate-settings.migrate-only-console", ConfigMain.STORAGE_MIGRATE_ONLYCONSOLE);
		ConfigMain.STORAGE_MIGRATE_SUFFIX = cfg.getString("storage.migrate-settings.migration-suffix", ConfigMain.STORAGE_MIGRATE_SUFFIX);
		
		ConfigMain.STORAGE_SETTINGS_FILE_TXT_LOGNAME = cfg.getString("storage.storage-settings.file-based.txt.log-name", ConfigMain.STORAGE_SETTINGS_FILE_TXT_LOGNAME);
		ConfigMain.STORAGE_SETTINGS_FILE_YAML_DBNAME = cfg.getString("storage.storage-settings.file-based.yaml.database-name", ConfigMain.STORAGE_SETTINGS_FILE_YAML_DBNAME);
		ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE = cfg.getInt("storage.storage-settings.sql-based.sql-based.general-settings.varchar-size", ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE);
		ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES = cfg.getString("storage.storage-settings.sql-based.general-settings.tables.parties", ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES);
		ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS = cfg.getString("storage.storage-settings.sql-based.general-settings.tables.players", ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS);
		ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG = cfg.getString("storage.storage-settings.sql-based.general-settings.tables.log", ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG);
		ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_VERSIONS = cfg.getString("storage.storage-settings.sql-based.general-settings.tables.versions", ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_VERSIONS);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_ADDRESS = cfg.getString("storage.storage-settings.sql-based.mysql.address", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_ADDRESS);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_DATABASE = cfg.getString("storage.storage-settings.sql-based.mysql.database", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_DATABASE);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_USERNAME = cfg.getString("storage.storage-settings.sql-based.mysql.username", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_USERNAME);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_PASSWORD = cfg.getString("storage.storage-settings.sql-based.mysql.password", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_PASSWORD);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_POOLSIZE = cfg.getInt("storage.storage-settings.sql-based.mysql.pool-size", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_POOLSIZE);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CONNLIFETIME = cfg.getInt("storage.storage-settings.sql-based.mysql.connection-lifetime", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CONNLIFETIME);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CONNTIMEOUT = cfg.getInt("storage.storage-settings.sql-based.mysql.connection-timeout", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CONNTIMEOUT);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_USESSL = cfg.getBoolean("storage.storage-settings.sql-based.mysql.use-ssl", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_USESSL);
		ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CHARSET = cfg.getString("storage.storage-settings.sql-based.mysql.charset", ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CHARSET);
		ConfigMain.STORAGE_SETTINGS_SQL_SQLITE_DBNAME = cfg.getString("storage.storage-settings.sql-based.sqlite.database-name", ConfigMain.STORAGE_SETTINGS_SQL_SQLITE_DBNAME);
		ConfigMain.STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT = cfg.getBoolean("storage.storage-settings.none.disband-on-leader-left", ConfigMain.STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT);
		ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY = cfg.getInt("storage.storage-settings.none.delay-delete-party", ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY);
		
		
		// Additional settings
		ConfigMain.ADDITIONAL_AUTOCMD_ENABLE = cfg.getBoolean("additional.auto-command.enable", ConfigMain.ADDITIONAL_AUTOCMD_ENABLE);
		ConfigMain.ADDITIONAL_AUTOCMD_BLACKLIST = cfg.getStringList("additional.auto-command.blacklist");
		ConfigMain.ADDITIONAL_AUTOCMD_WHITELIST = cfg.getStringList("additional.auto-command.whitelist");
		
		ConfigMain.ADDITIONAL_CENSOR_ENABLE = cfg.getBoolean("additional.censor-system.enable", ConfigMain.ADDITIONAL_CENSOR_ENABLE);
		ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE = cfg.getBoolean("additional.censor-system.case-sensitive", ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE);
		ConfigMain.ADDITIONAL_CENSOR_CONTAINS = cfg.getStringList("additional.censor-system.contains");
		ConfigMain.ADDITIONAL_CENSOR_STARTSWITH = cfg.getStringList("additional.censor-system.starts-with");
		ConfigMain.ADDITIONAL_CENSOR_ENDSWITH = cfg.getStringList("additional.censor-system.ends-with");
		
		ConfigMain.ADDITIONAL_EXP_ENABLE = cfg.getBoolean("additional.exp-system.enable", ConfigMain.ADDITIONAL_EXP_ENABLE);
		ConfigMain.ADDITIONAL_EXP_DIVIDE = cfg.getBoolean("additional.exp-system.divide-between-players", ConfigMain.ADDITIONAL_EXP_DIVIDE);
		ConfigMain.ADDITIONAL_EXP_SHAREIFMORE = cfg.getInt("additional.exp-system.share-if-more-than", ConfigMain.ADDITIONAL_EXP_SHAREIFMORE);
		ConfigMain.ADDITIONAL_EXP_FORMULA = cfg.getString("additional.exp-system.formula", ConfigMain.ADDITIONAL_EXP_FORMULA);
		ConfigMain.ADDITIONAL_EXP_RANGE = cfg.getInt("additional.exp-system.range", ConfigMain.ADDITIONAL_EXP_RANGE);
		ConfigMain.ADDITIONAL_EXP_HANDLE_VANILLA = cfg.getBoolean("additional.exp-system.exp-to-handle.vanilla", ConfigMain.ADDITIONAL_EXP_HANDLE_VANILLA);
		ConfigMain.ADDITIONAL_EXP_HANDLE_SKILLAPI = cfg.getBoolean("additional.exp-system.exp-to-handle.skillapi", ConfigMain.ADDITIONAL_EXP_HANDLE_SKILLAPI);
		ConfigMain.ADDITIONAL_EXP_GIVEAS_VANILLA = ConfigMain.ExpType.getValue(cfg.getString("additional.exp-system.give-exp-as.vanilla", "vanilla"));
		ConfigMain.ADDITIONAL_EXP_GIVEAS_SKILLAPI = ConfigMain.ExpType.getValue(cfg.getString("additional.exp-system.give-exp-as.skillapi", "skillapi"));
		/*
		 * ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE = cfg.getBoolean("additional.exp-system.addons.mythicmobs.enable", ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE);
		 * ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_VANILLA = cfg.getBoolean("additional.exp-system.addons.mythicmobs.exp-to-get.vanilla", ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_VANILLA);
		 * ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_SKILLAPI = cfg.getBoolean("additional.exp-system.addons.mythicmobs.exp-to-get.skillapi", ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_SKILLAPI);
		 */
		ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_ENABLE = cfg.getBoolean("additional.exp-system.addons.skillapi.enable", ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_ENABLE);
		ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_SOURCE = cfg.getString("additional.exp-system.addons.skillapi.exp-source", ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_SOURCE);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_MAXLENGTH = cfg.getInt("additional.tag-system.custom-tag.maximum-length", ConfigMain.ADDITIONAL_TAG_CUSTOM_MAXLENGTH);
		
		ConfigMain.ADDITIONAL_FOLLOW_ENABLE = cfg.getBoolean("additional.follow-party.enable", ConfigMain.ADDITIONAL_FOLLOW_ENABLE);
		ConfigMain.ADDITIONAL_FOLLOW_TYPE = cfg.getInt("additional.follow-party.type-of-teleport", ConfigMain.ADDITIONAL_FOLLOW_TYPE);
		ConfigMain.ADDITIONAL_FOLLOW_RANKNEEDED = cfg.getInt("additional.follow-party.rank-needed", ConfigMain.ADDITIONAL_FOLLOW_RANKNEEDED);
		ConfigMain.ADDITIONAL_FOLLOW_RANKMINIMUM = cfg.getInt("additional.follow-party.minimum-rank-to-follow", ConfigMain.ADDITIONAL_FOLLOW_RANKMINIMUM);
		ConfigMain.ADDITIONAL_FOLLOW_TIMEOUT = cfg.getInt("additional.follow-party.timeout-portal", ConfigMain.ADDITIONAL_FOLLOW_TIMEOUT);
		ConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS = cfg.getStringList("additional.follow-party.list-worlds");
		
		ConfigMain.ADDITIONAL_NOTIFY_ENABLE = cfg.getBoolean("additional.notify.enable", ConfigMain.ADDITIONAL_NOTIFY_ENABLE);
		ConfigMain.ADDITIONAL_NOTIFY_BLOCK_INVITE = cfg.getBoolean("additional.notify.block.INVITE", ConfigMain.ADDITIONAL_NOTIFY_BLOCK_INVITE);
		
		ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_NAME = cfg.getString("additional.placeholders.color-name", ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_NAME);
		ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_CODE = cfg.getString("additional.placeholders.color-code", ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_CODE);
		ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_COMMAND = cfg.getString("additional.placeholders.color-command", ConfigMain.ADDITIONAL_PLACEHOLDER_COLOR_COMMAND);
		ConfigMain.ADDITIONAL_PLACEHOLDER_DESC = cfg.getString("additional.placeholders.desc", ConfigMain.ADDITIONAL_PLACEHOLDER_DESC);
		ConfigMain.ADDITIONAL_PLACEHOLDER_KILLS = cfg.getString("additional.placeholders.kills", ConfigMain.ADDITIONAL_PLACEHOLDER_KILLS);
		ConfigMain.ADDITIONAL_PLACEHOLDER_MOTD = cfg.getString("additional.placeholders.motd", ConfigMain.ADDITIONAL_PLACEHOLDER_MOTD);
		ConfigMain.ADDITIONAL_PLACEHOLDER_PARTY = cfg.getString("additional.placeholders.party", ConfigMain.ADDITIONAL_PLACEHOLDER_PARTY);
		ConfigMain.ADDITIONAL_PLACEHOLDER_PREFIX = cfg.getString("additional.placeholders.prefix", ConfigMain.ADDITIONAL_PLACEHOLDER_PREFIX);
		ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_NAME = cfg.getString("additional.placeholders.rank-name", ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_NAME);
		ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_CHAT = cfg.getString("additional.placeholders. rank-chat", ConfigMain.ADDITIONAL_PLACEHOLDER_RANK_CHAT);
		ConfigMain.ADDITIONAL_PLACEHOLDER_SUFFIX = cfg.getString("additional.placeholders.suffix", ConfigMain.ADDITIONAL_PLACEHOLDER_SUFFIX);

		ConfigMain.ADDITIONAL_TAG_ENABLE = cfg.getBoolean("additional.tag-system.enable", ConfigMain.ADDITIONAL_TAG_ENABLE);
		ConfigMain.ADDITIONAL_TAG_ENGINE = ConfigMain.TagEngine.getValue(cfg.getString("additional.tag-system.tag-engine", "scoreboard"));
		ConfigMain.ADDITIONAL_TAG_TYPE = ConfigMain.TagType.getValue(cfg.getString("additional.tag-system.tag-type", "base"));
		ConfigMain.ADDITIONAL_TAG_BASE_FORMATPREFIX = cfg.getString("additional.tag-system.base-tag.format-prefix", ConfigMain.ADDITIONAL_TAG_BASE_FORMATPREFIX);
		ConfigMain.ADDITIONAL_TAG_BASE_FORMATSUFFIX = cfg.getString("additional.tag-system.base-tag.format-suffix", ConfigMain.ADDITIONAL_TAG_BASE_FORMATSUFFIX);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_PREFIX = cfg.getBoolean("additional.tag-system.custom-tag.prefix", ConfigMain.ADDITIONAL_TAG_CUSTOM_PREFIX);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATPREFIX = cfg.getString("additional.tag-system.custom-tag.format-prefix", ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATPREFIX);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_SUFFIX = cfg.getBoolean("additional.tag-system.custom-tag.suffix", ConfigMain.ADDITIONAL_TAG_CUSTOM_SUFFIX);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATSUFFIX = cfg.getString("additional.tag-system.custom-tag.format-suffix", ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATSUFFIX);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_ALLOWEDCHARS = cfg.getString("additional.tag-system.custom-tag.allowed-characters", ConfigMain.ADDITIONAL_TAG_CUSTOM_ALLOWEDCHARS);
		ConfigMain.ADDITIONAL_TAG_CUSTOM_MINLENGTH = cfg.getInt("additional.tag-system.custom-tag.minimum-length", ConfigMain.ADDITIONAL_TAG_CUSTOM_MINLENGTH);
		
		
		// Addons settings
		ConfigMain.ADDONS_BANMANAGER_ENABLE = cfg.getBoolean("addons.banmanager.enable", ConfigMain.ADDONS_BANMANAGER_ENABLE);
		ConfigMain.ADDONS_BANMANAGER_PREVENTCHAT = cfg.getBoolean("addons.banmanager.prevent-chat-muted", ConfigMain.ADDONS_BANMANAGER_PREVENTCHAT);
		ConfigMain.ADDONS_BANMANAGER_AUTOKICK = cfg.getBoolean("addons.banmanager.auto-kick-banned", ConfigMain.ADDONS_BANMANAGER_AUTOKICK);
		
		ConfigMain.ADDONS_DYNMAP_ENABLE = cfg.getBoolean("addons.dynmap.enable", ConfigMain.ADDONS_DYNMAP_ENABLE);
		ConfigMain.ADDONS_DYNMAP_HIDEDEFAULT = cfg.getBoolean("addons.dynmap.hide-by-default", ConfigMain.ADDONS_DYNMAP_HIDEDEFAULT);
		ConfigMain.ADDONS_DYNMAP_MINPLAYERS = cfg.getInt("addons.dynmap.settings.minimum-players", ConfigMain.ADDONS_DYNMAP_MINPLAYERS);
		ConfigMain.ADDONS_DYNMAP_MARKER_LAYER = cfg.getString("addons.dynmap.markers.layer", ConfigMain.ADDONS_DYNMAP_MARKER_LAYER);
		ConfigMain.ADDONS_DYNMAP_MARKER_LABEL = cfg.getString("addons.dynmap.markers.label", ConfigMain.ADDONS_DYNMAP_MARKER_LABEL);
		ConfigMain.ADDONS_DYNMAP_MARKER_ICON = cfg.getString("addons.dynmap.markers.icon", ConfigMain.ADDONS_DYNMAP_MARKER_ICON);
		
		ConfigMain.ADDONS_GRIEFPREVENTION_ENABLE = cfg.getBoolean("addons.griefprevention.enable", ConfigMain.ADDONS_GRIEFPREVENTION_ENABLE);
		ConfigMain.ADDONS_GRIEFPREVENTION_NEEDOWNER = cfg.getBoolean("addons.griefprevention.need-to-be-owner-claim", ConfigMain.ADDONS_GRIEFPREVENTION_NEEDOWNER);
		ConfigMain.ADDONS_GRIEFPREVENTION_CMD_TRUST = cfg.getString("addons.griefprevention.sub-commands.trust", ConfigMain.ADDONS_GRIEFPREVENTION_CMD_TRUST);
		ConfigMain.ADDONS_GRIEFPREVENTION_CMD_CONTAINER = cfg.getString("addons.griefprevention.sub-commands.container", ConfigMain.ADDONS_GRIEFPREVENTION_CMD_CONTAINER);
		ConfigMain.ADDONS_GRIEFPREVENTION_CMD_ACCESS = cfg.getString("addons.griefprevention.sub-commands.access", ConfigMain.ADDONS_GRIEFPREVENTION_CMD_ACCESS);
		ConfigMain.ADDONS_GRIEFPREVENTION_CMD_REMOVE = cfg.getString("addons.griefprevention.sub-commands.remove", ConfigMain.ADDONS_GRIEFPREVENTION_CMD_REMOVE);
		
		ConfigMain.ADDONS_TABLIST_ENABLE = cfg.getBoolean("addons.tablist.enable", ConfigMain.ADDONS_TABLIST_ENABLE);
		ConfigMain.ADDONS_TABLIST_INPARTY = cfg.getString("addons.tablist.in-party", ConfigMain.ADDONS_TABLIST_INPARTY);
		ConfigMain.ADDONS_TABLIST_OUTPARTY = cfg.getString("addons.tablist.out-party", ConfigMain.ADDONS_TABLIST_OUTPARTY);
		ConfigMain.ADDONS_TABLIST_HEADER_INPARTY = cfg.getString("addons.tablist.header.in-party", ConfigMain.ADDONS_TABLIST_HEADER_INPARTY);
		ConfigMain.ADDONS_TABLIST_HEADER_OUTPARTY = cfg.getString("addons.tablist.header.out-party", ConfigMain.ADDONS_TABLIST_HEADER_OUTPARTY);
		ConfigMain.ADDONS_TABLIST_FOOTER_INPARTY = cfg.getString("addons.tablist.footer.in-party", ConfigMain.ADDONS_TABLIST_FOOTER_INPARTY);
		ConfigMain.ADDONS_TABLIST_FOOTER_OUTPARTY = cfg.getString("addons.tablist.footer.out-party", ConfigMain.ADDONS_TABLIST_FOOTER_OUTPARTY);
		
		ConfigMain.ADDONS_VAULT_ENABLE = cfg.getBoolean("addons.vault.enable", ConfigMain.ADDONS_VAULT_ENABLE);
		ConfigMain.ADDONS_VAULT_CONFIRM_ENABLE = cfg.getBoolean("addons.vault.confirm-command.enable", ConfigMain.ADDONS_VAULT_CONFIRM_ENABLE);
		ConfigMain.ADDONS_VAULT_CONFIRM_TIMEOUT = cfg.getInt("addons.vault.confirm-command.timeout", ConfigMain.ADDONS_VAULT_CONFIRM_TIMEOUT);
		ConfigMain.ADDONS_VAULT_PRICE_CLAIM = cfg.getDouble("addons.vault.price-commands.claim", ConfigMain.ADDONS_VAULT_PRICE_CLAIM);
		ConfigMain.ADDONS_VAULT_PRICE_COLOR = cfg.getDouble("addons.vault.price-commands.color", ConfigMain.ADDONS_VAULT_PRICE_COLOR);
		ConfigMain.ADDONS_VAULT_PRICE_CREATE = cfg.getDouble("addons.vault.price-commands.create", ConfigMain.ADDONS_VAULT_PRICE_CREATE);
		ConfigMain.ADDONS_VAULT_PRICE_DESC = cfg.getDouble("addons.vault.price-commands.desc", ConfigMain.ADDONS_VAULT_PRICE_DESC);
		ConfigMain.ADDONS_VAULT_PRICE_HOME = cfg.getDouble("addons.vault.price-commands.home", ConfigMain.ADDONS_VAULT_PRICE_HOME);
		ConfigMain.ADDONS_VAULT_PRICE_JOIN = cfg.getDouble("addons.vault.price-commands.join", ConfigMain.ADDONS_VAULT_PRICE_JOIN);
		ConfigMain.ADDONS_VAULT_PRICE_MOTD = cfg.getDouble("addons.vault.price-commands.motd", ConfigMain.ADDONS_VAULT_PRICE_MOTD);
		ConfigMain.ADDONS_VAULT_PRICE_PREFIX = cfg.getDouble("addons.vault.price-commands.prefix", ConfigMain.ADDONS_VAULT_PRICE_PREFIX);
		ConfigMain.ADDONS_VAULT_PRICE_SETHOME = cfg.getDouble("addons.vault.price-commands.set-home", ConfigMain.ADDONS_VAULT_PRICE_SETHOME);
		ConfigMain.ADDONS_VAULT_PRICE_SUFFIX = cfg.getDouble("addons.vault.price-commands.suffix", ConfigMain.ADDONS_VAULT_PRICE_SUFFIX);
		ConfigMain.ADDONS_VAULT_PRICE_TELEPORT = cfg.getDouble("addons.vault.price-commands.teleport", ConfigMain.ADDONS_VAULT_PRICE_TELEPORT);
		
		
		// Commands settings
		ConfigMain.COMMANDS_TABSUPPORT = cfg.getBoolean("commands.tab-support", ConfigMain.COMMANDS_TABSUPPORT);
		ConfigMain.COMMANDS_HELP_PERMBASED = cfg.getBoolean("commands.help-pages.permission-based", ConfigMain.COMMANDS_HELP_PERMBASED);
		ConfigMain.COMMANDS_HELP_PERPAGE = cfg.getInt("commands.help-pages.commands-per-page", ConfigMain.COMMANDS_HELP_PERPAGE);
		ConfigMain.COMMANDS_DESC_PARTY = cfg.getString("commands.descriptions.party", ConfigMain.COMMANDS_DESC_PARTY);
		ConfigMain.COMMANDS_DESC_P = cfg.getString("commands.descriptions.p", ConfigMain.COMMANDS_DESC_P);
		
		ConfigMain.COMMANDS_CMD_HELP = cfg.getString("commands.main-commands.help", ConfigMain.COMMANDS_CMD_HELP);
		ConfigMain.COMMANDS_CMD_PARTY = cfg.getString("commands.main-commands.party", ConfigMain.COMMANDS_CMD_PARTY);
		ConfigMain.COMMANDS_CMD_P = cfg.getString("commands.main-commands.p", ConfigMain.COMMANDS_CMD_P);
		ConfigMain.COMMANDS_CMD_ACCEPT = cfg.getString("commands.main-commands.accept", ConfigMain.COMMANDS_CMD_ACCEPT);
		ConfigMain.COMMANDS_CMD_CHAT = cfg.getString("commands.main-commands.chat", ConfigMain.COMMANDS_CMD_CHAT);
		ConfigMain.COMMANDS_CMD_CLAIM = cfg.getString("commands.main-commands.claim", ConfigMain.COMMANDS_CMD_CLAIM);
		ConfigMain.COMMANDS_CMD_COLOR = cfg.getString("commands.main-commands.color", ConfigMain.COMMANDS_CMD_COLOR);
		ConfigMain.COMMANDS_CMD_CONFIRM = cfg.getString("commands.main-commands.confirm", ConfigMain.COMMANDS_CMD_CONFIRM);
		ConfigMain.COMMANDS_CMD_CREATE = cfg.getString("commands.main-commands.create", ConfigMain.COMMANDS_CMD_CREATE);
		ConfigMain.COMMANDS_CMD_DELETE = cfg.getString("commands.main-commands.delete", ConfigMain.COMMANDS_CMD_DELETE);
		ConfigMain.COMMANDS_CMD_DENY = cfg.getString("commands.main-commands.deny", ConfigMain.COMMANDS_CMD_DENY);
		ConfigMain.COMMANDS_CMD_DESC = cfg.getString("commands.main-commands.desc", ConfigMain.COMMANDS_CMD_DESC);
		ConfigMain.COMMANDS_CMD_HOME = cfg.getString("commands.main-commands.home", ConfigMain.COMMANDS_CMD_HOME);
		ConfigMain.COMMANDS_CMD_IGNORE = cfg.getString("commands.main-commands.ignore", ConfigMain.COMMANDS_CMD_IGNORE);
		ConfigMain.COMMANDS_CMD_INFO = cfg.getString("commands.main-commands.info", ConfigMain.COMMANDS_CMD_INFO);
		ConfigMain.COMMANDS_CMD_INVITE = cfg.getString("commands.main-commands.invite", ConfigMain.COMMANDS_CMD_INVITE);
		ConfigMain.COMMANDS_CMD_JOIN = cfg.getString("commands.main-commands.join", ConfigMain.COMMANDS_CMD_JOIN);
		ConfigMain.COMMANDS_CMD_KICK = cfg.getString("commands.main-commands.kick", ConfigMain.COMMANDS_CMD_KICK);
		ConfigMain.COMMANDS_CMD_LEAVE = cfg.getString("commands.main-commands.leave", ConfigMain.COMMANDS_CMD_LEAVE);
		ConfigMain.COMMANDS_CMD_LIST = cfg.getString("commands.main-commands.list", ConfigMain.COMMANDS_CMD_LIST);
		ConfigMain.COMMANDS_CMD_MIGRATE = cfg.getString("commands.main-commands.migrate", ConfigMain.COMMANDS_CMD_MIGRATE);
		ConfigMain.COMMANDS_CMD_MOTD = cfg.getString("commands.main-commands.motd", ConfigMain.COMMANDS_CMD_MOTD);
		ConfigMain.COMMANDS_CMD_NOTIFY = cfg.getString("commands.main-commands.notify", ConfigMain.COMMANDS_CMD_NOTIFY);
		ConfigMain.COMMANDS_CMD_PASSWORD = cfg.getString("commands.main-commands.password", ConfigMain.COMMANDS_CMD_PASSWORD);
		ConfigMain.COMMANDS_CMD_PREFIX = cfg.getString("commands.main-commands.prefix", ConfigMain.COMMANDS_CMD_PREFIX);
		ConfigMain.COMMANDS_CMD_RANK = cfg.getString("commands.main-commands.rank", ConfigMain.COMMANDS_CMD_RANK);
		ConfigMain.COMMANDS_CMD_RELOAD = cfg.getString("commands.main-commands.reload", ConfigMain.COMMANDS_CMD_RELOAD);
		ConfigMain.COMMANDS_CMD_RENAME = cfg.getString("commands.main-commands.rename", ConfigMain.COMMANDS_CMD_RENAME);
		ConfigMain.COMMANDS_CMD_SETHOME = cfg.getString("commands.main-commands.sethome", ConfigMain.COMMANDS_CMD_SETHOME);
		ConfigMain.COMMANDS_CMD_SPY = cfg.getString("commands.main-commands.spy", ConfigMain.COMMANDS_CMD_SPY);
		ConfigMain.COMMANDS_CMD_SUFFIX = cfg.getString("commands.main-commands.suffix", ConfigMain.COMMANDS_CMD_SUFFIX);
		ConfigMain.COMMANDS_CMD_TELEPORT = cfg.getString("commands.main-commands.teleport", ConfigMain.COMMANDS_CMD_TELEPORT);
		
		ConfigMain.COMMANDS_SUB_ON = cfg.getString("commands.sub-commands.on", ConfigMain.COMMANDS_SUB_ON);
		ConfigMain.COMMANDS_SUB_OFF = cfg.getString("commands.sub-commands.off", ConfigMain.COMMANDS_SUB_OFF);
		ConfigMain.COMMANDS_SUB_SILENT = cfg.getString("commands.sub-commands.silent", ConfigMain.COMMANDS_SUB_SILENT);
		ConfigMain.COMMANDS_SUB_FIXED = cfg.getString("commands.sub-commands.fixed", ConfigMain.COMMANDS_SUB_FIXED);
		ConfigMain.COMMANDS_SUB_REMOVE = cfg.getString("commands.sub-commands.remove", ConfigMain.COMMANDS_SUB_REMOVE);
	}
	
	private void reloadConfigParties() {
		File file = new File(plugin.getDataFolder(), "parties.yml");
		if (!file.exists()) {
			plugin.saveResource("parties.yml", true);
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if (cfg.getInt("dont-edit-this.config-version") < Constants.VERSION_CONFIG_PARTIES) {
			// Normal log, LogHandler will give error
			plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "File parties.yml is outdated");
		}
		
		// General settings
		ConfigParties.GENERAL_MEMBERSLIMIT = cfg.getInt("general.members-limit", ConfigParties.GENERAL_MEMBERSLIMIT);
		ConfigParties.GENERAL_NAME_ALLOWEDCHARS = cfg.getString("general.name.allowed-characters", ConfigParties.GENERAL_NAME_ALLOWEDCHARS);
		ConfigParties.GENERAL_NAME_MINLENGTH = cfg.getInt("general.name.minimum-length", ConfigParties.GENERAL_NAME_MINLENGTH);
		ConfigParties.GENERAL_NAME_MAXLENGTH = cfg.getInt("general.name.maximum-length", ConfigParties.GENERAL_NAME_MAXLENGTH);
		ConfigParties.GENERAL_NAME_RENAMECD = cfg.getInt("general.name.rename-cooldown", ConfigParties.GENERAL_NAME_RENAMECD);
		
		ConfigParties.GENERAL_INVITE_TIMEOUT = cfg.getInt("general.invite.timeout", ConfigParties.GENERAL_INVITE_TIMEOUT);
		ConfigParties.GENERAL_INVITE_REVOKE = cfg.getBoolean("general.invite.revoke", ConfigParties.GENERAL_INVITE_REVOKE);
		ConfigParties.GENERAL_INVITE_PREVENTINVITEPERM = cfg.getBoolean("general.invite.prevent-invite-player-no-permission-join", ConfigParties.GENERAL_INVITE_PREVENTINVITEPERM);
		ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE = cfg.getBoolean("general.invite.cooldown.enable", ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE);
		ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL = cfg.getInt("general.invite.cooldown.global", ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL);
		ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL = cfg.getInt("general.invite.cooldown.individual", ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL);
		
		ConfigParties.GENERAL_CHAT_ALLOWCOLORS = cfg.getBoolean("general.chat.allow-colors", ConfigParties.GENERAL_CHAT_ALLOWCOLORS);
		ConfigParties.GENERAL_CHAT_TOGGLECHATCMD = cfg.getBoolean("general.chat.enable-toggle-command", ConfigParties.GENERAL_CHAT_TOGGLECHATCMD);
		ConfigParties.GENERAL_CHAT_CHATCD = cfg.getInt("general.chat.chat-cooldown", ConfigParties.GENERAL_CHAT_CHATCD);
		ConfigParties.GENERAL_CHAT_FORMAT_PARTY = cfg.getString("general.chat.formats.party-chat", ConfigParties.GENERAL_CHAT_FORMAT_PARTY);
		ConfigParties.GENERAL_CHAT_FORMAT_SPY = cfg.getString("general.chat.formats.spy-alerts", ConfigParties.GENERAL_CHAT_FORMAT_SPY);
		ConfigParties.GENERAL_CHAT_FORMAT_BROADCAST = cfg.getString("general.chat.formats.broadcast", ConfigParties.GENERAL_CHAT_FORMAT_BROADCAST);
		
		
		// Ranks settings
		handleRanks(cfg, "ranks");
		
		
		// Additional settings
		ConfigParties.COLOR_ENABLE = cfg.getBoolean("additional.color.enable", ConfigParties.COLOR_ENABLE);
		ConfigParties.COLOR_COLORCMD = cfg.getBoolean("additional.color.color-command", ConfigParties.COLOR_COLORCMD);
		ConfigParties.COLOR_DYNAMIC = cfg.getBoolean("additional.color.dynamic-color", ConfigParties.COLOR_DYNAMIC);
		handleColors(cfg, "additional.color.list-colors");
		
		ConfigParties.DESC_ENABLE = cfg.getBoolean("additional.description.enable", ConfigParties.DESC_ENABLE);
		ConfigParties.DESC_MINLENGTH = cfg.getInt("additional.description.minimum-length", ConfigParties.DESC_MINLENGTH);
		ConfigParties.DESC_MAXLENGTH = cfg.getInt("additional.description.maximum-length", ConfigParties.DESC_MAXLENGTH);
		ConfigParties.DESC_ALLOWEDCHARS = cfg.getString("additional.description.allowed-characters", ConfigParties.DESC_ALLOWEDCHARS);
		
		ConfigParties.FIXED_ENABLE = cfg.getBoolean("additional.fixed-system.enable", ConfigParties.FIXED_ENABLE);
		ConfigParties.FIXED_DEFAULT_ENABLE = cfg.getBoolean("additional.fixed-system.default-party.enable", ConfigParties.FIXED_DEFAULT_ENABLE);
		ConfigParties.FIXED_DEFAULT_PARTY = cfg.getString("additional.fixed-system.default-party.party", ConfigParties.FIXED_DEFAULT_PARTY);

		ConfigParties.FRIENDLYFIRE_ENABLE = cfg.getBoolean("additional.friendly-fire.enable-protection", ConfigParties.FRIENDLYFIRE_ENABLE);
		ConfigParties.FRIENDLYFIRE_WARNONFIGHT = cfg.getBoolean("additional.friendly-fire.warn-players-on-fight", ConfigParties.FRIENDLYFIRE_WARNONFIGHT);
		ConfigParties.FRIENDLYFIRE_LISTWORLDS = cfg.getStringList("additional.friendly-fire.list-worlds");
		ConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE = cfg.getBoolean("additional.friendly-fire.crackshot.enable", ConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE);
		
		ConfigParties.HOME_ENABLE = cfg.getBoolean("additional.home.enable", ConfigParties.HOME_ENABLE);
		ConfigParties.HOME_DELAY = cfg.getInt("additional.home.delay", ConfigParties.HOME_DELAY);
		ConfigParties.HOME_HIT = cfg.getBoolean("additional.home.cancel.hit", ConfigParties.HOME_HIT);
		ConfigParties.HOME_MOVING = cfg.getBoolean("additional.home.cancel.moving", ConfigParties.HOME_MOVING);
		ConfigParties.HOME_DISTANCE = cfg.getInt("additional.home.cancel.distance", ConfigParties.HOME_DISTANCE);
		
		ConfigParties.PASSWORD_ENABLE = cfg.getBoolean("additional.join-password.enable", ConfigParties.PASSWORD_ENABLE);
		ConfigParties.PASSWORD_ALLOWECHARS = cfg.getString("additional.join-password.allowed-characters", ConfigParties.PASSWORD_ALLOWECHARS);
		ConfigParties.PASSWORD_HASH = cfg.getString("additional.join-password.hash", ConfigParties.PASSWORD_HASH);
		ConfigParties.PASSWORD_ENCODE = cfg.getString("additional.join-password.encode", ConfigParties.PASSWORD_ENCODE);
		ConfigParties.PASSWORD_MINLENGTH = cfg.getInt("additional.join-password.minimum-length", ConfigParties.PASSWORD_MINLENGTH);
		ConfigParties.PASSWORD_MAXLENGTH = cfg.getInt("additional.join-password.maximum-length", ConfigParties.PASSWORD_MAXLENGTH);
		
		ConfigParties.KILLS_ENABLE = cfg.getBoolean("additional.kills.enable", ConfigParties.KILLS_ENABLE);
		ConfigParties.KILLS_SAVEKILLS = cfg.getBoolean("additional.kills.save-kills", ConfigParties.KILLS_SAVEKILLS);
		ConfigParties.KILLS_MOB_NEUTRAL = cfg.getBoolean("additional.kills.which-save.neutral-mobs", ConfigParties.KILLS_MOB_NEUTRAL);
		ConfigParties.KILLS_MOB_HOSTILE = cfg.getBoolean("additional.kills.which-save.hostile-mobs", ConfigParties.KILLS_MOB_HOSTILE);
		ConfigParties.KILLS_MOB_PLAYERS = cfg.getBoolean("additional.kills.which-save.players", ConfigParties.KILLS_MOB_PLAYERS);
		
		ConfigParties.LIST_ENABLE = cfg.getBoolean("additional.list.enable", ConfigParties.LIST_ENABLE);
		ConfigParties.LIST_ORDEREDBY = cfg.getString("additional.list.ordered-by", ConfigParties.LIST_ORDEREDBY);
		ConfigParties.LIST_FILTERMIN = cfg.getInt("additional.list.filter-min", ConfigParties.LIST_FILTERMIN);
		ConfigParties.LIST_PERPAGE = cfg.getInt("additional.list.parties-per-page", ConfigParties.LIST_PERPAGE);
		ConfigParties.LIST_LIMITPARTIES = cfg.getInt("additional.list.limit-parties", ConfigParties.LIST_LIMITPARTIES);
		ConfigParties.LIST_HIDDENPARTIES = cfg.getStringList("additional.list.hidden-parties");
		
		ConfigParties.MOTD_ENABLE = cfg.getBoolean("additional.motd.enable", ConfigParties.MOTD_ENABLE);
		ConfigParties.MOTD_MINLENGTH = cfg.getInt("additional.motd.minimum-length", ConfigParties.MOTD_MINLENGTH);
		ConfigParties.MOTD_MAXLENGTH = cfg.getInt("additional.motd.maximum-length", ConfigParties.MOTD_MAXLENGTH);
		ConfigParties.MOTD_DELAY = cfg.getInt("additional.motd.delay", ConfigParties.MOTD_DELAY);
		ConfigParties.MOTD_ALLOWEDCHARS = cfg.getString("additional.motd.allowed-characters", ConfigParties.MOTD_ALLOWEDCHARS);
		ConfigParties.MOTD_NEWLINECODE = cfg.getString("additional.motd.new-line-code", ConfigParties.MOTD_NEWLINECODE);
		
		ConfigParties.TELEPORT_ENABLE = cfg.getBoolean("additional.teleport.enable", ConfigParties.TELEPORT_ENABLE);
		ConfigParties.TELEPORT_COOLDOWN = cfg.getInt("additional.teleport.cooldown", ConfigParties.TELEPORT_COOLDOWN);
	}
	private void handleRanks(ConfigurationSection cfg, String path) {
		Set<Rank> ranks = new HashSet<Rank>();
		Rank def = null;
		
		Rank lower = null;
		Rank higher = null;
		
		ConfigurationSection csRanks = cfg.getConfigurationSection(path);
		if (csRanks != null) {
			for (String key : csRanks.getKeys(false)) {
				int rank = csRanks.getInt(key + ".rank", 1);
				String name = csRanks.getString(key + ".name", key);
				String chat = csRanks.getString(key + ".chat", name);
				boolean dft = csRanks.getBoolean(key + ".default", false);
				List<String> perm = csRanks.getStringList(key + ".permissions");
				Rank newRank = new RankImpl(rank, key, name, chat, dft, perm);
				ranks.add(newRank);
				
				if (dft)
					def = newRank;
				
				if (lower == null || newRank.getLevel() < lower.getLevel())
					lower = newRank;
				if (higher == null || newRank.getLevel() > higher.getLevel())
					higher = newRank;
			}
			
			if (lower != null) {
				if (def == null) {
					// Give error: default rank not found
					def = lower;
					plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "Cannot find default rank, set lower one.");
				}
				
				// Save rank list
				ConfigParties.RANK_LIST = ranks;
				ConfigParties.RANK_SET_DEFAULT = def.getLevel();
				ConfigParties.RANK_SET_HIGHER = higher.getLevel();
			} else {
				// Give error: no ranks found
				plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "Rank list empty, restoring default one.");
			}
		} else {
			// Give error: no ranks node found
			plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "Cannot find rank list, restoring default one.");
		}
	}
	private void handleColors(ConfigurationSection cfg, String path) {
		Set<Color> colors = new HashSet<Color>();
		
		ConfigurationSection csColors = cfg.getConfigurationSection(path);
		if (csColors != null) {
			for (String key : csColors.getKeys(false)) {
				String name = key;
				String command = csColors.getString(key + ".command", "");
				String code = csColors.getString(key + ".code", "");
				int dynP = csColors.getInt(key + ".dynamic.priority", -1);
				int dynM = csColors.getInt(key + ".dynamic.members", -1);
				int dynK = csColors.getInt(key + ".dynamic.kills", -1);
				
				ColorImpl pc = new ColorImpl(name, command, code, dynP, dynM, dynK);
				colors.add(pc);
			}
			
			ConfigParties.COLOR_LIST = colors;
		} else {
			// Give error: no ranks node found
			plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "Cannot find color list, restoring default one.");
		}
	}
	
	
	private void reloadMessages() {
		File file = new File(plugin.getDataFolder(), "messages.yml");
		if (!file.exists()) {
			plugin.saveResource("messages.yml", true);
		}
		FileConfiguration msg = YamlConfiguration.loadConfiguration(file);
		
		if (msg.getInt("dont-edit-this.messages-version") < Constants.VERSION_MESSAGES) {
			// Normal log, LogHandler will give error
			plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + "File messages.yml is outdated");
		}
		
		
		// Parties messages
		Messages.PARTIES_UPDATEAVAILABLE = msg.getString("parties.update-available", Messages.PARTIES_UPDATEAVAILABLE);
		
		Messages.PARTIES_COMMON_INVALIDCMD = msg.getString("parties.common-messages.invalid-command", Messages.PARTIES_COMMON_INVALIDCMD);
		Messages.PARTIES_COMMON_CONFIGRELOAD = msg.getString("parties.common-messages.configuration-reloaded", Messages.PARTIES_COMMON_CONFIGRELOAD);
		Messages.PARTIES_COMMON_NOTINPARTY = msg.getString("parties.common-messages.not-in-party", Messages.PARTIES_COMMON_NOTINPARTY);
		Messages.PARTIES_COMMON_PARTYNOTFOUND = msg.getString("parties.common-messages.party-not-found", Messages.PARTIES_COMMON_PARTYNOTFOUND);
		Messages.PARTIES_COMMON_PARTYFULL = msg.getString("parties.common-messages.party-full", Messages.PARTIES_COMMON_PARTYFULL);
		
		Messages.PARTIES_PERM_NOPERM = msg.getString("parties.permissions.no-permission", Messages.PARTIES_PERM_NOPERM);
		Messages.PARTIES_PERM_NORANK = msg.getString("parties.permissions.no-permission-in-party", Messages.PARTIES_PERM_NORANK);
		
		
		// Main commands messages
		Messages.MAINCMD_ACCEPT_BROADCAST = msg.getString("main-commands.accept.broadcast", Messages.MAINCMD_ACCEPT_BROADCAST);
		Messages.MAINCMD_ACCEPT_ACCEPTED = msg.getString("main-commands.accept.accepted", Messages.MAINCMD_ACCEPT_ACCEPTED);
		Messages.MAINCMD_ACCEPT_ACCEPTRECEIPT = msg.getString("main-commands.accept.accept-receipt", Messages.MAINCMD_ACCEPT_ACCEPTRECEIPT);
		Messages.MAINCMD_ACCEPT_ALREADYINPARTY = msg.getString("main-commands.accept.already-in-party", Messages.MAINCMD_ACCEPT_ALREADYINPARTY);
		Messages.MAINCMD_ACCEPT_NOINVITE = msg.getString("main-commands.accept.no-invite", Messages.MAINCMD_ACCEPT_NOINVITE);
		Messages.MAINCMD_ACCEPT_NOEXISTS = msg.getString("main-commands.accept.no-exists", Messages.MAINCMD_ACCEPT_NOEXISTS);
		
		Messages.MAINCMD_CHAT_ENABLED = msg.getString("main-commands.chat.enabled", Messages.MAINCMD_CHAT_ENABLED);
		Messages.MAINCMD_CHAT_DISABLED = msg.getString("main-commands.chat.disabled", Messages.MAINCMD_CHAT_DISABLED);
		Messages.MAINCMD_CHAT_WRONGCMD = msg.getString("main-commands.chat.wrong-command", Messages.MAINCMD_CHAT_WRONGCMD);
		
		Messages.MAINCMD_CREATE_CREATED = msg.getString("main-commands.create.created", Messages.MAINCMD_CREATE_CREATED);
		Messages.MAINCMD_CREATE_CREATEDFIXED = msg.getString("main-commands.create.created-fixed", Messages.MAINCMD_CREATE_CREATEDFIXED);
		Messages.MAINCMD_CREATE_NAMEEXISTS = msg.getString("main-commands.create.name-already-exists", Messages.MAINCMD_CREATE_NAMEEXISTS);
		Messages.MAINCMD_CREATE_ALREADYINPARTY = msg.getString("main-commands.create.already-in-party", Messages.MAINCMD_CREATE_ALREADYINPARTY);
		Messages.MAINCMD_CREATE_NAMETOOLONG = msg.getString("main-commands.create.name-too-long", Messages.MAINCMD_CREATE_NAMETOOLONG);
		Messages.MAINCMD_CREATE_NAMETOOSHORT = msg.getString("main-commands.create.name-too-short", Messages.MAINCMD_CREATE_NAMETOOSHORT);
		Messages.MAINCMD_CREATE_INVALIDNAME = msg.getString("main-commands.create.invalid-name", Messages.MAINCMD_CREATE_INVALIDNAME);
		Messages.MAINCMD_CREATE_CENSORED = msg.getString("main-commands.create.censored", Messages.MAINCMD_CREATE_CENSORED);
		Messages.MAINCMD_CREATE_WRONGCMD = msg.getString("main-commands.create.wrong-command", Messages.MAINCMD_CREATE_WRONGCMD);
		
		Messages.MAINCMD_DELETE_DELETED = msg.getString("main-commands.delete.deleted", Messages.MAINCMD_DELETE_DELETED);
		Messages.MAINCMD_DELETE_DELETEDSILENTLY = msg.getString("main-commands.delete.deleted-silently", Messages.MAINCMD_DELETE_DELETEDSILENTLY);
		Messages.MAINCMD_DELETE_BROADCAST = msg.getString("main-commands.delete.broadcast", Messages.MAINCMD_DELETE_BROADCAST);
		Messages.MAINCMD_DELETE_WRONGCMD = msg.getString("main-commands.delete.wrong-command", Messages.MAINCMD_DELETE_WRONGCMD);
		
		Messages.MAINCMD_DENY_DENIED = msg.getString("main-commands.deny.denied", Messages.MAINCMD_DENY_DENIED);
		Messages.MAINCMD_DENY_DENYRECEIPT = msg.getString("main-commands.deny.deny-receipt", Messages.MAINCMD_DENY_DENYRECEIPT);
		Messages.MAINCMD_DENY_NOINVITE = msg.getString("main-commands.deny.no-invite", Messages.MAINCMD_DENY_NOINVITE);
		Messages.MAINCMD_DENY_NOEXISTS = msg.getString("main-commands.deny.no-exists", Messages.MAINCMD_DENY_NOEXISTS);
		
		Messages.MAINCMD_IGNORE_START = msg.getString("main-commands.ignore.start-ignore", Messages.MAINCMD_IGNORE_START);
		Messages.MAINCMD_IGNORE_STOP = msg.getString("main-commands.ignore.stop-ignore", Messages.MAINCMD_IGNORE_STOP);
		Messages.MAINCMD_IGNORE_LIST_HEADER = msg.getString("main-commands.ignore.ignore-list.header", Messages.MAINCMD_IGNORE_LIST_HEADER);
		Messages.MAINCMD_IGNORE_LIST_PARTYPREFIX = msg.getString("main-commands.ignore.ignore-list.party-prefix", Messages.MAINCMD_IGNORE_LIST_PARTYPREFIX);
		Messages.MAINCMD_IGNORE_LIST_SEPARATOR = msg.getString("main-commands.ignore.ignore-list.separator", Messages.MAINCMD_IGNORE_LIST_SEPARATOR);
		Messages.MAINCMD_IGNORE_LIST_EMPTY = msg.getString("main-commands.ignore.ignore-list.empty", Messages.MAINCMD_IGNORE_LIST_EMPTY);
		Messages.MAINCMD_IGNORE_WRONGCMD = msg.getString("main-commands.ignore.wrong-command", Messages.MAINCMD_IGNORE_WRONGCMD);
		
		Messages.MAINCMD_INFO_CONTENT = msg.getStringList("main-commands.info.content");
		Messages.MAINCMD_INFO_LIST_ONLINEPREFIX = msg.getString("main-commands.info.list.player-online-prefix", Messages.MAINCMD_INFO_LIST_ONLINEPREFIX);
		Messages.MAINCMD_INFO_LIST_OFFLINEPREFIX = msg.getString("main-commands.info.list.player-offline-prefix", Messages.MAINCMD_INFO_LIST_OFFLINEPREFIX);
		Messages.MAINCMD_INFO_LIST_SEPARATOR = msg.getString("main-commands.info.list.player-separator", Messages.MAINCMD_INFO_LIST_SEPARATOR);
		Messages.MAINCMD_INFO_LIST_EMPTY = msg.getString("main-commands.info.list.player-empty", Messages.MAINCMD_INFO_LIST_EMPTY);
		Messages.MAINCMD_INFO_LIST_UNKNOWN = msg.getString("main-commands.info.list.player-unknown", Messages.MAINCMD_INFO_LIST_UNKNOWN);
		Messages.MAINCMD_INFO_LIST_MISSING = msg.getString("main-commands.info.list.missing-value", Messages.MAINCMD_INFO_LIST_MISSING);
		Messages.MAINCMD_INFO_WRONGCMD = msg.getString("main-commands.info.wrong-command", Messages.MAINCMD_INFO_WRONGCMD);
		
		Messages.MAINCMD_INVITE_SENT = msg.getString("main-commands.invite.sent", Messages.MAINCMD_INVITE_SENT);
		Messages.MAINCMD_INVITE_PLAYERINVITED = msg.getString("main-commands.invite.player-invited", Messages.MAINCMD_INVITE_PLAYERINVITED);
		Messages.MAINCMD_INVITE_TIMEOUT_NORESPONSE = msg.getString("main-commands.invite.timeout.noresponse", Messages.MAINCMD_INVITE_TIMEOUT_NORESPONSE);
		Messages.MAINCMD_INVITE_TIMEOUT_TIMEOUT = msg.getString("main-commands.invite.timeout.timeout", Messages.MAINCMD_INVITE_TIMEOUT_TIMEOUT);
		Messages.MAINCMD_INVITE_REVOKE_SENT = msg.getString("main-commands.invite.revoke.sent-revoked", Messages.MAINCMD_INVITE_REVOKE_SENT);
		Messages.MAINCMD_INVITE_REVOKE_REVOKED = msg.getString("main-commands.invite.revoke.player-invite-revoked", Messages.MAINCMD_INVITE_REVOKE_REVOKED);
		Messages.MAINCMD_INVITE_COOLDOWN_GLOBAL = msg.getString("main-commands.invite.cooldown.global", Messages.MAINCMD_INVITE_COOLDOWN_GLOBAL);
		Messages.MAINCMD_INVITE_COOLDOWN_INDIVIDUAL = msg.getString("main-commands.invite.cooldown.individual", Messages.MAINCMD_INVITE_COOLDOWN_INDIVIDUAL);
		Messages.MAINCMD_INVITE_PLAYEROFFLINE = msg.getString("main-commands.invite.player-offline", Messages.MAINCMD_INVITE_PLAYEROFFLINE);
		Messages.MAINCMD_INVITE_PLAYERNOPERM = msg.getString("main-commands.invite.player-no-permission", Messages.MAINCMD_INVITE_PLAYERNOPERM);
		Messages.MAINCMD_INVITE_PLAYERINPARTY = msg.getString("main-commands.invite.player-in-party", Messages.MAINCMD_INVITE_PLAYERINPARTY);
		Messages.MAINCMD_INVITE_ALREADYINVITED = msg.getString("main-commands.invite.already-invited", Messages.MAINCMD_INVITE_ALREADYINVITED);
		Messages.MAINCMD_INVITE_WRONGCMD = msg.getString("main-commands.invite.wrong-command", Messages.MAINCMD_INVITE_WRONGCMD);
		
		Messages.MAINCMD_KICK_SENT = msg.getString("main-commands.kick.sent", Messages.MAINCMD_KICK_SENT);
		Messages.MAINCMD_KICK_PLAYERKICKED = msg.getString("main-commands.kick.player-kicked", Messages.MAINCMD_KICK_PLAYERKICKED);
		Messages.MAINCMD_KICK_BROADCAST = msg.getString("main-commands.kick.broadcast", Messages.MAINCMD_KICK_BROADCAST);
		Messages.MAINCMD_KICK_BROADCAST_DISBAND = msg.getString("main-commands.kick.broadcast-disband", Messages.MAINCMD_KICK_BROADCAST_DISBAND);
		Messages.MAINCMD_KICK_PLAYERHIGHERRANK = msg.getString("main-commands.kick.player-higher-rank", Messages.MAINCMD_KICK_PLAYERHIGHERRANK);
		Messages.MAINCMD_KICK_PLAYERNOTINPARTY = msg.getString("main-commands.kick.player-not-in-party", Messages.MAINCMD_KICK_PLAYERNOTINPARTY);
		Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER = msg.getString("main-commands.kick.player-not-in-other-party", Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER);
		Messages.MAINCMD_KICK_CONFLICT_CONTENT = msg.getStringList("main-commands.kick.players-conflict.content");
		Messages.MAINCMD_KICK_CONFLICT_PLAYER = msg.getString("main-commands.kick.players-conflict.player", Messages.MAINCMD_KICK_CONFLICT_PLAYER);
		Messages.MAINCMD_KICK_WRONGCMD = msg.getString("main-commands.kick.wrong-command", Messages.MAINCMD_KICK_WRONGCMD);
		
		Messages.MAINCMD_LEAVE_LEFT = msg.getString("main-commands.leave.left", Messages.MAINCMD_LEAVE_LEFT);
		Messages.MAINCMD_LEAVE_BROADCAST = msg.getString("main-commands.leave.broadcast", Messages.MAINCMD_LEAVE_BROADCAST);
		Messages.MAINCMD_LEAVE_DISBANDED = msg.getString("main-commands.leave.party-disbanded", Messages.MAINCMD_LEAVE_DISBANDED);
		
		Messages.MAINCMD_MIGRATE_INFO = msg.getString("main-commands.migrate.info", Messages.MAINCMD_MIGRATE_INFO);
		Messages.MAINCMD_MIGRATE_COMPLETED = msg.getString("main-commands.migrate.completed", Messages.MAINCMD_MIGRATE_COMPLETED);
		Messages.MAINCMD_MIGRATE_FAILED_DBOFFLINE = msg.getString("main-commands.migrate.failed.database-offline", Messages.MAINCMD_MIGRATE_FAILED_DBOFFLINE);
		Messages.MAINCMD_MIGRATE_FAILED_FAILED = msg.getString("main-commands.migrate.failed.failed", Messages.MAINCMD_MIGRATE_FAILED_FAILED);
		Messages.MAINCMD_MIGRATE_FAILED_SAMEDB = msg.getString("main-commands.migrate.failed.same-database", Messages.MAINCMD_MIGRATE_FAILED_SAMEDB);
		Messages.MAINCMD_MIGRATE_WRONGDB = msg.getString("main-commands.migrate.wrong-database", Messages.MAINCMD_MIGRATE_WRONGDB);
		Messages.MAINCMD_MIGRATE_WRONGCMD = msg.getString("main-commands.migrate.wrong-command", Messages.MAINCMD_MIGRATE_WRONGCMD);
		
		Messages.MAINCMD_P_COOLDOWN = msg.getString("main-commands.p.cooldown", Messages.MAINCMD_P_COOLDOWN);
		Messages.MAINCMD_P_WRONGCMD = msg.getString("main-commands.p.wrong-command", Messages.MAINCMD_P_WRONGCMD);
		
		Messages.MAINCMD_RANK_CHANGED = msg.getString("main-commands.rank.changed", Messages.MAINCMD_RANK_CHANGED);
		Messages.MAINCMD_RANK_BROADCAST = msg.getString("main-commands.rank.broadcast", Messages.MAINCMD_RANK_BROADCAST);
		Messages.MAINCMD_RANK_WRONGRANK = msg.getString("main-commands.rank.wrong-rank", Messages.MAINCMD_RANK_WRONGRANK);
		Messages.MAINCMD_RANK_SAMERANK = msg.getString("main-commands.rank.same-rank", Messages.MAINCMD_RANK_SAMERANK);
		Messages.MAINCMD_RANK_LOWRANK = msg.getString("main-commands.rank.low-rank", Messages.MAINCMD_RANK_LOWRANK);
		Messages.MAINCMD_RANK_TOHIGHERRANK = msg.getString("main-commands.rank.to-higher-rank", Messages.MAINCMD_RANK_TOHIGHERRANK);
		Messages.MAINCMD_RANK_CHANGINGYOURSELF = msg.getString("main-commands.rank.changing-yourself", Messages.MAINCMD_RANK_CHANGINGYOURSELF);
		Messages.MAINCMD_RANK_PLAYERNOTINPARTY = msg.getString("main-commands.rank.player-not-in-party", Messages.MAINCMD_RANK_PLAYERNOTINPARTY);
		Messages.MAINCMD_RANK_PLAYERNOTINPARTY_OTHER = msg.getString("main-commands.rank.player-not-in-other-party", Messages.MAINCMD_RANK_PLAYERNOTINPARTY_OTHER);
		Messages.MAINCMD_RANK_WRONGCMD = msg.getString("main-commands.rank.wrong-command", Messages.MAINCMD_RANK_WRONGCMD);
		
		Messages.MAINCMD_RENAME_RENAMED = msg.getString("main-commands.rename.renamed", Messages.MAINCMD_RENAME_RENAMED);
		Messages.MAINCMD_RENAME_BROADCAST = msg.getString("main-commands.rename.broadcast", Messages.MAINCMD_RENAME_BROADCAST);
		Messages.MAINCMD_RENAME_WRONGCMD = msg.getString("main-commands.rename.wrong-command", Messages.MAINCMD_RENAME_WRONGCMD);
		Messages.MAINCMD_RENAME_WRONGCMD_ADMIN = msg.getString("main-commands.rename.wrong-command-admin", Messages.MAINCMD_RENAME_WRONGCMD_ADMIN);
		
		Messages.MAINCMD_SPY_ENABLED = msg.getString("main-commands.spy.enabled", Messages.MAINCMD_SPY_ENABLED);
		Messages.MAINCMD_SPY_DISABLED = msg.getString("main-commands.spy.disabled", Messages.MAINCMD_SPY_DISABLED);

		
		// Additional commands messages
		Messages.ADDCMD_CLAIM_CLAIMED = msg.getString("additional-commands.claim.claimed", Messages.ADDCMD_CLAIM_CLAIMED);
		Messages.ADDCMD_CLAIM_REMOVED = msg.getString("additional-commands.claim.removed", Messages.ADDCMD_CLAIM_REMOVED);
		Messages.ADDCMD_CLAIM_NOMANAGER = msg.getString("additional-commands.claim.no-manager", Messages.ADDCMD_CLAIM_NOMANAGER);
		Messages.ADDCMD_CLAIM_CLAIMNOEXISTS = msg.getString("additional-commands.claim.claim-no-exists", Messages.ADDCMD_CLAIM_CLAIMNOEXISTS);
		Messages.ADDCMD_CLAIM_WRONGCMD = msg.getString("additional-commands.claim.wrong-command", Messages.ADDCMD_CLAIM_WRONGCMD);
		
		Messages.ADDCMD_COLOR_INFO = msg.getString("additional-commands.color.info", Messages.ADDCMD_COLOR_INFO);
		Messages.ADDCMD_COLOR_EMPTY = msg.getString("additional-commands.color.empty", Messages.ADDCMD_COLOR_EMPTY);
		Messages.ADDCMD_COLOR_CHANGED = msg.getString("additional-commands.color.changed", Messages.ADDCMD_COLOR_CHANGED);
		Messages.ADDCMD_COLOR_REMOVED = msg.getString("additional-commands.color.removed", Messages.ADDCMD_COLOR_REMOVED);
		Messages.ADDCMD_COLOR_BROADCAST = msg.getString("additional-commands.color.broadcast", Messages.ADDCMD_COLOR_BROADCAST);
		Messages.ADDCMD_COLOR_WRONGCOLOR = msg.getString("additional-commands.color.wrong-color", Messages.ADDCMD_COLOR_WRONGCOLOR);
		Messages.ADDCMD_COLOR_WRONGCMD = msg.getString("additional-commands.color.wrong-command", Messages.ADDCMD_COLOR_WRONGCMD);
		
		Messages.ADDCMD_DESC_CHANGED = msg.getString("additional-commands.desc.changed", Messages.ADDCMD_DESC_CHANGED);
		Messages.ADDCMD_DESC_REMOVED = msg.getString("additional-commands.desc.removed", Messages.ADDCMD_DESC_REMOVED);
		Messages.ADDCMD_DESC_BROADCAST = msg.getString("additional-commands.desc.broadcast", Messages.ADDCMD_DESC_BROADCAST);
		Messages.ADDCMD_DESC_INVALID = msg.getString("additional-commands.desc.invalid-chars", Messages.ADDCMD_DESC_INVALID);
		Messages.ADDCMD_DESC_CENSORED = msg.getString("additional-commands.desc.censored", Messages.ADDCMD_DESC_CENSORED);
		Messages.ADDCMD_DESC_WRONGCMD = msg.getString("additional-commands.desc.wrong-command", Messages.ADDCMD_DESC_WRONGCMD);
		
		Messages.ADDCMD_HOME_TELEPORTED = msg.getString("additional-commands.home.teleported", Messages.ADDCMD_HOME_TELEPORTED);
		Messages.ADDCMD_HOME_TELEPORTIN = msg.getString("additional-commands.home.teleport-in", Messages.ADDCMD_HOME_TELEPORTIN);
		Messages.ADDCMD_HOME_TELEPORTDENIED = msg.getString("additional-commands.home.teleport-denied", Messages.ADDCMD_HOME_TELEPORTDENIED);
		Messages.ADDCMD_HOME_NOHOME = msg.getString("additional-commands.home.no-home", Messages.ADDCMD_HOME_NOHOME);
		Messages.ADDCMD_HOME_NOEXISTS = msg.getString("additional-commands.home.no-exists", Messages.ADDCMD_HOME_NOEXISTS);
		Messages.ADDCMD_HOME_WRONGCMD = msg.getString("additional-commands.home.wrong-command", Messages.ADDCMD_HOME_WRONGCMD);
		Messages.ADDCMD_HOME_WRONGCMD_ADMIN = msg.getString("additional-commands.home.wrong-command-admin", Messages.ADDCMD_HOME_WRONGCMD_ADMIN);
		
		
		Messages.ADDCMD_JOIN_JOINED = msg.getString("additional-commands.join.joined", Messages.ADDCMD_JOIN_JOINED);
		Messages.ADDCMD_JOIN_PLAYERJOINED = msg.getString("additional-commands.join.player-joined", Messages.ADDCMD_JOIN_PLAYERJOINED);
		Messages.ADDCMD_JOIN_ALREADYINPARTY = msg.getString("additional-commands.join.already-in-party", Messages.ADDCMD_JOIN_ALREADYINPARTY);
		Messages.ADDCMD_JOIN_WRONGPASSWORD = msg.getString("additional-commands.join.wrong-password", Messages.ADDCMD_JOIN_WRONGPASSWORD);
		Messages.ADDCMD_JOIN_WRONGCMD = msg.getString("additional-commands.join.wrong-command", Messages.ADDCMD_JOIN_WRONGCMD);
		
		Messages.ADDCMD_LIST_HEADER = msg.getString("additional-commands.list.header", Messages.ADDCMD_LIST_HEADER);
		Messages.ADDCMD_LIST_FOOTER = msg.getString("additional-commands.list.footer", Messages.ADDCMD_LIST_FOOTER);
		Messages.ADDCMD_LIST_NOONE = msg.getString("additional-commands.list.no-one", Messages.ADDCMD_LIST_NOONE);
		Messages.ADDCMD_LIST_FORMATPARTY = msg.getString("additional-commands.list.format-party", Messages.ADDCMD_LIST_FORMATPARTY);
		Messages.ADDCMD_LIST_WRONGCMD = msg.getString("additional-commands.list.wrong-command", Messages.ADDCMD_LIST_WRONGCMD);
		
		Messages.ADDCMD_MOTD_CHANGED = msg.getString("additional-commands.motd.changed", Messages.ADDCMD_MOTD_CHANGED);
		Messages.ADDCMD_MOTD_REMOVED = msg.getString("additional-commands.motd.removed", Messages.ADDCMD_MOTD_REMOVED);
		Messages.ADDCMD_MOTD_BROADCAST = msg.getString("additional-commands.motd.broadcast", Messages.ADDCMD_MOTD_BROADCAST);
		Messages.ADDCMD_MOTD_CONTENT = msg.getStringList("additional-commands.motd.content");
		Messages.ADDCMD_MOTD_INVALID = msg.getString("additional-commands.motd.invalid-chars", Messages.ADDCMD_MOTD_INVALID);
		Messages.ADDCMD_MOTD_CENSORED = msg.getString("additional-commands.motd.censored", Messages.ADDCMD_MOTD_CENSORED);
		Messages.ADDCMD_MOTD_WRONGCMD = msg.getString("additional-commands.motd.wrong-command", Messages.ADDCMD_MOTD_WRONGCMD);
		
		Messages.ADDCMD_NOTIFY_ON = msg.getString("additional-commands.notify.on", Messages.ADDCMD_NOTIFY_ON);
		Messages.ADDCMD_NOTIFY_OFF = msg.getString("additional-commands.notify.off", Messages.ADDCMD_NOTIFY_OFF);
		
		Messages.ADDCMD_PASSWORD_CHANGED = msg.getString("additional-commands.password.changed", Messages.ADDCMD_PASSWORD_CHANGED);
		Messages.ADDCMD_PASSWORD_REMOVED = msg.getString("additional-commands.password.removed", Messages.ADDCMD_PASSWORD_REMOVED);
		Messages.ADDCMD_PASSWORD_BROADCAST = msg.getString("additional-commands.password.broadcast", Messages.ADDCMD_PASSWORD_BROADCAST);
		Messages.ADDCMD_PASSWORD_INVALID = msg.getString("additional-commands.password.invalid-chars", Messages.ADDCMD_PASSWORD_INVALID);
		Messages.ADDCMD_PASSWORD_WRONGCMD = msg.getString("additional-commands.password.wrong-command", Messages.ADDCMD_PASSWORD_WRONGCMD);
		
		Messages.ADDCMD_PREFIX_CHANGED = msg.getString("additional-commands.prefix.changed", Messages.ADDCMD_PREFIX_CHANGED);
		Messages.ADDCMD_PREFIX_REMOVED = msg.getString("additional-commands.prefix.removed", Messages.ADDCMD_PREFIX_REMOVED);
		Messages.ADDCMD_PREFIX_BROADCAST = msg.getString("additional-commands.prefix.broadcast", Messages.ADDCMD_PREFIX_BROADCAST);
		Messages.ADDCMD_PREFIX_INVALID = msg.getString("additional-commands.prefix.invalid-chars", Messages.ADDCMD_PREFIX_INVALID);
		Messages.ADDCMD_PREFIX_CENSORED = msg.getString("additional-commands.prefix.censored", Messages.ADDCMD_PREFIX_CENSORED);
		Messages.ADDCMD_PREFIX_WRONGCMD = msg.getString("additional-commands.prefix.wrong-command", Messages.ADDCMD_PREFIX_WRONGCMD);
		
		Messages.ADDCMD_SETHOME_CHANGED = msg.getString("additional-commands.sethome.changed", Messages.ADDCMD_SETHOME_CHANGED);
		Messages.ADDCMD_SETHOME_REMOVED = msg.getString("additional-commands.sethome.removed", Messages.ADDCMD_SETHOME_REMOVED);
		Messages.ADDCMD_SETHOME_BROADCAST = msg.getString("additional-commands.sethome.broadcast", Messages.ADDCMD_SETHOME_BROADCAST);
		Messages.ADDCMD_SETHOME_WRONGCMD = msg.getString("additional-commands.sethome.wrong-command", Messages.ADDCMD_SETHOME_WRONGCMD);
		
		Messages.ADDCMD_SUFFIX_CHANGED = msg.getString("additional-commands.suffix.changed", Messages.ADDCMD_SUFFIX_CHANGED);
		Messages.ADDCMD_SUFFIX_REMOVED = msg.getString("additional-commands.suffix.removed", Messages.ADDCMD_SUFFIX_REMOVED);
		Messages.ADDCMD_SUFFIX_BROADCAST = msg.getString("additional-commands.suffix.broadcast", Messages.ADDCMD_SUFFIX_BROADCAST);
		Messages.ADDCMD_SUFFIX_INVALID = msg.getString("additional-commands.suffix.invalid-chars", Messages.ADDCMD_SUFFIX_INVALID);
		Messages.ADDCMD_SUFFIX_CENSORED = msg.getString("additional-commands.suffix.censored", Messages.ADDCMD_SUFFIX_CENSORED);
		Messages.ADDCMD_SUFFIX_WRONGCMD = msg.getString("additional-commands.suffix.wrong-command", Messages.ADDCMD_SUFFIX_WRONGCMD);
		
		Messages.ADDCMD_TELEPORT_TELEPORTING = msg.getString("additional-commands.teleport.teleporting", Messages.ADDCMD_TELEPORT_TELEPORTING);
		Messages.ADDCMD_TELEPORT_TELEPORTED = msg.getString("additional-commands.teleport.player-teleported", Messages.ADDCMD_TELEPORT_TELEPORTED);
		Messages.ADDCMD_TELEPORT_COOLDOWN = msg.getString("additional-commands.teleport.cooldown", Messages.ADDCMD_TELEPORT_COOLDOWN);
		
		Messages.ADDCMD_VAULT_NOMONEY_CLAIM = msg.getString("additional-commands.vault.no-money.claim", Messages.ADDCMD_VAULT_NOMONEY_CLAIM);
		Messages.ADDCMD_VAULT_NOMONEY_COLOR = msg.getString("additional-commands.vault.no-money.color", Messages.ADDCMD_VAULT_NOMONEY_COLOR);
		Messages.ADDCMD_VAULT_NOMONEY_CREATE = msg.getString("additional-commands.vault.no-money.create", Messages.ADDCMD_VAULT_NOMONEY_CREATE);
		Messages.ADDCMD_VAULT_NOMONEY_DESC = msg.getString("additional-commands.vault.no-money.desc", Messages.ADDCMD_VAULT_NOMONEY_DESC);
		Messages.ADDCMD_VAULT_NOMONEY_HOME = msg.getString("additional-commands.vault.no-money.home", Messages.ADDCMD_VAULT_NOMONEY_HOME);
		Messages.ADDCMD_VAULT_NOMONEY_JOIN = msg.getString("additional-commands.vault.no-money.join", Messages.ADDCMD_VAULT_NOMONEY_JOIN);
		Messages.ADDCMD_VAULT_NOMONEY_MOTD = msg.getString("additional-commands.vault.no-money.motd", Messages.ADDCMD_VAULT_NOMONEY_MOTD);
		Messages.ADDCMD_VAULT_NOMONEY_PREFIX = msg.getString("additional-commands.vault.no-money.prefix", Messages.ADDCMD_VAULT_NOMONEY_PREFIX);
		Messages.ADDCMD_VAULT_NOMONEY_SETHOME = msg.getString("additional-commands.vault.no-money.sethome", Messages.ADDCMD_VAULT_NOMONEY_SETHOME);
		Messages.ADDCMD_VAULT_NOMONEY_SUFFIX = msg.getString("additional-commands.vault.no-money.suffix", Messages.ADDCMD_VAULT_NOMONEY_SUFFIX);
		Messages.ADDCMD_VAULT_NOMONEY_TELEPORT = msg.getString("additional-commands.vault.no-money.teleport", Messages.ADDCMD_VAULT_NOMONEY_TELEPORT);
		Messages.ADDCMD_VAULT_CONFIRM_WARNONBUY = msg.getString("additional-commands.vault.confirm.warn-onbuy", Messages.ADDCMD_VAULT_CONFIRM_WARNONBUY);
		Messages.ADDCMD_VAULT_CONFIRM_CONFIRMED = msg.getString("additional-commands.vault.confirm.confirmed", Messages.ADDCMD_VAULT_CONFIRM_CONFIRMED);
		Messages.ADDCMD_VAULT_CONFIRM_NOCMD = msg.getString("additional-commands.vault.confirm.no-cmd", Messages.ADDCMD_VAULT_CONFIRM_NOCMD);
		
		
		// Other messages
		Messages.OTHER_FOLLOW_WORLD = msg.getString("other.follow.following-world", Messages.OTHER_FOLLOW_WORLD);
		Messages.OTHER_FOLLOW_SERVER = msg.getString("other.follow.following-server", Messages.OTHER_FOLLOW_SERVER);
		
		Messages.OTHER_FRIENDLYFIRE_CANTHIT = msg.getString("other.friendly-fire.cant-hit-mates", Messages.OTHER_FRIENDLYFIRE_CANTHIT);
		Messages.OTHER_FRIENDLYFIRE_WARN = msg.getString("other.friendly-fire.cant-hit-mates", Messages.OTHER_FRIENDLYFIRE_WARN);
		
		Messages.OTHER_EXP_VANILLA_GAINED = msg.getString("other.exp-system.vanilla.gained", Messages.OTHER_EXP_VANILLA_GAINED);
		Messages.OTHER_EXP_SKILLAPI_GAINED = msg.getString("other.exp-system.vanilla.gained-from-other", Messages.OTHER_EXP_SKILLAPI_GAINED);
		Messages.OTHER_EXP_VANILLA_GAINEDFROMOTHER = msg.getString("other.exp-system.skillapi.gained", Messages.OTHER_EXP_VANILLA_GAINEDFROMOTHER);
		Messages.OTHER_EXP_SKILLAPI_GAINEDFROMOTHER = msg.getString("other.exp-system.skillapi.gained-from-other", Messages.OTHER_EXP_SKILLAPI_GAINEDFROMOTHER);
		
		Messages.OTHER_FIXED_DEFAULTJOIN = msg.getString("other.fixed-parties.default-join", Messages.OTHER_FIXED_DEFAULTJOIN);
		
		Messages.OTHER_JOINLEAVE_SERVERJOIN = msg.getString("other.join-leave.server-join", Messages.OTHER_JOINLEAVE_SERVERJOIN);
		Messages.OTHER_JOINLEAVE_SERVERLEAVE = msg.getString("other.join-leave.server-leave", Messages.OTHER_JOINLEAVE_SERVERLEAVE);
		
		
		// Help messages
		Messages.HELP_HEADER = msg.getString("help.header", Messages.HELP_HEADER);
		Messages.HELP_FOOTER = msg.getString("help.footer", Messages.HELP_FOOTER);
		Messages.HELP_CONSOLEHELP = msg.getStringList("help.console-help");
		
		Messages.HELP_MAINCMD_HELP = msg.getString("help.main-commands.help", Messages.HELP_MAINCMD_HELP);
		Messages.HELP_MAINCMD_ACCEPT = msg.getString("help.main-commands.accept", Messages.HELP_MAINCMD_ACCEPT);
		Messages.HELP_MAINCMD_CHAT = msg.getString("help.main-commands.chat", Messages.HELP_MAINCMD_CHAT);
		Messages.HELP_MAINCMD_CREATE = msg.getString("help.main-commands.create", Messages.HELP_MAINCMD_CREATE);
		Messages.HELP_MAINCMD_CREATE_FIXED = msg.getString("help.main-commands.create-fixed", Messages.HELP_MAINCMD_CREATE_FIXED);
		Messages.HELP_MAINCMD_DELETE = msg.getString("help.main-commands.delete", Messages.HELP_MAINCMD_DELETE);
		Messages.HELP_MAINCMD_DENY = msg.getString("help.main-commands.deny", Messages.HELP_MAINCMD_DENY);
		Messages.HELP_MAINCMD_IGNORE = msg.getString("help.main-commands.ignore", Messages.HELP_MAINCMD_IGNORE);
		Messages.HELP_MAINCMD_INFO = msg.getString("help.main-commands.info", Messages.HELP_MAINCMD_INFO);
		Messages.HELP_MAINCMD_INVITE = msg.getString("help.main-commands.invite", Messages.HELP_MAINCMD_INVITE);
		Messages.HELP_MAINCMD_KICK = msg.getString("help.main-commands.kick", Messages.HELP_MAINCMD_KICK);
		Messages.HELP_MAINCMD_LEAVE = msg.getString("help.main-commands.leave", Messages.HELP_MAINCMD_LEAVE);
		Messages.HELP_MAINCMD_MIGRATE = msg.getString("help.main-commands.migrate", Messages.HELP_MAINCMD_MIGRATE);
		Messages.HELP_MAINCMD_P = msg.getString("help.main-commands.p", Messages.HELP_MAINCMD_P);
		Messages.HELP_MAINCMD_RANK = msg.getString("help.main-commands.rank", Messages.HELP_MAINCMD_RANK);
		Messages.HELP_MAINCMD_RELOAD = msg.getString("help.main-commands.reload", Messages.HELP_MAINCMD_RELOAD);
		Messages.HELP_MAINCMD_RENAME = msg.getString("help.main-commands.rename", Messages.HELP_MAINCMD_RENAME);
		Messages.HELP_MAINCMD_RENAME_OTHERS = msg.getString("help.main-commands.rename-others", Messages.HELP_MAINCMD_RENAME_OTHERS);
		Messages.HELP_MAINCMD_SPY = msg.getString("help.main-commands.spy", Messages.HELP_MAINCMD_SPY);
		
		Messages.HELP_ADDCMD_CLAIM = msg.getString("help.additional-commands.claim", Messages.HELP_ADDCMD_CLAIM);
		Messages.HELP_ADDCMD_COLOR = msg.getString("help.additional-commands.color", Messages.HELP_ADDCMD_COLOR);
		Messages.HELP_ADDCMD_DESC = msg.getString("help.additional-commands.desc", Messages.HELP_ADDCMD_DESC);
		Messages.HELP_ADDCMD_HOME = msg.getString("help.additional-commands.home", Messages.HELP_ADDCMD_HOME);
		Messages.HELP_ADDCMD_HOME_OTHERS = msg.getString("help.additional-commands.home-others", Messages.HELP_ADDCMD_HOME_OTHERS);
		Messages.HELP_ADDCMD_JOIN = msg.getString("help.additional-commands.join", Messages.HELP_ADDCMD_JOIN);
		Messages.HELP_ADDCMD_LIST = msg.getString("help.additional-commands.list", Messages.HELP_ADDCMD_LIST);
		Messages.HELP_ADDCMD_MOTD = msg.getString("help.additional-commands.motd", Messages.HELP_ADDCMD_MOTD);
		Messages.HELP_ADDCMD_PASSWORD = msg.getString("help.additional-commands.password", Messages.HELP_ADDCMD_PASSWORD);
		Messages.HELP_ADDCMD_PREFIX = msg.getString("help.additional-commands.prefix", Messages.HELP_ADDCMD_PREFIX);
		Messages.HELP_ADDCMD_SETHOME = msg.getString("help.additional-commands.sethome", Messages.HELP_ADDCMD_SETHOME);
		Messages.HELP_ADDCMD_SUFFIX = msg.getString("help.additional-commands.suffix", Messages.HELP_ADDCMD_SUFFIX);
		Messages.HELP_ADDCMD_TELEPORT = msg.getString("help.additional-commands.teleport", Messages.HELP_ADDCMD_TELEPORT);
	}
}
