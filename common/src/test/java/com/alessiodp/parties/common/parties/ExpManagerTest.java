package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.utils.FormulaUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.ExpResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpManagerTest {
	private static final PartiesPlugin mockPlugin = Mockito.mock(PartiesPlugin.class);
	private static ExpManager expManager;
	
	@BeforeAll
	public static void setUp() {
		ConfigMain.ADDITIONAL_EXP_ENABLE = true;
		ConfigMain.ADDITIONAL_EXP_MODE = "progressive";
		
		FormulaUtils.initializeEngine();
		
		expManager = new ExpManager(mockPlugin);
		expManager.reload();
	}
	
	@Test
	public void testCalculateLevelProgressive() {
		ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_START = 100;
		ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP = "%previous%*2";
		
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
	
	@Test
	public void testCalculateLevelFixedNoRepeat() {
		ConfigMain.ADDITIONAL_EXP_MODE = "fixed";
		ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT = false;
		expManager.reload();
		ConfigMain.ADDITIONAL_EXP_FIXED_LIST = Arrays.asList(
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
	
	@Test
	public void testCalculateLevelFixedWithRepeat() {
		ConfigMain.ADDITIONAL_EXP_MODE = "fixed";
		ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT = true;
		expManager.reload();
		ConfigMain.ADDITIONAL_EXP_FIXED_LIST = Arrays.asList(
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

