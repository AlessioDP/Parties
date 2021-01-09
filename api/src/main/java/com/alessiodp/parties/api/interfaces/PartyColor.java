package com.alessiodp.parties.api.interfaces;

public interface PartyColor {
	
	/**
	 * Set the color name
	 *
	 * @param name The name of the color
	 */
	void setName(String name);
	
	/**
	 * Get the color name
	 *
	 * @return Returns the name of the color
	 */
	String getName();
	
	/**
	 * Set the color command
	 *
	 * @param command The command to set
	 */
	void setCommand(String command);
	
	/**
	 * Get the color command
	 *
	 * @return Returns the color command
	 */
	String getCommand();
	
	/**
	 * Set the color code
	 *
	 * @param code The code to set
	 */
	void setCode(String code);
	
	/**
	 * Get the color code
	 *
	 * @return Returns the color code
	 */
	String getCode();
	
	/**
	 * Set the dynamic priority
	 *
	 * @param priority The priority to set
	 */
	void setDynamicPriority(int priority);
	
	/**
	 * Get the dynamic priority
	 *
	 * @return Returns the color priority
	 */
	int getDynamicPriority();
	
	/**
	 * Set the dynamic members minimum
	 *
	 * @param members The dynamic members minimum number to set
	 */
	void setDynamicMembers(int members);
	
	/**
	 * Get the dynamic members minimum
	 *
	 * @return Returns the dynamic members minimum number
	 */
	int getDynamicMembers();
	
	/**
	 * Set the dynamic kills minimum
	 *
	 * @param kills The dynamic kills minimum number to set
	 */
	void setDynamicKills(int kills);
	
	/**
	 * Get the dynamic kills minimum
	 *
	 * @return Returns the dynamic kills minimum number
	 */
	int getDynamicKills();
}
