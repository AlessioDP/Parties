package com.alessiodp.parties.common.players.objects;

import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpyMessage {
	@NonNull private final PartiesPlugin plugin;
	@Getter private SpyType type;
	@Getter private String message;
	
	@Getter private PartyImpl party;
	@Getter private PartyPlayerImpl player;
	
	public SpyMessage setType(SpyType type) {
		this.type = type;
		return this;
	}
	
	public SpyMessage setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public SpyMessage setParty(PartyImpl party) {
		this.party = party;
		return this;
	}
	
	public SpyMessage setPlayer(PartyPlayerImpl player) {
		this.player = player;
		return this;
	}
	
	public boolean isEnabled() {
		switch (type) {
			case MESSAGE:
				return !Messages.PARTIES_FORMATS_SPY_PARTY_CHAT.isEmpty();
			case BROADCAST:
				return !Messages.PARTIES_FORMATS_SPY_BROADCAST.isEmpty();
			default:
				return true;
		}
	}
	
	public String toMessage() {
		String format;
		switch (type) {
			case MESSAGE:
				format = Messages.PARTIES_FORMATS_SPY_PARTY_CHAT;
				break;
			case BROADCAST:
				format = Messages.PARTIES_FORMATS_SPY_BROADCAST;
				break;
			default:
				format = "%message%";
				break;
		}
		format = plugin.getMessageUtils().convertPlaceholders(format, player, party);
		return Color.translateAlternateColorCodes(format)
				.replace("%message%", Color.translateAndStripColor(message));
	}
	
	public enum SpyType {
		MESSAGE, BROADCAST
	}
}
