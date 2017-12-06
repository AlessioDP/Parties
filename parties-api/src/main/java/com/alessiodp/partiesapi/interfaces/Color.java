package com.alessiodp.partiesapi.interfaces;

public interface Color {
	
	/**
	 *	Set color name
	 */
	public void setName(String name);
	/**
	 *	Get color name
	 */
	public String getName();
	
	/**
	 *	Set color command
	 */
	public void setCommand(String command);
	/**
	 *	Get color command
	 */
	public String getCommand();
	
	/**
	 *	Set color code
	 */
	public void setCode(String code);
	/**
	 *	Get color code
	 */
	public String getCode();
	
	/**
	 *	Set dynamic priority
	 */
	public void setDynamicPriority(int priority);
	/**
	 *	Get dynamic priority
	 */
	public int getDynamicPriority();
	
	/**
	 *	Set dynamic members minimum
	 */
	public void setDynamicMembers(int members);
	/**
	 *	Get dynamic members minimum
	 */
	public int getDynamicMembers();
	
	/**
	 *	Set dynamic kills minimum
	 */
	public void setDynamicKills(int kills);
	/**
	 *	Get dynamic kills minimum
	 */
	public int getDynamicKills();
}
