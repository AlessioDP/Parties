package com.alessiodp.parties.players.objects;

import java.util.List;

import com.alessiodp.partiesapi.interfaces.Rank;

public class RankImpl implements Rank {
	
	private String hardName;
	private String name;
	private String chat;
	private int level;
	private boolean def;
	private List<String> permissions;
	
	public RankImpl(int lvl, String hm, String nm, String ch, boolean dft, List<String> perm) {
		level = lvl;
		hardName = hm;
		name = nm;
		chat = ch;
		def = dft;
		permissions = perm;
	}
	
	public void setHardName(String hardName) {
		this.hardName = hardName;
	}
	
	public String getHardName() {
		return hardName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setChat(String chat) {
		this.chat = chat;
	}
	
	public String getChat() {
		return chat;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setDefault(boolean def) {
		this.def = def;
	}
	
	public boolean isDefault() {
		return def;
	}
	
	@Deprecated
	public boolean getDefault() {
		return def;
	}
	
	public void setPermissions(List<String> perm) {
		permissions = perm;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public boolean havePermission(String p) {
		boolean ret = false;
		for (int c = 0; c < permissions.size(); c++) {
			String s = permissions.get(c);
			if (s.equals("*")) {
				ret = true;
				break;
			}
			if (s.equalsIgnoreCase("-" + p)) {
				break;
			} else if (s.equalsIgnoreCase(p)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
