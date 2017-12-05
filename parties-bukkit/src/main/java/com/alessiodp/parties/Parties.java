package com.alessiodp.parties;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.configuration.storage.DatabaseDispatcher;
import com.alessiodp.parties.events.*;
import com.alessiodp.parties.commands.*;
import com.alessiodp.parties.handlers.*;
import com.alessiodp.parties.utils.BaseCommand;
import com.alessiodp.parties.utils.PartiesTabCompleter;
import com.alessiodp.parties.utils.PasswordFilter;
import com.alessiodp.parties.utils.addon.*;
import com.alessiodp.parties.utils.api.ApiHandler;
import com.alessiodp.parties.utils.bungeecord.BukkitHandler;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.StorageType;
import com.alessiodp.parties.utils.enums.LogLevel;

public class Parties extends JavaPlugin {
	private static Parties instance;
	
	private ConfigHandler			config;
	private PlayerHandler			player;
	private PartyHandler			party;
	private LibraryHandler 			library;
	private DatabaseDispatcher		databaseDispatcher;
	private StorageType				databaseType;
	private StorageType				logType;

	private final int				curseProject = 90889;
	private final int				configVersion = 16;
	private final int				messageVersion = 14;
	private final String			scoreboardprefix = "PARTY";
	private final static boolean	isDebug = false;
	
	private String					updaterNewVersion = "";

	/* Addons */
	private GriefPreventionHandler	addonGriefPrevention;
	private boolean					addonPlaceholderAPI = false;
	private DynmapHandler			addonDynmap;
	private Economy					addonVaultEconomy = null;
	
	
	public static Parties getInstance() {return instance;}
	
	@Override
	public void onEnable() {
		/* init */
		instance = this;
		log(ConsoleColors.CYAN.getCode() + "Initializing Parties " + getDescription().getVersion());
		
		handle();
		
		LogHandler.log(LogLevel.BASE, "Parties v" + getDescription().getVersion() + " enabled", true, ConsoleColors.CYAN);
	}
	
	@Override
	public void onDisable() {
		if (databaseDispatcher != null) {
			// This is not a force close
			getDatabaseDispatcher().stop();
			resetPendingPartyTask();
		}
		log(ConsoleColors.CYAN.getCode() + "Parties disabled");
		LogHandler.log(LogLevel.BASE, "========== Parties disabled - End of Log ==========", false);
	}
	
	private void checkUpdates() {
		// Examples: 1.6.4, 1.8.3, 1.9.0, 1.9.0-DEV(ignored from GravityUpdater), ...
		GravityUpdater updater;
		if (Variables.downloadupdates) {
			updater = new GravityUpdater(this, curseProject, this.getFile(), GravityUpdater.UpdateType.DEFAULT, false);
		} else {
			updater = new GravityUpdater(this, curseProject, this.getFile(), GravityUpdater.UpdateType.NO_DOWNLOAD, false);
		}
		if (updater.getResult() == GravityUpdater.UpdateResult.UPDATE_AVAILABLE) {
			String[] split = updater.getLatestName().split(GravityUpdater.DELIMETER);
			String newVersion = split[split.length - 1];
			String currentVersion = getDescription().getVersion();
			// Splitting -DEV, ...
			if (newVersion.contains("-"))
				newVersion = newVersion.split("-")[0];
			if (currentVersion.contains("-"))
				currentVersion = currentVersion.split("-")[0];
			// Splitting dots
			String[] splitNewVersion = newVersion.split("\\.");
			String[] splitCurrentVersion = currentVersion.split("\\.");
			// Begin matching
			boolean foundMatch = false;
			try {
				for (int c=0; c < splitNewVersion.length && !foundMatch; c++) {
					int a = Integer.parseInt(splitNewVersion[c]);
					int b = c < splitCurrentVersion.length ? Integer.parseInt(splitCurrentVersion[c]) : 0;
					if (a > b)
						foundMatch = true;
					else if (a < b)
						break;
				}
			} catch (Exception ex) {
				foundMatch = true;
			}
			if (foundMatch) {
				updaterNewVersion = split[split.length - 1];
				LogHandler.log(LogLevel.BASE, "Parties v" + getDescription().getVersion() + " found a new version: " + updaterNewVersion, true, ConsoleColors.CYAN);
				getPlayerHandler().alertNewVersion();
			}
		}
	}
	
