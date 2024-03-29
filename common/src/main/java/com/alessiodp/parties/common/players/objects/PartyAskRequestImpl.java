package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyAskRequest;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EqualsAndHashCode
public class PartyAskRequestImpl implements PartyAskRequest {
	@EqualsAndHashCode.Exclude private final PartiesPlugin plugin;
	@Getter @Setter @EqualsAndHashCode.Exclude private Party party;
	@Getter @Setter @EqualsAndHashCode.Exclude private PartyPlayer asker;
	private final UUID partyId;
	private final UUID playerId;
	
	public PartyAskRequestImpl(@NotNull PartiesPlugin plugin, Party party, PartyPlayer asker) {
		this.plugin = plugin;
		this.party = party;
		this.asker = asker;
		// Used for hash purpose
		partyId = party.getId();
		playerId = asker.getPlayerUUID();
	}
	
	@Override
	public void accept(boolean sendMessages, PartyPlayer accepter) {
		if (party.getAskRequests().remove(this) && asker.getPendingAskRequests().remove(this)) {
			
			// Calling API Event
			IPlayerPreJoinEvent partiesPreJoinEvent = plugin.getEventManager().preparePlayerPreJoinEvent(asker, party, JoinCause.ASK, accepter);
			plugin.getEventManager().callEvent(partiesPreJoinEvent);
			
			if (!partiesPreJoinEvent.isCancelled()) {
				boolean success = ((PartyImpl) party).addMember(asker, JoinCause.ASK, (PartyPlayerImpl) accepter);
				if (success) {
					if (sendMessages) {
						((PartyPlayerImpl) asker).sendMessage(Messages.ADDCMD_ASK_ACCEPT_ACCEPTED, (PartyPlayerImpl) accepter, (PartyImpl) party);
						
						if (accepter != null)
							((PartyPlayerImpl) accepter).sendMessage(Messages.ADDCMD_ASK_ACCEPT_RECEIPT, (PartyPlayerImpl) asker, (PartyImpl) party);
						
						party.broadcastMessage(Messages.ADDCMD_ASK_ACCEPT_BROADCAST, asker);
					}
				} else {
					if (sendMessages) {
						((PartyPlayerImpl) asker).sendMessage(Messages.PARTIES_COMMON_PARTYFULL, (PartyImpl) party);
					}
				}
			}
		}
	}
	
	@Override
	public void deny(boolean sendMessages, PartyPlayer denier) {
		if (party.getAskRequests().remove(this) && asker.getPendingAskRequests().remove(this) && sendMessages) {
			((PartyPlayerImpl) asker).sendMessage(Messages.ADDCMD_ASK_DENY_DENIED, (PartyPlayerImpl) denier, (PartyImpl) party);
			
			if (denier != null)
				((PartyPlayerImpl) denier).sendMessage(Messages.ADDCMD_ASK_DENY_RECEIPT, (PartyPlayerImpl) asker, (PartyImpl) party);
			
			party.broadcastMessage(Messages.ADDCMD_ASK_DENY_BROADCAST, asker);
		}
	}
	
	@Override
	public void timeout(boolean sendMessages) {
		if (party.getAskRequests().remove(this) && asker.getPendingAskRequests().remove(this) && sendMessages) {
			((PartyPlayerImpl) asker).sendMessage(Messages.ADDCMD_ASK_TIMEOUT_NORESPONSE, (PartyImpl) party);
		}
	}
}
