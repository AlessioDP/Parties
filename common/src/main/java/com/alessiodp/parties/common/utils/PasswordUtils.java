package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.configuration.data.ConfigParties;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class PasswordUtils {
	
	public static boolean isValid(String password) {
		return password != null
				&& Pattern.compile(ConfigParties.ADDITIONAL_JOIN_PASSWORD_ALLOWEDCHARS).matcher(password).matches()
				&& password.length() <= ConfigParties.ADDITIONAL_JOIN_PASSWORD_MAXLENGTH
				&& password.length() >= ConfigParties.ADDITIONAL_JOIN_PASSWORD_MINLENGTH;
	}
	
	public static String hashText(String text) {
		String ret = "";
		try {
			byte[] result = MessageDigest.getInstance(ConfigParties.ADDITIONAL_JOIN_PASSWORD_HASH)
					.digest(text.getBytes(ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENCODE));
			StringBuilder stringBuilder = new StringBuilder();
			for (byte b : result) {
				stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
			}
			ret = stringBuilder.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
