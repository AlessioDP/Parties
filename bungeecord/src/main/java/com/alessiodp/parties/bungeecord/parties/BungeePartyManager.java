package com.alessiodp.parties.bungeecord.parties;

import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.bungeecord.parties.objects.BungeePartyImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

import java.util.UUID;

public class BungeePartyManager extends PartyManager {
	public BungeePartyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public PartyImpl initializeParty(UUID id) {
		return new BungeePartyImpl(plugin, id);
	}
	
	@Override
	public PartyImpl loadParty(UUID id, boolean syncServers) {
		PartyImpl ret = super.loadParty(id, syncServers);
		
		// Only syncs join events
		if (syncServers && ret != null) {
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendLoadParty(ret);
		}
		
		return ret;
	}
	
	@Override
	public void unloadParty(PartyImpl party) {
		super.unloadParty(party);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUnloadParty(party);
	}
}
