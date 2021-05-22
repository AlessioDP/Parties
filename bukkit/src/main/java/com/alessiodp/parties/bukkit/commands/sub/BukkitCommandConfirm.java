package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.utils.LastConfirmedCommand;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitCommandConfirm extends PartiesSubCommand {
	
	public BukkitCommandConfirm(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				BukkitCommands.CONFIRM,
				null,
				BukkitConfigMain.COMMANDS_SUB_CONFIRM,
				false
		);
		
		syntax = baseSyntax();
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		BukkitPartyPlayerImpl partyPlayer = (BukkitPartyPlayerImpl) ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		LastConfirmedCommand packet = partyPlayer.getLastConfirmedCommand();
		if (packet == null
				|| ((System.currentTimeMillis() - packet.getTimestamp()) > BukkitConfigMain.ADDONS_VAULT_CONFIRM_TIMEOUT)) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_VAULT_CONFIRM_NOCMD);
			return;
		}
		
		// Command starts
		packet.setConfirmed(true);
		partyPlayer.setLastConfirmedCommand(packet);
		
		sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_VAULT_CONFIRM_CONFIRMED);
		
		// Make it sync
		plugin.getScheduler().getSyncExecutor().execute(() -> {
			Player player = Bukkit.getPlayer(sender.getUUID());
			if (player != null)
				Bukkit.getServer().dispatchCommand(player, packet.getCommand());
		});
	}
}
