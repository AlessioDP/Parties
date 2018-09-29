package com.alessiodp.parties.bungeecord.commands.executors;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.commands.executors.CommandTeleport;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.TeleportTask;
import com.alessiodp.parties.common.utils.EconomyManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeCommandTeleport extends CommandTeleport {
	
	public BungeeCommandTeleport(PartiesPlugin instance) {
		super(instance);
	}
	
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		long unixNow = -1;
		if (ConfigParties.TELEPORT_COOLDOWN > 0 && !plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = plugin.getCooldownManager().getTeleportCooldown().get(pp.getPlayerUUID());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				pp.sendMessage(BungeeMessages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(ConfigParties.TELEPORT_COOLDOWN - (unixNow - unixTimestamp))));
				return;
			}
		}
		
		if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		if (unixNow != -1) {
			plugin.getCooldownManager().getTeleportCooldown().put(pp.getPlayerUUID(), unixNow);
			plugin.getPartiesScheduler().scheduleTaskLater(new TeleportTask(plugin, pp.getPlayerUUID()), ConfigParties.TELEPORT_COOLDOWN);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_START
					.replace("{value}", Integer.toString(ConfigParties.TELEPORT_COOLDOWN * 20))
					.replace("{player}", pp.getName()), true);
		}
		
		ProxiedPlayer bungeePlayer = ((BungeePartiesPlugin)plugin).getBootstrap().getProxy().getPlayer(pp.getPlayerUUID());
		ServerInfo server = bungeePlayer.getServer().getInfo();
		
		pp.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTING);
		
		for (UUID member : party.getMembers()) {
			ProxiedPlayer bungeeMember = ((BungeePartiesPlugin)plugin).getBootstrap().getProxy().getPlayer(member);
			if (!member.equals(pp.getPlayerUUID()) && bungeeMember != null) {
				bungeeMember.connect(server);
				plugin.getPlayerManager().getPlayer(member).sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTED, pp);
			}
		}
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_TELEPORT
				.replace("{player}", pp.getName()), true);
	}
}
