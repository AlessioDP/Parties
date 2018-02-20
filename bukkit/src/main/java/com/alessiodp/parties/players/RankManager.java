package com.alessiodp.parties.players;

import java.util.Set;

import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.partiesapi.interfaces.Rank;

import lombok.Getter;

public class RankManager {
	@Getter private Set<Rank> rankList;
	
	public RankManager() {
		reload();
	}
	
	public void reload() {
		rankList = ConfigParties.RANK_LIST;
	}
	
	public Rank searchRankByLevel(int level) {
		Rank ret = null;
		for (Rank rank : rankList) {
			if (rank.getLevel() == level) {
				ret = rank;
				break;
			}
		}
		if (ret == null)
			ret = defaultRank();
		return ret;
	}
	
	public Rank searchRankByName(String name) {
		Rank ret = null;
		for (Rank rank : rankList) {
			if (rank.getName().equalsIgnoreCase(name)) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	public Rank searchRankByHardName(String hardName) {
		Rank ret = null;
		for (Rank rank : rankList) {
			if (rank.getHardName().equalsIgnoreCase(hardName)) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	public Rank defaultRank() {
		Rank ret = null;
		for (Rank rank : rankList) {
			if (rank.isDefault()) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	public Rank searchUpRank(int base, String perm) {
		// Search the rank with the right permission
		// Get the nearest to the base level
		Rank ret = null;
		for (Rank rank : rankList) {
			if (rank.getLevel() > base && rank.havePermission(perm)) {
				
				if (ret == null)
					ret = rank;
				else if (rank.getLevel() < ret.getLevel()) {
					ret = rank;
				}
			}
		}
		return ret;
	}
}
