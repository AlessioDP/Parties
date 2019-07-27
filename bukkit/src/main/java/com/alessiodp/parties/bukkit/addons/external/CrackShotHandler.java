package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

@RequiredArgsConstructor
public class CrackShotHandler implements Listener {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "CrackShot";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigParties.FRIENDLYFIRE_ENABLE &&
				BukkitConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, (BukkitPartiesBootstrap) plugin.getBootstrap());
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
						.replace("{addon}", ADDON_NAME), true);
			} else {
				BukkitConfigParties.FRIENDLYFIRE_CRACKSHOT_ENABLE = false;
				active = false;
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_FAILED
						.replace("{addon}", ADDON_NAME), true);
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
						User shooter = plugin.getPlayer(event.getPlayer().getUniqueId());
						
						// Calling API event
						BukkitPartiesFriendlyFireBlockedEvent partiesFriendlyFireEvent = ((BukkitEventManager) plugin.getEventManager()).preparePartiesFriendlyFireBlockedEvent(victimPp, shooterPp, null);
						plugin.getEventManager().callEvent(partiesFriendlyFireEvent);
						
						if (!partiesFriendlyFireEvent.isCancelled()) {
							// Friendly fire confirmed
							BukkitPartyImpl party = (BukkitPartyImpl) plugin.getPartyManager().getParty(victimPp.getPartyName());
							
							shooter.sendMessage(BukkitMessages.ADDCMD_PROTECTION_PROTECTED, true);
							party.warnFriendlyFire(victimPp, shooterPp);
							
							event.setCancelled(true);
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_FRIENDLYFIRE_DENIED
									.replace("{type}", "entity combust")
									.replace("{player}", shooter.getName())
									.replace("{victim}", victim.getName()), true);
						} else
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_FRIENDLYFIREEVENT_DENY
									.replace("{type}", "entity combust")
									.replace("{player}", shooter.getName())
									.replace("{victim}", victim.getName()), true);
					}
				}
			}
		}
	}
}