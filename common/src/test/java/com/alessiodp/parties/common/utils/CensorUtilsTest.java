package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.parties.common.PartiesPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CensorUtilsTest {
	private static MockedStatic<ADPPlugin> staticPlugin;
	
	@BeforeAll
	public static void setUp() {
		PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		doAnswer((args) -> {
			System.out.println((String) args.getArgument(0));
			return null;
		}).when(mockLoggerManager).logDebug(anyString(), anyBoolean());
		
		staticPlugin = Mockito.mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
	}
	
	@AfterAll
	public static void tearDown() {
		staticPlugin.close();
	}
	
	@Test
	public void testCensor() {
		String regex = "\\b(t+(\\W|\\d|_)*e+(\\W|\\d|_)*s+(\\W|\\d|_)*t+(\\W|\\d|_)*)";
		String str = "long test phrase";
		assertTrue(CensorUtils.checkCensor(regex, str, ""));
		
		str = "long test phrase";
		assertTrue(CensorUtils.checkCensor(regex, str, ""));
		
		str = "long teeest phrase";
		assertTrue(CensorUtils.checkCensor(regex, str, ""));
		
		str = "t.est";
		assertTrue(CensorUtils.checkCensor(regex, str, ""));
		
		str = "long tt phrase";
		assertFalse(CensorUtils.checkCensor(regex, str, ""));
	}
	
	@Test
	public void testAllowedCharacters() {
		String regex = "[a-zA-Z]+";
		String str = "simpleName";
		assertTrue(CensorUtils.checkAllowedCharacters(regex, str, ""));
		
		str = "simpleName1";
		assertFalse(CensorUtils.checkAllowedCharacters(regex, str, ""));
		
		str = "simpleName$";
		assertFalse(CensorUtils.checkAllowedCharacters(regex, str, ""));
		
		str = "simpleName试";
		assertFalse(CensorUtils.checkAllowedCharacters(regex, str, ""));
		
		regex = "[\\p{L}\\s]+";
		str = "simpleName试тÖทͳ";
		assertTrue(CensorUtils.checkAllowedCharacters(regex, str, ""));
	}
}
