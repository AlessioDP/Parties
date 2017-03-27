package com.alessiodp.parties;

import java.util.ArrayList;
import java.util.logging.Level;

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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;
import com.alessiodp.parties.utils.addon.SkillAPIHandler;
import com.alessiodp.parties.utils.tasks.MotdTask;
import com.alessiodp.parties.utils.tasks.PartyDeleteTask;
import com.alessiodp.parties.utils.tasks.PortalTask;

public class PlayerListener implements Listener{
	Parties plugin;
	
	public PlayerListener(Parties instance){
		plugin = instance;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(player);
		
		plugin.getPlayerHandler().initSpy(player.getUniqueId());
		
		if(!tp.haveParty() && !Variables.default_enable)
			return;
		
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
		if(party != null){
			if(!party.getOnlinePlayers().contains(player))
				party.getOnlinePlayers().add(player);
			plugin.getPartyHandler().tag_addPlayer(player, party);
			if(!party.getMOTD().isEmpty()){
				new MotdTask(plugin, player).runTaskLater(plugin, Variables.motd_delay);
			}
			if(plugin.getPartyHandler().listPartyToDelete.containsKey(party.getName()))
				Bukkit.getScheduler().cancelTask(plugin.getPartyHandler().listPartyToDelete.get(party.getName()));
			LogHandler.log(3, player.getName() + "[" + player.getUniqueId() + "] entered in the game, party " + party.getName());
		} else {
			if(Variables.default_enable){
				party = plugin.getPartyHandler().loadParty(Variables.default_party);
				if(party != null){
					party.getMembers().add(tp.getUUID());
					party.getOnlinePlayers().add(tp.getPlayer());
					tp.setHaveParty(true);
					tp.setPartyName(party.getName());
					tp.setRank(Variables.rank_default);
					party.updateParty();
					tp.updatePlayer();
					
					plugin.getPartyHandler().tag_addPlayer(player, party);
					
					if(!party.getMOTD().isEmpty()){
						new MotdTask(plugin, player).runTaskLater(plugin, Variables.motd_delay);
					}
					if(plugin.getPartyHandler().listPartyToDelete.containsKey(party.getName()))
						Bukkit.getScheduler().cancelTask(plugin.getPartyHandler().listPartyToDelete.get(party.getName()));
					
					tp.sendMessage(Messages.defaultjoined, party);
					LogHandler.log(2, player.getName() + "[" + player.getUniqueId() + "] entered in the game, setted default party " + party.getName());
				} else {
					plugin.log(Level.WARNING, ConsoleColors.RED.getCode() + "Cannot load default party");
					LogHandler.log(1, "Cannot load default party");
				}
			}
		}
		if(player.hasPermission(PartiesPermissions.ADMIN_UPDATES.toString()) && Variables.warnupdates){
			if(plugin.isUpdateAvailable()){
				tp.sendMessage(Messages.availableupdate.replace("%version%", plugin.getNewUpdate()).replace("%thisversion%", plugin.getDescription().getVersion()));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event){
		Player p = event.getPlayer();
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		
		if(tp.haveParty()){
			if(tp.getHomeTask() != -1)
				plugin.getPlayerHandler().remHomeCount();
			Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
			if(party != null){
				if(party.getOnlinePlayers().contains(p))
					party.getOnlinePlayers().remove(p);
				if(Variables.database_type.equalsIgnoreCase("none")){
					PartyDeleteTask task = (PartyDeleteTask) new PartyDeleteTask(party.getName());
					task.runTaskLaterAsynchronously(plugin, Variables.database_none_delay * 20);
					if(Variables.database_none_delay > 0)
						plugin.getPartyHandler().listPartyToDelete.put(party.getName(), task.getTaskId());
				}
			}
			plugin.getPartyHandler().tag_removePlayer(p, party);
		}
		plugin.getPlayerHandler().removePlayer(p.getUniqueId());
	}
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event){
		Player p = event.getPlayer();
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		
		if(tp.haveParty()){
			if(tp.getHomeTask() != -1)
				plugin.getPlayerHandler().remHomeCount();
			Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
			if(party != null){
				if(party.getOnlinePlayers().contains(p))
					party.getOnlinePlayers().remove(p);
				if(Variables.database_type.equalsIgnoreCase("none")){
					PartyDeleteTask task = (PartyDeleteTask) new PartyDeleteTask(party.getName());
					task.runTaskLater(plugin, Variables.database_none_delay * 20);
					if(Variables.database_none_delay > 0)
						plugin.getPartyHandler().listPartyToDelete.put(party.getName(), task.getTaskId());
				}
			}
			plugin.getPartyHandler().tag_removePlayer(p, party);
		}
		plugin.getPlayerHandler().removePlayer(p.getUniqueId());
	}
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			if(!Variables.friendlyfire_enable)
				return;
			Player p = (Player) event.getEntity();
			Player attacker = (Player) event.getDamager();
			if(!Variables.friendlyfire_listworlds.contains("*") && !Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))
				return;
			
			ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
			
