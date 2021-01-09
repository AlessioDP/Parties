package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.common.commands.sub.CommandSetHome;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeCommandSetHome extends CommandSetHome {
	
	public BungeeCommandSetHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void getLocationAndSave(PartyPlayerImpl sender, PartyImpl party, String name) {
		ProxiedPlayer player = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(sender.getPlayerUUID());
		if (player != null) {
			String serverName = player.getServer().getInfo().getName();
			
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendAddHome(party, sender, makeAddHomeRaw(name, serverName));
		}
	}
	
	private byte[] makeAddHomeRaw(String home, String server) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(home);
		raw.writeUTF(server);
		return raw.toByteArray();
	}
}