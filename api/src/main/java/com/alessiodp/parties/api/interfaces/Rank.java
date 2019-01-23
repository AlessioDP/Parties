package com.alessiodp.parties.api.interfaces;

import java.util.List;

public interface Rank {
	
	/**
	 * Set the configuration name of the rank. This should not be touched.
	 *
	 * @param hardname The configuration name of the rank
	 */
	void setHardName(String hardname);
	
	/**
	 * Get the configuration name of the rank This should not be touched.
	 *
	 * @return Returns the configuration name of the rank
	 */
	String getHardName();
	
	/**
	 * Set the name of the rank
	 *
	 * @param name The name of the rank
	 */
	void setName(String name);
	
	/**
	 * Get the name of the rank
	 *
	 * @return Returns the name of the rank
	 */
	String getName();
	
	/**
	 * Set the chat format of the rank
	 *
	 * @param chat The chat format of the rank
	 */
	void setChat(String chat);
	
	/**
	 * Get the chat format of the rank
	 *
	 * @return Returns the chat format of the rank
	 */
	String getChat();
	
	/**
	 * Set the level of the rank
	 *
	 * @param level The level of the rank
	 */
	void setLevel(int level);
	
	/**
	 * Get the level of the rank
	 *
	 * @return Returns the level of the rank
	 */
	int getLevel();
	
	/**
	 * Set the rank as default
	 *
	 * @param def {@code True} to set the rank as the default
	 */
	void setDefault(boolean def);
	
	/**
	 * Get if the rank is default
	 *
	 * @return Returns {@code true} if the rank is the default
	 */
	boolean isDefault();
	
	/**
	 * Set the permissions sub of the rank
	 *
	 * @param perm The permissions sub of the rank
	 */
	void setPermissions(List<String> perm);
	
	/**
	 * Get the permissions sub of the rank
	 *
	 * @return Returns the permissions sub of the rank
	 */
	List<String> getPermissions();
	
	/**
	 * Check if the rank have a permission
	 *
	 * @param perm The permission to check
	 * @return Returns {@code true} if the rank has that permission
	 */
	boolean havePermission(String perm);
}
