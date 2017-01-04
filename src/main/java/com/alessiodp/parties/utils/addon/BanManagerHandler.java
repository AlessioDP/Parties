package com.alessiodp.parties.utils.addon;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.utils.ConsoleColors;

import me.confuser.banmanager.BanManager;
import me.confuser.banmanager.BmAPI;
import me.confuser.banmanager.bukkitutil.listeners.Listeners;
import me.confuser.banmanager.data.PlayerData;
import me.confuser.banmanager.events.PlayerBannedEvent;

public class BanManagerHandler extends Listeners<BanManager>{
	Parties plugin;
	
	public BanManagerHandler(Parties instance){
		plugin = instance;
	}
	
	public static boolean isMuted(Player pl){
		return BmAPI.isMuted(pl);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event){
		PlayerData pl = event.getBan().getPlayer();
		String partyname = plugin.getConfigHandler().getData().getPlayerPartyName(pl.getUUID());
		if(!partyname.isEmpty()){
			Party party = plugin.getPartyHandler().loadParty(partyname);
			if(party != null){
				if(party.getLeader().equals(pl.getUUID())){
					party.sendBroadcastParty((OfflinePlayer)pl.getPlayer(), Messages.leave_disbanded);
					party.sendSpyMessage((OfflinePlayer)pl.getPlayer(), Messages.leave_disbanded);
					plugin.log(ConsoleColors.CYAN.getCode() + "Party " + party.getName() + " deleted via leader ban, by: " + pl.getName());
					party.removeParty();
				} else {
					party.getMembers().remove(pl.getUUID());
					party.getOnlinePlayers().remove(pl.getPlayer());
					
					party.sendBroadcastParty((OfflinePlayer)pl.getPlayer(), Messages.kick_kickedplayer);
					
					party.updateParty();
				}
			}
			plugin.getConfigHandler().getData().removePlayer(pl.getUUID());
		}
	}
}
