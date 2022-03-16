package com.alessiodp.parties.common.api;

import com.alessiodp.parties.api.interfaces.PartiesOptions;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartiesOptionsHandler implements PartiesOptions {
	@Override
	public boolean isSkriptHookEnabled() {
		return false;
	}
	
	@Override
	public @Nullable String getBungeecordName() {
		return null;
	}
	
	@Override
	public @Nullable String getBungeecordId() {
		return null;
	}
	
	@Override
	public boolean isRedisBungeeEnabled() {
		return false;
	}
	
	@Override
	public @NotNull String getStorageType() {
		return ConfigMain.STORAGE_TYPE_DATABASE;
	}
	
	@Override
	public boolean isAutoCommandEnabled() {
		return ConfigMain.ADDITIONAL_AUTOCMD_ENABLE;
	}
	
	@Override
	public boolean isExperienceEnabled() {
		return ConfigMain.ADDITIONAL_EXP_ENABLE;
	}
	
	@Override
	public @NotNull String getExperienceMode() {
		return ConfigMain.ADDITIONAL_EXP_MODE;
	}
	
	@Override
	public boolean isExperienceEarnFromMobsEnabled() {
		return ConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS;
	}
	
	@Override
	public boolean isFollowEnabled() {
		return ConfigMain.ADDITIONAL_FOLLOW_ENABLE;
	}
	
	@Override
	public boolean isModerationEnabled() {
		return ConfigMain.ADDITIONAL_MODERATION_ENABLE;
	}
	
	@Override
	public boolean isModerationPreventChatMutedEnabled() {
		return ConfigMain.ADDITIONAL_MODERATION_PREVENTCHAT;
	}
	
	@Override
	public boolean isModerationAutoKickBannedEnabled() {
		return ConfigMain.ADDITIONAL_MODERATION_AUTOKICK;
	}
	
	@Override
	public boolean isMuteEnabled() {
		return ConfigMain.ADDITIONAL_MUTE_ENABLE;
	}
	
	@Override
	public boolean isClaimEnabled() {
		return false;
	}
	
	@Override
	public boolean isDynmapEnabled() {
		return false;
	}
	
	@Override
	public String getDynmapLayer() {
		return null;
	}
	
	@Override
	public boolean isVaultEnabled() {
		return false;
	}
	
	@Override
	public boolean isVaultCommandEnabled() {
		return false;
	}
	
	@Override
	public int getPartyMembersLimit() {
		return ConfigParties.GENERAL_MEMBERS_LIMIT;
	}
	
	@Override
	public boolean isOnPartyLeaveChangeLeader() {
		return ConfigParties.GENERAL_MEMBERS_ON_PARTY_LEAVE_CHANGE_LEADER;
	}
	
	@Override
	public boolean isDisbandPartyOnDisable() {
		return ConfigParties.GENERAL_MEMBERS_DISBAND_PARTIES_ON_DISABLE;
	}
	
	@Override
	public boolean isOnPartyLeaveFromServerChangeLeader() {
		return ConfigParties.GENERAL_MEMBERS_ON_LEAVE_SERVER_CHANGE_LEADER;
	}
	
	@Override
	public boolean isOnPartyLeaveFromServerKickFromParty() {
		return ConfigParties.GENERAL_MEMBERS_ON_LEAVE_SERVER_KICK_FROM_PARTY;
	}
	
	@Override
	public boolean isBroadcastTitlesEnabled() {
		return ConfigParties.GENERAL_BROADCAST_TITLES_ENABLE;
	}
	
	@Override
	public @NotNull String getNameAllowedCharacters() {
		return ConfigParties.GENERAL_NAME_ALLOWEDCHARS;
	}
	
	@Override
	public @NotNull String getNameCensorRegex() {
		return ConfigParties.GENERAL_NAME_CENSORREGEX;
	}
	
	@Override
	public int getNameMinimumLength() {
		return ConfigParties.GENERAL_NAME_MINLENGTH;
	}
	
	@Override
	public int getNameMaximumLength() {
		return ConfigParties.GENERAL_NAME_MAXLENGTH;
	}
	
	@Override
	public boolean isAskEnabled() {
		return ConfigParties.ADDITIONAL_ASK_ENABLE;
	}
	
	@Override
	public boolean isColorEnabled() {
		return ConfigParties.ADDITIONAL_COLOR_ENABLE;
	}
	
	@Override
	public boolean isDescriptionEnabled() {
		return ConfigParties.ADDITIONAL_DESC_ENABLE;
	}
	
	@Override
	public boolean isFixedEnabled() {
		return ConfigParties.ADDITIONAL_FIXED_ENABLE;
	}
	
	@Override
	public boolean isFixedDefaultPartyEnabled() {
		return ConfigParties.ADDITIONAL_FIXED_DEFAULT_ENABLE;
	}
	
	@Override
	public @NotNull String getFixedDefaultParty() {
		return ConfigParties.ADDITIONAL_FIXED_DEFAULT_PARTY;
	}
	
	@Override
	public boolean isHomeEnabled() {
		return ConfigParties.ADDITIONAL_HOME_ENABLE;
	}
	
	@Override
	public boolean isJoinEnabled() {
		return ConfigParties.ADDITIONAL_JOIN_ENABLE;
	}
	
	@Override
	public boolean isJoinPasswordEnabled() {
		return ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE;
	}
	
	@Override
	public boolean isKillsEnabled() {
		return ConfigParties.ADDITIONAL_KILLS_ENABLE;
	}
	
	@Override
	public boolean isKillsSaveNeutralMobsEnabled() {
		return ConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL;
	}
	
	@Override
	public boolean isKillsSaveHostileMobsEnabled() {
		return ConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE;
	}
	
	@Override
	public boolean isKillsSavePlayersEnabled() {
		return ConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS;
	}
	
	@Override
	public boolean isListEnabled() {
		return ConfigParties.ADDITIONAL_LIST_ENABLE;
	}
	
	@Override
	public boolean isMotdEnabled() {
		return ConfigParties.ADDITIONAL_MOTD_ENABLE;
	}
	
	@Override
	public boolean isNicknameEnabled() {
		return ConfigParties.ADDITIONAL_NICKNAME_ENABLE;
	}
	
	@Override
	public boolean isTeleportEnabled() {
		return ConfigParties.ADDITIONAL_TELEPORT_ENABLE;
	}
}
