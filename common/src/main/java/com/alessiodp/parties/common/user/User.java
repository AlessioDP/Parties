package com.alessiodp.parties.common.user;

import java.util.UUID;

public interface User {
	UUID getUUID();
	boolean hasPermission(String permission);
	boolean isPlayer();
	String getName();
	void sendMessage(String message, boolean colorTranslation);
	void chat(String messageToSend);
}
