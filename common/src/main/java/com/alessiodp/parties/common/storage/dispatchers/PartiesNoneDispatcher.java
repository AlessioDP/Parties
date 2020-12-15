package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.storage.dispatchers.NoneDispatcher;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class PartiesNoneDispatcher extends NoneDispatcher implements IPartiesDatabase {
	private final PartiesPlugin plugin;
	
	public PartiesNoneDispatcher(PartiesPlugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		// Nothing to do
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		return null;
	}
	
	@Override
	public int getListPlayersNumber() {
		// Nothing to do
		return 0;
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		// Nothing to do
	}
	
	@Override
	public PartyImpl getParty(UUID id) {
		return null;
	}
	
	@Override
	public PartyImpl getPartyByName(String name) {
		return null;
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		// Nothing to do
	}
	
	@Override
	public boolean existsParty(String name) {
		return false;
	}
	
	@Override
	public boolean existsTag(String tag) {
		return false;
	}
	
	@Override
	public LinkedHashSet<PartyImpl> getListParties(PartiesDatabaseManager.ListOrder order, int limit, int offset) {
		// Directly list from memory
		LinkedHashSet<PartyImpl> ret = new LinkedHashSet<>();
		
		List<String> lowerCaseBlacklist = new ArrayList<>();
		for (String b : ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES)
			lowerCaseBlacklist.add(CommonUtils.toLowerCase(b));
		
		TreeSet<? extends Pair<PartyImpl, ?>> entries;
		if (order == PartiesDatabaseManager.ListOrder.NAME)
			entries = listByName(lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.MEMBERS)
			entries = listByMembers(lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.KILLS)
			entries = listByKills(lowerCaseBlacklist);
		else if (order == PartiesDatabaseManager.ListOrder.EXPERIENCE)
			entries = listByExperience(lowerCaseBlacklist);
		else
			throw new IllegalStateException("Cannot get the list of parties with the order" + order.name());
		
		// Limit and offset
		Iterator<? extends Pair<PartyImpl, ?>> iterator = entries.iterator();
		int n = 0;
		for (int c = 0; iterator.hasNext() && n < limit; c++) {
			PartyImpl p = iterator.next().getKey();
			if (c >= offset) {
				ret.add(p);
				n++;
			}
		}
		return ret;
	}
	
	private TreeSet<Pair<PartyImpl, String>> listByName(List<String> lowerCaseBlacklist) {
		TreeSet<Pair<PartyImpl, String>> ret = new TreeSet<>((p1, p2) -> p1.getValue().compareTo(p2.getValue()));
		for (PartyImpl party : plugin.getPartyManager().getCacheParties().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(party.getName())))
				ret.add(new Pair<>(party, party.getName()));
		}
		return ret;
	}
	
	private TreeSet<Pair<PartyImpl, Integer>> listByMembers(List<String> lowerCaseBlacklist) {
		TreeSet<Pair<PartyImpl, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (PartyImpl party : plugin.getPartyManager().getCacheParties().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(party.getName())))
				ret.add(new Pair<>(party, party.getMembers().size()));
		}
		return ret;
	}
	
	private TreeSet<Pair<PartyImpl, Integer>> listByKills(List<String> lowerCaseBlacklist) {
		TreeSet<Pair<PartyImpl, Integer>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (PartyImpl party : plugin.getPartyManager().getCacheParties().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(party.getName())))
				ret.add(new Pair<>(party, party.getKills()));
		}
		return ret;
	}
	
	private TreeSet<Pair<PartyImpl, Double>> listByExperience(List<String> lowerCaseBlacklist) {
		TreeSet<Pair<PartyImpl, Double>> ret = new TreeSet<>((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
		for (PartyImpl party : plugin.getPartyManager().getCacheParties().values()) {
			if (!lowerCaseBlacklist.contains(CommonUtils.toLowerCase(party.getName())))
				ret.add(new Pair<>(party, party.getExperience()));
		}
		return ret;
	}
	
	@Override
	public int getListPartiesNumber() {
		return plugin.getPartyManager().getCacheParties().size();
	}
	
	@Override
	public Set<PartyImpl> getListFixed() {
		return Collections.emptySet();
	}
}