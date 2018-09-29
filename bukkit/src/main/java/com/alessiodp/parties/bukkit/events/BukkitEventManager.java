package com.alessiodp.parties.bukkit.events;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
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
import com.alessiodp.parties.bukkit.events.bukkit.CombustFriendlyFireBlockedEventHook;
import com.alessiodp.parties.bukkit.events.bukkit.FriendlyFireBlockedEventHook;
import com.alessiodp.parties.bukkit.events.bukkit.PotionsFriendlyFireBlockedEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartyPostCreateEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartyPostDeleteEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartyPreCreateEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartyPreDeleteEventHook;
import com.alessiodp.parties.bukkit.events.common.party.PartyRenameEventHook;
import com.alessiodp.parties.bukkit.events.common.player.ChatEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PlayerPostJoinEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PlayerPostLeaveEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PlayerPreJoinEventHook;
import com.alessiodp.parties.bukkit.events.common.player.PlayerPreLeaveEventHook;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesCombustFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesPotionsFriendlyFireBlockedEvent;
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
	
	public BukkitPartiesCombustFriendlyFireBlockedEvent prepareCombustFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityCombustByEntityEvent originalEvent) {
		return new CombustFriendlyFireBlockedEventHook(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesFriendlyFireBlockedEvent preparePartiesFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, EntityDamageByEntityEvent originalEvent) {
		return new FriendlyFireBlockedEventHook(victim, attacker, originalEvent);
	}
	
	public BukkitPartiesPotionsFriendlyFireBlockedEvent preparePartiesPotionsFriendlyFireBlockedEvent(PartyPlayer victim, PartyPlayer attacker, PotionSplashEvent originalEvent) {
		return new PotionsFriendlyFireBlockedEventHook(victim, attacker, originalEvent);
	}
	
	@Override
	public void callEvent(PartiesEvent event) {
		BukkitPartiesEvent bukkitEvent = (BukkitPartiesEvent) event;
		bukkitEvent.setApi(plugin.getApi());
		Bukkit.getPluginManager().callEvent(bukkitEvent);
	}
}
