package com.alessiodp.parties.common.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.messaging.ADPPacket;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString
public class PartiesPacket extends ADPPacket {
	// Common
	@Getter private String source;
	@Getter private UUID party;
	@Getter private UUID player;
	@Getter private UUID secondaryPlayer;
	
	@Getter private String text;
	@Getter private String secondaryText;
	@Getter private double number;
	@Getter private double secondaryNumber;
	@Getter private boolean bool;
	@Getter private Enum<?> cause;
	@Getter private ConfigData configData;
	
	public static PartiesPacket read(ADPPlugin plugin, byte[] bytes) {
		PartiesPacket ret = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
			ObjectInput in = new ObjectInputStream(bis);
			ret = (PartiesPacket) in.readObject();
		} catch (Exception ex) {
			plugin.getLoggerManager().logError(Constants.DEBUG_LOG_MESSAGING_FAILED_READ, ex);
		}
		return ret;
	}


	
	
	public PartiesPacket setSource(String source) {
		this.source = source;
		return this;
	}
	
	public PartiesPacket setParty(UUID party) {
		this.party = party;
		return this;
	}
	
	public PartiesPacket setPlayer(UUID player) {
		this.player = player;
		return this;
	}
	
	public PartiesPacket setSecondaryPlayer(UUID secondaryPlayer) {
		this.secondaryPlayer = secondaryPlayer;
		return this;
	}
	
	public PartiesPacket setText(String text) {
		this.text = text;
		return this;
	}
	
	public PartiesPacket setSecondaryText(String secondaryText) {
		this.secondaryText = secondaryText;
		return this;
	}
	
	public PartiesPacket setNumber(double number) {
		this.number = number;
		return this;
	}
	
	public PartiesPacket setSecondaryNumber(double secondaryNumber) {
		this.secondaryNumber = secondaryNumber;
		return this;
	}
	
	public PartiesPacket setBool(boolean bool) {
		this.bool = bool;
		return this;
	}
	
	public PartiesPacket setCause(Enum<?> cause) {
		this.cause = cause;
		return this;
	}
	
	public PartiesPacket setConfigData(ConfigData configData) {
		this.configData = configData;
		return this;
	}

	public PartiesPacket.ConfigData makePacketConfigData() {
		return new PartiesPacket.ConfigData(
				ConfigMain.STORAGE_TYPE_DATABASE,
				ConfigMain.ADDITIONAL_EXP_ENABLE,
				ConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS,
				ConfigMain.ADDITIONAL_EXP_MODE,
				ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_START,
				ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP,
				ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_SAFE_CALCULATION,
				ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT,
				ConfigMain.ADDITIONAL_EXP_FIXED_LIST,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_WARNONFIGHT,
				ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_FISH_HOOK,
				ConfigParties.ADDITIONAL_KILLS_ENABLE,
				ConfigParties.ADDITIONAL_KILLS_MOB_NEUTRAL,
				ConfigParties.ADDITIONAL_KILLS_MOB_HOSTILE,
				ConfigParties.ADDITIONAL_KILLS_MOB_PLAYERS
		);
	}
	
	public enum PacketType {
		UPDATE_PARTY, UPDATE_PLAYER, LOAD_PARTY, LOAD_PLAYER, UNLOAD_PARTY, UNLOAD_PLAYER, PLAY_SOUND,
		
		// Party fields sync
		CREATE_PARTY, DELETE_PARTY, RENAME_PARTY, ADD_MEMBER_PARTY, REMOVE_MEMBER_PARTY, CHAT_MESSAGE, BROADCAST_MESSAGE, INVITE_PLAYER,
		
		// Actions fields
		ADD_HOME, HOME_TELEPORT, TELEPORT, EXPERIENCE, LEVEL_UP,
		
		// Config packets
		CONFIGS, REQUEST_CONFIGS, DEBUG_BUNGEECORD,
		
		// Redis packets
		REDIS_MESSAGE, REDIS_TITLE, REDIS_CHAT
	}
	
	@EqualsAndHashCode
	@ToString
	@AllArgsConstructor
	public static class ConfigData implements Serializable {
		@Getter String storageTypeDatabase;
		@Getter boolean additionalExpEnable;
		@Getter boolean additionalExpEarnFromMobs;
		@Getter String additionalExpMode;
		@Getter double additionalExpProgressiveStart;
		@Getter String additionalExpProgressiveLevelExp;
		@Getter boolean additionalExpProgressiveSafeCalculation;
		@Getter boolean additionalExpFixedRepeat;
		@Getter List<Double> additionalExpFixedList;
		@Getter boolean additionalFriendlyFireEnable;
		@Getter String additionalFriendlyFireType;
		@Getter boolean additionalFriendlyFireWarnOnFight;
		@Getter boolean additionalFriendlyFirePreventFishHook;
		@Getter boolean additionalKillsEnable;
		@Getter boolean additionalKillsMobNeutral;
		@Getter boolean additionalKillsMobHostile;
		@Getter boolean additionalKillsMobPlayers;
	}
}
