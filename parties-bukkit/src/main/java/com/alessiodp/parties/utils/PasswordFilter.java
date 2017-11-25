package com.alessiodp.parties.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import com.alessiodp.parties.configuration.Variables;

public class PasswordFilter implements Filter {
	
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
	
	public Result filter(LogEvent event) {
		return filter(event.getMessage().getFormattedMessage());
	}
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return filter(msg.getFormattedMessage());
	}
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return filter(msg.toString());
	}
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return filter(msg);
	}

	public Result getOnMatch() {return Result.NEUTRAL;}
	public Result getOnMismatch() {return Result.NEUTRAL;}
}
