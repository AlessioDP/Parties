package com.alessiodp.parties.common.configuration.data;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.addons.external.simpleyaml.configuration.MemorySection;
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
	@ConfigOption(path = "parties.automatic-upgrade-configs")
	public static boolean		PARTIES_AUTOMATIC_UPGRADE_CONFIGS;
	
	@ConfigOption(path = "parties.bungeecord.packets.load-players")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_LOAD_PLAYERS;
	@ConfigOption(path = "parties.bungeecord.packets.load-parties")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_LOAD_PARTIES;
	@ConfigOption(path = "parties.bungeecord.packets.player-sync")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_PLAYER_SYNC;
	@ConfigOption(path = "parties.bungeecord.packets.party-sync")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_PARTY_SYNC;
	@ConfigOption(path = "parties.bungeecord.packets.config-sync")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_CONFIG_SYNC;
	@ConfigOption(path = "parties.bungeecord.packets.debug-bungeecord")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_DEBUG_BUNGEECORD;
	@ConfigOption(path = "parties.bungeecord.packets.chat")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_CHAT;
	@ConfigOption(path = "parties.bungeecord.packets.broadcast")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_BROADCAST;
	@ConfigOption(path = "parties.bungeecord.packets.sounds")
	public static boolean		PARTIES_BUNGEECORD_PACKETS_SOUNDS;
	
	
	// Storage settings
	@ConfigOption(path = "storage.database-storage-type")
	public static String		STORAGE_TYPE_DATABASE;
	@ConfigOption(path = "storage.storage-settings.yaml.database-file")
	public static String		STORAGE_SETTINGS_YAML_DBFILE;
	@ConfigOption(path = "storage.storage-settings.general-sql-settings.prefix")
	public static String		STORAGE_SETTINGS_GENERAL_SQL_PREFIX;
	@ConfigOption(path = "storage.storage-settings.sqlite.database-file")
	public static String		STORAGE_SETTINGS_SQLITE_DBFILE;
	@ConfigOption(path = "storage.storage-settings.h2.database-file")
	public static String		STORAGE_SETTINGS_H2_DBFILE;
	@ConfigOption(path = "storage.storage-settings.remote-sql.address")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_ADDRESS;
	@ConfigOption(path = "storage.storage-settings.remote-sql.port")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_PORT;
	@ConfigOption(path = "storage.storage-settings.remote-sql.database")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_DATABASE;
	@ConfigOption(path = "storage.storage-settings.remote-sql.username")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_USERNAME;
	@ConfigOption(path = "storage.storage-settings.remote-sql.password")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_PASSWORD;
	@ConfigOption(path = "storage.storage-settings.remote-sql.pool-size")
	public static int			STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE;
	@ConfigOption(path = "storage.storage-settings.remote-sql.connection-lifetime")
	public static int			STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME;
	@ConfigOption(path = "storage.storage-settings.remote-sql.use-ssl")
	public static boolean		STORAGE_SETTINGS_REMOTE_SQL_USESSL;
	@ConfigOption(path = "storage.storage-settings.remote-sql.charset")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_CHARSET;
	@ConfigOption(path = "storage.storage-settings.remote-sql.additional-parameters")
	public static String		STORAGE_SETTINGS_REMOTE_SQL_ADDITIONAL_PARAMETERS;
	
	
	// Additional settings
	@ConfigOption(path = "additional.auto-command.enable")
	public static boolean		ADDITIONAL_AUTOCMD_ENABLE;
	@ConfigOption(path = "additional.auto-command.regex-whitelist")
	public static String		ADDITIONAL_AUTOCMD_REGEXWHITELIST;
	@ConfigOption(path = "additional.auto-command.delay")
	public static long			ADDITIONAL_AUTOCMD_DELAY;
	
	@ConfigOption(path = "additional.exp-system.enable")
	public static boolean		ADDITIONAL_EXP_ENABLE;
	@ConfigOption(path = "additional.exp-system.mode")
	public static String		ADDITIONAL_EXP_MODE;
	@ConfigOption(path = "additional.exp-system.earn-exp-from-mobs")
	public static boolean		ADDITIONAL_EXP_EARN_FROM_MOBS;
	@ConfigOption(path = "additional.exp-system.progressive.start-level-experience")
	public static double		ADDITIONAL_EXP_PROGRESSIVE_START;
	@ConfigOption(path = "additional.exp-system.progressive.level-experience")
	public static String		ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP;
	@ConfigOption(path = "additional.exp-system.progressive.safe-calculation")
	public static boolean		ADDITIONAL_EXP_PROGRESSIVE_SAFE_CALCULATION;
	@ConfigOption(path = "additional.exp-system.fixed.repeat-last-one")
	public static boolean		ADDITIONAL_EXP_FIXED_REPEAT;
	@ConfigOption(path = "additional.exp-system.fixed.list")
	public static List<Double>	ADDITIONAL_EXP_FIXED_LIST;
	
	@ConfigOption(path = "additional.follow.enable")
	public static boolean		ADDITIONAL_FOLLOW_ENABLE;
	@ConfigOption(path = "additional.follow.toggle-command")
	public static boolean		ADDITIONAL_FOLLOW_TOGGLECMD;
	@ConfigOption(path = "additional.follow.delay")
	public static long			ADDITIONAL_FOLLOW_DELAY;
	@ConfigOption(path = "additional.follow.perform-commands.enable")
	public static boolean		ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE;
	@ConfigOption(path = "additional.follow.perform-commands.delay")
	public static int			ADDITIONAL_FOLLOW_PERFORMCMD_DELAY;
	@ConfigOption(path = "additional.follow.perform-commands.commands")
	public static List<String>	ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS;
	
	@ConfigOption(path = "additional.moderation.enable")
	public static boolean		ADDITIONAL_MODERATION_ENABLE;
	@ConfigOption(path = "additional.moderation.prevent-chat-muted")
	public static boolean		ADDITIONAL_MODERATION_PREVENTCHAT;
	@ConfigOption(path = "additional.moderation.auto-kick-banned")
	public static boolean		ADDITIONAL_MODERATION_AUTOKICK;
	
	@ConfigOption(path = "additional.mute.enable")
	public static boolean		ADDITIONAL_MUTE_ENABLE;
	@ConfigOption(path = "additional.mute.block.invite")
	public static boolean		ADDITIONAL_MUTE_BLOCK_INVITE;
	
	@ConfigOption(path = "additional.placeholders.customs")
	public static MemorySection RAW_ADDITIONAL_PLACEHOLDER_CUSTOMS;
	public static HashMap<String, String> ADDITIONAL_PLACEHOLDER_CUSTOMS;
	
	
	// Commands settings
	@ConfigOption(path = "commands.tab-support")
	public static boolean		COMMANDS_TABSUPPORT;
	@ConfigOption(path = "commands.help-pages.commands-per-page")
	public static int			COMMANDS_HELP_PERPAGE;
	
	@ConfigOption(path = "commands.main-commands.party.command")
	public static String		COMMANDS_MAIN_PARTY_COMMAND;
	@ConfigOption(path = "commands.main-commands.party.aliases")
	public static List<String>		COMMANDS_MAIN_PARTY_ALIASES;
	@ConfigOption(path = "commands.main-commands.p.command")
	public static String			COMMANDS_MAIN_P_COMMAND;
	@ConfigOption(path = "commands.main-commands.p.aliases")
	public static List<String>		COMMANDS_MAIN_P_ALIASES;
	
	@ConfigOption(path = "commands.sub-commands.help")
	public static String		COMMANDS_SUB_HELP;
	@ConfigOption(path = "commands.sub-commands.accept")
	public static String		COMMANDS_SUB_ACCEPT;
	@ConfigOption(path = "commands.sub-commands.ask")
	public static String		COMMANDS_SUB_ASK;
	@ConfigOption(path = "commands.sub-commands.chat")
	public static String		COMMANDS_SUB_CHAT;
	@ConfigOption(path = "commands.sub-commands.close")
	public static String		COMMANDS_SUB_CLOSE;
	@ConfigOption(path = "commands.sub-commands.color")
	public static String		COMMANDS_SUB_COLOR;
	@ConfigOption(path = "commands.sub-commands.create")
	public static String		COMMANDS_SUB_CREATE;
	@ConfigOption(path = "commands.sub-commands.createfixed")
	public static String		COMMANDS_SUB_CREATEFIXED;
	@ConfigOption(path = "commands.sub-commands.debug")
	public static String		COMMANDS_SUB_DEBUG;
	@ConfigOption(path = "commands.sub-commands.delete")
	public static String		COMMANDS_SUB_DELETE;
	@ConfigOption(path = "commands.sub-commands.deny")
	public static String		COMMANDS_SUB_DENY;
	@ConfigOption(path = "commands.sub-commands.desc")
	public static String		COMMANDS_SUB_DESC;
	@ConfigOption(path = "commands.sub-commands.follow")
	public static String		COMMANDS_SUB_FOLLOW;
	@ConfigOption(path = "commands.sub-commands.home")
	public static String		COMMANDS_SUB_HOME;
	@ConfigOption(path = "commands.sub-commands.ignore")
	public static String		COMMANDS_SUB_IGNORE;
	@ConfigOption(path = "commands.sub-commands.info")
	public static String		COMMANDS_SUB_INFO;
	@ConfigOption(path = "commands.sub-commands.invite")
	public static String		COMMANDS_SUB_INVITE;
	@ConfigOption(path = "commands.sub-commands.join")
	public static String		COMMANDS_SUB_JOIN;
	@ConfigOption(path = "commands.sub-commands.kick")
	public static String		COMMANDS_SUB_KICK;
	@ConfigOption(path = "commands.sub-commands.leave")
	public static String		COMMANDS_SUB_LEAVE;
	@ConfigOption(path = "commands.sub-commands.list")
	public static String		COMMANDS_SUB_LIST;
	@ConfigOption(path = "commands.sub-commands.motd")
	public static String		COMMANDS_SUB_MOTD;
	@ConfigOption(path = "commands.sub-commands.mute")
	public static String		COMMANDS_SUB_MUTE;
	@ConfigOption(path = "commands.sub-commands.nickname")
	public static String		COMMANDS_SUB_NICKNAME;
	@ConfigOption(path = "commands.sub-commands.open")
	public static String		COMMANDS_SUB_OPEN;
	@ConfigOption(path = "commands.sub-commands.password")
	public static String		COMMANDS_SUB_PASSWORD;
	@ConfigOption(path = "commands.sub-commands.protection")
	public static String		COMMANDS_SUB_PROTECTION;
	@ConfigOption(path = "commands.sub-commands.rank")
	public static String		COMMANDS_SUB_RANK;
	@ConfigOption(path = "commands.sub-commands.reload")
	public static String		COMMANDS_SUB_RELOAD;
	@ConfigOption(path = "commands.sub-commands.rename")
	public static String		COMMANDS_SUB_RENAME;
	@ConfigOption(path = "commands.sub-commands.sethome")
	public static String		COMMANDS_SUB_SETHOME;
	@ConfigOption(path = "commands.sub-commands.spy")
	public static String		COMMANDS_SUB_SPY;
	@ConfigOption(path = "commands.sub-commands.tag")
	public static String		COMMANDS_SUB_TAG;
	@ConfigOption(path = "commands.sub-commands.teleport")
	public static String		COMMANDS_SUB_TELEPORT;
	@ConfigOption(path = "commands.sub-commands.version")
	public static String		COMMANDS_SUB_VERSION;
	
	@ConfigOption(path = "commands.misc-commands.config")
	public static String		COMMANDS_MISC_CONFIG;
	@ConfigOption(path = "commands.misc-commands.exp")
	public static String		COMMANDS_MISC_EXP;
	@ConfigOption(path = "commands.misc-commands.party")
	public static String		COMMANDS_MISC_PARTY;
	@ConfigOption(path = "commands.misc-commands.player")
	public static String		COMMANDS_MISC_PLAYER;
	@ConfigOption(path = "commands.misc-commands.word-on")
	public static String		COMMANDS_MISC_ON;
	@ConfigOption(path = "commands.misc-commands.word-off")
	public static String		COMMANDS_MISC_OFF;
	@ConfigOption(path = "commands.misc-commands.silent")
	public static String		COMMANDS_MISC_SILENT;
	@ConfigOption(path = "commands.misc-commands.remove")
	public static String		COMMANDS_MISC_REMOVE;
	
	@ConfigOption(path = "commands.order")
	public static List<String>	COMMANDS_ORDER;
	
	
	protected ConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadCustomDefaultOptions() {
		ADDITIONAL_PLACEHOLDER_CUSTOMS = new HashMap<>();
		ADDITIONAL_PLACEHOLDER_CUSTOMS.put("example1", "[%color_code%%party%] ");
		ADDITIONAL_PLACEHOLDER_CUSTOMS.put("example2", "[%rank_chat% %party%] ");
	}
	
	@Override
	public void loadCustomFileOptions() {
		ConfigurationSection csPlaceholders = configuration.getConfigurationSection("additional.placeholders.customs");
		if (csPlaceholders != null) {
			ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS = new HashMap<>();
			for (String key : csPlaceholders.getKeys(false)) {
				ConfigMain.ADDITIONAL_PLACEHOLDER_CUSTOMS.put(key, csPlaceholders.getString(key, ""));
			}
		} else {
			// Give error: no custom placeholders node found
			plugin.getLoggerManager().logError(PartiesConstants.DEBUG_CONFIG_FAILED_PLACEHOLDERS_NOTFOUND);
		}
	}
}
