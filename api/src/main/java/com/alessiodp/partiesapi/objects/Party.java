package com.alessiodp.partiesapi.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import com.alessiodp.partiesapi.interfaces.Color;

public class Party {
	
	private String name;
	private List<UUID> members;
	private UUID leader;
	private boolean fixed;
	private String description;
	private String motd;
	private Location home;
	private String prefix;
	private String suffix;
	private Color color;
	private int kills;
	private String password;
	
	public Party(String name) {
		this.name = name;
		members = new ArrayList<UUID>();
		leader = null;
		fixed = false;
		description = "";
		motd = "";
		home = null;
		prefix = "";
		suffix = "";
		color = null;
		kills = 0;
		password = "";
	}
	
	public Party(Party copy) {
		name = copy.name;
		members = copy.members;
		leader = copy.leader;
		fixed = copy.fixed;
		description = copy.description;
		motd = copy.motd;
		home = copy.home;
		prefix = copy.prefix;
		suffix = copy.suffix;
		color = copy.color;
		kills = copy.kills;
		password = copy.password;
	}
	
	/**
	 * Get the party name
	 * 
	 * @return Returns the name of the party
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the party name
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the party members list
	 * 
	 * @return Returns the members list of the party
	 */
	public List<UUID> getMembers() {
		return members;
	}
	
	/**
	 * Set the party members list
	 * 
	 * @param members
	 *            The list composed by members UUIDs
	 */
	public void setMembers(List<UUID> members) {
		this.members = members;
	}
	
	/**
	 * Get the party leader
	 * 
	 * @return Returns the {@link UUID} of the party leader
	 */
	public UUID getLeader() {
		return leader;
	}
	
	/**
	 * Set the party leader
	 * 
	 * @param leader
	 *            The {@link UUID} of the leader
	 */
	public void setLeader(UUID leader) {
		this.leader = leader;
	}
	
	/**
	 * Is the party fixed?
	 * 
	 * @return Returns if the party is fixed
	 */
	public boolean isFixed() {
		return fixed;
	}
	
	/**
	 * Toggle a fixed party
	 * 
	 * @param fixed
	 *            {@code True} to be fixed
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	/**
	 * Get the party description
	 * 
	 * @return Returns party description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the party description
	 * 
	 * @param description
	 *            The description of the party
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the Message Of The Day of the party
	 * 
	 * @return Returns the MOTD of the party
	 */
	public String getMotd() {
		return motd;
	}
	
	/**
	 * Set the Message Of The Day of the party
	 * 
	 * @param motd
	 *            The MOTD of the party
	 */
	public void setMotd(String motd) {
		this.motd = motd;
	}
	
	/**
	 * Get the home of the party
	 * 
	 * @return Returns the {@link Location} of the party home
	 */
	public Location getHome() {
		return home;
	}
	
	/**
	 * Set the home of the party
	 * 
	 * @param home
	 *            The {@code Location} of the party home
	 */
	public void setHome(Location home) {
		this.home = home;
	}
	
	/**
	 * Get the party prefix
	 * 
	 * @return Returns the prefix of the party
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Set the party prefix
	 * 
	 * @param prefix
	 *            The prefix of the party
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Get the party suffix
	 * 
	 * @return Returns the suffix of the party
	 */
	public String getSuffix() {
		return suffix;
	}
	
	/**
	 * Set the party suffix
	 * 
	 * @param suffix
	 *            The suffix of the party
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	/**
	 * Get the party color
	 * 
	 * @return Returns the {@code Color} of the party
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Set the party color
	 * 
	 * @param color
	 *            The {@code Color} of the party
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Get the kills number of the party
	 * 
	 * @return The number of kills of the party
	 */
	public int getKills() {
		return kills;
	}
	
	/**
	 * Set the number of kills of the party
	 * 
	 * @param kills
	 *            The number of kills of the party
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	/**
	 * Get the party password
	 * 
	 * @return Returns the password of the party, HASHED
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Set the party password
	 * 
	 * @param password
	 *            The password of the party, HASHED
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
