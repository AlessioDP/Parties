package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyImpl;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.ConsoleColor;
import com.alessiodp.parties.api.events.bukkit.PartiesFriendlyFireBlockedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

public class CrackShotHandler implements Listener {
	private BukkitPartiesPlugin plugin;
	private static final String ADDON_NAME = "CrackShot";
	private static boolean active;
	
	
	public CrackShotHandler(BukkitPartiesPlugin instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		active = false;
		if (BukkitConfigParties.FRIENDLYFIRE_ENABLE &&
				BukkitConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				plugin.getBootstrap().getServer().getPluginManager().registerEvents(this, plugin.getBootstrap());
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				BukkitConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE = false;
				active = false;
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	
	@EventHandler
	public void onWeaponDamageEntity(WeaponDamageEntityEvent event) {
		if (active && BukkitConfigParties.FRIENDLYFIRE_ENABLE) {
			if (event.getVictim() instanceof Player) {
				PartyPlayerImpl shooterPp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
				Player victim = (Player) event.getVictim();
				
				if (!victim.getUniqueId().equals(shooterPp.getPlayerUUID())) {
					PartyPlayerImpl victimPp = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
					
					if (!shooterPp.getPartyName().isEmpty()
							&& shooterPp.getPartyName().equalsIgnoreCase(victimPp.getPartyName())) {
						// Calling API event
						PartiesFriendlyFireBlockedEvent partiesFriendlyFireEvent = ((BukkitEventManager) plugin.getEventManager()).preparePartiesFriendlyFireBlockedEvent(victimPp, shooterPp, null);
						plugin.getEventManager().callEvent(partiesFriendlyFireEvent);
						if (!partiesFriendlyFireEvent.isCancelled()) {
							// Friendly fire confirmed
							BukkitPartyImpl party = (BukkitPartyImpl) plugin.getPartyManager().getParty(victimPp.getPartyName());
							
							shooterPp.sendMessage(BukkitMessages.ADDCMD_PVP_PROTECTED);
							party.sendFriendlyFireWarn(victimPp, shooterPp);
							
							event.setCancelled(true);
							LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_FRIENDLYFIRE_DENIED
									.replace("{type}", "entity combust")
									.replace("{player}", shooterPp.getName())
									.replace("{victim}", victim.getName()), true);
						} else
							LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_FRIENDLYFIREEVENT_DENY
									.replace("{type}", "entity combust")
									.replace("{player}", shooterPp.getName())
									.replace("{victim}", victim.getName()), true);
					}
				}
			}
		}
	}
}