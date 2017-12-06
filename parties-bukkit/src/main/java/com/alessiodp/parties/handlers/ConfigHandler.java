package com.alessiodp.parties.handlers;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.ColorObj;
import com.alessiodp.parties.objects.RankObj;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.PartiesPlaceholder;
import com.alessiodp.parties.utils.enums.StorageType;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.interfaces.Rank;

public class ConfigHandler {
	private Parties plugin;
	private Variables variables;
	private Messages messages;
	
	public ConfigHandler(Parties instance) {
		plugin = instance;
		variables = new Variables();
		messages = new Messages();
		reloadConfig();
		reloadMessages();
	}
	
	@SuppressWarnings("static-access")
	public void reloadConfig() {
		File cfgFile = new File(plugin.getDataFolder(), "config.yml");
		if (!cfgFile.exists()) {
			plugin.saveResource("config.yml", true);
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
		
		if (cfg.getInt("dont-edit-this.config-version") < plugin.getConfigVersion()) {
			// Normal log, LogHandler will give error
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode() + "Configuration file outdated");
		}
		
		variables.downloadupdates = cfg.getBoolean("functions.download-updates", variables.downloadupdates);
		variables.warnupdates = cfg.getBoolean("functions.warn-updates-in-game", variables.warnupdates);
		variables.perpermissionhelp = cfg.getBoolean("functions.per-permission-help", variables.perpermissionhelp);
		variables.commandsperpage = cfg.getInt("functions.commands-per-page", variables.commandsperpage);
		variables.commandtab = cfg.getBoolean("functions.command-tab", variables.commandtab);
		
		variables.fixedparty = cfg.getBoolean("functions.enable-fixed-party", variables.fixedparty);
		variables.default_enable = cfg.getBoolean("functions.default-party.enable", variables.default_enable);
		variables.default_party = cfg.getString("functions.default-party.party", variables.default_party);
		variables.invisibleallies = cfg.getBoolean("functions.see-allies-invisible", variables.invisibleallies);
		variables.bungeecord = cfg.getBoolean("functions.bungeecord", variables.bungeecord);
		variables.joinleavemessages = cfg.getBoolean("functions.join-leave-messages", variables.joinleavemessages);
		
		variables.storage_type_log = cfg.getString("storage.log-storage-type", variables.storage_type_log);
		variables.storage_type_database = cfg.getString("storage.database-storage-type", variables.storage_type_database);
		
		variables.storage_log_format = cfg.getString("storage.log-settings.format", variables.storage_log_format);
		variables.storage_log_chat = cfg.getBoolean("storage.log-settings.chat", variables.storage_log_chat);
		variables.storage_log_printconsole = cfg.getBoolean("storage.log-settings.print-console", variables.storage_log_printconsole);
		variables.storage_log_level = cfg.getInt("storage.log-settings.log-level", variables.storage_log_level);
		
		variables.storage_migrate_forcemysql = cfg.getBoolean("storage.migrate-settings.force-mysql", variables.storage_migrate_forcemysql);
		variables.storage_migrate_onlyconsole = cfg.getBoolean("storage.migrate-settings.migrate-only-console", variables.storage_migrate_onlyconsole);
		variables.storage_migrate_suffix = cfg.getString("storage.migrate-settings.migration-suffix", variables.storage_migrate_suffix);
		
		variables.storage_settings_yaml_name_database = cfg.getString("storage.storage-settings.yaml.database-name", variables.storage_settings_yaml_name_database);
		variables.storage_settings_yaml_name_log = cfg.getString("storage.storage-settings.yaml.log-name", variables.storage_settings_yaml_name_log);
		
		variables.storage_settings_mysql_url = cfg.getString("storage.storage-settings.mysql.url", variables.storage_settings_mysql_url);
		variables.storage_settings_mysql_username = cfg.getString("storage.storage-settings.mysql.username", variables.storage_settings_mysql_username);
		variables.storage_settings_mysql_password = cfg.getString("storage.storage-settings.mysql.password", variables.storage_settings_mysql_password);
		variables.storage_settings_mysql_varcharsize = cfg.getInt("storage.storage-settings.mysql.varchar-size", variables.storage_settings_mysql_varcharsize);
		variables.storage_settings_mysql_poolsize = cfg.getInt("storage.storage-settings.mysql.pool-size", variables.storage_settings_mysql_poolsize);
		variables.storage_settings_mysql_connlifetime = cfg.getInt("storage.storage-settings.mysql.conn-lifetime", variables.storage_settings_mysql_connlifetime);
		variables.storage_settings_mysql_conntimeout = cfg.getInt("storage.storage-settings.mysql.conn-timeout", variables.storage_settings_mysql_conntimeout);
		
		variables.storage_settings_mysql_tables_parties = cfg.getString("storage.storage-settings.mysql.tables.parties", variables.storage_settings_mysql_tables_parties);
		variables.storage_settings_mysql_tables_players = cfg.getString("storage.storage-settings.mysql.tables.players", variables.storage_settings_mysql_tables_players);
		variables.storage_settings_mysql_tables_spies = cfg.getString("storage.storage-settings.mysql.tables.spies", variables.storage_settings_mysql_tables_spies);
		variables.storage_settings_mysql_tables_log = cfg.getString("storage.storage-settings.mysql.tables.log", variables.storage_settings_mysql_tables_log);
		
		variables.storage_settings_none_disbandonleaderleft = cfg.getBoolean("storage.storage-settings.none.disband-on-leader-left", variables.storage_settings_none_disbandonleaderleft);
		variables.storage_settings_none_delaydeleteparty = cfg.getInt("storage.storage-settings.none.delay-delete-party", variables.storage_settings_none_delaydeleteparty);
		
		variables.party_maxmembers = cfg.getInt("party.max-members", variables.party_maxmembers);
		variables.party_allowedchars = cfg.getString("party.allowed-chars", variables.party_allowedchars);
		variables.party_minlengthname = cfg.getInt("party.min-length-name", variables.party_minlengthname);
		variables.party_maxlengthname = cfg.getInt("party.max-length-name", variables.party_maxlengthname);
		variables.party_renamecooldown = cfg.getInt("party.rename-cooldown", variables.party_renamecooldown);
		variables.party_placeholder = cfg.getString("party.party-placeholder", variables.party_placeholder);
		
		variables.friendlyfire_enable = cfg.getBoolean("party.prevent-friendly-fire.enable", variables.friendlyfire_enable);
		variables.friendlyfire_warn = cfg.getBoolean("party.prevent-friendly-fire.warn-players-on-fight", variables.friendlyfire_warn);
		variables.friendlyfire_listworlds = cfg.getStringList("party.prevent-friendly-fire.list-worlds");
		
		variables.invite_timeout = cfg.getInt("party.invite.timeout", variables.invite_timeout);
		variables.invite_revoke = cfg.getBoolean("party.invite.revoke", variables.invite_revoke);
		variables.invite_noperm = cfg.getBoolean("party.invite.prevent-invite-player-no-permission-join", variables.invite_noperm);
		
		variables.home_cooldown = cfg.getInt("party.home.cooldown", variables.home_cooldown);
		variables.home_cancelmove = cfg.getBoolean("party.home.cancel-if-move", variables.home_cancelmove);
		variables.home_distance = cfg.getInt("party.home.distance-cancel", variables.home_distance);
		
		variables.teleport_enable = cfg.getBoolean("party.teleport.enable", variables.teleport_enable);
		variables.teleport_delay = cfg.getInt("party.teleport.delay", variables.teleport_delay);
		
		variables.password_enable = cfg.getBoolean("party.join-password.enable", variables.password_enable);
		variables.password_bypassleave = cfg.getBoolean("party.join-password.bypass-leave", variables.password_bypassleave);
		variables.password_allowedchars = cfg.getString("party.join-password.allowed-chars", variables.password_allowedchars);
		variables.password_hash = cfg.getString("party.join-password.hash", variables.password_hash);
		variables.password_encode = cfg.getString("party.join-password.encode", variables.password_encode);
		variables.password_lengthmin = cfg.getInt("party.join-password.length-min", variables.password_lengthmin);
		variables.password_lengthmax = cfg.getInt("party.join-password.length-max", variables.password_lengthmax);;
		
		variables.desc_min = cfg.getInt("description.length-min", variables.desc_min);
		variables.desc_max = cfg.getInt("description.length-max", variables.desc_max);
		variables.desc_allowedchars = cfg.getString("description.allowed-chars", variables.desc_allowedchars);
		variables.desc_censored = cfg.getStringList("description.censored-words");
			
		variables.motd_min = cfg.getInt("motd.length-min", variables.motd_min);
		variables.motd_max = cfg.getInt("motd.length-max", variables.motd_max);
		variables.motd_delay = cfg.getInt("motd.delay", variables.motd_delay);
		variables.motd_allowedchars = cfg.getString("motd.allowed-chars", variables.motd_allowedchars);
		variables.motd_newline = cfg.getString("motd.new-line-code", variables.motd_newline);
		variables.motd_censored = cfg.getStringList("motd.censored-words");
		
		variables.kill_enable = cfg.getBoolean("kills.save-kills", variables.kill_enable);
		variables.kill_save_mobsneutral = cfg.getBoolean("kills.which-save.neutral-mobs", variables.kill_save_mobsneutral);
		variables.kill_save_mobshostile = cfg.getBoolean("kills.which-save.hostile-mobs", variables.kill_save_mobshostile);
		variables.kill_save_players = cfg.getBoolean("kills.which-save.players", variables.kill_save_players);
		
		variables.tablist_enable = cfg.getBoolean("tablist.enable", variables.tablist_enable);
		variables.tablist_inparty = cfg.getString("tablist.in-party", variables.tablist_inparty);
		variables.tablist_outparty = cfg.getString("tablist.out-party", variables.tablist_outparty);
		variables.tablist_header_inparty = cfg.getString("tablist.header.in-party", variables.tablist_header_inparty);
		variables.tablist_header_outparty = cfg.getString("tablist.header.out-party", variables.tablist_header_outparty);
		variables.tablist_footer_inparty = cfg.getString("tablist.footer.in-party", variables.tablist_footer_inparty);
		variables.tablist_footer_outparty = cfg.getString("tablist.footer.out-party", variables.tablist_footer_outparty);
		
		variables.tag_enable = cfg.getBoolean("tag.tag-system", variables.tag_enable);
		variables.tag_system = cfg.getBoolean("tag.which-tag-system", variables.tag_system);
		variables.tag_base_formatprefix = cfg.getString("tag.base-tag.format-prefix", variables.tag_base_formatprefix);
		variables.tag_base_formatsuffix = cfg.getString("tag.base-tag.format-suffix", variables.tag_base_formatsuffix);
		variables.tag_custom_prefix = cfg.getBoolean("tag.custom-tag.prefix", variables.tag_custom_prefix);
		variables.tag_custom_formatprefix = cfg.getString("tag.custom-tag.format-prefix", variables.tag_custom_formatprefix);
		variables.tag_custom_suffix = cfg.getBoolean("tag.custom-tag.suffix", variables.tag_custom_suffix);
		variables.tag_custom_formatsuffix = cfg.getString("tag.custom-tag.format-suffix", variables.tag_custom_formatsuffix);
		variables.tag_custom_allowedchars = cfg.getString("tag.custom-tag.allowed-chars", variables.tag_custom_allowedchars);
		variables.tag_custom_maxlength = cfg.getInt("tag.custom-tag.max-length", variables.tag_custom_maxlength);
		variables.tag_custom_minlength = cfg.getInt("tag.custom-tag.min-length", variables.tag_custom_minlength);
		variables.tag_custom_censor = cfg.getStringList("tag.custom-tag.censored-words");

		variables.chat_chatformat = cfg.getString("chat.chat-format", variables.chat_chatformat);
		variables.chat_allowcolors = cfg.getBoolean("chat.allow-colors", variables.chat_allowcolors);
		variables.chat_chatcooldown = cfg.getInt("chat.chat-cooldown", variables.chat_chatcooldown);
		variables.chat_spychatformat = cfg.getString("chat.spy-chat-format", variables.chat_spychatformat);
		variables.chat_partybroadcastformat = cfg.getString("chat.party-broadcast-format", variables.chat_partybroadcastformat);
		variables.chat_formatgroup = cfg.getString("chat.format-group", variables.chat_formatgroup);
		
		variables.list_enable = cfg.getBoolean("list.enable", variables.list_enable);
		variables.list_orderedby = cfg.getString("list.ordered-by", variables.list_orderedby);
		variables.list_filter = cfg.getInt("list.filter-min", variables.list_filter);
		variables.list_maxpages = cfg.getInt("list.parties-per-page", variables.list_maxpages);
		variables.list_limitparties = cfg.getInt("list.limit-parties", variables.list_limitparties);
		variables.list_hiddenparty = cfg.getStringList("list.hidden-parties");
		
		variables.follow_enable = cfg.getBoolean("follow-party.enable", variables.follow_enable);
		variables.follow_type = cfg.getInt("follow-party.type-of-teleport", variables.follow_type);
		variables.follow_neededrank = cfg.getInt("follow-party.needed-rank", variables.follow_neededrank);
		variables.follow_minimumrank = cfg.getInt("follow-party.minimum-rank-to-follow", variables.follow_minimumrank);
		variables.follow_timeoutportal = cfg.getInt("follow-party.timeout-portal", variables.follow_timeoutportal);
		variables.follow_listworlds = cfg.getStringList("follow-party.list-worlds");
		
		variables.autocommand_enable = cfg.getBoolean("auto-command.enable", variables.autocommand_enable);
		variables.autocommand_blacklist = cfg.getStringList("auto-command.blacklist");
		variables.autocommand_whitelist= cfg.getStringList("auto-command.whitelist");
		
		variables.censor_enable = cfg.getBoolean("censor.enable", variables.censor_enable);
		variables.censor_casesensitive = cfg.getBoolean("censor.case-sensitive", variables.censor_casesensitive);
		variables.censor_contains = cfg.getStringList("censor.contains");
		variables.censor_startswith = cfg.getStringList("censor.starts-with");
		variables.censor_endswith = cfg.getStringList("censor.ends-with");
		
		variables.exp_enable = cfg.getBoolean("exp.enable", variables.exp_enable);
		variables.exp_divide = cfg.getBoolean("exp.divide-between-players", variables.exp_divide);
		variables.exp_sharemorethan = cfg.getInt("exp.share-if-more-than", variables.exp_sharemorethan);
		variables.exp_formula = cfg.getString("exp.formula", variables.exp_formula);
		variables.exp_range = cfg.getInt("exp.range", variables.exp_range);
		variables.exp_skillapi_enable = cfg.getBoolean("exp.skillapi.enable", variables.exp_skillapi_enable);
		variables.exp_skillapi_source = cfg.getString("exp.skillapi.exp-source", variables.exp_skillapi_source);
		/*variables.exp_mythicmobs_enable = cfg.getBoolean("exp.mythicmobs.enable", variables.exp_mythicmobs_enable);
		variables.exp_mythicmobs_normalexp = cfg.getBoolean("exp.mythicmobs.normal-exp", variables.exp_mythicmobs_normalexp);
		variables.exp_mythicmobs_skillapiexp = cfg.getBoolean("exp.mythicmobs.skillapi-exp", variables.exp_mythicmobs_skillapiexp);*/
		
		variables.placeholders_colorname = cfg.getString("placeholders.color-name", variables.placeholders_colorname);
		variables.placeholders_colorcode = cfg.getString("placeholders.color-code", variables.placeholders_colorcode);
		variables.placeholders_colorcommand = cfg.getString("placeholders.color-command", variables.placeholders_colorcommand);
		variables.placeholders_desc = cfg.getString("placeholders.desc", variables.placeholders_desc);
		variables.placeholders_kills = cfg.getString("placeholders.kills", variables.placeholders_kills);
		variables.placeholders_motd = cfg.getString("placeholders.motd", variables.placeholders_motd);
		variables.placeholders_party = cfg.getString("placeholders.party", variables.placeholders_party);
		variables.placeholders_prefix = cfg.getString("placeholders.prefix", variables.placeholders_prefix);
		variables.placeholders_rankname = cfg.getString("placeholders.rank-name", variables.placeholders_rankname);
		variables.placeholders_rankchat = cfg.getString("placeholders.rank-chat", variables.placeholders_rankchat);
		variables.placeholders_suffix = cfg.getString("placeholders.suffix", variables.placeholders_suffix);
		
		variables.banmanager_enable = cfg.getBoolean("banmanager.enable", variables.banmanager_enable);
		variables.banmanager_muted = cfg.getBoolean("banmanager.prevent-chat-muted", variables.banmanager_muted);
		variables.banmanager_kickban = cfg.getBoolean("banmanager.auto-kick-banned", variables.banmanager_kickban);
		
		variables.dynmap_enable = cfg.getBoolean("dynmap.enable", variables.dynmap_enable);
		variables.dynmap_showpartyhomes = cfg.getBoolean("dynmap.show-party-homes", variables.dynmap_showpartyhomes);
		variables.dynmap_hidedefault = cfg.getBoolean("dynmap.hide-by-default", variables.dynmap_hidedefault);
		variables.dynmap_settings_minimumplayers = cfg.getInt("dynmap.settings.minimum-players", variables.dynmap_settings_minimumplayers);
		variables.dynmap_marker_layer = cfg.getString("dynmap.markers.layer", variables.dynmap_marker_layer);
		variables.dynmap_marker_label = cfg.getString("dynmap.markers.label", variables.dynmap_marker_label);
		variables.dynmap_marker_icon = cfg.getString("dynmap.markers.icon", variables.dynmap_marker_icon);
		
		variables.griefprevention_enable = cfg.getBoolean("griefprevention.enable", variables.griefprevention_enable);
		variables.griefprevention_needowner = cfg.getBoolean("griefprevention.need-to-be-owner-claim", variables.griefprevention_needowner);
		variables.griefprevention_command_trust = cfg.getString("griefprevention.sub-commands.trust", variables.griefprevention_command_trust);
		variables.griefprevention_command_container = cfg.getString("griefprevention.sub-commands.container", variables.griefprevention_command_container);
		variables.griefprevention_command_access = cfg.getString("griefprevention.sub-commands.access", variables.griefprevention_command_access);
		variables.griefprevention_command_remove = cfg.getString("griefprevention.sub-commands.remove", variables.griefprevention_command_remove);
		
		variables.vault_enable = cfg.getBoolean("vault.enable", variables.vault_enable);
		variables.vault_confirm_enable = cfg.getBoolean("vault.confirm-command.enable", variables.vault_confirm_enable);
		variables.vault_confirm_timeout = cfg.getInt("vault.confirm-command.timeout", variables.vault_confirm_timeout);
		variables.vault_command_create_price = cfg.getDouble("vault.price-commands.create", variables.vault_command_create_price);
		variables.vault_command_join_price = cfg.getDouble("vault.price-commands.join", variables.vault_command_join_price);
		variables.vault_command_home_price = cfg.getDouble("vault.price-commands.home", variables.vault_command_home_price);
		variables.vault_command_sethome_price = cfg.getDouble("vault.price-commands.set-home", variables.vault_command_sethome_price);
		variables.vault_command_desc_price = cfg.getDouble("vault.price-commands.desc", variables.vault_command_desc_price);
		variables.vault_command_motd_price = cfg.getDouble("vault.price-commands.motd", variables.vault_command_motd_price);
		variables.vault_command_color_price = cfg.getDouble("vault.price-commands.color", variables.vault_command_color_price);
		variables.vault_command_prefix_price = cfg.getDouble("vault.price-commands.prefix", variables.vault_command_prefix_price);
		variables.vault_command_suffix_price = cfg.getDouble("vault.price-commands.suffix", variables.vault_command_suffix_price);
		variables.vault_command_teleport_price = cfg.getDouble("vault.price-commands.teleport", variables.vault_command_teleport_price);
		variables.vault_command_claim_price = cfg.getDouble("vault.price-commands.claim", variables.vault_command_claim_price);
		
		variables.command_party = cfg.getString("commands.command-party", variables.command_party);
		variables.command_party_desc = cfg.getString("commands.command-party-desc", variables.command_party_desc);
		variables.command_help = cfg.getString("commands.command-help", variables.command_help);
		variables.command_p = cfg.getString("commands.command-p", variables.command_p);
		variables.command_p_desc = cfg.getString("commands.command-p-desc", variables.command_p_desc);
		variables.command_create = cfg.getString("commands.command-create", variables.command_create);
		variables.command_password = cfg.getString("commands.command-password", variables.command_password);
		variables.command_join = cfg.getString("commands.command-join", variables.command_join);
		variables.command_accept = cfg.getString("commands.command-accept", variables.command_accept);
		variables.command_deny = cfg.getString("commands.command-deny", variables.command_deny);
		variables.command_ignore = cfg.getString("commands.command-ignore", variables.command_ignore);
		variables.command_leave = cfg.getString("commands.command-leave", variables.command_leave);
		variables.command_list = cfg.getString("commands.command-list", variables.command_list);
		variables.command_info = cfg.getString("commands.command-info", variables.command_info);
		variables.command_members = cfg.getString("commands.command-members", variables.command_members);
		variables.command_home = cfg.getString("commands.command-home", variables.command_home);
		variables.command_sethome = cfg.getString("commands.command-sethome", variables.command_sethome);
		variables.command_teleport = cfg.getString("commands.command-teleport", variables.command_teleport);
		variables.command_desc = cfg.getString("commands.command-desc", variables.command_desc);
		variables.command_motd = cfg.getString("commands.command-motd", variables.command_motd);
		variables.command_chat = cfg.getString("commands.command-chat", variables.command_chat);
		variables.command_invite = cfg.getString("commands.command-invite", variables.command_invite);
		variables.command_color = cfg.getString("commands.command-color", variables.command_color);
		variables.command_prefix = cfg.getString("commands.command-prefix", variables.command_prefix);
		variables.command_suffix = cfg.getString("commands.command-suffix", variables.command_suffix);
		variables.command_rank = cfg.getString("commands.command-rank", variables.command_rank);
		variables.command_kick = cfg.getString("commands.command-kick", variables.command_kick);
		variables.command_delete = cfg.getString("commands.command-delete", variables.command_delete);
		variables.command_rename = cfg.getString("commands.command-rename", variables.command_rename);
		variables.command_rank = cfg.getString("commands.command-rank", variables.command_rank);
		variables.command_silent = cfg.getString("commands.command-silent", variables.command_silent);
		variables.command_leader = cfg.getString("commands.command-leader", variables.command_leader);
		variables.command_spy = cfg.getString("commands.command-spy", variables.command_spy);
		variables.command_reload = cfg.getString("commands.command-reload", variables.command_reload);
		variables.command_sub_on = cfg.getString("commands.sub-command-on", variables.command_sub_on);
		variables.command_sub_off = cfg.getString("commands.sub-command-off", variables.command_sub_off);
		variables.command_sub_fixed = cfg.getString("commands.sub-command-fixed", variables.command_sub_fixed);
		variables.command_sub_remove = cfg.getString("commands.sub-command-remove", variables.command_sub_remove);
		variables.command_migrate = cfg.getString("commands.command-migrate", variables.command_migrate);
		variables.command_claim = cfg.getString("commands.command-claim", variables.command_claim);
		variables.command_confirm = cfg.getString("commands.command-confirm", variables.command_confirm);
		
		/*
		 * Storage
		 */
		plugin.setDatabaseType(StorageType.getEnum(variables.storage_type_database));
		plugin.setLogType(StorageType.getEnum(variables.storage_type_log));
		
		/*
		 * Get ranks
		 */
		boolean bypass_restart = false;
		List<Rank> ranks = new ArrayList<Rank>();
		int def = -1;
		ConfigurationSection csRanks = cfg.getConfigurationSection("party.ranks");
		while (!bypass_restart) {
			bypass_restart = true;
			if (csRanks != null) {
				for (String key : csRanks.getKeys(false)) {
					int rank = csRanks.get(key+".rank") != null ? csRanks.getInt(key+".rank") : 1;
					String name = csRanks.get(key+".name") != null ? csRanks.getString(key+".name") : key;
					String chat = csRanks.get(key+".chat") != null ? csRanks.getString(key+".chat") : name;
					boolean dft = csRanks.get(key+".default") != null ? csRanks.getBoolean(key+".default") : false;
					List<String> perm = csRanks.get(key+".permissions") != null ? csRanks.getStringList(key+".permissions") : new ArrayList<String>();
					Rank newRank = new RankObj(rank, key, name, chat, dft, perm);
					ranks.add(newRank);
					if (dft)
						def = newRank.getLevel();
				}
				for (int c = 0; c < (ranks.size()-1); c++) {
					if (ranks.get(c).getLevel() > ranks.get(c+1).getLevel()) {
						Collections.swap(ranks, c, c+1);
						c=-1;
					}
				}
				if (def == -1) {
					try {
						def = ranks.get(0).getLevel();
						// Normal log, LogHandler will give error
						plugin.log(ConsoleColors.CYAN.getCode() + "Cannot find default rank, set: " + ranks.get(0).getName());
					} catch(IndexOutOfBoundsException ex) {
						// Normal log, LogHandler will give error
						plugin.log(ConsoleColors.CYAN.getCode() + "Cannot find ranks! Restoring default ranks!");
						
						csRanks = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml"))).getConfigurationSection("party.ranks");
						ranks = new ArrayList<Rank>();
						def = -1;
						bypass_restart = false;
					}
				}
				if (bypass_restart) {
					variables.rank_list = (List<Rank>) ranks;
					variables.rank_default = def;
					variables.rank_last = ranks.get(ranks.size()-1).getLevel();
					bypass_restart = true;
				}
			} else {
				// Normal log, LogHandler will give error
				plugin.log(ConsoleColors.CYAN.getCode() + "Cannot find section party.ranks! Restoring default ranks!");
				csRanks = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml"))).getConfigurationSection("party.ranks");
				ranks = new ArrayList<Rank>();
				def = -1;
				bypass_restart = false;
			}
		}
		
		/*
		 * Party color
		 */
		variables.color_enable = cfg.getBoolean("party.color.enable", variables.color_enable);
		variables.color_colorcommand = cfg.getBoolean("party.color.color-command", variables.color_colorcommand);
		variables.color_dynamic = cfg.getBoolean("party.color.dynamic-color", variables.color_dynamic);
		
		List<Color> colors = new ArrayList<Color>();
		ConfigurationSection csColors = cfg.getConfigurationSection("party.color.list-colors");
		if (csColors != null) {
			for (String key : csColors.getKeys(false)) {
				String name = key;
				String command = csColors.getString(key + ".command", null);
				String code = csColors.getString(key + ".code", null);
				int dynP = -1, dynM = -1, dynK = -1;
				ConfigurationSection csDynamic = csColors.getConfigurationSection(key + ".dynamic");
				if (csDynamic != null) {
					// Getting dynamic color
					dynP = csDynamic.getInt("priority", -1);
					dynM = csDynamic.getInt("members", -1);
					dynK = csDynamic.getInt("kills", -1);
				}
				if (code != null) {
					// Code value is necessary
					ColorObj pc = new ColorObj(name, command, code, dynP, dynM, dynK);
					colors.add(pc);
				}
			}
			variables.color_list = colors;
		}
		
		/*
		 * Placeholders
		 */
		PartiesPlaceholder.setupFormats();
	}
	
