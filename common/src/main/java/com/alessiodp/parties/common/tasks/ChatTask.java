package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;

import java.util.UUID;

public class ChatTask implements Runnable {
	private PartiesPlugin plugin;
	private UUID uuid;
	
	public ChatTask(PartiesPlugin instance, UUID playerUUID) {
		plugin = instance;
		uuid = playerUUID;
	}
	
	@Override
	public void run() {
		plugin.getCooldownManager().getChatCooldown().remove(uuid);
		
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_CHAT_EXPIRE
				.replace("{uuid}", uuid.toString()), true);
	}
	
}