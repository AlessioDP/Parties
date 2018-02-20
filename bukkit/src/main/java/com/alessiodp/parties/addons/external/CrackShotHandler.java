package com.alessiodp.parties.addons.external;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.partiesapi.events.PartiesFriendlyFireBlockedEvent;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

public class CrackShotHandler implements Listener {
	private Parties plugin;
	private static final String ADDON_NAME = "CrackShot";
	private static boolean active;
	
	
	public CrackShotHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		active = false;
		if (ConfigParties.FRIENDLYFIRE_ENABLE &&
				ConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				plugin.getServer().getPluginManager().registerEvents(this, plugin);
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	
	@EventHandler
	public void onWeaponDamageEntity(WeaponDamageEntityEvent event) {
		if (active && ConfigParties.FRIENDLYFIRE_ENABLE) {
			if (event.getVictim() instanceof Player) {
				PartyPlayerEntity shooterPp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
				Player victim = (Player) event.getVictim();
				
				if (!victim.getUniqueId().equals(shooterPp.getPlayerUUID())
						&& (ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains("*")
								|| ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains(victim.getWorld().getName()))) {
					PartyPlayerEntity victimPp = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
					
					if (!shooterPp.getPartyName().isEmpty()
							&& shooterPp.getPartyName().equalsIgnoreCase(victimPp.getPartyName())) {
						// Calling API event
						PartiesFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesFriendlyFireBlockedEvent(victimPp, shooterPp, null);
						Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
						if (!partiesFriendlyFireEvent.isCancelled()) {
							// Friendly fire confirmed
							PartyEntity party = plugin.getPartyManager().getParty(victimPp.getPartyName());
							
							shooterPp.sendMessage(Messages.OTHER_FRIENDLYFIRE_CANTHIT);
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