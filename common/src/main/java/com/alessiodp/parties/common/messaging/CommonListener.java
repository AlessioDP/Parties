package com.alessiodp.parties.common.messaging;

import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostBroadcastEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandSetHome;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


public class CommonListener extends CommonPartyListener{
	private final PartiesPlugin plugin;

	public CommonListener(PartiesPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	public void handleUpdatePlayer(UUID playerId) {
		if (plugin.getPlayerManager().reloadPlayer(playerId)) {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_UPDATE_PLAYER,
					playerId.toString()), true);
		}
	}
	
	public void handleChatMessage(UUID partyId, UUID playerId, String message) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			try {
				PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
				
				player.performPartyMessage(message);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE,
						playerId.toString(), partyId.toString(), message), true);
			} catch (Exception ex) {
				plugin.getLoggerManager().logError(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY_ERROR, ex);
			}
		}
	}
	
	public void handleBroadcastMessage(UUID partyId, UUID playerId, String message) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			try {
				PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
				
				party.broadcastMessage(message, player);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_BROADCAST_MESSAGE,
						playerId != null ? playerId.toString() : "none", partyId.toString(), message), true);
			} catch (Exception ex) {
				plugin.getLoggerManager().logError(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY_ERROR, ex);
			}
		}
	}
	
	public void handleInvitePlayer(UUID partyId, UUID invitedId, UUID inviterId) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			try {
				PartyPlayerImpl invitedPlayer = plugin.getPlayerManager().getPlayer(invitedId);
				PartyPlayerImpl inviter = plugin.getPlayerManager().getPlayer(inviterId);
				
				party.invitePlayer(invitedPlayer, inviter);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY,
						invitedId.toString(), partyId.toString(), inviterId != null ? inviterId.toString() : "none"), true);
			} catch (Exception ex) {
				plugin.getLoggerManager().logError(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY_ERROR, ex);
			}
		}
	}
	
	public void handleAddHome(UUID partyId, String home) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			CommandSetHome.savePartyHome(party, PartyHomeImpl.deserialize(home));
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_ADD_HOME_BUNGEE,
					party.getId()), true);
		}
	}
	
	public void handleExperience(UUID partyId, UUID playerId, double experience, boolean gainMessage) {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
			PartyImpl party = plugin.getPartyManager().getParty(partyId);
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(playerId);
			if (party != null) {
				party.giveExperience(experience, partyPlayer, null, gainMessage);
			}
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_EXPERIENCE,
					CommonUtils.formatDouble(experience), partyId.toString(), playerId != null ? playerId.toString() : "none"), true);
		}
	}
	

	
	public void handlePostPartyDelete(UUID partyId, DeleteCause cause, UUID kickedId, UUID senderId) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl kickedPlayer = kickedId != null ? plugin.getPlayerManager().getPlayer(kickedId) : null;
			PartyPlayerImpl commandSender = senderId != null ? plugin.getPlayerManager().getPlayer(senderId) : null;
			
			if (plugin.getPartyManager().isPartyCached(partyId)) {
				party.getMembers().forEach(u -> {
					plugin.getPlayerManager().reloadPlayer(u);
				});
			}
			
			plugin.getPartyManager().removePartyFromCache(partyId);
			
			IPartyPostDeleteEvent event = plugin.getEventManager().preparePartyPostDeleteEvent(party, cause, kickedPlayer, commandSender);
			plugin.getEventManager().callEvent(event);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_DELETE_PARTY,
					partyId.toString(), cause.name(), kickedId != null ? kickedId.toString() : "none", senderId != null ? senderId.toString() : "none"), true);
		}
	}
	

	public void handlePostChat(UUID partyId, UUID playerId, String formattedMessage, String message) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
		if (party != null && player != null) {
			IPlayerPostChatEvent event = plugin.getEventManager().preparePlayerPostChatEvent(player, party, formattedMessage, message);
			plugin.getEventManager().callEvent(event);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE,
					playerId.toString(), partyId.toString(), message), true);
		}
	}
	
	public void handlePostBroadcast(UUID partyId, UUID playerId, String message) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
			
			IPartyPostBroadcastEvent event = plugin.getEventManager().preparePartyPostBroadcastEvent(party, message, player);
			plugin.getEventManager().callEvent(event);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_BROADCAST_MESSAGE,
					playerId != null ? playerId.toString() : "none", partyId.toString(), message), true);
		}
	}
	
	public void handlePostInvitePlayer(UUID partyId, UUID invitedId, UUID inviterId) {
		PartyImpl party = plugin.getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl invited = plugin.getPlayerManager().getPlayer(invitedId);
			PartyPlayerImpl inviter = inviterId != null ? plugin.getPlayerManager().getPlayer(inviterId) : null;
			
			IPlayerPostInviteEvent event = plugin.getEventManager().preparePlayerPostInviteEvent(invited, inviter, party);
			plugin.getEventManager().callEvent(event);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY,
					invitedId.toString(), partyId.toString(), inviterId != null ? inviterId.toString() : "none"), true);
		}
	}
	
	public void handlePostExperience(UUID partyId, UUID playerId, double number, boolean reload) {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
			if (reload)
				plugin.getPartyManager().reloadParty(partyId);
			PartyImpl party = plugin.getPartyManager().getParty(partyId);
			if (party != null) {
				PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(playerId);
				
				IPartyGetExperienceEvent event = plugin.getEventManager().preparePartyGetExperienceEvent(party, number, player);
				plugin.getEventManager().callEvent(event);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_EXPERIENCE,
						CommonUtils.formatDouble(number), partyId.toString(), playerId != null ? playerId.toString() : "none"), true);
			}
		}
	}
	
	public void handlePostLevelUp(UUID partyId, int number, boolean reload) {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE) {
			if (reload)
				plugin.getPartyManager().reloadParty(partyId);
			PartyImpl party = plugin.getPartyManager().getParty(partyId);
			if (party != null) {
				IPartyLevelUpEvent event = plugin.getEventManager().prepareLevelUpEvent(party, number);
				plugin.getEventManager().callEvent(event);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LEVEL_UP,
						partyId.toString(), number), true);
			}
		}
	}
}
