package com.alessiodp.parties.bungeecord.api;

import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.common.api.PartiesOptionsHandler;

public class BungeePartiesOptionsHandler extends PartiesOptionsHandler {
	@Override
	public boolean isRedisBungeeEnabled() {
		return BungeeConfigMain.PARTIES_BUNGEECORD_REDIS;
	}
}
