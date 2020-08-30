package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.Getter;

public class BungeeMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "bungee/messages.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUNGEE_MESSAGES;
	
	//Additional
	@ConfigOption(path = "other.follow.following-server")
	public static String OTHER_FOLLOW_SERVER;
	
	public BungeeMessages(PartiesPlugin plugin) {
		super(plugin);
	}
}
