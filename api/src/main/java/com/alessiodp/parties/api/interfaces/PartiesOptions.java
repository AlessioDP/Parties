package com.alessiodp.parties.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PartiesOptions {
	// Config options
	boolean isSkriptHookEnabled();
	@Nullable String getBungeecordName();
	@Nullable String getBungeecordId();
	boolean isRedisBungeeEnabled();
	
	@NotNull String getStorageType();
	
	boolean isAutoCommandEnabled();
	
	boolean isExperienceEnabled();
	@NotNull String getExperienceMode();
	boolean isExperienceEarnFromMobsEnabled();
	
	boolean isFollowEnabled();
	
	boolean isModerationEnabled();
	boolean isModerationPreventChatMutedEnabled();
	boolean isModerationAutoKickBannedEnabled();
	
	boolean isMuteEnabled();
	
	boolean isClaimEnabled();
	
	boolean isDynmapEnabled();
	@Nullable String getDynmapLayer();
	
	boolean isVaultEnabled();
	boolean isVaultCommandEnabled();
	
	// Parties options
	int getPartyMembersLimit();
	boolean isOnPartyLeaveChangeLeader();
	boolean isDisbandPartyOnDisable();
	boolean isOnPartyLeaveFromServerChangeLeader();
	boolean isOnPartyLeaveFromServerKickFromParty();
	boolean isBroadcastTitlesEnabled();
	@NotNull String getNameAllowedCharacters();
	@NotNull String getNameCensorRegex();
	int getNameMinimumLength();
	int getNameMaximumLength();
	
	boolean isAskEnabled();
	boolean isColorEnabled();
	boolean isDescriptionEnabled();
	boolean isFixedEnabled();
	boolean isFixedDefaultPartyEnabled();
	@NotNull String getFixedDefaultParty();
	
	boolean isHomeEnabled();
	
	boolean isJoinEnabled();
	boolean isJoinPasswordEnabled();
	
	boolean isKillsEnabled();
	boolean isKillsSaveNeutralMobsEnabled();
	boolean isKillsSaveHostileMobsEnabled();
	boolean isKillsSavePlayersEnabled();
	
	boolean isListEnabled();
	
	boolean isMotdEnabled();
	
	boolean isNicknameEnabled();
	
	boolean isTeleportEnabled();
}
