package com.alessiodp.parties.velocity.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.sub.CommandSetHome;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.bootstrap.VelocityPartiesBootstrap;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import org.jetbrains.annotations.NotNull;

public class VelocityCommandSetHome extends CommandSetHome {
	
	public VelocityCommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void getLocationAndSave(@NotNull PartyPlayerImpl sender, @NotNull PartyImpl party, @NotNull String name) {
		User user = plugin.getPlayer(sender.getPlayerUUID());
		Player player = ((VelocityPartiesBootstrap) plugin.getBootstrap()).getServer().getPlayer(sender.getPlayerUUID()).orElse(null);
		if (user != null && player != null) {
			ServerConnection serverConnection = player.getCurrentServer().orElse(null);
			if (serverConnection != null) {
				String serverName = serverConnection.getServerInfo().getName();
				
				((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddHome(user, party, name, serverName);
			}
		}
	}
}