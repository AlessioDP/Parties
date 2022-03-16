package com.alessiodp.parties.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PartyHome {
	
	/**
	 * Gets the name of the home
	 *
	 * @return the name
	 */
	@Nullable String getName();
	
	/**
	 * Set the name
	 *
	 * @param name the name to set
	 */
	void setName(String name);
	
	/**
	 * Gets the world name of the location
	 *
	 * @return the world name
	 */
	@NotNull String getWorld();
	
	/**
	 * Set the world name
	 *
	 * @param world the world name to set
	 */
	void setWorld(String world);
	
	/**
	 * Get the x-coordinate of the location
	 *
	 * @return the x-coordinate
	 */
	double getX();
	
	/**
	 * Set the x-coordinate
	 *
	 * @param x the x-coordinate to set
	 */
	void setX(double x);
	
	/**
	 * Get the y-coordinate of the location
	 *
	 * @return the y-coordinate
	 */
	double getY();
	
	/**
	 * Set the y-coordinate
	 *
	 * @param y the y-coordinate to set
	 */
	void setY(double y);
	
	/**
	 * Get the z-coordinate of the location
	 *
	 * @return the z-coordinate
	 */
	double getZ();
	
	/**
	 * Set the z-coordinate
	 *
	 * @param z the z-coordinate to set
	 */
	void setZ(double z);
	
	/**
	 * Get the yaw of the location
	 *
	 * @return the yaw
	 */
	float getYaw();
	
	/**
	 * Set the yaw
	 *
	 * @param yaw the yaw to set
	 */
	void setYaw(float yaw);
	
	/**
	 * Get the pitch of the location
	 *
	 * @return the pitch
	 */
	float getPitch();
	
	/**
	 * Set the pitch
	 *
	 * @param pitch the pitch to set
	 */
	void setPitch(float pitch);
	
	/**
	 * Get the BungeeCord server of the location
	 *
	 * @return the server
	 */
	@Nullable String getServer();
	
	/**
	 * Set the BungeeCord server
	 *
	 * @param server the server to set
	 */
	void setServer(String server);
}
