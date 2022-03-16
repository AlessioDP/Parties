package com.alessiodp.parties.velocity.listeners;

import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.listeners.JoinLeaveListener;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;

public class VelocityJoinLeaveListener extends JoinLeaveListener {
	
	public VelocityJoinLeaveListener(PartiesPlugin instance) {
		super(instance);
	}
	
	@Subscribe
	public void onPlayerJoin(LoginEvent event) {
		super.onPlayerJoin(new VelocityUser(plugin, event.getPlayer()));
	}
	
	@Subscribe
	public void onPlayerQuit(DisconnectEvent event) {
		super.onPlayerQuit(new VelocityUser(plugin, event.getPlayer()));
	}
	
	@Override
	protected void onJoinComplete(PartyPlayerImpl partyPlayer) {
		((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendLoadPlayer(partyPlayer);
	}
	
	@Override
	protected void onLeaveComplete(PartyPlayerImpl partyPlayer) {
		((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUnloadPlayer(partyPlayer);
	}
	
	
	@Override
	protected boolean moderationOnServerBan() {
		return false;
	}
}
