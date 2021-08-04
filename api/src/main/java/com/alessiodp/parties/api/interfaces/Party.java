package com.alessiodp.parties.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public interface Party {
	
	/**
	 * Get the party id
	 *
	 * @return Returns the id of the party
	 */
	@NotNull UUID getId();
	
	/**
	 * Get the party name
	 *
	 * @return Returns the name of the party
	 */
	@Nullable String getName();
	
	/**
	 * Delete the party
	 */
	void delete();
	
	/**
	 * Rename the party
	 *
	 * @param newName The name to set
	 */
	void rename(@Nullable String newName);
	
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
	 * Get a list of party members. It will contains members of every rank, leader too.
	 *
	 * @return Returns the members sub of the party
	 */
	@NotNull Set<UUID> getMembers();
	
	/**
	 * Get a list of online members
	 *
	 * @return Returns an unmodifiable {@code Set<PartyPlayer>}
	 */
	@NotNull default Set<PartyPlayer> getOnlineMembers() {
		return getOnlineMembers(true);
	}
	
	/**
	 * Get a list of online members
	 *
	 * @param bypassVanish Bypass player with vanish?
	 * @return Returns an unmodifiable {@code Set<PartyPlayer>}
	 */
	@NotNull Set<PartyPlayer> getOnlineMembers(boolean bypassVanish);
	
	/**
	 * Add the player to the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to add
	 * @return Returns true if successfully added
	 */
	boolean addMember(@NotNull PartyPlayer partyPlayer);
	
	/**
	 * Remove the player from the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to remove
	 * @return Returns true if successfully removed
	 */
	boolean removeMember(@NotNull PartyPlayer partyPlayer);
	
	/**
	 * Get a list of pending invite requests
	 *
	 * @return A set of {@link PartyInvite}
	 */
	Set<PartyInvite> getInviteRequests();
	
	/**
	 * Invite the player into the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to invite
	 * @return Returns the {@link PartyInvite} instance
	 */
	default PartyInvite invitePlayer(@NotNull PartyPlayer partyPlayer) {
		return invitePlayer(partyPlayer, null);
	}
	
	/**
	 * Invite the player into the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to invite
	 * @param inviter The {@link PartyPlayer} who is inviting partyPlayer
	 * @return Returns the {@link PartyInvite} instance
	 */
	default PartyInvite invitePlayer(@NotNull PartyPlayer partyPlayer, @Nullable PartyPlayer inviter) {
		return invitePlayer(partyPlayer, inviter, true);
	}
	
	/**
	 * Invite the player into the party
	 *
	 * @param partyPlayer The {@link PartyPlayer} to invite
	 * @param inviter The {@link PartyPlayer} who is inviting partyPlayer
	 * @param sendMessages True if the event should send messages to players
	 * @return Returns the {@link PartyInvite} instance
	 */
	PartyInvite invitePlayer(@NotNull PartyPlayer partyPlayer, @Nullable PartyPlayer inviter, boolean sendMessages);
	
	/**
	 * Get a list of pending ask requests
	 *
	 * @return A set of {@link PartyAskRequest}
	 */
	Set<PartyAskRequest> getAskRequests();
	
	/**
	 * Is the party full?
	 *
	 * @return Returns true if the party is full
	 */
	boolean isFull();
	
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
	void changeLeader(@NotNull PartyPlayer leaderPartyPlayer);
	
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
	 * Get the party tag
	 *
	 * @return Returns party tag
	 */
	@Nullable String getTag();
	
	/**
	 * Set the party tag
	 *
	 * @param tag The tag of the party
	 */
	void setTag(@Nullable String tag);
	
	/**
	 * Get the party description
	 *
	 * @return Returns party description
	 */
	@Nullable String getDescription();
	
	/**
	 * Set the party description
	 *
	 * @param description The description of the party
	 */
	void setDescription(@Nullable String description);
	
	/**
	 * Get the Message Of The Day of the party
	 *
	 * @return Returns the MOTD of the party
	 */
	@Nullable String getMotd();
	
	/**
	 * Set the Message Of The Day of the party
	 *
	 * @param motd The MOTD of the party
	 */
	void setMotd(@Nullable String motd);
	
	/**
	 * Get the set of homes of the party
	 *
	 * @return Returns the {@code Set<PartyHome>} of the party
	 */
	@NotNull Set<PartyHome> getHomes();
	
	/**
	 * Set the set of homes of the party
	 *
	 * @param homes The {@code Set<PartyHome>} of the party
	 */
	void setHomes(@NotNull Set<PartyHome> homes);
	
	/**
	 * @deprecated Use {@code getHomes()} instead
	 * @return The {@code PartyHome} of the party
	 */
	@Deprecated
	default PartyHome getHome() {
		return getHomes().iterator().next();
	}
	
	/**
	 * @deprecated Use {@code setHomes(...)} instead
	 * @param home The new party home
	 */
	@Deprecated
	default void setHome(@Nullable PartyHome home) {
		HashSet<PartyHome> hs = new HashSet<>();
		if (home != null) {
			hs.add(home);
		}
		setHomes(hs);
	}
	
	/**
	 * Get the party color
	 *
	 * @return Returns the {@code Color} of the party
	 */
	@Nullable PartyColor getColor();
	
	/**
	 * Set the party color
	 *
	 * @param color The {@code Color} of the party
	 */
	void setColor(@Nullable PartyColor color);
	
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
	@Nullable String getPassword();
	
	/**
	 * Set the party password
	 *
	 * @param password The password of the party, HASHED
	 */
	void setPassword(@Nullable String password);
	
	/**
	 * Set the party password unhashed, the plugin will hash it
	 *
	 * @param password The password of the party
	 * @return Returns true if the password is valid
	 */
	boolean setPasswordUnhashed(@Nullable String password);
	
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
	 * Give party experience
	 *
	 * @param experience The experience number to give
	 */
	void giveExperience(double experience);
	
	/**
	 * Get the current party level
	 *
	 * @return Returns the calculated level of the party
	 */
	int getLevel();
	
	/**
	 * Get the total experience of the current level
	 *
	 * @return Returns the total experience of the current level
	 */
	double getLevelExperience();
	
	/**
	 * Get the current party level experience. How many experience of the current level
	 *
	 * @return Returns the current level experience of the party
	 */
	double getLevelUpCurrent();
	
	/**
	 * Get the experience required to level up the party
	 *
	 * @return Returns the experience required to level up the party
	 */
	double getLevelUpNecessary();
	
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
