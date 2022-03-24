package com.alessiodp.parties.common.messaging;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CommonPartyListener {
    private final PartiesPlugin plugin;

    public void handleUpdateParty(UUID partyId) {
        if (plugin.getPartyManager().reloadParty(partyId)) {
            plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PARTY,
                    partyId.toString()), true);
        }
    }

    public void handlePostPartyCreate(UUID partyId, UUID playerId, boolean load) {
        PartyImpl party = load ? plugin.getPartyManager().loadParty(partyId) : plugin.getPartyManager().getParty(partyId);
        if (party != null) {
            if (load)
                plugin.getPlayerManager().reloadPlayer(playerId);
            PartyPlayerImpl leader = plugin.getPlayerManager().getPlayer(partyId);

            IPartyPostCreateEvent partiesPostCreateEvent = plugin.getEventManager().preparePartyPostCreateEvent(leader, party);
            plugin.getEventManager().callEvent(partiesPostCreateEvent);

            plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_CREATE_PARTY,
                    partyId.toString(), playerId != null ? playerId.toString() : "none"), true);
        }
    }
    public void handlePostPartyRename(UUID partyId, String oldName, String newName, UUID playerId, boolean isAdmin) {
        plugin.getPartyManager().reloadPartyIfCached(partyId);
        PartyImpl party = plugin.getPartyManager().getParty(partyId);
        if (party != null) {
            PartyPlayerImpl player = playerId != null ? plugin.getPlayerManager().getPlayer(playerId) : null;

            IPartyPostRenameEvent event = plugin.getEventManager().preparePartyPostRenameEvent(party, oldName, newName, player, isAdmin);
            plugin.getEventManager().callEvent(event);

            plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_RENAME_PARTY,
                    party.getId(), oldName, newName, player != null ? player.getPlayerUUID().toString() : "none"), true);
        }
    }

    public void handlePostPartyAddMember(UUID partyId, UUID playerId, JoinCause cause, UUID inviterId) {
        plugin.getPartyManager().reloadPartyIfCached(partyId);
        PartyImpl party = plugin.getPartyManager().getParty(partyId);
        if (party != null) {
            PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
            PartyPlayerImpl inviter = inviterId != null ? plugin.getPlayerManager().getPlayer(inviterId) : null;

            IPlayerPostJoinEvent event = plugin.getEventManager().preparePlayerPostJoinEvent(player, party, cause, inviter);
            plugin.getEventManager().callEvent(event);

            plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_MEMBER_PARTY,
                    playerId.toString(), partyId.toString(), cause.name(), inviterId != null ? inviterId.toString() : "none"), true);
        }
    }

    public void handlePostPartyRemoveMember(UUID partyId, UUID playerId, LeaveCause cause, UUID kickerId) {
        plugin.getPartyManager().reloadPartyIfCached(partyId);
        PartyImpl party = plugin.getPartyManager().getParty(partyId);
        if (party != null) {
            PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
            PartyPlayerImpl kicker = kickerId != null ? plugin.getPlayerManager().getPlayer(kickerId) : null;

            IPlayerPostLeaveEvent event = plugin.getEventManager().preparePlayerPostLeaveEvent(player, party, cause, kicker);
            plugin.getEventManager().callEvent(event);

            plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_REMOVE_MEMBER_PARTY,
                    playerId.toString(), partyId.toString(), cause.name(), kickerId != null ? kickerId.toString() : "none"), true);
        }
    }

}
