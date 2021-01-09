package com.alessiodp.parties.api.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PartyHome {
	
	/**
	 * Gets the name of the home
	 *
	 * @return Returns the name
	 */
	@Nullable String getName();
	
	/**
	 * Set the name
	 *
	 * @param name The name to set
	 */
	void setName(String name);
	
	/**
	 * Gets the world name of the location
	 *
	 * @return Returns the world name
	 */
	@NonNull String getWorld();
	
	/**
	 * Set the world name
	 *
	 * @param world The world name to set
	 */
	void setWorld(String world);
	
	/**
	 * Get the x-coordinate of the location
	 *
	 * @return Returns the x-coordinate
	 */
	double getX();
	
	/**
	 * Set the x-coordinate
	 *
	 * @param x The x-coordinate to set
	 */
	void setX(double x);
	
	/**
	 * Get the y-coordinate of the location
	 *
	 * @return Returns the y-coordinate
	 */
	double getY();
	
	/**
	 * Set the y-coordinate
	 *
	 * @param y The y-coordinate to set
	 */
	void setY(double y);
	
	/**
	 * Get the z-coordinate of the location
	 *
	 * @return Returns the z-coordinate
	 */
	double getZ();
	
	/**
	 * Set the z-coordinate
	 *
	 * @param z The z-coordinate to set
	 */
	void setZ(double z);
	
	/**
	 * Get the yaw of the location
	 *
	 * @return Returns the yaw
	 */
	float getYaw();
	
	/**
	 * Set the yaw
	 *
	 * @param yaw The yaw to set
	 */
	void setYaw(float yaw);
	
	/**
	 * Get the pitch of the location
	 *
	 * @return Returns the pitch
	 */
	float getPitch();
	
	/**
	 * Set the pitch
	 *
	 * @param pitch The pitch to set
	 */
	void setPitch(float pitch);
	
	/**
	 * Get the BungeeCord server of the location
	 *
	 * @return Returns the server
	 */
	@Nullable String getServer();
	
	/**
	 * Set the BungeeCord server
	 *
	 * @param server The server to set
	 */
	void setServer(String server);
}
