package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.parties.common.PartiesPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ADPPlugin.class
})
public class CensorUtilsTest {
	@Before
	public void setUp() {
		PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		
		when(mockLoggerManager.isDebugEnabled()).thenReturn(true);
		doAnswer((args) -> {
			System.out.println((String) args.getArgument(0));
			return null;
		}).when(mockLoggerManager).logDebug(anyString(), anyBoolean());
		doAnswer((args) -> {
			((Exception) args.getArgument(1)).printStackTrace();
			return null;
		}).when(mockLoggerManager).printErrorStacktrace(any(), any());
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
