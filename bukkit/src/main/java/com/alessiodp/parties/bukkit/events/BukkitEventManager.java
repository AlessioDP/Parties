package com.alessiodp.parties.bukkit.events;

import com.alessiodp.parties.bukkit.events.bukkit.BukkitPartiesCombustFriendlyFireBlockedEventHook;
import com.alessiodp.parties.bukkit.events.bukkit.BukkitPartiesFriendlyFireBlockedEventHook;
import com.alessiodp.parties.bukkit.events.bukkit.BukkitPartiesPotionsFriendlyFireBlockedEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartiesPartyPostCreateEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartiesPartyPostDeleteEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartiesPartyPreCreateEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartiesPartyPreDeleteEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartiesPartyRenameEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PartiesChatEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PartiesPlayerJoinEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PartiesPlayerLeaveEventHook;
import com.alessiodp.parties.bukkit.events.implementations.BukkitAbstractEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.events.bukkit.PartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.PartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.PartiesPotionsFriendlyFireBlockedEvent;
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
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.UUID;

public class BukkitEventManager extends EventManager {
	
	public BukkitEventManager(PartiesPlugin instance) {
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
	
	public PartiesCombustFriendlyFireBlockedEvent prepareCombustFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
		return new BukkitPartiesCombustFriendlyFireBlockedEventHook(victim, attacker, originalEvent);
	}
	
	public PartiesFriendlyFireBlockedEvent preparePartiesFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
		return new BukkitPartiesFriendlyFireBlockedEventHook(victim, attacker, originalEvent);
	}
	
	public PartiesPotionsFriendlyFireBlockedEvent preparePartiesPotionsFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
		return new BukkitPartiesPotionsFriendlyFireBlockedEventHook(victim, attacker, originalEvent);
	}
	
	@Override
	public void callEvent(PartiesEvent event) {
		BukkitAbstractEvent bukkitEvent = (BukkitAbstractEvent) event;
		bukkitEvent.setApi(plugin.getApi());
		Bukkit.getPluginManager().callEvent(bukkitEvent);
	}
}
