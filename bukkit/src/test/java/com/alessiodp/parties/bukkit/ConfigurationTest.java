package com.alessiodp.parties.bukkit;

import com.alessiodp.parties.bukkit.configuration.adapter.BukkitConfigurationAdapter;
import com.alessiodp.parties.bukkit.configuration.adapter.BukkitConfigurationSectionAdapter;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import org.bukkit.configuration.ConfigurationSection;
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
		BukkitPartiesPlugin.class,
		BukkitConfigurationAdapter.class,
		BukkitConfigurationSectionAdapter.class,
		ConfigurationSection.class,
		BukkitConfigMain.class,
		BukkitConfigParties.class,
		BukkitMessages.class
})
public class ConfigurationTest {
	@Test
	public void testOnConfigMainLoadMatch() throws Exception {
		mockStatic(BukkitConfigMain.class);
		BukkitPartiesPlugin mockPlugin = mock(BukkitPartiesPlugin.class);
		BukkitConfigMain configMainInstance = new BukkitConfigMain(mockPlugin);
		configMainInstance.loadDefaults();
		
		HashMap<String, Object> savedEntries = new HashMap<>();
		
		// Save all default fields into the hashmap
		Field[] listFields = PowerMockito.fields(configMainInstance.getClass());
		for (Field f : listFields) {
			if (!f.getType().equals(PartiesPlugin.class))
				savedEntries.put(f.getName(), f.get(configMainInstance));
		}
		
		// Prepare config
		File configFile = new File(getClass().getResource("/bukkit/config.yml").toURI());
		
		// Prepare ConfigurationAdapter
		ConfigurationAdapter configMainCA = new BukkitConfigurationAdapter(configFile.toPath());
		
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
		mockStatic(BukkitConfigParties.class);
		BukkitPartiesPlugin mockPlugin = mock(BukkitPartiesPlugin.class);
		BukkitConfigParties configPartiesInstance = new BukkitConfigParties(mockPlugin);
		configPartiesInstance.loadDefaults();
		
		HashMap<String, Object> savedEntries = new HashMap<>();
		
		// Save all default fields into the hashmap
		Field[] listFields = PowerMockito.fields(configPartiesInstance.getClass());
		for (Field f : listFields) {
			if (!f.getType().equals(PartiesPlugin.class))
				savedEntries.put(f.getName(), f.get(configPartiesInstance));
		}
		
		// Prepare config
		File partiesFile = new File(getClass().getResource("/bukkit/parties.yml").toURI());
		
		// Prepare ConfigurationAdapter
		ConfigurationAdapter configPartiesCA = new BukkitConfigurationAdapter(partiesFile.toPath());
		
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
		mockStatic(BukkitMessages.class);
		BukkitPartiesPlugin mockPlugin = mock(BukkitPartiesPlugin.class);
		BukkitMessages messagesInstance = new BukkitMessages(mockPlugin);
		messagesInstance.loadDefaults();
		
		HashMap<String, Object> savedEntries = new HashMap<>();
		
		// Save all default fields into the hashmap
		Field[] listFields = PowerMockito.fields(messagesInstance.getClass());
		for (Field f : listFields) {
			if (!f.getType().equals(PartiesPlugin.class))
				savedEntries.put(f.getName(), f.get(messagesInstance));
		}
		
		// Prepare config
		File messagesFile = new File(getClass().getResource("/bukkit/messages.yml").toURI());
		
		// Prepare ConfigurationAdapter
		ConfigurationAdapter messagesCA = new BukkitConfigurationAdapter(messagesFile.toPath());
		
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
