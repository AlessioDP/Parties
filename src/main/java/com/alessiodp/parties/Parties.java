package com.alessiodp.parties;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.mcstats.Metrics;

import com.alessiodp.parties.commands.CommandAccept;
import com.alessiodp.parties.commands.CommandChat;
import com.alessiodp.parties.commands.CommandClaim;
import com.alessiodp.parties.commands.CommandConfirm;
import com.alessiodp.parties.commands.CommandCreate;
import com.alessiodp.parties.commands.CommandDelete;
import com.alessiodp.parties.commands.CommandDeny;
import com.alessiodp.parties.commands.CommandDesc;
import com.alessiodp.parties.commands.CommandHelp;
import com.alessiodp.parties.commands.CommandHome;
import com.alessiodp.parties.commands.CommandIgnore;
import com.alessiodp.parties.commands.CommandInfo;
import com.alessiodp.parties.commands.CommandInvite;
import com.alessiodp.parties.commands.CommandJoin;
import com.alessiodp.parties.commands.CommandKick;
import com.alessiodp.parties.commands.CommandLeave;
import com.alessiodp.parties.commands.CommandList;
import com.alessiodp.parties.commands.CommandMembers;
import com.alessiodp.parties.commands.CommandMigrate;
import com.alessiodp.parties.commands.CommandMotd;
import com.alessiodp.parties.commands.CommandP;
import com.alessiodp.parties.commands.CommandParty;
import com.alessiodp.parties.commands.CommandPassword;
import com.alessiodp.parties.commands.CommandPrefix;
import com.alessiodp.parties.commands.CommandRank;
import com.alessiodp.parties.commands.CommandReload;
import com.alessiodp.parties.commands.CommandRename;
import com.alessiodp.parties.commands.CommandSetHome;
import com.alessiodp.parties.commands.CommandSpy;
import com.alessiodp.parties.commands.CommandSuffix;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.CommandsHandler;
import com.alessiodp.parties.handlers.ConfigHandler;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.handlers.PartyHandler;
import com.alessiodp.parties.handlers.PlayerHandler;
import com.alessiodp.parties.utils.BaseCommand;
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.SQLDatabase;
import com.alessiodp.parties.utils.addon.BanManagerHandler;
import com.alessiodp.parties.utils.addon.DeluxeChatHandler;
import com.alessiodp.parties.utils.addon.DynmapHandler;
import com.alessiodp.parties.utils.addon.EssentialsChatHandler;
import com.alessiodp.parties.utils.addon.GravityUpdater;
import com.alessiodp.parties.utils.addon.GriefPreventionHandler;
import com.alessiodp.parties.utils.addon.PlaceholderHandler;
import com.alessiodp.parties.utils.api.PartiesAPI;
import com.alessiodp.parties.utils.bungeecord.BukkitHandler;

public class Parties extends JavaPlugin {
	private static Parties instance;
	
	private ConfigHandler config;
	private PlayerHandler player;
	private PartyHandler party;
	private SQLDatabase sqldatabase;

	private static final int ver_config = 10;
	private static final int ver_mess = 9;
	private static final String scoreboardprefix = "PARTY";
	
	/* Updates variables */
	private boolean update_avail = false;
	private String update_newver = "";

	/* Addons */
	private boolean addon_PEX = false;
	private boolean addon_GM = false;
	private GriefPreventionHandler addon_GP;
	private DeluxeChatHandler addon_DC;
	private boolean addon_PlhAPI = false;
	private DynmapHandler addon_Dynmap;
	private static Economy addon_VEcon = null;
	private static Chat addon_VChat = null;
	
	
	public static Parties getInstance() {return instance;}

	@Override
	public void onEnable() {
		/* init */
		instance = this;
		log(ConsoleColors.CYAN.getCode() + "Initializing Parties " + this.getDescription().getVersion());
		
		handle();
		
		log(ConsoleColors.CYAN.getCode() + "Parties enabled");
		LogHandler.log(1, "Parties v"+getDescription().getVersion()+" enabled");
	}

	@Override
	public void onDisable() {
		if(addon_DC != null)
			addon_DC.disable();
		resetPendingPartyTask();
		log(ConsoleColors.CYAN.getCode() + "Parties disabled");
		LogHandler.log(1, "Parties disabled");
	}
	
