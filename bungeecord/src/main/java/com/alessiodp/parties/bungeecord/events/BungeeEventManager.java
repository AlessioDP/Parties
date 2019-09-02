package com.alessiodp.parties.bungeecord.events;

import com.alessiodp.core.bungeecord.events.BungeeEventDispatcher;
import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyRenameEvent;
import com.alessiodp.parties.api.events.bungee.player.BungeePartiesChatEvent;
import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.bungee.player.BungeePartiesPlayerPreLeaveEvent;
import com.alessiodp.parties.api.events.bungee.unique.BungeePartiesPartyFollowEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyRenameEvent;
import com.alessiodp.parties.api.events.common.player.IChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

import java.util.UUID;

public class BungeeEventManager extends EventManager {
	
	public BungeeEventManager(PartiesPlugin plugin) {
		super(plugin, new BungeeEventDispatcher(plugin));
	}
	
	@Override
	public IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party) {
		return new BungeePartiesPartyPostCreateEvent(player, party);
	}
	
	@Override
	public IPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new BungeePartiesPartyPostDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String party, boolean fixed) {
		return new BungeePartiesPartyPreCreateEvent(player, party, fixed);
	}
	
	@Override
	public IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new BungeePartiesPartyPreDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		return new BungeePartiesPartyRenameEvent(party, newName, player, isAdmin);
	}
	
	@Override
	public IChatEvent prepareChatEvent(PartyPlayer player, Party party, String message) {
		return new BungeePartiesChatEvent(player, party, message);
	}
	
	@Override
	public IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		return new BungeePartiesPlayerPreJoinEvent(player, party, isInvited, invitedBy);
	}
	
	@Override
	public IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		return new BungeePartiesPlayerPostJoinEvent(player, party, isInvited, invitedBy);
	}
	
	@Override
	public IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new BungeePartiesPlayerPreLeaveEvent(player, party, isKicked, kickedBy);
	}
	
	@Override
	public IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new BungeePartiesPlayerPostLeaveEvent(player, party, isKicked, kickedBy);
	}
	
	public BungeePartiesPartyFollowEvent preparePartyFollowEvent(Party party, String server) {
		return new BungeePartiesPartyFollowEvent(party, server);
	}
}
