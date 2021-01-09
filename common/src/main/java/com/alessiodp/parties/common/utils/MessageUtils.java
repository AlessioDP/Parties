package com.alessiodp.parties.common.utils;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class MessageUtils {
	private final PartiesPlugin plugin;
	private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([%][^%]+[%])");
	
	
	public String convertPlaceholders(String message, PartyPlayerImpl player, PartyImpl party) {
		return convertPlaceholders(message, player, party, "");
	}
	
	public String convertPlaceholders(String message, PartyPlayerImpl player, PartyImpl party, String emptyPlaceholder) {
		String ret = message;
		String replacement;
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
		while (matcher.find()) {
			String identifier = matcher.group(1);
			// Match basic placeholders
			switch (CommonUtils.toLowerCase(identifier)) {
				case "%player%":
				case "%user%":
					if (player != null) {
						replacement = player.getName();
						ret = ret.replace(identifier, replacement);
					}
					break;
				default: // Nothing to do
			}
			
			PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder(stripPlaceholder(identifier));
			if (placeholder != null) {
				replacement = placeholder.formatPlaceholder(player, party, stripPlaceholder(identifier), emptyPlaceholder);
				if (replacement != null)
					ret = ret.replace(identifier, replacement);
			}
		}
		return ret;
	}
	
	public void sendMessage(User receiver, String message, PartyPlayerImpl victim, PartyImpl party) {
		if (receiver != null && message != null && !message.isEmpty()) {
			String formattedMessage = plugin.getMessageUtils().convertPlaceholders(message, victim, party);
			
			if (receiver.isPlayer())
				formattedMessage = Color.translateAlternateColorCodes(formattedMessage);
			else
				formattedMessage = Color.translateAndStripColor(plugin.getJsonHandler().removeJson(formattedMessage));
			
			receiver.sendMessage(formattedMessage, false);
		}
	}
	
	public String formatEnabledDisabled(boolean value) {
		return value ? Messages.PARTIES_OPTIONS_ENABLED : Messages.PARTIES_OPTIONS_DISABLED;
	}
	
	public String formatOnOff(boolean value) {
		return value ? Messages.PARTIES_OPTIONS_TOGGLED_ON : Messages.PARTIES_OPTIONS_TOGGLED_OFF;
	}
	
	public String formatYesNo(boolean value) {
		return value ? Messages.PARTIES_OPTIONS_WORD_YES : Messages.PARTIES_OPTIONS_WORD_NO;
	}
	
	public String formatText(String text) {
		return text == null ? Messages.PARTIES_OPTIONS_NONE : (text.isEmpty() ? Messages.PARTIES_OPTIONS_EMPTY : text);
	}
	
	public String stripPlaceholder(String placeholder) {
		return placeholder.substring(1, placeholder.length() - 1);
	}
}
