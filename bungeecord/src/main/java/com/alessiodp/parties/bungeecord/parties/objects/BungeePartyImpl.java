package com.alessiodp.parties.bungeecord.parties.objects;

import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdateParty(this);
	}
	
	@Override
	public void sendPacketCreate(@Nullable PartyPlayerImpl leader) {
		super.sendPacketCreate(leader);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendCreateParty(this, leader);
	}
	
	@Override
	public void sendPacketDelete(@NotNull DeleteCause cause, @Nullable PartyPlayerImpl kicked, @Nullable PartyPlayerImpl commandSender) {
		super.sendPacketDelete(cause, kicked, commandSender);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDeleteParty(this, cause, kicked, commandSender);
	}
	
	@Override
	public void sendPacketRename(@Nullable String oldName, @Nullable String newName, @Nullable PartyPlayerImpl player, boolean isAdmin) {
		super.sendPacketRename(oldName, newName, player, isAdmin);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRenameParty(this, oldName, newName, player, isAdmin);
	}
	
	@Override
	public void sendPacketAddMember(@NotNull PartyPlayerImpl player, @NotNull JoinCause cause, @Nullable PartyPlayerImpl inviter) {
		super.sendPacketAddMember(player, cause, inviter);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddMemberParty(this, player, cause, inviter);
	}
	
	@Override
	public void sendPacketRemoveMember(@NotNull PartyPlayerImpl player, @NotNull LeaveCause cause, @Nullable PartyPlayerImpl kicker) {
		super.sendPacketRemoveMember(player, cause, kicker);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRemoveMemberParty(this, player, cause, kicker);
	}
	
	@Override
	public void sendPacketChat(@NotNull PartyPlayerImpl player, @NotNull String formattedMessage, @NotNull String message, boolean dispatchBetweenServers) {
		super.sendPacketChat(player, formattedMessage, message, dispatchBetweenServers);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendChatMessage(this, player, formattedMessage, message, dispatchBetweenServers);
	}
	
	@Override
	public void sendPacketBroadcast(@NotNull String message, @Nullable PartyPlayerImpl player, boolean dispatchBetweenServers) {
		super.sendPacketBroadcast(message, player, dispatchBetweenServers);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendBroadcastMessage(this, player, message, dispatchBetweenServers);
	}
	
	@Override
	public void sendPacketInvite(@NotNull PartyPlayer invitedPlayer, @Nullable PartyPlayer inviter) {
		super.sendPacketInvite(invitedPlayer, inviter);
		
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendInvitePlayer(this, (PartyPlayerImpl) invitedPlayer, inviter != null ? (PartyPlayerImpl) inviter : null);
	}
	
	@Override
	public void sendPacketExperience(double newExperience, @Nullable PartyPlayer killer, boolean gainMessage) {
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPartyExperience(this, (PartyPlayerImpl) killer, newExperience, gainMessage);
	}
	
	@Override
	public void sendPacketLevelUp(int newLevel) {
		((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendLevelUp(this, newLevel);
	}
}