			if(tp.haveParty()){
				Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				if(party.getOnlinePlayers().contains(attacker)){
					plugin.getPlayerHandler().getThePlayer(attacker).sendMessage(Messages.canthitmates);
					if(Variables.friendlyfire_warn){
						for(Player onlineP : party.getOnlinePlayers()){
							if(onlineP.equals(attacker))
								continue;
							Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
							if(r != null)
								if(r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
									plugin.getPlayerHandler().getThePlayer(onlineP).sendMessage(Messages.warnondamage.replace("%player%", attacker.getName()).replace("%victim%", p.getName()));
						}
					}
					event.setCancelled(true);
				}
			}
		}
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Arrow){
			if(((Arrow)event.getDamager()).getShooter() instanceof Player){
				if(!Variables.friendlyfire_enable)
					return;
				Player p = (Player) event.getEntity();
				Player attacker = (Player) ((Arrow)event.getDamager()).getShooter();
				if(!Variables.friendlyfire_listworlds.contains("*") && !Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))
					return;
				
				ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
				
				if(tp.haveParty()){
					Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
					if(party.getOnlinePlayers().contains(attacker)){
						plugin.getPlayerHandler().getThePlayer(attacker).sendMessage(Messages.canthitmates);
						if(Variables.friendlyfire_warn){
							for(Player onlineP : party.getOnlinePlayers()){
								if(onlineP.equals(attacker))
									continue;
								Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
								if(r != null)
									if(r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
										plugin.getPlayerHandler().getThePlayer(onlineP).sendMessage(Messages.warnondamage.replace("%player%", attacker.getName()).replace("%victim%", p.getName()));
							}
						}
						event.setCancelled(true);
					}
				}
			}
		}
		if(event.getEntity() instanceof Player && event.getDamager() instanceof EnderPearl){
			if(((EnderPearl)event.getDamager()).getShooter() instanceof Player){
				if(!Variables.friendlyfire_enable)
					return;
				Player p = (Player) event.getEntity();
				Player attacker = (Player) ((EnderPearl)event.getDamager()).getShooter();
				if(!Variables.friendlyfire_listworlds.contains("*") && !Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))
					return;
				
				ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
				
				if(tp.haveParty()){
					Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
					if(party.getOnlinePlayers().contains(attacker)){
						plugin.getPlayerHandler().getThePlayer(attacker).sendMessage(Messages.canthitmates);
						if(Variables.friendlyfire_warn){
							for(Player onlineP : party.getOnlinePlayers()){
								if(onlineP.equals(attacker))
									continue;
								Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
								if(r != null)
									if(r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
										plugin.getPlayerHandler().getThePlayer(onlineP).sendMessage(Messages.warnondamage.replace("%player%", attacker.getName()).replace("%victim%", p.getName()));
							}
						}
						event.setCancelled(true);
					}
				}
			}
		}
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Snowball){
			if(((Snowball)event.getDamager()).getShooter() instanceof Player){
				if(!Variables.friendlyfire_enable)
					return;
				Player p = (Player) event.getEntity();
				Player attacker = (Player) ((Snowball)event.getDamager()).getShooter();
				if(!Variables.friendlyfire_listworlds.contains("*") && !Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))
					return;
				
				ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
				
				if(tp.haveParty()){
					Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
					if(party.getOnlinePlayers().contains(attacker)){
						plugin.getPlayerHandler().getThePlayer(attacker).sendMessage(Messages.canthitmates);
						if(Variables.friendlyfire_warn){
							for(Player onlineP : party.getOnlinePlayers()){
								if(onlineP.equals(attacker))
									continue;
								Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
								if(r != null)
									if(r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
										plugin.getPlayerHandler().getThePlayer(onlineP).sendMessage(Messages.warnondamage.replace("%player%", attacker.getName()).replace("%victim%", p.getName()));
							}
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event){
		if(event.getPotion().getShooter() instanceof Player){
			if(!Variables.friendlyfire_enable)
				return;
			Player attacker = (Player) event.getPotion().getShooter();
			if(!Variables.friendlyfire_listworlds.contains("*") && !Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))
				return;
			boolean cancel = false;
			for(PotionEffect pe : event.getEntity().getEffects()){
				switch(pe.getType().getName().toLowerCase()){
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
				if(cancel)
					break;
			}
			if(!cancel)
				return;
			for(LivingEntity e : event.getAffectedEntities()){
				if(e instanceof Player){
					if(attacker.equals(e))
						continue;
					ThePlayer tp = plugin.getPlayerHandler().getThePlayer((Player)e);
					
					if(tp.haveParty()){
						Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
						if(party.getOnlinePlayers().contains(attacker)){
							plugin.getPlayerHandler().getThePlayer(attacker).sendMessage(Messages.canthitmates);
							if(Variables.friendlyfire_warn){
								for(Player onlineP : party.getOnlinePlayers()){
									if(onlineP == attacker)
										continue;
									Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
									if(r != null)
										if(r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
											plugin.getPlayerHandler().getThePlayer(onlineP).sendMessage(Messages.warnondamage.replace("%player%", attacker.getName()).replace("%victim%", tp.getName()));
								}
							}
							event.setIntensity(e, 0);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onEntityCombustByEntity(EntityCombustByEntityEvent event){
		if(!(event.getEntity() instanceof Player))
			return;
		if(event.getCombuster() instanceof Projectile){
			if(((Projectile) event.getCombuster()).getShooter() instanceof Player){
				Player attacker = (Player)((Projectile) event.getCombuster()).getShooter();
				if(!Variables.friendlyfire_listworlds.contains("*") && !Variables.friendlyfire_listworlds.contains(attacker.getWorld().getName()))
					return;
				ThePlayer tp = plugin.getPlayerHandler().getThePlayer((Player)event.getEntity());
				
				if(tp.haveParty()){
					Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
					if(party.getOnlinePlayers().contains(attacker)){
						plugin.getPlayerHandler().getThePlayer(attacker).sendMessage(Messages.canthitmates);
						if(Variables.friendlyfire_warn){
							for(Player onlineP : party.getOnlinePlayers()){
								if(onlineP == attacker)
									continue;
								Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
								if(r != null)
									if(r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
										plugin.getPlayerHandler().getThePlayer(onlineP).sendMessage(Messages.warnondamage.replace("%player%", attacker.getName()).replace("%victim%", tp.getName()));
							}
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler
	public void onEntityPortalEvent(PlayerPortalEvent event){
		if(event.isCancelled())
			return;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer((Player)event.getPlayer());
		if(tp.getPortalTimeout() != -1)
			event.setCancelled(true);
	}
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event){
		if(!Variables.follow_enable)
			return;
		if(!Variables.follow_listworlds.contains("*") && !Variables.follow_listworlds.contains(event.getPlayer().getWorld().getName()))
			return;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(event.getPlayer());
		if(tp.haveParty()){
			Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
			if(party != null){
				if(tp.getRank() < Variables.follow_neededrank)
					return;
				for(Player pl : party.getOnlinePlayers()){
					if(pl.getUniqueId().equals(event.getPlayer().getUniqueId()))
						continue;
					if(pl.getWorld().equals(event.getPlayer().getWorld()))
						continue;
					ThePlayer victim = plugin.getPlayerHandler().getThePlayer(pl);
					if(victim.getRank() < Variables.follow_minimumrank)
						continue;
					switch(Variables.follow_type){
					case 1:
						victim.setPortalTimeout(new PortalTask(pl.getUniqueId()).runTaskLaterAsynchronously(plugin, Variables.follow_timeoutportal).getTaskId());
						plugin.getPlayerHandler().getThePlayer(pl).sendMessage(Messages.follow_following_world.replace("%player%", event.getPlayer().getName()).replace("%world%", event.getPlayer().getWorld().getName()));
						pl.teleport(event.getPlayer().getWorld().getSpawnLocation());
						break;
					case 2:
						victim.setPortalTimeout(new PortalTask(pl.getUniqueId()).runTaskLaterAsynchronously(plugin, Variables.follow_timeoutportal).getTaskId());
						plugin.getPlayerHandler().getThePlayer(pl).sendMessage(Messages.follow_following_world.replace("%player%", event.getPlayer().getName()).replace("%world%", event.getPlayer().getWorld().getName()));
						pl.teleport(event.getPlayer());
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		if(event.isCancelled())
			return;
		Player p = event.getPlayer();
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		if(tp.haveParty() && tp.chatParty()){
			Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
			if(r != null){
				if(!r.havePermission(PartiesPermissions.PRIVATE_SENDMESSAGE.toString())){
					Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_SENDMESSAGE.toString());
					if(rr!=null)
						tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
					else
						tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.SENDMESSAGE.toString()));
					event.setCancelled(true);
				} else {
					Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
					
					party.sendPlayerMessage(p, event.getMessage());
					party.sendSpyMessage(p, event.getMessage());
					if(Variables.log_chat)
						LogHandler.log(1, "Chat of "+party.getName()+":" + p.getName() + "[" + p.getUniqueId() + "]:"+event.getMessage());
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDie(EntityDeathEvent event){
		if(!(event.getEntity().getKiller() instanceof Player))
			return;
		if(!Variables.exp_enable)
			return;
		double exp = event.getDroppedExp();
		double exptotal = exp;
		event.setDroppedExp(0);
		ArrayList<Player> list = new ArrayList<Player>();
		
		Player killer = event.getEntity().getKiller();
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(killer);
		
		list.add(killer);
		if(tp.haveParty()){
			Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
				for(Player p : party.getOnlinePlayers()){
					if(killer.getLocation().distance(p.getLocation()) < Variables.exp_range && (p != killer)){
						list.add(p);
					}
			}
		}
		if(list.size() < 2){
			event.setDroppedExp((int) exp);
			return;
		}
		// SkillAPI watcher
		if(SkillAPIHandler.active)
			exp = SkillAPIHandler.getExp(exp, event.getEntity());
		
		if(exp<1)
			return;
		if(exp==1){
			// Giving exp
			if(SkillAPIHandler.active)
				SkillAPIHandler.giveExp(killer, exp);
			else
				killer.giveExp((int) exp);
			tp.sendMessage(Messages.expgain.replace("%exp%", exp+"").replace("%exptotal%", exptotal+"").replace("%mob%", event.getEntity().getType().getName()));
			return;
		}
		exp /= list.size();
		for(int c=0;c<list.size();c++){
			// Giving exp to party
			if(SkillAPIHandler.active)
				SkillAPIHandler.giveExp(list.get(c), exp);
			else
				list.get(c).giveExp((int) exp);
			
			if(list.get(c) == killer)
				tp.sendMessage(Messages.expgain.replace("%exp%", exp+"").replace("%exptotal%", exptotal+"").replace("%mob%", event.getEntity().getType().getName()));
			else
				plugin.getPlayerHandler().getThePlayer(list.get(c)).sendMessage(Messages.expgainother.replace("%exp%", exp+"").replace("%exptotal%", exptotal+"").replace("%mob%", event.getEntity().getType().getName()), killer);
		}
	}
	@EventHandler
	public void onEntityDieKills(EntityDeathEvent event){
		if(!(event.getEntity().getKiller() instanceof Player) || !Variables.kill_enable)
			return;
		
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(event.getEntity().getKiller());
		if(!tp.haveParty())
			return;
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
		if(event.getEntity() instanceof Monster && Variables.kill_save_mobshostile){
			party.setKills(party.getKills()+1);
			party.updateParty();
		} else if(event.getEntity() instanceof Animals && Variables.kill_save_mobsneutral){
			party.setKills(party.getKills()+1);
			party.updateParty();
		} else if(event.getEntity() instanceof Player && Variables.kill_save_players){
			party.setKills(party.getKills()+1);
			party.updateParty();
		}
		LogHandler.log(3, "Adding a kill to the party " + party.getName());
		return;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.getFrom().getBlockX() == event.getTo().getBlockX() &&
				event.getFrom().getBlockY() == event.getTo().getBlockY() &&
					event.getFrom().getBlockZ() == event.getTo().getBlockZ())
			return;
		if(!Variables.home_cancelmove)
			return;
		if(plugin.getPlayerHandler().getHomeCount() == 0)
			return;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(event.getPlayer());
		if(tp.getHomeTask() != -1){
			if(tp.getHomeFrom().distance(event.getTo()) > Variables.home_distance){
				plugin.getServer().getScheduler().cancelTask(tp.getHomeTask());
				tp.setHomeTask(-1);
				plugin.getPlayerHandler().remHomeCount();
				tp.sendMessage(Messages.home_denied);
				LogHandler.log(3, "Denied home of " + tp.getName() + "[" + tp.getUUID() + "] because move");
			}
		}
	}
	@EventHandler
	public void onPlayerHittedHome(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			if(!Variables.home_cancelmove)
				return;
			if(plugin.getPlayerHandler().getHomeCount() == 0)
				return;
			ThePlayer tp = plugin.getPlayerHandler().getThePlayer((Player)event.getEntity());
			if(tp.getHomeTask() != -1){
				plugin.getServer().getScheduler().cancelTask(tp.getHomeTask());
				tp.setHomeTask(-1);
				plugin.getPlayerHandler().remHomeCount();
				tp.sendMessage(Messages.home_denied);
				LogHandler.log(3, "Denied home of " + tp.getName() + "[" + tp.getUUID() + "] because hitted");
			}
		}
	}
	/*
	 * Auto-command
	 */
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled())
			return;
		if(!Variables.autocommand_enable)
			return;
		if(event.getMessage().endsWith("\t")){
			event.setMessage(event.getMessage().replace("\t", ""));
			return;
		}
		boolean cancel = false;
		for(String str : Variables.autocommand_blacklist){
			if(str.equalsIgnoreCase("*") || event.getMessage().toLowerCase().startsWith(str.toLowerCase())){
				cancel = true;
				break;
			}
		}
		for(String str : Variables.autocommand_whitelist){
			if(str.equalsIgnoreCase("*") && event.getMessage().toLowerCase().startsWith(str.toLowerCase())){
				cancel = false;
				break;
			}
		}
		if(cancel)
			return;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(event.getPlayer());
		if(tp.haveParty()){
			Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
			if(r != null){
				if(r.havePermission(PartiesPermissions.PRIVATE_AUTOCOMMAND.toString())){
					Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
					for(Player pl : party.getOnlinePlayers()){
						if(pl.getUniqueId().equals(event.getPlayer().getUniqueId()))
							continue;
						pl.chat(event.getMessage()+"\t");
						LogHandler.log(2, "[" + pl.getUniqueId() + "] using autocommand '" + event.getMessage() + "'");
					}
				}
			}
		}
	}
}
