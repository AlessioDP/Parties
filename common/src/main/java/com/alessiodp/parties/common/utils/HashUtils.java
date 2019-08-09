package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.configuration.data.ConfigParties;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	public static String hashText(String text) {
		String ret = "";
		try {
			byte[] result = MessageDigest.getInstance(ConfigParties.PASSWORD_HASH)
					.digest(text.getBytes(ConfigParties.PASSWORD_ENCODE));
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
