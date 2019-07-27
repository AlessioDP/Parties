package com.alessiodp.parties.api.interfaces;

import com.alessiodp.parties.api.Parties;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface Party {
	
	/**
	 * Get the party name
	 *
	 * @return Returns the name of the party
	 */
	@NonNull String getName();
	
	/**
	 * Delete the party
	 */
	void delete();
	
	/**
	 * Rename the party
	 *
	 * @param newName The name to set
	 */
	void rename(@NonNull String newName);
	
	/**
	 * Set the party name
	 *
	 * @param name The name to set
	 * @deprecated Use rename(String)
	 */
	@Deprecated
	default void setName(String name) {
		rename(name);
	}
	
	/**
	 * Get the party members sub
	 *
	 * @return Returns the members sub of the party
	 */
	@NonNull List<UUID> getMembers();
	
	/**
	 * Add the player to the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to add
	 * @return Returns true if successfully added
	 */
	boolean addMember(@NonNull PartyPlayer partyPlayer);
	
	/**
	 * Remove the player from the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to remove
	 */
	void removeMember(@NonNull PartyPlayer partyPlayer);
	
	/**
	 * Set the party members sub
	 *
	 * @param members The sub composed by members UUIDs
	 * @deprecated Use addMember(PartyPlayer) or removeMember(PartyPlayer)
	 */
	@Deprecated
	default void setMembers(List<UUID> members) {
		// Add players
		for (UUID uuid : members) {
			PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(uuid);
			if (partyPlayer != null) {
				if (!getMembers().contains(uuid))
					addMember(partyPlayer);
			}
			
		}
		
		// Remove players
		for (UUID uuid : getMembers()) {
			PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(uuid);
			if (partyPlayer != null) {
				if (!members.contains(uuid))
					removeMember(partyPlayer);
			}
			
		}
	}
	
	/**
	 * Get a list of online members
	 *
	 * @param bypassVanish Bypass player with vanish?
	 * @return Returns an unmodifiable {@code Set<PartyPlayer>}
	 */
	@NonNull Set<PartyPlayer> getOnlineMembers(boolean bypassVanish);
	
	/**
	 * Get the party leader
	 *
	 * @return Returns the {@link UUID} of the party leader, can be magic if the party is fixed
	 */
	@Nullable UUID getLeader();
	
	/**
	 * Change the party leader
	 *
	 * @param leaderPartyPlayer The {@link PartyPlayer} to promote as leader
	 */
	void changeLeader(@NonNull PartyPlayer leaderPartyPlayer);
	
	/**
	 * Set the party leader
	 *
	 * @param leader The {@link UUID} of the leader
	 * @deprecated Use changeLeader(PartyPlayer)
	 */
	@Deprecated
	default void setLeader(UUID leader) {
		PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(leader);
		if (partyPlayer != null) {
			changeLeader(partyPlayer);
		}
	}
	
	/**
	 * Is the party fixed?
	 *
	 * @return Returns if the party is fixed
	 */
	boolean isFixed();
	
	/**
	 * Toggle a fixed party
	 *
	 * @param fixed {@code True} to be fixed
	 * @param newLeader New leader to set, null if setting Party as fixed
	 */
	void setFixed(boolean fixed, @Nullable PartyPlayer newLeader);
	
	/**
	 * Toggle a fixed party
	 *
	 * @param fixed {@code True} to be fixed
	 * @deprecated Use setFixed(boolean, PartyPlayer)
	 */
	@Deprecated
	default void setFixed(boolean fixed) {
		UUID firstMember = getMembers().size() > 0 ? getMembers().get(0) : null;
		PartyPlayer partyPlayer = firstMember != null ? Parties.getApi().getPartyPlayer(firstMember) : null;
		setFixed(fixed, partyPlayer);
	}
	
	/**
	 * Get the party description
	 *
	 * @return Returns party description
	 */
	@NonNull String getDescription();
	
	/**
	 * Set the party description
	 *
	 * @param description The description of the party
	 */
	void setDescription(@NonNull String description);
	
	/**
	 * Get the Message Of The Day of the party
	 *
	 * @return Returns the MOTD of the party
	 */
	@NonNull String getMotd();
	
	/**
	 * Set the Message Of The Day of the party
	 *
	 * @param motd The MOTD of the party
	 */
	void setMotd(@NonNull String motd);
	
	/**
	 * Get the home of the party
	 *
	 * @return Returns the {@link HomeLocation} of the party home
	 */
	@Nullable HomeLocation getHome();
	
	/**
	 * Set the home of the party
	 *
	 * @param home The {@code HomeLocation} of the party home
	 */
	void setHome(@Nullable HomeLocation home);
	
	/**
	 * Get the party color
	 *
	 * @return Returns the {@code Color} of the party
	 */
	@Nullable Color getColor();
	
	/**
	 * Set the party color
	 *
	 * @param color The {@code Color} of the party
	 */
	void setColor(@Nullable Color color);
	
	/**
	 * Get the kills number of the party
	 *
	 * @return The number of kills of the party
	 */
	int getKills();
	
	/**
	 * Set the number of kills of the party
	 *
	 * @param kills The number of kills of the party
	 */
	void setKills(int kills);
	
	/**
	 * Get the party password
	 *
	 * @return Returns the password of the party, HASHED
	 */
	@NonNull String getPassword();
	
	/**
	 * Set the party password
	 *
	 * @param password The password of the party, HASHED
	 */
	void setPassword(@NonNull String password);
	
	/**
	 * Get the party friendly fire protection
	 *
	 * @return Returns true if the party is protected
	 */
	boolean getProtection();
	
	/**
	 * Set the party friendly fire protection
	 *
	 * @param protection True if you want protect the party
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
	 * @param experience The experience number to set
	 */
	void setExperience(double experience);
	
	/**
	 * Get the current party level
	 *
	 * @return Returns the calculated level of the party
	 */
	int getLevel();
	
	/**
	 * Get the party follow option
	 *
	 * @return Returns true if the party have follow option enabled
	 * @deprecated Use isFollowEnabled()
	 */
	@Deprecated
	default boolean getFollowEnabled() {
		return isFollowEnabled();
	}
	
	/**
	 * Set the party follow option
	 *
	 * @param follow True if you want enable follow option
	 */
	void setFollowEnabled(boolean follow);
	
	/**
	 * Check if the party have the follow option enabled
	 *
	 * @return Returns true if follow option is enabled
	 */
	boolean isFollowEnabled();
	
	/**
	 * Send a broadcast message to the party.
	 * It requires a player to send the message because Parties gets the placeholder info from the player.
	 * The player can be null if its a general broadcast.
	 *
	 * @param message The message to broadcast
	 * @param player  The {@link PartyPlayer} who sent the message
	 */
	void broadcastMessage(@Nullable String message, @Nullable PartyPlayer player);
}
