package com.alessiodp.parties.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import com.alessiodp.parties.configuration.Variables;

public class PasswordFilter extends AbstractFilter {
	
	private Result filter(String message) {
		Result ret = Result.NEUTRAL;
		if (message != null) {
			String[] blacklist = new String[] {
					"issued server command: /" + Variables.command_party + " " + Variables.command_password + " ",
					"issued server command: /" + Variables.command_party + " " + Variables.command_join + " "
			};
			for (String str : blacklist) {
				if (message.toLowerCase().contains(str))
					ret = Result.DENY;
			}
		}
		return ret;
	}
	
	@Override
	public Result filter(LogEvent event) {
		return filter(event.getMessage().getFormattedMessage());
	}
	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return filter(msg.getFormattedMessage());
	}
	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return filter(msg.toString());
	}
	
	@Override
	public final Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return filter(msg);
	}
}
