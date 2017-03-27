package com.alessiodp.parties.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.utils.addon.ProtocolHandler;


public class PartyHandler {
	private Parties plugin;
	
	private ArrayList<Rank> ranks;
	private Scoreboard scoreboard;
	
	public HashMap<String, Party> listParty;
	public HashMap<String, Integer> listPartyToDelete;
	
	public PartyHandler(Parties instance){
		LogHandler.log(3, "Initializing PartyHandler");
		plugin = instance;
		ranks = Variables.rank_list;
		
		listParty = new HashMap<String, Party>();
		listPartyToDelete = new HashMap<String, Integer>();
		
		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		tag_reset();
		checkParties();
	}
	private void checkParties(){
		if(Variables.fixedparty){
			List<String> lst = plugin.getConfigHandler().getData().getAllFixed();
			for(String party : lst){
				LogHandler.log(3, "Loading fixed party " + party);
				loadParty(party);
			}
		}
	}
	/*
	 * Party Management
	 */
	
	public Party loadParty(String name){
		if(name == null || name.isEmpty())
			return null;
		Party party = listParty.get(name);
		if(Variables.database_type.equalsIgnoreCase("none"))
			return party;
		if(party == null){
			if(!plugin.getConfigHandler().getData().existParty(name))
				return null;
			
			party = plugin.getConfigHandler().getData().getParty(name);
			for(Player p : plugin.getServer().getOnlinePlayers()){
				if(party.getMembers().contains(p.getUniqueId()))
					party.getOnlinePlayers().add(p);
			}
			listParty.put(party.getName(), party);
			LogHandler.log(3, "Got party " + party.getName() + " from database");
		} else {
			LogHandler.log(3, "Loaded party " + party.getName() + " from list");
		}
		return party;
	}
	public boolean existParty(String name){
		Party party = (Party)listParty.get(name);
		if(party != null)
			return true;
		
		if(Variables.database_type.equalsIgnoreCase("none"))
			return false;
		if(plugin.getConfigHandler().getData().existParty(name))
			return true;
		return false;
	}
	public void deleteTimedParty(String name, boolean forced){
		Party party = loadParty(name);
		if(party != null){
			if(Variables.database_type.equalsIgnoreCase("none")){
				if(party.getOnlinePlayers().size() == 0){
					party.removeParty();
					LogHandler.log(3, "Deleted party " + party.getName() + " because empty" + (forced ? " (forced)" : ""));
				} else if(Variables.database_none_leaderleft){
					if(!Bukkit.getOfflinePlayer(party.getLeader()).isOnline()){
						party.sendBroadcastParty(Bukkit.getOfflinePlayer(party.getLeader()), Messages.leave_disbanded);
						party.removeParty();
						LogHandler.log(3, "Deleted party " + party.getName() + " because leader left" + (forced ? " (forced)" : ""));
					}
				}
				if(listPartyToDelete.containsKey(name))
					listPartyToDelete.remove(name);
			}
		}
	}
	/*
	 * Rank system
	 */
	public Rank searchRank(int level){
		Rank r = null;
		for(int c=0;c<ranks.size();c++){
			if(ranks.get(c).getLevel() == level){
				r = ranks.get(c);
				break;
			}
		}
		if(r == null)
			r = plugin.getPartyHandler().defaultRank();
		return r;
	}
	public Rank searchRank(String name){
		Rank r = null;
		for(int c=0;c<ranks.size();c++){
			if(ranks.get(c).getName().equalsIgnoreCase(name)){
				r = ranks.get(c);
				break;
			}
		}
		return r;
	}
	public Rank defaultRank(){
		Rank r = null;
		for(int c=0;c<ranks.size();c++){
			if(ranks.get(c).getDefault()){
				r = ranks.get(c);
				break;
			}
		}
		return r;
	}
	public Rank searchUpRank(int base, String perm){
		Rank rank = null;
		for(int c=0;c<ranks.size();c++){
			if(ranks.get(c).getLevel() > base){
				if(ranks.get(c).havePermission(perm)){
					rank = ranks.get(c);
					break;
				}
			}
		}
		return rank;
	}
	public void insertRank(Rank r){
		if(!ranks.contains(r))
			ranks.add(r);
	}
	public void removeRank(Rank r){
		if(ranks.contains(r))
			ranks.remove(r);
	}
	public ArrayList<Rank> getRankList(){return ranks;}
	public void setRankList(ArrayList<Rank> ar){ranks = ar;}
	
