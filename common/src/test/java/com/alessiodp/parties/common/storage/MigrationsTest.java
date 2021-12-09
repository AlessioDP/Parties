package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.ColorManager;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.PartiesYAMLDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MigrationsTest {
	private static final PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
	private final Path testingPath = Paths.get("../testing/");
	private static MockedStatic<ADPPlugin> staticPlugin;
	
	@BeforeAll
	public static void setUp(@TempDir Path tempDir) {
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getPluginFallbackName()).thenReturn("parties");
		when(mockPlugin.getFolder()).thenReturn(tempDir);
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> ClassLoader.getSystemResourceAsStream(mock.getArgument(0)));
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		
		// Mock managers for player/party initialization
		ColorManager mockColorManager = mock(ColorManager.class);
		when(mockPlugin.getColorManager()).thenReturn(mockColorManager);
		when(mockColorManager.searchColorByName(anyString())).thenReturn(null);
		
		PlayerManager mockPlayerManager = mock(PlayerManager.class);
		when(mockPlugin.getPlayerManager()).thenReturn(mockPlayerManager);
		when(mockPlayerManager.initializePlayer(any())).thenAnswer((mock) -> SQLDispatcherTest.initializePlayer(mockPlugin, mock.getArgument(0)));
		
		PartyManager mockPartyManager = mock(PartyManager.class);
		when(mockPlugin.getPartyManager()).thenReturn(mockPartyManager);
		when(mockPartyManager.initializeParty(any())).thenAnswer((mock) -> SQLDispatcherTest.initializeParty(mockPlugin, mock.getArgument(0)));
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
		
		staticPlugin = Mockito.mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
	}
	
	@AfterAll
	public static void tearDown() {
		staticPlugin.close();
	}
	
	private void prepareDatabase(String database) throws IOException {
		Files.copy(
				testingPath.resolve(database),
				mockPlugin.getFolder().resolve(database),
				StandardCopyOption.REPLACE_EXISTING
		);
	}
	
	@Test
	public void testDatabase2_6_X() throws IOException {
		// YAML
		ConfigMain.STORAGE_SETTINGS_YAML_DBFILE = "database_2_6_X.yml";
		PartiesYAMLDispatcher fileDispatcher = new PartiesYAMLDispatcher(mockPlugin);
		prepareDatabase(ConfigMain.STORAGE_SETTINGS_YAML_DBFILE);
		database2_6_X_YAML(fileDispatcher);
		
		// SQLite
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_6_X.db";
		prepareDatabase(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
		PartiesSQLDispatcher dispatcher = new PartiesSQLDispatcher(mockPlugin, StorageType.SQLITE);
		database2_6_X_SQL(dispatcher);
	}
	
	private void database2_6_X_YAML(PartiesYAMLDispatcher dispatcher) {
		dispatcher.init();
		
		PartyImpl party = dispatcher.getPartyByName("test");
		assertNotNull(party);
		assertEquals("test description", party.getDescription());
		assertEquals(2, party.getMembers().size());
		
		// Leader check
		assertNotNull(party.getLeader());
		PartyPlayerImpl leader = dispatcher.getPlayer(party.getLeader());
		assertNotNull(leader);
		assertEquals(party.getId(), leader.getPartyId());
		
		// Home check
		assertEquals(1, party.getHomes().size());
		assertEquals("default", party.getHomes().stream().findFirst().get().getName());
		
		// Another party
		assertNotNull(dispatcher.getPartyByName("test2"));
		
		dispatcher.stop();
	}
	
	private void database2_6_X_SQL(PartiesSQLDispatcher dispatcher) {
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>parties");
			handle.execute("SELECT 1 FROM <prefix>players");
		});
		
		PartyImpl party = dispatcher.getPartyByName("test");
		assertNotNull(party);
		assertEquals("test description", party.getDescription());
		assertEquals(2, party.getMembers().size());
		
		// Leader check
		assertNotNull(party.getLeader());
		PartyPlayerImpl leader = dispatcher.getPlayer(party.getLeader());
		assertNotNull(leader);
		assertEquals(party.getId(), leader.getPartyId());
		
		// Home check
		assertEquals(1, party.getHomes().size());
		assertEquals("default", party.getHomes().stream().findFirst().get().getName());
		
		// Another party
		assertNotNull(dispatcher.getPartyByName("test2"));
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabaseFreshSQLite() {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_sqlite.db";
		
		PartiesSQLDispatcher dispatcher = new PartiesSQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>parties");
			handle.execute("SELECT 1 FROM <prefix>players");
		});
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabaseFreshH2() {
		ConfigMain.STORAGE_SETTINGS_H2_DBFILE = "database_h2.db";
		
		PartiesSQLDispatcher dispatcher = new PartiesSQLDispatcher(mockPlugin, StorageType.H2);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>parties");
			handle.execute("SELECT 1 FROM <prefix>players");
		});
		
		dispatcher.stop();
	}
	
	@Test
	public void testDatabaseFreshMySQL() {
		PartiesSQLDispatcher dispatcher = SQLDispatcherTest.getSQLDispatcherMySQL(mockPlugin);
		if (dispatcher != null) {
			dispatcher.init();
			
			dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
				handle.execute("SELECT 1 FROM <prefix>parties");
				handle.execute("SELECT 1 FROM <prefix>players");
			});
			
			dispatcher.stop();
		}
	}
	
	@Test
	public void testDatabaseFreshMariaDB() {
		PartiesSQLDispatcher dispatcher = SQLDispatcherTest.getSQLDispatcherMariaDB(mockPlugin);
		if (dispatcher != null) {
			dispatcher.init();
			
			dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
				handle.execute("SELECT 1 FROM <prefix>parties");
				handle.execute("SELECT 1 FROM <prefix>players");
			});
			
			dispatcher.stop();
		}
	}
	
	@Test
	public void testDatabaseFreshPostgreSQL() {
		PartiesSQLDispatcher dispatcher = SQLDispatcherTest.getSQLDispatcherPostgreSQL(mockPlugin);
		if (dispatcher != null) {
			dispatcher.init();
			
			dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
				handle.execute("SELECT 1 FROM <prefix>parties");
				handle.execute("SELECT 1 FROM <prefix>players");
			});
			
			dispatcher.stop();
		}
	}
}