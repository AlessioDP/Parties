package com.alessiodp.parties.bungeecord.events;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyRenameEvent;
import com.alessiodp.parties.api.events.common.player.IChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerLeaveEvent;
import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPostCreateEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPostDeleteEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPreCreateEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyPreDeleteEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartyRenameEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.ChatEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PlayerJoinEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PlayerLeaveEventHook;
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
	public IPlayerJoinEvent preparePlayerJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		return new PlayerJoinEventHook(player, party, isInvited, invitedBy);
	}
	
	@Override
	public IPlayerLeaveEvent preparePlayerLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new PlayerLeaveEventHook(player, party, isKicked, kickedBy);
	}
	
	@Override
	public void callEvent(PartiesEvent event) {
		BungeePartiesEvent bungeeEvent = (BungeePartiesEvent) event;
		bungeeEvent.setApi(plugin.getApi());
		((BungeePartiesPlugin)plugin).getBootstrap().getProxy().getPluginManager().callEvent(bungeeEvent);
	}
}