	/*
	 *  Scoreboard system
	 */
	public void tag_refresh(Party party){
		if(party==null)
			return;
		if(Variables.tablist_enable){
			ProtocolHandler.handleHF();
			for(Player pl : party.getOnlinePlayers())
				ProtocolHandler.send(pl.getUniqueId());
		}
		Team t = scoreboard.getTeam(plugin.getScoreboardPrefix()+party.getName().toLowerCase());
		if(t != null)
			t.unregister();
		if(Variables.tag_enable || Variables.invisibleallies){
			if(party.getOnlinePlayers().size()>0){
				String str = plugin.getScoreboardPrefix()+party.getName().toLowerCase();
				if(str.length() > 10)
					str = str.substring(0, 10);
			    Team team = scoreboard.getTeam(str);
			    if (team == null) {
			      team = scoreboard.registerNewTeam(str);
			      if(Variables.tag_enable && Variables.tag_system){
			    	  team.setPrefix(ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatprefix).replace("%party%", party.getName()));
			    	  team.setSuffix(ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatsuffix).replace("%party%", party.getName()));
			      } else if(Variables.tag_enable && !Variables.tag_system){
			    	  if(Variables.tag_custom_prefix)
			    		  if(!party.getPrefix().isEmpty())
			    			  team.setPrefix(ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatprefix).replace("%prefix%", party.getPrefix()));
			    	  else
			    		  team.setPrefix("");
			          if(Variables.tag_custom_suffix)
			        	  if(!party.getSuffix().isEmpty())
			        		  team.setSuffix(ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatsuffix).replace("%suffix%", party.getSuffix()));
			          else
			        	  team.setSuffix("");
			      } else {
			    	  team.setPrefix("");
			    	  team.setSuffix("");
			      }
			      if(Variables.invisibleallies)
			    	  team.setCanSeeFriendlyInvisibles(true);
			      else
			    	  team.setCanSeeFriendlyInvisibles(false);
			    }
				for(Player player : party.getOnlinePlayers()){
					team.addPlayer(player);
				}
			}
		}
	}
	public void tag_addPlayer(Player player, Party party){
		if(Variables.tablist_enable){
			ProtocolHandler.handleHF();
			if(party != null)
				for(Player pl : party.getOnlinePlayers())
					ProtocolHandler.send(pl.getUniqueId());
		}
		if(Variables.tag_system || Variables.invisibleallies){
			if(party != null){
				String str = plugin.getScoreboardPrefix()+party.getName().toLowerCase();
				if(str.length() > 10)
					str = str.substring(0, 10);
			    Team team = scoreboard.getTeam(str);
			    if (team == null) {
			      team = scoreboard.registerNewTeam(str);
			      if(Variables.tag_enable && Variables.tag_system){
			    	  team.setPrefix(ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatprefix).replace("%party%", party.getName()));
			    	  team.setSuffix(ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatsuffix).replace("%party%", party.getName()));
			      } else if(Variables.tag_enable && !Variables.tag_system){
			    	  if(Variables.tag_custom_prefix)
			    		  if(!party.getPrefix().isEmpty())
			    			  team.setPrefix(ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatprefix).replace("%prefix%", party.getPrefix()));
			    	  else
			    		  team.setPrefix("");
			          if(Variables.tag_custom_suffix)
			        	  if(!party.getSuffix().isEmpty())
			        		  team.setSuffix(ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatsuffix).replace("%suffix%", party.getSuffix()));
			          else
			        	  team.setSuffix("");
			      } else {
			    	  team.setPrefix("");
			    	  team.setSuffix("");
			      }
			      if(Variables.invisibleallies)
			    	  team.setCanSeeFriendlyInvisibles(true);
			      else
			    	  team.setCanSeeFriendlyInvisibles(false);
			    }
			    team.addPlayer(player);
			}
		}
	}
	public void tag_removePlayer(Player player, Party party){
		if(Variables.tablist_enable){
			ProtocolHandler.handleHF();
			ProtocolHandler.send(player.getUniqueId());
			if(party != null)
				for(Player pl : party.getOnlinePlayers())
					ProtocolHandler.send(pl.getUniqueId());
		}
		if(Variables.tag_system || Variables.invisibleallies){
			Team team = scoreboard.getPlayerTeam(player);
		    if ((team != null) && (team.getName().startsWith(plugin.getScoreboardPrefix())))
		    	team.removePlayer(player);
		}
	}
	public void tag_delete(Party party){
		if(party==null)
			return;
		if(Variables.tablist_enable){
			ProtocolHandler.handleHF();
			for(Player pl : party.getOnlinePlayers())
				ProtocolHandler.send(pl.getUniqueId());
		}
		if(Variables.tag_system || Variables.invisibleallies){
			Team t = scoreboard.getTeam(plugin.getScoreboardPrefix()+party.getName().toLowerCase());
			if(t != null){
				for(OfflinePlayer p : t.getPlayers())
					t.removePlayer(p);
				t.unregister();
			}
		}
	}
	public void tag_reset(){
		if(scoreboard == null)
			return;
		for(Team team : scoreboard.getTeams()){
			if((team != null) && (team.getName().startsWith(plugin.getScoreboardPrefix())))
				team.unregister();
		}
	}
}
