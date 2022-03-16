package com.alessiodp.parties.velocity.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.Getter;

public class VelocityMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "velocity/messages.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_VELOCITY_MESSAGES;
	
	//Additional
	@ConfigOption(path = "other.follow.following-server")
	public static String OTHER_FOLLOW_SERVER;
	
	public VelocityMessages(PartiesPlugin plugin) {
		super(plugin);
	}
}
