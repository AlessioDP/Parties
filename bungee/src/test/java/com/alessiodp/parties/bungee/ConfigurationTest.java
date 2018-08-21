package com.alessiodp.parties.bungee;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.configuration.adapter.BungeeConfigurationAdapter;
import com.alessiodp.parties.bungeecord.configuration.adapter.BungeeConfigurationSectionAdapter;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		BungeePartiesPlugin.class,
		BungeeConfigurationAdapter.class,
		BungeeConfigurationSectionAdapter.class,
		BungeeConfigMain.class,
		BungeeConfigParties.class,
		BungeeMessages.class
})
public class ConfigurationTest {
	@Test
	public void testOnConfigMainLoadMatch() throws Exception {
		mockStatic(BungeeConfigMain.class);
		BungeePartiesPlugin mockPlugin = mock(BungeePartiesPlugin.class);
		BungeeConfigMain configMainInstance = new BungeeConfigMain(mockPlugin);
		configMainInstance.loadDefaults();
		
		HashMap<String, Object> savedEntries = new HashMap<>();
		
		// Save all default fields into the hashmap
		Field[] listFields = PowerMockito.fields(configMainInstance.getClass());
		for (Field f : listFields) {
			if (!f.getType().equals(PartiesPlugin.class))
				savedEntries.put(f.getName(), f.get(configMainInstance));
		}
		
		// Prepare config
		File configFile = new File(getClass().getResource("/bungee/config.yml").toURI());
		
		// Prepare ConfigurationAdapter
		ConfigurationAdapter configMainCA = new BungeeConfigurationAdapter(configFile.toPath());
		
		// Load configuration
		configMainInstance.loadConfiguration(configMainCA);
		
		// Check if configuration matches
		for (Field f : listFields) {
			try {
				if (savedEntries.containsKey(f.getName()) && !savedEntries.get(f.getName()).equals(f.get(configMainInstance))) {
					fail("Fields are mismatched: " + f.getName());
				}
			} catch (Exception ex) {
				fail("Error at field " + f.getName());
				ex.printStackTrace();
			}
		}
	}
	
	@Test
	public void testOnConfigPartiesLoadMatch() throws Exception {
		mockStatic(BungeeConfigParties.class);
		BungeePartiesPlugin mockPlugin = mock(BungeePartiesPlugin.class);
		BungeeConfigParties configPartiesInstance = new BungeeConfigParties(mockPlugin);
		configPartiesInstance.loadDefaults();
		
		HashMap<String, Object> savedEntries = new HashMap<>();
		
		// Save all default fields into the hashmap
		Field[] listFields = PowerMockito.fields(configPartiesInstance.getClass());
		for (Field f : listFields) {
			if (!f.getType().equals(PartiesPlugin.class))
				savedEntries.put(f.getName(), f.get(configPartiesInstance));
		}
		
		// Prepare config
		File partiesFile = new File(getClass().getResource("/bungee/parties.yml").toURI());
		
		// Prepare ConfigurationAdapter
		ConfigurationAdapter configPartiesCA = new BungeeConfigurationAdapter(partiesFile.toPath());
		
		// Load configuration
		configPartiesInstance.loadConfiguration(configPartiesCA);
		
		// Check if configuration matches
		for (Field f : listFields)
			try {
				if (savedEntries.containsKey(f.getName()) && !savedEntries.get(f.getName()).equals(f.get(configPartiesInstance))) {
					fail("Fields are mismatched: " + f.getName());
				}
			} catch (Exception ex) {
				fail("Error at field " + f.getName());
				ex.printStackTrace();
			}
	}
	
	@Test
	public void testOnMessagesLoadMatch() throws Exception {
		mockStatic(BungeeMessages.class);
		BungeePartiesPlugin mockPlugin = mock(BungeePartiesPlugin.class);
		BungeeMessages messagesInstance = new BungeeMessages(mockPlugin);
		messagesInstance.loadDefaults();
		
		HashMap<String, Object> savedEntries = new HashMap<>();
		
		// Save all default fields into the hashmap
		Field[] listFields = PowerMockito.fields(messagesInstance.getClass());
		for (Field f : listFields) {
			if (!f.getType().equals(PartiesPlugin.class))
				savedEntries.put(f.getName(), f.get(messagesInstance));
		}
		
		// Prepare config
		File messagesFile = new File(getClass().getResource("/bungee/messages.yml").toURI());
		
		// Prepare ConfigurationAdapter
		ConfigurationAdapter messagesCA = new BungeeConfigurationAdapter(messagesFile.toPath());
		
		// Load configuration
		messagesInstance.loadConfiguration(messagesCA);
		
		// Check if configuration matches
		for (Field f : listFields) {
			try {
				if (savedEntries.containsKey(f.getName()) && !savedEntries.get(f.getName()).equals(f.get(messagesInstance))) {
					fail("Fields are mismatched: " + f.getName());
				}
			} catch (Exception ex) {
				fail("Error at field " + f.getName());
				ex.printStackTrace();
			}
		}
	}
}
