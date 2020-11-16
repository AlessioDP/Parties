package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;

import java.util.HashMap;
import java.util.List;


public abstract class ConfigMain extends ConfigurationFile {
	// Parties settings
	@ConfigOption(path = "parties.updates.check")
	public static boolean		PARTIES_UPDATES_CHECK;
	@ConfigOption(path = "parties.updates.warn")
	public static boolean		PARTIES_UPDATES_WARN;
	
	@ConfigOption(path = "parties.logging.debug")
	public static boolean		PARTIES_LOGGING_DEBUG;
	@ConfigOption(path = "parties.logging.party-chat")
	public static boolean		PARTIES_LOGGING_PARTY_CHAT;
	@ConfigOption(path = "parties.logging.save-file.enable")
	public static boolean		PARTIES_LOGGING_SAVE_ENABLE;
	@ConfigOption(path = "parties.logging.save-file.format")
	public static String		PARTIES_LOGGING_SAVE_FORMAT;
	@ConfigOption(path = "parties.logging.save-file.file")
	public static String		PARTIES_LOGGING_SAVE_FILE;
	
	@ConfigOption(path = "parties.debug-command")
	public static boolean		PARTIES_DEBUG_COMMAND;
	
	@ConfigOption(path = "parties.bungeecord.config-sync")
	public static boolean		PARTIES_BUNGEECORD_CONFIG_SYNC;
	
	
	// Storage settings
	@ConfigOption(path = "storage.database-storage-type")
	public static String		STORAGE_TYPE_DATABASE;
	@ConfigOption(path = "storage.storage-settings.none.disband-on-leader-left")
	public static boolean		STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT;
	@ConfigOption(path = "storage.storage-settings.none.delay-delete-party")
	public static int			STORAGE_SETTINGS_NONE_DELAYDELETEPARTY;
	@ConfigOption(path = "storage.storage-settings.yaml.database-file")
	public static String		STORAGE_SETTINGS_YAML_DBFILE;
	@ConfigOption(path = "storage.storage-settings.general-sql-settings.prefix")
	public static String		STORAGE_SETTINGS_GENERAL_SQL_PREFIX;
	@ConfigOption(path = "storage.storage-settings.sqlite.database-file")
	public static String		STORAGE_SETTINGS_SQLITE_DBFILE;
	@ConfigOption(path = "storage.storage-settings.h2.database-file")
	public static String		STORAGE_SETTINGS_H2_DBFILE;
	@ConfigOption(path = "storage.storage-settings.mysql.address")
	public static String		STORAGE_SETTINGS_MYSQL_ADDRESS;
	@ConfigOption(path = "storage.storage-settings.mysql.port")
	public static String		STORAGE_SETTINGS_MYSQL_PORT;
	@ConfigOption(path = "storage.storage-settings.mysql.database")
	public static String		STORAGE_SETTINGS_MYSQL_DATABASE;
	@ConfigOption(path = "storage.storage-settings.mysql.username")
	public static String		STORAGE_SETTINGS_MYSQL_USERNAME;
	@ConfigOption(path = "storage.storage-settings.mysql.password")
	public static String		STORAGE_SETTINGS_MYSQL_PASSWORD;
	@ConfigOption(path = "storage.storage-settings.mysql.pool-size")
	public static int			STORAGE_SETTINGS_MYSQL_POOLSIZE;
	@ConfigOption(path = "storage.storage-settings.mysql.connection-lifetime")
	public static int			STORAGE_SETTINGS_MYSQL_CONNLIFETIME;
	@ConfigOption(path = "storage.storage-settings.mysql.use-ssl")
	public static boolean		STORAGE_SETTINGS_MYSQL_USESSL;
	@ConfigOption(path = "storage.storage-settings.mysql.charset")
	public static String		STORAGE_SETTINGS_MYSQL_CHARSET;
	
	
	// Additional settings
	@ConfigOption(path = "additional.auto-command.enable")
	public static boolean		ADDITIONAL_AUTOCMD_ENABLE;
	@ConfigOption(path = "additional.auto-command.regex-whitelist")
	public static String		ADDITIONAL_AUTOCMD_REGEXWHITELIST;
	
