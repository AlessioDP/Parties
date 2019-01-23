package com.alessiodp.parties.bungeecord.events;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
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
import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.events.bungee.PartyFollowEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPostCreateEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPostDeleteEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPreCreateEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPreDeleteEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyRenameEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.ChatEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PlayerPostJoinEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PlayerPostLeaveEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PlayerPreJoinEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PlayerPreLeaveEventHook;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

import java.util.UUID;

public class BungeeEventManager extends EventManager {
	
	public BungeeEventManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party) {
		return new PartyPostCreateEventHook(player, party);
	}
	
	@Override
	public IPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new PartyPostDeleteEventHook(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String party, boolean fixed) {
		return new PartyPreCreateEventHook(player, party, fixed);
	}
	
	@Override
	public IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new PartyPreDeleteEventHook(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		return new PartyRenameEventHook(party, newName, player, isAdmin);
	}
	
	@Override
	public IChatEvent prepareChatEvent(PartyPlayer player, Party party, String message) {
		return new ChatEventHook(player, party, message);
	}
	
	@Override
	public IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		return new PlayerPreJoinEventHook(player, party, isInvited, invitedBy);
	}
	
	@Override
	public IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		return new PlayerPostJoinEventHook(player, party, isInvited, invitedBy);
	}
	
	@Override
	public IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new PlayerPreLeaveEventHook(player, party, isKicked, kickedBy);
	}
	
	@Override
	public IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new PlayerPostLeaveEventHook(player, party, isKicked, kickedBy);
	}
	
	public BungeePartiesPartyFollowEvent preparePartyFollowEvent(Party party, String server) {
		return new PartyFollowEventHook(party, server);
	}
	
	@Override
	public void callEvent(PartiesEvent event) {
		BungeePartiesEvent bungeeEvent = (BungeePartiesEvent) event;
		bungeeEvent.setApi(plugin.getApi());
		((BungeePartiesPlugin)plugin).getBootstrap().getProxy().getPluginManager().callEvent(bungeeEvent);
	}
}
