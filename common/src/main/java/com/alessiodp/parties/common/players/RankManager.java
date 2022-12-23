package com.alessiodp.parties.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.RankPermission;
import lombok.Getter;

import java.util.Set;

public class RankManager {
	private final PartiesPlugin plugin;
	@Getter private Set<PartyRankImpl> rankList;
	
	public RankManager(PartiesPlugin instance) {
		plugin = instance;
		reload();
	}
	
	public void reload() {
		rankList = ConfigParties.RANK_LIST;
	}
	
	public PartyRankImpl searchRankByLevel(int level) {
		PartyRankImpl ret = null;
		for (PartyRankImpl rank : rankList) {
			if (rank.getLevel() == level) {
				ret = rank;
				break;
			}
		}
		if (ret == null)
			ret = defaultRank();
		return ret;
	}
	
	public PartyRankImpl searchRankByName(String name) {
		PartyRankImpl ret = null;
		for (PartyRankImpl rank : rankList) {
			if (rank.getName().equalsIgnoreCase(name)) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	public PartyRankImpl searchRankByHardName(String hardName) {
		PartyRankImpl ret = null;
		for (PartyRankImpl rank : rankList) {
			if (rank.getConfigName().equalsIgnoreCase(hardName)) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	private PartyRankImpl defaultRank() {
		PartyRankImpl ret = null;
		for (PartyRankImpl rank : rankList) {
			if (rank.isDefault()) {
				ret = rank;
				break;
			}
		}
		return ret;
	}
	
	private PartyRankImpl searchUpRank(int base, RankPermission rankPermission) {
		// Search the rank with the right permission
		// Get the nearest to the base level
		PartyRankImpl ret = null;
		for (PartyRankImpl rank : rankList) {
			if (rank.getLevel() > base && rank.havePermission(rankPermission)) {
				
				if (ret == null)
					ret = rank;
				else if (rank.getLevel() < ret.getLevel()) {
					ret = rank;
				}
			}
		}
		return ret;
	}
	
	public boolean checkPlayerRank(PartyPlayerImpl pp, RankPermission rankPermission) {
		boolean ret = true;
		PartyRankImpl r = searchRankByLevel(pp.getRank());
		User player = plugin.getPlayer(pp.getPlayerUUID());
		
		if (r != null
				&& player != null
				&& !player.hasPermission(PartiesPermission.ADMIN_RANK_BYPASS)
				&& !r.havePermission(rankPermission.toString())) {
				ret = false;
		}
		return ret;
	}
	
	public boolean checkPlayerRankAlerter(PartyPlayerImpl partyPlayer, RankPermission rankPermission) {
		boolean ret = true;
		PartyRankImpl r = searchRankByLevel(partyPlayer.getRank());
		User user = plugin.getPlayer(partyPlayer.getPlayerUUID());
		
		if (r != null
				&& user != null
				&& !user.hasPermission(PartiesPermission.ADMIN_RANK_BYPASS)
				&& !r.havePermission(rankPermission.toString())) {
			PartyRankImpl rr = searchUpRank(partyPlayer.getRank(), rankPermission);
			if (rr != null) {
				partyPlayer.sendMessage(Messages.PARTIES_PERM_NORANK_UPRANK
						.replace("%rank_name%", rr.getName())
						.replace("%rank_chat%", rr.getChat())
						.replace("%rank_level%", Integer.toString(rr.getLevel())));
			} else {
				partyPlayer.sendMessage(Messages.PARTIES_PERM_NORANK_GENERAL
						.replace("%permission%", rankPermission.toString()));
			}
			ret = false;
		}
		return ret;
	}
}
