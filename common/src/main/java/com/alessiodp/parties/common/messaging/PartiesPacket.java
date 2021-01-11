package com.alessiodp.parties.common.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.messaging.ADPPacket;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString
public class PartiesPacket extends ADPPacket {
	// Common
	@Getter private PacketType type;
	
	@Getter private UUID partyId;
	@Getter private UUID playerUuid;
	@Getter private String payload;
	@Getter private double payloadNumber = 0;
	@Getter private byte[] payloadRaw = new byte[] {};
	
	public PartiesPacket(String version) {
		super(version);
	}
	
	@Override
	public String getName() {
		return type != null ? type.name() : "UNKNOWN";
	}
	
	@Override
	public byte[] build() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		
		try {
			output.writeUTF(getVersion());
			output.writeUTF(type.name());
			output.writeUTF(partyId != null ? partyId.toString() : "");
			output.writeUTF(playerUuid != null ? playerUuid.toString() : "");
			output.writeUTF(payload != null ? payload : "");
			output.writeDouble(payloadNumber);
			output.writeInt(payloadRaw.length);
			output.write(payloadRaw);
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
				String type = input.readUTF();
				String partyId = input.readUTF();
				String playerUuid = input.readUTF();
				String payload = input.readUTF();
				double payloadNumber = input.readDouble();
				byte[] raw = new byte[input.readInt()];
				input.readFully(raw);
				
				PartiesPacket packet = new PartiesPacket(foundVersion);
				packet.type = PacketType.valueOf(type);
				if (!partyId.isEmpty())
					packet.partyId = UUID.fromString(partyId);
				if (!playerUuid.isEmpty())
					packet.playerUuid = UUID.fromString(playerUuid);
				if (!payload.isEmpty())
					packet.payload = payload;
				packet.payloadNumber = payloadNumber;
				packet.payloadRaw = raw;
				ret = packet;
			} else {
				plugin.getLoggerManager().printError(String.format(Constants.DEBUG_LOG_MESSAGING_FAILED_VERSION, plugin.getVersion(), foundVersion));
			}
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(String.format(Constants.DEBUG_LOG_MESSAGING_FAILED_READ, ex.getMessage()));
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
	
	public PartiesPacket setPayloadNumber(double payloadNumber) {
		this.payloadNumber = payloadNumber;
		return this;
	}
	
	public PartiesPacket setPayloadRaw(byte[] payloadRaw) {
		this.payloadRaw = payloadRaw;
		return this;
	}
	
	public enum PacketType {
		UPDATE_PARTY, UPDATE_PLAYER, LOAD_PARTY, LOAD_PLAYER, UNLOAD_PARTY, UNLOAD_PLAYER, PLAY_SOUND,
		
		// Party fields sync
		CREATE_PARTY, DELETE_PARTY, RENAME_PARTY, ADD_MEMBER_PARTY, REMOVE_MEMBER_PARTY, CHAT_MESSAGE, BROADCAST_MESSAGE, INVITE_PLAYER,
		
		// Actions fields
		ADD_HOME, HOME_TELEPORT, EXPERIENCE, LEVEL_UP,
		
		// Config packets
		CONFIGS, REQUEST_CONFIGS
	}
}
