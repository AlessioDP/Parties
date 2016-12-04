package com.alessiodp.parties.utils.api;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;

public class PartiesAPI implements Api{
	
	Parties plugin;
	
	public PartiesAPI(){
		plugin = Parties.getInstance();
	}

	public Status createParty(Player paramLeader, String paramPartyName){
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(paramLeader);
		if(tp.haveParty())
			return Status.ALREADYINPARTY;
		if (plugin.getPartyHandler().existParty(paramPartyName)) {
			return Status.ALREADYEXISTPARTY;
		}
		Party party = new Party(paramPartyName, plugin);
		party.getMembers().add(paramLeader.getUniqueId());
		party.getOnlinePlayers().add(paramLeader);
		plugin.getPartyHandler().listParty.put(party.getName(), party);
		
		tp.setRank(Variables.rank_last);
		
		tp.setHaveParty(true);
		tp.setPartyName(paramPartyName);

		party.setLeader(paramLeader.getUniqueId());
		party.updateParty();
		tp.updatePlayer();

		plugin.getPartyHandler().scoreboard_refreshParty(party.getName());
		return Status.SUCCESS;
	}
	
	public Status deleteParty(String paramParty){
		Party party = plugin.getPartyHandler().loadParty(paramParty);
		if(party == null)
			return Status.NOEXIST;
		party.removeParty();
		return Status.SUCCESS;
	}
	
	public Status addPlayerInParty(Player paramPlayer, String paramParty){
		String partyName = plugin.getPlayerHandler().getThePlayer(paramPlayer).getPartyName();
		if(!partyName.isEmpty() && partyName != null)
			return Status.ALREADYINPARTY;
		if(!plugin.getPartyHandler().existParty(paramParty))
			return Status.NOEXIST;
		Party party = plugin.getPartyHandler().loadParty(paramParty);
		if((Variables.party_maxmembers <= party.getMembers().size()) && Variables.party_maxmembers != -1)
			return Status.PARTYFULL;
		
		party.getMembers().add(paramPlayer.getUniqueId());
		party.getOnlinePlayers().add(paramPlayer);
		
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(paramPlayer);
		tp.setHaveParty(true);
		tp.setPartyName(party.getName());
		
		party.updateParty();
		tp.updatePlayer();
		
		plugin.getPartyHandler().scoreboard_refreshParty(partyName);
		return Status.SUCCESS;
	}
	
	public Status removePlayerFromParty(UUID uuid){
		String partyName = plugin.getPlayerHandler().getThePlayer(uuid).getPartyName();
		if(partyName.isEmpty() || partyName == null)
			return Status.NOPARTY;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(uuid);
		if(!plugin.getPartyHandler().existParty(tp.getPartyName()))
			return Status.NOPARTY;
		Party party = plugin.getPartyHandler().loadParty(partyName);

		if (uuid.equals(party.getLeader().toString())) {
			party.removeParty();
			return Status.SUCCESS;
		}
		if(party.getMembers().contains(uuid)){
			party.getMembers().remove(uuid);
		}
		party.updateParty();
		tp.updatePlayer();
		if(tp.getPlayer().isOnline()){
			plugin.getPartyHandler().scoreboard_removePlayer(Bukkit.getPlayer(uuid));
			plugin.getPartyHandler().scoreboard_refreshParty(party.getName());
		}
		return Status.SUCCESS;
	}
	
	public boolean haveParty(UUID uuid) {
		return plugin.getPlayerHandler().getThePlayer(uuid).haveParty();
	}

	public boolean isSpy(UUID uuid) {
		return plugin.getPlayerHandler().isSpy(uuid);
	}

	public String getPartyName(UUID uuid) {
		return plugin.getPlayerHandler().getThePlayer(uuid).getPartyName();
	}
	
	public int getRank(UUID paramPlayer){
		return plugin.getPlayerHandler().getThePlayer(paramPlayer).getRank();
	}

	public UUID getPartyLeader(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getLeader();
	}

	public ArrayList<UUID> getPartyMembers(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getMembers();
	}

	public ArrayList<Player> getPartyOnlinePlayers(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getOnlinePlayers();
	}
	
	public String getPartyDescription(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getDescription();
	}
	
	public String getPartyMotd(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getMOTD();
	}
	
	public String getPartyPrefix(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getPrefix();
	}
	
	public String getPartySuffix(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getSuffix();
	}
	
	public int getPartyKills(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getKills();
	}
	public Location getPartyHome(String paramPartyName) {
		return plugin.getPartyHandler().loadParty(paramPartyName).getHome();
	}
	
	public ArrayList<Rank> getRankList() {
		return plugin.getPartyHandler().getRankList();
	}
}
