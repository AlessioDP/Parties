package com.alessiodp.parties.bukkit;

import com.alessiodp.core.bukkit.configuration.adapter.BukkitConfigurationAdapter;
import com.alessiodp.core.bukkit.configuration.adapter.BukkitConfigurationSectionAdapter;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		PartiesPlugin.class,
		BukkitConfigurationAdapter.class,
		BukkitConfigurationSectionAdapter.class,
		ConfigurationSection.class,
		BukkitConfigMain.class,
		BukkitConfigParties.class,
		BukkitMessages.class
})
public class BukkitConfigurationTest {
	private PartiesPlugin mockPlugin;
	private final Pattern pattern = Pattern.compile("[A-Z_]+");
	
	@Before
	public void setUp() {
		mockPlugin = mock(PartiesPlugin.class);
	}
	
	@Test
	public void testConfigMain() throws URISyntaxException {
		BukkitConfigMain configMain = new BukkitConfigMain(mockPlugin);
		
		testConfiguration(configMain);
	}
	
	@Test
	public void testConfigParties() throws URISyntaxException {
		BukkitConfigParties configParties = new BukkitConfigParties(mockPlugin);
		
		testConfiguration(configParties);
	}
	
	@Test
	public void testMessages() throws URISyntaxException {
		BukkitMessages messages = new BukkitMessages(mockPlugin);
		
		testConfiguration(messages);
	}
	
	private void testConfiguration(ConfigurationFile configurationFile) throws URISyntaxException {
		Field[] fields = PowerMockito.fields(configurationFile.getClass());
		
		// Load defaults
		configurationFile.loadDefaults();
		
		// Save default values
		HashMap<String, Object> savedMap = populateMap(fields, configurationFile);
		
		// Get config file
		Path path = Paths.get(getClass().getResource("/" + configurationFile.getResourceName()).toURI());
		Assert.assertNotNull(path);
		
		// Initialize configuration
		ConfigurationAdapter configurationAdapter = new BukkitConfigurationAdapter(path);
		
		// Load configuration
		configurationFile.loadConfiguration(configurationAdapter);
		
		// Match configuration
		match(fields, savedMap, configurationFile);
	}
	
	private HashMap<String, Object> populateMap(Field[] fields, ConfigurationFile configurationFile) {
		HashMap<String, Object> ret = new HashMap<>();
		for (Field f : fields) {
			if (pattern.matcher(f.getName()).matches()) {
				try {
					ret.put(f.getName(), f.get(configurationFile));
				} catch (Exception ex) {
					ex.printStackTrace();
					fail(ex.getMessage());
				}
			}
		}
		return ret;
	}
	
	private void match(Field[] fields, HashMap<String, Object> savedMap, ConfigurationFile configurationFile) {
		for (Field f : fields) {
			try {
				if (savedMap.containsKey(f.getName()) && !savedMap.get(f.getName()).equals(f.get(configurationFile))) {
					fail("Fields are mismatched: " + f.getName() + "\n" + savedMap.get(f.getName()) + " != " + f.get(configurationFile));
				}
			} catch (Exception ex) {
				fail("Error at field " + f.getName());
				ex.printStackTrace();
			}
		}
	}
}
