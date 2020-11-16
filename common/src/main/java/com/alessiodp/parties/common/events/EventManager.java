package com.alessiodp.parties.common.events;

import com.alessiodp.core.common.events.EventDispatcher;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class EventManager {
	@NonNull protected final PartiesPlugin plugin;
	@NonNull protected final EventDispatcher eventDispatcher;
	
	public final void callEvent(PartiesEvent event) {
		event.setApi(plugin.getApi());
		eventDispatcher.callEvent(event);
	}
	
	public abstract IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String name, String tag, boolean fixed);
	public abstract IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party);
	
	public abstract IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	public abstract IPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	
	public abstract IPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin);
	
	public abstract IPartyGetExperienceEvent preparePartyGetExperienceEvent(Party party, double experience, PartyPlayer killer);
	
	public abstract IChatEvent prepareChatEvent(PartyPlayer player, Party party, String message);
	
	public abstract IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, PartyPlayer inviter, JoinCause cause);
	public abstract IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, PartyPlayer inviter, JoinCause cause);
	
	public abstract IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy);
	public abstract IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy);
	
	public abstract IPartyLevelUpEvent prepareLevelUpEvent(Party party, int newLevel);
}
