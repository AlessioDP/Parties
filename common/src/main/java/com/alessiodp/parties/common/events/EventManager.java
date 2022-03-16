package com.alessiodp.parties.common.events;

import com.alessiodp.core.common.events.EventDispatcher;
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
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class EventManager {
	@NotNull protected final PartiesPlugin plugin;
	@NotNull protected final EventDispatcher eventDispatcher;
	
	public final void callEvent(PartiesEvent event) {
		event.setApi(plugin.getApi());
		eventDispatcher.callEvent(event);
	}
	
	public abstract IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String name, boolean fixed);
	public abstract IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party);
	
	public abstract IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	public abstract IPartyPostDeleteEvent preparePartyPostDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender);
	
	public abstract IPartyPreRenameEvent preparePartyPreRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin);
	public abstract IPartyPostRenameEvent preparePartyPostRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin);
	
	public abstract IPartyGetExperienceEvent preparePartyGetExperienceEvent(Party party, double experience, PartyPlayer killer);
	
	public abstract IPlayerPreChatEvent preparePlayerPreChatEvent(PartyPlayer player, Party party, String formattedMessage, String message);
	public abstract IPlayerPostChatEvent preparePlayerPostChatEvent(PartyPlayer player, Party party, String formattedMessage, String message);
	
	public abstract IPartyPreBroadcastEvent preparePartyPreBroadcastEvent(Party party, String message, PartyPlayer player);
	public abstract IPartyPostBroadcastEvent preparePartyPostBroadcastEvent(Party party, String message, PartyPlayer player);
	
	public abstract IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter);
	public abstract IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter);
	
	public abstract IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker);
	public abstract IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker);
	
	public abstract IPlayerPreInviteEvent preparePlayerPreInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party);
	public abstract IPlayerPostInviteEvent preparePlayerPostInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party);
	
	public abstract IPlayerPreHomeEvent preparePlayerPreHomeEvent(PartyPlayer player, Party party, PartyHome home);
	public abstract IPlayerPostHomeEvent preparePlayerPostHomeEvent(PartyPlayer player, Party party, PartyHome home);
	
	public abstract IPlayerPreTeleportEvent preparePlayerPreTeleportEvent(PartyPlayer player, Party party, Object destination);
	public abstract IPlayerPostTeleportEvent preparePlayerPostTeleportEvent(PartyPlayer player, Party party, Object destination);
	
	public abstract IPartyPreExperienceDropEvent preparePreExperienceDropEvent(Party party, PartyPlayer player, Object killedEntity, double experience);
	public abstract IPartyLevelUpEvent prepareLevelUpEvent(Party party, int newLevel);
}
