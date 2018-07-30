package com.alessiodp.parties.common.api;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.enums.Status;
import com.alessiodp.parties.api.interfaces.Color;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Rank;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ApiHandler implements PartiesAPI {
	private PartiesPlugin plugin;
	
	public ApiHandler(PartiesPlugin instance) {
		plugin = instance;
	}
	
	/*
	 * 
	 * Parties based
	 * 
	 */
	@Override
	public void updateParty(Party paramParty) {
		plugin.getDatabaseManager().updateParty((PartyImpl) paramParty);
	}
	
	@Override
	public void updatePartyPlayer(PartyPlayer paramPartyPlayer) {
		plugin.getDatabaseManager().updatePlayer((PartyPlayerImpl) paramPartyPlayer);
	}
	
	@Override
	public void reloadParties() {
		plugin.reloadConfiguration();
	}
	
	@Override
	public void broadcastPartyMessage(Party paramParty, PartyPlayer paramPartyPlayer, String paramMessage) {
		PartyImpl party = (PartyImpl) paramParty;
		party.sendBroadcast((PartyPlayerImpl) paramPartyPlayer, paramMessage);
	}
	
	@Override
	public List<Party> getOnlineParties() {
		List<Party> ret = new ArrayList<>();
		// The key of the entry is case insensitive
		for (Map.Entry<String, PartyImpl> entry : plugin.getPartyManager().getListParties().entrySet()) {
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
					PartyImpl party = (PartyImpl) paramParty;
					
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
	
	@Override
	public Status removePlayerFromParty(PartyPlayer paramPartyPlayer) {
		Status ret = Status.NOPARTY;
		if (!paramPartyPlayer.getPartyName().isEmpty()) {
			PartyPlayerImpl player = (PartyPlayerImpl) paramPartyPlayer;
			PartyImpl party = plugin.getPartyManager().getParty(player.getPartyName());
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
	
	@Override
	public Set<Rank> getRanks() {
		return new HashSet<>(plugin.getRankManager().getRankList());
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
		PartyPlayerImpl player = (PartyPlayerImpl) paramPartyPlayer;
		
		if (player.getPartyName().isEmpty()) {
			if (!plugin.getPartyManager().existParty(paramPartyName)) {
				PartyImpl party = plugin.getPartyManager().initializeParty(paramPartyName);
				party.createParty(player);
				
				player.setRank(ConfigParties.RANK_SET_HIGHER);
				player.setPartyName(party.getName());
				
				party.updateParty();
				player.updatePlayer();
				
				plugin.getPartyManager().getListParties().put(party.getName().toLowerCase(), party);
				party.callChange();
				return Status.SUCCESS;
			} else
				ret = Status.ALREADYEXISTPARTY;
		}
		return ret;
	}
	
	@Override
	public Status deleteParty(Party paramParty) {
		Status ret = Status.NOEXIST;
		PartyImpl party = (PartyImpl) paramParty;
		if (party != null) {
			party.removeParty();
			ret = Status.SUCCESS;
		}
		return ret;
	}
	
	@Override
	public Set<PartyPlayer> getOnlinePlayers(Party paramParty) {
		return new HashSet<>(((PartyImpl) paramParty).getOnlinePlayers());
	}
	
	@Override
	public void refreshOnlinePlayers(Party paramParty) {
		((PartyImpl) paramParty).refreshPlayers();
	}
	
	
	@Override
	public Set<Color> getColors() {
		return new HashSet<>(plugin.getColorManager().getColorList());
	}
}
