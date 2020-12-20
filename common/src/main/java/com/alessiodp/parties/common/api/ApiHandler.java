package com.alessiodp.parties.common.api;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.interfaces.PartyColor;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.PartyRank;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class ApiHandler implements PartiesAPI {
	@lombok.NonNull private final PartiesPlugin plugin;
	
	@Override
	public void reloadPlugin() {
		plugin.reloadConfiguration();
	}
	
	@Override
	public boolean isBungeeCordEnabled() {
		return plugin.isBungeeCordEnabled();
	}
	
	@Override
	public boolean createParty(@NonNull String name, PartyPlayer leader) {
		return createParty(name, null, leader);
	}
	
	@Override
	public boolean createParty(@NonNull String name, @Nullable String tag, PartyPlayer leader) {
		if (!name.isEmpty()
				&& !plugin.getPartyManager().existsParty(name)
				&& (leader == null || !leader.isInParty())) {
			PartyImpl partyImpl = plugin.getPartyManager().initializeParty(UUID.randomUUID());
			partyImpl.create(name, tag, leader != null ? (PartyPlayerImpl) leader : null);
			return true;
		}
		return false;
	}
	
	@Override
	public Party getParty(@NonNull String party) {
		return plugin.getPartyManager().getParty(party);
	}
	
	@Override
	public Party getParty(@NonNull UUID party) {
		return plugin.getPartyManager().getParty(party);
	}
	
	@Override
	public PartyPlayer getPartyPlayer(UUID uuid) {
		return plugin.getPlayerManager().getPlayer(uuid);
	}
	
	@Override
	public List<Party> getOnlineParties() {
		return new ArrayList<>(plugin.getPartyManager().getCacheParties().values());
	}
	
	@Override
	public Set<PartyRank> getRanks() {
		return new HashSet<>(plugin.getRankManager().getRankList());
	}
	
	@Override
	public Set<PartyColor> getColors() {
		return new HashSet<>(plugin.getColorManager().getColorList());
	}
	
	@Override
	public LinkedList<Party> getPartiesListByName(int numberOfPlayers, int offset) {
		return new LinkedList<>(plugin.getDatabaseManager().getListParties(PartiesDatabaseManager.ListOrder.NAME, numberOfPlayers, offset));
	}
	
	@Override
	public LinkedList<Party> getPartiesListByOnlineMembers(int numberOfPlayers, int offset) {
		return new LinkedList<>(plugin.getDatabaseManager().getListParties(PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS, numberOfPlayers, offset));
	}
	
	@Override
	public LinkedList<Party> getPartiesListByMembers(int numberOfPlayers, int offset) {
		return new LinkedList<>(plugin.getDatabaseManager().getListParties(PartiesDatabaseManager.ListOrder.MEMBERS, numberOfPlayers, offset));
	}
	
	@Override
	public LinkedList<Party> getPartiesListByKills(int numberOfPlayers, int offset) {
		return new LinkedList<>(plugin.getDatabaseManager().getListParties(PartiesDatabaseManager.ListOrder.KILLS, numberOfPlayers, offset));
	}
	
	@Override
	public LinkedList<Party> getPartiesListByExperience(int numberOfPlayers, int offset) {
		return new LinkedList<>(plugin.getDatabaseManager().getListParties(PartiesDatabaseManager.ListOrder.EXPERIENCE, numberOfPlayers, offset));
	}
	
	@Override
	public boolean areInTheSameParty(UUID player1, UUID player2) {
		PartyPlayer pp1 = getPartyPlayer(player1);
		PartyPlayer pp2 = getPartyPlayer(player2);
		return pp1 != null && pp2 != null && pp1.getPartyId() != null && pp1.getPartyId().equals(pp2.getPartyId());
	}
}
