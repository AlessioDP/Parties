package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.api.interfaces.Rank;

import java.util.List;

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
		for (String perm : permissions) {
			if (perm.equals("*")) {
				ret = true;
				break;
			}
			
			if (perm.equalsIgnoreCase("-" + p)) {
				break;
			} else if (perm.equalsIgnoreCase(p)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
