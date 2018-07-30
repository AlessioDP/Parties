package com.alessiodp.parties.common.storage;

import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

public class DatabaseData {
	@Getter @Setter private Map<UUID, PartyPlayerImpl> players;
	@Getter @Setter private Map<String, PartyImpl> parties;
	
	public DatabaseData(Map<UUID, PartyPlayerImpl> players, Map<String, PartyImpl> parties) {
		this.players = players;
		this.parties = parties;
	}
}