	@SuppressWarnings("static-access")
	public void reloadMessages() {
		File msgFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!msgFile.exists()) {
			plugin.saveResource("messages.yml", true);
		}
		FileConfiguration msg = YamlConfiguration.loadConfiguration(msgFile);
		messages.loadDefaults();
		
		if (msg.getInt("dont-edit-this.messages-version") < plugin.getMessagesVersion()) {
			// Normal log, LogHandler will give error
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode() + "Messages file outdated");
		}
		
		
		messages.nopermission = msg.getString("no-permission", messages.nopermission);
		messages.nopermission_party = msg.getString("no-permission-in-party", messages.nopermission_party);
		messages.invalidcommand = msg.getString("invalid-command", messages.invalidcommand);
		messages.canthitmates = msg.getString("cant-hit-mates", messages.canthitmates);
		messages.warnondamage = msg.getString("warn-on-hit-leaders", messages.warnondamage);
		messages.noparty = msg.getString("no-party", messages.noparty);
		messages.configurationreloaded = msg.getString("configuration-reloaded", messages.configurationreloaded);
		messages.updateavailable = msg.getString("update-available", messages.updateavailable);
		messages.expgain = msg.getString("exp-gained", messages.expgain);
		messages.expgainother = msg.getString("exp-gained-from-other", messages.expgainother);
		messages.defaultjoined = msg.getString("default-joined", messages.defaultjoined);
		messages.serverjoin = msg.getString("server-join", messages.serverjoin);
		messages.serverleave = msg.getString("server-leave", messages.serverleave);

		messages.p_cooldown = msg.getString("p.cooldown", messages.p_cooldown);
		messages.p_wrongcmd = msg.getString("p.wrong-command", messages.p_wrongcmd);
		
		messages.create_partycreated = msg.getString("create.party-created", messages.create_partycreated);
		messages.create_partycreated_fixed = msg.getString("create.party-fixed-created", messages.create_partycreated_fixed);
		messages.create_alreadyexist = msg.getString("create.name-already-exist", messages.create_alreadyexist);
		messages.create_alreadyinparty = msg.getString("create.already-in-party", messages.create_alreadyinparty);
		messages.create_toolongname = msg.getString("create.too-long-name", messages.create_toolongname);
		messages.create_tooshortname = msg.getString("create.too-short-name", messages.create_tooshortname);
		messages.create_invalidname = msg.getString("create.invalid-name", messages.create_invalidname);
		if ( msg.getString("create.censored-name") != null)
			messages.create_censoredname = msg.getString("create.censored-name", messages.create_censoredname);
		messages.create_wrongcmd = msg.getString("create.wrong-command", messages.create_wrongcmd);
		
		messages.accept_welcomeplayer = msg.getString("accept.welcome-player", messages.accept_welcomeplayer);
		messages.accept_playerjoined = msg.getString("accept.player-joined", messages.accept_playerjoined);
		messages.accept_inviteaccepted = msg.getString("accept.invite-accepted", messages.accept_inviteaccepted);
		messages.accept_accepted = msg.getString("accept.accepted", messages.accept_accepted);
		messages.accept_maxplayers = msg.getString("accept.max-player-reached", messages.accept_maxplayers);
		messages.accept_alreadyinparty = msg.getString("accept.already-in-party", messages.accept_alreadyinparty);
		messages.accept_noinvite = msg.getString("accept.no-invite", messages.accept_noinvite);
		messages.accept_noexist = msg.getString("accept.no-exist", messages.accept_noexist);
		
		messages.deny_invitedenied = msg.getString("deny.invite-denied", messages.deny_invitedenied);
			messages.deny_denied = msg.getString("deny.denied", messages.deny_denied);
		messages.deny_noinvite = msg.getString("deny.no-invite", messages.deny_noinvite);
		messages.deny_noexist = msg.getString("deny.no-exist", messages.deny_noexist);

		messages.ignore_header = msg.getString("ignore.header", messages.ignore_header);
		messages.ignore_color = msg.getString("ignore.list-ignored-color", messages.ignore_color);
		messages.ignore_separator = msg.getString("ignore.list-ignored-separator", messages.ignore_separator);
		messages.ignore_empty = msg.getString("ignore.list-ignored-empty", messages.ignore_empty);
		messages.ignore_ignored = msg.getString("ignore.party-ignored", messages.ignore_ignored);
		messages.ignore_deignored = msg.getString("ignore.party-deignored", messages.ignore_deignored);
		messages.ignore_noexist = msg.getString("ignore.no-exist", messages.ignore_noexist);
		messages.ignore_wrongcmd = msg.getString("ignore.wrong-command", messages.ignore_wrongcmd);

		messages.leave_byeplayer = msg.getString("leave.bye-player", messages.leave_byeplayer);
		messages.leave_playerleaved = msg.getString("leave.player-leaved-from-party", messages.leave_playerleaved);
		messages.leave_disbanded = msg.getString("leave.party-disbanded", messages.leave_disbanded);

		messages.info_content = msg.getStringList("info.content");
		messages.info_separator = msg.getString("info.player-separator", messages.info_separator);
		messages.info_online = msg.getString("info.player-online-prefix", messages.info_online);
		messages.info_offline = msg.getString("info.player-offline-prefix", messages.info_offline);
		messages.info_empty = msg.getString("info.player-empty", messages.info_empty);
		messages.info_missing = msg.getString("info.missing", messages.info_missing);
		messages.info_noexist = msg.getString("info.no-exist", messages.info_noexist);
		messages.info_wrongcmd = msg.getString("info.wrong-command", messages.info_wrongcmd);
		
		messages.members_content = msg.getStringList("members.content");
		messages.members_separator = msg.getString("members.player-separator", messages.members_separator);
		messages.members_online = msg.getString("members.player-online-prefix", messages.members_online);
		messages.members_offline = msg.getString("members.player-offline-prefix", messages.members_offline);
		messages.members_someone = msg.getString("members.someone-player", messages.members_someone);
		messages.members_empty = msg.getString("members.player-empty", messages.members_empty);
		messages.members_noexist = msg.getString("members.no-exist", messages.members_noexist);
		messages.members_wrongcmd = msg.getString("members.wrong-command", messages.members_wrongcmd);
		
		messages.desc_changed = msg.getString("desc.changed", messages.desc_changed);
		messages.desc_removed = msg.getString("desc.removed", messages.desc_removed);
		messages.desc_invalidchars = msg.getString("desc.invalid-chars", messages.desc_invalidchars);
		messages.desc_censored = msg.getString("desc.censored-name", messages.desc_censored);
		messages.desc_wrongcmd = msg.getString("desc.wrong-command", messages.desc_wrongcmd);
		
		messages.motd_changed = msg.getString("motd.changed", messages.motd_changed);
		messages.motd_removed = msg.getString("motd.removed", messages.motd_removed);
		messages.motd_header = msg.getString("motd.header", messages.motd_header);
		messages.motd_color = msg.getString("motd.color-motd", messages.motd_color);
		messages.motd_footer = msg.getString("motd.footer", messages.motd_footer);
		messages.motd_invalidchars = msg.getString("motd.invalid-chars", messages.motd_invalidchars);
		messages.motd_censored = msg.getString("motd.censored-name", messages.motd_censored);
		messages.motd_wrongcmd = msg.getString("motd.wrong-command", messages.motd_wrongcmd);

		messages.chat_enabled = msg.getString("chat.enabled", messages.chat_enabled);
		messages.chat_disabled = msg.getString("chat.disabled", messages.chat_disabled);
		messages.chat_wrongcmd = msg.getString("chat.wrong-command", messages.chat_wrongcmd);
		
		messages.list_header = msg.getString("list.header", messages.list_header);
		messages.list_subheader = msg.getString("list.sub-header", messages.list_subheader);
		messages.list_offline = msg.getString("list.no-one-online", messages.list_offline);
		messages.list_formatparty = msg.getString("list.format-party", messages.list_formatparty);
		messages.list_footer = msg.getString("list.footer", messages.list_footer);
		messages.list_wrongcmd = msg.getString("list.wrong-command", messages.list_wrongcmd);

		messages.invite_onlyonline = msg.getString("invite.only-online", messages.invite_onlyonline);
		messages.invite_maxplayers = msg.getString("invite.max-player-reached", messages.invite_maxplayers);
		messages.invite_alreadyparty = msg.getString("invite.already-party", messages.invite_alreadyparty);
		messages.invite_alreadyinvited = msg.getString("invite.already-invited", messages.invite_alreadyinvited);
		messages.invite_playernopex = msg.getString("invite.no-permission-player", messages.invite_playernopex);
		messages.invite_send = msg.getString("invite.invite-send", messages.invite_send);
		messages.invite_rec = msg.getString("invite.invite-rec", messages.invite_rec);
		messages.invite_timeout = msg.getString("invite.invite-timeout", messages.invite_timeout);
		messages.invite_noresponse = msg.getString("invite.invite-noresponse", messages.invite_noresponse);
		messages.invite_revoked_send = msg.getString("invite.invite-revoked-send", messages.invite_revoked_send);
		messages.invite_revoked_rec = msg.getString("invite.invite-revoked-rec", messages.invite_revoked_rec);
		messages.invite_wrongcmd = msg.getString("invite.wrong-command", messages.invite_wrongcmd);

		messages.color_info = msg.getString("color.info", messages.color_info);
		messages.color_empty = msg.getString("color.empty", messages.color_empty);
		messages.color_changed = msg.getString("color.changed", messages.color_changed);
		messages.color_removed = msg.getString("color.removed", messages.color_removed);
		messages.color_wrongcolor = msg.getString("color.wrong-color", messages.color_wrongcolor);
		messages.color_wrongcmd = msg.getString("color.wrong-command", messages.color_wrongcmd);

		messages.prefix_changed = msg.getString("prefix.changed", messages.prefix_changed);
		messages.prefix_removed = msg.getString("prefix.removed", messages.prefix_removed);
		messages.prefix_invalidchars = msg.getString("prefix.invalid-chars", messages.prefix_invalidchars);
		messages.prefix_censored = msg.getString("prefix.censored-name", messages.prefix_censored);
		messages.prefix_wrongcmd = msg.getString("prefix.wrong-command", messages.prefix_wrongcmd);

		messages.suffix_changed = msg.getString("suffix.changed", messages.suffix_changed);
		messages.suffix_removed = msg.getString("suffix.removed", messages.suffix_removed);
		messages.suffix_invalidchars = msg.getString("suffix.invalid-chars", messages.suffix_invalidchars);
		messages.suffix_censored = msg.getString("suffix.censored-name", messages.suffix_censored);
		messages.suffix_wrongcmd = msg.getString("suffix.wrong-command", messages.suffix_wrongcmd);

		messages.kick_kicksend = msg.getString("kick.kick-send", messages.kick_kicksend);
		messages.kick_uprank = msg.getString("kick.kick-up-rank", messages.kick_uprank);
		messages.kick_kickedfrom = msg.getString("kick.kicked-from-party", messages.kick_kickedfrom);
		messages.kick_kickedplayer = msg.getString("kick.kicked-player-party", messages.kick_kickedplayer);
		messages.kick_kicksendother = msg.getString("kick.kick-send-other", messages.kick_kicksendother);
		messages.kick_nomemberother = msg.getString("kick.other-no-member", messages.kick_nomemberother);
		messages.kick_nomember = msg.getString("kick.no-member", messages.kick_nomember);
		messages.kick_wrongcmd = msg.getString("kick.wrong-command", messages.kick_wrongcmd);

		messages.delete_deleted = msg.getString("delete.deleted", messages.delete_deleted);
		messages.delete_deleted_silent = msg.getString("delete.silent-delete", messages.delete_deleted_silent);
		messages.delete_warn = msg.getString("delete.warn", messages.delete_warn);
		messages.delete_noexist = msg.getString("delete.no-exist", messages.delete_noexist);
		messages.delete_wrongcmd = msg.getString("delete.wrong-command", messages.delete_wrongcmd);
		
		messages.rename_renamed = msg.getString("rename.renamed", messages.rename_renamed);
		messages.rename_broadcast = msg.getString("rename.broadcast-party", messages.rename_broadcast);
		messages.rename_noexist = msg.getString("rename.no-exist", messages.rename_noexist);
		messages.rename_wrongcmd = msg.getString("rename.wrong-command", messages.rename_wrongcmd);
		messages.rename_wrongcmd_admin = msg.getString("rename.wrong-command-admin", messages.rename_wrongcmd_admin);

		messages.rank_promoted = msg.getString("rank.promoted", messages.rank_promoted);
		messages.rank_nomember = msg.getString("rank.no-member", messages.rank_nomember);
		messages.rank_noparty = msg.getString("rank.no-party", messages.rank_noparty);
		messages.rank_wrongrank = msg.getString("rank.wrong-rank", messages.rank_wrongrank);
		messages.rank_alreadyrank = msg.getString("rank.already-rank", messages.rank_alreadyrank);
		messages.rank_lowrank = msg.getString("rank.low-rank", messages.rank_lowrank);
		messages.rank_tohigherrank = msg.getString("rank.to-higher-rank", messages.rank_tohigherrank);
		messages.rank_nodegradeyourself = msg.getString("rank.no-degrade-yourself", messages.rank_nodegradeyourself);
		messages.rank_nopromoteyourself = msg.getString("rank.no-promote-yourself", messages.rank_nopromoteyourself);
		messages.rank_wrongcmd = msg.getString("rank.wrong-command", messages.rank_wrongcmd);
		
		messages.sethome_set = msg.getString("sethome.set-broadcast", messages.sethome_set);
		messages.sethome_wrongcmd = msg.getString("sethome.wrong-command", messages.sethome_wrongcmd);
		
		messages.home_teleported = msg.getString("home.teleported", messages.home_teleported);
		messages.home_in = msg.getString("home.teleport-in", messages.home_in);
		messages.home_denied = msg.getString("home.teleport-denied", messages.home_denied);
		messages.home_nohome = msg.getString("home.no-home", messages.home_nohome);
		messages.home_noexist = msg.getString("home.no-exist-party", messages.home_noexist);
		messages.home_wrongcmd = msg.getString("home.wrong-command", messages.home_wrongcmd);
		messages.home_wrongcmd_admin = msg.getString("home.wrong-command-admin", messages.home_wrongcmd_admin);
		
		messages.teleport_teleporting = msg.getString("teleport.teleporting", messages.teleport_teleporting);
		messages.teleport_playerteleported = msg.getString("teleport.player-teleported", messages.teleport_playerteleported);
		messages.teleport_delay = msg.getString("teleport.delay", messages.teleport_delay);
		messages.teleport_wrongcmd = msg.getString("teleport.wrong-command", messages.teleport_wrongcmd);
		
		messages.claim_done = msg.getString("claim.done", messages.claim_done);
		messages.claim_removed = msg.getString("claim.removed", messages.claim_removed);
		messages.claim_nomanager = msg.getString("claim.no-manager", messages.claim_nomanager);
		messages.claim_noexistclaim = msg.getString("claim.no-exist-claim", messages.claim_noexistclaim);
		messages.claim_wrongcmd = msg.getString("claim.wrong-command", messages.claim_wrongcmd);
		
		messages.spy_active = msg.getString("spy.activated", messages.spy_active);
		messages.spy_disable = msg.getString("spy.disabled", messages.spy_disable);
		
		messages.migrate_info = msg.getString("migrate.info", messages.migrate_info);
		messages.migrate_complete = msg.getString("migrate.complete", messages.migrate_complete);
		messages.migrate_failed_offline = msg.getString("migrate.failed-offline", messages.migrate_failed_offline);
		messages.migrate_failed_migration = msg.getString("migrate.failed-migration", messages.migrate_failed_migration);
		messages.migrate_failed_same = msg.getString("migrate.failed-same", messages.migrate_failed_same);
		messages.migrate_wrongdatabase = msg.getString("migrate.wrong-database", messages.migrate_wrongdatabase);
		messages.migrate_wrongcmd = msg.getString("migrate.wrong-command", messages.migrate_wrongcmd);
		
		messages.vault_create_nomoney = msg.getString("vault.create-nomoney", messages.vault_create_nomoney);
		messages.vault_join_nomoney = msg.getString("vault.join-nomoney", messages.vault_join_nomoney);
		messages.vault_home_nomoney = msg.getString("vault.home-nomoney", messages.vault_home_nomoney);
		messages.vault_sethome_nomoney = msg.getString("vault.sethome-nomoney", messages.vault_sethome_nomoney);
		messages.vault_desc_nomoney = msg.getString("vault.desc-nomoney", messages.vault_desc_nomoney);
		messages.vault_motd_nomoney = msg.getString("vault.motd-nomoney", messages.vault_motd_nomoney);
		messages.vault_prefix_nomoney = msg.getString("vault.prefix-nomoney", messages.vault_prefix_nomoney);
		messages.vault_suffix_nomoney = msg.getString("vault.suffix-nomoney", messages.vault_suffix_nomoney);
		messages.vault_teleport_nomoney = msg.getString("vault.teleport-nomoney", messages.vault_teleport_nomoney);
		messages.vault_claim_nomoney = msg.getString("vault.claim-nomoney", messages.vault_claim_nomoney);
		messages.vault_confirm_anycmd = msg.getString("vault.confirm.any-cmd", messages.vault_confirm_anycmd);
		messages.vault_confirm_warnonbuy = msg.getString("vault.confirm.warn-onbuy", messages.vault_confirm_warnonbuy);
		messages.vault_confirm_confirmed = msg.getString("vault.confirm.confirmed", messages.vault_confirm_confirmed);
		messages.vault_confirm_wrongcmd = msg.getString("vault.confirm.wrong-command", messages.vault_confirm_wrongcmd);
		
		messages.follow_following_world = msg.getString("follow.following-world", messages.follow_following_world);
		messages.follow_following_server = msg.getString("follow.following-server", messages.follow_following_server);
		
		messages.help_header = msg.getString("help.header", messages.help_header);
		messages.help_help = msg.getString("help.help", messages.help_help);
		messages.help_p = msg.getString("help.p", messages.help_p);
		messages.help_create = msg.getString("help.create", messages.help_create);
		messages.help_createfixed = msg.getString("help.create-fixed", messages.help_createfixed);
		messages.help_join = msg.getString("help.join", messages.help_join);
		messages.help_accept = msg.getString("help.accept", messages.help_accept);
		messages.help_deny = msg.getString("help.deny", messages.help_deny);
		messages.help_ignore = msg.getString("help.ignore", messages.help_ignore);
		messages.help_leave = msg.getString("help.leave", messages.help_leave);
		messages.help_info = msg.getString("help.info", messages.help_info);
		messages.help_members = msg.getString("help.members", messages.help_members);
		messages.help_password = msg.getString("help.password", messages.help_password);
		messages.help_rank = msg.getString("help.rank", messages.help_rank);
		messages.help_home = msg.getString("help.home", messages.help_home);
		messages.help_home_others = msg.getString("help.home-others", messages.help_home_others);
		messages.help_sethome = msg.getString("help.sethome", messages.help_sethome);
		messages.help_teleport = msg.getString("help.teleport", messages.help_teleport);
		messages.help_desc = msg.getString("help.desc", messages.help_desc);
		messages.help_motd = msg.getString("help.motd", messages.help_motd);
		messages.help_chat = msg.getString("help.chat", messages.help_chat);
		messages.help_list = msg.getString("help.list", messages.help_list);
		messages.help_invite = msg.getString("help.invite", messages.help_invite);
		messages.help_color = msg.getString("help.color", messages.help_color);
		messages.help_prefix = msg.getString("help.prefix", messages.help_prefix);
		messages.help_suffix = msg.getString("help.suffix", messages.help_suffix);
		messages.help_kick = msg.getString("help.kick", messages.help_kick);
		messages.help_spy = msg.getString("help.spy", messages.help_spy);
		messages.help_delete = msg.getString("help.delete", messages.help_delete);
		messages.help_rename = msg.getString("help.rename", messages.help_rename);
		messages.help_rename_others = msg.getString("help.rename-others", messages.help_rename_others);
		messages.help_reload = msg.getString("help.reload", messages.help_reload);
		messages.help_migrate = msg.getString("help.migrate", messages.help_migrate);
		messages.help_claim = msg.getString("help.claim", messages.help_claim);
	}
	
	public Messages getMessages() {
		return messages;
	}
	public Variables getVariables() {
		return variables;
	}
}
