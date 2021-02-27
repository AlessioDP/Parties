package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.utils.FormulaUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
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
		ConfigMain.class,
		ExpManager.class,
		FormulaUtils.class,
		PartiesPlugin.class
})
public class ExpManagerTest {
	private boolean runnable = false;
	private ExpManager expManager;
	
	@Before
	public void setUp() {
		// Java 15 we use JDK Nashorn module but cannot be loaded in earlier versions
		int version = Integer.parseInt(System.getProperties().getProperty("java.version").split("\\.")[0]);
		if (version < 15)
			runnable = true;
		
		if (runnable) {
			PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
			
			ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE = true;
			ConfigMain.ADDITIONAL_EXP_LEVELS_MODE = "progressive";
			
			expManager = new ExpManager(mockPlugin);
			expManager.reload();
		}
	}
	
	@Test
	public void testCalculateLevelProgressive() throws Exception {
		if (runnable) {
			ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START = 100;
			ConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP = "%previous%*2";
			
			ExpResult expResult = expManager.calculateLevel(0);
			assertEquals(1, expResult.getLevel());
			assertEquals(100, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(100, expResult.getLevelUpNecessary(), 0);
			
			expResult = expManager.calculateLevel(100);
			assertEquals(2, expResult.getLevel());
			assertEquals(200.0, expResult.getLevelExperience(), 0);
			assertEquals(0.0, expResult.getLevelUpCurrent(), 0);
			assertEquals(200.0, expResult.getLevelUpNecessary(), 0);
			
			expResult = expManager.calculateLevel(250);
			assertEquals(2, expResult.getLevel());
			assertEquals(200, expResult.getLevelExperience(), 0);
			assertEquals(150, expResult.getLevelUpCurrent(), 0);
			assertEquals(50, expResult.getLevelUpNecessary(), 0);
			
			expResult = expManager.calculateLevel(500);
			assertEquals(3, expResult.getLevel());
			assertEquals(400, expResult.getLevelExperience(), 0);
			assertEquals(200, expResult.getLevelUpCurrent(), 0);
			assertEquals(200, expResult.getLevelUpNecessary(), 0);
		}
	}
	
	@Test
	public void testCalculateLevelFixedNoRepeat() throws Exception {
		if (runnable) {
			ConfigMain.ADDITIONAL_EXP_LEVELS_MODE = "fixed";
			ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_REPEAT = false;
			expManager.reload();
			ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST = Arrays.asList(
					100D,
					200D,
					300D,
					400D
			);
			
			// Current exp: 0
			ExpResult expResult = expManager.calculateLevel(0);
			assertEquals(1, expResult.getLevel());
			assertEquals(100, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(100, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 0
			expResult = expManager.calculateLevel(100);
			assertEquals(2, expResult.getLevel());
			assertEquals(200, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(200, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 50
			expResult = expManager.calculateLevel(150);
			assertEquals(2, expResult.getLevel());
			assertEquals(200, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(150, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 0
			expResult = expManager.calculateLevel(300);
			assertEquals(3, expResult.getLevel());
			assertEquals(300, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(300, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 50
			expResult = expManager.calculateLevel(650);
			assertEquals(4, expResult.getLevel());
			assertEquals(400, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(350, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 400 + 50
			expResult = expManager.calculateLevel(1050);
			assertEquals(5, expResult.getLevel());
			assertEquals(0, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(0, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 400 + 400
			expResult = expManager.calculateLevel(1400);
			assertEquals(5, expResult.getLevel());
			assertEquals(0, expResult.getLevelExperience(), 0);
			assertEquals(400, expResult.getLevelUpCurrent(), 0);
			assertEquals(0, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 400 + 400 + 50
			expResult = expManager.calculateLevel(1450);
			assertEquals(5, expResult.getLevel());
			assertEquals(0, expResult.getLevelExperience(), 0);
			assertEquals(450, expResult.getLevelUpCurrent(), 0);
			assertEquals(0, expResult.getLevelUpNecessary(), 0);
		}
	}
	
	@Test
	public void testCalculateLevelFixedWithRepeat() throws Exception {
		if (runnable) {
			ConfigMain.ADDITIONAL_EXP_LEVELS_MODE = "fixed";
			ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_REPEAT = true;
			expManager.reload();
			ConfigMain.ADDITIONAL_EXP_LEVELS_FIXED_LIST = Arrays.asList(
					100D,
					200D,
					300D,
					400D
			);
			
			// Current exp: 0
			ExpResult expResult = expManager.calculateLevel(0);
			assertEquals(1, expResult.getLevel());
			assertEquals(100, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(100, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 0
			expResult = expManager.calculateLevel(100);
			assertEquals(2, expResult.getLevel());
			assertEquals(200, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(200, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 50
			expResult = expManager.calculateLevel(150);
			assertEquals(2, expResult.getLevel());
			assertEquals(200, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(150, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 0
			expResult = expManager.calculateLevel(300);
			assertEquals(3, expResult.getLevel());
			assertEquals(300, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(300, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 50
			expResult = expManager.calculateLevel(650);
			assertEquals(4, expResult.getLevel());
			assertEquals(400, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(350, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 400 + 50
			expResult = expManager.calculateLevel(1050);
			assertEquals(5, expResult.getLevel());
			assertEquals(400, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(350, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 400 + 400
			expResult = expManager.calculateLevel(1400);
			assertEquals(6, expResult.getLevel());
			assertEquals(400, expResult.getLevelExperience(), 0);
			assertEquals(0, expResult.getLevelUpCurrent(), 0);
			assertEquals(400, expResult.getLevelUpNecessary(), 0);
			
			// Current exp: 100 + 200 + 300 + 400 + 400 + 50
			expResult = expManager.calculateLevel(1450);
			assertEquals(6, expResult.getLevel());
			assertEquals(400, expResult.getLevelExperience(), 0);
			assertEquals(50, expResult.getLevelUpCurrent(), 0);
			assertEquals(350, expResult.getLevelUpNecessary(), 0);
		}
	}
}

