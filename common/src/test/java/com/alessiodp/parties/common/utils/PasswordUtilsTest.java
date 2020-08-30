package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.configuration.data.ConfigParties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
		ConfigParties.class,
		PasswordUtils.class
})
public class PasswordUtilsTest {
	
	@Before
	public void setUp() {
		// Configuration
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_ALLOWEDCHARS = "[a-zA-Z0-9]+";
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_MINLENGTH = 3;
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_MAXLENGTH = 12;
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_HASH = "MD5";
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENCODE = "UTF-8";
	}
	
	@Test
	public void testIsValid() {
		String password = "ab";
		assertFalse(PasswordUtils.isValid(password));
		
		password = "abc";
		assertTrue(PasswordUtils.isValid(password));
		
		password = "abcdefg";
		assertTrue(PasswordUtils.isValid(password));
		
		password = "abcdefghilmno";
		assertFalse(PasswordUtils.isValid(password));
	}
	
	@Test
	public void testHash() {
		String password = "abcde";
		String passwordHash = "ab56b4d92b40713acc5af89985d4b786";
		assertEquals(PasswordUtils.hashText(password), passwordHash);
		
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_HASH = "SHA-1";
		passwordHash = "03de6c570bfe24bfc328ccd7ca46b76eadaf4334";
		assertEquals(PasswordUtils.hashText(password), passwordHash);
		
		ConfigParties.ADDITIONAL_JOIN_PASSWORD_HASH = "SHA-256";
		passwordHash = "36bbe50ed96841d10443bcb670d6554f0a34b761be67ec9c4a8ad2c0c44ca42c";
		assertEquals(PasswordUtils.hashText(password), passwordHash);
	}
}
