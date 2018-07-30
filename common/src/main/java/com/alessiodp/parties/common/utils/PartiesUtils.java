package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PartiesUtils {
	
	/*
	 * Censor utils
	 */
	public static boolean checkCensor(String phrase) {
		boolean ret = false;
		if (ConfigMain.ADDITIONAL_CENSOR_ENABLE) {
			for (String cen : ConfigMain.ADDITIONAL_CENSOR_CONTAINS) {
				// Contains
				if (ret)
					break;
				
				if (!ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE) {
					if (phrase.toLowerCase().contains(cen.toLowerCase()))
						ret = true;
				} else if (phrase.contains(cen))
					ret = true;
			}
			for (String cen : ConfigMain.ADDITIONAL_CENSOR_STARTSWITH) {
				// Starts with
				if (ret)
					break;
				
				if (!ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE) {
					if (phrase.toLowerCase().startsWith(cen.toLowerCase()))
						ret = true;
				} else if (phrase.startsWith(cen))
					ret = true;
			}
			for (String cen : ConfigMain.ADDITIONAL_CENSOR_ENDSWITH) {
				// Ends with
				if (ret)
					break;
				
				if (!ConfigMain.ADDITIONAL_CENSOR_CASESENSITIVE) {
					if (phrase.toLowerCase().endsWith(cen.toLowerCase()))
						ret = true;
				} else if (phrase.endsWith(cen))
					ret = true;
			}
		}
		return ret;
	}
	
	/*
	 * Hashing
	 */
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
