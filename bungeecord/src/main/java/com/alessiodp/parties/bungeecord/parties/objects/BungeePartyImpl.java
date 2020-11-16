package com.alessiodp.parties.bungeecord.parties.objects;

import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class BungeePartyImpl extends PartyImpl {
	
	public BungeePartyImpl(PartiesPlugin plugin, UUID id) {
		super(plugin, id);
	}
	
	@Override
	public void delete() {
		super.delete();
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUnloadParty(this);
	}
	
	@Override
	public void rename(@NonNull String newName) {
		super.rename(newName);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRenameParty(this);
	}
	
	@Override
	public void callChange() {
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdateParty(this);
	}
	
	@Override
	public void sendExperiencePacket(double newExperience, PartyPlayer killer) {
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPartyExperience(this, (PartyPlayerImpl) killer, newExperience);
	}
	
	@Override
	public void sendLevelUpPacket(int newLevel) {
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendLevelUp(this, newLevel);
	}
}
