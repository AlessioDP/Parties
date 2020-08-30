package com.alessiodp.parties.common.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.messaging.ADPPacket;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;

import java.util.UUID;

public class PartiesPacket extends ADPPacket {
	// Common
	@Getter private PacketType type;
	
	@Getter private UUID partyId = UUID.randomUUID();
	@Getter private UUID playerUuid = UUID.randomUUID();
	@Getter private String payload = "";
	
	public PartiesPacket(String version) {
		super(version);
	}
	
	@Override
	public byte[] build() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		
		try {
			output.writeUTF(getVersion());
			output.writeUTF(type.name());
			output.writeUTF(partyId.toString());
			output.writeUTF(playerUuid.toString());
			output.writeUTF(payload);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return output.toByteArray();
	}
	
	public static PartiesPacket read(ADPPlugin plugin, byte[] bytes) {
		PartiesPacket ret = null;
		try {
			ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
			String foundVersion = input.readUTF();
			
			if (foundVersion.equals(plugin.getVersion())) {
				PartiesPacket packet = new PartiesPacket(foundVersion);
				packet.type = PacketType.valueOf(input.readUTF());
				packet.partyId = UUID.fromString(input.readUTF());
				packet.playerUuid = UUID.fromString(input.readUTF());
				packet.payload = input.readUTF();
				ret = packet;
			} else {
				plugin.getLoggerManager().printError(Constants.DEBUG_LOG_MESSAGING_FAILED_VERSION
						.replace("{current}", plugin.getVersion())
						.replace("{version}", foundVersion));
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(Constants.DEBUG_LOG_MESSAGING_FAILED_READ
					.replace("{message}", ex.getMessage()));
		}
		return ret;
	}
	
	public PartiesPacket setType(PacketType type) {
		this.type = type;
		return this;
	}
	
	public PartiesPacket setPartyId(UUID partyId) {
		this.partyId = partyId;
		return this;
	}
	
	public PartiesPacket setPlayerUuid(UUID playerUuid) {
		this.playerUuid = playerUuid;
		return this;
	}
	
	public PartiesPacket setPayload(String payload) {
		this.payload = payload;
		return this;
	}
	
	public enum PacketType {
		PLAYER_UPDATED, PARTY_UPDATED, PARTY_RENAMED, PARTY_REMOVED, CHAT_MESSAGE, BROADCAST_MESSAGE
	}
}
