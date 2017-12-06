package com.alessiodp.parties.utils.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.partiesapi.enums.Status;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.interfaces.PartiesAPI;
import com.alessiodp.partiesapi.interfaces.Rank;

public class ApiHandler implements PartiesAPI {
	private Parties plugin;
	
	public ApiHandler(Parties instance) {
		plugin = instance;
	}
	
	/*
	 * 
	 * Parties based
	 * 
	 */
	public void reloadParties() {
		plugin.reloadConfiguration();
	}
	public Status broadcastPartyMessage(String paramParty, UUID paramUUID, String paramMessage) {
		Status ret = Status.NOEXIST;
		Party party = plugin.getPartyHandler().loadParty(paramParty);
		if (party != null) {
			party.sendBroadcastParty(Bukkit.getOfflinePlayer(paramUUID), paramMessage);
			ret = Status.SUCCESS;
		}
		return ret;
	}
	public List<String> getOnlineParties() {
		List<String> ret = new ArrayList<String>();
		// The key of the entry is case insensitive
		for (Entry<String, Party> entry : plugin.getPartyHandler().getListParties().entrySet()) {
			ret.add(entry.getValue().getName());
		}
		return ret;
	}
	/*
	 * 
	 * Player based
	 * 
	 */
	public Status addPlayerIntoParty(UUID paramUUID, String paramParty) {
		Status ret = Status.ALREADYINPARTY;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(paramUUID);
		if (tp.getPartyName().isEmpty()) {
			Party party = plugin.getPartyHandler().getParty(paramParty);
			if (party != null) {
				if (Variables.party_maxmembers < 0 || party.getMembers().size() < Variables.party_maxmembers) {
					party.getMembers().add(paramUUID);
					tp.setPartyName(party.getName());
					
					party.refreshPlayers();
					party.updateParty();
					tp.updatePlayer();
					ret = Status.SUCCESS;
				} else
					ret = Status.PARTYFULL;
			} else
				ret = Status.NOEXIST;
		}
		return ret;
	}
	public Status removePlayerFromParty(UUID paramUUID) {
		Status ret = Status.NOPARTY;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(paramUUID);
		if (!tp.getPartyName().isEmpty()) {
			Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
			if (party != null) {
				if (paramUUID.equals(party.getLeader())) {
					party.removeParty();
				} else {
					if (party.getMembers().contains(paramUUID)) {
						party.getMembers().remove(paramUUID);
						party.updateParty();
					}
					tp.cleanupPlayer(true);
				}
				ret = Status.SUCCESS;
			}
		}
		return ret;
	}
	
	public boolean haveParty(UUID paramUUID) {
		return !plugin.getPlayerHandler().getPlayer(paramUUID).getPartyName().isEmpty();
	}
	
	public boolean isSpy(UUID paramUUID) {
		return plugin.getPlayerHandler().isSpy(paramUUID);
	}
	public void setSpy(UUID paramUUID, boolean paramSpy) {
		plugin.getPlayerHandler().setSpy(paramUUID, paramSpy);
	}
	
	public String getPartyName(UUID paramUUID) {
		return plugin.getPlayerHandler().getPlayer(paramUUID).getPartyName();
	}
	
	public int getRank(UUID paramUUID) {
		return plugin.getPlayerHandler().getPlayer(paramUUID).getRank();
	}
	public void setRank(UUID paramUUID, int paramRank) {
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(paramUUID);
		tp.setRank(paramRank);
		tp.updatePlayer();
	}
	
	public List<Rank> getRankList() {
		return plugin.getPartyHandler().getRankList();
	}
	
	/*
	 * 
	 * Party based
	 * 
	 */
	
