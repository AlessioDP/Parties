package com.alessiodp.parties.common.user;

import java.util.UUID;

public interface OfflineUser {
	UUID getUUID();
	boolean isOnline();
	String getName();
}
