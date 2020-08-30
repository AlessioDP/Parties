package com.alessiodp.parties.bungeecord.parties.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

import java.util.UUID;

public class BungeePartyImpl extends PartyImpl {
	
	public BungeePartyImpl(PartiesPlugin plugin, UUID id) {
		super(plugin, id);
	}
	
	@Override
	public void callChange() {
		// Nothing to do
	}
}
