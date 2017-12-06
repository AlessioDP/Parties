package com.alessiodp.parties.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.utils.addon.ProtocolHandler;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.interfaces.Rank;


public class PartyHandler {
	private Parties plugin;
	
	private List<Rank> ranks;
	private Scoreboard scoreboard;
	
	private HashMap<String, Party> listParty;
	private HashMap<String, Integer> listPartyToDelete;
	
	public PartyHandler(Parties instance) {
		LogHandler.log(LogLevel.DEBUG, "Initializing PartyHandler", true);
		plugin = instance;
		ranks = Variables.rank_list;
		
		listParty = new HashMap<String, Party>();
		listPartyToDelete = new HashMap<String, Integer>();
	}
	
	public void init() {
		// Used to avoid NullPointer to PartyHandler.class from Parties.class
		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		tag_reset();
		
		if (Variables.fixedparty) {
			List<String> lst = plugin.getDatabaseDispatcher().getAllFixed();
			for (String party : lst) {
				LogHandler.log(LogLevel.DEBUG, "Loading fixed party " + party, true);
				loadParty(party);
			}
		}
	}
	public void reloadParties() {
		ranks = Variables.rank_list;
	}
	
	/*
	 * Party Management
	 */
	public Party loadParty(String name) {
		// Get the party and save it into the party list
		Party ret = getParty(name);
		getListParties().put(name.toLowerCase(), ret);
		return ret;
	}
	public void unloadParty(String name) {
		getListParties().remove(name.toLowerCase());
	}
	public Party getParty(String name) {
		// Just get the party without save it into the party list
		Party ret = null;
		if (name != null && !name.isEmpty()) {
			ret = getListParties().get(name.toLowerCase());
			if (ret == null) {
				ret = plugin.getDatabaseDispatcher().getParty(name);
				if (ret != null)
					LogHandler.log(LogLevel.DEBUG, "Got party " + ret.getName() + " from database", true);
			} else
				LogHandler.log(LogLevel.DEBUG, "Loaded party " + ret.getName() + " from list", true);
		}
		
		if (ret != null) {
			ret.refreshPlayers();
		}
		return ret;
	}
	public boolean existParty(String name) {
		boolean ret = false;
		Party party = (Party) getListParties().get(name.toLowerCase());
		if (party != null || plugin.getDatabaseDispatcher().existParty(name))
			ret = true;
		return ret;
	}
	public void deleteTimedParty(String name, boolean leaderLeft) {
		Party party = getParty(name);
		if (party != null) {
			// Calling Pre API event
			PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party.getName(), PartiesPartyPreDeleteEvent.DeleteCause.TIMEOUT, null, null);
			Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
			if (!partiesPreDeleteEvent.isCancelled()) {
				for (UUID u : plugin.getPlayerHandler().getListPlayersToDelete()) {
					if (party.getMembers().contains(u))
						plugin.getPlayerHandler().getListPlayers().remove(u);
				}
				String cause = "empty";
				if (leaderLeft) {
					party.sendBroadcastParty(Bukkit.getOfflinePlayer(party.getLeader()), Messages.leave_disbanded);
					cause = "leader left";
				}
				
				party.removeParty();
				// Calling Post API event
				PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.TIMEOUT, null, null);
				Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
				LogHandler.log(LogLevel.DEBUG, "Deleted party " + party.getName() + " because " + cause, true);
				
				if (getListPartiesToDelete().containsKey(name.toLowerCase()))
					getListPartiesToDelete().remove(name.toLowerCase());
			} else {
				LogHandler.log(LogLevel.DEBUG, "PartiesDeleteEvent is cancelled, ignoring delete of " + party.getName(), true);
			}
		}
	}
	public HashMap<String, Party> getListParties() {return listParty;}
	public HashMap<String, Integer> getListPartiesToDelete() {return listPartyToDelete;}
	/*
	 * Rank system
	 */
	public Rank searchRank(int level) {
		Rank r = null;
		for (int c = 0; c < ranks.size(); c++) {
			if (ranks.get(c).getLevel() == level) {
				r = ranks.get(c);
				break;
			}
		}
		if (r == null)
			r = plugin.getPartyHandler().defaultRank();
		return r;
	}
	public Rank searchRank(String hardname) {
		Rank r = null;
		for (int c = 0; c < ranks.size(); c++) {
			if (ranks.get(c).getHardName().equalsIgnoreCase(hardname)) {
				r = ranks.get(c);
				break;
			}
		}
		return r;
	}
	public Rank defaultRank() {
		Rank r = null;
		for (int c = 0; c < ranks.size(); c++) {
			if (ranks.get(c).getDefault()) {
				r = ranks.get(c);
				break;
			}
		}
		return r;
	}
	public Rank searchUpRank(int base, String perm) {
		Rank rank = null;
		for (int c = 0; c < ranks.size(); c++) {
			if (ranks.get(c).getLevel() > base) {
				if (ranks.get(c).havePermission(perm)) {
					rank = ranks.get(c);
					break;
				}
			}
		}
		return rank;
	}
	public void insertRank(Rank r) {
		if (!ranks.contains(r))
			ranks.add(r);
	}
	public void removeRank(Rank r) {
		if (ranks.contains(r))
			ranks.remove(r);
	}
	public List<Rank> getRankList() {return ranks;}
	public void setRankList(List<Rank> ar) {ranks = ar;}
	
	/*
	 * Color system
	 */
	public Color searchColorByName(String name) {
		Color ret = null;
		for (Color pc : Variables.color_list) {
			if (pc.getName().equalsIgnoreCase(name)) {
				ret = pc;
				break;
			}
		}
		return ret;
	}
	public Color searchColorByCommand(String cmd) {
		Color ret = null;
		for (Color pc : Variables.color_list) {
			if (pc.getCommand().equalsIgnoreCase(cmd)) {
				ret = pc;
				break;
			}
		}
		return ret;
	}
	public void loadDynamicColor(Party party) {
		if (Variables.color_enable && Variables.color_dynamic && party.getColor() == null) {
			Color selected = null;
			for (Color pc : Variables.color_list) {
				boolean found = false;
				if (pc.getDynamicMembers() > -1) {
					if (party.getMembers().size() >= pc.getDynamicMembers())
						found = true;
				} else if (pc.getDynamicKills() > -1) {
					if (party.getKills() >= pc.getDynamicKills())
						found = true;
				}
				
				if (found) {
					if (selected == null || pc.getDynamicPriority() > selected.getDynamicPriority())
						selected = pc;
				}
			}
			
			if (selected != null)
				party.setTemporaryColor(selected);
		}
	}
	/*
	 * Scoreboard system
	 */
	public void tag_refresh(Party party) {
		if (party != null) {
			// ProtocolLib
			if (Variables.tablist_enable && ProtocolHandler.isActive()) {
				ProtocolHandler.handleHF();
				for (Player pl : party.getOnlinePlayers())
					ProtocolHandler.send(pl.getUniqueId());
			}
			// Scoreboard
			Team t = scoreboard.getTeam(plugin.getScoreboardPrefix() + party.getName().toLowerCase());
			if (t != null)
				t.unregister();
			if (Variables.tag_enable || Variables.invisibleallies) {
				if (party.getOnlinePlayers().size() > 0) {
					String str = plugin.getScoreboardPrefix()+party.getName().toLowerCase();
					if (str.length() > 10)
						str = str.substring(0, 10);
					Team team = scoreboard.getTeam(str);
					if (team == null) {
						team = scoreboard.registerNewTeam(str);
						if (Variables.tag_enable && Variables.tag_system) {
							team.setPrefix(ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatprefix).replace("%party%", party.getName()));
							team.setSuffix(ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatsuffix).replace("%party%", party.getName()));
						} else if (Variables.tag_enable && !Variables.tag_system) {
							if (Variables.tag_custom_prefix)
								if (!party.getPrefix().isEmpty())
									team.setPrefix(ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatprefix).replace("%prefix%", party.getPrefix()));
								else
									team.setPrefix("");
							if (Variables.tag_custom_suffix)
								if (!party.getSuffix().isEmpty())
									team.setSuffix(ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatsuffix).replace("%suffix%", party.getSuffix()));
								else
									team.setSuffix("");
						} else {
							team.setPrefix("");
							team.setSuffix("");
						}
						if (Variables.invisibleallies)
							team.setCanSeeFriendlyInvisibles(true);
						else
							team.setCanSeeFriendlyInvisibles(false);
					}
					for (Player player : party.getOnlinePlayers()) {
						team.addEntry(player.getName());
					}
				}
			}
		}
	}
	public void tag_addPlayer(Player player, Party party) {
		if (player != null) {
			// ProtocolLib
			if (Variables.tablist_enable && ProtocolHandler.isActive()) {
				ProtocolHandler.handleHF();
				if (party != null)
					for (Player pl : party.getOnlinePlayers())
						ProtocolHandler.send(pl.getUniqueId());
			}
			// Scoreboard
			if (Variables.tag_system || Variables.invisibleallies) {
				if (party != null) {
					String str = plugin.getScoreboardPrefix()+party.getName().toLowerCase();
					if (str.length() > 10)
						str = str.substring(0, 10);
					Team team = scoreboard.getTeam(str);
					if (team == null) {
						String fix;
						team = scoreboard.registerNewTeam(str);
						if (Variables.tag_enable && Variables.tag_system) {
							fix = ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatprefix).replace("%party%", party.getName());
							if (fix.length() > 16)
								fix = fix.substring(0,16);
							team.setPrefix(fix);
							
							fix = ChatColor.translateAlternateColorCodes('&', Variables.tag_base_formatsuffix).replace("%party%", party.getName());
							if (fix.length() > 16)
								fix = fix.substring(0,16);
							team.setSuffix(fix);
						} else if (Variables.tag_enable && !Variables.tag_system) {
							if (Variables.tag_custom_prefix) {
								if (!party.getPrefix().isEmpty()) {
									fix = ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatprefix).replace("%prefix%", party.getPrefix());
									if (fix.length() > 16)
										fix = fix.substring(0,16);
									team.setPrefix(fix);
								} else
									team.setPrefix("");
							}
							if (Variables.tag_custom_suffix) {
								if (!party.getSuffix().isEmpty()) {
									fix = ChatColor.translateAlternateColorCodes('&', Variables.tag_custom_formatsuffix).replace("%suffix%", party.getSuffix());
									if (fix.length() > 16)
										fix = fix.substring(0,16);
									team.setSuffix(fix);
								} else
									team.setSuffix("");
							}
						} else {
							team.setPrefix("");
							team.setSuffix("");
						}
						if (Variables.invisibleallies)
							team.setCanSeeFriendlyInvisibles(true);
						else
							team.setCanSeeFriendlyInvisibles(false);
					}
					team.addEntry(player.getName());
				}
			}
		}
	}
	public void tag_removePlayer(Player player, Party party) {
		if (player != null) {
			// ProtocolLib
			if (Variables.tablist_enable && ProtocolHandler.isActive()) {
				ProtocolHandler.handleHF();
				ProtocolHandler.send(player.getUniqueId()); // Player is not present into the party anymore
				if (party != null)
					for (Player pl : party.getOnlinePlayers())
						ProtocolHandler.send(pl.getUniqueId());
			}
			// Scoreboard
			if (Variables.tag_system || Variables.invisibleallies) {
				Team team = scoreboard.getEntryTeam(player.getName());
				if (team != null && team.getName().startsWith(plugin.getScoreboardPrefix()))
					team.removeEntry(player.getName());
			}
		}
	}
	public void tag_delete(Party party) {
		// ProtocolLib
		if (party != null && Variables.tablist_enable && ProtocolHandler.isActive()) {
			ProtocolHandler.handleHF();
			for (Player pl : party.getOnlinePlayers())
				ProtocolHandler.send(pl.getUniqueId());
		}
		// Scoreboard
		if (Variables.tag_system || Variables.invisibleallies) {
			Team t = scoreboard.getTeam(plugin.getScoreboardPrefix() + party.getName().toLowerCase());
			if (t != null) {
				for (String e : t.getEntries())
					t.removeEntry(e);
				t.unregister();
			}
		}
	}
	public void tag_reset() {
		if (scoreboard != null) {
			for (Team team : scoreboard.getTeams()) {
				if (team != null && team.getName().startsWith(plugin.getScoreboardPrefix()))
					team.unregister();
			}
		}
	}
}
