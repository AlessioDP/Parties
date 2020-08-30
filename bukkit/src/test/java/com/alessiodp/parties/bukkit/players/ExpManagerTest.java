package com.alessiodp.parties.bukkit.players;

import com.alessiodp.core.common.utils.FormulaUtils;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.ExpResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.script.*", "com.sun.script.*"})
@PrepareForTest({
		BukkitConfigMain.class,
		ExpManager.class,
		FormulaUtils.class,
		PartiesPlugin.class
})
public class ExpManagerTest {
	private ExpManager expManager;
	
	@Before
	public void setUp() {
		PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
		
		BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE = true;
		BukkitConfigMain.ADDITIONAL_EXP_LEVELS_MODE = "progressive";
		BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_RANGE = 30;
		BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_NORMAL = "party";
		BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI = "party";
		
		expManager = new ExpManager(mockPlugin);
	}
	
	@Test
	public void testCalculateLevelProgressive() throws Exception {
		BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START = 100;
		BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP = "%previous%*2";
		
		ExpResult expResult = expManager.calculateLevel(0);
		assertEquals(1, expResult.getLevel());
		assertEquals(100, expResult.getLevelExperience());
		assertEquals(0, expResult.getCurrentExperience());
		assertEquals(100, expResult.getNecessaryExperience());
		
		expResult = expManager.calculateLevel(100);
		assertEquals(2, expResult.getLevel());
		assertEquals(200, expResult.getLevelExperience());
		assertEquals(0, expResult.getCurrentExperience());
		assertEquals(200, expResult.getNecessaryExperience());
		
		expResult = expManager.calculateLevel(250);
		assertEquals(2, expResult.getLevel());
		assertEquals(200, expResult.getLevelExperience());
		assertEquals(150, expResult.getCurrentExperience());
		assertEquals(50, expResult.getNecessaryExperience());
		
		expResult = expManager.calculateLevel(500);
		assertEquals(3, expResult.getLevel());
		assertEquals(400, expResult.getLevelExperience());
		assertEquals(200, expResult.getCurrentExperience());
		assertEquals(200, expResult.getNecessaryExperience());
	}
	
	@Test
	public void testCalculateLevelFixed() throws Exception {
		BukkitConfigMain.ADDITIONAL_EXP_LEVELS_MODE = "fixed";
		expManager.reload();
		BukkitConfigMain.ADDITIONAL_EXP_LEVELS_FIXED = Arrays.asList(
				100D,
				200D,
				300D,
				400D
		);
		
		ExpResult expResult = expManager.calculateLevel(0);
		assertEquals(1, expResult.getLevel());
		assertEquals(100, expResult.getLevelExperience());
		assertEquals(0, expResult.getCurrentExperience());
		assertEquals(100, expResult.getNecessaryExperience());
		
		expResult = expManager.calculateLevel(100);
		assertEquals(2, expResult.getLevel());
		assertEquals(200, expResult.getLevelExperience());
		assertEquals(0, expResult.getCurrentExperience());
		assertEquals(200, expResult.getNecessaryExperience());
		
		expResult = expManager.calculateLevel(250);
		assertEquals(2, expResult.getLevel());
		assertEquals(200, expResult.getLevelExperience());
		assertEquals(150, expResult.getCurrentExperience());
		assertEquals(50, expResult.getNecessaryExperience());
		
		expResult = expManager.calculateLevel(500);
		assertEquals(3, expResult.getLevel());
		assertEquals(300, expResult.getLevelExperience());
		assertEquals(200, expResult.getCurrentExperience());
		assertEquals(100, expResult.getNecessaryExperience());
	}
}
