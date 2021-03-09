package com.alessiodp.parties.bukkit.events;

import com.alessiodp.core.bukkit.events.BukkitEventDispatcher;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyLevelUpEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostRenameEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreRenameEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostChatEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreChatEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreInviteEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreLeaveEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFishHookFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPreExperienceDropEvent;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreRenameEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPotionsFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class BukkitEventManager extends EventManager {
	public BukkitEventManager(PartiesPlugin plugin) {
		super(plugin, new BukkitEventDispatcher(plugin));
	}
	
	@Override
	public IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party) {
		return new BukkitPartiesPartyPostCreateEvent(player, party);
	}
	
	@Override
	public IPartyPostDeleteEvent preparePartyPostDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new BukkitPartiesPartyPostDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String name, boolean fixed) {
		return new BukkitPartiesPartyPreCreateEvent(player, name, fixed);
	}
	
	@Override
	public IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new BukkitPartiesPartyPreDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreRenameEvent preparePartyPreRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin) {
		return new BukkitPartiesPartyPreRenameEvent(party, oldName, newName, player, isAdmin);
	}
	
	@Override
	public IPartyPostRenameEvent preparePartyPostRenameEvent(Party party, String oldName, String newName, PartyPlayer player, boolean isAdmin) {
		return new BukkitPartiesPartyPostRenameEvent(party, oldName, newName, player, isAdmin);
	}
	
	@Override
	public IPartyGetExperienceEvent preparePartyGetExperienceEvent(Party party, double experience, PartyPlayer killer) {
		return new BukkitPartiesPartyGetExperienceEvent(party, experience, killer);
	}
	
	@Override
	public IPlayerPreChatEvent preparePlayerPreChatEvent(PartyPlayer player, Party party, String message) {
		return new BukkitPartiesPlayerPreChatEvent(player, party, message);
	}
	
	@Override
	public IPlayerPostChatEvent preparePlayerPostChatEvent(PartyPlayer player, Party party, String message) {
		return new BukkitPartiesPlayerPostChatEvent(player, party, message);
	}
	
	@Override
	public IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter) {
		return new BukkitPartiesPlayerPreJoinEvent(player, party, cause, inviter);
	}
	
	@Override
	public IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, JoinCause cause, PartyPlayer inviter) {
		return new BukkitPartiesPlayerPostJoinEvent(player, party, cause, inviter);
	}
	
	@Override
	public IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker) {
		return new BukkitPartiesPlayerPreLeaveEvent(player, party, cause, kicker);
	}
	
	@Override
	public IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, LeaveCause cause, PartyPlayer kicker) {
		return new BukkitPartiesPlayerPostLeaveEvent(player, party, cause, kicker);
	}
	
	@Override
	public IPlayerPreInviteEvent preparePlayerPreInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		return new BukkitPartiesPlayerPreInviteEvent(invitedPlayer, inviter, party);
	}
	
	@Override
	public IPlayerPostInviteEvent preparePlayerPostInviteEvent(PartyPlayer invitedPlayer, PartyPlayer inviter, Party party) {
		return new BukkitPartiesPlayerPostInviteEvent(invitedPlayer, inviter, party);
	}
	
	@Override
	public IPartyLevelUpEvent prepareLevelUpEvent(Party party, int newLevel) {
		return new BukkitPartiesPartyLevelUpEvent(party, newLevel);
	}
	
	public BukkitPartiesCombustFriendlyFireBlockedEvent prepareCombustFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
		return new BukkitPartiesCombustFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesFriendlyFireBlockedEvent preparePartiesFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
		return new BukkitPartiesFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesFishHookFriendlyFireBlockedEvent preparePartiesFishHookFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, PlayerFishEvent originalEvent) {
		return new BukkitPartiesFishHookFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesPotionsFriendlyFireBlockedEvent preparePartiesPotionsFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
		return new BukkitPartiesPotionsFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesPreExperienceDropEvent preparePreExperienceDropEvent(Party party, PartyPlayer player, Entity killedEntity, double normalExperience, double skillApiExperience) {
		return new BukkitPartiesPreExperienceDropEvent(party, player, killedEntity, normalExperience, skillApiExperience);
	}
}
