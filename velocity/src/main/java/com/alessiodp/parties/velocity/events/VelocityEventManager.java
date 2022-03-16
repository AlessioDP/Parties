package com.alessiodp.parties.velocity.events;

import com.alessiodp.core.velocity.events.VelocityEventDispatcher;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostBroadcastEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreBroadcastEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreExperienceDropEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreRenameEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostHomeEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreHomeEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreTeleportEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyLevelUpEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPostBroadcastEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPostRenameEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPreBroadcastEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPreExperienceDropEvent;
import com.alessiodp.parties.api.events.velocity.party.VelocityPartiesPartyPreRenameEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPostChatEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPostHomeEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPostTeleportEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPreChatEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPreHomeEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPreInviteEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPreLeaveEvent;
import com.alessiodp.parties.api.events.velocity.player.VelocityPartiesPlayerPreTeleportEvent;
import com.alessiodp.parties.api.events.velocity.unique.VelocityPartiesPartyFollowEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class VelocityEventManager extends EventManager {
	
	public VelocityEventManager(PartiesPlugin plugin) {
		super(plugin, new VelocityEventDispatcher(plugin));
	}
	
	@Override
	public IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party) {
		return new VelocityPartiesPartyPostCreateEvent(player, party);
	}
	
	@Override
	public IPartyPostDeleteEvent preparePartyPostDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new VelocityPartiesPartyPostDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String name, boolean fixed) {
		return new VelocityPartiesPartyPreCreateEvent(player, name, fixed);
	}
	
	@Override
	public IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new VelocityPartiesPartyPreDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreRenameEvent preparePartyPreRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin) {
		return new VelocityPartiesPartyPreRenameEvent(party, oldName, newName, player, isAdmin);
	}
	
	@Override
	public IPartyPostRenameEvent preparePartyPostRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin) {
		return new VelocityPartiesPartyPostRenameEvent(party, oldName, newName, player, isAdmin);
	}
	
	@Override
	public IPartyGetExperienceEvent preparePartyGetExperienceEvent(Party party, double experience, PartyPlayer killer) {
		return new VelocityPartiesPartyGetExperienceEvent(party, experience, killer);
	}
	
	@Override
	public IPlayerPreChatEvent preparePlayerPreChatEvent(PartyPlayer player, Party party, String formattedMessage, String message) {
		return new VelocityPartiesPlayerPreChatEvent(player, party, formattedMessage, message);
	}
	
	@Override
	public IPlayerPostChatEvent preparePlayerPostChatEvent(PartyPlayer player, Party party, String formattedMessage, String message) {
		return new VelocityPartiesPlayerPostChatEvent(player, party, formattedMessage, message);
	}
	
	@Override
	public IPartyPreBroadcastEvent preparePartyPreBroadcastEvent(Party party, String message, PartyPlayer player) {
		return new VelocityPartiesPartyPreBroadcastEvent(party, message, player);
	}
	
	@Override
	public IPartyPostBroadcastEvent preparePartyPostBroadcastEvent(Party party, String message, PartyPlayer player) {
		return new VelocityPartiesPartyPostBroadcastEvent(party, message, player);
	}
	
	@Override
	public IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter) {
		return new VelocityPartiesPlayerPreJoinEvent(player, party, cause, inviter);
	}
	
	@Override
	public IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter) {
		return new VelocityPartiesPlayerPostJoinEvent(player, party, cause, inviter);
	}
	
	@Override
	public IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker) {
		return new VelocityPartiesPlayerPreLeaveEvent(player, party, cause, kicker);
	}
	
	@Override
	public IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker) {
		return new VelocityPartiesPlayerPostLeaveEvent(player, party, cause, kicker);
	}
	
	@Override
	public IPlayerPreInviteEvent preparePlayerPreInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		return new VelocityPartiesPlayerPreInviteEvent(invitedPlayer, inviter, party);
	}
	
	@Override
	public IPlayerPostInviteEvent preparePlayerPostInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		return new VelocityPartiesPlayerPostInviteEvent(invitedPlayer, inviter, party);
	}
	
	@Override
	public IPlayerPreHomeEvent preparePlayerPreHomeEvent(PartyPlayer player, Party party, PartyHome home) {
		return new VelocityPartiesPlayerPreHomeEvent(player, party, home);
	}
	
	@Override
	public IPlayerPostHomeEvent preparePlayerPostHomeEvent(PartyPlayer player, Party party, PartyHome home) {
		return new VelocityPartiesPlayerPostHomeEvent(player, party, home);
	}
	
	@Override
	public IPlayerPreTeleportEvent preparePlayerPreTeleportEvent(PartyPlayer player, Party party, Object destination) {
		return new VelocityPartiesPlayerPreTeleportEvent(player, party, (RegisteredServer) destination);
	}
	
	@Override
	public IPlayerPostTeleportEvent preparePlayerPostTeleportEvent(PartyPlayer player, Party party, Object destination) {
		return new VelocityPartiesPlayerPostTeleportEvent(player, party, (RegisteredServer) destination);
	}
	
	@Override
	public IPartyPreExperienceDropEvent preparePreExperienceDropEvent(Party party, PartyPlayer player, Object killedEntity, double experience) {
		return new VelocityPartiesPartyPreExperienceDropEvent(party, player, killedEntity, experience);
	}
	
	@Override
	public IPartyLevelUpEvent prepareLevelUpEvent(Party party, int newLevel) {
		return new VelocityPartiesPartyLevelUpEvent(party, newLevel);
	}
	
	public VelocityPartiesPartyFollowEvent preparePartyFollowEvent(Party party, RegisteredServer server) {
		return new VelocityPartiesPartyFollowEvent(party, server);
	}
}
