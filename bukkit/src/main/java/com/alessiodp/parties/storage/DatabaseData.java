package com.alessiodp.parties.storage;

import java.util.Map;
import java.util.UUID;

import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

import lombok.Getter;
import lombok.Setter;

public class DatabaseData {
	@Getter @Setter private Map<UUID, PartyPlayer> players;
	@Getter @Setter private Map<String, Party> parties;
	
	public DatabaseData(Map<UUID, PartyPlayer> players, Map<String, Party> parties) {
		this.players = players;
		this.parties = parties;
	}
}
