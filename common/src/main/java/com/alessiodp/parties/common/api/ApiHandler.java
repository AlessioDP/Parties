package com.alessiodp.parties.common.api;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.interfaces.Color;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Rank;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	public boolean createParty(@NonNull String party, PartyPlayer leader) {
		if (!party.isEmpty()
				&& !plugin.getPartyManager().existParty(party)
				&& (leader == null || leader.getPartyName().isEmpty())) {
			PartyImpl partyImpl = plugin.getPartyManager().initializeParty(party);
			partyImpl.create(leader != null ? (PartyPlayerImpl) leader : null);
			return true;
		}
		return false;
	}
	
	@Override
	public Party getParty(@NonNull String party) {
		return plugin.getPartyManager().getParty(party);
	}
	
	@Override
	public PartyPlayer getPartyPlayer(UUID uuid) {
		return plugin.getPlayerManager().getPlayer(uuid);
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
	
	@Override
	public Set<Rank> getRanks() {
		return new HashSet<>(plugin.getRankManager().getRankList());
	}
	
	@Override
	public Set<Color> getColors() {
		return new HashSet<>(plugin.getColorManager().getColorList());
	}
}
