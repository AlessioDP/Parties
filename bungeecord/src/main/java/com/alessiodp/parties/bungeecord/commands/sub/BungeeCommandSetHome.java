package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.bungeecord.user.BungeeUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.commands.sub.CommandSetHome;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public class BungeeCommandSetHome extends CommandSetHome {
	
	public BungeeCommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void getLocationAndSave(PartyPlayerImpl sender, PartyImpl party, String name) {
		User user = plugin.getPlayer(sender.getPlayerUUID());
		if (user != null && ((BungeeUser) user).getServer() != null) {
			String serverName = ((BungeeUser) user).getServer().getName();
			
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddHome(user, party, name, serverName);
		}
	}
}