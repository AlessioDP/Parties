package com.alessiodp.parties.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.enums.Status;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.interfaces.PartiesAPI;
import com.alessiodp.partiesapi.interfaces.Rank;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

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
	@Override
	public void updateParty(Party paramParty) {
		plugin.getDatabaseManager().updateParty(paramParty);
	}
	
	@Override
	public void updatePartyPlayer(PartyPlayer paramPartyPlayer) {
		plugin.getDatabaseManager().updatePlayer(paramPartyPlayer);
	}
	
	@Override
	public void reloadParties() {
		plugin.reloadConfiguration();
	}
	
	@Override
	public Status broadcastPartyMessage(Party paramParty, PartyPlayer paramPartyPlayer, String paramMessage) {
		Status ret = Status.NOEXIST;
		PartyEntity party = new PartyEntity(paramParty, plugin);
		if (party != null) {
			party.sendBroadcast(new PartyPlayerEntity(paramPartyPlayer, plugin), paramMessage);
			ret = Status.SUCCESS;
		}
		return ret;
	}
	
	@Deprecated
	@Override
	public Status broadcastPartyMessage(String paramParty, UUID paramUUID, String paramMessage) {
		Party party = plugin.getPartyManager().getParty(paramParty);
		PartyPlayer player = plugin.getPlayerManager().getPlayer(paramUUID);
		return broadcastPartyMessage(party, player, paramMessage);
	}
	
	@Override
	public List<Party> getOnlineParties() {
		List<Party> ret = new ArrayList<Party>();
		// The key of the entry is case insensitive
		for (Entry<String, PartyEntity> entry : plugin.getPartyManager().getListParties().entrySet()) {
			ret.add(entry.getValue());
		}
		return ret;
	}
	
	/*
	 * 
	 * Player based
	 * 
	 */
	@Override
	public PartyPlayer getPartyPlayer(UUID paramUUID) {
		return plugin.getPlayerManager().getPlayer(paramUUID);
	}
	
	@Override
	public Status addPlayerIntoParty(PartyPlayer paramPartyPlayer, Party paramParty) {
		Status ret = Status.ALREADYINPARTY;
		if (!paramPartyPlayer.getPartyName().isEmpty()) {
			if (paramParty != null) {
				if (ConfigParties.GENERAL_MEMBERSLIMIT < 0
						|| paramParty.getMembers().size() < ConfigParties.GENERAL_MEMBERSLIMIT) {
					PartyEntity party = new PartyEntity(paramParty, plugin);
					
					party.getMembers().add(paramPartyPlayer.getPlayerUUID());
					paramPartyPlayer.setPartyName(party.getName());
					
					party.refreshPlayers();
					party.updateParty();
					updatePartyPlayer(paramPartyPlayer);
					
					party.callChange();
					ret = Status.SUCCESS;
				} else
					ret = Status.PARTYFULL;
			} else
				ret = Status.NOEXIST;
		}
		return ret;
	}
	
	@Deprecated
	@Override
	public Status addPlayerIntoParty(UUID paramUUID, String paramParty) {
		Party party = plugin.getPartyManager().getParty(paramParty);
		PartyPlayer player = plugin.getPlayerManager().getPlayer(paramUUID);
		return addPlayerIntoParty(player, party);
	}
	
	@Override
	public Status removePlayerFromParty(PartyPlayer paramPartyPlayer) {
		Status ret = Status.NOPARTY;
		if (!paramPartyPlayer.getPartyName().isEmpty()) {
			PartyPlayerEntity player = new PartyPlayerEntity(paramPartyPlayer, plugin);
			PartyEntity party = plugin.getPartyManager().getParty(player.getPartyName());
			if (party != null) {
				if (player.getPlayerUUID().equals(party.getLeader())) {
					party.removeParty();
				} else {
					if (party.getMembers().contains(player.getPlayerUUID())) {
						party.getMembers().remove(player.getPlayerUUID());
						party.updateParty();
						party.callChange();
					}
					player.cleanupPlayer(true);
				}
				ret = Status.SUCCESS;
			}
		}
		return ret;
	}
	
	@Deprecated
	@Override
	public Status removePlayerFromParty(UUID paramUUID) {
		PartyPlayer player = plugin.getPlayerManager().getPlayer(paramUUID);
		return removePlayerFromParty(player);
	}
	
	@Deprecated
	@Override
	public boolean haveParty(UUID paramUUID) {
		return !plugin.getPlayerManager().getPlayer(paramUUID).getPartyName().isEmpty();
	}
	
	@Override
	public boolean isSpy(UUID paramUUID) {
		return getPartyPlayer(paramUUID).isSpy();
	}
	
	@Deprecated
	@Override
	public void setSpy(UUID paramUUID, boolean paramSpy) {
		PartyPlayer pp = getPartyPlayer(paramUUID);
		pp.setSpy(paramSpy);
		updatePartyPlayer(pp);
	}
	
	@Deprecated
	@Override
	public String getPartyName(UUID paramUUID) {
		return getPartyPlayer(paramUUID).getPartyName();
	}
	
	@Deprecated
	@Override
	public int getRank(UUID paramUUID) {
		return getPartyPlayer(paramUUID).getRank();
	}
	
	@Deprecated
	@Override
	public void setRank(UUID paramUUID, int paramRank) {
		PartyPlayer pp = getPartyPlayer(paramUUID);
		pp.setRank(paramRank);
		updatePartyPlayer(pp);
	}
	
	@Override
	public Set<Rank> getRanks() {
		return plugin.getRankManager().getRankList();
	}
	
	@Deprecated
	@Override
	public List<Rank> getRankList() {
		List<Rank> ret = new ArrayList<Rank>();
		ret.addAll(plugin.getRankManager().getRankList());
		return ret;
	}
	
	/*
	 * 
	 * Party based
	 * 
	 */
	@Override
	public Party getParty(String paramPartyName) {
		return plugin.getPartyManager().getParty(paramPartyName);
	}
	
	@Override
	public Status createParty(PartyPlayer paramPartyPlayer, String paramPartyName) {
		Status ret = Status.ALREADYINPARTY;
		// Get an instance of PartyPlayerEntity, used to save same player
		PartyPlayerEntity player = null;
		if (paramPartyPlayer instanceof PartyPlayerEntity) {
			player = (PartyPlayerEntity) paramPartyPlayer;
		} else {
			player = (PartyPlayerEntity) getPartyPlayer(paramPartyPlayer.getPlayerUUID());
		}
		
		if (player.getPartyName().isEmpty()) {
			if (!plugin.getPartyManager().existParty(paramPartyName)) {
				PartyEntity party = new PartyEntity(paramPartyName, plugin);
				party.getMembers().add(player.getPlayerUUID());
				
				Player p = player.getPlayer();
				if (p != null)
					party.getOnlinePlayers().add(p);
				party.setLeader(player.getPlayerUUID());
				plugin.getPartyManager().getListParties().put(party.getName().toLowerCase(), party);
				
				player.setRank(ConfigParties.RANK_SET_HIGHER);
				player.setPartyName(party.getName());
				
				party.updateParty();
				player.updatePlayer();
				
				party.callChange();
				return Status.SUCCESS;
			} else
				ret = Status.ALREADYEXISTPARTY;
		}
		return ret;
	}
	
	@Deprecated
	@Override
	public Status createParty(UUID paramLeader, String paramParty) {
		PartyPlayer player = getPartyPlayer(paramLeader);
		return createParty(player, paramParty);
	}
	
	@Override
	public Status deleteParty(Party paramParty) {
		Status ret = Status.NOEXIST;
		PartyEntity party = new PartyEntity(paramParty, plugin);
		if (party != null) {
			party.removeParty();
			ret = Status.SUCCESS;
		}
		return ret;
	}
	
	@Deprecated
	@Override
	public Status deleteParty(String paramParty) {
		Party party = getParty(paramParty);
		return deleteParty(party);
	}
	
	@Deprecated
	@Override
	public UUID getPartyLeader(String paramParty) {
		return getParty(paramParty).getLeader();
	}
	
	@Deprecated
	@Override
	public void setPartyLeader(String paramParty, UUID paramUUID) {
		Party party = getParty(paramParty);
		party.setLeader(paramUUID);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public List<UUID> getPartyMembers(String paramParty) {
		return getParty(paramParty).getMembers();
	}
	
	@Deprecated
	@Override
	public void setPartyMembers(String paramParty, List<UUID> paramListPlayers) {
		Party party = getParty(paramParty);
		party.setMembers(paramListPlayers);
		updateParty(party);
	}
	
	@Override
	public Set<Player> getPartyOnlinePlayers(Party paramParty) {
		PartyEntity party = new PartyEntity(paramParty, plugin);
		return party.getOnlinePlayers();
	}
	
	@Deprecated
	@Override
	public List<Player> getPartyOnlinePlayers(String paramParty) {
		List<Player> ret = new ArrayList<Player>();
		Party party = getParty(paramParty);
		ret.addAll(getPartyOnlinePlayers(party));
		return ret;
	}
	
	@Override
	public void refreshOnlinePlayers(Party paramParty) {
		new PartyEntity(paramParty, plugin).refreshPlayers();
	}
	
	@Deprecated
	@Override
	public void refreshOnlinePlayers(String paramParty) {
		refreshOnlinePlayers(getParty(paramParty));
	}
	
	@Deprecated
	@Override
	public boolean isPartyFixed(String paramParty) {
		return getParty(paramParty).isFixed();
	}
	
	@Deprecated
	@Override
	public void setPartyFixed(String paramParty, boolean paramFixed) {
		Party party = getParty(paramParty);
		party.setFixed(paramFixed);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public String getPartyDescription(String paramParty) {
		return getParty(paramParty).getDescription();
	}
	
	@Deprecated
	@Override
	public void setPartyDescription(String paramParty, String paramDescription) {
		Party party = getParty(paramParty);
		party.setDescription(paramDescription);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public String getPartyMotd(String paramParty) {
		return getParty(paramParty).getMotd();
	}
	
	@Deprecated
	@Override
	public void setPartyMotd(String paramParty, String paramMotd) {
		Party party = getParty(paramParty);
		party.setMotd(paramMotd);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public Location getPartyHome(String paramParty) {
		return getParty(paramParty).getHome();
	}
	
	@Deprecated
	@Override
	public void setPartyHome(String paramParty, Location paramHome) {
		Party party = getParty(paramParty);
		party.setHome(paramHome);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public String getPartyPrefix(String paramParty) {
		return getParty(paramParty).getPrefix();
	}
	
	@Deprecated
	@Override
	public void setPartyPrefix(String paramParty, String paramPrefix) {
		Party party = getParty(paramParty);
		party.setPrefix(paramPrefix);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public String getPartySuffix(String paramParty) {
		return getParty(paramParty).getSuffix();
	}
	
	@Deprecated
	@Override
	public void setPartySuffix(String paramParty, String paramSuffix) {
		Party party = getParty(paramParty);
		party.setSuffix(paramSuffix);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public String getPartyColor(String paramParty) {
		String ret = "";
		Party party = getParty(paramParty);
		if (party.getColor() != null)
			ret = party.getColor().getName();
		return ret;
	}
	
	@Deprecated
	@Override
	public void setPartyColor(String paramParty, String paramColor) {
		Party party = getParty(paramParty);
		party.setColor(plugin.getColorManager().searchColorByName(paramColor));
		updateParty(party);
	}
	
	@Override
	public Set<Color> getColors() {
		return plugin.getColorManager().getColorList();
	}
	
	@Deprecated
	@Override
	public List<Color> getColorList() {
		List<Color> ret = new ArrayList<Color>();
		ret.addAll(plugin.getColorManager().getColorList());
		return ret;
	}
	
	@Deprecated
	@Override
	public int getPartyKills(String paramParty) {
		return getParty(paramParty).getKills();
	}
	
	@Deprecated
	@Override
	public void setPartyKills(String paramParty, int paramKills) {
		Party party = getParty(paramParty);
		party.setKills(paramKills);
		updateParty(party);
	}
	
	@Deprecated
	@Override
	public String getPassword(String paramParty) {
		return getParty(paramParty).getPassword();
	}
	
	@Deprecated
	@Override
	public void setPassword(String paramParty, String paramPassword) {
		Party party = getParty(paramParty);
		party.setPassword(paramPassword);
		updateParty(party);
	}
}
