package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.ADPLibraryManager;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.migrator.Migrator;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.ColorManager;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.PartiesFileDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPPlugin.class,
		ADPBootstrap.class,
		ConfigMain.class,
		LoggerManager.class,
		Migrator.class,
		PartiesSQLDispatcher.class,
		OfflineUser.class,
		PartiesPlugin.class,
		PlayerManager.class
})
public class FileDispatcherTest {
	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();
	
	private PartiesPlugin mockPlugin;
	
	@Before
	public void setUp() {
		mockPlugin = mock(PartiesPlugin.class);
		ADPBootstrap mockBootstrap = mock(ADPBootstrap.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getPluginFallbackName()).thenReturn("parties");
		when(mockPlugin.getFolder()).thenReturn(testFolder.getRoot().toPath());
		when(mockPlugin.getBootstrap()).thenReturn(mockBootstrap);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getVersion()).thenReturn("1.0.0");
		
		ColorManager mockColorManager = mock(ColorManager.class);
		when(mockPlugin.getColorManager()).thenReturn(mockColorManager);
		when(mockColorManager.searchColorByName(anyString())).thenReturn(null);
		
		// Mock static ADPPlugin, used in DAOs
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		
		// Mock debug methods
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> getClass().getClassLoader().getResourceAsStream(mock.getArgument(0)));
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		doAnswer((args) -> {
			System.out.println((String) args.getArgument(0));
			return null;
		}).when(mockLoggerManager).logDebug(anyString(), anyBoolean());
		doAnswer((args) -> {
			System.out.println((String) args.getArgument(0));
			return null;
		}).when(mockLoggerManager).printError(anyString());
		doAnswer((args) -> {
			((Exception) args.getArgument(1)).printStackTrace();
			return null;
		}).when(mockLoggerManager).printErrorStacktrace(any(), any());
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		// Mock class loaders
		ADPLibraryManager mockLibraryManager = mock(ADPLibraryManager.class);
		when(mockLibraryManager.getIsolatedClassLoaderOf(any())).thenReturn(getClass().getClassLoader());
		when(mockPlugin.getLibraryManager()).thenReturn(mockLibraryManager);
	}
	
	private PartiesFileDispatcher getFileDispatcher() {
		ConfigMain.STORAGE_SETTINGS_YAML_DBFILE = "database.yaml";
		PartiesFileDispatcher ret = new PartiesFileDispatcher(mockPlugin, StorageType.YAML);
		ret.init();
		assertFalse(ret.isFailed());
		return ret;
	}
	
	
	@Test
	public void testPlayer() {
		PartiesFileDispatcher dispatcher = getFileDispatcher();
		player(dispatcher);
		dispatcher.stop();
	}
	
	private void player(PartiesFileDispatcher dispatcher) {
		PartyPlayerImpl player = initializePlayer(UUID.randomUUID());
		PartyPlayerImpl mockPlayer = mock(player.getClass());
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mock.getArgument(0)));
		
		
		player.setAccessible(true);
		player.setMuted(true);
		player.setAccessible(false);
		
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("players").getChildrenMap().size(), 0);
		dispatcher.updatePlayer(player);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("players").getChildrenMap().size(), 1);
		
		PartyPlayerImpl newPlayer = dispatcher.getPlayer(player.getPlayerUUID());
		
		assertEquals(player, newPlayer);
		
		// Player remove
		player.setAccessible(true);
		player.setMuted(false);
		player.setAccessible(false);
		dispatcher.updatePlayer(player);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("players").getChildrenMap().size(), 0);
	}
	
	@Test
	public void testParty() {
		PartiesFileDispatcher dispatcher = getFileDispatcher();
		party(dispatcher, true);
		dispatcher.stop();
	}
	
	private void party(PartiesFileDispatcher dispatcher, boolean remove) {
		PartyImpl party = initializeParty(UUID.randomUUID());
		PartyPlayerImpl player = initializePlayer(UUID.randomUUID());
		
		PartyImpl mockParty = mock(party.getClass());
		doReturn(CompletableFuture.completedFuture(null)).when(mockParty).updateParty();
		PartyPlayerImpl mockPlayer = mock(player.getClass());
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PartyManager mockPartyManager = mock(PartyManager.class);
		when(mockPlugin.getPartyManager()).thenReturn(mockPartyManager);
		when(mockPartyManager.initializeParty(any())).thenAnswer((mock) -> initializeParty(mock.getArgument(0)));
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mock.getArgument(0)));
		
		party.setAccessible(true);
		party.setup("test", player.getPlayerUUID().toString());
		party.setDescription("description");
		party.setKills(10);
		party.setMembers(Collections.singleton(player.getPlayerUUID()));
		party.setAccessible(false);
		
		
		player.setAccessible(true);
		player.setPartyId(party.getId());
		player.setAccessible(false);
		dispatcher.updatePlayer(player);
		
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 0);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("map-parties-by-name").getChildrenMap().size(), 0);
		dispatcher.updateParty(party);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 1);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("map-parties-by-name").getChildrenMap().size(), 1);
		
		PartyImpl newParty = dispatcher.getParty(party.getId());
		
		assertEquals(party, newParty);
		
		// Party remove
		if (remove) {
			dispatcher.removeParty(party);
			assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 0);
			assertEquals(dispatcher.getDatabase().getRootNode().getNode("map-parties-by-name").getChildrenMap().size(), 0);
		}
	}
	
	@Test
	public void testExists() {
		PartiesFileDispatcher dispatcher = getFileDispatcher();
		party(dispatcher, false);
		exists(dispatcher);
		dispatcher.stop();
	}
	
	private void exists(PartiesFileDispatcher dispatcher) {
		assertTrue(dispatcher.existsParty("test"));
		assertFalse(dispatcher.existsParty("test2"));
	}
	
	@Test
	public void testListParties() {
		PartyManager mockPartyManager = mock(PartyManager.class);
		when(mockPlugin.getPartyManager()).thenReturn(mockPartyManager);
		when(mockPartyManager.initializeParty(any())).thenAnswer((mock) -> initializeParty(mock.getArgument(0)));
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mock.getArgument(0)));
		
		PartiesFileDispatcher dispatcher = getFileDispatcher();
		populateWithParties(dispatcher);
		listPartiesNumber(dispatcher);
		listPartiesByName(dispatcher);
		listPartiesByMembers(dispatcher);
		listPartiesByKills(dispatcher);
		listPartiesByExperience(dispatcher);
		dispatcher.stop();
	}
	
	private void listPartiesNumber(PartiesFileDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test2");
		
		assertEquals(7, dispatcher.getListPartiesNumber());
	}
	
	private void listPartiesByName(PartiesFileDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test2");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.NAME, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test1", list.get(0).getName());
		assertEquals("test3", list.get(1).getName());
		assertEquals("test8", list.get(6).getName());
		
	}
	
	private void listPartiesByMembers(PartiesFileDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test7");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.MEMBERS, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test8", list.get(0).getName());
		assertEquals("test6", list.get(1).getName());
		assertEquals("test1", list.get(6).getName());
		
	}
	
	private void listPartiesByKills(PartiesFileDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test3");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.KILLS, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test4", list.get(0).getName());
		assertEquals("test2", list.get(1).getName());
		assertEquals("test5", list.get(6).getName());
		
	}
	
	private void listPartiesByExperience(PartiesFileDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test6");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.EXPERIENCE, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test7", list.get(0).getName());
		assertEquals("test5", list.get(1).getName());
		assertEquals("test8", list.get(6).getName());
		
	}
	
	@Test
	public void testListFixed() {
		PartiesFileDispatcher dispatcher = getFileDispatcher();
		party(dispatcher, false);
		listFixed(dispatcher);
		dispatcher.stop();
	}
	
	private void listFixed(PartiesFileDispatcher dispatcher) {
		PartyImpl party = initializeParty(UUID.randomUUID());
		
		party.setAccessible(true);
		party.setup("test3", null);
		party.setDescription("description");
		party.setAccessible(false);
		
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 1);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("map-parties-by-name").getChildrenMap().size(), 1);
		dispatcher.updateParty(party);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 2);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("map-parties-by-name").getChildrenMap().size(), 2);
		
		assertEquals(party, dispatcher.getParty(party.getId()));
		assertEquals(party, dispatcher.getPartyByName(party.getName()));
		
		Set<PartyImpl> list = dispatcher.getListFixed();
		
		assertEquals(1, list.size());
		assertEquals(party, list.iterator().next());
	}
	
	private void populateWithParties(PartiesFileDispatcher dispatcher) {
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 0);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("players").getChildrenMap().size(), 0);
		
		insertOneParty(dispatcher, "test1", 1, 170, 200);
		insertOneParty(dispatcher, "test2", 2, 180, 300);
		insertOneParty(dispatcher, "test3", 3, 190, 400);
		insertOneParty(dispatcher, "test4", 4, 200, 500);
		insertOneParty(dispatcher, "test5", 5, 130, 600);
		insertOneParty(dispatcher, "test6", 6, 140, 700);
		insertOneParty(dispatcher, "test7", 7, 150, 800);
		insertOneParty(dispatcher, "test8", 8, 160, 100);
		
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("parties").getChildrenMap().size(), 8);
		assertEquals(dispatcher.getDatabase().getRootNode().getNode("players").getChildrenMap().size(), 36);
	}
	
	private void insertOneParty(PartiesFileDispatcher dispatcher, String partyName, int numberOfPlayers, int kills, double experience) {
		PartyImpl mockParty = mock(PartyImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockParty).updateParty();
		PartyPlayerImpl mockPlayer = mock(PartyPlayerImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PartyImpl party = initializeParty(UUID.randomUUID());
		Set<UUID> members = new HashSet<>();
		for (int c=0; c < numberOfPlayers; c++) {
			PartyPlayerImpl player = initializePlayer(UUID.randomUUID());
			members.add(player.getPlayerUUID());
			player.setAccessible(true);
			player.setPartyId(party.getId());
			player.setAccessible(false);
			dispatcher.updatePlayer(player);
		}
		
		party.setAccessible(true);
		party.setup(partyName, members.iterator().next().toString());
		party.setKills(kills);
		party.setExperience(experience);
		party.setMembers(members);
		party.setAccessible(false);
		
		dispatcher.updateParty(party);
	}
	
	private PartyPlayerImpl initializePlayer(UUID uuid) {
		return new PartyPlayerImpl(mockPlugin, uuid) {
			@Override
			public void playSound(String sound, double volume, double pitch) {
				// Nothing to do
			}
			
			@Override
			public void playChatSound() {
				// Nothing to do
			}
			
			@Override
			public void playBroadcastSound() {
				// Nothing to do
			}
			
			@Override
			public void sendPacketUpdate() {
				// Nothing to do
			}
			
			@Override
			public boolean isVanished() {
				return false;
			}
		};
	}
	
	private PartyImpl initializeParty(UUID id) {
		return new PartyImpl(mockPlugin, id) {
			@Override
			public void sendPacketUpdate() {
				// Nothing to do
			}
			
			@Override
			public void sendPacketExperience(double newExperience, PartyPlayer killer) {
				// Nothing to do
			}
			
			@Override
			public void sendPacketLevelUp(int newLevel) {
				// Nothing to do
			}
		};
	}
}
