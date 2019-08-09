package com.alessiodp.parties.common.players.spy;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
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
	
	public String toMessage() {
		String format;
		switch (type) {
			case MESSAGE:
				format = ConfigParties.GENERAL_CHAT_FORMAT_SPY_CHAT;
				break;
			case BROADCAST:
				format = ConfigParties.GENERAL_CHAT_FORMAT_SPY_BROADCAST;
				break;
			default:
				format = "%message%";
				break;
		}
		if (party != null)
			format = plugin.getMessageUtils().convertPartyPlaceholders(format, party);
		if (player != null)
			format = plugin.getMessageUtils().convertPlayerPlaceholders(format, player);
		return plugin.getColorUtils().convertColors(format)
				.replace("%message%", plugin.getColorUtils().removeColors(message));
	}
	
	public enum SpyType {
		MESSAGE, BROADCAST
	}
}