	@ConfigOption(path = "additional.exp-system.enable")
	public static boolean		ADDITIONAL_EXP_ENABLE;
	@ConfigOption(path = "additional.exp-system.levels.enable")
	public static boolean		ADDITIONAL_EXP_LEVELS_ENABLE;
	@ConfigOption(path = "additional.exp-system.levels.mode")
	public static String		ADDITIONAL_EXP_LEVELS_MODE;
	@ConfigOption(path = "additional.exp-system.levels.progressive.start-level-experience")
	public static double		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START;
	@ConfigOption(path = "additional.exp-system.levels.progressive.level-experience")
	public static String		ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP;
	@ConfigOption(path = "additional.exp-system.levels.fixed.repeat-last-one")
	public static boolean		ADDITIONAL_EXP_LEVELS_FIXED_REPEAT;
	@ConfigOption(path = "additional.exp-system.levels.fixed.list")
	public static List<Double>	ADDITIONAL_EXP_LEVELS_FIXED_LIST;
	
	@ConfigOption(path = "additional.follow.enable")
	public static boolean		ADDITIONAL_FOLLOW_ENABLE;
	@ConfigOption(path = "additional.follow.toggle-command")
	public static boolean		ADDITIONAL_FOLLOW_TOGGLECMD;
	
	@ConfigOption(path = "additional.mute.enable")
	public static boolean		ADDITIONAL_MUTE_ENABLE;
	@ConfigOption(path = "additional.mute.block.invite")
	public static boolean		ADDITIONAL_MUTE_BLOCK_INVITE;
	
	public static HashMap<String, String> ADDITIONAL_PLACEHOLDER_CUSTOMS;
	
	
	// Commands settings
	@ConfigOption(path = "commands.tab-support")
	public static boolean		COMMANDS_TABSUPPORT;
	@ConfigOption(path = "commands.help-pages.commands-per-page")
	public static int			COMMANDS_HELP_PERPAGE;
	
