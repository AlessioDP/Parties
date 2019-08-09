package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.TeleportTask;
import com.alessiodp.parties.common.utils.EconomyManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BungeeCommandTeleport extends CommandTeleport {
	
	public BungeeCommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		long unixNow = -1;
		if (ConfigParties.TELEPORT_COOLDOWN > 0 && !((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = ((PartiesPlugin) plugin).getCooldownManager().getTeleportCooldown().get(partyPlayer.getPlayerUUID());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(ConfigParties.TELEPORT_COOLDOWN - (unixNow - unixTimestamp))));
				return;
			}
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.TELEPORT, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		if (unixNow != -1) {
			((PartiesPlugin) plugin).getCooldownManager().getTeleportCooldown().put(partyPlayer.getPlayerUUID(), unixNow);
			plugin.getScheduler().scheduleAsyncLater(new TeleportTask(((PartiesPlugin) plugin), partyPlayer.getPlayerUUID()), ConfigParties.TELEPORT_COOLDOWN, TimeUnit.SECONDS);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_TELEPORT_START
					.replace("{value}", Integer.toString(ConfigParties.TELEPORT_COOLDOWN * 20))
					.replace("{player}", sender.getName()), true);
		}
		
		ProxiedPlayer bungeePlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(partyPlayer.getPlayerUUID());
		ServerInfo server = bungeePlayer.getServer().getInfo();
		
		sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_TELEPORTING);
		
		for (UUID member : party.getMembers()) {
			ProxiedPlayer bungeeMember = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(member);
			if (!member.equals(partyPlayer.getPlayerUUID()) && bungeeMember != null) {
				PartyPlayerImpl pp = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(bungeeMember.getUniqueId());
				bungeeMember.connect(server);
				pp.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTED, partyPlayer);
			}
		}
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_TELEPORT
				.replace("{player}", sender.getName()), true);
	}
}
