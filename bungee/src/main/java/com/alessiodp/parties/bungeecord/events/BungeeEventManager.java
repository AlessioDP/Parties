package com.alessiodp.parties.bungeecord.events;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.events.common.party.PartiesPartyPostCreateEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartiesPartyPostDeleteEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartiesPartyPreCreateEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartiesPartyPreDeleteEventHook;
import com.alessiodp.parties.bungeecord.events.common.party.PartiesPartyRenameEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PartiesChatEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PartiesPlayerJoinEventHook;
import com.alessiodp.parties.bungeecord.events.common.player.PartiesPlayerLeaveEventHook;
import com.alessiodp.parties.bungeecord.events.implementations.BungeeAbstractEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyRenameEvent;
import com.alessiodp.parties.api.events.player.PartiesChatEvent;
import com.alessiodp.parties.api.events.player.PartiesPlayerJoinEvent;
import com.alessiodp.parties.api.events.player.PartiesPlayerLeaveEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

import java.util.UUID;

public class BungeeEventManager extends EventManager {
	
	public BungeeEventManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public PartiesPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party) {
		return new PartiesPartyPostCreateEventHook(player, party);
	}
	
	@Override
	public PartiesPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new PartiesPartyPostDeleteEventHook(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public PartiesPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String party, boolean fixed) {
		return new PartiesPartyPreCreateEventHook(player, party, fixed);
	}
	
	@Override
	public PartiesPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new PartiesPartyPreDeleteEventHook(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public PartiesPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		return new PartiesPartyRenameEventHook(party, newName, player, isAdmin);
	}
	
	@Override
	public PartiesChatEvent prepareChatEvent(PartyPlayer player, Party party, String message) {
		return new PartiesChatEventHook(player, party, message);
	}
	
	@Override
	public PartiesPlayerJoinEvent preparePlayerJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy) {
		return new PartiesPlayerJoinEventHook(player, party, isInvited, invitedBy);
	}
	
	@Override
	public PartiesPlayerLeaveEvent preparePlayerLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new PartiesPlayerLeaveEventHook(player, party, isKicked, kickedBy);
	}
	
	@Override
	public void callEvent(PartiesEvent event) {
		BungeeAbstractEvent bungeeEvent = (BungeeAbstractEvent) event;
		bungeeEvent.setApi(plugin.getApi());
		((BungeePartiesPlugin)plugin).getBootstrap().getProxy().getPluginManager().callEvent(bungeeEvent);
	}
}