	@ConfigOption(path = "commands.main-commands.help")
	public static String		COMMANDS_CMD_HELP;
	@ConfigOption(path = "commands.main-commands.party")
	public static String		COMMANDS_CMD_PARTY;
	@ConfigOption(path = "commands.main-commands.p")
	public static String		COMMANDS_CMD_P;
	@ConfigOption(path = "commands.main-commands.accept")
	public static String		COMMANDS_CMD_ACCEPT;
	@ConfigOption(path = "commands.main-commands.ask")
	public static String		COMMANDS_CMD_ASK;
	@ConfigOption(path = "commands.main-commands.chat")
	public static String		COMMANDS_CMD_CHAT;
	@ConfigOption(path = "commands.main-commands.color")
	public static String		COMMANDS_CMD_COLOR;
	@ConfigOption(path = "commands.main-commands.create")
	public static String		COMMANDS_CMD_CREATE;
	@ConfigOption(path = "commands.main-commands.debug")
	public static String		COMMANDS_CMD_DEBUG;
	@ConfigOption(path = "commands.main-commands.delete")
	public static String		COMMANDS_CMD_DELETE;
	@ConfigOption(path = "commands.main-commands.deny")
	public static String		COMMANDS_CMD_DENY;
	@ConfigOption(path = "commands.main-commands.desc")
	public static String		COMMANDS_CMD_DESC;
	@ConfigOption(path = "commands.main-commands.follow")
	public static String		COMMANDS_CMD_FOLLOW;
	@ConfigOption(path = "commands.main-commands.ignore")
	public static String		COMMANDS_CMD_IGNORE;
	@ConfigOption(path = "commands.main-commands.info")
	public static String		COMMANDS_CMD_INFO;
	@ConfigOption(path = "commands.main-commands.invite")
	public static String		COMMANDS_CMD_INVITE;
	@ConfigOption(path = "commands.main-commands.join")
	public static String		COMMANDS_CMD_JOIN;
	@ConfigOption(path = "commands.main-commands.kick")
	public static String		COMMANDS_CMD_KICK;
	@ConfigOption(path = "commands.main-commands.leave")
	public static String		COMMANDS_CMD_LEAVE;
	@ConfigOption(path = "commands.main-commands.list")
	public static String		COMMANDS_CMD_LIST;
	@ConfigOption(path = "commands.main-commands.motd")
	public static String		COMMANDS_CMD_MOTD;
	@ConfigOption(path = "commands.main-commands.mute")
	public static String		COMMANDS_CMD_MUTE;
	@ConfigOption(path = "commands.main-commands.password")
	public static String		COMMANDS_CMD_PASSWORD;
	@ConfigOption(path = "commands.main-commands.rank")
	public static String		COMMANDS_CMD_RANK;
	@ConfigOption(path = "commands.main-commands.reload")
	public static String		COMMANDS_CMD_RELOAD;
	@ConfigOption(path = "commands.main-commands.rename")
	public static String		COMMANDS_CMD_RENAME;
	@ConfigOption(path = "commands.main-commands.spy")
	public static String		COMMANDS_CMD_SPY;
	@ConfigOption(path = "commands.main-commands.tag")
	public static String		COMMANDS_CMD_TAG;
	@ConfigOption(path = "commands.main-commands.teleport")
	public static String		COMMANDS_CMD_TELEPORT;
	@ConfigOption(path = "commands.main-commands.version")
	public static String		COMMANDS_CMD_VERSION;
	
	@ConfigOption(path = "commands.sub-commands.config")
	public static String		COMMANDS_SUB_CONFIG;
	@ConfigOption(path = "commands.sub-commands.exp")
	public static String		COMMANDS_SUB_EXP;
	@ConfigOption(path = "commands.sub-commands.party")
	public static String		COMMANDS_SUB_PARTY;
	@ConfigOption(path = "commands.sub-commands.player")
	public static String		COMMANDS_SUB_PLAYER;
	@ConfigOption(path = "commands.sub-commands.word-on")
	public static String		COMMANDS_SUB_ON;
	@ConfigOption(path = "commands.sub-commands.word-off")
	public static String		COMMANDS_SUB_OFF;
	@ConfigOption(path = "commands.sub-commands.silent")
	public static String		COMMANDS_SUB_SILENT;
	@ConfigOption(path = "commands.sub-commands.fixed")
	public static String		COMMANDS_SUB_FIXED;
	@ConfigOption(path = "commands.sub-commands.remove")
	public static String		COMMANDS_SUB_REMOVE;
	
	@ConfigOption(path = "commands.order")
	public static List<String>	COMMANDS_ORDER;
	
	
	protected ConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		loadDefaultConfigOptions();
		
		ADDITIONAL_PLACEHOLDER_CUSTOMS = new HashMap<>();
		ADDITIONAL_PLACEHOLDER_CUSTOMS.put("example1", "[%color_code%%party%] ");
		ADDITIONAL_PLACEHOLDER_CUSTOMS.put("example2", "[%rank_chat% %party%] ");
	}
	
	@Override
	public void loadConfiguration() {
		loadConfigOptions();
		ConfigurationSection csPlaceholders = configuration.getConfigurationSection("additional.placeholders.customs");
		if (csPlaceholders != null) {
			ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS = new HashMap<>();
			for (String key : csPlaceholders.getKeys(false)) {
				ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS.put(key, csPlaceholders.getString(key, ""));
			}
		} else {
			// Give error: no ranks node found
			plugin.getLoggerManager().printError(PartiesConstants.DEBUG_CONFIG_FAILED_PLACEHOLDERS_NOTFOUND);
		}
	}
}
