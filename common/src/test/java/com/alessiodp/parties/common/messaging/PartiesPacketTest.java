package com.alessiodp.parties.common.messaging;

import com.alessiodp.parties.common.PartiesPlugin;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartiesPacketTest {
	private static final PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
	private static final String VERSION = "1";
	
	@BeforeAll
	public static void setUp() {
		when(mockPlugin.getVersion()).thenReturn(VERSION);
	}
	
	
	@Test
	public void testBuildBasic() {
		makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.build();
	}
	
	@Test
	public void testBuildAdvanced() {
		makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPlayerUuid(UUID.randomUUID())
				.setPlayerUuid(UUID.randomUUID())
				.setPayload("text")
				.setPayloadNumber(100D)
				.build();
	}
	
	@Test
	public void testBuildWithPayloadRaw() {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeInt(1);
		raw.writeInt(2);
		makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPayloadRaw(raw.toByteArray())
				.build();
	}
	
	@Test
	public void testReadBasic() {
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadBasicFail() {
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		packet = packet.setPayloadNumber(1D);
		assertNotEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvanced() {
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPlayerUuid(UUID.randomUUID())
				.setPlayerUuid(UUID.randomUUID())
				.setPayload("text")
				.setPayloadNumber(100D);
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadWithPayloadRaw() {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeInt(1);
		raw.writeInt(2);
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPayloadRaw(raw.toByteArray());
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
		
		ByteArrayDataInput rawReader = ByteStreams.newDataInput(newPacket.getPayloadRaw());
		assertEquals(1, rawReader.readInt());
		assertEquals(2, rawReader.readInt());
	}
	
	@Test
	public void testReadWithPayloadRawFail() {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeInt(1);
		raw.writeInt(2);
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPayloadRaw(raw.toByteArray());
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		
		raw.writeInt(3);
		packet.setPayloadRaw(raw.toByteArray());
		
		assertNotEquals(packet, newPacket);
	}
	
	private PartiesPacket makePacket() {
		return new PartiesPacket(VERSION);
	}
}
