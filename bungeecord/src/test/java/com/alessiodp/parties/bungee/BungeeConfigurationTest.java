package com.alessiodp.parties.bungee;

import com.alessiodp.core.bungeecord.configuration.adapter.BungeeConfigurationAdapter;
import com.alessiodp.core.bungeecord.configuration.adapter.BungeeConfigurationSectionAdapter;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.common.PartiesPlugin;
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
		BungeeConfigurationAdapter.class,
		BungeeConfigurationSectionAdapter.class,
		BungeeConfigMain.class,
		BungeeConfigParties.class,
		BungeeMessages.class
})
public class BungeeConfigurationTest {
	private PartiesPlugin mockPlugin;
	private final Pattern pattern = Pattern.compile("[A-Z_]+");
	
	@Before
	public void setUp() {
		mockPlugin = mock(PartiesPlugin.class);
	}
	
	@Test
	public void testConfigMain() throws URISyntaxException {
		BungeeConfigMain configMain = new BungeeConfigMain(mockPlugin);
		
		testConfiguration(configMain);
	}
	
	@Test
	public void testConfigParties() throws URISyntaxException {
		BungeeConfigParties configParties = new BungeeConfigParties(mockPlugin);
		
		testConfiguration(configParties);
	}
	
	@Test
	public void testMessages() throws URISyntaxException {
		BungeeMessages messages = new BungeeMessages(mockPlugin);
		
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
		ConfigurationAdapter configurationAdapter = new BungeeConfigurationAdapter(path);
		
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
