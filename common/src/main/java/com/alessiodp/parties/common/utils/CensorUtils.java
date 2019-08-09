package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class CensorUtils {
	private final PartiesPlugin plugin;
	
	public boolean checkCensor(String regex, String text, String regexError) {
		boolean ret = false;
		if (!regex.isEmpty()) {
			try {
				Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
				
				if (matcher.find()) {
					ret = true;
				}
			} catch (Exception ex) {
				plugin.getLoggerManager().printErrorStacktrace(regexError, ex);
			}
		}
		return ret;
	}
	
	public boolean checkAllowedCharacters(String regex, String text, String regexError) {
		boolean ret = true;
		try {
			Matcher matcher = Pattern.compile(regex).matcher(text);
			
			ret = matcher.matches();
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(regexError, ex);
		}
		return ret;
	}
}
