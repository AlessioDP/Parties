package com.alessiodp.parties.velocity.parties;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;
import com.alessiodp.parties.velocity.parties.objects.VelocityPartyImpl;

import java.util.UUID;

public class VelocityPartyManager extends PartyManager {
	public VelocityPartyManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public PartyImpl initializeParty(UUID id) {
		return new VelocityPartyImpl(plugin, id);
	}
	
	@Override
	public PartyImpl loadParty(UUID id, boolean syncServers) {
		PartyImpl ret = super.loadParty(id, syncServers);
		
		// Only syncs join events
		if (syncServers && ret != null) {
			((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendLoadParty(ret);
		}
		
		return ret;
	}
	
	@Override
	public void unloadParty(PartyImpl party) {
		super.unloadParty(party);
		
		((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUnloadParty(party);
	}
}
