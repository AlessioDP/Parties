package com.alessiodp.parties.bukkit.configuration;

import com.alessiodp.core.common.addons.external.simpleyaml.configuration.file.YamlFile;
import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.core.common.configuration.ConfigurationFile;
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

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		PartiesPlugin.class,
		ConfigurationSection.class,
		BukkitConfigMain.class,
		BukkitConfigParties.class,
		BukkitMessages.class
})
public class BukkitConfigurationTest {
	private PartiesPlugin mockPlugin;
	
	@Before
	public void setUp() {
		mockPlugin = mock(PartiesPlugin.class);
	}
	
	@Test
	public void testConfigMain() throws IllegalAccessException {
		BukkitConfigMain configMain = new BukkitConfigMain(mockPlugin);
		
		List<String> skipPaths = Collections.emptyList();
		
		testConfiguration(configMain, skipPaths);
	}
	
	@Test
	public void testConfigParties() throws IllegalAccessException {
		BukkitConfigParties configParties = new BukkitConfigParties(mockPlugin);
		
		List<String> skipPaths = Collections.emptyList();
		
		testConfiguration(configParties, skipPaths);
	}
	
	@Test
	public void testMessages() throws IllegalAccessException {
		BukkitMessages messages = new BukkitMessages(mockPlugin);
		
		List<String> skipPaths = Collections.emptyList();
		
		testConfiguration(messages, skipPaths);
	}
	
	private void testConfiguration(ConfigurationFile configurationFile, List<String> skipPaths) throws IllegalAccessException {
		Field[] fields = PowerMockito.fields(configurationFile.getClass());
		
		// Initialize YAML
		YamlFile yf = YamlFile.loadConfiguration(new InputStreamReader(getClass().getResourceAsStream("/" + configurationFile.getResourceName())));
		
		// Check fields
		for (Field f : fields) {
			ConfigOption co = f.getAnnotation(ConfigOption.class);
			if (co != null && !skippablePath(co.path(), skipPaths)) {
				Object value = yf.get(co.path());
				Assert.assertNotNull("The " + configurationFile.getClass().getSimpleName() + " path '" + co.path() + "' is null.", value);
				f.set(null, value);
			}
		}
	}
	
	private boolean skippablePath(String path, List<String> skipPaths) {
		for (String sp : skipPaths) {
			if (path.startsWith(sp))
				return true;
		}
		return false;
	}
}
