package com.alessiodp.parties.api.interfaces;

import java.util.List;
import java.util.UUID;

public interface Party {
	
	/**
	 * Get the party name
	 * 
	 * @return Returns the name of the party
	 */
	String getName();
	
	/**
	 * Set the party name
	 * 
	 * @param name
	 *            The name to set
	 */
	void setName(String name);
	
	/**
	 * Get the party members executors
	 * 
	 * @return Returns the members executors of the party
	 */
	List<UUID> getMembers();
	
	/**
	 * Set the party members executors
	 * 
	 * @param members
	 *            The executors composed by members UUIDs
	 */
	void setMembers(List<UUID> members);
	
	/**
	 * Get the party leader
	 * 
	 * @return Returns the {@link UUID} of the party leader
	 */
	UUID getLeader();
	
	/**
	 * Set the party leader
	 * 
	 * @param leader
	 *            The {@link UUID} of the leader
	 */
	void setLeader(UUID leader);
	
	/**
	 * Is the party fixed?
	 * 
	 * @return Returns if the party is fixed
	 */
	boolean isFixed();
	
	/**
	 * Toggle a fixed party
	 * 
	 * @param fixed
	 *            {@code True} to be fixed
	 */
	void setFixed(boolean fixed);
	
	/**
	 * Get the party description
	 * 
	 * @return Returns party description
	 */
	String getDescription();
	
	/**
	 * Set the party description
	 * 
	 * @param description
	 *            The description of the party
	 */
	void setDescription(String description);
	
	/**
	 * Get the Message Of The Day of the party
	 * 
	 * @return Returns the MOTD of the party
	 */
	String getMotd();
	
	/**
	 * Set the Message Of The Day of the party
	 * 
	 * @param motd
	 *            The MOTD of the party
	 */
	void setMotd(String motd);
	
	/**
	 * Get the home of the party
	 * 
	 * @return Returns the {@link HomeLocation} of the party home
	 */
	HomeLocation getHome();
	
	/**
	 * Set the home of the party
	 * 
	 * @param home
	 *            The {@code HomeLocation} of the party home
	 */
	void setHome(HomeLocation home);
	
	/**
	 * Get the party color
	 * 
	 * @return Returns the {@code Color} of the party
	 */
	Color getColor();
	
	/**
	 * Set the party color
	 * 
	 * @param color
	 *            The {@code Color} of the party
	 */
	void setColor(Color color);
	
	/**
	 * Get the kills number of the party
	 * 
	 * @return The number of kills of the party
	 */
	int getKills();
	
	/**
	 * Set the number of kills of the party
	 * 
	 * @param kills
	 *            The number of kills of the party
	 */
	void setKills(int kills);
	
	/**
	 * Get the party password
	 * 
	 * @return Returns the password of the party, HASHED
	 */
	String getPassword();
	
	/**
	 * Set the party password
	 * 
	 * @param password
	 *            The password of the party, HASHED
	 */
	void setPassword(String password);
	
	/**
	 * Get the party friendly fire protection
	 *
	 * @return Returns true if the party is protected
	 */
	boolean getProtection();
	
	/**
	 * Set the party friendly fire protection
	 *
	 * @param protection
	 *            True if you want protect the party
	 */
	void setProtection(boolean protection);
	
	/**
	 * Check if the party is protected from friendly fire using both
	 * command and global protection.
	 * Use this if you just want to check for FF.
	 *
	 * @return Returns true if pvp between players is protected
	 */
	boolean isFriendlyFireProtected();
	
	/**
	 * Get the party experience
	 *
	 * @return Returns the total experience of the party
	 */
	double getExperience();
	
	/**
	 * Set the party experience
	 *
	 * @param experience
	 *            The experience number to set
	 */
	void setExperience(double experience);
	
	/**
	 * Get the current party level
	 *
	 * @return Returns the calculated level of the party
	 */
	int getLevel();
}
