package com.alessiodp.parties.events;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.addon.SkillAPIHandler;
import com.alessiodp.parties.utils.enums.LogLevel;
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
		if (Variables.friendlyfire_enable && event.getEntity() instanceof Player) {
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
							&& (Variables.friendlyfire_listworlds.contains("*")
									|| Variables.friendlyfire_listworlds.contains(victim.getWorld().getName()))) {
						// Friendly fire not allowed here
						ThePlayer tpVictim = plugin.getPlayerHandler().getPlayer(victim.getUniqueId());
						ThePlayer tpAttacker = plugin.getPlayerHandler().getPlayer(attacker.getUniqueId());
						
						if (!tpVictim.getPartyName().isEmpty() && tpVictim.getPartyName().equalsIgnoreCase(tpAttacker.getPartyName())) {
							// Calling API event
							PartiesFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesFriendlyFireBlockedEvent(victim, attacker, event);
							Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
							if (!partiesFriendlyFireEvent.isCancelled()) {
								// Friendly fire confirmed
								Party party = plugin.getPartyHandler().getParty(tpVictim.getPartyName());
								
								tpAttacker.sendMessage(Messages.canthitmates);
								party.sendFriendlyFireWarn(tpVictim, tpAttacker);
								
								event.setCancelled(true);
								LogHandler.log(LogLevel.DEBUG, "Denied PvP friendly fire [type " + type.name() + "] between A:'" + attacker.getName() + "' and V:'" + victim.getName() + "'", true);
							} else
								LogHandler.log(LogLevel.DEBUG, "PartiesFriendlyFireBlockedEvent is cancelled, ignoring [type " + type.name() + "] between A:'" + attacker.getName() + "' and V:'" + victim.getName() + "'", true);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		if (Variables.friendlyfire_enable
				&& event.getEntity() instanceof Player
				&& event.getPotion().getShooter() instanceof Player) {
			Player attacker = (Player) event.getPotion().getShooter();
			ThePlayer tpAttacker = plugin.getPlayerHandler().getPlayer(attacker.getUniqueId());
			
			if (!tpAttacker.getPartyName().isEmpty()
					&& (Variables.friendlyfire_listworlds.contains("*")
							|| Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))) {
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
					Party party = plugin.getPartyHandler().getParty(tpAttacker.getPartyName());
					for (LivingEntity e : event.getAffectedEntities()) {
						if (e instanceof Player) {
							Player victim = (Player) e;
							if (!attacker.equals(victim)) {
								ThePlayer tpVictim = plugin.getPlayerHandler().getPlayer(victim.getUniqueId());
								if (tpVictim.getPartyName().equalsIgnoreCase(tpAttacker.getPartyName())) {
									// Calling API event
									PartiesPotionsFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesPotionsFriendlyFireBlockedEvent(victim, attacker, event);
									Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
									if (!partiesFriendlyFireEvent.isCancelled()) {
										// Friendly fire confirmed
										tpAttacker.sendMessage(Messages.canthitmates);
										party.sendFriendlyFireWarn(tpVictim, tpAttacker);
										
										event.setIntensity(e, 0);
										LogHandler.log(LogLevel.DEBUG, "Denied PvP friendly fire [Potion splash] between A:'" + attacker.getName() + "' and V:'" + victim.getName() + "'", true);
									} else
										LogHandler.log(LogLevel.DEBUG, "PartiesPotionsFriendlyFireBlockedEvent is cancelled, ignoring [Potion splash] between A:'" + attacker.getName() + "' and V:'" + victim.getName() + "'", true);
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
		if (Variables.friendlyfire_enable
				&& event.getEntity() instanceof Player
				&& event.getCombuster() instanceof Projectile
				&& ((Projectile) event.getCombuster()).getShooter() instanceof Player) {
			Player victim = (Player) event.getEntity();
			Player attacker = (Player)((Projectile) event.getCombuster()).getShooter();
			
			// Found right attacker
			if (!victim.getUniqueId().equals(attacker.getUniqueId())
					&& (Variables.friendlyfire_listworlds.contains("*")
							|| Variables.friendlyfire_listworlds.contains(victim.getWorld().getName()))) {
				// Friendly fire not allowed here
				ThePlayer tpVictim = plugin.getPlayerHandler().getPlayer(victim.getUniqueId());
				ThePlayer tpAttacker = plugin.getPlayerHandler().getPlayer(attacker.getUniqueId());
				
				if (!tpVictim.getPartyName().isEmpty() && tpVictim.getPartyName().equalsIgnoreCase(tpAttacker.getPartyName())) {
					// Calling API event
					PartiesCombustFriendlyFireBlockedEvent partiesFriendlyFireEvent = new PartiesCombustFriendlyFireBlockedEvent(victim, attacker, event);
					Bukkit.getServer().getPluginManager().callEvent(partiesFriendlyFireEvent);
					if (!partiesFriendlyFireEvent.isCancelled()) {
						// Friendly fire confirmed
						Party party = plugin.getPartyHandler().getParty(tpVictim.getPartyName());
						
						tpAttacker.sendMessage(Messages.canthitmates);
						party.sendFriendlyFireWarn(tpVictim, tpAttacker);
						
						event.setCancelled(true);
						LogHandler.log(LogLevel.DEBUG, "Denied PvP friendly fire [Entity combust] between A:'" + attacker.getName() + "' and V:'" + victim.getName() + "'", true);
					} else
						LogHandler.log(LogLevel.DEBUG, "PartiesCombustFriendlyFireBlockedEvent is cancelled, ignoring [Entity combust] between A:'" + attacker.getName() + "' and V:'" + victim.getName() + "'", true);
					
				}
			}
			
		}
	}
	
	@EventHandler
	public void onEntityDie(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = event.getEntity().getKiller();
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getEntity().getKiller().getUniqueId());
			
			if (!tp.getPartyName().isEmpty()) {
				Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
				/*
				 * Kill system
				 */
				if (Variables.kill_enable) {
					boolean gotKill = false;
					
					if (Variables.kill_save_mobshostile
							&& event.getEntity() instanceof Monster)
						gotKill = true;
					else if (Variables.kill_save_mobsneutral
							&& event.getEntity() instanceof Animals)
						gotKill = true;
					else if (Variables.kill_save_players
							&& event.getEntity() instanceof Player)
						gotKill = true;
					
					if (gotKill) {
						party.setKills(party.getKills() + 1);
						party.updateParty();
						LogHandler.log(LogLevel.MEDIUM, "Adding a kill to the party " + party.getName(), true);
					}
				}
				/*
				 * EXP System
				 */
				event.getEntity().setMetadata("parties_killed", new FixedMetadataValue(plugin, true));
				if(Variables.exp_enable) {
					double exp = event.getDroppedExp();
					List<Player> list = new ArrayList<Player>();
					
					list.add(killer);
					for (Player p : party.getOnlinePlayers()) {
						if (p != killer
								&& p.getLocation().getWorld() == killer.getWorld()
								&& killer.getLocation().distance(p.getLocation()) < Variables.exp_range) {
							list.add(p);
						}
					}
					if (list.size() > 1) {
						if (Variables.exp_skillapi_enable) {
							exp = SkillAPIHandler.getExp(exp, event.getEntity());
						}
						/*
						 * MythicMobs is currently unsupported
						if (Variables.exp_mythicmobs_enable) {
							double totalExp = 0;
							HashMap<String, Double> expMap = MythicMobsHandler.getExp(exp, event.getEntity());
							if (Variables.exp_mythicmobs_normalexp && expMap.containsKey("exp")) {
								totalExp += expMap.get("exp");
							}
							if (Variables.exp_mythicmobs_skillapiexp && expMap.containsKey("skillapi-exp")) {
								totalExp += expMap.get("skillapi-exp");
							}
							exp = totalExp;
						}
						*/
						
						if (exp > Variables.exp_sharemorethan) {
							// SHARE
							event.setDroppedExp(0);
							double givenExp = 0;
							if (Variables.exp_skillapi_enable) {
								// Used to block SkillAPI kill event (else killer got exp 2 times)
								SkillAPIHandler.blockEvent(event.getEntity());
							}
							
							// Calculate exp
							try {
								ScriptEngine sem = new ScriptEngineManager().getEngineByName("JavaScript");
								String formula = Variables.exp_formula
										.replace("%exp%", Double.toString(exp))
										.replace("%number%", Integer.toString(list.size()));
								givenExp = (double) sem.eval(formula);
							} catch (Exception ex) {
								LogHandler.printError("Failed to calculate shared exp: " + ex.getMessage());
							}
							
							// Give exp
							for (Player pl : list) {
								ExpGiveType giveType = ExpGiveType.NORMAL_OWN;
								if (pl.equals(killer)) {
									// Killer
									if (Variables.exp_skillapi_enable) {
										SkillAPIHandler.giveExp(pl, -50);
										giveType = ExpGiveType.SKILLAPI_OWN;
									} else {
										killer.giveExp((int) givenExp);
									}
									
									tp.sendMessage(Messages.expgain
											.replace("%exp%", Double.toString(givenExp))
											.replace("%exptotal%", Double.toString(givenExp))
											.replace("%mob%", event.getEntity().getType().getName()));
								} else {
									// Party mate
									if (Variables.exp_skillapi_enable) {
										SkillAPIHandler.giveExp(pl, givenExp);
										giveType = ExpGiveType.SKILLAPI_OTHER;
									} else {
										pl.giveExp((int) givenExp);
										giveType = ExpGiveType.NORMAL_OTHER;
									}
									plugin.getPlayerHandler().getPlayer(pl.getUniqueId()).sendMessage(Messages.expgainother
											.replace("%exp%", Double.toString(givenExp))
											.replace("%exptotal%", Double.toString(givenExp))
											.replace("%mob%", event.getEntity().getType().getName()), killer);
								}
								switch (giveType) {
								case NORMAL_OWN:
									LogHandler.log(LogLevel.DEBUG, pl.getName() + " got (" + givenExp + ") exp by killing (" + event.getEntity().getName() + ")", true);
									break;
								case NORMAL_OTHER:
									LogHandler.log(LogLevel.DEBUG, "Giving exp (" + givenExp + ") to " + pl.getName(), true);
									break;
								case SKILLAPI_OWN:
									LogHandler.log(LogLevel.DEBUG, pl.getName() + " got SkillAPI exp (" + givenExp + ") by killing (" + event.getEntity().getName() + ")", true);
								case SKILLAPI_OTHER:
									LogHandler.log(LogLevel.DEBUG, "Giving SkillAPI exp (" + givenExp + ") to " + pl.getName(), true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private enum DamageType {
		UNSUPPORTED, PLAYER, ARROW, ENDERPEARL, SNOWBALL;
	}
	
	private enum ExpGiveType {
		NORMAL_OWN, NORMAL_OTHER, SKILLAPI_OWN, SKILLAPI_OTHER;
	}
}
