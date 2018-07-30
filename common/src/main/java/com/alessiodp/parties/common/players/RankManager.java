package com.alessiodp.parties.common.players;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.RankImpl;
import lombok.Getter;

import java.util.Set;

public class RankManager {
	private PartiesPlugin plugin;
	@Getter private Set<RankImpl> rankList;
	
	public RankManager(PartiesPlugin instance) {
		plugin = instance;
		reload();
	}
	
	public void reload() {
		rankList = ConfigParties.RANK_LIST;
	}
	
	public RankImpl searchRankByLevel(int level) {
		RankImpl ret = null;
		for (RankImpl rank : rankList) {
			if (rank.getLevel() == level) {
				ret = rank;
				break;
			}
		}
		if (ret == null)
			ret = defaultRank();
		return ret;
	}
	
	public RankImpl searchRankByName(String name) {
		RankImpl ret = null;
		for (RankImpl rank : rankList) {
			if (rank.getName().equalsIgnoreCase(name)) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	public RankImpl searchRankByHardName(String hardName) {
		RankImpl ret = null;
		for (RankImpl rank : rankList) {
			if (rank.getHardName().equalsIgnoreCase(hardName)) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	private RankImpl defaultRank() {
		RankImpl ret = null;
		for (RankImpl rank : rankList) {
			if (rank.isDefault()) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	private RankImpl searchUpRank(int base, String perm) {
		// Search the rank with the right permission
		// Get the nearest to the base level
		RankImpl ret = null;
		for (RankImpl rank : rankList) {
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
	
	public boolean checkPlayerRank(PartyPlayerImpl pp, PartiesPermission perm) {
		boolean ret = true;
		RankImpl r = searchRankByLevel(pp.getRank());
		
		if (r != null && !plugin.getPlayer(pp.getPlayerUUID()).hasPermission(PartiesPermission.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(perm.toString())) {
				ret = false;
			}
		}
		return ret;
	}
	
	public boolean checkPlayerRankAlerter(PartyPlayerImpl pp, PartiesPermission perm) {
		boolean ret = true;
		RankImpl r = searchRankByLevel(pp.getRank());
		
		if (r != null && !plugin.getPlayer(pp.getPlayerUUID()).hasPermission(PartiesPermission.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(perm.toString())) {
				RankImpl rr = plugin.getRankManager().searchUpRank(pp.getRank(), perm.toString());
				if (rr != null)
					pp.sendMessage(Messages.PARTIES_PERM_NORANK
							.replace(Constants.PLACEHOLDER_PLAYER_RANK_NAME, rr.getName())
							.replace(Constants.PLACEHOLDER_PLAYER_RANK_CHAT, rr.getChat()));
				else
					pp.sendNoPermission(perm);
				ret = false;
			}
		}
		return ret;
	}
}
