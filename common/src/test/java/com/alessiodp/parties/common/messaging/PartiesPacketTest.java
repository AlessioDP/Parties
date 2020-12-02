package com.alessiodp.parties.common.messaging;

import com.alessiodp.parties.common.PartiesPlugin;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		PartiesPacket.class
})
public class PartiesPacketTest {
	private static final String VERSION = "1";
	
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
		PartiesPlugin plugin = mockPlugin();
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(plugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadBasicFail() {
		PartiesPlugin plugin = mockPlugin();
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(plugin, packet.build());
		packet = packet.setPayloadNumber(1D);
		assertNotEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvanced() {
		PartiesPlugin plugin = mockPlugin();
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPlayerUuid(UUID.randomUUID())
				.setPlayerUuid(UUID.randomUUID())
				.setPayload("text")
				.setPayloadNumber(100D);
		
		PartiesPacket newPacket = PartiesPacket.read(plugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadWithPayloadRaw() {
		PartiesPlugin plugin = mockPlugin();
		
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeInt(1);
		raw.writeInt(2);
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPayloadRaw(raw.toByteArray());
		
		PartiesPacket newPacket = PartiesPacket.read(plugin, packet.build());
		
		assertEquals(packet, newPacket);
		
		ByteArrayDataInput rawReader = ByteStreams.newDataInput(newPacket.getPayloadRaw());
		assertEquals(1, rawReader.readInt());
		assertEquals(2, rawReader.readInt());
	}
	
	@Test
	public void testReadWithPayloadRawFail() {
		PartiesPlugin plugin = mockPlugin();
		
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeInt(1);
		raw.writeInt(2);
		PartiesPacket packet = makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.setPayloadRaw(raw.toByteArray());
		
		PartiesPacket newPacket = PartiesPacket.read(plugin, packet.build());
		
		raw.writeInt(3);
		packet.setPayloadRaw(raw.toByteArray());
		
		assertNotEquals(packet, newPacket);
	}
	
	private PartiesPlugin mockPlugin() {
		PartiesPlugin ret = mock(PartiesPlugin.class);
		when(ret.getVersion()).thenReturn(VERSION);
		return ret;
	}
	
	private PartiesPacket makePacket() {
		return new PartiesPacket(VERSION);
	}
}
