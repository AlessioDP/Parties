package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

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
		if (ConfigParties.GENERAL_NAME_RENAME_COOLDOWN > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_TELEPORT_BYPASS)) {
			long remainingCooldown = ((PartiesPlugin) plugin).getCooldownManager().canTeleport(partyPlayer.getPlayerUUID(), ConfigParties.ADDITIONAL_TELEPORT_COOLDOWN);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_TELEPORT_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
			
			((PartiesPlugin) plugin).getCooldownManager().startTeleportCooldown(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
		}
		
		// Command starts
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
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_TELEPORT,
				partyPlayer.getName(), party.getName()), true);
	}
}
