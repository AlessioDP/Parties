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
	 * @return the id of the party
	 */
	@NotNull UUID getId();
	
	/**
	 * Get the party name
	 *
	 * @return the name of the party
	 */
	@Nullable String getName();
	
	/**
	 * Delete the party
	 */
	void delete();
	
	/**
	 * Rename the party
	 *
	 * @param newName the name to set
	 */
	void rename(@Nullable String newName);
	
	/**
	 * Set the party name
	 *
	 * @param name the name to set
	 * @deprecated use rename(String)
	 */
	@Deprecated
	default void setName(String name) {
		rename(name);
	}
	
	/**
	 * Get a list of party members. It will contains members of every rank, leader too.
	 *
	 * @return the members sub of the party
	 */
	@NotNull Set<UUID> getMembers();
	
	/**
	 * Get a list of online members
	 *
	 * @return an unmodifiable {@code Set<PartyPlayer>}
	 */
	@NotNull default Set<PartyPlayer> getOnlineMembers() {
		return getOnlineMembers(true);
	}
	
	/**
	 * Get a list of online members
	 *
	 * @param bypassVanish bypass player with vanish?
	 * @return an unmodifiable {@code Set<PartyPlayer>}
	 */
	@NotNull Set<PartyPlayer> getOnlineMembers(boolean bypassVanish);
	
	/**
	 * Add the player to the party
	 *
	 * @param partyPlayer the {@link PartyPlayer} to add
	 * @return true if successfully added
	 */
	boolean addMember(@NotNull PartyPlayer partyPlayer);
	
	/**
	 * Remove the player from the party
	 *
	 * @param partyPlayer the {@link PartyPlayer} to remove
	 * @return true if successfully removed
	 */
	boolean removeMember(@NotNull PartyPlayer partyPlayer);
	
	/**
	 * Get a list of pending invite requests
	 *
	 * @return a set of {@link PartyInvite}
	 */
	Set<PartyInvite> getInviteRequests();
	
	/**
	 * Invite the player into the party
	 *
	 * @param partyPlayer the {@link PartyPlayer} to invite
	 * @return the {@link PartyInvite} instance
	 */
	default PartyInvite invitePlayer(@NotNull PartyPlayer partyPlayer) {
		return invitePlayer(partyPlayer, null);
	}
	
	/**
	 * Invite the player into the party
	 *
	 * @param partyPlayer the {@link PartyPlayer} to invite
	 * @param inviter the {@link PartyPlayer} who is inviting partyPlayer
	 * @return the {@link PartyInvite} instance
	 */
	default PartyInvite invitePlayer(@NotNull PartyPlayer partyPlayer, @Nullable PartyPlayer inviter) {
		return invitePlayer(partyPlayer, inviter, true);
	}
	
	/**
	 * Invite the player into the party
	 *
	 * @param partyPlayer the {@link PartyPlayer} to invite
	 * @param inviter the {@link PartyPlayer} who is inviting partyPlayer
	 * @param sendMessages true if the event should send messages to players
	 * @return the {@link PartyInvite} instance
	 */
	PartyInvite invitePlayer(@NotNull PartyPlayer partyPlayer, @Nullable PartyPlayer inviter, boolean sendMessages);
	
	/**
	 * Get a list of pending ask requests
	 *
	 * @return a set of {@link PartyAskRequest}
	 */
	Set<PartyAskRequest> getAskRequests();
	
	/**
	 * Is the party full?
	 *
	 * @return true if the party is full
	 */
	boolean isFull();
	
	/**
	 * Get the party leader
	 *
	 * @return the {@link UUID} of the party leader, can be magic if the party is fixed
	 */
	@Nullable UUID getLeader();
	
	/**
	 * Change the party leader
	 *
	 * @param leaderPartyPlayer the {@link PartyPlayer} to promote as leader
	 */
	void changeLeader(@NotNull PartyPlayer leaderPartyPlayer);
	
	/**
	 * Is the party fixed?
	 *
	 * @return true if the party is fixed
	 */
	boolean isFixed();
	
	/**
	 * Toggle a fixed party
	 *
	 * @param fixed {@code true} to be fixed
	 * @param newLeader new leader to set, null if setting Party as fixed
	 */
	void setFixed(boolean fixed, @Nullable PartyPlayer newLeader);
	
	/**
	 * Get the party tag
	 *
	 * @return the party tag
	 */
	@Nullable String getTag();
	
	/**
	 * Set the party tag
	 *
	 * @param tag the tag of the party
	 */
	void setTag(@Nullable String tag);
	
	/**
	 * Get the party description
	 *
	 * @return the party description
	 */
	@Nullable String getDescription();
	
	/**
	 * Set the party description
	 *
	 * @param description the description of the party
	 */
	void setDescription(@Nullable String description);
	
	/**
	 * Get the Message Of The Day of the party
	 *
	 * @return the MOTD of the party
	 */
	@Nullable String getMotd();
	
	/**
	 * Set the Message Of The Day of the party
	 *
	 * @param motd the MOTD of the party
	 */
	void setMotd(@Nullable String motd);
	
	/**
	 * Get the set of homes of the party
	 *
	 * @return the {@code Set<PartyHome>} of the party
	 */
	@NotNull Set<PartyHome> getHomes();
	
	/**
	 * Set the set of homes of the party
	 *
	 * @param homes the {@code Set<PartyHome>} of the party
	 */
	void setHomes(@NotNull Set<PartyHome> homes);
	
	/**
	 * @deprecated use {@code getHomes()} instead
	 * @return the {@code PartyHome} of the party
	 */
	@Deprecated
	default PartyHome getHome() {
		return getHomes().iterator().next();
	}
	
	/**
	 * @deprecated use {@code setHomes(...)} instead
	 * @param home the new party home
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
	 * @return the {@code Color} of the party
	 */
	@Nullable PartyColor getColor();
	
	/**
	 * Set the party color
	 *
	 * @param color the {@code Color} of the party
	 */
	void setColor(@Nullable PartyColor color);
	
	/**
	 * Get the kills number of the party
	 *
	 * @return the number of kills of the party
	 */
	int getKills();
	
	/**
	 * Set the number of kills of the party
	 *
	 * @param kills the number of kills of the party
	 */
	void setKills(int kills);
	
	/**
	 * Is the party open to players?
	 *
	 * @return true if its open
	 */
	boolean isOpen();
	
	/**
	 * Set the party as open
	 *
	 * @param open true for open
	 */
	void setOpen(boolean open);
	
	/**
	 * Get the party password
	 *
	 * @return the password of the party, HASHED
	 */
	@Nullable String getPassword();
	
	/**
	 * Set the party password
	 *
	 * @param password the password of the party, HASHED
	 */
	void setPassword(@Nullable String password);
	
	/**
	 * Set the party password unhashed, the plugin will hash it
	 *
	 * @param password the password of the party
	 * @return true if the password is valid
	 */
	boolean setPasswordUnhashed(@Nullable String password);
	
	/**
	 * Get the party friendly fire protection
	 *
	 * @return true if the party is protected
	 */
	boolean getProtection();
	
	/**
	 * Set the party friendly fire protection
	 *
	 * @param protection true if you want protect the party
	 */
	void setProtection(boolean protection);
	
	/**
	 * Check if the party is protected from friendly fire using both
	 * command and global protection.
	 * Use this if you just want to check for FF.
	 *
	 * @return true if pvp between players is protected
	 */
	boolean isFriendlyFireProtected();
	
	/**
	 * Get the party experience
	 *
	 * @return the total experience of the party
	 */
	double getExperience();
	
	/**
	 * Set the party experience
	 *
	 * @param experience the experience number to set
	 */
	void setExperience(double experience);
	
	/**
	 * Give party experience
	 *
	 * @param experience the experience number to give
	 */
	default void giveExperience(double experience) {
		giveExperience(experience, true);
	}
	
	/**
	 * Give party experience. Choose to send the gain message or not.
	 *
	 * @param experience the experience number to give
	 * @param gainMessage should the gain message be sent or not
	 */
	void giveExperience(double experience, boolean gainMessage);
	
	/**
	 * Get the current party level
	 *
	 * @return the calculated level of the party
	 */
	int getLevel();
	
	/**
	 * Get the total experience of the current level
	 *
	 * @return the total experience of the current level
	 */
	double getLevelExperience();
	
	/**
	 * Get the current party level experience. How many experience of the current level
	 *
	 * @return the current level experience of the party
	 */
	double getLevelUpCurrent();
	
	/**
	 * Get the experience required to level up the party
	 *
	 * @return the experience required to level up the party
	 */
	double getLevelUpNecessary();
	
	/**
	 * Set the party follow option
	 *
	 * @param follow true if you want enable follow option
	 */
	void setFollowEnabled(boolean follow);
	
	/**
	 * Check if the party have the follow option enabled
	 *
	 * @return true if follow option is enabled
	 */
	boolean isFollowEnabled();
	
	/**
	 * Send a broadcast message to the party.
	 * It requires a player to send the message because Parties gets the placeholder info from the player.
	 * The player can be null if its a general broadcast.
	 *
	 * @param message the message to broadcast
	 * @param player  the {@link PartyPlayer} who sent the message
	 */
	void broadcastMessage(@NotNull String message, @Nullable PartyPlayer player);
}
