package com.alessiodp.parties.common.messaging;

import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class PartiesPacket {
	@Getter String version;
	@Getter @Setter Type type;
	
	@Getter @Setter String partyName;
	@Getter @Setter UUID playerUuid;
	@Getter @Setter String payload;
	
	public PartiesPacket(String pluginVersion) {
		version = pluginVersion;
		partyName = "";
		playerUuid = UUID.randomUUID();
		payload = "";
	}
	
	public byte[] makePacket() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		
		output.writeUTF(version);
		output.writeUTF(type.toString());
		output.writeUTF(partyName);
		output.writeUTF(playerUuid.toString());
		output.writeUTF(payload);
		
		return output.toByteArray();
	}
	
	public static PartiesPacket readPacket(String pluginVersion, byte[] bytes) {
		PartiesPacket ret = null;
		try {
			ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
			String version = input.readUTF();
			
			if (version.equals(pluginVersion)) {
				ret = new PartiesPacket(version);
				ret.type = Type.valueOf(input.readUTF());
				ret.partyName = input.readUTF();
				ret.playerUuid = UUID.fromString(input.readUTF());
				ret.payload = input.readUTF();
			} else {
				LoggerManager.printError(Constants.DEBUG_MESSAGING_PACKET_VERSIONMISMATCH);
			}
		} catch (Exception ex) {
			LoggerManager.printError(Constants.DEBUG_MESSAGING_PACKET_READERROR);
			ex.printStackTrace();
			ret = null;
		}
		return ret;
	}
	
	public enum Type {
		PLAYER_UPDATED, PARTY_UPDATED, PARTY_RENAMED, PARTY_REMOVED, CHAT_MESSAGE, BROADCAST_MESSAGE
	}
}
