package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyInvite;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
public class PartyInviteImpl implements PartyInvite {
	@NonNull private final PartiesPlugin plugin;
	@Getter @Setter private Party party;
	@Getter @Setter private PartyPlayer invitedPlayer;
	@Getter @Setter private PartyPlayer inviter;
	
	@Override
	public void accept(boolean sendMessages) {
		if (party.getInviteRequests().remove(this) && invitedPlayer.getPendingInvites().remove(this)) {
			
			// Calling API Event
			IPlayerPreJoinEvent partiesPreJoinEvent = plugin.getEventManager().preparePlayerPreJoinEvent(invitedPlayer, party, JoinCause.INVITE, inviter);
			plugin.getEventManager().callEvent(partiesPreJoinEvent);
			
			if (!partiesPreJoinEvent.isCancelled()) {
				boolean success = ((PartyImpl) party).addMember(invitedPlayer, JoinCause.INVITE, ((PartyPlayerImpl) inviter));
				if (success) {
					if (sendMessages) {
						((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.MAINCMD_INVITE_ACCEPT_ACCEPTED, (PartyPlayerImpl) inviter, (PartyImpl) party);
						
						if (inviter != null) {
							((PartyPlayerImpl) inviter).sendMessage(Messages.MAINCMD_INVITE_ACCEPT_RECEIPT, (PartyPlayerImpl) invitedPlayer, (PartyImpl) party);
						}
						
						party.broadcastMessage(Messages.MAINCMD_INVITE_ACCEPT_BROADCAST, invitedPlayer);
					}
				} else {
					if (sendMessages) {
						((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.PARTIES_COMMON_PARTYFULL, (PartyImpl) party);
						if (inviter != null) {
							((PartyPlayerImpl) inviter).sendMessage(Messages.PARTIES_COMMON_PARTYFULL, (PartyImpl) party);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void deny(boolean sendMessages) {
		if (party.getInviteRequests().remove(this) && invitedPlayer.getPendingInvites().remove(this) && sendMessages) {
			((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.MAINCMD_INVITE_DENY_DENIED, (PartyPlayerImpl) inviter, (PartyImpl) party);
			
			if (inviter != null) {
				((PartyPlayerImpl) inviter).sendMessage(Messages.MAINCMD_INVITE_DENY_RECEIPT, (PartyPlayerImpl) invitedPlayer, (PartyImpl) party);
			}
			
			party.broadcastMessage(Messages.MAINCMD_INVITE_DENY_BROADCAST, invitedPlayer);
		}
	}
	
	@Override
	public void revoke(boolean sendMessages) {
		if (party.getInviteRequests().remove(this)) {
			if (inviter != null && sendMessages) {
				((PartyPlayerImpl) inviter).sendMessage(Messages.MAINCMD_INVITE_REVOKE_SENT, (PartyPlayerImpl) invitedPlayer, (PartyImpl) party);
			}
			
			if (invitedPlayer.getPendingInvites().remove(this) && sendMessages) {
				((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.MAINCMD_INVITE_REVOKE_REVOKED, (PartyPlayerImpl) inviter, (PartyImpl) party);
			}
		}
	}
	
	@Override
	public void timeout(boolean sendMessages) {
		if (party.getInviteRequests().remove(this)) {
			if (inviter != null && sendMessages) {
				((PartyPlayerImpl) inviter).sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_NORESPONSE, (PartyPlayerImpl) invitedPlayer, (PartyImpl) party);
			}
			
			if (invitedPlayer.getPendingInvites().remove(this) && sendMessages) {
				((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_TIMEOUT, (PartyPlayerImpl) inviter, (PartyImpl) party);
			}
		}
	}
}
