package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PartyHomeTest {
	
	@Test
	public void testDeserialize() {
		PartyHomeImpl expected = new PartyHomeImpl("name", "world", 100, 200, 300, 10, 20);
		
		assertEquals(expected, PartyHomeImpl.deserialize("name,world,100.0,200.0,300.0,10.0,20.0"));
		
		assertNotEquals(expected, PartyHomeImpl.deserialize("name,world,0.0,0.0,0.0,10.0,20.0"));
		assertNotEquals(expected, PartyHomeImpl.deserialize("name,world,100.0,200.0,300.0,0.0,0.0"));
		assertNotEquals(expected, PartyHomeImpl.deserialize(",world,100.0,200.0,300.0,10.0,20.0"));
		assertNotEquals(expected, PartyHomeImpl.deserialize("name,,100.0,200.0,300.0,10.0,20.0"));
		
		expected = new PartyHomeImpl("name", "world", 100, 200, 300, 10, 20, "server");
		assertEquals(expected, PartyHomeImpl.deserialize("name,world,100.0,200.0,300.0,10.0,20.0,server"));
	}
	
	@Test
	public void testDeserializeMultiple() {
		HashSet<PartyHomeImpl> expected = new HashSet<>();
		expected.add(new PartyHomeImpl("name", "world", 100, 200, 300, 10, 20));
		expected.add(new PartyHomeImpl("name2", "world", 100, 200, 300, 10, 20, "server"));
		
		assertEquals(expected, PartyHomeImpl.deserializeMultiple("name,world,100.0,200.0,300.0,10.0,20.0;name2,world,100.0,200.0,300.0,10.0,20.0,server"));
		
		assertNotEquals(expected, PartyHomeImpl.deserializeMultiple("nameX,world,100.0,200.0,300.0,10.0,20.0;name2,world,100.0,200.0,300.0,10.0,20.0,server"));
	}
	
	@Test
	public void testSerialize() {
		assertEquals(
				"name,world,100.0,200.0,300.0,10.0,20.0",
				new PartyHomeImpl("name", "world", 100, 200, 300, 10, 20).toString()
		);
		assertEquals(
				",world,100.0,200.0,300.0,10.0,20.0",
				new PartyHomeImpl(null, "world", 100, 200, 300, 10, 20).toString()
		);
		assertEquals(
				",world,100.0,200.0,300.0,10.0,20.0,server",
				new PartyHomeImpl(null, "world", 100, 200, 300, 10, 20, "server").toString()
		);
	}
	
	@Test
	public void testSerializeMultiple() {
		assertEquals(
				"name,world,100.0,200.0,300.0,10.0,20.0;name2,world,100.0,200.0,300.0,10.0,20.0,server",
				PartyHomeImpl.serializeMultiple(Sets.newSet(
						new PartyHomeImpl("name", "world", 100, 200, 300, 10, 20),
						new PartyHomeImpl("name2", "world", 100, 200, 300, 10, 20, "server")
				))
		);
	}
}
