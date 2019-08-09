package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ChatTask implements Runnable {
	@NonNull private final PartiesPlugin plugin;
	@NonNull private final UUID uuid;
	
	@Override
	public void run() {
		plugin.getCooldownManager().getChatCooldown().remove(uuid);
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_CHAT_EXPIRE
				.replace("{uuid}", uuid.toString()), true);
	}
	
}