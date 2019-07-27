package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.IPlayerUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PartiesPlayerUtils implements IPlayerUtils {
	private final PartiesPlugin plugin;
	
	@Override
	public List<ADPCommand> getAllowedCommands(@NonNull User user) {
		List<ADPCommand> ret = new ArrayList<>();
		PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(user.getUUID());
		if (player != null) {
			ret = player.getAllowedCommands();
		}
		return ret;
	}
}