	private void handle() {
		com.alessiodp.partiesapi.Parties.setApi(new ApiHandler(this));
		config = new ConfigHandler(this);
		new LogHandler(this);
		library = new LibraryHandler(this);
		databaseDispatcher = new DatabaseDispatcher(this);
		
		party = new PartyHandler(this);
		player = new PlayerHandler(this);
		player.init();
		
		registerListeners();
		
		registerCommands();
		
		handleAddons();
		
		registerMetrics();
		
		// Updater task
		getServer().getScheduler().runTaskTimer(this, new Runnable() {
			public void run() {
				checkUpdates();
			}
		}, 20, 28800); // Timer of 24 hours (28800)
		
		if (isDebug) {
			// Debug task
			getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
				public void run() {
					int players = getPlayerHandler().getListPlayers().size();
					String parties = getPartyHandler().getListParties().keySet().toString();
					debugLog("Entities = Players: " + Integer.toString(players) + " - Parties: " + parties);
				}
			}, 200, 300);
		}
	}
	
	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ChatListener(this), this);
		pm.registerEvents(new FightListener(this), this);
		pm.registerEvents(new FollowListener(this), this);
		pm.registerEvents(new JoinLeaveListener(this), this);
		pm.registerEvents(new MoveListener(this), this);
	}
	
	private void registerCommands() {
		CommandsHandler handler = new CommandsHandler(this);
		handler.register(Variables.command_chat, new CommandChat(this));
		handler.register(Variables.command_create, new CommandCreate(this));
		handler.register(Variables.command_rank, new CommandRank(this));
		handler.register(Variables.command_list, new CommandList(this));
		handler.register(Variables.command_help, new CommandHelp(this));
		handler.register(Variables.command_invite, new CommandInvite(this));
		handler.register(Variables.command_accept, new CommandAccept(this));
		handler.register(Variables.command_deny, new CommandDeny(this));
		handler.register(Variables.command_ignore, new CommandIgnore(this));
		handler.register(Variables.command_kick, new CommandKick(this));
		handler.register(Variables.command_leave, new CommandLeave(this));
		handler.register(Variables.command_info, new CommandInfo(this));
		handler.register(Variables.command_members, new CommandMembers(this));
		handler.register(Variables.command_home, new CommandHome(this));
		handler.register(Variables.command_sethome, new CommandSetHome(this));
		handler.register(Variables.command_desc, new CommandDesc(this));
		handler.register(Variables.command_motd, new CommandMotd(this));
		handler.register(Variables.command_p, new CommandP(this));
		handler.register(Variables.command_party, new CommandParty(this));
		handler.register(Variables.command_reload, new CommandReload(this));
		handler.register(Variables.command_spy, new CommandSpy(this));
		handler.register(Variables.command_migrate, new CommandMigrate(this));
		handler.register(Variables.command_delete, new CommandDelete(this));
		handler.register(Variables.command_rename, new CommandRename(this));
		
		/* Vault */
		if (Variables.vault_enable && Variables.vault_confirm_enable)
			handler.register(Variables.command_confirm, new CommandConfirm(this));
		/* GriefPrevention */
		if (Variables.griefprevention_enable)
			handler.register(Variables.command_claim, new CommandClaim(this));
		/* Password system */
		if (Variables.password_enable) {
			handler.register(Variables.command_join, new CommandJoin(this));
			handler.register(Variables.command_password, new CommandPassword(this));
			((org.apache.logging.log4j.core.Logger)LogManager.getRootLogger()).addFilter(new PasswordFilter());
		}
		/* Color system */
		if (Variables.color_enable) {
			handler.register(Variables.command_color, new CommandColor(this));
		}
		/* Tag system */
		if (Variables.tag_enable && !Variables.tag_system) {
			if (Variables.tag_custom_prefix)
				handler.register(Variables.command_prefix, new CommandPrefix(this));
			if (Variables.tag_custom_suffix)
				handler.register(Variables.command_suffix, new CommandSuffix(this));
		}
		/* Teleport system */
		if (Variables.teleport_enable)
			handler.register(Variables.command_teleport, new CommandTeleport(this));
		
		/* Setup */
		try {
			BaseCommand cmdParty = new BaseCommand(Variables.command_party);
			BaseCommand cmdP = new BaseCommand(Variables.command_p);
			cmdParty.setDescription(Variables.command_party_desc);
			cmdP.setDescription(Variables.command_p_desc);

			final Field bukkitCommandMap = Bukkit.getServer().getClass()
					.getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit
					.getServer());

			commandMap.register(Variables.command_party, cmdParty);
			commandMap.register(Variables.command_p, cmdP);
			
			cmdParty.setExecutor(handler);
			if (Variables.commandtab)
				cmdParty.setTabCompleter(new PartiesTabCompleter(this));
			cmdP.setExecutor(handler);
			
			bukkitCommandMap.setAccessible(false);
			if (getServer().getPluginCommand(Variables.command_party) != null && !getServer().getPluginCommand(Variables.command_party).getPlugin().getName().equals("Parties")) {
				getServer().getPluginCommand(Variables.command_party).setExecutor(handler);
				if (Variables.commandtab)
					getServer().getPluginCommand(Variables.command_party).setTabCompleter(new PartiesTabCompleter(this));
			}
			if (getServer().getPluginCommand(Variables.command_p) != null && !getServer().getPluginCommand(Variables.command_p).getPlugin().getName().equals("Parties"))
				getServer().getPluginCommand(Variables.command_p).setExecutor(handler);
			
			LogHandler.log(LogLevel.DEBUG, "All commands mapped", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleAddons() {
		/* Bungeecoord */
		if (Variables.bungeecord) {
			new BukkitHandler(this);
			LogHandler.log(LogLevel.BASE, "Ready for Bungeecord", true, ConsoleColors.CYAN);
		}
		/* ProtocolLib */
		if (Variables.tablist_enable) {
			ProtocolHandler protocol = new ProtocolHandler(this);
			if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib") && protocol.start()) {
				LogHandler.log(LogLevel.BASE, "ProtocolLib Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.tablist_enable = false;
				LogHandler.log(LogLevel.BASE, "Failed to hook into ProtocolLib, disabled tablist feature", true, ConsoleColors.RED);
			}
		}
		/* GriefPrevention */
		if (Variables.griefprevention_enable) {
			if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
				addonGriefPrevention = new GriefPreventionHandler(this);
				LogHandler.log(LogLevel.BASE, "GriefPrevention Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.griefprevention_enable = false;
				LogHandler.log(LogLevel.BASE, "Failed to hook into GriefPrevention, disabled claim feature", true, ConsoleColors.RED);
			}
		}
		/* BanManager */
		if (Variables.banmanager_enable) {
			if (Bukkit.getPluginManager().getPlugin("BanManager") != null) {
				new BanManagerHandler(this).register();
				LogHandler.log(LogLevel.BASE, "BanManager Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.banmanager_enable = false;
				LogHandler.log(LogLevel.BASE, "Failed to hook into BanManager, disabled banmanager features", true, ConsoleColors.RED);
			}
		}
		/* Dynmap */
		if (Variables.dynmap_enable) {
			if (Bukkit.getPluginManager().getPlugin("dynmap") != null) {
				addonDynmap = new DynmapHandler(this);
				LogHandler.log(LogLevel.BASE, "Dynmap Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.dynmap_enable = false;
				LogHandler.log(LogLevel.BASE, "Failed to hook into Dynmap, disabled dynmap features", true, ConsoleColors.RED);
			}
		}
		/* PlaceholderAPI */
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			if (new PlaceholderAPIHandler(this).hook()) {
				LogHandler.log(LogLevel.BASE, "PlaceholderAPI Hooked", true, ConsoleColors.CYAN);
				addonPlaceholderAPI = true;
			}
		}
		/* EssentialsChat */
		if (Bukkit.getPluginManager().getPlugin("EssentialsChat") != null) {
			getServer().getPluginManager().registerEvents(new EssentialsChatHandler(this), this);
			LogHandler.log(LogLevel.BASE, "EssentialsChat Hooked", true, ConsoleColors.CYAN);
		}
		/* SkillAPI */
		if (Variables.exp_enable && Variables.exp_skillapi_enable) {
			if (Bukkit.getPluginManager().isPluginEnabled("SkillAPI")) {
				new SkillAPIHandler(this);
				LogHandler.log(LogLevel.BASE, "SkillAPI Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.exp_skillapi_enable = false;
				LogHandler.log(LogLevel.BASE, "Failed to hook into SkillAPI, disabled skillapi features", true, ConsoleColors.RED);
			}
		}
		/* MythicMobs */
		/*
		 * Unsupported, WIP
		
		if (Variables.exp_enable && Variables.exp_mythicmobs_enable) {
			if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
				new MythicMobsHandler(this);
				LogHandler.log(LogLevel.BASIC, "MythicMobs Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.exp_mythicmobs_enable = false;
				LogHandler.log(LogLevel.BASIC, "Failed to hook into MythicMobs, disabled mythicmobs features", true, ConsoleColors.RED);
			}
		}
		*/
		/* Vault */
		if (Variables.vault_enable) {
			if (setupEconomy()) {
				LogHandler.log(LogLevel.BASE, "Vault Hooked", true, ConsoleColors.CYAN);
			} else {
				Variables.vault_enable = false;
				LogHandler.log(LogLevel.BASE, "Failed to hook into Vault, disabled vault features", true, ConsoleColors.RED);
			}
		}
	}
	
	private void registerMetrics() {
		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SimplePie("type_of_party_used") {
			@Override
			public String getValue() {
				if (Variables.fixedparty)
					return "Fixed";
				return "Normal";
			}
		});
		metrics.addCustomChart(new Metrics.SimplePie("type_of_database_used") {
			@Override
			public String getValue() {
				if (getDatabaseType().isNone())
					return "None";
				else if (getDatabaseType().isMySQL())
					return "MySQL";
				return "YAML";
			}
		});
		metrics.addCustomChart(new Metrics.SimplePie("exp_system") {
			@Override
			public String getValue() {
				if (Variables.exp_enable)
					return "Enabled";
				return "Disabled";
			}
		});
		metrics.addCustomChart(new Metrics.SimplePie("vault_system") {
			@Override
			public String getValue() {
				if (Variables.vault_enable)
					return "Enabled";
				return "Disabled";
			}
		});
		metrics.addCustomChart(new Metrics.SimplePie("tag_system") {
			@Override
			public String getValue() {
				if (Variables.tag_enable) {
					if (Variables.tag_system)
						return "Base";
					else
						return "Custom";
				}
				return "Disabled";
			}
		});
		metrics.addCustomChart(new Metrics.SimplePie("using_api") {
			@Override
			public String getValue() {
				if (com.alessiodp.partiesapi.Parties.isFlagHook())
					return "Yes";
				return "No";
			}
		});
	}
	
	public void reloadConfiguration() {
		resetPendingPartyTask();
		
		checkUpdates();
		config = new ConfigHandler(this);
		new LogHandler(this);
		
		party.reloadParties();
		player.reloadPlayers();
		
		registerCommands();
		handleAddons();
	}
	
	/*
	 * Log
	 */
	public void log(String msg) {
		getServer().getLogger().log(Level.INFO, "[" + ConsoleColors.CYAN.getCode() + "Parties" + ConsoleColors.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)) + ConsoleColors.RESET.getCode());
	}
	public void log(Level level, String msg) {
		getServer().getLogger().log(level, "[" + ConsoleColors.CYAN.getCode() + "Parties" + ConsoleColors.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)) + ConsoleColors.RESET.getCode());
	}
	public static void debugLog(String message) {
		if (isDebug)
			System.out.println(ConsoleColors.PURPLE.getCode() + "[Parties Debug] " + message + ConsoleColors.RESET.getCode());
	}
	
	/* Gets & Sets */
	public ConfigHandler getConfigHandler() {return config;}
	public PlayerHandler getPlayerHandler() {return player;}
	public PartyHandler getPartyHandler() {return party;}
	public LibraryHandler getLibraryHandler() {return library;}
	
	public DatabaseDispatcher getDatabaseDispatcher() {return databaseDispatcher;}
	public void setDatabaseDispatcher(DatabaseDispatcher dd) {databaseDispatcher = dd;}
	public StorageType getDatabaseType() {return databaseType;}
	public void setDatabaseType(StorageType dt) {databaseType = dt;}
	public StorageType getLogType() {return logType;}
	public void setLogType(StorageType dt) {logType = dt;}
	
	public GriefPreventionHandler getGriefPrevention() {return addonGriefPrevention;}
	public boolean isPlaceholderAPIHooked() {return addonPlaceholderAPI;}
	public DynmapHandler getDynmap() {return addonDynmap;}
	
	public int getConfigVersion() {return configVersion;}
	public int getMessagesVersion() {return messageVersion;}
	
	public boolean isUpdateAvailable() {return !updaterNewVersion.isEmpty();}
	public String getNewUpdate() {return updaterNewVersion;}
	
	public String getScoreboardPrefix() {return scoreboardprefix;}
	
	
	/*
	 * Misc
	 */
	private void resetPendingPartyTask() {
		for (BukkitTask bt : Bukkit.getScheduler().getPendingTasks()) {
			if (bt.getOwner() instanceof Parties) {
				if (party.getListPartiesToDelete().containsValue(bt.getTaskId())) {
					for (Entry<String, Integer> et : getPartyHandler().getListPartiesToDelete().entrySet()) {
						if (et.getValue() == bt.getTaskId()) {
							bt.cancel();
							party.deleteTimedParty(et.getKey(), true);
							break;
						}
					}
				}
			}
		}
	}
	
	/*
	 * Vault
	 */
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		addonVaultEconomy = rsp.getProvider();
		return addonVaultEconomy != null;
	}
	
	public Economy getEconomy() {return addonVaultEconomy;}
}
