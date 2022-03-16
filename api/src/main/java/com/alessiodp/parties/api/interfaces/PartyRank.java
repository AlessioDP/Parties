package com.alessiodp.parties.api.interfaces;

import java.util.List;

public interface PartyRank {
	
	/**
	 * Set the configuration name of the rank. This should not be touched.
	 *
	 * @param configName the configuration name of the rank
	 */
	void setConfigName(String configName);
	
	@Deprecated
	default void setHardName(String hardname) {
		setConfigName(hardname);
	}
	
	/**
	 * Get the configuration name of the rank This should not be touched.
	 *
	 * @return the configuration name of the rank
	 */
	String getConfigName();
	
	@Deprecated
	default String getHardName() {
		return getConfigName();
	}
	
	/**
	 * Set the name of the rank
	 *
	 * @param name the name of the rank
	 */
	void setName(String name);
	
	/**
	 * Get the name of the rank
	 *
	 * @return the name of the rank
	 */
	String getName();
	
	/**
	 * Set the chat format of the rank
	 *
	 * @param chat the chat format of the rank
	 */
	void setChat(String chat);
	
	/**
	 * Get the chat format of the rank
	 *
	 * @return the chat format of the rank
	 */
	String getChat();
	
	/**
	 * Set the level of the rank
	 *
	 * @param level the level of the rank
	 */
	void setLevel(int level);
	
	/**
	 * Get the level of the rank
	 *
	 * @return the level of the rank
	 */
	int getLevel();
	
	/**
	 * Set the slot of the rank
	 *
	 * @param slot the slot of the rank
	 */
	void setSlot(int slot);
	
	/**
	 * Get the slot of the rank
	 *
	 * @return the slot of the rank
	 */
	int getSlot();
	
	/**
	 * Set the rank as default
	 *
	 * @param def {@code true} to set the rank as the default
	 */
	void setDefault(boolean def);
	
	/**
	 * Get if the rank is default
	 *
	 * @return {@code true} if the rank is the default
	 */
	boolean isDefault();
	
	/**
	 * Set the permissions sub of the rank
	 *
	 * @param perm the permissions sub of the rank
	 */
	void setPermissions(List<String> perm);
	
	/**
	 * Get the permissions sub of the rank
	 *
	 * @return the permissions sub of the rank
	 */
	List<String> getPermissions();
	
	/**
	 * Check if the rank have a permission
	 *
	 * @param perm the permission to check
	 * @return {@code true} if the rank has that permission
	 */
	boolean havePermission(String perm);
}
