package com.alessiodp.parties.bungeecord.configuration;

import java.util.ArrayList;
import java.util.List;

public class Variables {
	public static boolean follow_enable;
	public static int follow_neededrank;
	public static int follow_minimumrank;
	public static List<String> follow_listserver;
	
	public Variables() {
		loadDefaults();
	}
	public void loadDefaults() {
		follow_enable = false;
		follow_neededrank = 0;
		follow_minimumrank = 0;
		follow_listserver = new ArrayList<String>();
		follow_listserver.add("hub");
		follow_listserver.add("lobby");
	}
}
