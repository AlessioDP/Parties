package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.sql.connection.ConnectionFactory;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.ColorManager;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import com.alessiodp.parties.common.storage.sql.dao.parties.H2PartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.PartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.PostgreSQLPartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.parties.SQLitePartiesDao;
import com.alessiodp.parties.common.storage.sql.dao.players.H2PlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.PlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.PostgreSQLPlayersDao;
import com.alessiodp.parties.common.storage.sql.dao.players.SQLitePlayersDao;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPPlugin.class
})
public class SQLDispatcherTest {
	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();
	
	private PartiesPlugin mockPlugin;
	
	@Before
	public void setUp() {
		mockPlugin = mock(PartiesPlugin.class);
		ADPBootstrap mockBootstrap = mock(ADPBootstrap.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getPluginFallbackName()).thenReturn("parties");
		when(mockPlugin.getFolder()).thenReturn(Paths.get("../testing/"));
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
			((Exception) args.getArgument(1)).printStackTrace();
			return null;
		}).when(mockLoggerManager).printErrorStacktrace(any(), any());
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
	}
	
	private PartiesSQLDispatcher getSQLDispatcherH2() {
		ConfigMain.STORAGE_SETTINGS_H2_DBFILE = "";
		PartiesSQLDispatcher ret = new PartiesSQLDispatcher(mockPlugin, StorageType.H2) {
			@Override
			public ConnectionFactory initConnectionFactory() {
				ConnectionFactory ret = super.initConnectionFactory();
				ret.setDatabaseUrl("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
				return ret;
			}
		};
		ret.init();
		return ret;
	}
	
	private PartiesSQLDispatcher getSQLDispatcherSQLite() {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "";
		PartiesSQLDispatcher ret = new PartiesSQLDispatcher(mockPlugin, StorageType.SQLITE) {
			@Override
			public ConnectionFactory initConnectionFactory() {
				ConnectionFactory ret = super.initConnectionFactory();
				try {
					ret.setDatabaseUrl("jdbc:sqlite:" + testFolder.newFile("database.db").toPath().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ret;
			}
		};
		ret.init();
		return ret;
	}
	
	public static PartiesSQLDispatcher getSQLDispatcherMySQL(PartiesPlugin plugin) {
		// Manual test only
		/*
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET = "utf8";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS = "localhost";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT = "3306";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE = "parties";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME = "root";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD = "";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE = 10;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME = 1800000;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL = false;
		PartiesSQLDispatcher ret = new PartiesSQLDispatcher(plugin, StorageType.MYSQL);
		ret.init();
		
		ret.getConnectionFactory().getJdbi().onDemand(PartiesDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PlayersDao.class).deleteAll();
		return ret;
		*/
		return null;
	}
	
	public static PartiesSQLDispatcher getSQLDispatcherMariaDB(PartiesPlugin plugin) {
		// Manual test only
		/*
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET = "utf8";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS = "localhost";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT = "3306";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE = "parties";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME = "root";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD = "";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE = 10;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME = 1800000;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL = false;
		PartiesSQLDispatcher ret = new PartiesSQLDispatcher(plugin, StorageType.MARIADB);
		ret.init();
		
		ret.getConnectionFactory().getJdbi().onDemand(PartiesDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PlayersDao.class).deleteAll();
		return ret;
		*/
		return null;
	}
	
	public static PartiesSQLDispatcher getSQLDispatcherPostgreSQL(PartiesPlugin plugin) {
		// Manual test only
		/*
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CHARSET = "utf8";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_ADDRESS = "localhost";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PORT = "5432";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_DATABASE = "parties";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USERNAME = "postgres";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_PASSWORD = "";
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_POOLSIZE = 10;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_CONNLIFETIME = 1800000;
		ConfigMain.STORAGE_SETTINGS_REMOTE_SQL_USESSL = false;
		PartiesSQLDispatcher ret = new PartiesSQLDispatcher(plugin, StorageType.POSTGRESQL);
		ret.init();
		
		ret.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class).deleteAll();
		ret.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class).deleteAll();
		return ret;
		*/
		return null;
	}
	
	@Test
	public void testPlayer() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PlayersDao.class), true);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePlayersDao.class), true);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class), true);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class), true);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class), true);
			dispatcher.stop();
		}
	}
	
	private void player(PartiesSQLDispatcher dispatcher, PlayersDao dao, boolean remove) {
		PartyPlayerImpl player = initializePlayer(mockPlugin, UUID.randomUUID());
		PartyPlayerImpl mockPlayer = mock(PartyPlayerImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mockPlugin, mock.getArgument(0)));
		
		
		player.setAccessible(true);
		player.setMuted(true);
		player.setAccessible(false);
		assertEquals(dao.countAll(), 0);
		dispatcher.updatePlayer(player);
		assertEquals(dao.countAll(), 1);
		
		PartyPlayerImpl newPlayer = dispatcher.getPlayer(player.getPlayerUUID());
		
		assertEquals(player, newPlayer);
		
		// Player remove
		if (remove) {
			player.setAccessible(true);
			player.setMuted(false);
			player.setAccessible(false);
			dispatcher.updatePlayer(player);
			assertEquals(dao.countAll(), 0);
		}
	}
	
	@Test
	public void testParty() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class), true);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class), true);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), true);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), true);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class), true);
			dispatcher.stop();
		}
	}
	
	private void party(PartiesSQLDispatcher dispatcher, PartiesDao dao, boolean remove) {
		PartyImpl party = initializeParty(mockPlugin, UUID.randomUUID());
		PartyPlayerImpl player = initializePlayer(mockPlugin, UUID.randomUUID());
		
		PartyImpl mockParty = mock(PartyImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockParty).updateParty();
		PartyPlayerImpl mockPlayer = mock(PartyPlayerImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PartyManager mockPartyManager = mock(PartyManager.class);
		when(mockPlugin.getPartyManager()).thenReturn(mockPartyManager);
		when(mockPartyManager.initializeParty(any())).thenAnswer((mock) -> initializeParty(mockPlugin, mock.getArgument(0)));
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mockPlugin, mock.getArgument(0)));
		
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
		
		assertEquals(dao.countAll(), 0);
		dispatcher.updateParty(party);
		assertEquals(dao.countAll(), 1);
		
		assertEquals(party, dispatcher.getParty(party.getId()));
		assertEquals(party, dispatcher.getPartyByName(party.getName()));
		
		// Party remove
		if (remove) {
			dispatcher.removeParty(party);
			assertEquals(dao.countAll(), 0);
		}
	}
	
	@Test
	public void testExists() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class), false);
		exists(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class), false);
		exists(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), false);
			exists(dispatcher);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), false);
			exists(dispatcher);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class), false);
			exists(dispatcher);
			dispatcher.stop();
		}
	}
	
	private void exists(PartiesSQLDispatcher dispatcher) {
		assertTrue(dispatcher.existsParty("test"));
		assertFalse(dispatcher.existsParty("test2"));
	}
	
	@Test
	public void testListParties() {
		PartyManager mockPartyManager = mock(PartyManager.class);
		when(mockPlugin.getPartyManager()).thenReturn(mockPartyManager);
		when(mockPartyManager.initializeParty(any())).thenAnswer((mock) -> initializeParty(mockPlugin, mock.getArgument(0)));
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mockPlugin, mock.getArgument(0)));
		
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		PartiesDao daoParties = dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class);
		PlayersDao daoPlayers = dispatcher.getConnectionFactory().getJdbi().onDemand(H2PlayersDao.class);
		populateWithParties(dispatcher, daoParties, daoPlayers);
		listPartiesNumber(dispatcher);
		listPartiesByName(dispatcher);
		listPartiesByMembers(dispatcher);
		listPartiesByKills(dispatcher);
		listPartiesByExperience(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		daoParties = dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class);
		daoPlayers = dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePlayersDao.class);
		populateWithParties(dispatcher, daoParties, daoPlayers);
		listPartiesNumber(dispatcher);
		listPartiesByName(dispatcher);
		listPartiesByMembers(dispatcher);
		listPartiesByKills(dispatcher);
		listPartiesByExperience(dispatcher);
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			daoParties = dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class);
			daoPlayers = dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class);
			populateWithParties(dispatcher, daoParties, daoPlayers);
			listPartiesNumber(dispatcher);
			listPartiesByName(dispatcher);
			listPartiesByMembers(dispatcher);
			listPartiesByKills(dispatcher);
			listPartiesByExperience(dispatcher);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			daoParties = dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class);
			daoPlayers = dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class);
			populateWithParties(dispatcher, daoParties, daoPlayers);
			listPartiesNumber(dispatcher);
			listPartiesByName(dispatcher);
			listPartiesByMembers(dispatcher);
			listPartiesByKills(dispatcher);
			listPartiesByExperience(dispatcher);
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			daoParties = dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class);
			daoPlayers = dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class);
			populateWithParties(dispatcher, daoParties, daoPlayers);
			listPartiesNumber(dispatcher);
			listPartiesByName(dispatcher);
			listPartiesByMembers(dispatcher);
			listPartiesByKills(dispatcher);
			listPartiesByExperience(dispatcher);
			dispatcher.stop();
		}
	}
	
	private void listPartiesNumber(PartiesSQLDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test2");
		
		assertEquals(7, dispatcher.getListPartiesNumber());
		
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.emptyList();
		
		assertEquals(8, dispatcher.getListPartiesNumber());
	}
	
	private void listPartiesByName(PartiesSQLDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test2");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.NAME, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test1", list.get(0).getName());
		assertEquals("test3", list.get(1).getName());
		assertEquals("test8", list.get(6).getName());
		
		// Filter by id instead of name
		PartyImpl firstParty = list.get(0);
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Arrays.asList("test2", firstParty.getId().toString());
		list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.NAME, 100, 0));
		
		assertEquals(6, list.size());
		assertNotEquals(firstParty, list.get(0));
	}
	
	private void listPartiesByMembers(PartiesSQLDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test7");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.MEMBERS, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test8", list.get(0).getName());
		assertEquals("test6", list.get(1).getName());
		assertEquals("test1", list.get(6).getName());
	}
	
	private void listPartiesByKills(PartiesSQLDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test3");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.KILLS, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test4", list.get(0).getName());
		assertEquals("test2", list.get(1).getName());
		assertEquals("test5", list.get(6).getName());
	}
	
	private void listPartiesByExperience(PartiesSQLDispatcher dispatcher) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.singletonList("test6");
		List<PartyImpl> list = new LinkedList<>(dispatcher.getListParties(PartiesDatabaseManager.ListOrder.EXPERIENCE, 100, 0));
		
		assertEquals(7, list.size());
		assertEquals("test7", list.get(0).getName());
		assertEquals("test5", list.get(1).getName());
		assertEquals("test8", list.get(6).getName());
	}
	
	@Test
	public void testCountPlayersInParty() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PlayersDao.class), false);
		countPlayersInParty(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PlayersDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePlayersDao.class), false);
		countPlayersInParty(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePlayersDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class), false);
			countPlayersInParty(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class), false);
			countPlayersInParty(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PlayersDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			player(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class), false);
			countPlayersInParty(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPlayersDao.class));
			dispatcher.stop();
		}
	}
	
	private void countPlayersInParty(PartiesSQLDispatcher dispatcher, PlayersDao dao) {
		PartyPlayerImpl player = initializePlayer(mockPlugin, UUID.randomUUID());
		player.setAccessible(true);
		player.setPartyId(UUID.randomUUID());
		player.setAccessible(false);
		
		assertEquals(dao.countAll(), 1);
		assertEquals(dispatcher.getListPlayersInPartyNumber(), 0);
		
		dispatcher.updatePlayer(player);
		
		assertEquals(dao.countAll(), 2);
		assertEquals(dispatcher.getListPlayersInPartyNumber(), 1);
	}
	
	@Test
	public void testCountParties() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class), false);
		countParties(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class), false);
		countParties(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), false);
			countParties(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), false);
			countParties(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class), false);
			countParties(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class));
			dispatcher.stop();
		}
	}
	
	private void countParties(PartiesSQLDispatcher dispatcher, PartiesDao dao) {
		ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES = Collections.emptyList();
		
		PartyImpl party = initializeParty(mockPlugin, UUID.randomUUID());
		
		party.setAccessible(true);
		party.setup("test3", null);
		party.setDescription("description");
		party.setAccessible(false);
		
		assertEquals(dao.countAll(), 1);
		assertEquals(dispatcher.getListPartiesNumber(), 1);
		
		dispatcher.updateParty(party);
		
		assertEquals(dao.countAll(), 2);
		assertEquals(dispatcher.getListPartiesNumber(), 2);
	}
	
	@Test
	public void testListFixed() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class), false);
		listFixed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherSQLite();
		party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class), false);
		listFixed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(SQLitePartiesDao.class));
		dispatcher.stop();
		
		dispatcher = getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), false);
			listFixed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class), false);
			listFixed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PartiesDao.class));
			dispatcher.stop();
		}
		
		dispatcher = getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			party(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class), false);
			listFixed(dispatcher, dispatcher.getConnectionFactory().getJdbi().onDemand(PostgreSQLPartiesDao.class));
			dispatcher.stop();
		}
	}
	
	private void listFixed(PartiesSQLDispatcher dispatcher, PartiesDao dao) {
		PartyImpl party = initializeParty(mockPlugin, UUID.randomUUID());
		
		party.setAccessible(true);
		party.setup("test3", null);
		party.setDescription("description");
		party.setAccessible(false);
		
		assertEquals(dao.countAll(), 1);
		dispatcher.updateParty(party);
		assertEquals(dao.countAll(), 2);
		
		assertEquals(party, dispatcher.getParty(party.getId()));
		assertEquals(party, dispatcher.getPartyByName(party.getName()));
		
		Set<PartyImpl> list = dispatcher.getListFixed();
		
		assertEquals(1, list.size());
		assertEquals(party, list.iterator().next());
	}
	
	private void populateWithParties(PartiesSQLDispatcher dispatcher, PartiesDao daoParties, PlayersDao daoPlayers) {
		assertEquals(daoParties.countAll(), 0);
		assertEquals(daoPlayers.countAll(), 0);
		
		insertOneParty(dispatcher, "test1", 1, 170, 200);
		insertOneParty(dispatcher, "test2", 2, 180, 300);
		insertOneParty(dispatcher, "test3", 3, 190, 400);
		insertOneParty(dispatcher, "test4", 4, 200, 500);
		insertOneParty(dispatcher, "test5", 5, 130, 600);
		insertOneParty(dispatcher, "test6", 6, 140, 700);
		insertOneParty(dispatcher, "test7", 7, 150, 800);
		insertOneParty(dispatcher, "test8", 8, 160, 100);
		
		assertEquals(daoParties.countAll(), 8);
		assertEquals(daoPlayers.countAll(), 36);
	}
	
	private void insertOneParty(PartiesSQLDispatcher dispatcher, String partyName, int numberOfPlayers, int kills, double experience) {
		PartyImpl mockParty = mock(PartyImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockParty).updateParty();
		PartyPlayerImpl mockPlayer = mock(PartyPlayerImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PartyImpl party = initializeParty(mockPlugin, UUID.randomUUID());
		Set<UUID> members = new HashSet<>();
		for (int c=0; c < numberOfPlayers; c++) {
			PartyPlayerImpl player = initializePlayer(mockPlugin, UUID.randomUUID());
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
	
	@Test
	public void testMultipleOperations() {
		PartiesSQLDispatcher dispatcher = getSQLDispatcherH2();
		PartiesDao dao = dispatcher.getConnectionFactory().getJdbi().onDemand(H2PartiesDao.class);
		ArrayList<CompletableFuture<?>> lst = new ArrayList<CompletableFuture<?>>();
		final int concurrentOperations = 20;
		
		PartyImpl mockParty = mock(PartyImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockParty).updateParty();
		PartyPlayerImpl mockPlayer = mock(PartyPlayerImpl.class);
		doReturn(CompletableFuture.completedFuture(null)).when(mockPlayer).updatePlayer();
		
		PartyManager mockPartyManager = mock(PartyManager.class);
		when(mockPlugin.getPartyManager()).thenReturn(mockPartyManager);
		when(mockPartyManager.initializeParty(any())).thenAnswer((mock) -> initializeParty(mockPlugin, mock.getArgument(0)));
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> initializePlayer(mockPlugin, mock.getArgument(0)));
		
		for (int c=0; c < concurrentOperations; c++) {
			int finalC = c;
			lst.add(CompletableFuture.runAsync(() -> {
				PartyImpl party = initializeParty(mockPlugin, UUID.randomUUID());
				PartyPlayerImpl player = initializePlayer(mockPlugin, UUID.randomUUID());
				
				party.setAccessible(true);
				party.setup("test-" + finalC, player.getPlayerUUID().toString());
				party.setDescription("description");
				party.setKills(10);
				party.setMembers(Collections.singleton(player.getPlayerUUID()));
				party.setAccessible(false);
				
				
				player.setAccessible(true);
				player.setPartyId(party.getId());
				player.setAccessible(false);
				dispatcher.updatePlayer(player);
				
				dispatcher.updateParty(party);
				
				Party sameParty = dispatcher.getParty(party.getId());
				assertEquals(sameParty, party);
			}));
		}
		
		lst.forEach(CompletableFuture::join);
		assertEquals(concurrentOperations, dao.countAll());
	}
	
	public static PartyPlayerImpl initializePlayer(PartiesPlugin mockPlugin, UUID uuid) {
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
	
	public static PartyImpl initializeParty(PartiesPlugin mockPlugin, UUID id) {
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
