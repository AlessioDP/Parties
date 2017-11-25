package com.alessiodp.parties.utils.bungeecord;

import java.util.ArrayList;
import java.util.List;

public class VariablesBungee {
	public static boolean follow_enable;
	public static int follow_neededrank;
	public static int follow_minimumrank;
	public static List<String> follow_listserver;
	
	public VariablesBungee() {
		loadDefaults();
	}
	public void loadDefaults() {
		follow_enable = false;
		follow_neededrank = 0;
		follow_minimumrank = 0;
		List<String> list = new ArrayList<String>();
		list.add("hub");
		list.add("lobby");
		follow_listserver = list;
	}
}
