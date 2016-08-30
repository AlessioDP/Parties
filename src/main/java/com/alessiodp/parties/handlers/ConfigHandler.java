package com.alessiodp.parties.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Data;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.SQLDatabase;

public class ConfigHandler {
	Parties plugin;
	Variables variables;
	Messages messages;
	Data data;
	
	public ConfigHandler(Parties instance){
		boolean flag = false;
		plugin = instance;
		variables = new Variables();
		messages = new Messages();
		reloadConfig();
		reloadMessages();
		if(Variables.database_sql_enable){
			SQLDatabase database = new SQLDatabase(plugin, Variables.database_sql_username, Variables.database_sql_password, Variables.database_sql_url);
			if(database.isFailed()){
				Variables.database_sql_enable = false;
				plugin.log(ConsoleColors.CYAN.getCode() + "Failed open connection with Server SQL. Database changed to File");
				LogHandler.log(1, "Failed open connection with Server SQL. Database changed to File");
			} else {
				plugin.setSQLDatabase(database);
				flag=true;
			}
		}
		if(flag)
			data = new Data(this, true);
		else
		    data = new Data(this, false);
	}
	
	@SuppressWarnings("static-access")
	public void reloadConfig(){
		File cfgFile = new File(plugin.getDataFolder(), "config.yml");
		if (!cfgFile.exists()) {
			plugin.saveResource("config.yml", true);
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
		
		if(cfg.getInt("dont-edit-this.config-version") < plugin.getConfigVersion()){
			plugin.log(ConsoleColors.RED.getCode() + "Configuration file outdated");
			LogHandler.log(1, "Configuration file outdated");
		}
		
		if(cfg.get("functions.use-uuid") != null)
			variables.use_uuid = cfg.getBoolean("functions.use-uuid");
		if(cfg.get("functions.download-updates") != null)
			variables.downloadupdates = cfg.getBoolean("functions.download-updates");
		if(cfg.get("functions.warn-updates-in-game") != null)
			variables.warnupdates = cfg.getBoolean("functions.warn-updates-in-game");
		if(cfg.get("functions.per-permission-help") != null)
			variables.perpermissionhelp = cfg.getBoolean("functions.per-permission-help");
		if(cfg.get("functions.permission-page-help") != null)
			variables.permissionspagehelp = cfg.getInt("functions.permission-page-help");
		if(cfg.get("functions.divide-exp-between-players") != null)
			variables.divideexp = cfg.getBoolean("functions.divide-exp-between-players");
		if(cfg.get("functions.divide-exp-range") != null)
			variables.exprange = cfg.getInt("functions.divide-exp-range");
		if(cfg.get("functions.see-allies-invisible") != null)
			variables.invisibleallies = cfg.getBoolean("functions.see-allies-invisible");
		if(cfg.get("functions.bungeecord") != null)
			variables.bungeecord = cfg.getBoolean("functions.bungeecord");
		
		if(cfg.get("log.enable") != null)
			variables.log_enable = cfg.getBoolean("log.enable");
		if(cfg.get("log.prefix") != null)
		    variables.log_prefix = cfg.getString("log.prefix");
		if(cfg.get("log.chat") != null)
			variables.log_chat = cfg.getBoolean("log.chat");
		if(cfg.get("log.mode") != null)
			variables.log_mode = cfg.getInt("log.mode");
		
		if(cfg.get("log.type") != null)
		    variables.log_type = cfg.getString("log.type");
		if(cfg.get("log.file.name") != null)
		    variables.log_file_name = cfg.getString("log.file.name");
		if(cfg.get("log.sql.url") != null)
		    variables.log_sql_url = cfg.getString("log.sql.url");
		if(cfg.get("log.sql.username") != null)
		    variables.log_sql_username = cfg.getString("log.sql.username");
		if(cfg.get("log.sql.password") != null)
		    variables.log_sql_password = cfg.getString("log.sql.password");
		if(cfg.get("log.sql.log-table") != null)
		    variables.log_sql_logtable = cfg.getString("log.sql.log-table");
		
		if(cfg.get("database.type") != null)
		    variables.database_type = cfg.getString("database.type");
		if(cfg.get("database.migrate-only-console") != null)
			variables.database_migrate_console = cfg.getBoolean("database.migrate-only-console");
		if(cfg.get("database.none.disband-on-leaders-left") != null)
		    variables.database_none_leaderleft = cfg.getBoolean("database.none.disband-on-leaders-left");
		if(cfg.get("database.none.delay-delete-party") != null)
		    variables.database_none_delay = cfg.getInt("database.none.delay-delete-party");
		if(cfg.get("database.file.name") != null)
		    variables.database_file_name = cfg.getString("database.file.name");
		if(cfg.get("database.sql.enable") != null)
		    variables.database_sql_enable = cfg.getBoolean("database.sql.enable");
		if(cfg.get("database.sql.url") != null)
		    variables.database_sql_url = cfg.getString("database.sql.url");
		if(cfg.get("database.sql.username") != null)
		    variables.database_sql_username = cfg.getString("database.sql.username");
		if(cfg.get("database.sql.password") != null)
		    variables.database_sql_password = cfg.getString("database.sql.password");
		if(cfg.get("database.sql.tables.spies") != null)
		    variables.database_sql_tables_spies = cfg.getString("database.sql.tables.spies");
		if(cfg.get("database.sql.tables.players") != null)
	    	variables.database_sql_tables_players = cfg.getString("database.sql.tables.players");
		if(cfg.get("database.sql.tables.parties") != null)
			variables.database_sql_tables_parties = cfg.getString("database.sql.tables.parties");
		
		if(cfg.get("party.max-members") != null)
			variables.party_maxmembers = cfg.getInt("party.max-members");
		if(cfg.get("party.allowed-chars") != null)
			variables.party_allowedchars = cfg.getString("party.allowed-chars");
		if(cfg.get("party.min-length-name") != null)
			variables.party_minlengthname = cfg.getInt("party.min-length-name");
		if(cfg.get("party.max-length-name") != null)
			variables.party_maxlengthname = cfg.getInt("party.max-length-name");
		
		if(cfg.get("party.prevent-friendly-fire.enable") != null)
			variables.friendlyfire_enable = cfg.getBoolean("party.prevent-friendly-fire.enable");
		if(cfg.get("party.prevent-friendly-fire.warn-players-on-fight") != null)
			variables.friendlyfire_warn = cfg.getBoolean("party.prevent-friendly-fire.warn-players-on-fight");
		if(cfg.get("party.prevent-friendly-fire.list-worlds") != null)
			variables.friendlyfire_listworlds = cfg.getStringList("party.prevent-friendly-fire.list-worlds");
		
		if(cfg.get("party.invite.timeout") != null)
			variables.invite_timeout = cfg.getInt("party.invite.timeout");
		if(cfg.get("party.invite.revoke") != null)
			variables.invite_revoke = cfg.getBoolean("party.invite.revoke");
		if(cfg.get("party.invite.prevent-invite-player-no-permission-join") != null)
			variables.invite_nopex = cfg.getBoolean("party.invite.prevent-invite-player-no-permission-join");
		
		if(cfg.get("party.home.cooldown") != null)
			variables.home_cooldown = cfg.getInt("party.home.cooldown");
		if(cfg.get("party.home.cancel-if-move") != null)
			variables.home_cancelmove = cfg.getBoolean("party.home.cancel-if-move");
		if(cfg.get("party.home.distance-cancel") != null)
			variables.home_distance = cfg.getInt("party.home.distance-cancel");
		
		if(cfg.get("party.password.enable") != null)
			variables.password_enable = cfg.getBoolean("party.password.enable");
		if(cfg.get("party.password.allowed-chars") != null)
			variables.password_allowedchars = cfg.getString("party.password.allowed-chars");
		if(cfg.get("party.password.hash") != null)
			variables.password_hash = cfg.getString("party.password.hash");
		if(cfg.get("party.password.encode") != null)
			variables.password_encode = cfg.getString("party.password.encode");
		if(cfg.get("party.password.length-min") != null)
			variables.password_lengthmin = cfg.getInt("party.password.length-min");
		if(cfg.get("party.password.length-max") != null)
			variables.password_lengthmax = cfg.getInt("party.password.length-max");;
		
		if(cfg.get("description.length-min") != null)
			variables.desc_min = cfg.getInt("description.length-min");
		if(cfg.get("description.length-max") != null)
			variables.desc_max = cfg.getInt("description.length-max");
		if(cfg.get("description.allowed-chars") != null)
			variables.desc_allowedchars = cfg.getString("description.allowed-chars");
		if(cfg.get("description.remove-word") != null)
			variables.desc_removeword = cfg.getString("description.remove-word");
		if(cfg.get("description.censored-words") != null)
			variables.desc_censored = cfg.getStringList("description.censored-words");
			
		if(cfg.get("motd.length-min") != null)
			variables.motd_min = cfg.getInt("motd.length-min");
		if(cfg.get("motd.length-max") != null)
			variables.motd_max = cfg.getInt("motd.length-max");
		if(cfg.get("motd.delay") != null)
			variables.motd_delay = cfg.getInt("motd.delay");
		if(cfg.get("motd.allowed-chars") != null)
			variables.motd_allowedchars = cfg.getString("motd.allowed-chars");
		if(cfg.get("motd.new-line-code") != null)
			variables.motd_newline = cfg.getString("motd.new-line-code");
		if(cfg.get("motd.remove-word") != null)
			variables.motd_removeword = cfg.getString("motd.remove-word");
		variables.motd_censored = cfg.getStringList("motd.censored-words");
		
		if(cfg.get("kills.save-kills") != null)
			variables.kill_enable = cfg.getBoolean("kills.save-kills");
		if(cfg.get("kills.which-save.neutral-mobs") != null)
			variables.kill_save_mobsneutral = cfg.getBoolean("kills.which-save.neutral-mobs");
		if(cfg.get("kills.which-save.mobs") != null)
			variables.kill_save_mobshostile = cfg.getBoolean("kills.which-save.hostile-mobs");
		if(cfg.get("kills.which-save.players") != null)
			variables.kill_save_players = cfg.getBoolean("kills.which-save.players");
		
		if(cfg.get("tag.tag-system") != null)
			variables.tag_enable = cfg.getBoolean("tag.tag-system");
		if(cfg.get("tag.which-tag-system") != null)
			variables.tag_system = cfg.getBoolean("tag.which-tag-system");
		if(cfg.get("tag.base-tag.format-prefix") != null)
			variables.tag_base_formatprefix = cfg.getString("tag.base-tag.format-prefix");
		if(cfg.get("tag.base-tag.format-suffix") != null)
			variables.tag_base_formatsuffix = cfg.getString("tag.base-tag.format-suffix");
		if(cfg.get("tag.custom-tag.prefix") != null)
			variables.tag_custom_prefix = cfg.getBoolean("tag.custom-tag.prefix");
		if(cfg.get("tag.custom-tag.format-prefix") != null)
			variables.tag_custom_formatprefix = cfg.getString("tag.custom-tag.format-prefix");
		if(cfg.get("tag.custom-tag.suffix") != null)
			variables.tag_custom_suffix = cfg.getBoolean("tag.custom-tag.suffix");
		if(cfg.get("tag.custom-tag.format-suffix") != null)
			variables.tag_custom_formatsuffix = cfg.getString("tag.custom-tag.format-suffix");
		if(cfg.get("tag.custom-tag.remove-word") != null)
			variables.tag_custom_removeword = cfg.getString("tag.custom-tag.remove-word");
		if(cfg.get("tag.custom-tag.allowed-chars") != null)
			variables.tag_custom_allowedchars = cfg.getString("tag.custom-tag.allowed-chars");
		if(cfg.get("tag.custom-tag.max-length") != null)
			variables.tag_custom_maxlength = cfg.getInt("tag.custom-tag.max-length");
		if(cfg.get("tag.custom-tag.min-length") != null)
			variables.tag_custom_minlength = cfg.getInt("tag.custom-tag.min-length");
		variables.tag_custom_censor = cfg.getStringList("tag.custom-tag.censored-words");

		if(cfg.get("chat.chat-format") != null)
			variables.chatformat = cfg.getString("chat.chat-format");
		if(cfg.get("chat.spy-chat-format") != null)
			variables.spychatformat = cfg.getString("chat.spy-chat-format");
		if(cfg.get("chat.party-broadcast-format") != null)
			variables.partybroadcastformat = cfg.getString("chat.party-broadcast-format");
		if(cfg.get("chat.format-group") != null)
			variables.formatgroup = cfg.getString("chat.format-group");
		
		if(cfg.get("list.ordered-by") != null)
			variables.list_orderedby = cfg.getString("list.ordered-by");
		if(cfg.get("list.filter-min") != null)
			variables.list_filter = cfg.getInt("list.filter-min");
		if(cfg.get("list.parties-per-page") != null)
			variables.list_maxpages = cfg.getInt("list.parties-per-page");
		variables.list_hiddenparty = cfg.getStringList("list.hidden-parties");
		
		if(cfg.get("follow-party.enable") != null)
			variables.follow_enable = cfg.getBoolean("follow-party.enable");
		if(cfg.get("follow-party.type-of-teleport") != null)
			variables.follow_type = cfg.getInt("follow-party.type-of-teleport");
		if(cfg.get("follow-party.needed-rank") != null)
			variables.follow_neededrank = cfg.getInt("follow-party.needed-rank");
		if(cfg.get("follow-party.minimum-rank-to-follow") != null)
			variables.follow_minimumrank = cfg.getInt("follow-party.minimum-rank-to-follow");
		if(cfg.get("follow-party.timeout-portal") != null)
			variables.follow_timeoutportal = cfg.getInt("follow-party.timeout-portal");
		if(cfg.get("follow-party.list-worlds") != null)
			variables.follow_listworlds = cfg.getStringList("follow-party.list-worlds");
		
		if(cfg.get("auto-command.enable") != null)
			variables.autocommand_enable = cfg.getBoolean("auto-command.enable");
		if(cfg.get("auto-command.blacklist") != null)
			variables.autocommand_blacklist = cfg.getStringList("auto-command.blacklist");
		if(cfg.get("auto-command.whitelist") != null)
			variables.autocommand_whitelist= cfg.getStringList("auto-command.whitelist");
		
		if(cfg.get("censor.enable") != null)
			variables.censor_enable = cfg.getBoolean("censor.enable");
		if(cfg.get("censor.case-sensitive") != null)
			variables.censor_casesensitive = cfg.getBoolean("censor.case-sensitive");
		variables.censor_contains = cfg.getStringList("censor.contains");
		variables.censor_startwith = cfg.getStringList("censor.start-with");
		variables.censor_endwith = cfg.getStringList("censor.end-with");
		
		if(cfg.get("banmanager.enable") != null)
			variables.banmanager_enable = cfg.getBoolean("banmanager.enable");
		if(cfg.get("banmanager.prevent-chat-muted") != null)
			variables.banmanager_muted = cfg.getBoolean("banmanager.prevent-chat-muted");
		if(cfg.get("banmanager.auto-kick-banned") != null)
			variables.banmanager_kickban = cfg.getBoolean("banmanager.auto-kick-banned");
		
		if(cfg.get("dynmap.enable") != null)
			variables.dynmap_enable = cfg.getBoolean("dynmap.enable");
		if(cfg.get("dynmap.show-party-homes") != null)
			variables.dynmap_showpartyhomes = cfg.getBoolean("dynmap.show-party-homes");
		if(cfg.get("dynmap.hide-by-default") != null)
			variables.dynmap_hidedefault = cfg.getBoolean("dynmap.hide-by-default");
		if(cfg.get("dynmap.settings.minimum-players") != null)
			variables.dynmap_settings_minimumplayers = cfg.getInt("dynmap.settings.minimum-players");
		if(cfg.get("dynmap.markers.layer") != null)
			variables.dynmap_marker_layer = cfg.getString("dynmap.markers.layer");
		if(cfg.get("dynmap.markers.label") != null)
			variables.dynmap_marker_label = cfg.getString("dynmap.markers.label");
		if(cfg.get("dynmap.markers.icon") != null)
			variables.dynmap_marker_icon = cfg.getString("dynmap.markers.icon");
		
		if(cfg.get("griefprevention.enable") != null)
			variables.griefprevention_enable = cfg.getBoolean("griefprevention.enable");
		if(cfg.get("griefprevention.need-to-be-owner-claim") != null)
			variables.griefprevention_needowner = cfg.getBoolean("griefprevention.need-to-be-owner-claim");
		if(cfg.get("griefprevention.sub-commands.trust") != null)
			variables.griefprevention_command_trust = cfg.getString("griefprevention.sub-commands.trust");
		if(cfg.get("griefprevention.sub-commands.container") != null)
			variables.griefprevention_command_container = cfg.getString("griefprevention.sub-commands.container");
		if(cfg.get("griefprevention.sub-commands.access") != null)
			variables.griefprevention_command_access = cfg.getString("griefprevention.sub-commands.access");
		if(cfg.get("griefprevention.sub-commands.remove") != null)
			variables.griefprevention_command_remove = cfg.getString("griefprevention.sub-commands.remove");
		
		if(cfg.get("vault.enable") != null)
			variables.vault_enable = cfg.getBoolean("vault.enable");
		if(cfg.get("vault.confirm-command.enable") != null)
			variables.vault_confirm_enable = cfg.getBoolean("vault.confirm-command.enable");
		if(cfg.get("vault.confirm-command.timeout") != null)
			variables.vault_confirm_timeout = cfg.getInt("vault.confirm-command.timeout");
		if(cfg.get("vault.price-commands.create") != null)
			variables.vault_create_price = cfg.getDouble("vault.price-commands.create");
		if(cfg.get("vault.price-commands.home") != null)
			variables.vault_home_price = cfg.getDouble("vault.price-commands.home");
		if(cfg.get("vault.price-commands.set-home") != null)
			variables.vault_sethome_price = cfg.getDouble("vault.price-commands.set-home");
		if(cfg.get("vault.price-commands.desc") != null)
			variables.vault_desc_price = cfg.getDouble("vault.price-commands.desc");
		if(cfg.get("vault.price-commands.motd") != null)
			variables.vault_motd_price = cfg.getDouble("vault.price-commands.motd");
		if(cfg.get("vault.price-commands.prefix") != null)
			variables.vault_prefix_price = cfg.getDouble("vault.price-commands.prefix");
		if(cfg.get("vault.price-commands.suffix") != null)
			variables.vault_suffix_price = cfg.getDouble("vault.price-commands.suffix");
		
		if(cfg.get("commands.command-party") != null)
			variables.command_party = cfg.getString("commands.command-party");
		if(cfg.get("commands.command-party-desc") != null)
			variables.command_party_desc = cfg.getString("commands.command-party-desc");
		if(cfg.get("commands.command-help") != null)
			variables.command_help = cfg.getString("commands.command-help");
		if(cfg.get("commands.command-p") != null)
			variables.command_p = cfg.getString("commands.command-p");
		if(cfg.get("commands.command-p-desc") != null)
			variables.command_p_desc = cfg.getString("commands.command-p-desc");
		if( cfg.get("commands.command-create") != null)
			variables.command_create = cfg.getString("commands.command-create");
		if( cfg.get("commands.command-password") != null)
			variables.command_password = cfg.getString("commands.command-password");
		if( cfg.get("commands.command-join") != null)
			variables.command_join = cfg.getString("commands.command-join");
		if(cfg.get("commands.command-accept") != null)
			variables.command_accept = cfg.getString("commands.command-accept");
		if(cfg.get("commands.command-deny") != null)
			variables.command_deny = cfg.getString("commands.command-deny");
		if(cfg.get("commands.command-ignore") != null)
			variables.command_ignore = cfg.getString("commands.command-ignore");
		if(cfg.get("commands.command-leave") != null)
			variables.command_leave = cfg.getString("commands.command-leave");
		if(cfg.get("commands.command-list") != null)
			variables.command_list = cfg.getString("commands.command-list");
		if(cfg.get("commands.command-info") != null)
			variables.command_info = cfg.getString("commands.command-info");
		if(cfg.get("commands.command-members") != null)
			variables.command_members = cfg.getString("commands.command-members");
		if(cfg.get("commands.command-home") != null)
			variables.command_home = cfg.getString("commands.command-home");
		if(cfg.get("commands.command-sethome") != null)
			variables.command_sethome = cfg.getString("commands.command-sethome");
		if(cfg.get("commands.command-desc") != null)
			variables.command_desc = cfg.getString("commands.command-desc");
		if(cfg.get("commands.command-motd") != null)
			variables.command_motd = cfg.getString("commands.command-motd");
		if(cfg.get("commands.command-chat") != null)
			variables.command_chat = cfg.getString("commands.command-chat");
		if(cfg.get("commands.command-invite") != null)
			variables.command_invite = cfg.getString("commands.command-invite");
		if(cfg.get("commands.command-prefix") != null)
			variables.command_prefix = cfg.getString("commands.command-prefix");
		if(cfg.get("commands.command-suffix") != null)
			variables.command_suffix = cfg.getString("commands.command-suffix");
		if(cfg.get("commands.command-rank") != null)
			variables.command_rank = cfg.getString("commands.command-rank");
		if(cfg.get("commands.command-kick") != null)
			variables.command_kick = cfg.getString("commands.command-kick");
		if(cfg.get("commands.command-delete") != null)
			variables.command_delete = cfg.getString("commands.command-delete");
		if(cfg.get("commands.command-rename") != null)
			variables.command_rename = cfg.getString("commands.command-rename");
		if(cfg.get("commands.command-rank") != null)
			variables.command_rank = cfg.getString("commands.command-rank");
		if(cfg.get("commands.command-silent") != null)
			variables.command_silent = cfg.getString("commands.command-silent");
		if(cfg.get("commands.command-leader") != null)
			variables.command_leader = cfg.getString("commands.command-leader");
		if(cfg.get("commands.command-spy") != null)
			variables.command_spy = cfg.getString("commands.command-spy");
		if(cfg.get("commands.command-reload") != null)
			variables.command_reload = cfg.getString("commands.command-reload");
		if(cfg.get("commands.sub-command-on") != null)
			variables.command_sub_on = cfg.getString("commands.sub-command-on");
		if(cfg.get("commands.sub-command-off") != null)
			variables.command_sub_off = cfg.getString("commands.sub-command-off");
		if(cfg.get("commands.command-migrate") != null)
			variables.command_migrate = cfg.getString("commands.command-migrate");
		if(cfg.get("commands.command-claim") != null)
			variables.command_claim = cfg.getString("commands.command-claim");
		if(cfg.get("commands.command-confirm") != null)
			variables.command_confirm = cfg.getString("commands.command-confirm");
		
		/*
		 * Get ranks
		 */
		boolean bypass_restart = false;
		List<Rank> ranks = new ArrayList<Rank>();
		int def = -1;
		ConfigurationSection cs = cfg.getConfigurationSection("party.ranks");
		while(!bypass_restart){
			bypass_restart = true;
			if(cs != null){
				for(String key : cs.getKeys(false)){
			    	int rank = cs.get(key+".rank") != null ? cs.getInt(key+".rank") : 1;
			    	String name = cs.get(key+".name") != null ? cs.getString(key+".name") : key;
			    	String chat = cs.get(key+".chat") != null ? cs.getString(key+".chat") : name;
			    	boolean dft = cs.get(key+".default") != null ? cs.getBoolean(key+".default") : false;
			    	List<String> perm = cs.get(key+".permissions") != null ? cs.getStringList(key+".permissions") : new ArrayList<String>();
			    	Rank newRank = new Rank(rank, name, chat, dft, perm);
			    	ranks.add(newRank);
			    	if(dft)
			    		def = newRank.getLevel();
			    }
			    for(int c=0; c<(ranks.size()-1) ; c++){
			    	if(ranks.get(c).getLevel() > ranks.get(c+1).getLevel()){
			    		Collections.swap(ranks, c, c+1);
			    		c=-1;
			    	}
			    }
			    if(def==-1){
			    	try{
			    		def = ranks.get(0).getLevel();
			    		plugin.log(ConsoleColors.RED.getCode() + "Cannot find default rank, setted: " + ranks.get(0).getName());
			    		LogHandler.log(1, "Cannot find default rank, setted: " + ranks.get(0).getName());
			    	} catch(IndexOutOfBoundsException ex){
			    		plugin.log(ConsoleColors.RED.getCode() + "Cannot find ranks! Restoring default ranks!");
			    		LogHandler.log(1, "Cannot find ranks! Restoring default ranks!")
			    		;
			    		cs = YamlConfiguration.loadConfiguration(plugin.getResource("config.yml")).getConfigurationSection("party.ranks");
						ranks = new ArrayList<Rank>();
						def = -1;
						bypass_restart = false;
			    	}
			    }
			    if(bypass_restart){
				    Variables.rank_list = (ArrayList<Rank>) ranks;
				    Variables.rank_default = def;
				    Variables.rank_last = ranks.get(ranks.size()-1).getLevel();
				    bypass_restart = true;
			    }
			} else {
				plugin.log(ConsoleColors.RED.getCode() + "Cannot find section party.ranks! Restoring default ranks!");
				LogHandler.log(1, "Cannot find section party.ranks! Restoring default ranks!");
				
				cs = YamlConfiguration.loadConfiguration(plugin.getResource("config.yml")).getConfigurationSection("party.ranks");
				ranks = new ArrayList<Rank>();
				def = -1;
				bypass_restart = false;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void reloadMessages(){
		File msgFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!msgFile.exists()){
			plugin.saveResource("messages.yml", true);
		}
		FileConfiguration msg = YamlConfiguration.loadConfiguration(msgFile);
		messages.loadDefaults();
		
		if(msg.getInt("dont-edit-this.messages-version") < plugin.getMessagesVersion()){
			plugin.log(ConsoleColors.RED.getCode() + "Messages file outdated");
			LogHandler.log(1, "Messages file outdated");
		}
		
		
		if(msg.get("no-permission") != null)
			messages.nopermission = msg.getString("no-permission");
		if(msg.get("no-permission-in-party") != null)
			messages.nopermission_party = msg.getString("no-permission-in-party");
		if(msg.get("invalid-command") != null)
			messages.invalidcommand = msg.getString("invalid-command");
		if(msg.get("cant-hit-mates") != null)
			messages.canthitmates = msg.getString("cant-hit-mates");
		if(msg.get("warn-on-hit-leaders") != null)
			messages.warnondamage = msg.getString("warn-on-hit-leaders");
		if(msg.get("no-party") != null)
			messages.noparty = msg.getString("no-party");
		if(msg.get("configuration-reloaded") != null)
			messages.configurationreloaded = msg.getString("configuration-reloaded");
		if(msg.get("update-available") != null)
			messages.availableupdate = msg.getString("update-available");
		if(msg.get("exp-gained") != null)
			messages.expgain = msg.getString("exp-gained");
		if(msg.get("exp-gained-from-other") != null)
			messages.expgainother = msg.getString("exp-gained-from-other");

		if(msg.get("p.wrong-command") != null)
			messages.p_wrongcmd = msg.getString("p.wrong-command");
		
		if(msg.get("create.party-created") != null)
			messages.create_partycreated = msg.getString("create.party-created");
		if(msg.get("create.name-already-exist") != null)
			messages.create_alreadyexist = msg.getString("create.name-already-exist");
		if(msg.get("create.already-in-party") != null)
			messages.create_alreadyinparty = msg.getString("create.already-in-party");
		if(msg.get("create.too-long-name") != null)
			messages.create_toolongname = msg.getString("create.too-long-name");
		if(msg.get("create.too-short-name") != null)
			messages.create_tooshortname = msg.getString("create.too-short-name");
		if(msg.get("create.invalid-name") != null)
			messages.create_invalidname = msg.getString("create.invalid-name");
		if( msg.getString("create.censored-name") != null)
			messages.create_censoredname = msg.getString("create.censored-name");
		if(msg.get("create.wrong-command") != null)
			messages.create_wrongcmd = msg.getString("create.wrong-command");
		
		if(msg.get("accept.welcome-player") != null)
			messages.accept_welcomeplayer = msg.getString("accept.welcome-player");
		if(msg.get("accept.player-joined") != null)
			messages.accept_playerjoined = msg.getString("accept.player-joined");
		if(msg.get("accept.invite-accepted") != null)
			messages.accept_inviteaccepted = msg.getString("accept.invite-accepted");
		if(msg.get("accept.accepted") != null)
			messages.accept_accepted = msg.getString("accept.accepted");
		if(msg.get("accept.max-player-reached") != null)
			messages.accept_maxplayers = msg.getString("accept.max-player-reached");
		if(msg.get("accept.already-in-party") != null)
			messages.accept_alreadyinparty = msg.getString("accept.already-in-party");
		if(msg.get("accept.no-invite") != null)
			messages.accept_noinvite = msg.getString("accept.no-invite");
		if(msg.get("accept.no-exist") != null)
			messages.accept_noexist = msg.getString("accept.no-exist");
		
		if(msg.get("deny.invite-denied") != null)
			messages.deny_invitedenied = msg.getString("deny.invite-denied");
		if(msg.get("deny.denied") != null)
				messages.deny_denied = msg.getString("deny.denied");
		if(msg.get("deny.no-invite") != null)
			messages.deny_noinvite = msg.getString("deny.no-invite");
		if(msg.get("deny.no-exist") != null)
			messages.deny_noexist = msg.getString("deny.no-exist");

		if(msg.get("ignore.header") != null)
			messages.ignore_header = msg.getString("ignore.header");
		if(msg.get("ignore.list-ignored-color") != null)
			messages.ignore_color = msg.getString("ignore.list-ignored-color");
		if(msg.get("ignore.list-ignored-separator") != null)
			messages.ignore_separator = msg.getString("ignore.list-ignored-separator");
		if(msg.get("ignore.list-ignored-empty") != null)
			messages.ignore_empty = msg.getString("ignore.list-ignored-empty");
		if(msg.get("ignore.party-ignored") != null)
			messages.ignore_ignored = msg.getString("ignore.party-ignored");
		if(msg.get("ignore.party-deignored") != null)
			messages.ignore_deignored = msg.getString("ignore.party-deignored");
		if(msg.get("ignore.no-exist") != null)
			messages.ignore_noexist = msg.getString("ignore.no-exist");
		if(msg.get("ignore.wrong-command") != null)
			messages.ignore_wrongcmd = msg.getString("ignore.wrong-command");

		if(msg.get("leave.bye-player") != null)
			messages.leave_byeplayer = msg.getString("leave.bye-player");
		if(msg.get("leave.player-leaved-from-party") != null)
			messages.leave_playerleaved = msg.getString("leave.player-leaved-from-party");
		if(msg.get("leave.party-disbanded") != null)
			messages.leave_disbanded = msg.getString("leave.party-disbanded");

		if(msg.get("info.content") != null)
			messages.info_content = msg.getStringList("info.content");
		if(msg.get("info.player-separator") != null)
			messages.info_separator = msg.getString("info.player-separator");
		if(msg.get("info.player-online-prefix") != null)
			messages.info_online = msg.getString("info.player-online-prefix");
		if(msg.get("info.player-offline-prefix") != null)
			messages.info_offline = msg.getString("info.player-offline-prefix");
		if(msg.get("info.player-empty") != null)
			messages.info_empty = msg.getString("info.player-empty");
		if(msg.get("info.missing") != null)
			messages.info_missing = msg.getString("info.missing");
		if(msg.get("info.no-exist") != null)
			messages.info_noexist = msg.getString("info.no-exist");
		if(msg.get("info.wrong-command") != null)
			messages.info_wrongcmd = msg.getString("info.wrong-command");
		
		if(msg.get("members.content") != null)
			messages.members_content = msg.getStringList("members.content");
		if(msg.get("members.player-separator") != null)
			messages.members_separator = msg.getString("members.player-separator");
		if(msg.get("members.player-online-prefix") != null)
			messages.members_online = msg.getString("members.player-online-prefix");
		if(msg.get("members.player-offline-prefix") != null)
			messages.members_offline = msg.getString("members.player-offline-prefix");
		if(msg.get("members.someone-player") != null)
			messages.members_someone = msg.getString("members.someone-player");
		if(msg.get("members.player-empty") != null)
			messages.members_empty = msg.getString("members.player-empty");
		if(msg.get("members.no-exist") != null)
			messages.members_noexist = msg.getString("members.no-exist");
		if(msg.get("members.wrong-command") != null)
			messages.members_wrongcmd = msg.getString("members.wrong-command");
		
		if(msg.get("desc.changed") != null)
			messages.desc_changed = msg.getString("desc.changed");
		if(msg.get("desc.removed") != null)
			messages.desc_removed = msg.getString("desc.removed");
		if(msg.get("desc.invalid-chars") != null)
			messages.desc_invalidchars = msg.getString("desc.invalid-chars");
		if(msg.get("desc.censored-name") != null)
			messages.desc_censored = msg.getString("desc.censored-name");
		if(msg.get("desc.wrong-command") != null)
			messages.desc_wrongcmd = msg.getString("desc.wrong-command");
		
		if(msg.get("motd.changed") != null)
			messages.motd_changed = msg.getString("motd.changed");
		if(msg.get("motd.removed") != null)
			messages.motd_removed = msg.getString("motd.removed");
		if(msg.get("motd.header") != null)
			messages.motd_header = msg.getString("motd.header");
		if(msg.get("motd.color-motd") != null)
			messages.motd_color = msg.getString("motd.color-motd");
		if(msg.get("motd.footer") != null)
			messages.motd_footer = msg.getString("motd.footer");
		if(msg.get("motd.invalid-chars") != null)
			messages.motd_invalidchars = msg.getString("motd.invalid-chars");
		if(msg.get("motd.censored-name") != null)
			messages.motd_censored = msg.getString("motd.censored-name");
		if(msg.get("motd.wrong-command") != null)
			messages.motd_wrongcmd = msg.getString("motd.wrong-command");

		if(msg.get("chat.enabled") != null)
			messages.chat_enabled = msg.getString("chat.enabled");
		if(msg.get("chat.disabled") != null)
			messages.chat_disabled = msg.getString("chat.disabled");
		if(msg.get("chat.wrong-command") != null)
			messages.chat_wrongcmd = msg.getString("chat.wrong-command");
		
		if(msg.get("list.header") != null)
			messages.list_header = msg.getString("list.header");
		if(msg.get("list.sub-header") != null)
			messages.list_subheader = msg.getString("list.sub-header");
		if(msg.get("list.no-one-online") != null)
			messages.list_offline = msg.getString("list.no-one-online");
		if(msg.get("list.format-party") != null)
			messages.list_formatparty = msg.getString("list.format-party");
		if(msg.get("list.footer") != null)
			messages.list_footer = msg.getString("list.footer");
		if(msg.get("list.wrong-command") != null)
			messages.list_wrongcmd = msg.getString("list.wrong-command");

		if(msg.get("invite.only-online") != null)
			messages.invite_onlyonline = msg.getString("invite.only-online");
		if(msg.get("invite.max-player-reached") != null)
			messages.invite_maxplayers = msg.getString("invite.max-player-reached");
		if(msg.get("invite.already-party") != null)
			messages.invite_alreadyparty = msg.getString("invite.already-party");
		if(msg.get("invite.already-invited") != null)
			messages.invite_alreadyinvited = msg.getString("invite.already-invited");
		if(msg.get("invite.no-permission-player") != null)
			messages.invite_playernopex = msg.getString("invite.no-permission-player");
		if(msg.get("invite.invite-send") != null)
			messages.invite_send = msg.getString("invite.invite-send");
		if(msg.get("invite.invite-rec") != null)
			messages.invite_rec = msg.getString("invite.invite-rec");
		if(msg.get("invite.invite-timeout") != null)
			messages.invite_timeout = msg.getString("invite.invite-timeout");
		if(msg.get("invite.invite-noresponse") != null)
			messages.invite_noresponse = msg.getString("invite.invite-noresponse");
		if(msg.get("invite.invite-revoked-send") != null)
			messages.invite_revoked_send = msg.getString("invite.invite-revoked-send");
		if(msg.get("invite.invite-revoked-rec") != null)
			messages.invite_revoked_rec = msg.getString("invite.invite-revoked-rec");
		if(msg.get("invite.wrong-command") != null)
			messages.invite_wrongcmd = msg.getString("invite.wrong-command");

		if(msg.get("prefix.changed") != null)
			messages.prefix_changed = msg.getString("prefix.changed");
		if(msg.get("prefix.removed") != null)
			messages.prefix_removed = msg.getString("prefix.removed");
		if(msg.get("prefix.invalid-chars") != null)
			messages.prefix_invalidchars = msg.getString("prefix.invalid-chars");
		if(msg.get("prefix.censored-name") != null)
			messages.prefix_censored = msg.getString("prefix.censored-name");
		if(msg.get("prefix.wrong-command") != null)
			messages.prefix_wrongcmd = msg.getString("prefix.wrong-command");

		if(msg.get("suffix.changed") != null)
			messages.suffix_changed = msg.getString("suffix.changed");
		if(msg.get("suffix.removed") != null)
			messages.suffix_removed = msg.getString("suffix.removed");
		if(msg.get("suffix.invalid-chars") != null)
			messages.suffix_invalidchars = msg.getString("suffix.invalid-chars");
		if(msg.get("suffix.censored-name") != null)
			messages.suffix_censored = msg.getString("suffix.censored-name");
		if(msg.get("suffix.wrong-command") != null)
			messages.suffix_wrongcmd = msg.getString("suffix.wrong-command");

		if(msg.get("kick.kick-send") != null)
			messages.kick_kicksend = msg.getString("kick.kick-send");
		if(msg.get("kick.kick-up-rank") != null)
			messages.kick_uprank = msg.getString("kick.kick-up-rank");
		if(msg.get("kick.kicked-from-party") != null)
			messages.kick_kickedfrom = msg.getString("kick.kicked-from-party");
		if(msg.get("kick.kicked-player-party") != null)
			messages.kick_kickedplayer = msg.getString("kick.kicked-player-party");
		if(msg.get("kick.no-member") != null)
			messages.kick_nomember = msg.getString("kick.no-member");
		if(msg.get("kick.wrong-command") != null)
			messages.kick_wrongcmd = msg.getString("kick.wrong-command");

		if(msg.get("delete.deleted") != null)
			messages.delete_deleted = msg.getString("delete.deleted");
		if(msg.get("delete.silent-delete") != null)
			messages.delete_deleted_silent = msg.getString("delete.silent-delete");
		if(msg.get("delete.warn") != null)
			messages.delete_warn = msg.getString("delete.warn");
		if(msg.get("delete.no-exist") != null)
			messages.delete_noexist = msg.getString("delete.no-exist");
		if(msg.get("delete.wrong-command") != null)
			messages.delete_wrongcmd = msg.getString("delete.wrong-command");
		
		if(msg.get("rename.renamed") != null)
			messages.rename_renamed = msg.getString("rename.renamed");
		if(msg.get("rename.broadcast-party") != null)
			messages.rename_broadcast = msg.getString("rename.broadcast-party");
		if(msg.get("rename.no-exist") != null)
			messages.rename_noexist = msg.getString("rename.no-exist");
		if(msg.get("rename.wrong-command") != null)
			messages.rename_wrongcmd = msg.getString("rename.wrong-command");

		if(msg.get("rank.promoted") != null)
			messages.rank_promoted = msg.getString("rank.promoted");
		if(msg.get("rank.no-member") != null)
			messages.rank_nomember = msg.getString("rank.no-member");
		if(msg.get("rank.no-party") != null)
			messages.rank_noparty = msg.getString("rank.no-party");
		if(msg.get("rank.wrong-rank") != null)
			messages.rank_wrongrank = msg.getString("rank.wrong-rank");
		if(msg.get("rank.already-rank") != null)
			messages.rank_alreadyrank = msg.getString("rank.already-rank");
		if(msg.get("rank.low-rank") != null)
			messages.rank_lowrank = msg.getString("rank.low-rank");
		if(msg.get("rank.to-higher-rank") != null)
			messages.rank_tohigherrank = msg.getString("rank.to-higher-rank");
		if(msg.get("rank.no-degrade-yourself") != null)
			messages.rank_nodegradeyourself = msg.getString("rank.no-degrade-yourself");
		if(msg.get("rank.no-promote-yourself") != null)
			messages.rank_nopromoteyourself = msg.getString("rank.no-promote-yourself");
		if(msg.get("rank.wrong-command") != null)
			messages.rank_wrongcmd = msg.getString("rank.wrong-command");
		
		if(msg.get("sethome.setted-broadcast") != null)
			messages.sethome_setted = msg.getString("sethome.setted-broadcast");
		if(msg.get("sethome.wrong-command") != null)
			messages.sethome_wrongcmd = msg.getString("sethome.wrong-command");
		
		if(msg.get("home.teleported") != null)
			messages.home_teleported = msg.getString("home.teleported");
		if(msg.get("home.teleport-in") != null)
			messages.home_in = msg.getString("home.teleport-in");
		if(msg.get("home.teleport-denied") != null)
			messages.home_denied = msg.getString("home.teleport-denied");
		if(msg.get("home.no-home") != null)
			messages.home_nohome = msg.getString("home.no-home");
		if(msg.get("home.no-exist-party") != null)
			messages.home_noexist = msg.getString("home.no-exist-party");
		if(msg.get("home.wrong-command") != null)
			messages.home_wrongcmd = msg.getString("home.wrong-command");
		
		if(msg.get("claim.done") != null)
			messages.claim_done = msg.getString("claim.done");
		if(msg.get("claim.removed") != null)
			messages.claim_removed = msg.getString("claim.removed");
		if(msg.get("claim.no-manager") != null)
			messages.claim_nomanager = msg.getString("claim.no-manager");
		if(msg.get("claim.no-exist-claim") != null)
			messages.claim_noexistclaim = msg.getString("claim.no-exist-claim");
		if(msg.get("claim.wrong-command") != null)
			messages.claim_wrongcmd = msg.getString("claim.wrong-command");
		
		if(msg.get("spy.activated") != null)
			messages.spy_active = msg.getString("spy.activated");
		if(msg.get("spy.disabled") != null)
			messages.spy_disable = msg.getString("spy.disabled");
		
		if(msg.get("database.to-file") != null)
			messages.database_tofile = msg.getString("database.to-file");
		if(msg.get("database.to-sql") != null)
			messages.database_tosql = msg.getString("database.to-sql");
		if(msg.get("database.sql-offline") != null)
			messages.database_offlinesql = msg.getString("database.sql-offline");
		if(msg.get("database.none") != null)
			messages.database_none = msg.getString("database.none");
		if(msg.get("database.wrong-command") != null)
			messages.database_wrongcmd = msg.getString("database.wrong-command");
		
		if(msg.get("vault.create-nomoney") != null)
			messages.vault_create_nomoney = msg.getString("vault.create-nomoney");
		if(msg.get("vault.home-nomoney") != null)
			messages.vault_home_nomoney = msg.getString("vault.home-nomoney");
		if(msg.get("vault.sethome-nomoney") != null)
			messages.vault_sethome_nomoney = msg.getString("vault.sethome-nomoney");
		if(msg.get("vault.desc-nomoney") != null)
			messages.vault_desc_nomoney = msg.getString("vault.desc-nomoney");
		if(msg.get("vault.motd-nomoney") != null)
			messages.vault_motd_nomoney = msg.getString("vault.motd-nomoney");
		if(msg.get("vault.prefix-nomoney") != null)
			messages.vault_prefix_nomoney = msg.getString("vault.prefix-nomoney");
		if(msg.get("vault.suffix-nomoney") != null)
			messages.vault_suffix_nomoney = msg.getString("vault.suffix-nomoney");
		if(msg.get("vault.confirm.any-cmd") != null)
			messages.vault_confirm_anycmd = msg.getString("vault.confirm.any-cmd");
		if(msg.get("vault.confirm.warn-onbuy") != null)
			messages.vault_confirm_warnonbuy = msg.getString("vault.confirm.warn-onbuy");
		if(msg.get("vault.confirm.confirmed") != null)
			messages.vault_confirm_confirmed = msg.getString("vault.confirm.confirmed");
		if(msg.get("vault.confirm.wrong-command") != null)
			messages.vault_confirm_wrongcmd = msg.getString("vault.confirm.wrong-command");
		
		
		
		if(msg.get("follow.following-world") != null)
			messages.follow_following_world = msg.getString("follow.following-world");
		if(msg.get("follow.following-server") != null)
			messages.follow_following_server = msg.getString("follow.following-server");
		
		if(msg.get("help.header") != null)
			messages.help_header = msg.getString("help.header");
		if(msg.get("help.help") != null)
			messages.help_help = msg.getString("help.help");
		if(msg.get("help.p") != null)
			messages.help_p = msg.getString("help.p");
		if(msg.get("help.create") != null)
			messages.help_create = msg.getString("help.create");
		if(msg.get("help.join") != null)
			messages.help_join = msg.getString("help.join");
		if(msg.get("help.accept") != null)
			messages.help_accept = msg.getString("help.accept");
		if(msg.get("help.deny") != null)
			messages.help_deny = msg.getString("help.deny");
		if(msg.get("help.ignore") != null)
			messages.help_ignore = msg.getString("help.ignore");
		if(msg.get("help.leave") != null)
			messages.help_leave = msg.getString("help.leave");
		if(msg.get("help.info") != null)
			messages.help_info = msg.getString("help.info");
		if(msg.get("help.members") != null)
			messages.help_members = msg.getString("help.members");
		if(msg.get("help.password") != null)
			messages.help_password = msg.getString("help.password");
		if(msg.get("help.rank") != null)
			messages.help_rank = msg.getString("help.rank");
		if(msg.get("help.home") != null)
			messages.help_home = msg.getString("help.home");
		if(msg.get("help.home-others") != null)
			messages.help_home_others = msg.getString("help.home-others");
		if(msg.get("help.sethome") != null)
			messages.help_sethome = msg.getString("help.sethome");
		if(msg.get("help.desc") != null)
			messages.help_desc = msg.getString("help.desc");
		if(msg.get("help.motd") != null)
			messages.help_motd = msg.getString("help.motd");
		if(msg.get("help.chat") != null)
			messages.help_chat = msg.getString("help.chat");
		if(msg.get("help.list") != null)
			messages.help_list = msg.getString("help.list");
		if(msg.get("help.invite") != null)
			messages.help_invite = msg.getString("help.invite");
		if(msg.get("help.prefix") != null)
			messages.help_prefix = msg.getString("help.prefix");
		if(msg.get("help.suffix") != null)
			messages.help_suffix = msg.getString("help.suffix");
		if(msg.get("help.kick") != null)
			messages.help_kick = msg.getString("help.kick");
		if(msg.get("help.spy") != null)
			messages.help_spy = msg.getString("help.spy");
		if(msg.get("help.delete") != null)
			messages.help_delete = msg.getString("help.delete");
		if(msg.get("help.rename") != null)
			messages.help_rename = msg.getString("help.rename");
		if(msg.get("help.reload") != null)
			messages.help_reload = msg.getString("help.reload");
		if(msg.get("help.migrate") != null)
			messages.help_migrate = msg.getString("help.migrate");
		if(msg.get("help.claim") != null)
			messages.help_claim = msg.getString("help.claim");
	}
	public void reloadData(){
		data = new Data(this, false);
	}
	
	public Parties getMain(){
		return plugin;
	}
	public Messages getMessages(){
		return messages;
	}
	public Variables getVariables(){
		return variables;
	}
	public Data getData(){
		return data;
	}
}
