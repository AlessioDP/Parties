package com.alessiodp.parties.bungeecord.parties.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public class BungeePartyImpl extends PartyImpl {
	
	public BungeePartyImpl(PartiesPlugin instance, String name) {
		super(instance, name);
	}
	
	@Override
	public void updateParty() {
		super.updateParty();
	}
	
	@Override
	public void renamingParty() {
		super.renamingParty();
	}
	
	@Override
	public void removeParty() {
		super.removeParty();
	}
	
	@Override
	public void callChange() {
	}
	
	@Override
	public void sendChatMessage(PartyPlayerImpl sender, String playerMessage) {
		super.sendChatMessage(sender, playerMessage);
	}
}