	public Status createParty(UUID paramLeader, String paramParty) {
		Status ret = Status.ALREADYINPARTY;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(paramLeader);
		if (tp.getPartyName().isEmpty()) {
			if (!plugin.getPartyHandler().existParty(paramParty)) {
				Party party = new Party(paramParty, plugin);
				party.getMembers().add(paramLeader);
				Player p = tp.getPlayer();
				if (p != null)
					party.addOnlinePlayer(p);
				party.setLeader(paramLeader);
				plugin.getPartyHandler().getListParties().put(party.getName().toLowerCase(), party);
				
				tp.setRank(Variables.rank_last);
				tp.setPartyName(paramParty);

				party.updateParty();
				tp.updatePlayer();

				plugin.getPartyHandler().tag_refresh(party);
				return Status.SUCCESS;
			} else
				ret = Status.ALREADYEXISTPARTY;
		}
		return ret;
	}
	
	public Status deleteParty(String paramParty) {
		Status ret = Status.NOEXIST;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.removeParty();
			ret = Status.SUCCESS;
		}
		return ret;
	}
	
	public UUID getPartyLeader(String paramParty) {
		UUID ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getLeader();
		}
		return ret;
	}
	public void setPartyLeader(String paramParty, UUID paramUUID) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setLeader(paramUUID);
			party.updateParty();
		}
	}
	
	public List<UUID> getPartyMembers(String paramParty) {
		List<UUID> ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getMembers();
		}
		return ret;
	}
	public void setPartyMembers(String paramParty, List<UUID> paramListPlayers) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setMembers(paramListPlayers);
			party.updateParty();
		}
	}
	
	public List<Player> getPartyOnlinePlayers(String paramParty) {
		List<Player> ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getOnlinePlayers();
		}
		return ret;
	}
	public void refreshOnlinePlayers(String paramParty) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.refreshPlayers();
		}
	}
	
	public boolean isPartyFixed(String paramParty) {
		boolean ret = false;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.isFixed();
		}
		return ret;
	}
	public void setPartyFixed(String paramParty, boolean paramFixed) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setFixed(paramFixed);
			party.updateParty();
		}
	}
	
	public String getPartyDescription(String paramParty) {
		String ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getDescription();
		}
		return ret;
	}
	public void setPartyDescription(String paramParty, String paramDescription) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setDescription(paramDescription);
			party.updateParty();
		}
	}
	
	public String getPartyMotd(String paramParty) {
		String ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getMOTD();
		}
		return ret;
	}
	public void setPartyMotd(String paramParty, String paramMotd) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setMOTD(paramMotd);
			party.updateParty();
		}
	}
	
	public Location getPartyHome(String paramParty) {
		Location ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getHome();
		}
		return ret;
	}
	public void setPartyHome(String paramParty, Location paramHome) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setHome(paramHome);
			party.updateParty();
		}
	}
	
	public String getPartyPrefix(String paramParty) {
		String ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getPrefix();
		}
		return ret;
	}
	public void setPartyPrefix(String paramParty, String paramPrefix) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setPrefix(paramPrefix);
			party.updateParty();
		}
	}
	
	public String getPartySuffix(String paramParty) {
		String ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getSuffix();
		}
		return ret;
	}
	public void setPartySuffix(String paramParty, String paramSuffix) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setSuffix(paramSuffix);
			party.updateParty();
		}
	}
	
	public String getPartyColor(String paramParty) {
		String ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getColorRaw();
		}
		return ret;
	}

	public void setPartyColor(String paramParty, String paramColor) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setColorRaw(paramColor);
			party.updateParty();
		}
	}

	public List<Color> getColorList() {
		return Variables.color_list;
	}
	
	public int getPartyKills(String paramParty) {
		int ret = -1;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getKills();
		}
		return ret;
	}
	public void setPartyKills(String paramParty, int paramKills) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setKills(paramKills);
			party.updateParty();
		}
	}
	
	public String getPassword(String paramParty) {
		String ret = null;
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			ret = party.getPassword();
		}
		return ret;
	}
	public void setPassword(String paramParty, String paramPassword) {
		Party party = plugin.getPartyHandler().getParty(paramParty);
		if (party != null) {
			party.setPassword(paramPassword);
			party.updateParty();
		}
	}
}
