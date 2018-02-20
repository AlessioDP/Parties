package com.alessiodp.parties.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.SkillAPIHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.events.PartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.partiesapi.events.PartiesFriendlyFireBlockedEvent;
import com.alessiodp.partiesapi.events.PartiesPotionsFriendlyFireBlockedEvent;

public class FightListener implements Listener {
	Parties plugin;
	
	public FightListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent event) {
		if (ConfigParties.FRIENDLYFIRE_ENABLE && event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			Player attacker = null;
			DamageType type = DamageType.UNSUPPORTED; // 0=Basic, 1=Arrow, 2=EnderPearl, 3=Snowball
			if (event.getDamager() instanceof Player)
				type = DamageType.PLAYER;
			else if (event.getDamager() instanceof Arrow)
				type = DamageType.ARROW;
			else if (event.getDamager() instanceof EnderPearl)
				type = DamageType.ENDERPEARL;
			else if (event.getDamager() instanceof Snowball)
				type = DamageType.SNOWBALL;
			
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
				case UNSUPPORTED:
				}
				if (attacker != null) {
					// Found right attacker
					if (!victim.getUniqueId().equals(attacker.getUniqueId())
							&& (ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains("*")
									|| ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains(victim.getWorld().getName()))) {
						// Friendly fire not allowed here
						PartyPlayerEntity ppVictim = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
						PartyPlayerEntity ppAttacker = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
						
						if (!ppVictim.getPartyName().isEmpty() && ppVictim.getPartyName().equalsIgnoreCase(ppAttacker.getPartyName())) {
							// Calling API event
							PartiesFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesFriendlyFireBlockedEvent(ppVictim, ppAttacker, event);
							Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
							if (!partiesFriendlyFireEvent.isCancelled()) {
								// Friendly fire confirmed
								PartyEntity party = plugin.getPartyManager().getParty(ppVictim.getPartyName());
								
								ppAttacker.sendMessage(Messages.OTHER_FRIENDLYFIRE_CANTHIT);
								party.sendFriendlyFireWarn(ppVictim, ppAttacker);
								
								event.setCancelled(true);
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_FRIENDLYFIRE_DENIED
										.replace("{type}", type.name())
										.replace("{player}", attacker.getName())
										.replace("{victim}", victim.getName()), true);
							} else
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_FRIENDLYFIREEVENT_DENY
										.replace("{type}", type.name())
										.replace("{player}", attacker.getName())
										.replace("{victim}", victim.getName()), true);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		if (ConfigParties.FRIENDLYFIRE_ENABLE
				&& event.getEntity() instanceof Player
				&& event.getPotion().getShooter() instanceof Player) {
			Player attacker = (Player) event.getPotion().getShooter();
			PartyPlayerEntity ppAttacker = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
			
			if (!ppAttacker.getPartyName().isEmpty()
					&& (ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains("*")
							|| ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains(attacker.getWorld().getName()))) {
				boolean cancel = false;
				for (PotionEffect pe : event.getEntity().getEffects()) {
					switch (pe.getType().getName().toLowerCase()) {
					case "harm":
					case "blindness":
					case "confusion":
					case "poison":
					case "slow":
					case "slow_digging":
					case "weakness":
					case "unluck":
						cancel = true;
					}
					if (cancel)
						break;
				}
				if (cancel) {
					// Friendly fire not allowed here
					PartyEntity party = plugin.getPartyManager().getParty(ppAttacker.getPartyName());
					for (LivingEntity e : event.getAffectedEntities()) {
						if (e instanceof Player) {
							Player victim = (Player) e;
							if (!attacker.equals(victim)) {
								PartyPlayerEntity ppVictim = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
								if (ppVictim.getPartyName().equalsIgnoreCase(ppAttacker.getPartyName())) {
									// Calling API event
									PartiesPotionsFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesPotionsFriendlyFireBlockedEvent(ppVictim, ppAttacker, event);
									Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
									if (!partiesFriendlyFireEvent.isCancelled()) {
										// Friendly fire confirmed
										ppAttacker.sendMessage(Messages.OTHER_FRIENDLYFIRE_CANTHIT);
										party.sendFriendlyFireWarn(ppVictim, ppAttacker);
										
										event.setIntensity(e, 0);
										LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_FRIENDLYFIRE_DENIED
												.replace("{type}", "potion splash")
												.replace("{player}", attacker.getName())
												.replace("{victim}", victim.getName()), true);
									} else
										LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_FRIENDLYFIREEVENT_DENY
												.replace("{type}", "potion splash")
												.replace("{player}", attacker.getName())
												.replace("{victim}", victim.getName()), true);
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
		if (ConfigParties.FRIENDLYFIRE_ENABLE
				&& event.getEntity() instanceof Player
				&& event.getCombuster() instanceof Projectile
				&& ((Projectile) event.getCombuster()).getShooter() instanceof Player) {
			Player victim = (Player) event.getEntity();
			Player attacker = (Player)((Projectile) event.getCombuster()).getShooter();
			
			// Found right attacker
			if (!victim.getUniqueId().equals(attacker.getUniqueId())
					&& (ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains("*")
							|| ConfigParties.FRIENDLYFIRE_LISTWORLDS.contains(victim.getWorld().getName()))) {
				// Friendly fire not allowed here
				PartyPlayerEntity ppVictim = plugin.getPlayerManager().getPlayer(victim.getUniqueId());
				PartyPlayerEntity ppAttacker = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
				
				if (!ppVictim.getPartyName().isEmpty() && ppVictim.getPartyName().equalsIgnoreCase(ppAttacker.getPartyName())) {
					// Calling API event
					PartiesCombustFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesCombustFriendlyFireBlockedEvent(ppVictim, ppAttacker, event);
					Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
					if (!partiesFriendlyFireEvent.isCancelled()) {
						// Friendly fire confirmed
						PartyEntity party = plugin.getPartyManager().getParty(ppVictim.getPartyName());
						
						ppAttacker.sendMessage(Messages.OTHER_FRIENDLYFIRE_CANTHIT);
						party.sendFriendlyFireWarn(ppVictim, ppAttacker);
						
						event.setCancelled(true);
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_FRIENDLYFIRE_DENIED
								.replace("{type}", "entity combust")
								.replace("{player}", attacker.getName())
								.replace("{victim}", victim.getName()), true);
					} else
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_FRIENDLYFIREEVENT_DENY
								.replace("{type}", "entity combust")
								.replace("{player}", attacker.getName())
								.replace("{victim}", victim.getName()), true);
				}
			}
			
		}
	}
	
	@EventHandler
	public void onEntityDieExperience(EntityDeathEvent event) {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
			Entity killedMob = event.getEntity();
			//if (!ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE
			//		|| !MythicMobsHandler.isMythicMob(killedMob)) {
				if (event.getEntity().getKiller() instanceof Player) {
					Player killer = event.getEntity().getKiller();
					
					// Count experience
					double vanillaExp = 0;
					double skillApiExp = 0;
					if (ConfigMain.ADDITIONAL_EXP_HANDLE_VANILLA)
						vanillaExp = event.getDroppedExp();
					if (ConfigMain.ADDITIONAL_EXP_HANDLE_SKILLAPI)
						skillApiExp = SkillAPIHandler.getExp(killedMob);
					
					if (PartiesUtils.handleExperienceDistribution(killer, killedMob, killedMob.getType().getName(), vanillaExp, skillApiExp)) {
						// Remove exp from vanilla event
						event.setDroppedExp(0);
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_VANILLA_SET, true);
					}
				}
			//}
		}
	}
	
	@EventHandler
	public void onEntityDieKill(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = event.getEntity().getKiller();
			PartyPlayerEntity ppKiller = plugin.getPlayerManager().getPlayer(killer.getUniqueId());
			
			if (!ppKiller.getPartyName().isEmpty()) {
				PartyEntity party = plugin.getPartyManager().getParty(ppKiller.getPartyName());
				
				if (ConfigParties.KILLS_ENABLE) {
					boolean gotKill = false;
					
					if (ConfigParties.KILLS_MOB_HOSTILE
							&& event.getEntity() instanceof Monster)
						gotKill = true;
					else if (ConfigParties.KILLS_MOB_NEUTRAL
							&& event.getEntity() instanceof Animals)
						gotKill = true;
					else if (ConfigParties.KILLS_MOB_PLAYERS
							&& event.getEntity() instanceof Player)
						gotKill = true;
					
					if (gotKill) {
						party.setKills(party.getKills() + 1);
						party.updateParty();
						LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_KILL_ADD
								.replace("{party}", party.getName())
								.replace("{player}", killer.getName()), true);
					}
				}
			}
		}
	}
	
	private enum DamageType {
		UNSUPPORTED, PLAYER, ARROW, ENDERPEARL, SNOWBALL;
	}
}
