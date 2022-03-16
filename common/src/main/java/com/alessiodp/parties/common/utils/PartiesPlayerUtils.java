package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.IPlayerUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class PartiesPlayerUtils implements IPlayerUtils {
	private final PartiesPlugin plugin;
	
	@Override
	public Set<ADPCommand> getAllowedCommands(@NotNull User user) {
		Set<ADPCommand> ret = new HashSet<>();
		PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
		if (player != null) {
			ret = player.getAllowedCommands();
		}
		return ret;
	}
}
