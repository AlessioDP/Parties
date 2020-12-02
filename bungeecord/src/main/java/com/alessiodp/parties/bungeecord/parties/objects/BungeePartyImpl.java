package com.alessiodp.parties.bungeecord.parties.objects;

import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

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
	public void sendPacketUpdate() {
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdateParty(this);
	}
	
	@Override
	public void sendPacketCreate(PartyPlayerImpl leader) {
		super.sendPacketCreate(leader);
		
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendCreateParty(this, leader);
	}
	
	@Override
	public void sendPacketDelete(DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl commandSender) {
		super.sendPacketDelete(cause, kicked, commandSender);
		
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDeleteParty(this, makeRawDelete(cause, kicked, commandSender));
	}
	
	@Override
	public void sendPacketRename(String oldName, String newName, PartyPlayerImpl player, boolean isAdmin) {
		super.sendPacketRename(oldName, newName, player, isAdmin);
		
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRenameParty(this, makeRawRename(oldName, newName, player, isAdmin));
	}
	
	@Override
	public void sendPacketAddMember(PartyPlayerImpl player, JoinCause cause, PartyPlayerImpl inviter) {
		super.sendPacketAddMember(player, cause, inviter);
		
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddMemberParty(this, makeRawAddMember(player, cause, inviter));
	}
	
	@Override
	public void sendPacketRemoveMember(PartyPlayerImpl player, LeaveCause cause, PartyPlayerImpl kicker) {
		super.sendPacketRemoveMember(player, cause, kicker);
		
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRemoveMemberParty(this, makeRawRemoveMember(player, cause, kicker));
	}
	
	@Override
	public void sendPacketChat(PartyPlayerImpl player, String message) {
		super.sendPacketChat(player, message);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendChatMessage(this, player, message);
	}
	
	@Override
	public void sendPacketInvite(PartyPlayer invitedPlayer, PartyPlayer inviter) {
		super.sendPacketInvite(invitedPlayer, inviter);
		
		// Send event to Bukkit servers
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendInvitePlayer(this, makeRawInvite(invitedPlayer, inviter));
	}
	
	@Override
	public void sendPacketExperience(double newExperience, PartyPlayer killer) {
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPartyExperience(this, (PartyPlayerImpl) killer, newExperience);
	}
	
	@Override
	public void sendPacketLevelUp(int newLevel) {
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendLevelUp(this, newLevel);
	}
}
