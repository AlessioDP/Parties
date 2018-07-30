package com.alessiodp.parties.common.logging;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogLine {
	@Getter private String base;
	@Getter private String level;
	@Getter private Date date;
	@Getter private String position;
	@Getter private String message;
	
	LogLine(String b, LogLevel ll, String pos, String txt) {
		base = b;
		level = Integer.toString(ll.getLevel());
		date = Calendar.getInstance().getTime();
		position = pos;
		message = txt;
	}
	
	private String getFormattedDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
	}
	private String getFormattedTime() {
		return new SimpleDateFormat("HH:mm:ss").format(date.getTime());
	}
	
	public String getFormattedMessage() {
		return getBase()
			.replace("%date%", getFormattedDate())
			.replace("%time%", getFormattedTime())
			.replace("%level%", getLevel())
			.replace("%position%", getPosition())
			.replace("%message%", getMessage());
	}
}
