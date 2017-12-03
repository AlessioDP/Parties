package com.alessiodp.parties.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.alessiodp.parties.utils.enums.LogLevel;

public class LogLine {
	private String base;
	private String level;
	private Date time;
	private String position;
	private String message;
	
	public LogLine(String b, LogLevel ll, String pos, String txt) {
		base = b;
		level = Integer.toString(ll.getLevel());
		time = Calendar.getInstance().getTime();
		position = pos;
		message = txt;
	}
	
	public String getBase() {
		return base;
	}
	public String getLevel() {
		return level;
	}
	public Date getFullDate() {
		return time;
	}
	public String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(time.getTime());
	}
	public String getTime() {
		return new SimpleDateFormat("HH:mm:ss").format(time.getTime());
	}
	public String getPosition() {
		return position;
	}
	public String getMessage() {
		return message;
	}
	
	public String getFormattedMessage() {
		return getBase()
			.replace("%date%", getDate())
			.replace("%time%", getTime())
			.replace("%level%", getLevel())
			.replace("%position%", getPosition())
			.replace("%message%", getMessage());
	}
}
