package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.PartiesPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CensorUtils {
	public static boolean checkCensor(String regex, String text, String regexError) {
		boolean ret = false;
		if (!regex.isEmpty()) {
			try {
				Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
				
				if (matcher.find()) {
					ret = true;
				}
			} catch (Exception ex) {
				PartiesPlugin.getInstance().getLoggerManager().printError(regexError);
				ex.printStackTrace();
			}
		}
		return ret;
	}
	
	public static boolean checkAllowedCharacters(String regex, String text, String regexError) {
		boolean ret = true;
		try {
			Matcher matcher = Pattern.compile(regex).matcher(text);
			
			ret = matcher.matches();
		} catch (Exception ex) {
			PartiesPlugin.getInstance().getLoggerManager().printError(regexError);
			ex.printStackTrace();
		}
		return ret;
	}
}
