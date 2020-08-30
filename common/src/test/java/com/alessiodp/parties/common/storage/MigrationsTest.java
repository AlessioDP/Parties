package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.ADPLibraryManager;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPPlugin.class,
		ADPBootstrap.class,
		ConfigMain.class,
		LoggerManager.class,
		PartiesSQLDispatcher.class,
		PartiesPlugin.class
})
public class MigrationsTest {
	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();
	
	private PartiesPlugin mockPlugin;
	private final Path testingPath = Paths.get("../testing/");
	
	@Before
	public void setUp() throws IOException {
		mockPlugin = mock(PartiesPlugin.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getPluginFallbackName()).thenReturn("parties");
		when(mockPlugin.getFolder()).thenReturn(testFolder.newFolder().toPath());
		when(mockPlugin.getResource(anyString())).thenAnswer((mock) -> getClass().getClassLoader().getResourceAsStream(mock.getArgument(0)));
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		
		// Mock class loaders
		ADPLibraryManager mockLibraryManager = mock(ADPLibraryManager.class);
		when(mockLibraryManager.getIsolatedClassLoaderOf(any())).thenReturn(getClass().getClassLoader());
		when(mockPlugin.getLibraryManager()).thenReturn(mockLibraryManager);
		
		ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_PREFIX = "test_";
	}
	
	private void prepareDatabase(String database) throws IOException {
		Files.copy(
				testingPath.resolve(database),
				mockPlugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE),
				StandardCopyOption.REPLACE_EXISTING
		);
	}
	
	@Test
	public void testDatabase2_6_X() throws IOException {
		ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE = "database_2_6_X.db";
		prepareDatabase(ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
		
		PartiesSQLDispatcher dispatcher = new PartiesSQLDispatcher(mockPlugin, StorageType.SQLITE);
		dispatcher.init();
		
		dispatcher.getConnectionFactory().getJdbi().useHandle(handle -> {
			handle.execute("SELECT 1 FROM <prefix>parties");
			handle.execute("SELECT 1 FROM <prefix>players");
		});
		
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
}