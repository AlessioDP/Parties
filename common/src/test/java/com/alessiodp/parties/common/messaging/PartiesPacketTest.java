package com.alessiodp.parties.common.messaging;

import com.alessiodp.parties.common.PartiesPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
	public void testBuildBasic() throws IOException {
		makePacket()
				.setType(PartiesPacket.PacketType.values()[0])
				.build();
	}
	
	@Test
	public void testBuildAdvanced() throws IOException {
		makePacket()
				.setPlayer(UUID.randomUUID())
				.setPlayer(UUID.randomUUID())
				.setText("text")
				.setNumber(100D)
				.setType(PartiesPacket.PacketType.values()[0])
				.build();
	}
	
	@Test
	public void testReadBasic() throws IOException {
		PartiesPacket packet = (PartiesPacket) makePacket()
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	@Test
	public void testReadBasicFail() throws IOException {
		PartiesPacket packet = (PartiesPacket) makePacket()
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		packet = packet.setNumber(1D);
		assertNotEquals(packet, newPacket);
	}
	
	@Test
	public void testReadAdvanced() throws IOException {
		PartiesPacket packet = (PartiesPacket) makePacket()
				.setPlayer(UUID.randomUUID())
				.setPlayer(UUID.randomUUID())
				.setText("text")
				.setNumber(100D)
				.setType(PartiesPacket.PacketType.values()[0]);
		
		PartiesPacket newPacket = PartiesPacket.read(mockPlugin, packet.build());
		
		assertEquals(packet, newPacket);
	}
	
	private PartiesPacket makePacket() {
		return (PartiesPacket) new PartiesPacket().setVersion(VERSION);
	}
}
