package com.alessiodp.parties.bungeecord.messaging.redis;

import com.alessiodp.core.bungeecord.addons.external.RedisBungeeHandler;
import com.alessiodp.core.bungeecord.messaging.redis.RedisBungeeListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.messaging.CommonListener;
import com.alessiodp.parties.common.messaging.PartiesPacket;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyInviteImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PartiesRedisBungeeListener extends RedisBungeeListener {
	private final CommonListener commonListener;
	
	public PartiesRedisBungeeListener(ADPPlugin plugin) {
		super(plugin, plugin.getPluginName());
		commonListener = new CommonListener((PartiesPlugin) plugin);
	}
	
	@Override
	protected void onMessageEvent(byte[] bytes) {
		PartiesPacket packet = PartiesPacket.read(plugin, bytes);
		if (packet != null) {
			if (packet.getSource().equals(RedisBungeeHandler.getCurrentServer()))
				return;
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_REDIS_RECEIVED, packet.getType().name()), true);
			switch ((PartiesPacket.PacketType) packet.getType()) {
				case UPDATE_PARTY:
					commonListener.handleUpdateParty(packet.getParty());
					break;
				case UPDATE_PLAYER:
					commonListener.handleUpdatePlayer(packet.getPlayer());
					break;
				case CREATE_PARTY:
					commonListener.handlePostPartyCreate(packet.getParty(), packet.getPlayer(), false);
					break;
				case DELETE_PARTY:
					commonListener.handlePostPartyDelete(packet.getParty(), (DeleteCause) packet.getCause(), packet.getPlayer(), packet.getSecondaryPlayer());
					break;
				case RENAME_PARTY:
					commonListener.handlePostPartyRename(packet.getParty(), packet.getText(), packet.getSecondaryText(), packet.getPlayer(), packet.isBool());
					break;
				case ADD_MEMBER_PARTY:
					handlePostPartyAddMember(packet.getParty(), packet.getPlayer(), (JoinCause) packet.getCause(), packet.getSecondaryPlayer());
					break;
				case REMOVE_MEMBER_PARTY:
					commonListener.handlePostPartyRemoveMember(packet.getParty(), packet.getPlayer(), (LeaveCause) packet.getCause(), packet.getSecondaryPlayer());
					break;
				case CHAT_MESSAGE:
					handlePostChatMessage(packet.getParty(), packet.getPlayer(), packet.getText(), packet.getSecondaryText());
					break;
				case BROADCAST_MESSAGE:
					handlePostBroadcastMessage(packet.getParty(), packet.getPlayer(), packet.getText());
					break;
				case INVITE_PLAYER:
					handlePostInvitePlayer(packet.getParty(), packet.getPlayer(), packet.getSecondaryPlayer(), packet.getNumber());
					break;
				case EXPERIENCE:
					handlePostExperience(packet.getParty(), packet.getPlayer(), packet.getNumber());
					break;
				case LEVEL_UP:
					handlePostLevelUp(packet.getParty(), packet.getNumber());
					break;
				case REDIS_MESSAGE:
					handleRedisMessage(packet.getPlayer(), packet.getText(), packet.isBool());
					break;
				case REDIS_TITLE:
					handleRedisTitle(packet.getPlayer(), packet.getText(), packet.getSecondaryText());
					break;
				case REDIS_CHAT:
					handleRedisChat(packet.getPlayer(), packet.getText());
					break;
				default:
					// Nothing to do
					break;
			}
		} else {
			plugin.getLoggerManager().logError(PartiesConstants.DEBUG_MESSAGING_REDIS_RECEIVED_WRONG);
		}
	}
	
	public void handlePostPartyAddMember(UUID partyId, UUID playerId, JoinCause cause, UUID inviterId) {
		// Remove party invite, if in this server, before party reload
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getCacheParties().get(partyId);
		if (party != null) {
			party.getInviteRequests().stream()
					.filter(partyInvite -> partyInvite.getInvitedPlayer().getPlayerUUID().equals(playerId))
					.findAny()
					.ifPresent(partyInvite -> partyInvite.timeout(false));
		}
		
		commonListener.handlePostPartyAddMember(partyId, playerId, cause, inviterId);
	}
	
	public void handlePostChatMessage(UUID partyId, UUID playerId, String formattedMessage, String message) {
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl player = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerId);
			
			party.dispatchChatMessage(player, formattedMessage, message, false);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_CHAT_MESSAGE,
					playerId.toString(), partyId.toString(), message), true);
		}
	}
	
	public void handlePostBroadcastMessage(UUID partyId, UUID playerId, String message) {
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl player = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerId);
			
			party.broadcastDirectMessage(message, player, false);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_BROADCAST_MESSAGE,
					playerId != null ? playerId.toString() : "none", partyId.toString(), message), true);
		}
	}
	
	public void handlePostInvitePlayer(UUID partyId, UUID invitedId, UUID inviterId, double timeout) {
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl invited = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(invitedId);
			PartyPlayerImpl inviter = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(inviterId);
			
			IPlayerPostInviteEvent event = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostInviteEvent(invited, inviter, party);
			((PartiesPlugin) plugin).getEventManager().callEvent(event);
			
			PartyInviteImpl invite = new PartyInviteImpl((PartiesPlugin) plugin, party, invited, inviter);
			
			party.getInviteRequests().add(invite);
			invited.getPendingInvites().add(invite);
			
			invite.setActiveTask(plugin.getScheduler().scheduleAsyncLater(
					() -> invite.timeout(false),
					(int) timeout,
					TimeUnit.SECONDS
			));
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_INVITE_PARTY,
					invitedId.toString(), partyId.toString(), inviterId != null ? inviterId.toString() : "none"), true);
		}
	}
	
	public void handlePostExperience(UUID partyId, UUID playerId, double experience) {
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyId);
		if (party != null) {
			PartyPlayerImpl player = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerId);
			
			IPartyGetExperienceEvent event = ((PartiesPlugin) plugin).getEventManager().preparePartyGetExperienceEvent(party, experience, player);
			((PartiesPlugin) plugin).getEventManager().callEvent(event);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_EXPERIENCE,
					CommonUtils.formatDouble(experience), partyId.toString(), playerId != null ? playerId.toString() : "none"), true);
		}
	}
	
	public void handlePostLevelUp(UUID partyId, double level) {
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyId);
		if (party != null) {
			IPartyLevelUpEvent event = ((PartiesPlugin) plugin).getEventManager().prepareLevelUpEvent(party, (int) level);
			((PartiesPlugin) plugin).getEventManager().callEvent(event);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_MESSAGING_LISTEN_LEVEL_UP,
					partyId.toString(), (int) level), true);
		}
	}
	
	public void handleRedisMessage(UUID player, String message, boolean colorTranslation) {
		User user = plugin.getPlayer(player);
		if (user != null && user.isInsideNetwork()) {
			user.sendMessage(message, colorTranslation);
		}
	}
	
	public void handleRedisTitle(UUID player, String message, String numbersAsString) {
		User user = plugin.getPlayer(player);
		if (user != null && user.isInsideNetwork()) {
			int fadeInTime = Integer.parseInt(numbersAsString.split(":")[0]);
			int showTime = Integer.parseInt(numbersAsString.split(":")[1]);
			int fadeOutTime = Integer.parseInt(numbersAsString.split(":")[2]);
			user.sendTitle(message, fadeInTime, showTime, fadeOutTime);
		}
	}
	
	public void handleRedisChat(UUID player, String message) {
		User user = plugin.getPlayer(player);
		if (user != null && user.isInsideNetwork()) {
			user.chat(message);
		}
	}
}