	public void checkUpdates() {
		GravityUpdater updater;
		if(Variables.downloadupdates){
			updater = new GravityUpdater(this, 90889, this.getFile(), GravityUpdater.UpdateType.DEFAULT, false);
		} else {
			updater = new GravityUpdater(this, 90889, this.getFile(), GravityUpdater.UpdateType.NO_DOWNLOAD, false);
		}
		update_avail = updater.getResult() == GravityUpdater.UpdateResult.UPDATE_AVAILABLE;
		if(update_avail) {
			update_newver = updater.getLatestName().split("v")[1];
			LogHandler.log(1, "Parties v" + getDescription().getVersion() + " found new update: " + update_newver);
			log(ConsoleColors.CYAN.getCode() + "Parties v" + getDescription().getVersion() + " found new update: " + update_newver);
		}
	}
	private void handle() {
		new PartiesAPI();
		config = new ConfigHandler(this);
		new LogHandler(this);
		party = new PartyHandler(this);
		checkUpdates();
		player = new PlayerHandler(this);
		
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		registerCommands();
		
		handleAddons();
	}
	
	private void handleAddons(){
		/* Bungeecoord */
		if(Variables.bungeecord){
			new BukkitHandler(this);
			log(ConsoleColors.CYAN.getCode() + "Ready for Bungeecord!");
			LogHandler.log(1, "Ready for Bungeecord");
		}
		/* PEX */
		if (Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")
				|| Bukkit.getPluginManager().isPluginEnabled("PermissionsEX")) {
			addon_PEX = true;
			log(ConsoleColors.CYAN.getCode() + "PEX Hooked!");
			LogHandler.log(1, "PEX Hooked");
		/* GroupManager */
		}  else if(Bukkit.getPluginManager().getPlugin("GroupManager") != null){
			addon_GM = true;
			log(ConsoleColors.CYAN.getCode() + "GroupManager Hooked!");
			LogHandler.log(1, "GroupManager Hooked");
		}
		/* Griefprevention */
		if(Variables.griefprevention_enable){
			if(Bukkit.getPluginManager().getPlugin("GriefPrevention") != null){
				addon_GP = new GriefPreventionHandler(this);
				log(ConsoleColors.CYAN.getCode() + "GriefPrevention Hooked!");
				LogHandler.log(1, "GriefPrevention Hooked");
			}
		}
		/* DeluxeChat */
		if(Bukkit.getPluginManager().getPlugin("DeluxeChat") != null){
			LogHandler.log(3, "Trying hook into DeluxeChat (exist)");
			try{
				addon_DC = new DeluxeChatHandler(this);
			}catch(NoClassDefFoundError ex){
				log(ConsoleColors.RED.getCode() + "Impossible hook into DeluxeChat (Maybe too old version)!");
				LogHandler.log(1, "Impossible hook into DeluxeChat (Maybe too old version)!");
			}
		}
		/* BanManager */
		if(Variables.banmanager_enable){
			if(Bukkit.getPluginManager().getPlugin("BanManager") != null){
				new BanManagerHandler(this).register();
				log(ConsoleColors.CYAN.getCode() + "BanManager Hooked!");
				LogHandler.log(1, "BanManager Hooked");
			}
		}
		/* Dynmap */
		if(Variables.dynmap_enable){
			if(Bukkit.getPluginManager().getPlugin("dynmap") != null){
				addon_Dynmap = new DynmapHandler(this);
				log(ConsoleColors.CYAN.getCode() + "Dynmap Hooked!");
				LogHandler.log(1, "Dynmap Hooked");
			}
		}
		/* PlaceholderAPI */
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			if(new PlaceholderHandler(this).hook()){
				log(ConsoleColors.CYAN.getCode() + "PlaceholderAPI Hooked!");
				LogHandler.log(1, "PlaceholderAPI Hooked");
				addon_PlhAPI = true;
			}
		}
		/* EssentialsChat */
		if(Bukkit.getPluginManager().getPlugin("EssentialsChat") != null){
			getServer().getPluginManager().registerEvents(new EssentialsChatHandler(this), this);
			log(ConsoleColors.CYAN.getCode() + "EssentialsChat Hooked!");
			LogHandler.log(1, "EssentialsChat Hooked");
		}
		/* Vault */
		if(Variables.vault_enable){
			if (!setupEconomy()) {
				log(ConsoleColors.RED.getCode() + "Failed hook into Vault, disabled vault features!");
				LogHandler.log(1, "Failed hook into Vault, disabled vault features");
				Variables.vault_enable = false;
	        } else {
	        	if(!setupChat()){
	        		log(ConsoleColors.RED.getCode() + "Failed hook into Vault Chat");
					LogHandler.log(1, "Failed hook into Vault Chat");
	        	}
	        	log(ConsoleColors.CYAN.getCode() + "Vault Hooked!");
	        	LogHandler.log(1, "Vault Hooked");
	        }
		}
		/* Metrics */
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
			if(Variables.log_mode > 2)
				LogHandler.log(1, "Metrics Hooked");
		} catch (IOException e) {}
	}
	
	public void reloadConfiguration() {
		resetPendingPartyTask();
		checkUpdates();
		config = new ConfigHandler(this);
		player.reloadPlayers();
		registerCommands();
	}

	public void registerCommands() {
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
		if(Variables.vault_enable && Variables.vault_confirm_enable)
			handler.register(Variables.command_confirm, new CommandConfirm(this));
		/* GriefPrevention */
		if(Variables.griefprevention_enable)
			handler.register(Variables.command_claim, new CommandClaim(this));
		/* Password system */
		if(Variables.password_enable){
			handler.register(Variables.command_join, new CommandJoin(this));
			handler.register(Variables.command_password, new CommandPassword(this));
		}
		/* Tag system */
		if(Variables.tag_enable && !Variables.tag_system){
			if(Variables.tag_custom_prefix)
				handler.register(Variables.command_prefix, new CommandPrefix(this));
			if(Variables.tag_custom_suffix)
				handler.register(Variables.command_suffix, new CommandSuffix(this));
		}
		
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
			cmdP.setExecutor(handler);
			
			bukkitCommandMap.setAccessible(false);
			if(getServer().getPluginCommand(Variables.command_party) != null && !getServer().getPluginCommand(Variables.command_party).equals("Parties"))
				getServer().getPluginCommand(Variables.command_party).setExecutor(handler);
			if(getServer().getPluginCommand(Variables.command_p) != null && !getServer().getPluginCommand(Variables.command_p).equals("Parties"))
				getServer().getPluginCommand(Variables.command_p).setExecutor(handler);
			LogHandler.log(3, "All commands mapped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Gets & Sets */
	public ConfigHandler getConfigHandler(){return config;}
	public PlayerHandler getPlayerHandler(){return player;}
	public PartyHandler getPartyHandler(){return party;}
	public SQLDatabase getSQLDatabase(){return sqldatabase;}
	public void setSQLDatabase(SQLDatabase s){sqldatabase = s;}
	
	public boolean getPex(){return addon_PEX;}
	public boolean getGM(){return addon_GM;}
	public GriefPreventionHandler getGriefPrevention(){return addon_GP;}
	public boolean isPlaceholderAPIHooked(){return addon_PlhAPI;}
	public DynmapHandler getDynmap(){return addon_Dynmap;}
	
	public int getConfigVersion() {return ver_config;}
	public int getMessagesVersion() {return ver_mess;}
	
	public boolean isUpdateAvailable(){return update_avail;}
	public String getNewUpdate(){return update_newver;}
	
	public String getScoreboardPrefix(){return scoreboardprefix;}
	
	
	/* Misc */
	private void resetPendingPartyTask(){
		for(BukkitTask bt : Bukkit.getScheduler().getPendingTasks()){
			if(bt.getOwner() instanceof Parties){
				if(party.listPartyToDelete.containsValue(bt.getTaskId())){
					for(Entry<String, Integer> et : getPartyHandler().listPartyToDelete.entrySet()){
						if(et.getValue() == bt.getTaskId()){
							bt.cancel();
							party.deleteTimedParty(et.getKey(), true);
							break;
						}
					}
				}
			}
		}
	}

	public void log(String msg) {
		this.getServer().getLogger().log(Level.INFO, ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)) + ConsoleColors.RESET.getCode());
	}
	public void log(Level warn, String msg) {
		this.getServer().getLogger().log(warn, ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)) + ConsoleColors.RESET.getCode());
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
        addon_VEcon = rsp.getProvider();
        return addon_VEcon != null;
    }
	private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
        	addon_VChat = chatProvider.getProvider();
        }
        return (addon_VChat != null);
    }
     
	public Economy getEconomy(){return addon_VEcon;}
	public Chat getVaultChat(){return addon_VChat;}
}
