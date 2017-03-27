package com.alessiodp.parties.objects;

import java.util.List;

public class Rank {
	private String name;
	private String chat;
	private int level;
	private boolean dft;
	private List<String> permissions;
	
	public Rank(int lvl, String nm, String ch, boolean dft, List<String> perm){
		level = lvl;
		name = nm;
		chat = ch;
		this.dft = dft;
		permissions = perm;
	}
	
	/**
	    *  	Set name of the rank
	    */
	public void setName(String name){this.name = name;}
	/**
	    *  	Get name of the rank
	    */
	public String getName(){return name;}
	
	/**
	    *  	Set chat format of the rank
	    */
	public void setChat(String ch){this.chat = ch;}
	/**
	    *  	Get chat format of the rank
	    */
	public String getChat(){return chat;}
	
	/**
	    *  	Set level of the rank (Rank int)
	    */
	public void setLevel(int lvl){level = lvl;}
	/**
	    *  	Get level of the rank (Rank int)
	    */
	public int getLevel(){return level;}
	
	/**
	    *  	Set rank as default
	    */
	public void setDefault(boolean def){dft = def;}
	/**
	    *  	Get boolean if rank is default
	    */
	public boolean getDefault(){return dft;}
	
	/**
	    *  	Set permissions of the rank
	    */
	public void setPermissions(List<String> perm){permissions = perm;}
	/**
	    *  	Get permissions of the rank
	    */
	public List<String> getPermissions(){return permissions;}
	
	/**
	    *  	Check if the rank have a permission
	    */
	public boolean havePermission(String p){                                                                                                                                                                                                                                                                                                                                                                   
		boolean ret = false;
		for(int c=0;c<permissions.size();c++){
			String s = permissions.get(c);
			if(s.equals("*")){
				ret = true;
				break;
			}
			if(s.equalsIgnoreCase("-"+p)){
					break;
			} else if(s.equalsIgnoreCase(p)){
				ret = true;
				break;
			}	
		}
		return ret;
	}
}
