package com.alessiodp.parties.common.events;

import com.alessiodp.parties.common.PartiesPlugin;
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

public abstract class EventManager {
	protected PartiesPlugin plugin;
	
	protected EventManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract PartiesPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party);
	public abstract PartiesPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	public abstract PartiesPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String party, boolean fixed);
	public abstract PartiesPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	public abstract PartiesPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin);
	
	public abstract PartiesChatEvent prepareChatEvent(PartyPlayer player, Party party, String message);
	public abstract PartiesPlayerJoinEvent preparePlayerJoinEvent(PartyPlayer player, Party party, boolean isInvited, UUID invitedBy);
	public abstract PartiesPlayerLeaveEvent preparePlayerLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy);
	
	public abstract void callEvent(PartiesEvent event);
}
