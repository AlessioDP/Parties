package com.alessiodp.parties.common.events;

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
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;

import java.util.UUID;

public abstract class EventManager {
	protected PartiesPlugin plugin;
	
	protected EventManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party);
	public abstract IPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	public abstract IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String party, boolean fixed);
	public abstract IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	public abstract IPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin);
	
	public abstract IChatEvent prepareChatEvent(PartyPlayer player, Party party, String message);
	public abstract IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy);
	public abstract IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy);
	public abstract IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy);
	public abstract IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy);
	
	public abstract void callEvent(PartiesEvent event);
}
