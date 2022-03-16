package com.alessiodp.parties.bukkit.parties.objects;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessageDispatcher;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitPartyImpl extends PartyImpl {
	
	public BukkitPartyImpl(PartiesPlugin plugin, UUID id) {
		super(plugin, id);
	}
	
	@Override
	public CompletableFuture<Void> updateParty() {
		DynmapHandler.updatePartyMarker(this);
		return super.updateParty();
	}
	
	@Override
	public void delete() {
		DynmapHandler.cleanupMarkers(this);
		
		super.delete();
	}
	
	@Override
	public void rename(@Nullable String newName) {
		DynmapHandler.cleanupMarkers(this);
		
		super.rename(newName);
	}
	
	@Override
	public void sendPacketUpdate() {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendUpdateParty(this);
		}
	}
	
	@Override
	public void sendPacketCreate(PartyPlayerImpl leader) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendCreateParty(this, leader);
		} else {
			super.sendPacketCreate(leader);
		}
	}
	
	@Override
	public void sendPacketDelete(DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl commandSender) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendDeleteParty(this, cause, kicked, commandSender);
		} else {
			super.sendPacketDelete(cause, kicked, commandSender);
		}
	}
	
	@Override
	public void sendPacketRename(String oldName, String newName, PartyPlayerImpl player, boolean isAdmin) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRenameParty(this, oldName, newName, player, isAdmin);
		} else {
			super.sendPacketRename(oldName, newName, player, isAdmin);
		}
	}
	
	@Override
	public void sendPacketAddMember(PartyPlayerImpl player, JoinCause cause, PartyPlayerImpl inviter) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddMemberParty(this, player, cause, inviter);
		} else {
			super.sendPacketAddMember(player, cause, inviter);
		}
	}
	
	@Override
	public void sendPacketRemoveMember(PartyPlayerImpl player, LeaveCause cause, PartyPlayerImpl kicker) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRemoveMemberParty(this, player, cause, kicker);
		} else {
			super.sendPacketRemoveMember(player, cause, kicker);
		}
	}
	
	@Override
	public void sendPacketInvite(PartyPlayer invitedPlayer, PartyPlayer inviter) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to Bukkit servers
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendInvitePlayer(this, (PartyPlayerImpl) invitedPlayer, inviter != null ? (PartyPlayerImpl) inviter : null);
		} else {
			super.sendPacketInvite(invitedPlayer, inviter);
		}
	}
	
	@Override
	public void sendPacketExperience(double newExperience, PartyPlayer killer, boolean gainMessage) {
		if (plugin.isBungeeCordEnabled()) {
			// Send event to BungeeCord
			((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendPartyExperience(this, newExperience, (PartyPlayerImpl) killer, gainMessage);
		}
	}
	
	@Override
	public void sendPacketLevelUp(int newLevel) {
		throw new IllegalStateException("this method should be executed on BungeeCord only");
	}
	
	@Override
	public void broadcastMessage(@Nullable String message, @Nullable PartyPlayer partyPlayer) {
		if (message != null) {
			if (plugin.isBungeeCordEnabled())
				((BukkitPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendBroadcastMessage(this, partyPlayer != null ? (PartyPlayerImpl) partyPlayer : null, message);
			else
				super.broadcastMessage(message, partyPlayer);
		}
	}
	
	public void warnFriendlyFire(PartyPlayerImpl victim, PartyPlayerImpl attacker) {
		if (BukkitConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT) {
			String message = BukkitMessages.ADDCMD_PROTECTION_WARNHIT
					.replace("%player%", attacker.getName())
					.replace("%victim%", victim.getName());
			
			for (PartyPlayer onlineP : getOnlineMembers(true)) {
				if (!onlineP.getPlayerUUID().equals(attacker.getPlayerUUID())
						&& !plugin.getRankManager().checkPlayerRank((PartyPlayerImpl) onlineP, RankPermission.WARNONDAMAGE)) {
					User user = plugin.getPlayer(onlineP.getPlayerUUID());
					if (user != null)
						user.sendMessage(plugin.getMessageUtils().convertPlaceholders(message, (PartyPlayerImpl) onlineP, this), true);
				}
			}
		}
	}
}
