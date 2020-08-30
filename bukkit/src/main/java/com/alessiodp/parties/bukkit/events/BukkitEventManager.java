package com.alessiodp.parties.bukkit.events;

import com.alessiodp.core.bukkit.events.BukkitEventDispatcher;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreExperienceDropEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyRenameEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesChatEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreLeaveEvent;
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
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPotionsFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

public class BukkitEventManager extends EventManager {
	public BukkitEventManager(PartiesPlugin plugin) {
		super(plugin, new BukkitEventDispatcher(plugin));
	}
	
	@Override
	public IPartyPostCreateEvent preparePartyPostCreateEvent(PartyPlayer player, Party party) {
		return new BukkitPartiesPartyPostCreateEvent(player, party);
	}
	
	@Override
	public IPartyPostDeleteEvent preparePartyPostDeleteEvent(String party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new BukkitPartiesPartyPostDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyPreCreateEvent preparePartyPreCreateEvent(PartyPlayer player, String name, String tag, boolean fixed) {
		return new BukkitPartiesPartyPreCreateEvent(player, name, tag, fixed);
	}
	
	@Override
	public IPartyPreDeleteEvent preparePartyPreDeleteEvent(Party party, DeleteCause cause, PartyPlayer kickedPlayer, PartyPlayer commandSender) {
		return new BukkitPartiesPartyPreDeleteEvent(party, cause, kickedPlayer, commandSender);
	}
	
	@Override
	public IPartyRenameEvent preparePartyRenameEvent(Party party, String newName, PartyPlayer player, boolean isAdmin) {
		return new BukkitPartiesPartyRenameEvent(party, newName, player, isAdmin);
	}
	
	@Override
	public IChatEvent prepareChatEvent(PartyPlayer player, Party party, String message) {
		return new BukkitPartiesChatEvent(player, party, message);
	}
	
	@Override
	public IPlayerPreJoinEvent preparePlayerPreJoinEvent(PartyPlayer player, Party party, PartyPlayer inviter, JoinCause cause) {
		return new BukkitPartiesPlayerPreJoinEvent(player, party, inviter, cause);
	}
	
	@Override
	public IPlayerPostJoinEvent preparePlayerPostJoinEvent(PartyPlayer player, Party party, PartyPlayer inviter, JoinCause cause) {
		return new BukkitPartiesPlayerPostJoinEvent(player, party, inviter, cause);
	}
	
	@Override
	public IPlayerPreLeaveEvent preparePlayerPreLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new BukkitPartiesPlayerPreLeaveEvent(player, party, isKicked, kickedBy);
	}
	
	@Override
	public IPlayerPostLeaveEvent preparePlayerPostLeaveEvent(PartyPlayer player, Party party, boolean isKicked, PartyPlayer kickedBy) {
		return new BukkitPartiesPlayerPostLeaveEvent(player, party, isKicked, kickedBy);
	}
	
	public BukkitPartiesCombustFriendlyFireBlockedEvent prepareCombustFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
		return new BukkitPartiesCombustFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesFriendlyFireBlockedEvent preparePartiesFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
		return new BukkitPartiesFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesPotionsFriendlyFireBlockedEvent preparePartiesPotionsFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
		return new BukkitPartiesPotionsFriendlyFireBlockedEvent(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesPartyPreExperienceDropEvent preparePartyPreExperienceDropEvent(Party party, PartyPlayer player, Entity killedEntity, int normalExperience, int skillapiExperience) {
		return new BukkitPartiesPartyPreExperienceDropEvent(party, player, killedEntity, normalExperience, skillapiExperience);
	}
	
	public BukkitPartiesPartyGetExperienceEvent preparePartyGetExperienceEvent(Party party, int experience, PartyPlayer killer) {
		return new BukkitPartiesPartyGetExperienceEvent(party, experience, killer);
	}
}
