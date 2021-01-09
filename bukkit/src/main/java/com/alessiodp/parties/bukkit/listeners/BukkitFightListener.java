package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyImpl;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPotionsFriendlyFireBlockedEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

@RequiredArgsConstructor
public class BukkitFightListener implements Listener {
	private final PartiesPlugin plugin;
	
	@EventHandler (ignoreCancelled = true)
	public void onPlayerHit(EntityDamageByEntityEvent event) {
		if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE && event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			Player attacker = null;
			DamageType type = DamageType.UNSUPPORTED;
			if (event.getDamager() instanceof Player)
				type = DamageType.PLAYER;
			else if (event.getDamager() instanceof Arrow)
				type = DamageType.ARROW;
			else if (event.getDamager() instanceof EnderPearl)
				type = DamageType.ENDERPEARL;
			else if (event.getDamager() instanceof Snowball)
				type = DamageType.SNOWBALL;
			try {
				if (event.getDamager() instanceof Trident)
					type = DamageType.TRIDENT;
			} catch (NoClassDefFoundError ignored) {}
			
			if (!type.equals(DamageType.UNSUPPORTED)) {
				ProjectileSource shooterSource;
				switch (type) {
				case PLAYER:
					attacker = (Player) event.getDamager();
					break;
				case ARROW:
					shooterSource = ((Arrow)event.getDamager()).getShooter();
					if (shooterSource instanceof Player)
						attacker = (Player) shooterSource;
					break;
				case ENDERPEARL:
					shooterSource = ((EnderPearl)event.getDamager()).getShooter();
					if (shooterSource instanceof Player)
						attacker = (Player) shooterSource;
					break;
				case SNOWBALL:
					shooterSource = ((Snowball)event.getDamager()).getShooter();
					if (shooterSource instanceof Player)
						attacker = (Player) shooterSource;
					break;
				case TRIDENT:
					shooterSource = ((Trident)event.getDamager()).getShooter();
					if (shooterSource instanceof Player)
						attacker = (Player) shooterSource;
					break;
				default:
					// Nothing to do
					break;
				}
				if (attacker != null) {
					// Found right attacker
					if (!victim.getUniqueId().equals(attacker.getUniqueId())) {
						// Friendly fire not allowed here
						PartyPlayerImpl ppVictim = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
						PartyPlayerImpl ppAttacker = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
						BukkitPartyImpl party = (BukkitPartyImpl) plugin.getPartyManager().getParty(ppAttacker.getPartyId());
						
						if (party != null
								&& ppAttacker.getPartyId().equals(ppVictim.getPartyId())
								&& party.isFriendlyFireProtected()
								&& !attacker.hasPermission(PartiesPermission.ADMIN_PROTECTION_BYPASS.toString())) {
							// Calling API event
							BukkitPartiesFriendlyFireBlockedEvent partiesFriendlyFireEvent = ((BukkitEventManager) plugin.getEventManager()).preparePartiesFriendlyFireBlockedEvent(ppVictim, ppAttacker, event);
							plugin.getEventManager().callEvent(partiesFriendlyFireEvent);
							
							if (!partiesFriendlyFireEvent.isCancelled()) {
								// Friendly fire confirmed
								User userAttacker = plugin.getPlayer(attacker.getUniqueId());
								userAttacker.sendMessage(
										plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_PROTECTION_PROTECTED, ppAttacker, party)
										, true);
								party.warnFriendlyFire(ppVictim, ppAttacker);
								
								event.setCancelled(true);
								plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_FRIENDLYFIRE_DENIED, type.name(), attacker.getUniqueId().toString(), victim.getUniqueId().toString()), true);
							} else
								plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_FRIENDLYFIREEVENT_DENY, type.name(), attacker.getUniqueId().toString(), victim.getUniqueId().toString()), true);
						}
					}
				}
			}
		}
	}
	@EventHandler (ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event) {
		if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE
				&& event.getEntity() instanceof Player
				&& event.getPotion().getShooter() instanceof Player) {
			Player attacker = (Player) event.getPotion().getShooter();
			PartyPlayerImpl ppAttacker = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
			BukkitPartyImpl party = (BukkitPartyImpl) plugin.getPartyManager().getParty(ppAttacker.getPartyId());
			
			if (party != null && party.isFriendlyFireProtected() && !attacker.hasPermission(PartiesPermission.ADMIN_PROTECTION_BYPASS.toString())) {
				boolean cancel = false;
				for (PotionEffect pe : event.getEntity().getEffects()) {
					switch (CommonUtils.toLowerCase(pe.getType().getName())) {
					case "harm":
					case "blindness":
					case "confusion":
					case "poison":
					case "slow":
					case "slow_digging":
					case "weakness":
					case "unluck":
						cancel = true;
						break;
					default:
						// Do not cancel
						break;
					}
					if (cancel)
						break;
				}
				if (cancel) {
					// Friendly fire not allowed here
					for (LivingEntity e : event.getAffectedEntities()) {
						if (e instanceof Player) {
							Player victim = (Player) e;
							if (!attacker.equals(victim)) {
								PartyPlayerImpl ppVictim = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
								if (ppAttacker.getPartyId().equals(ppVictim.getPartyId())) {
									// Calling API event
									BukkitPartiesPotionsFriendlyFireBlockedEvent partiesFriendlyFireEvent = ((BukkitEventManager) plugin.getEventManager()).preparePartiesPotionsFriendlyFireBlockedEvent(ppVictim, ppAttacker, event);
									plugin.getEventManager().callEvent(partiesFriendlyFireEvent);
									
									if (!partiesFriendlyFireEvent.isCancelled()) {
										// Friendly fire confirmed
										User userAttacker = plugin.getPlayer(attacker.getUniqueId());
										userAttacker.sendMessage(
												plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_PROTECTION_PROTECTED, ppAttacker, party)
												, true);
										party.warnFriendlyFire(ppVictim, ppAttacker);
										
										event.setIntensity(e, 0);
										plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_FRIENDLYFIRE_DENIED, "potion splash", attacker.getUniqueId().toString(), victim.getUniqueId().toString()), true);
									} else
										plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_FRIENDLYFIREEVENT_DENY, "potion splash", attacker.getUniqueId().toString(), victim.getUniqueId().toString()), true);
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler (ignoreCancelled = true)
	public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
		if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE
				&& event.getEntity() instanceof Player
				&& event.getCombuster() instanceof Projectile
				&& ((Projectile) event.getCombuster()).getShooter() instanceof Player) {
			Player victim = (Player) event.getEntity();
			Player attacker = (Player)((Projectile) event.getCombuster()).getShooter();
			
			// Found right attacker
			if (attacker != null && !victim.getUniqueId().equals(attacker.getUniqueId())) {
				// Friendly fire not allowed here
				PartyPlayerImpl ppVictim = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
				PartyPlayerImpl ppAttacker = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
				BukkitPartyImpl party = (BukkitPartyImpl) plugin.getPartyManager().getParty(ppAttacker.getPartyId());
				
				if (party != null
						&& ppAttacker.getPartyId().equals(ppVictim.getPartyId())
						&& party.isFriendlyFireProtected()
						&& !attacker.hasPermission(PartiesPermission.ADMIN_PROTECTION_BYPASS.toString())) {
					// Calling API event
					BukkitPartiesCombustFriendlyFireBlockedEvent partiesFriendlyFireEvent = ((BukkitEventManager) plugin.getEventManager()).prepareCombustFriendlyFireBlockedEvent(ppVictim, ppAttacker, event);
					plugin.getEventManager().callEvent(partiesFriendlyFireEvent);
					
					if (!partiesFriendlyFireEvent.isCancelled()) {
						// Friendly fire confirmed
						User userAttacker = plugin.getPlayer(attacker.getUniqueId());
						userAttacker.sendMessage(
								plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_PROTECTION_PROTECTED, ppAttacker, party)
								, true);
						party.warnFriendlyFire(ppVictim, ppAttacker);
						
						event.setCancelled(true);
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_FRIENDLYFIRE_DENIED, "entity combust", attacker.getUniqueId().toString(), victim.getUniqueId().toString()), true);
					} else
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_FRIENDLYFIREEVENT_DENY, "entity combust", attacker.getUniqueId().toString(), victim.getUniqueId().toString()), true);
				}
			}
			
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDieKill(EntityDeathEvent event) {
		if (BukkitConfigParties.ADDITIONAL_KILLS_ENABLE
				&& event.getEntity().getKiller() != null) {
			Player killer = event.getEntity().getKiller();
			PartyPlayerImpl ppKiller = plugin.getPlayerManager().getPlayer(killer.getUniqueId());
			
			PartyImpl party = plugin.getPartyManager().getParty(ppKiller.getPartyId());
			if (party != null) {
				boolean gotKill = false;
				
				if (BukkitConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE
						&& event.getEntity() instanceof Monster)
					gotKill = true;
				else if (BukkitConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL
						&& event.getEntity() instanceof Animals)
					gotKill = true;
				else if (BukkitConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS
						&& event.getEntity() instanceof Player)
					gotKill = true;
				
				if (gotKill) {
					party.setKills(party.getKills() + 1);
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_KILL_ADD, party.getId().toString(), killer.getUniqueId().toString()), true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerGotHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && BukkitConfigParties.ADDITIONAL_HOME_CANCEL_HIT) {
			// Make it async
			plugin.getScheduler().runAsync(() -> {
				BukkitPartyPlayerImpl partyPlayer = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(event.getEntity().getUniqueId());
				// Check if the player is on home cooldown
				if (partyPlayer.getHomeTeleporting() != null) {
					// Cancelling home task
					partyPlayer.getHomeTeleporting().cancel();
					
					User user = plugin.getPlayer(partyPlayer.getPlayerUUID());
					user.sendMessage(
							plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED, partyPlayer, null)
							, true);
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_HOME_DENIED_FIGHT, partyPlayer.getPlayerUUID().toString()), true);
				}
			});
		}
	}
	
	private enum DamageType {
		UNSUPPORTED, PLAYER, ARROW, ENDERPEARL, SNOWBALL, TRIDENT
	}
}
