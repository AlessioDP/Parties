package com.alessiodp.partiesapi.interfaces;

import java.util.List;

public interface Rank {
	
	/**
	 *	Set configuration name of the rank
	 */
	public void setHardName(String hardname);
	/**
	 *	Get configuration name of the rank
	 */
	public String getHardName();
	
	/**
	 *	Set name of the rank
	 */
	public void setName(String name);
	/**
	 *	Get name of the rank
	 */
	public String getName();
	
	/**
	 *	Set chat format of the rank
	 */
	public void setChat(String ch);
	/**
	 *	Get chat format of the rank
	 */
	public String getChat();
	
	/**
	 *	Set level of the rank (Rank int)
	 */
	public void setLevel(int lvl);
	/**
	 *	Get level of the rank (Rank int)
	 */
	public int getLevel();
	
	/**
	 *	Set rank as default
	 */
	public void setDefault(boolean def);
	/**
	 *	Get boolean if rank is default
	 */
	public boolean getDefault();
	
	/**
	 *	Set permissions of the rank
	 */
	public void setPermissions(List<String> perm);
	/**
	 *	Get permissions of the rank
	 */
	public List<String> getPermissions();
	
	/**
	 *	Check if the rank have a permission
	 */
	public boolean havePermission(String p);
}
