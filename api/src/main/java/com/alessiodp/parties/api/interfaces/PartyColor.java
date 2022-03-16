package com.alessiodp.parties.api.interfaces;

public interface PartyColor {
	
	/**
	 * Set the color name
	 *
	 * @param name the name of the color
	 */
	void setName(String name);
	
	/**
	 * Get the color name
	 *
	 * @return the name of the color
	 */
	String getName();
	
	/**
	 * Set the color command
	 *
	 * @param command the command to set
	 */
	void setCommand(String command);
	
	/**
	 * Get the color command
	 *
	 * @return the color command
	 */
	String getCommand();
	
	/**
	 * Set the color code
	 *
	 * @param code the code to set
	 */
	void setCode(String code);
	
	/**
	 * Get the color code
	 *
	 * @return the color code
	 */
	String getCode();
	
	/**
	 * Set the dynamic priority
	 *
	 * @param priority the priority to set
	 */
	void setDynamicPriority(int priority);
	
	/**
	 * Get the dynamic priority
	 *
	 * @return the color priority
	 */
	int getDynamicPriority();
	
	/**
	 * Set the dynamic members minimum
	 *
	 * @param members the dynamic members minimum number to set
	 */
	void setDynamicMembers(int members);
	
	/**
	 * Get the dynamic members minimum
	 *
	 * @return the dynamic members minimum number
	 */
	int getDynamicMembers();
	
	/**
	 * Set the dynamic kills minimum
	 *
	 * @param kills the dynamic kills minimum number to set
	 */
	void setDynamicKills(int kills);
	
	/**
	 * Get the dynamic kills minimum
	 *
	 * @return the dynamic kills minimum number
	 */
	int getDynamicKills();
}
